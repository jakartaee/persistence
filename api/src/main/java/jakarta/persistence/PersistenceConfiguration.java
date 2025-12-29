/*
 * Copyright (c) 2023, 2025 Contributors to the Eclipse Foundation
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


package jakarta.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a configuration of a persistence unit, allowing programmatic
 * creation of an {@link EntityManagerFactory}. The configuration options
 * available via this API reflect the similarly named elements of the
 * {@code persistence.xml} file.
 *
 * <p>This API may not be used to configure a container-managed persistence
 * unit. That is, the configured persistence unit should be considered a
 * Java SE persistence unit, even when this API is used within the Jakarta
 * EE environment.
 *
 * <p>If injection of the {@link EntityManagerFactory} is required, a CDI
 * {@code Producer} may be used to make the {@link EntityManagerFactory}
 * available as a CDI managed bean.
 * {@snippet :
 * @Produces @ApplicationScoped @Documents
 * EntityManagerFactory configure() {
 *     return new PersistenceConfiguration("DocumentData")
 *             .nonJtaDataSource("java:global/jdbc/DocumentDatabase")
 *             .defaultToOneFetchType(FetchType.LAZY)
 *             .managedClass(Document.class)
 *             .createEntityManagerFactory();
 * }
 * }
 *
 * <p>Similarly, if injection of an {@link EntityManager} is required,
 * a CDI {@code Producer} method/{@code Disposer} method pair may be
 * used to make the {@link EntityManager} available as a CDI managed
 * bean.
 * {@snippet :
 * @Produces @TransactionScoped @Documents
 * EntityManager create(@Documents EntityManagerFactory factory) {
 *     return factory.createEntityManager();
 * }
 *
 * void close(@Disposes @Documents EntityManager entityManager) {
 *     entityManager.close();
 * }
 * }
 *
 * <p>It is intended that persistence providers define subclasses of
 * this class with vendor-specific configuration options. A provider
 * must support configuration via any instance of this class or of any
 * subclass of this class.
 *
 * @see Persistence#createEntityManagerFactory(PersistenceConfiguration)
 *
 * @since 3.2
 */
public class PersistenceConfiguration {

    /**
     * Fully qualified name of the JDBC driver class.
     */
    public static final String JDBC_DRIVER = "jakarta.persistence.jdbc.driver";
    /**
     * JDBC URL.
     */
    public static final String JDBC_URL = "jakarta.persistence.jdbc.url";
    /**
     * Username for JDBC authentication.
     */
    public static final String JDBC_USER = "jakarta.persistence.jdbc.user";
    /**
     * Password for JDBC authentication.
     */
    public static final String JDBC_PASSWORD = "jakarta.persistence.jdbc.password";
    /**
     * An instance of {@code javax.sql.DataSource}.
     */
    public static final String JDBC_DATASOURCE = "jakarta.persistence.dataSource";
    /**
     * Override the default {@linkplain java.sql.Statement#setFetchSize JDBC fetch size}.
     * @since 4.0
     */
    public static final String JDBC_FETCH_SIZE = "jakarta.persistence.jdbc.fetchSize";
    /**
     * Enable {@linkplain java.sql.Statement#executeBatch JDBC statement batching}
     * by setting a batch size.
     * <p>This setting is a hint.
     * @since 4.0
     */
    public static final String JDBC_BATCH_SIZE = "jakarta.persistence.jdbc.batchSize";

    /**
     * Default pessimistic lock timeout hint.
     */
    public static final String LOCK_TIMEOUT = "jakarta.persistence.lock.timeout";
    /**
     * Default query timeout hint.
     */
    public static final String QUERY_TIMEOUT = "jakarta.persistence.query.timeout";

