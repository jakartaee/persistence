/*
 * Copyright (c) 2008, 2026 Oracle and/or its affiliates and others. All rights reserved.
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
//     Steve Ebersole  - 4.0
//     Gavin King      - 4.0
//     Lukas Jungmann  - 3.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.spi;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;
import java.net.URL;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.PersistenceUnitTransactionType;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;

/**
 * Interface implemented by the container and used by the persistence
 * provider when creating an {@link EntityManagerFactory}.
 *
 * @apiNote This is an SPI interface forming part of the Jakarta EE
 * container / persistence provider contract. It is not intended for
 * direct use by application programs.
 *
 * @since 1.0
 */
public interface PersistenceUnitInfo {
	
    /**
     * Returns the name of the persistence unit. Corresponds to the
     * {@code name} attribute in the {@code persistence.xml} file.
     * @return the name of the persistence unit
     */
    @Nonnull
    String getPersistenceUnitName();

    /**
     * Returns the fully qualified name of the persistence provider
     * implementation class. Corresponds to the {@code provider} element
     * in the {@code persistence.xml} file.
     * @return the fully qualified name of the persistence provider
     *         implementation class
     */
    @Nullable
    String getPersistenceProviderClassName();

    /**
     * Returns the fully qualified class name of an annotation annotated
     * {@code Scope} or {@code NormalScope}. Corresponds to the {@code scope}
     * element in {@code persistence.xml}.
     * @return the fully qualified class name of the scope annotation,
     *         or null if no scope was explicitly specified
     */
    @Nullable
    String getScopeAnnotationName();

    /**
     * Returns the fully qualified class names of annotations annotated
     * {@code Qualifier}. Corresponds to the {@code qualifier} element in
     * {@code persistence.xml}.
     * @return the fully qualified class names of the qualifier annotations,
     *         or an empty list if no qualifier annotations were explicitly
     *         specified
     */
    @Nullable
    List<String> getQualifierAnnotationNames();

    /**
     * Returns the transaction type of the entity managers created by
     * the {@link EntityManagerFactory}. The transaction type corresponds
     * to the {@code transaction-type} attribute in the {@code persistence.xml}
     * file.
     * @return the transaction type of the entity managers created by
     *         the {@code EntityManagerFactory}
     */
    @Nonnull
    PersistenceUnitTransactionType getTransactionType();

    /**
     * Returns the JTA-enabled data source to be used by the
     * persistence provider. The data source corresponds to the
     * {@code jta-data-source} element in the {@code persistence.xml}
     * file or is provided at deployment or by the container.
     * @return the JTA-enabled data source to be used by the 
     *         persistence provider
     */
    @Nullable
    DataSource getJtaDataSource();

    /**
     * Returns the non-JTA-enabled data source to be used by the
     * persistence provider for accessing data outside a JTA
     * transaction. The data source corresponds to the named
     * {@code non-jta-data-source} element in the {@code persistence.xml}
     * file or provided at deployment or by the container.
     * @return the non-JTA-enabled data source to be used by the 
     *         persistence provider for accessing data outside a
     *         JTA transaction
     */
    @Nullable
    DataSource getNonJtaDataSource();

    /**
     * Returns the list of the names of the mapping files that the
     * persistence provider must load to determine the mappings for
     * the entity classes. The mapping files must be in the standard
     * XML mapping format, be uniquely named and be resource-loadable
     * from the application classpath.  Each mapping file name
     * corresponds to a {@code mapping-file} element in the
     * {@code persistence.xml} file.
     * @return the list of mapping file names that the persistence
     *         provider must load to determine the mappings for the
     *         entity classes
     */
    @Nonnull
    List<String> getMappingFileNames();

    /**
     * Returns a list of URLs for the jar files or exploded jar
     * file directories that the persistence provider must examine
     * for program elements belonging to the persistence unit. Each URL
     * corresponds to a {@code jar-file} element in the
     * {@code persistence.xml} file. A URL will either be a
     * file: URL referring to a jar file or referring to a directory
     * that contains an exploded jar file, or some other URL from
     * which an InputStream in jar format can be obtained.
     * @return a list of URL objects referring to jar files or
     *         directories
     */
    @Nonnull
    List<URL> getJarFileUrls();

