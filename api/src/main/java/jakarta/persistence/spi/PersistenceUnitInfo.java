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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.spi;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;
import java.net.URL;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.EntityManagerFactory;

/**
 * Interface implemented by the container and used by the
 * persistence provider when creating an {@link EntityManagerFactory}.
 *
 * @since 1.0
 */
public interface PersistenceUnitInfo {
	
    /**
     * Returns the name of the persistence unit. Corresponds to the
     * <code>name</code> attribute in the <code>persistence.xml</code> file.
     * @return  the name of the persistence unit 
     */
    public String getPersistenceUnitName();

    /**
     * Returns the fully qualified name of the persistence provider
     * implementation class. Corresponds to the <code>provider</code> element in
     * the <code>persistence.xml</code> file.
     * @return  the fully qualified name of the persistence provider 
     * implementation class
     */
    public String getPersistenceProviderClassName();

    /**
     * Returns the transaction type of the entity managers created by
     * the <code>EntityManagerFactory</code>. The transaction type corresponds to
     * the <code>transaction-type</code> attribute in the <code>persistence.xml</code> file.
     * @return  transaction type of the entity managers created
     * by the EntityManagerFactory
     */
    public PersistenceUnitTransactionType getTransactionType();

    /**
     * Returns the JTA-enabled data source to be used by the
     * persistence provider. The data source corresponds to the
     * <code>jta-data-source</code> element in the <code>persistence.xml</code> file or is
     * provided at deployment or by the container.
     * @return the JTA-enabled data source to be used by the 
     * persistence provider
     */
    public DataSource getJtaDataSource();

    /**
     * Returns the non-JTA-enabled data source to be used by the
     * persistence provider for accessing data outside a JTA
     * transaction. The data source corresponds to the named
     * <code>non-jta-data-source</code> element in the <code>persistence.xml</code> file or
     * provided at deployment or by the container.
     * @return the non-JTA-enabled data source to be used by the 
     * persistence provider for accessing data outside a JTA 
     * transaction
     */
    public DataSource getNonJtaDataSource();

    /**
     * Returns the list of the names of the mapping files that the
     * persistence provider must load to determine the mappings for
     * the entity classes. The mapping files must be in the standard
     * XML mapping format, be uniquely named and be resource-loadable
     * from the application classpath.  Each mapping file name
     * corresponds to a <code>mapping-file</code> element in the
     * <code>persistence.xml</code> file.
     * @return the list of mapping file names that the persistence
     * provider must load to determine the mappings for the entity
     * classes 
     */
    public List<String> getMappingFileNames();

    /**
     * Returns a list of URLs for the jar files or exploded jar
     * file directories that the persistence provider must examine
     * for managed classes of the persistence unit. Each URL
     * corresponds to a <code>jar-file</code> element in the
     * <code>persistence.xml</code> file. A URL will either be a 
     * file: URL referring to a jar file or referring to a directory
     * that contains an exploded jar file, or some other URL from
     * which an InputStream in jar format can be obtained.
     * @return a list of URL objects referring to jar files or
     * directories 
     */
    public List<URL> getJarFileUrls();

    /**
     * Returns the URL for the jar file or directory that is the
     * root of the persistence unit. (If the persistence unit is
     * rooted in the WEB-INF/classes directory, this will be the 
     * URL of that directory.)
     * The URL will either be a file: URL referring to a jar file 
     * or referring to a directory that contains an exploded jar
     * file, or some other URL from which an InputStream in jar
     * format can be obtained.
     * @return a URL referring to a jar file or directory
     */
    public URL getPersistenceUnitRootUrl();

    /**
     * Returns the list of the names of the classes that the
     * persistence provider must add to its set of managed
     * classes. Each name corresponds to a named <code>class</code> element in the
     * <code>persistence.xml</code> file.
     * @return the list of the names of the classes that the 
     * persistence provider must add to its set of managed 
     * classes 
     */
    public List<String> getManagedClassNames();

    /**
     * Returns whether classes in the root of the persistence unit
     * that have not been explicitly listed are to be included in the
     * set of managed classes. This value corresponds to the
     * <code>exclude-unlisted-classes</code> element in the <code>persistence.xml</code> file.
     * @return whether classes in the root of the persistence
     * unit that have not been explicitly listed are to be
     * included in the set of managed classes
     */
    public boolean excludeUnlistedClasses();

    /**
     * Returns the specification of how the provider must use
     * a second-level cache for the persistence unit.
     * The result of this method corresponds to the <code>shared-cache-mode</code>
     * element in the <code>persistence.xml</code> file.
     * @return the second-level cache mode that must be used by the
     * provider for the persistence unit
     *
     * @since 2.0
     */
    public SharedCacheMode getSharedCacheMode();

    /**
     * Returns the validation mode to be used by the persistence
     * provider for the persistence unit.  The validation mode
     * corresponds to the <code>validation-mode</code> element in the
     * <code>persistence.xml</code> file.
     * @return the validation mode to be used by the 
     * persistence provider for the persistence unit
     * 
     * @since 2.0
     */
    public ValidationMode getValidationMode();

    /**
     * Returns a properties object. Each property corresponds to a
     * <code>property</code> element in the <code>persistence.xml</code> file
     * or to a property set by the container.
     * @return Properties object 
     */
    public Properties getProperties();
    
    /**
     * Returns the schema version of the <code>persistence.xml</code> file.
     * @return persistence.xml schema version
     *
     * @since 2.0
     */
    public String getPersistenceXMLSchemaVersion();

    /**
     * Returns ClassLoader that the provider may use to load any
     * classes, resources, or open URLs.
     * @return ClassLoader that the provider may use to load any 
     * classes, resources, or open URLs 
     */
    public ClassLoader getClassLoader();

    /**
     * Add a transformer supplied by the provider that will be 
     * called for every new class definition or class redefinition
     * that gets loaded by the loader returned by the
     * {@link PersistenceUnitInfo#getClassLoader} method. The transformer 
     * has no effect on the result returned by the
     * {@link PersistenceUnitInfo#getNewTempClassLoader} method.
     * Classes are only transformed once within the same classloading
     * scope, regardless of how many persistence units they may be 
     * a part of.
     * @param transformer   provider-supplied transformer that the
     * container invokes at class-(re)definition time
     */
    public void addTransformer(ClassTransformer transformer);

    /**
     * Return a new instance of a ClassLoader that the provider may
     * use to temporarily load any classes, resources, or open
     * URLs. The scope and classpath of this loader is exactly the
     * same as that of the loader returned by {@link
     * PersistenceUnitInfo#getClassLoader}. None of the classes loaded
     * by this class loader will be visible to application
     * components. The provider may only use this ClassLoader within
     * the scope of the {@link
     * PersistenceProvider#createContainerEntityManagerFactory} call.
     * @return temporary ClassLoader with same visibility as current
     * loader
     */
    public ClassLoader getNewTempClassLoader();
}
