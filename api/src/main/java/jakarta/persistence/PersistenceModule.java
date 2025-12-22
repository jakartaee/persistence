/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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

package jakarta.persistence;

import jakarta.persistence.PersistenceConfiguration.SchemaManagementAction;
import jakarta.persistence.spi.PersistenceProvider;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static jakarta.persistence.PersistenceConfiguration.SchemaManagementAction.NONE;
import static jakarta.persistence.PersistenceUnitTransactionType.JTA;
import static jakarta.persistence.SharedCacheMode.UNSPECIFIED;
import static jakarta.persistence.ValidationMode.AUTO;

/**
 * Declares that the annotated module defines a persistence unit
 * with the same name as the module itself and specifies static
 * configuration for the persistence unit.
 * <p>For example, the following annotation declares a persistence
 * unit named {@code org.example.library} with resouce-local
 * transaction management:
 * {@snippet :
 * @PersistenceModule(
 *     transactionType = RESOURCE_LOCAL,
 *     classes = {Book.class, Author.class, Publisher.class},
 *     excludeUnlistedClasses = true, // faster startup (no scanning)
 *     nonJtaDatasource = "java:/comp/env/libraryData",
 *     qualifiers = Library.class,
 *     schemaManagementDatabaseAction = VALIDATE,
 *     properties = {
 *         @PersistenceProperty(name="jakarta.persistence.jdbc.batchSize", value="20"),
 *         @PersistenceProperty(name="jakarta.persistence.jdbc.fetchSize", value="1000")
 *     }
 * )
 * module org.example.library { ... }
 * }
 * <p>
 * Configuration more specific to a given deployment of the unit
 * may be provided in a standard Java properties file located at
 * for example, {@code META-INF/org.example.library.properties},
 * where {@code org.example.library} is the name of the unit
 * (the module name). Properties settings specified in this file
 * override setting specified by this annotation.
 * <p>
 * This annotation is an alternative to declaring the persistence
 * unit by means of the {@code <persistence-unit>} element of the
 * venerable {@code META-INF/persistence.xml} file.
 * <p>
 * An entity manager factory may be created for the persistence
 * unit by passing the unit/module name to
 * {@link Persistence#createEntityManagerFactory(String)}.
 * @see Persistence#createEntityManagerFactory(String)
 * @see Persistence#createEntityManagerFactory(String,java.util.Map)
 * @see PersistenceConfiguration
 * @since 4.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.MODULE)
public @interface PersistenceModule {

    /**
     * The transaction type of the persistence unit.
     * <p>Defaults to
     * {@link PersistenceUnitTransactionType#JTA JTA}
     */
    PersistenceUnitTransactionType transactionType() default JTA;

    /**
     * An optional description of the persistence unit.
     */
    String description() default "";

    /**
     * The name of a class implementing
     * {@link PersistenceProvider}.
     */
    String provider() default "";

    /**
     * The fully qualified class name of an annotation
     * annotated {@code jakarta.inject.Scope} or
     * {@code jakarta.enterprise.context.NormalScope}.
     * This scope annotation may be used to determine
     * the scope of an injected persistence context.
     */
    Class<? extends Annotation> scope() default Annotation.class;

    /**
     * The fully qualified class names of annotations
     * annotated {@code jakarta.inject.Qualifier}.
     * These qualifier annotations may be used to
     * disambiguate an injected persistence unit.
     */
    Class<? extends Annotation>[] qualifiers() default {};

    /**
     * The JNDI name of a JTA {@code javax.sql.DataSource}.
     */
    String jtaDataSource() default "";

    /**
     * The JNDI name of a non-JTA {@code javax.sql.DataSource}.
     */
    String nonJtaDataSource() default "";

    /**
     * A list of URLs of jar files the persistence provider must
     * examine and search for managed classes of the persistence
     * unit. A URL might be a {@code file:} URL referring to a jar
     * file or referring to a directory that contains an exploded
     * jar file.
     */
    String[] jarFileUrls() default {};

    /**
     * A list of names of the mapping files the persistence
     * provider must load to determine mappings for the entity
     * classes. The mapping files must be in the standard XML
     * mapping format, be uniquely named, and be resource-loadable
     * from the application classpath.
     */
    String[] mappingFileNames() default {};

    /**
     * A list of managed classes. Each class is an {@link Entity},
     * {@link Embeddable}, {@link MappedSuperclass}, or {@link Converter}.
     */
    String[] classes() default {};

    /**
     * Determines whether classes in the root of the persistence
     * unit that have not been explicitly listed are included in
     * the set of managed classes.
     * <p>By default, they are included.
     */
    boolean excludeUnlistedClasses() default false;

    /**
     * The {@linkplain SharedCacheMode shared cache mode} of the
     * persistence unit.
     */
    SharedCacheMode sharedCacheMode() default UNSPECIFIED;

    /**
     * The {@linkplain ValidationMode validation mode} of the
     * persistence unit.
     */
    ValidationMode validationMode() default AUTO;

//    /**
//     * The {@linkplain FetchType default fetch type} for one-to-one
//     * and many-to-one associations for this persistence unit.
//     * <p>Defaults to {@link FetchType#LAZY} for a persistence unit
//     * declared via this annotation.
//     * @apiNote The default fetch type for a persistence unit
//     * declared via this annotation is the opposite of the default
//     * fetch type for a persistence unit declared in the
//     * {@code META-INF/persistence.xml} file, reflecting that the
//     * use of {@code fetch=LAZY} is strongly recommended for all
//     * newly written code.
//     */
//    FetchType defaultFetchType() default LAZY;

    /**
     * The schema management action to be performed against the database.
     */
    SchemaManagementAction schemaManagementDatabaseAction() default NONE;

    /**
     * The schema management action to be performed by generated DDL scripts.
     */
    SchemaManagementAction schemaManagementScriptsAction() default NONE;

    /**
     * Standard and vendor-specific property settings.
     * <p>These property settings may be overridden by supplying
     * additional properties in a standard Java properties file
     * located at {@code META-INF/unitName.properties}.
     */
    PersistenceProperty[] properties() default {};
}
