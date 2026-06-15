/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 3.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.spi;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.PersistenceException;
import java.util.Map;

/**
 * Interface implemented by the persistence provider.
 * <p>
 * In Java SE, the {@link Persistence} class calls:
 * <ul>
 * <li>{@link #createEntityManagerFactory(String, Map)} when the
 *     application program calls
 *     {@link Persistence#createEntityManagerFactory(String, Map)},
 * <li>{@link #createEntityManagerFactory(PersistenceConfiguration)}
 *    when the application program calls
 *    {@link PersistenceConfiguration#createEntityManagerFactory},
 * <li>{@link #generateSchema(String, Map)} when the application,
 *     program calls {@link Persistence#generateSchema(String, Map)},
 * <li>{@link #generateSchema(PersistenceConfiguration)} when the
 *     application program calls
 *     {@link PersistenceConfiguration#exportSchema()}, and
 * <li>{@link #getProviderUtil()} to obtain a {@link ProviderUtil}.
 * </ul>
 * <p>In a Jakarta EE environment, the container calls:
 * <ul>
 * <li>{@link #createContainerEntityManagerFactory(PersistenceUnitInfo, Map)}
 *     to create an {@link EntityManagerFactory} for a container-managed
 *     persistence unit.
 * <li>{@link #generateSchema(PersistenceUnitInfo, Map)} to delegate
 *     DDL schema management to the persistence provider, and
 *     {@link #getClassTransformer(PersistenceUnitInfo, Map)} to
 *     obtain a provider-supplied {@link ClassTransformer}.
 * </ul>
 *
 * @apiNote This is an SPI interface forming part of the Jakarta EE
 * container / persistence provider contract. It is not intended for
 * direct use by application programs.
 *
 * @since 1.0
 */
public interface PersistenceProvider {

    /**
     * Called by {@link Persistence} class to create a new
     * {@link EntityManagerFactory} for the persistence unit with the
     * given name, optionally specifying configuration properties which
     * override the settings specified in the {@code persistence.xml}
     * file for the given persistence unit.
     *
     * @param unitName the name of the persistence unit
     * @param properties property settings for use by the persistence
     *                   provider. These properties may be used to
     *                   specify values for properties not specified
     *                   in the {@code persistence.xml} file for the
     *                   given persistence unit or to override the
     *                   values specified by the corresponding elements
     *                   of the {@code persistence.xml} file. The map
     *                   may be null when no properties are specified.
     * @return a newly created {@link EntityManagerFactory} for the
     *         named persistence unit, or null if the persistence unit
     *         configuration specified a different persistence provider
     *
     * @see Persistence#createEntityManagerFactory(String, Map)
     */
    @Nullable
    EntityManagerFactory createEntityManagerFactory(@Nonnull String unitName,
                                                    @Nullable Map<?, ?> properties);

    /**
     * Called by {@link Persistence} class to create a new
     * {@link EntityManagerFactory} for a persistence unit with the
     * given programmatic configuration.
     *
     * @param configuration the configuration of the persistence unit
     * @return a newly created {@link EntityManagerFactory} for the
     *         persistence unit with the given configuration, or null
     *         if the persistence unit configuration specified a
     *         different persistence provider
     * @throws IllegalStateException if any required configuration
     *                               is missing
     *
     * @see Persistence#createEntityManagerFactory(PersistenceConfiguration)
     *
     * @since 3.2
     */
    @Nullable
    EntityManagerFactory createEntityManagerFactory(@Nonnull PersistenceConfiguration configuration);

    /**
     * Called by the Jakarta EE container to create a new
     * {@link EntityManagerFactory} for the persistence unit with the
     * given {@linkplain PersistenceUnitInfo metadata}, optionally
     * specifying configuration properties which override the settings
     * specified in the given {@link PersistenceUnitInfo}.
     * <ul>
     * <li>If a Jakarta Validation provider is present in the classpath,
     * the container must pass the {@code ValidatorFactory} instance
     * as an entry in the given map with the key
     * {@link jakarta.persistence.Persistence.ValidationProperties#VALIDATION_FACTORY}.
     * <li>If the containing archive is a bean archive, the container
     * must pass the {@code BeanManager} instance as an entry in the
     * given map with the key
     * {@link jakarta.persistence.Persistence.BeanManagementProperties#BEAN_MANAGER}.
     * </ul>
     *
     * @param info metadata describing the persistence unit
     * @param properties integration-level property settings for use by
     *                   the persistence provider. The given properties
     *                   may include properties to control schema
     *                   generation. The map may be null when no
     *                   properties are specified.
     * @return a newly created {@link EntityManagerFactory} for the
     *         persistence unit described by the given metadata
     * @throws IllegalArgumentException if the given info specifies an incompatible
     *         {@linkplain PersistenceUnitInfo#getPersistenceProviderClassName
     *         provider class}
     */
    @Nonnull
    EntityManagerFactory createContainerEntityManagerFactory(@Nonnull PersistenceUnitInfo info,
                                                             @Nullable Map<?, ?> properties);