    /**
     * Returns the URL for the jar file or directory that is the
     * root of the persistence unit. (If the persistence unit is
     * rooted in the WEB-INF/classes directory, this is the URL
     * of that directory.)
     * The URL will either be a file: URL referring to a jar file 
     * or referring to a directory that contains an exploded jar
     * file, or some other URL from which an InputStream in jar
     * format can be obtained.
     * @return a URL referring to a jar file or directory
     */
    @Nonnull
    URL getPersistenceUnitRootUrl();

    /**
     * Returns the names of ordinary Java types listed by {@code class}
     * elements of the {@code persistence.xml} file. Package and module
     * descriptors are not included.
     *
     * @return the class names listed in {@code persistence.xml},
     *         without duplicates
     */
    @Nonnull
    List<String> getManagedClassNames();

    /**
     * Returns the package names listed by {@code package} elements
     * of the {@code persistence.xml} file. Each name is a qualified
     * package name, for example, {@code com.example.model}, and does
     * not include the suffix {@code .package-info}.
     *
     * @return the package names listed in {@code persistence.xml},
     *         without duplicates
     * @since 4.0
     */
    @Nonnull
    List<String> getManagedPackageNames();

    /**
     * Returns the module names listed by {@code module} elements
     * of the {@code persistence.xml} file. Each name is the declared
     * JPMS module name and is not {@code module-info}.
     *
     * @return the module names listed in {@code persistence.xml},
     *         without duplicates
     * @since 4.0
     */
    @Nonnull
    List<String> getManagedModuleNames();

    /**
     * Returns the names of all ordinary compiled Java types belonging
     * to the persistence unit, including types:
     * <ul>
     * <li>named explicitly in the {@code persistence.xml}
     *     file,
     * <li>named explicitly in {@code META-INF/orm.xml} or
     *     in an XML mapping file listed in
     *     {@code persistence.xml}, or
     * <li>discovered by the container via scanning the
     *     archive containing the {@code persistence.xml}
     *     file and all archives referenced by
     *     {@code <jar-file>} elements of the persistence
     *     unit definition.
     * </ul>
     * <p>A type is discoverable via scanning if it bears
     * a {@linkplain Discoverable discoverable annotation}.
     * Discoverable types include entity classes, mapped
     * superclasses, embeddable classes, attribute converter
     * classes, and every type declaring a named query,
     * named statement, named stored procedure query, or
     * SQL result set mapping. Types bearing custom
     * discoverable annotation types are also discoverable.
     *
     * <p>Package and module descriptors are not included.
     *
     * @return the list of names of all ordinary compiled Java types
     *         belonging to the persistence unit, without duplicates
     * @see Discoverable
     * @since 4.0
     */
    @Nonnull
    List<String> getAllClassNames();

    /**
     * Returns the names of all package descriptors belonging to the
     * persistence unit, including package descriptors explicitly named
     * in {@code persistence.xml} and package descriptors discovered by
     * the container via scanning. Each name is a qualified package name,
     * for example, {@code com.example.model}, and does not include the
     * suffix {@code .package-info}.
     *
     * <p>Naming a package descriptor does not cause the ordinary Java
     * types in that package to belong to the persistence unit.
     *
     * @return the list of names of all package descriptors belonging
     *         to the persistence unit, without duplicates
     * @see Discoverable
     * @since 4.0
     */
    @Nonnull
    List<String> getAllPackageNames();

    /**
     * Returns the names of all module descriptors belonging to the
     * persistence unit, including module descriptors explicitly named
     * in {@code persistence.xml} and module descriptors discovered by
     * the container via scanning. Each name is the declared JPMS module
     * name and is not {@code module-info}.
     *
     * <p>Naming a module descriptor does not cause the ordinary Java
     * types or package descriptors in that module to belong to the
     * persistence unit.
     *
     * @return the list of names of all module descriptors belonging
     *         to the persistence unit, without duplicates
     * @see Discoverable
     * @since 4.0
     */
    @Nonnull
    List<String> getAllModuleNames();