    /**
     * The action to be performed against the database.
     *
     * <p>Standard actions are: {@code none}, {@code create},
     * {@code drop}, {@code drop-and-create}, {@code validate},
     * {@code populate}.
     *
     * @see SchemaManagementAction
     * @see #schemaManagementDatabaseAction(SchemaManagementAction)
     */
    public static final String SCHEMAGEN_DATABASE_ACTION = "jakarta.persistence.schema-generation.database.action";
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
     * @see #schemaManagementScriptsAction(SchemaManagementAction)
     */
    public static final String SCHEMAGEN_SCRIPTS_ACTION = "jakarta.persistence.schema-generation.scripts.action";
    /**
     * The source of artifacts to be created.
     *
     * <p>Standard sources are: {@code metadata}, {@code script},
     * {@code metadata-then-script}, {@code script-then-metadata}.
     *
     * <p>The location of the script source is specified by
     * {@value #SCHEMAGEN_CREATE_SCRIPT_SOURCE}.
     */
    public static final String SCHEMAGEN_CREATE_SOURCE = "jakarta.persistence.schema-generation.create-source";
    /**
     * The source of artifacts to be dropped.
     *
     * <p>Standard sources are: {@code metadata}, {@code script},
     * {@code metadata-then-script}, {@code script-then-metadata}.
     *
     * <p>The location of the script source is specified by
     * {@value #SCHEMAGEN_DROP_SCRIPT_SOURCE}.
     */
    public static final String SCHEMAGEN_DROP_SOURCE = "jakarta.persistence.schema-generation.drop-source";
    /**
     * An application-provided SQL script to be executed when the
     * schema is created.
     * <p>
     * An instance of {@link java.io.Reader} or a string specifying
     * the file URL of the DDL script.
     */
    public static final String SCHEMAGEN_CREATE_SCRIPT_SOURCE = "jakarta.persistence.schema-generation.create-script-source";
    /**
     * An application-provided SQL script to be executed when the
     * schema is dropped.
     * <p>
     * An instance of {@link java.io.Reader} or a string specifying
     * the file URL of the DDL script.
     */
    public static final String SCHEMAGEN_DROP_SCRIPT_SOURCE = "jakarta.persistence.schema-generation.drop-script-source";
    /**
     * An application-provided SQL script to be executed after the
     * schema is created, typically used for loading data.
     * <p>
     * An instance of {@link java.io.Reader} or a string specifying
     * the file URL of the DML script.
     */
    public static final String SCHEMAGEN_LOAD_SCRIPT_SOURCE = "jakarta.persistence.sql-load-script-source";
    /**
     * The provider-generated SQL script which creates the schema
     * when {@value SCHEMAGEN_SCRIPTS_ACTION} is set.
     * <p>
     * An instance of `{@link java.io.Writer} or a string specifying
     * the file URL of the DDL script.
     */
    public static final String SCHEMAGEN_CREATE_TARGET = "jakarta.persistence.schema-generation.scripts.create-target";
    /**
     * The provider-generated SQL script which drops the schema
     * when {@value SCHEMAGEN_SCRIPTS_ACTION} is set.
     * <p>
     * An instance of `{@link java.io.Writer} or a string specifying
     * the file URL of the DDL script.
     */
    public static final String SCHEMAGEN_DROP_TARGET = "jakarta.persistence.schema-generation.scripts.drop-target";

    /**
     * The value returned by {@link java.sql.DatabaseMetaData#getDatabaseProductName()},
     * for use when JDBC metadata is not available.
     * @since 4.0
     */
    public static final String DATABASE_PRODUCT_NAME = "jakarta.persistence.database-product-name";

    /**
     * The value returned by {@link java.sql.DatabaseMetaData#getDatabaseMajorVersion()},
     * for use when JDBC metadata is not available.
     * @since 4.0
     */
    public static final String DATABASE_MAJOR_VERSION = "jakarta.persistence.database-major-version";

    /**
     * The value returned by {@link java.sql.DatabaseMetaData#getDatabaseMinorVersion()},
     * for use when JDBC metadata is not available.
     * @since 4.0
     */
    public static final String DATABASE_MINOR_VERSION = "jakarta.persistence.database-minor-version";