    /**
     * Create database schemas and/or tables and/or create DDL scripts,
     * as determined by the supplied properties.
     * <p>
     * Called by the container when schema generation is to occur as a
     * separate phase from the creation of the entity manager factory.
     *
     * @param info metadata describing the persistence unit
     * @param properties property settings for schema generation;
     *                   these may include provider-specific
     *                   properties
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided or if schema
     *         generation otherwise fails
     *
     * @since 2.1
     */
    void generateSchema(@Nonnull PersistenceUnitInfo info,
                        @Nullable Map<?, ?> properties);

    /**
     * Create database schemas and/or tables and/or create DDL scripts,
     * as determined by the supplied properties.
     * <p>
     * Called by the {@link Persistence} class when schema generation
     * is to occur as a separate phase from creation of the entity
     * manager factory.
     *
     * @param persistenceUnitName the name of the persistence unit
     * @param properties property settings for schema generation;
     *                   these may include provider-specific
     *                   properties. The values of these properties
     *                   override any values configured elsewhere.
     * @return true if the schema was generated, otherwise false
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided or if schema
     *         generation otherwise fails
     *
     * @since 2.1
     */
    boolean generateSchema(@Nonnull String persistenceUnitName,
                           @Nullable Map<?, ?> properties);

    /**
     * Create database schemas and/or tables and/or create DDL scripts,
     * as determined by the supplied properties.
     * <p>
     * Called by the {@link Persistence} class when schema generation
     * is to occur as a separate phase from creation of the entity
     * manager factory.
     *
     * @param configuration the configuration of the persistence unit
     * @return true if the schema was generated, otherwise false
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided or if schema
     *         generation otherwise fails
     *
     * @since 4.0
     */
    boolean generateSchema(@Nonnull PersistenceConfiguration configuration);

    /**
     * Return the utility interface implemented by the persistence
     * provider.
     * @return an instance of {@link ProviderUtil}
     *
     * @since 2.0
     */
    @Nonnull
    ProviderUtil getProviderUtil();

    /**
     * Obtain a provider-supplied class transformer that is called
     * for every new class definition or class redefinition performed
     * by the class loader returned by
     * {@link PersistenceUnitInfo#getClassLoader}. The transformer
     * has no effect on the class loader returned by
     * {@link PersistenceUnitInfo#getNewTempClassLoader}.
     * <p>
     * A class is transformed exactly once within a given classloading
     * scope, regardless of how many persistence units it belongs to.
     * <p>The given instance of {@link PersistenceUnitInfo} may
     * return {@code null} when any of the accessor methods
     * {@link PersistenceUnitInfo#getClassLoader()},
     * {@link PersistenceUnitInfo#getJtaDataSource()}, or
     * {@link PersistenceUnitInfo#getNonJtaDataSource()} is called
     * by an implementation of this method.
     * <p>
     * If the container calls this method before invoking
     * {@link #createContainerEntityManagerFactory} to create the
     * {@link EntityManagerFactory}, then the transformer returned
     * by this method is used instead of any transformer registered
     * via {@link PersistenceUnitInfo#addTransformer}. The container
     * is not required to call this method.
     *
     * @return a provider-supplied transformer that is later invoked
     *         by the container when a managed class belonging to the
     *         persistence unit is defined or redefined
     * @param info metadata describing the persistence unit
     * @param properties integration-level property settings for use
     *                   by the persistence provider, which will not
     *                   usually contain a {@code ValidatorFactory}
     *                   or {@code BeanManager}.
     * @since 4.0
     */
    @Nonnull
    ClassTransformer getClassTransformer(@Nonnull PersistenceUnitInfo info,
                                         @Nullable Map<?, ?> properties);
}