    /**
     * Determines whether the root directory of the persistence
     * unit is scanned for program elements bearing discoverable
     * annotation types. The returned boolean value corresponds
     * to the value of the {@code exclude-unlisted-classes}
     * element in the {@code persistence.xml} file.
     * @return {@code false} if the root directory of the
     *         persistence unit is scanned
     */
    boolean excludeUnlistedClasses();

    /**
     * Returns the specification of how the provider must use
     * a second-level cache for the persistence unit.
     * The returned shared cache mode corresponds to the
     * value of the {@code shared-cache-mode} element in the
     * {@code persistence.xml} file.
     * @return the second-level cache mode that must be used
     *         by the provider for the persistence unit
     *
     * @since 2.0
     */
    @Nonnull
    SharedCacheMode getSharedCacheMode();

    /**
     * Returns the validation mode to be used by the persistence
     * provider for the persistence unit. The returned validation
     * mode corresponds to the value of the {@code validation-mode}
     * element in the {@code persistence.xml} file.
     * @return the validation mode to be used by the persistence
     *         provider for the persistence unit
     * 
     * @since 2.0
     */
    @Nonnull
    ValidationMode getValidationMode();

    /**
     * Returns the {@linkplain FetchType#DEFAULT default fetch type}
     * for one-to-one and many-to-one associations. The default fetch
     * type corresponds to the {@code default-to-one-fetch-type}
     * element in the {@code persistence.xml} file.
     * @return the default fetch type for one-to-one and many-to-one
     *         associations for the persistence unit
     *
     * @since 4.0
     */
    @Nonnull
    FetchType getDefaultToOneFetchType();

    /**
     * Returns a properties object. Each property corresponds to a
     * {@code property} element in the {@code persistence.xml} file
     * or to a property set by the container.
     * @return an instance of {@link Properties}
     */
    @Nonnull
    Properties getProperties();
    
    /**
     * Returns the schema version of the {@code persistence.xml} file.
     * @return {@code persistence.xml} schema version
     *
     * @since 2.0
     */
    @Nonnull
    String getPersistenceXMLSchemaVersion();

    /**
     * A {@link ClassLoader} that the provider may use to load
     * any classes, resources, or open URLs.
     * @return a {@code ClassLoader} that the provider may use
     *         to load classes, resources, or open URLs
     */
    @Nonnull
    ClassLoader getClassLoader();

    /**
     * Add a transformer supplied by the provider that is called for
     * every new class definition or class redefinition that gets
     * loaded by the loader returned by the
     * {@link PersistenceUnitInfo#getClassLoader} method. The
     * transformer has no effect on the result returned by the
     * {@link PersistenceUnitInfo#getNewTempClassLoader} method.
     * Only ordinary Java types returned by {@link #getAllClassNames()} are
     * eligible for transformation. The transformer must not transform any
     * other class definition, including package and module descriptors.
     * Classes are only transformed once within the same classloading
     * scope, regardless of how many persistence units they may be 
     * a part of.
     * <p>If the container previously called
     * {@link PersistenceProvider#getClassTransformer} with this
     * {@code PersistenceUnitInfo}, then this method has no effect.
     * @param transformer a provider-supplied transformer that the
     *        container invokes at class-(re)definition time
     */
    void addTransformer(@Nonnull ClassTransformer transformer);

    /**
     * Return a new instance of a {@link ClassLoader} that the provider
     * may use to temporarily load any classes, resources, or open
     * URLs. The scope and classpath of this loader is exactly the
     * same as that of the loader returned by {@link
     * PersistenceUnitInfo#getClassLoader}. None of the classes loaded
     * by this class loader are visible to application components. The
     * provider may only use this {@code ClassLoader} within the scope
     * of the {@link PersistenceProvider#createContainerEntityManagerFactory}
     * call.
     * @return a temporary {@code ClassLoader} with same visibility as
     *         current loader
     */
    @Nonnull
    ClassLoader getNewTempClassLoader();
}