    /**
     * An instance of {@code jakarta.validation.ValidatorFactory}.
     */
    public static final String VALIDATION_FACTORY = "jakarta.persistence.validation.factory";
    /**
     * Target groups for validation at {@link PrePersist}.
     */
    public static final String VALIDATION_GROUP_PRE_PERSIST = "jakarta.persistence.validation.group.pre-persist";
    /**
     * Target groups for validation at {@link PreUpdate}.
     */
    public static final String VALIDATION_GROUP_PRE_UPDATE = "jakarta.persistence.validation.group.pre-update";
    /**
     * Target groups for validation at {@link PreRemove}.
     */
    public static final String VALIDATION_GROUP_PRE_REMOVE = "jakarta.persistence.validation.group.pre-remove";

    /**
     * String specifying a {@link SharedCacheMode}.
     *
     * <p>Defined for use with
     * {@link Persistence#createEntityManagerFactory(String, Map)}.
     * Clients of this {@code PersistenceConfiguration} class
     * should use {@link #sharedCacheMode(SharedCacheMode)}.
     */
    public static final String CACHE_MODE = "jakarta.persistence.sharedCache.mode";

    /**
     * Represents an action that can be performed by the schema management tooling.
     *
     * @since 4.0
     */
    public enum SchemaManagementAction {
        /**
         * No action.
         */
        NONE,
        /**
         * Create the generated database schema.
         *
         * @see SchemaManager#create(boolean)
         */
        CREATE,
        /**
         * Drop the generated database schema.
         *
         * @see SchemaManager#drop(boolean)
         */
        DROP,
        /**
         * Drop and then recreate the generated database schema.
         */
        DROP_AND_CREATE,
        /**
         * Validate the schema held in the database against the
         * generated schema.
         *
         * @see SchemaManager#validate()
         */
        VALIDATE,
        /**
         * Populate the database with data from the DML load script.
         *
         * @see PersistenceConfiguration#SCHEMAGEN_LOAD_SCRIPT_SOURCE
         * @see SchemaManager#populate()
         */
        POPULATE
    }

    private final String name;

    private String provider;
    private String jtaDataSource;
    private String nonJtaDataSource;

    private SharedCacheMode sharedCacheMode = SharedCacheMode.UNSPECIFIED;
    private ValidationMode validationMode = ValidationMode.AUTO;
    private PersistenceUnitTransactionType transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
    private FetchType defaultToOneFetchType = FetchType.EAGER;

    private SchemaManagementAction schemaManagementDatabaseAction = SchemaManagementAction.NONE;
    private SchemaManagementAction schemaManagementScriptsAction = SchemaManagementAction.NONE;

    private DataSourceConfiguration jtaDataSourceConfiguration;
    private DataSourceConfiguration nonJtaDataSourceConfiguration;

    private final List<Class<?>> managedClasses = new ArrayList<>();
    private final List<String> mappingFileNames = new ArrayList<>();
    private final Map<String,Object> properties = new HashMap<>();

    /**
     * Create a new empty configuration. An empty configuration does not
     * typically hold enough information for successful invocation of
     * {@link #createEntityManagerFactory()}.
     *
     * @param name the name of the persistence unit, which may be used by
     *             the persistence provider for logging and error reporting
     */
    public PersistenceConfiguration(String name) {
        Objects.requireNonNull(name, "Persistence unit name should not be null");
        this.name = name;
    }

