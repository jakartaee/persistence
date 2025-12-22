/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Petros Splinakis - 2.2
//     Linda DeMichiel - 2.1


package jakarta.persistence;

import jakarta.persistence.sql.ResultSetMapping;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;  

/**
 * Declares and names a stored procedure, its parameters, and its result type.
 *
 * <p>The {@code NamedStoredProcedureQuery} annotation can be applied to an 
 * entity or mapped superclass.
 *
 * <p>The {@link #name} element is the name that is passed as an argument
 * to the {@link EntityManager#createNamedStoredProcedureQuery} method to
 * create an executable {@link StoredProcedureQuery} object. Names are
 * scoped to the persistence unit.
 *
 * <p>The {@link #procedureName} element is the name of the stored procedure
 * in the database.
 *
 * <p>The parameters of the stored procedure are specified by the
 * {@link #parameters} element. Parameters must be specified in the order
 * in which they occur in the parameter list of the stored procedure.
 *
 * <p>The {@link #resultClasses} element refers to the class (or classes)
 * that are used to map the results. The {@link #resultSetMappings} element
 * names one or more result set mappings, as defined by the
 * {@link SqlResultSetMapping} annotation.
 *
 * <p>If there are multiple result sets, it is assumed that they are
 * mapped using the same mechanism &#8212; e.g., either all via a set of
 * result class mappings or all via a set of result set mappings. The
 * order of the specification of these mappings must be the same as the
 * order in which the result sets are returned by the stored procedure
 * invocation. If the stored procedure returns one or more result sets
 * and no {@link #resultClasses} or {@link #resultSetMappings} are
 * specified, any result set is returned as a list of type
 * {@code Object[]}. The combining of different strategies for the mapping
 * of stored procedure result sets is undefined.
 *
 * <p>The {@link #hints} element may be used to specify query properties
 * and hints. Properties defined by this specification must be observed
 * by the provider. Vendor-specific hints that are not recognized by a
 * provider must be ignored.
 *
 * <p>All parameters of a named stored procedure query must be specified
 * using the {@code StoredProcedureParameter} annotation.
 *
 * @see StoredProcedureQuery
 * @see StoredProcedureParameter
 * @see EntityHandler#createNamedStoredProcedureQuery(String)
 *
 * @since 2.1
 */
@Repeatable(NamedStoredProcedureQueries.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface NamedStoredProcedureQuery { 

    /**
     * The name used to refer to the query with the {@link EntityManager} 
     * methods that create stored procedure query objects.
     */
    String name();

    /**
     * The name of the stored procedure in the database.
     */
    String procedureName();

    /**
     * Information about all parameters of the stored procedure.
     * Parameters must be specified in the order in which they
     * occur in the parameter list of the stored procedure.
     */
    StoredProcedureParameter[] parameters() default {};

    /**
     * The result classes.
     *
     * <p>When the result classes are explicitly specified and the
     * {@linkplain #resultSetMappings result set mappings} are not, either:
     * <ul>
     * <li>the result class is an entity class and is interpreted as a
     *     managed {@linkplain EntityResult entity result} with implicit
     *     field mappings determined by the names of the columns in the
     *     result set and the object/relational mapping of the entity,
     * <li>the result class is the class of a {@linkplain Basic basic}
     *     type and the result set must have a single column which is
     *     interpreted as a {@linkplain ColumnResult scalar result},
     * <li>the result class is a non-abstract class or record type
     *     with a constructor with the same number of parameters as the
     *     result set has columns, and is interpreted as a
     *     {@linkplain ConstructorResult constructor result} including
     *     all the columns of the result set, or
     * <li>the result class is {@code Object[].class} and each query
     *     result is packaged in an array of type {@code Object[]},
     *     with the array elements corresponding by position with the
     *     columns of the select list and column values obtained
     *     according to the default type mappings defined by the JDBC
     *     specification.
     * </ul>
     *
     * <p>Otherwise, if {@linkplain #resultSetMappings result set mappings}
     * are specified, each explicitly specified result class must agree with
     * the type inferred from the corresponding result set mapping.
     *
     * <p>If the result classes are not explicitly specified, then they
     * are inferred from the result set mapping, if any, or default to
     * {@code Object} or {@code Object[]}. Any result class may be
     * overridden by explicitly passing a class object to
     * {@link StoredProcedureQuery#getResults(Class)},
     * {@link StoredProcedureQuery#getSingleResult(Class)}, or
     * {@link StoredProcedureQuery#getSingleResultOrNull(Class)}.
     */
    Class<?>[] resultClasses() default {};

    /**
     * The {@linkplain SqlResultSetMapping#name names} of one or more
     * {@linkplain SqlResultSetMapping result set mappings} declared in
     * metadata. The named result set mappings are used to interpret the
     * result sets of the stored procedure.
     *
     * <p>Any result set mapping may be overridden by explicitly passing
     * a {@link ResultSetMapping} to
     * {@link StoredProcedureQuery#getResults(ResultSetMapping)},
     * {@link StoredProcedureQuery#getSingleResult(ResultSetMapping)}, or
     * {@link StoredProcedureQuery#getSingleResultOrNull(ResultSetMapping)}.
     *
     * @see SqlResultSetMapping
     * @see ResultSetMapping
     */
    String[] resultSetMappings() default {};

    /**
     * Query properties and hints.
     * (May include vendor-specific query hints.)
     */
    QueryHint[] hints() default {};

}
