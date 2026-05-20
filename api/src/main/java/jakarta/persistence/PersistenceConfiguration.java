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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

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
                   Persistence.ConnectionProperties,
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
    public PersistenceConfiguration(@Nonnull String name) {
        requireNonNull(name, "Persistence unit name should not be null");
        this.name = name;
    }

    /**
     * Create a new {@link EntityManagerFactory} based on this configuration.
     * @throws PersistenceException if required configuration is missing or
     *                              if the factory could not be created
     * @apiNote This operation is very expensive. It should usually be called
     *          just once for each persistence unit.
     */
    @Nonnull
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
    @Nonnull
    public String name() {
        return name;
    }

    /**
     * Specify the persistence provider.
     * @param providerClassName the qualified name of the persistence provider class
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration provider(@Nullable String providerClassName) {
        this.provider = providerClassName;
        return this;
    }

    /**
     * The fully-qualified name of a concrete class implementing
     * {@link jakarta.persistence.spi.PersistenceProvider}.
     * @return the qualified name of the persistence provider class.
     */
    @Nullable
    public String provider() {
        return provider;
    }

    /**
     * Specify the JNDI name of a JTA {@code javax.sql.DataSource}.
     * @param dataSourceJndiName the JNDI name of a JTA datasource
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration jtaDataSource(@Nullable String dataSourceJndiName) {
        this.jtaDataSource = dataSourceJndiName;
        return this;
    }

    /**
     * The JNDI name of a JTA {@code javax.sql.DataSource}.
     * @return the configured JTA datasource, if any, or null
     */
    @Nullable
    public String jtaDataSource() {
        return jtaDataSource;
    }

    /**
     * Specify the JNDI name of a non-JTA {@code javax.sql.DataSource}.
     * @param dataSourceJndiName the JNDI name of a non-JTA datasource
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration nonJtaDataSource(@Nullable String dataSourceJndiName) {
        this.nonJtaDataSource = dataSourceJndiName;
        return this;
    }

    /**
     * The JNDI name of a non-JTA {@code javax.sql.DataSource}.
     * @return the configured non-JTA datasource, if any, or null
     */
    @Nullable
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
    @Nonnull
    public PersistenceConfiguration managedClass(@Nonnull Class<?> managedClass) {
        requireNonNull(managedClass, "managedClass cannot be null");
        managedClasses.add(managedClass);
        return this;
    }

    /**
     * The configured managed classes, that is, a list of classes
     * annotated {@link Entity}, {@link Embeddable},
     * {@link MappedSuperclass}, or {@link Converter}.
     * @return all configured managed classes
     */
    @Nonnull
    public List<Class<?>> managedClasses() {
        return managedClasses;
    }

    /**
     * Add the path of an XML mapping file loaded as a resource to
     * the configuration.
     * @param name the resource path of the mapping file
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration mappingFile(@Nonnull String name) {
        requireNonNull(name, "name cannot be null");
        mappingFileNames.add(name);
        return this;
    }

    /**
     * The configured resource paths of XML mapping files.
     * @return all configured mapping file resource paths
     */
    @Nonnull
    public List<String> mappingFiles() {
        return mappingFileNames;
    }

    /**
     * Specify the transaction type for the persistence unit.
     * @param transactionType the transaction type
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration transactionType(@Nonnull PersistenceUnitTransactionType transactionType) {
        requireNonNull(transactionType, "transactionType cannot be null");
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
    @Nonnull
    public PersistenceUnitTransactionType transactionType() {
        return transactionType;
    }

    /**
     * Specify the shared cache mode for the persistence unit.
     * @param sharedCacheMode the shared cache mode
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration sharedCacheMode(@Nonnull SharedCacheMode sharedCacheMode) {
        requireNonNull(sharedCacheMode, "sharedCacheMode cannot be null");
        this.sharedCacheMode = sharedCacheMode;
        return this;
    }

    /**
     * The shared cache mode. The default behavior is unspecified
     * and {@linkplain SharedCacheMode#UNSPECIFIED provider-specific}.
     * @return the shared cache mode
     */
    @Nonnull
    public SharedCacheMode sharedCacheMode() {
        return sharedCacheMode;
    }

    /**
     * Specify the validation mode for the persistence unit.
     * @param validationMode the shared cache mode
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration validationMode(@Nonnull ValidationMode validationMode) {
        requireNonNull(validationMode, "validationMode cannot be null");
        this.validationMode = validationMode;
        return this;
    }

    /**
     * The validation mode, {@link ValidationMode#AUTO} by default.
     * @return the validation mode
     */
    @Nonnull
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
    @Nonnull
    public PersistenceConfiguration defaultToOneFetchType(@Nonnull FetchType defaultToOneFetchType) {
        requireNonNull(defaultToOneFetchType, "defaultToOneFetchType cannot be null");
        if (defaultToOneFetchType == FetchType.DEFAULT) {
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
    @Nonnull
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
    @Nonnull
    public PersistenceConfiguration schemaManagementDatabaseAction(@Nonnull SchemaManagementAction action) {
        requireNonNull(action, "action cannot be null");
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
    @Nonnull
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
    @Nonnull
    public PersistenceConfiguration schemaManagementScriptsAction(@Nonnull SchemaManagementAction action) {
        requireNonNull(action, "action cannot be null");
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
    @Nonnull
    public SchemaManagementAction schemaManagementScriptsAction() {
        return schemaManagementScriptsAction;
    }

    /**
     * Set a property of this persistence unit.
     * @param name the property name
     * @param value the property value
     * @return this configuration
     * @see Persistence.JdbcProperties
     * @see Persistence.ConnectionProperties
     * @see Persistence.DatabaseProperties
     * @see Persistence.SchemaManagementProperties
     * @see Persistence.ValidationProperties
     * @see Persistence.CacheProperties
     * @see Persistence.BeanManagementProperties
     */
    @Nonnull
    public PersistenceConfiguration property(@Nonnull String name,
                                             @Nullable Object value) {
        properties.put(name, value);
        return this;
    }

    /**
     * Set multiple properties of this persistence unit.
     * @param properties the properties
     * @return this configuration
     */
    @Nonnull
    public PersistenceConfiguration properties(@Nonnull Map<String,?> properties) {
        this.properties.putAll(properties);
        return this;
    }

    /**
     * Standard and vendor-specific property settings.
     * @return the configured properties
     */
    @Nonnull
    public Map<String, Object> properties() {
        return properties;
    }
}
