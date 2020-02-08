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
//     Petros Splinakis - 2.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** 
 * Specifies a static, named query in the Jakarta Persistence query language.
 * Query names are scoped to the persistence unit.
 * The <code>NamedQuery</code> annotation can be applied to an entity or mapped superclass.
 *
 * <p> The following is an example of the definition of a named query 
 * in the Jakarta Persistence query language:
 *
 * <pre>
 *    &#064;NamedQuery(
 *            name="findAllCustomersWithName",
 *            query="SELECT c FROM Customer c WHERE c.name LIKE :custName"
 *    )
 * </pre>
 *
 * <p> The following is an example of the use of a named query:
 *
 * <pre>
 *    &#064;PersistenceContext
 *    public EntityManager em;
 *    ...
 *    customers = em.createNamedQuery("findAllCustomersWithName")
 *            .setParameter("custName", "Smith")
 *            .getResultList();
 * </pre>
 *
 * @since 1.0
 */
@Repeatable(NamedQueries.class)
@Target({TYPE}) 
@Retention(RUNTIME)
public @interface NamedQuery {

    /** 
     * (Required) The name used to refer to the query with the {@link EntityManager} 
     * methods that create query objects. 
     */
    String name();

    /** (Required) 
     * The query string in the Jakarta Persistence query language.
     */
    String query();

    /** 
     * (Optional) The lock mode type to use in query execution.  If a <code>lockMode</code>
     * other than <code>LockModeType.NONE</code> is specified, the query must be executed in
     * a transaction and the persistence context joined to the transaction.
     * @since 2.0
     */
    LockModeType lockMode() default LockModeType.NONE;
    
    /** (Optional) Query properties and hints.  May include
     * vendor-specific query hints. 
     */
    QueryHint[] hints() default {};
}
