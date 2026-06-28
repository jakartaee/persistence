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
//     Gavin King      - 4.0
//     Lukas Jungmann  - 2.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.spi;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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

import static java.util.List.copyOf;


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

    private static volatile PersistenceProviderResolver singleton = new DefaultPersistenceProviderResolver();

    /**
     * Returns the current persistence provider resolver.
     *
     * @return the current persistence provider resolver
     */
    @Nonnull
    public static PersistenceProviderResolver getPersistenceProviderResolver() {
        return singleton;
    }

    /**
     * Defines the persistence provider resolver used.
     *
     * @param resolver persistence provider resolver to be used,
     *                 or {@code null} to use the default resolver.
     */
    public static void setPersistenceProviderResolver(@Nullable PersistenceProviderResolver resolver) {
        singleton = resolver == null ? new DefaultPersistenceProviderResolver() : resolver;
    }

    /**
     * Default provider resolver class to use when none is explicitly set.
     * <p>
     * Uses the service loading mechanism as described in the Jakarta Persistence
     * specification. A {@code ServiceLoader.load()} call is made with the current
     * context classloader to find the service provider files on the classpath.
     */
    private static class DefaultPersistenceProviderResolver implements PersistenceProviderResolver {

        /**
         * Cached lists of available providers keyed by {@linkplain LoaderKey class loader}
         * to avoid provider visibility issues.
         */
        private final Map<LoaderKey, SoftReference<List<PersistenceProvider>>> providers = new HashMap<>();

        @Nonnull
        public synchronized List<PersistenceProvider> getPersistenceProviders() {
            removeClearedReferences();

            final var loader = Thread.currentThread().getContextClassLoader();
            final var loaderKey = new LoaderKey(loader);

            final var loadedProvidersReference = providers.get(loaderKey);
            if (loadedProvidersReference != null) {
                final var loadedProviders = loadedProvidersReference.get();
                if (loadedProviders != null) {
                    return new ArrayList<>(loadedProviders);
                }
            }

            final var loadedProviders = loadPersistenceProviders(loader);
            providers.put(loaderKey, new SoftReference<>(copyOf(loadedProviders)));
            return loadedProviders;
        }

        @Nonnull
        private List<PersistenceProvider> loadPersistenceProviders(@Nonnull ClassLoader loader) {
            final var providers = new ArrayList<PersistenceProvider>();
            try {
                for (var provider : ServiceLoader.load(PersistenceProvider.class, loader)) {
                    try {
                        providers.add(provider);
                    }
                    catch (ServiceConfigurationError sce) {
                        log(Level.TRACE, sce.toString());
                    }
                }
            }
            catch (ServiceConfigurationError sce) {
                log(Level.TRACE, sce.toString());
            }

            if (providers.isEmpty()) {
                log(Level.WARNING, "No valid providers found.");
            }

            return providers;
        }

        /**
         * Remove entries whose class loader or provider list was garbage collected.
         */
        private void removeClearedReferences() {
            providers.entrySet()
                    .removeIf(entry -> entry.getKey().isCleared()
                                    || entry.getValue().get() == null);
        }

        private static final String LOGGER_SUBSYSTEM = "jakarta.persistence.spi";

        private Logger logger;

        private void log(@Nonnull Level level, @Nonnull String message) {
            if (logger == null) {
                logger = System.getLogger(LOGGER_SUBSYSTEM);
            }
            logger.log(level, LOGGER_SUBSYSTEM + "::" + message);
        }

        /**
         * Clear all cached providers
         */
        public synchronized void clearCachedProviders() {
            providers.clear();
        }

        /**
         * A key used for caching persistence provider lists by {@link ClassLoader}.
         * The key holds a weak reference to the class loader so that it does not
         * prevent the class loader from being garbage collected.
         */
        private static final class LoaderKey {

            private final WeakReference<ClassLoader> loaderRef;
            private final int hashCodeCache;

            private LoaderKey(@Nullable ClassLoader loader) {
                if (loader == null) {
                    // indicates the system class loader
                    loaderRef = null;
                    hashCodeCache = 0;
                }
                else {
                    loaderRef = new WeakReference<>(loader);
                    hashCodeCache = System.identityHashCode(loader);
                }
            }

            private boolean isCleared() {
                return loaderRef != null
                    && loaderRef.get() == null;
            }

            @Override
            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                else if (!(other instanceof LoaderKey otherEntry)) {
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

            @Override
            @Nonnull
            public String toString() {
                return "CacheKey[" + hashCodeCache + "]";
            }
        }
    }
}
