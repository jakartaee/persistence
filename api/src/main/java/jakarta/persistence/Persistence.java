/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates and others. All rights reserved.
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
//     Lukas Jungmann  - 3.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.util.List;
import java.util.Map;
import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceProviderResolver;
import jakarta.persistence.spi.PersistenceProviderResolverHolder;
import jakarta.persistence.spi.LoadState;

/**
 * Bootstrap class used to obtain an {@link EntityManagerFactory}
 * in Java SE environments. It may also be used to cause schema
 * generation to occur.
 * 
 * <p>The {@code Persistence} class is available in a Jakarta EE
 * container environment as well; however, support for the Java SE
 * bootstrapping APIs is not required in container environments.
 * 
 * <p>The {@code Persistence} class is used to obtain a
 * {@link PersistenceUtil PersistenceUtil} instance in both
 * Jakarta EE and Java SE environments.
 *
 * @since 1.0
 */
public final class Persistence {

    private Persistence() {
    }

    /**
     * Create and return an {@link EntityManagerFactory} for the named
     * persistence unit.
     * 
     * @param persistenceUnitName the name of the persistence unit
     * @return the factory that creates {@link EntityManager}s configured
     *         according to the specified persistence unit
     */
    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, null);
    }

    /**
     * Create and return an {@link EntityManagerFactory} for the named
     * persistence unit, using the given properties.
     * 
     * @param persistenceUnitName the name of the persistence unit
     * @param properties additional properties to use when creating the
     *                   factory. These properties may include properties
     *                   to control schema generation. The values of these
     *                   properties override any values that may have been
     *                   configured elsewhere.
     * @return the factory that creates {@link EntityManager}s configured
     *        according to the specified persistence unit
     */
    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map<?,?> properties) {

        EntityManagerFactory emf = null;
        PersistenceProviderResolver resolver = PersistenceProviderResolverHolder.getPersistenceProviderResolver();

        List<PersistenceProvider> providers = resolver.getPersistenceProviders();

        for (PersistenceProvider provider : providers) {
            emf = provider.createEntityManagerFactory(persistenceUnitName, properties);
            if (emf != null) {
                break;
            }
        }
        if (emf == null) {
            throw new PersistenceException("No Persistence provider for EntityManager named " + persistenceUnitName);
        }
        return emf;
    }

    /**
     * Create and return an {@link EntityManagerFactory} for the named
     * persistence unit, using the given properties.
     *
     * @param configuration configuration of the persistence unit
     * @return the factory that creates {@link EntityManager}s configured
     *         according to the specified persistence unit
     *
     * @since 3.2
     */
    public static EntityManagerFactory createEntityManagerFactory(PersistenceConfiguration configuration) {

        EntityManagerFactory emf = null;
        PersistenceProviderResolver resolver = PersistenceProviderResolverHolder.getPersistenceProviderResolver();

        List<PersistenceProvider> providers = resolver.getPersistenceProviders();

        for (PersistenceProvider provider : providers) {
            emf = provider.createEntityManagerFactory(configuration);
            if (emf != null) {
                break;
            }
        }
        if (emf == null) {
            throw new PersistenceException("No Persistence provider for EntityManager named " + configuration.name());
        }
        return emf;
    }


    /**
     * Create database schemas and/or tables and/or create DDL scripts
     * as determined by the supplied properties.
     * <p>
     * Called when schema generation is to occur as a separate phase
     * from creation of the entity manager factory.
     *
     * @param persistenceUnitName the name of the persistence unit
     * @param map properties for schema generation; these may also
     *            contain provider-specific properties. The values
     *            of these properties override any values that may
     *            have been configured elsewhere.
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided or if schema
     *         generation otherwise fails.
     *
     * @since 2.1
     */
    public static void generateSchema(String persistenceUnitName, Map<?,?> map) {
        PersistenceProviderResolver resolver = PersistenceProviderResolverHolder.getPersistenceProviderResolver();
        List<PersistenceProvider> providers = resolver.getPersistenceProviders();
        
        for (PersistenceProvider provider : providers) {
            if (provider.generateSchema(persistenceUnitName, map)) {
                return;
            }
        }
        
        throw new PersistenceException("No Persistence provider to generate schema named " + persistenceUnitName);
    }

    /**
     * Create database schemas and/or tables and/or create DDL scripts
     * as determined by the supplied properties.
     * <p>
     * Called when schema generation is to occur as a separate phase
     * from creation of the entity manager factory.
     *
     * @param configuration configuration of the persistence unit
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided or if schema
     *         generation otherwise fails.
     *
     * @since 4.0
     */
    public static void generateSchema(PersistenceConfiguration configuration) {
        PersistenceProviderResolver resolver = PersistenceProviderResolverHolder.getPersistenceProviderResolver();
        List<PersistenceProvider> providers = resolver.getPersistenceProviders();

        for (PersistenceProvider provider : providers) {
            if (provider.generateSchema(configuration)) {
                return;
            }
        }

        throw new PersistenceException("No Persistence provider to generate schema named " + configuration.name());
    }

    /**
     * Return the {@link PersistenceUtil} instance
     * @return {@link PersistenceUtil} instance
     * @since 2.0
     */
    public static PersistenceUtil getPersistenceUtil() {
       return new PersistenceUtilImpl();
    }

    
    /**
     * Implementation of the {@link PersistenceUtil} interface
     * @since 2.0
     */
    private static class PersistenceUtilImpl implements PersistenceUtil {
        public boolean isLoaded(Object entity, String attributeName) {
            PersistenceProviderResolver resolver = PersistenceProviderResolverHolder.getPersistenceProviderResolver();

            List<PersistenceProvider> providers = resolver.getPersistenceProviders();

            for (PersistenceProvider provider : providers) {
                LoadState loadstate = provider.getProviderUtil().isLoadedWithoutReference(entity, attributeName);
                if(loadstate == LoadState.LOADED) {
                    return true;
                } else if (loadstate == LoadState.NOT_LOADED) {
                    return false;
                } // else continue
            }

            //None of the providers could determine the load state try isLoadedWithReference
            for (PersistenceProvider provider : providers) {
                LoadState loadstate = provider.getProviderUtil().isLoadedWithReference(entity, attributeName);
                if(loadstate == LoadState.LOADED) {
                    return true;
                } else if (loadstate == LoadState.NOT_LOADED) {
                    return false;
                } // else continue
            }

            //None of the providers could determine the load state.
            return true;
        }

        public boolean isLoaded(Object entity) {
            PersistenceProviderResolver resolver = PersistenceProviderResolverHolder.getPersistenceProviderResolver();

            List<PersistenceProvider> providers = resolver.getPersistenceProviders();

            for (PersistenceProvider provider : providers) {
                LoadState loadstate = provider.getProviderUtil().isLoaded(entity);
                if(loadstate == LoadState.LOADED) {
                    return true;
                } else if (loadstate == LoadState.NOT_LOADED) {
                    return false;
                } // else continue
            }
            //None of the providers could determine the load state
            return true;
        }
    }

    /**
     * Properties used to override elements the configuration of a persistence unit
     * in {@code persistence.xml}. Clients of the {@link PersistenceConfiguration}
     * class should not use these properties.
     * @since 4.0
     */
    public interface UnitProperties {
        /**
         * The name of a class implementing {@link PersistenceProvider}.
         */
        String PERSISTENCE_PROVIDER = "jakarta.persistence.provider";

        /**
         * String specifying a {@link PersistenceUnitTransactionType}.
         */
        String PERSISTENCE_UNIT_TRANSACTION_TYPE = "jakarta.persistence.transactionType";

        /**
         * The JNDI name of a JTA data source used by the persistence provider to obtain database
         * connections.
         */
        String PERSISTENCE_UNIT_JTA_DATASOURCE = "jakarta.persistence.jtaDataSource";

        /**
         * The JNDI name of a non-JTA data source used by the persistence provider to obtain database
         * connections.
         */
        String PERSISTENCE_UNIT_NON_JTA_DATASOURCE = "jakarta.persistence.nonJtaDataSource";

        /**
         * The fully-qualified class name of an annotation annotated {@code jakarta.inject.Qualifier}.
         * This qualifier annotation may be used to disambiguate the persistence unit for the
         * purposes of dependency injection.
         */
        String PERSISTENCE_UNIT_BEAN_QUALIFIERS = "jakarta.persistence.qualifiers";

        /**
         * The fully-qualified class name of an annotation annotated {@code jakarta.inject.Scope}
         * or {@code jakarta.enterprise.context.NormalScope}. This scope annotation may be used to
         * determine the scope of a persistence context for the purposes of dependency injection.
         */
        String PERSISTENCE_UNIT_BEAN_SCOPE = "jakarta.persistence.scope";
    }

    /**
     * Properties used to configure the second-level cache.
     * @since 4.0
     */
    public interface CacheProperties {
        /**
         * String specifying a {@link SharedCacheMode}.
         *
         * <p>Defined for use with
         * {@link Persistence#createEntityManagerFactory(String, Map)}.
         * Clients of the {@link PersistenceConfiguration} class
         * should use {@link PersistenceConfiguration#sharedCacheMode(SharedCacheMode)}.
         */
        String CACHE_MODE = "jakarta.persistence.sharedCache.mode";
    }

    /**
     * Properties used to connect to the database via JDBC in Java SE.
     * @since 4.0
     */
    public interface ConnectionProperties {
        /**
         * Fully qualified name of the JDBC driver class.
         */
        String JDBC_DRIVER = "jakarta.persistence.jdbc.driver";

        /**
         * JDBC URL.
         */
        String JDBC_URL = "jakarta.persistence.jdbc.url";

        /**
         * Username for JDBC authentication.
         */
        String JDBC_USER = "jakarta.persistence.jdbc.user";

        /**
         * Password for JDBC authentication.
         */
        String JDBC_PASSWORD = "jakarta.persistence.jdbc.password";

        /**
         * An instance of {@code javax.sql.DataSource}.
         */
        String JDBC_DATASOURCE = "jakarta.persistence.dataSource";
    }

    /**
     * Properties used to optimize the interaction with JDBC.
     * @since 4.0
     */
    public interface JdbcProperties {
        /**
         * Override the default {@linkplain java.sql.Statement#setFetchSize JDBC fetch size}.
         * @since 4.0
         */
        String JDBC_FETCH_SIZE = "jakarta.persistence.jdbc.fetchSize";

        /**
         * Enable {@linkplain java.sql.Statement#executeBatch JDBC statement batching}
         * by setting a batch size.
         * <p>This setting is a hint.
         * @since 4.0
         */
        String JDBC_BATCH_SIZE = "jakarta.persistence.jdbc.batchSize";

        /**
         * Default pessimistic lock timeout hint.
         */
        String LOCK_TIMEOUT = "jakarta.persistence.lock.timeout";

        /**
         * Default {@linkplain java.sql.Statement#setQueryTimeout query timeout} hint.
         */
        String QUERY_TIMEOUT = "jakarta.persistence.query.timeout";
    }

    /**
     * Properties used to specify the database platform when JDBC metadata is not available.
     * @since 4.0
     */
    public interface DatabaseProperties {
        /**
         * The value returned by {@link java.sql.DatabaseMetaData#getDatabaseProductName()},
         * for use when JDBC metadata is not available.
         * @since 4.0
         */
        String DATABASE_PRODUCT_NAME = "jakarta.persistence.database-product-name";

        /**
         * The value returned by {@link java.sql.DatabaseMetaData#getDatabaseMajorVersion()},
         * for use when JDBC metadata is not available.
         * @since 4.0
         */
        String DATABASE_MAJOR_VERSION = "jakarta.persistence.database-major-version";

        /**
         * The value returned by {@link java.sql.DatabaseMetaData#getDatabaseMinorVersion()},
         * for use when JDBC metadata is not available.
         * @since 4.0
         */
        String DATABASE_MINOR_VERSION = "jakarta.persistence.database-minor-version";
    }

    /**
     * Properties used to control the schema management tooling.
     * @since 4.0
     */
    public interface SchemaManagementProperties {
        /**
         * The action to be performed against the database.
         *
         * <p>Standard actions are: {@code none}, {@code create},
         * {@code drop}, {@code drop-and-create}, {@code validate},
         * {@code populate}.
         *
         * @see SchemaManagementAction
         * @see PersistenceConfiguration#schemaManagementDatabaseAction(SchemaManagementAction)
         */
        String SCHEMAGEN_DATABASE_ACTION = "jakarta.persistence.schema-generation.database.action";

        /**
         * The action to be generated as a SQL script.
         *
         * <p>The script is generated in the location specified by
         * {@value #SCHEMAGEN_CREATE_TARGET} or {@value #SCHEMAGEN_DROP_TARGET}.
         *
         * <p>Standard actions are: {@code none}, {@code create},
         * {@code drop}, {@code drop-and-create}.
         *
         * @see SchemaManagementAction
         * @see PersistenceConfiguration#schemaManagementScriptsAction(SchemaManagementAction)
         */
        String SCHEMAGEN_SCRIPTS_ACTION = "jakarta.persistence.schema-generation.scripts.action";

        /**
         * The source of artifacts to be created.
         *
         * <p>Standard sources are: {@code metadata}, {@code script},
         * {@code metadata-then-script}, {@code script-then-metadata}.
         *
         * <p>The location of the script source is specified by
         * {@value #SCHEMAGEN_CREATE_SCRIPT_SOURCE}.
         */
        String SCHEMAGEN_CREATE_SOURCE = "jakarta.persistence.schema-generation.create-source";

        /**
         * The source of artifacts to be dropped.
         *
         * <p>Standard sources are: {@code metadata}, {@code script},
         * {@code metadata-then-script}, {@code script-then-metadata}.
         *
         * <p>The location of the script source is specified by
         * {@value #SCHEMAGEN_DROP_SCRIPT_SOURCE}.
         */
        String SCHEMAGEN_DROP_SOURCE = "jakarta.persistence.schema-generation.drop-source";

        /**
         * An application-provided SQL script to be executed when the
         * schema is created.
         * <p>
         * An instance of {@link java.io.Reader} or a string specifying
         * the file URL of the DDL script.
         */
        String SCHEMAGEN_CREATE_SCRIPT_SOURCE = "jakarta.persistence.schema-generation.create-script-source";

        /**
         * An application-provided SQL script to be executed when the
         * schema is dropped.
         * <p>
         * An instance of {@link java.io.Reader} or a string specifying
         * the file URL of the DDL script.
         */
        String SCHEMAGEN_DROP_SCRIPT_SOURCE = "jakarta.persistence.schema-generation.drop-script-source";

        /**
         * An application-provided SQL script to be executed after the
         * schema is created, typically used for loading data.
         * <p>
         * An instance of {@link java.io.Reader} or a string specifying
         * the file URL of the DML script.
         */
        String SCHEMAGEN_LOAD_SCRIPT_SOURCE = "jakarta.persistence.sql-load-script-source";

        /**
         * The provider-generated SQL script which creates the schema
         * when {@value SCHEMAGEN_SCRIPTS_ACTION} is set.
         * <p>
         * An instance of {@link java.io.Writer} or a string specifying
         * the file URL of the DDL script.
         */
        String SCHEMAGEN_CREATE_TARGET = "jakarta.persistence.schema-generation.scripts.create-target";

        /**
         * The provider-generated SQL script which drops the schema
         * when {@value SCHEMAGEN_SCRIPTS_ACTION} is set.
         * <p>
         * An instance of {@link java.io.Writer} or a string specifying
         * the file URL of the DDL script.
         */
        String SCHEMAGEN_DROP_TARGET = "jakarta.persistence.schema-generation.scripts.drop-target";

        /**
         * Specifies whether the persistence provider creates database schemas
         * in addition to creating database objects such as tables, sequences,
         * constraints, and so on. The value {@code true} specifies that the
         * persistence provider creates schemas in the database or generates
         * DDL containing {@code CREATE SCHEMA} commands.
         * <p>
         * If this property is not supplied, the provider does not create database
         * schemas.
         * @since 4.0
         */
        String SCHEMAGEN_CREATE_SCHEMAS = "jakarta.persistence.schema-generation.create-database-schemas";

        /**
         * Supplies a JDBC {@code Connection} to use for executing schema
         * management {@linkplain #SCHEMAGEN_DATABASE_ACTION actions} on the
         * database. If not specified, the persistence provider uses the
         * {@code DataSource} configured for use by the persistence unit to
         * obtain a JDBC connection.
         * @since 4.0
         */
        String SCHEMAGEN_CONNECTION = "jakarta.persistence.schema-generation.connection";
    }

    /**
     * Properties used to control the integration with Bean Validation.
     * @since 4.0
     */
    public interface ValidationProperties {
        /**
         * An instance of {@code jakarta.validation.ValidatorFactory}.
         */
        String VALIDATION_FACTORY = "jakarta.persistence.validation.factory";

        /**
         * Target groups for validation at {@link PrePersist}.
         */
        String VALIDATION_GROUP_PRE_PERSIST = "jakarta.persistence.validation.group.pre-persist";

        /**
         * Target groups for validation at {@link PreUpdate}.
         */
        String VALIDATION_GROUP_PRE_UPDATE = "jakarta.persistence.validation.group.pre-update";

        /**
         * Target groups for validation at {@link PreRemove}.
         */
        String VALIDATION_GROUP_PRE_REMOVE = "jakarta.persistence.validation.group.pre-remove";

        /**
         * String specifying a {@link ValidationMode}.
         *
         * <p>Defined for use with
         * {@link Persistence#createEntityManagerFactory(String, Map)}.
         * Clients of the {@link PersistenceConfiguration} class
         * should use {@link PersistenceConfiguration#validationMode(ValidationMode)}.
         */
        String VALIDATION_MODE = "jakarta.persistence.validation.mode";
    }

    /**
     * Properties used to control the integration with CDI.
     * @since 4.0
     */
    public interface BeanManagementProperties {
        /**
         * An instance of {@code jakarta.enterprise.inject.spi.BeanManager}.
         */
        String BEAN_MANAGER = "jakarta.persistence.bean.manager";
    }
}
