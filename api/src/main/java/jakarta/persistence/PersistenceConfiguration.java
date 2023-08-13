/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 3.2
package jakarta.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a configuration of a persistence unit, allowing programmatic
 * creation of an {@link EntityManagerFactory}. The configuration options
 * available via this API reflect the similarly-named elements of the
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
 *
 * <pre>
 * &#064;Produces &#064;ApplicationScoped &#064;Documents
 * EntityManagerFactory configure() {
 *     return new PersistenceConfiguration()
 *             .name("DocumentData")
 *             .nonJtaDataSource("java:global/jdbc/DocumentDatabase")
 *             .managedClass(Document.class)
 *             .createEntityManagerFactory();
 * }
 * </pre>
 *
 * <p>Similarly, if injection of an {@link EntityManager} is required,
 * a CDI {@code Producer} method/{@code Disposer} method pair may be
 * used to make the {@link EntityManager} available as a CDI managed
 * bean.
 *
 * <pre>
 * &#064;Produces &#064;TransactionScoped &#064;Documents
 * EntityManager create(&#064;Documents EntityManagerFactory factory) {
 *     return factory.createEntityManager();
 * }
 *
 * void close(&#064;Disposes &#064;Documents EntityManager entityManager) {
 *     entityManager.close();
 * }
 * </pre>
 *
 * @see Persistence#createEntityManagerFactory(PersistenceConfiguration)
 *
 * @since 3.2
 */
public class PersistenceConfiguration {

    private String name;
    private String provider;
    private String jtaDataSource;
    private String nonJtaDataSource;

    private SharedCacheMode sharedCacheMode = SharedCacheMode.UNSPECIFIED;
    private ValidationMode validationMode = ValidationMode.AUTO;
    private PersistenceTransactionType transactionType = PersistenceTransactionType.RESOURCE_LOCAL;

    private List<Class<?>> managedClasses = new ArrayList<>();
    private List<String> mappingFileNames = new ArrayList<>();
    private Map<String,Object> properties = new HashMap<>();

    /**
     * Create a new {@link EntityManagerFactory} based on this configuration.
     * @throws IllegalStateException if required configuration is missing
     */
    public EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(this);
    }

    /**
     * Specify the name of the persistence unit.
     */
    public PersistenceConfiguration name(String name) {
        this.name = name;
        return this;
    }

    /**
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
     * @return the configured JTA datasource, if any
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
     * @return the configured non-JTA datasource, if any
     */
    public String nonJtaDataSource() {
        return nonJtaDataSource;
    }

    /**
     * Add a managed class (an {@link Entity}, {@link Embeddable}, or
     * {@link MappedSuperclass}) to the configuration.
     * @param managedClass the managed class
     * @return this configuration
     */
    public PersistenceConfiguration managedClass(Class<?> managedClass) {
        managedClasses.add(managedClass);
        return this;
    }

    /**
     * @return all configured managed classes
     */
    public List<Class<?>> managedClasses() {
        return managedClasses;
    }

    /**
     * Add an {@code orm.xml} mapping file name to the configuration.
     * @param name the file path of the mapping file
     * @return this configuration
     */
    public PersistenceConfiguration mappingFile(String name) {
        mappingFileNames.add(name);
        return this;
    }

    /**
     * @return all configured mapping file names
     */
    public List<String> mappingFiles() {
        return mappingFileNames;
    }

    /**
     * Specify the transaction type for the persistence unit.
     * @param transactionType the transaction type
     * @return this configuration
     */
    public PersistenceConfiguration transactionType(PersistenceTransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    /**
     * @return the transaction type
     */
    public PersistenceTransactionType transactionType() {
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
     * @return the validation mode
     */
    public ValidationMode validationMode() {
        return validationMode;
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
     * @return the configured properties
     */
    public Map<String, Object> properties() {
        return properties;
    }
}