    /**
     * Create a new {@link EntityManagerFactory} based on this configuration.
     * @throws PersistenceException if required configuration is missing or
     *                             if the factory could not be created
     */
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(this);
    }

    /**
     * Execute the schema management action specified by the property
     * {@link #SCHEMAGEN_DATABASE_ACTION} or {@link #SCHEMAGEN_SCRIPTS_ACTION},
     * or by {@link #schemaManagementDatabaseAction(SchemaManagementAction)}
     * or {@link #schemaManagementScriptsAction(SchemaManagementAction)}
     * without creating an {@link EntityManagerFactory}.
     *
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided or if schema
     *         generation otherwise fails.
     *
     * @since 4.0
     */
    public void exportSchema() {
        Persistence.generateSchema( this );
    }

    /**
     * The name of the persistence unit, which may be used by the persistence
     * provider for logging and error reporting.
     * @return the name of the persistence unit.
     */
    public String name() {
        return name;
    }

    /**
     * Specify the persistence provider.
     * @param providerClassName the qualified name of the persistence provider class
     * @return this configuration
     */
    public PersistenceConfiguration provider(String providerClassName) {
        this.provider = providerClassName;
        return this;
    }

    /**
     * The fully-qualified name of a concrete class implementing
     * {@link jakarta.persistence.spi.PersistenceProvider}.
     * @return the qualified name of the persistence provider class.
     */
    public String provider() {
        return provider;
    }

    /**
     * Specify the JNDI name of a JTA {@code javax.sql.DataSource}.
     * @param dataSourceJndiName the JNDI name of a JTA datasource
     * @return this configuration
     * @see #jtaDataSourceConfiguration(DataSourceConfiguration)
     */
    public PersistenceConfiguration jtaDataSource(String dataSourceJndiName) {
        this.jtaDataSource = dataSourceJndiName;
        return this;
    }

    /**
     * The JNDI name of a JTA {@code javax.sql.DataSource}.
     * @return the configured JTA datasource, if any, or null
     */
    public String jtaDataSource() {
        return jtaDataSource;
    }

    /**
     * Specify the JNDI name of a non-JTA {@code javax.sql.DataSource}.
     * @param dataSourceJndiName the JNDI name of a non-JTA datasource
     * @return this configuration
     * @see #nonJtaDataSourceConfiguration(DataSourceConfiguration)
     */
    public PersistenceConfiguration nonJtaDataSource(String dataSourceJndiName) {
        this.nonJtaDataSource = dataSourceJndiName;
        return this;
    }

    /**
     * The JNDI name of a non-JTA {@code javax.sql.DataSource}.
     * @return the configured non-JTA datasource, if any, or null
     */
    public String nonJtaDataSource() {
        return nonJtaDataSource;
    }

    /**
     * Add a managed class (an {@link Entity}, {@link Embeddable},
     * {@link MappedSuperclass}, or {@link Converter}) to the
     * configuration.
     * @param managedClass the managed class
     * @return this configuration
     */
    public PersistenceConfiguration managedClass(Class<?> managedClass) {
        managedClasses.add(managedClass);
        return this;
    }

    /**
     * The configured managed classes, that is, a list of classes
     * annotated {@link Entity}, {@link Embeddable},
     * {@link MappedSuperclass}, or {@link Converter}.
     * @return all configured managed classes
     */
    public List<Class<?>> managedClasses() {
        return managedClasses;
    }

    /**
     * Add the path of an XML mapping file loaded as a resource to
     * the configuration.
     * @param name the resource path of the mapping file
     * @return this configuration
     */
    public PersistenceConfiguration mappingFile(String name) {
        mappingFileNames.add(name);
        return this;
    }

    /**
     * The configured resource paths of XML mapping files.
     * @return all configured mapping file resource paths
     */
    public List<String> mappingFiles() {
        return mappingFileNames;
    }

    /**
     * Specify the transaction type for the persistence unit.
     * @param transactionType the transaction type
     * @return this configuration
     */
    public PersistenceConfiguration transactionType(PersistenceUnitTransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    /**
     * The {@linkplain PersistenceUnitTransactionType transaction type}.
     * <ul>
     * <li>If {@link PersistenceUnitTransactionType#JTA}, a JTA data
     *    source must be provided via {@link #jtaDataSource()},
     *    or by the container.
     * <li>If {@link PersistenceUnitTransactionType#RESOURCE_LOCAL},
     *    database connection properties may be specified via
     *    {@link #properties()}, or a non-JTA datasource may be
     *    provided via {@link #nonJtaDataSource()}.
     * </ul>
     * @return the transaction type
     */
    public PersistenceUnitTransactionType transactionType() {
        return transactionType;
    }

    /**
     * Specify the shared cache mode for the persistence unit.
     * @param sharedCacheMode the shared cache mode
     * @return this configuration
     */
    public PersistenceConfiguration sharedCacheMode(SharedCacheMode sharedCacheMode) {
        this.sharedCacheMode = sharedCacheMode;
        return this;
    }

    /**
     * The shared cache mode. The default behavior is unspecified
     * and {@linkplain SharedCacheMode#UNSPECIFIED provider-specific}.
     * @return the shared cache mode
     */
    public SharedCacheMode sharedCacheMode() {
        return sharedCacheMode;
    }

    /**
     * Specify the validation mode for the persistence unit.
     * @param validationMode the shared cache mode
     * @return this configuration
     */
    public PersistenceConfiguration validationMode(ValidationMode validationMode) {
        this.validationMode = validationMode;
        return this;
    }

    /**
     * The validation mode, {@link ValidationMode#AUTO} by default.
     * @return the validation mode
     */
    public ValidationMode validationMode() {
        return validationMode;
    }

    /**
     * Specify the {@linkplain FetchType#DEFAULT default fetch type}
     * for one-to-one and many-to-one associations. The given fetch type
     * value must be {@link FetchType#EAGER} or {@link FetchType#LAZY}.
     * @param defaultToOneFetchType the default fetch type
     * @return this configuration
     * @since 4.0
     */
    public PersistenceConfiguration defaultToOneFetchType(FetchType defaultToOneFetchType) {
        if (defaultToOneFetchType == null || defaultToOneFetchType == FetchType.DEFAULT) {
            throw new IllegalArgumentException("defaultToOneFetchType: " + defaultToOneFetchType);
        }
        this.defaultToOneFetchType = defaultToOneFetchType;
        return this;
    }

    /**
     * The {@linkplain FetchType#DEFAULT default fetch type},
     * {@link FetchType#EAGER} by default.
     * @since 4.0
     */
    public FetchType defaultToOneFetchType() {
        return defaultToOneFetchType;
    }

    /**
     * Set the schema management action to be performed against the database.
     *
     * @param action The schema management action to be performed
     * @see #SCHEMAGEN_DATABASE_ACTION
     * @since 4.0
     */
    public PersistenceConfiguration schemaManagementDatabaseAction(SchemaManagementAction action) {
        this.schemaManagementDatabaseAction = action;
        return this;
    }

    /**
     * The schema management action to be performed against the database.
     *
     * @return The schema management action
     * @see #SCHEMAGEN_DATABASE_ACTION
     * @since 4.0
     */
    public SchemaManagementAction schemaManagementDatabaseAction() {
        return schemaManagementDatabaseAction;
    }

    /**
     * Set the schema management action to be performed by generated DDL scripts.
     *
     * @param action The schema management action to be performed
     * @see #SCHEMAGEN_SCRIPTS_ACTION
     * @since 4.0
     */
    public PersistenceConfiguration schemaManagementScriptsAction(SchemaManagementAction action) {
        this.schemaManagementScriptsAction = action;
        return this;
    }

    /**
     * The schema management action to be performed by generated DDL scripts.
     *
     * @return The schema management action
     * @see #SCHEMAGEN_SCRIPTS_ACTION
     * @since 4.0
     */
    public SchemaManagementAction getSchemaManagementScriptsAction() {
        return schemaManagementScriptsAction;
    }

    /**
     * Set a property of this persistence unit.
     * @param name the property name
     * @param value the property value
     * @return this configuration
     */
    public PersistenceConfiguration property(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    /**
     * Set multiple properties of this persistence unit.
     * @param properties the properties
     * @return this configuration
     */
    public PersistenceConfiguration properties(Map<String,?> properties) {
        this.properties.putAll(properties);
        return this;
    }

    /**
     * Standard and vendor-specific property settings.
     * @return the configured properties
     */
    public Map<String, Object> properties() {
        return properties;
    }

    /**
     * Specify a JTA data source configuration.
     * Takes precedence over {@link #jtaDataSource()}.
     * @param jtaDataSource A {@link DataSourceConfiguration}
     * @since 4.0
     */
    public void jtaDataSourceConfiguration(DataSourceConfiguration jtaDataSource) {
        this.jtaDataSourceConfiguration = jtaDataSource;
    }

    /**
     * The JTA data source configuration, if any.
     * Takes precedence over {@link #jtaDataSource()}.
     * @return A {@link DataSourceConfiguration}
     * @since 4.0
     */
    public DataSourceConfiguration jtaDataSourceConfiguration() {
        return jtaDataSourceConfiguration;
    }

    /**
     * Specify a non-JTA data source configuration.
     * Takes precedence over {@link #nonJtaDataSource()} if non-null.
     * @param nonJtaDataSource A {@link DataSourceConfiguration}
     * @since 4.0
     */
    public void nonJtaDataSourceConfiguration(DataSourceConfiguration nonJtaDataSource) {
        this.nonJtaDataSourceConfiguration = nonJtaDataSource;
    }

    /**
     * The non-JTA data source configuration, if any.
     * Takes precedence over {@link #nonJtaDataSource()} if non-null.
     * @return A {@link DataSourceConfiguration}
     * @since 4.0
     */
    public DataSourceConfiguration nonJtaDataSourceConfiguration() {
        return nonJtaDataSourceConfiguration;
    }

    /**
     * Represents a data source configuration, allowing programmatic definition
     * of a data source. The configuration options available via this API reflect
     * the attributes of the {@code jakarta.annotation.sql.DataSourceDefinition}
     * annotation.
     * {@snippet :
     * var entityManagerFactory =
     *         new PersistenceConfiguration("DocumentData")
     *             .nonJtaDataSource(new DataSourceConfiguration()
     *                 .className("org.postgresql.Driver")
     *                 .url("jdbc:postgresql://localhost/documents")
     *                 .user("gavin")
     *                 .password("pandemuert0"))
     *             .managedClass(Document.class)
     *             .createEntityManagerFactory();
     * }
     *
     * @see #jtaDataSourceConfiguration(DataSourceConfiguration)
     * @see #nonJtaDataSourceConfiguration(DataSourceConfiguration)
     *
     * @since 4.0
     */
    public static class DataSourceConfiguration {

        private String className;
        private String description = "";
        private String url = "";
        private String user = "";
        private String password = "";
        private String databaseName = "";
        private int portNumber = -1;
        private String serverName = "localhost";
        private int isolationLevel = -1;
        private boolean transactional = true;
        private int initialPoolSize = -1;
        private int maxPoolSize = -1;
        private int minPoolSize = -1;
        private int maxIdleTime = -1;
        private int maxStatements = -1;
        private int loginTimeout = 0;

        private final Map<String, Object> properties = new HashMap<>();

        /**
         * Specify the name of a DataSource class that implements
         * {@code javax.sql.DataSource}, {@code javax.sql.XADataSource},
         * or {@code javax.sql.ConnectionPoolDataSource}.
         * <p>This setting must always be specified explicitly.
         * @param className the DataSource implementation class name
         * @return this configuration
         */
        public DataSourceConfiguration className(String className) {
            this.className = className;
            return this;
        }

        /**
         * Name of a DataSource class that implements
         * {@code javax.sql.DataSource}, {@code javax.sql.XADataSource},
         * or {@code javax.sql.ConnectionPoolDataSource}.
         * @return the DataSource implementation class name
         */
        public String className() {
            return className;
        }

        /**
         * Description of this data source.
         * @param description the description
         * @return this configuration
         */
        public DataSourceConfiguration description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Description of this data source.
         * @return the description
         */
        public String description() {
            return description;
        }

        /**
         * Specify the JDBC URL. If the {@code url} property is specified
         * along with other standard properties such as {@link #serverName()},
         * {@link #portNumber()}, and {@link #databaseName()}, the precedence
         * is implementation-specific.
         * @param url the JDBC URL
         * @return this configuration
         */
        public DataSourceConfiguration url(String url) {
            this.url = url;
            return this;
        }

        /**
         * The JDBC URL. If the {@code url} property is specified along
         * with other standard properties such as {@link #serverName()},
         * {@link #portNumber()}, and {@link #databaseName()}, the
         * precedence is implementation-specific.
         * @return the JDBC URL
         */
        public String url() {
            return url;
        }

        /**
         * Set the username to use for connection authentication.
         * @param user the username
         * @return this configuration
         */
        public DataSourceConfiguration user(String user) {
            this.user = user;
            return this;
        }

        /**
         * Username to use for connection authentication.
         * @return the username
         */
        public String user() {
            return user;
        }

        /**
         * Set the password to use for connection authentication.
         * @param password the password
         * @return this configuration
         */
        public DataSourceConfiguration password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Password to use for connection authentication.
         * @return the password
         */
        public String password() {
            return password;
        }

        /**
         * Name of a database on the server.
         * @param databaseName the database name
         * @return this configuration
         */
        public DataSourceConfiguration databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        /**
         * Name of a database on the server.
         * @return the database name
         */
        public String databaseName() {
            return databaseName;
        }

        /**
         * Port number where a server is listening for requests.
         * @param portNumber the port number
         * @return this configuration
         */
        public DataSourceConfiguration portNumber(int portNumber) {
            this.portNumber = portNumber;
            return this;
        }

        /**
         * Port number where a server is listening for requests.
         * @return the port number
         */
        public int portNumber() {
            return portNumber;
        }

        /**
         * Database server name.
         * @param serverName the server name
         * @return this configuration
         */
        public DataSourceConfiguration serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        /**
         * Database server name.
         * @return the server name
         */
        public String serverName() {
            return serverName;
        }

        /**
         * Isolation level for connections. The isolation level must be one
         * of the following values:
         * <ul>
         * <li>{@link java.sql.Connection#TRANSACTION_NONE}
         * <li>{@link java.sql.Connection#TRANSACTION_READ_UNCOMMITTED}
         * <li>{@link java.sql.Connection#TRANSACTION_READ_COMMITTED}
         * <li>{@link java.sql.Connection#TRANSACTION_REPEATABLE_READ}
         * <li>{@link java.sql.Connection#TRANSACTION_SERIALIZABLE}
         * </ul>
         * <p>Default is vendor-specific.
         * @param isolationLevel the isolation level
         * @return this configuration
         * @see java.sql.Connection
         */
        public DataSourceConfiguration isolationLevel(int isolationLevel) {
            this.isolationLevel = isolationLevel;
            return this;
        }

        /**
         * Isolation level for connections. The isolation level is one of
         * the following values:
         * <ul>
         * <li>{@link java.sql.Connection#TRANSACTION_NONE}
         * <li>{@link java.sql.Connection#TRANSACTION_READ_UNCOMMITTED}
         * <li>{@link java.sql.Connection#TRANSACTION_READ_COMMITTED}
         * <li>{@link java.sql.Connection#TRANSACTION_REPEATABLE_READ}
         * <li>{@link java.sql.Connection#TRANSACTION_SERIALIZABLE}
         * </ul>
         * <p>Default is vendor-specific.
         * @return the isolation level
         * @see java.sql.Connection
         */
        public int isolationLevel() {
            return isolationLevel;
        }

        /**
         * Specifies whether connections should participate in transactions.
         * @return this configuration
         */
        public DataSourceConfiguration transactional(boolean transactional) {
            this.transactional = transactional;
            return this;
        }

        /**
         * Whether connections should participate in transactions.
         * <p>By default, a connection is enlisted in a transaction whenever
         * a transaction is active or becomes active.
         * @return whether connections participate in transactions
         */
        public boolean transactional() {
            return transactional;
        }

        /**
         * Specify the number of connections that should be created when a
         * connection pool is initialized.
         * <p>Default is vendor-specific.
         * @param initialPoolSize the initial pool size
         * @return this configuration
         */
        public DataSourceConfiguration initialPoolSize(int initialPoolSize) {
            this.initialPoolSize = initialPoolSize;
            return this;
        }

        /**
         * The number of connections that should be created when a connection
         * pool is initialized.
         * <p>Default is vendor-specific.
         * @return the initial pool size
         */
        public int initialPoolSize() {
            return initialPoolSize;
        }

        /**
         * Specify the maximum number of connections that may be concurrently
         * allocated by a connection pool.
         * <p>Default is vendor-specific.
         * @param maxPoolSize the maximum pool size
         * @return this configuration
         */
        public DataSourceConfiguration maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        /**
         * The maximum number of connections that may be concurrently
         * allocated by a connection pool.
         * <p>Default is vendor-specific.
         * @return the maximum pool size
         */
        public int maxPoolSize() {
            return maxPoolSize;
        }

        /**
         * Specify the minimum number of connections that should be allocated
         * by a connection pool.
         * <p>Default is vendor-specific.
         * @param minPoolSize the minimum pool size
         * @return this configuration
         */
        public DataSourceConfiguration minPoolSize(int minPoolSize) {
            this.minPoolSize = minPoolSize;
            return this;
        }

        /**
         * The minimum number of connections that should be allocated by a
         * connection pool.
         * <p>Default is vendor-specific.
         * @return the minimum pool size
         */
        public int minPoolSize() {
            return minPoolSize;
        }

        /**
         * Specify the number of seconds that a physical connection should
         * remain unused in the connection pool before the connection is
         * closed.
         * A value of 0 indicates that there is no limit.
         * @param maxIdleTime maximum idle time in seconds
         * @return this configuration
         */
        public DataSourceConfiguration maxIdleTime(int maxIdleTime) {
            this.maxIdleTime = maxIdleTime;
            return this;
        }

        /**
         * The number of seconds that a physical connection should remain
         * unused in the connection pool before the connection is closed.
         * A value of 0 indicates that there is no limit.
         * <p>Default is vendor-specific.
         * @return maximum idle time in seconds
         */
        public int maxIdleTime() {
            return maxIdleTime;
        }

        /**
         * Specify the total number of statements that a connection pool
         * should keep open. A value of 0 indicates that statement caching
         * is disabled.
         * @param maxStatements the maximum number of statements
         * @return this configuration
         */
        public DataSourceConfiguration maxStatements(int maxStatements) {
            this.maxStatements = maxStatements;
            return this;
        }

        /**
         * The total number of statements that a connection pool should
         * keep open. A value of 0 indicates that statement caching is
         * disabled.
         * <p>Default is vendor-specific.
         * @return the maximum number of statements
         */
        public int maxStatements() {
            return maxStatements;
        }

        /**
         * Set a property of this DataSource.
         * @param name the property name
         * @param value the property value
         * @return this configuration
         */
        public DataSourceConfiguration property(String name, Object value) {
            properties.put(name, value);
            return this;
        }

        /**
         * Set multiple properties of this DataSource.
         * @param properties the properties
         * @return this configuration
         */
        public DataSourceConfiguration properties(Map<String,?> properties) {
            this.properties.putAll(properties);
            return this;
        }

        /**
         * Vendor-specific and less commonly used DataSource properties.
         * @return vendor-specific properties
         */
        public Map<String, Object> properties() {
            return properties;
        }

        /**
         * Specify the maximum time in seconds that this data source will
         * wait while attempting to connect to a database. A value of 0
         * specifies that the timeout is the default system timeout if
         * there is one; otherwise, it specifies that there is no timeout.
         * @param loginTimeout the login timeout in seconds
         * @return this configuration
         */
        public DataSourceConfiguration loginTimeout(int loginTimeout) {
            this.loginTimeout = loginTimeout;
            return this;
        }

        /**
         * Maximum time in seconds that this data source will wait while
         * attempting to connect to a database. A value of 0 specifies
         * that the timeout is the default system timeout if there is one;
         * otherwise, it specifies that there is no timeout.
         * <p>Default is 0.
         * @return the login timeout in seconds
         */
        public int loginTimeout() {
            return loginTimeout;
        }
    }
}
