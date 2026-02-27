/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Lukas Jungmann  - 2.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.spi;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;


/**
 * Holds the global {@link PersistenceProviderResolver} instance.
 * If no {@code PersistenceProviderResolver} is set by the environment,
 * the default {@code PersistenceProviderResolver} is used.
 *
 * <p>Enable {@code "jakarta.persistence.spi"} logger to show diagnostic
 * information.
 *
 * <p>Implementations must be thread-safe.
 *
 * @since 2.0
 */
public class PersistenceProviderResolverHolder {

    private static PersistenceProviderResolver singleton = new DefaultPersistenceProviderResolver();

    /**
     * Returns the current persistence provider resolver.
     *
     * @return the current persistence provider resolver
     */
    public static PersistenceProviderResolver getPersistenceProviderResolver() {
        return singleton;
    }

    /**
     * Defines the persistence provider resolver used.
     *
     * @param resolver persistence provider resolver to be used.
     */
    public static void setPersistenceProviderResolver(PersistenceProviderResolver resolver) {
        singleton = resolver == null ? new DefaultPersistenceProviderResolver() : resolver;
    }

    /**
     * Default provider resolver class to use when none is explicitly set.
     *
     * <p>Uses service loading mechanism as described in the Jakarta Persistence
     * specification. A {@code ServiceLoader.load()} call is made with the current
     * context classloader to find the service provider files on the classpath.
     */
    private static class DefaultPersistenceProviderResolver implements PersistenceProviderResolver {

        /**
         * Cached list of available providers cached by CacheKey to ensure
         * there is not potential for provider visibility issues.
         */
        private final Map<CacheKey, PersistenceProviderReference> providers = new HashMap<>();

        /**
         * Queue for reference objects referring to class loaders or persistence providers.
         */
        private static final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();

        public List<PersistenceProvider> getPersistenceProviders() {
            // Before we do the real loading work, see whether we need to
            // do some cleanup: If references to class loaders or
            // persistence providers have been nulled out, remove all related
            // information from the cache.
            processQueue();

            final var loader = Thread.currentThread().getContextClassLoader();
            final var cacheKey = new CacheKey(loader);

            final var providerRef = providers.get(cacheKey);
            if (providerRef != null) {
                final var loadedProviders = providerRef.get();
                if (loadedProviders != null) {
                    return loadedProviders;
                }
            }

            final var loadedProviders = loadPersistenceProviders(loader);
            final var newProviderRef = new PersistenceProviderReference(loadedProviders, referenceQueue, cacheKey);
            providers.put(cacheKey, newProviderRef);
            return loadedProviders;
        }

        private ArrayList<PersistenceProvider> loadPersistenceProviders(ClassLoader loader) {
            final var providers = new ArrayList<PersistenceProvider>();
            try {
                ServiceLoader.load(PersistenceProvider.class, loader).iterator()
                        .forEachRemaining( ip -> {
                            try {
                                providers.add(ip);
                            }
                            catch (ServiceConfigurationError sce) {
                                log(Level.TRACE, sce.toString());
                            }
                        } );
            }
            catch (ServiceConfigurationError sce) {
                log(Level.TRACE, sce.toString());
            }

            // If none are found we'll log the provider names for diagnostic
            // purposes.
            if (providers.isEmpty()) {
                log(Level.WARNING, "No valid providers found.");
            }

            return providers;
        }

        /**
         * Remove garbage collected cache keys and providers.
         */
        private void processQueue() {
            CacheKeyReference ref;
            while ((ref = (CacheKeyReference) referenceQueue.poll()) != null) {
                providers.remove(ref.getCacheKey());
            }
        }

        private static final String LOGGER_SUBSYSTEM = "jakarta.persistence.spi";

        private Logger logger;

        private void log(Level level, String message) {
            if (logger == null) {
                logger = System.getLogger(LOGGER_SUBSYSTEM);
            }
            logger.log(level, LOGGER_SUBSYSTEM + "::" + message);
        }

        /**
         * Clear all cached providers
         */
        public void clearCachedProviders() {
            providers.clear();
        }


        /**
         * The common interface to get a {@link CacheKey} implemented by
         * {@link LoaderReference} and {@link PersistenceProviderReference}.
         */
        private sealed interface CacheKeyReference
                permits LoaderReference, PersistenceProviderReference {
            CacheKey getCacheKey();
        }

        /**
         * Key used for cached persistence providers. The key checks
         * the class loader to determine if the persistence providers
         * is a match to the requested one. The loader may be null.
         */
        private final class CacheKey {

            /* Weak Reference to ClassLoader */
            private final LoaderReference loaderRef;

            /* Cached Hashcode */
            private int hashCodeCache;

            private CacheKey(ClassLoader loader) {
                loaderRef = loader == null ? null : new LoaderReference(loader, referenceQueue, this);
                calculateHashCode();
            }

            private ClassLoader getLoader() {
                return loaderRef != null ? loaderRef.get() : null;
            }

            @Override
            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                else if (!(other instanceof CacheKey otherEntry)) {
                    return false;
                }
                else {
                    try {
                        // quick check to see if they are not equal
                        if (hashCodeCache != otherEntry.hashCodeCache) {
                            return false;
                        }
                        // are refs (both non-null) or (both null)?
                        else if (loaderRef == null) {
                            return otherEntry.loaderRef == null;
                        }
                        else {
                            var loader = loaderRef.get();
                            return otherEntry.loaderRef != null
                                // with a null reference we can no longer find
                                // out which class loader was referenced; so
                                // treat it as unequal
                                && loader != null
                                && loader == otherEntry.loaderRef.get();
                        }
                    }
                    catch (NullPointerException | ClassCastException e) {
                        return false;
                    }
                }
            }

            @Override
            public int hashCode() {
                return hashCodeCache;
            }

            private void calculateHashCode() {
                var loader = getLoader();
                if (loader != null) {
                    hashCodeCache = loader.hashCode();
                }
            }

            @Override
            public String toString() {
                return "CacheKey[" + getLoader() + ")]";
            }
        }

        /**
         * References to class loaders are weak references, so that they can be garbage
         * collected when nobody else is using them. {@link DefaultPersistenceProviderResolver}
         * has no reason to keep class loaders alive.
         */
        private final class LoaderReference
                extends WeakReference<ClassLoader>
                implements CacheKeyReference {
            private final CacheKey cacheKey;

            private LoaderReference(
                    ClassLoader referent,
                    ReferenceQueue<? super ClassLoader> queue,
                    CacheKey key) {
                super(referent, queue);
                cacheKey = key;
            }

            @Override
            public CacheKey getCacheKey() {
                return cacheKey;
            }
        }

        /**
         * References to persistence provider are soft references so that they can be garbage
         * collected when they have no hard references.
         */
        private final class PersistenceProviderReference
                extends SoftReference<List<PersistenceProvider>>
                implements CacheKeyReference {
            private final CacheKey cacheKey;

            private PersistenceProviderReference(
                    List<PersistenceProvider> referent,
                    ReferenceQueue<? super List<PersistenceProvider>> queue,
                    CacheKey key) {
                super(referent, queue);
                cacheKey = key;
            }

            @Override
            public CacheKey getCacheKey() {
                return cacheKey;
            }
        }
    }
}
