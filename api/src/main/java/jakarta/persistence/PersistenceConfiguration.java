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
public class PersistenceConfiguration
        implements Persistence.JdbcProperties,
                   Persistence.TimeoutProperties,
                   Persistence.DatabaseProperties,
                   Persistence.SchemaManagementProperties,
                   Persistence.ValidationProperties,
                   Persistence.CacheProperties,
                   Persistence.BeanManagementProperties {

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
}
