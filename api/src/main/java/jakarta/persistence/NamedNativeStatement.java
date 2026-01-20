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
//    Steve Ebersole  - 4.0

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares a named {@linkplain Statement statement} written in
 * native SQL. Query names are scoped to the persistence unit.
 * The SQL statement must return a row count.
 *
 * <p> This annotation may be applied to any class or interface
 * belonging to the persistence unit.
 * {@snippet :
 * @NamedNativeStatement(name = "deleteNamedCustomers",
 *             statement = "delete from Customer c where c.name like :custName")
 * @Entity
 * class Customer { ... }
 * }
 * <p> A named statement may be executed by calling
 * {@link EntityManager#createNamedStatement(String)}.
 * {@snippet :
 * em.createNamedStatement("deleteNamedCustomers")
 *             .setParameter("custName", "Smith")
 *             .execute();
 * }
 *
 * @see EntityManager#createNamedStatement(String)
 * @see EntityManager#createStatement(StatementReference)
 * @see EntityManagerFactory#addNamedStatement(String, Statement)
 * @see NamedStatement
 *
 * @since 4.0
 */
@Repeatable(NamedNativeStatements.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface NamedNativeStatement {
	/**
	 * (Required) The name used to identify the query in calls to
	 * {@link EntityManager#createNamedStatement(String)}.
	 */
	String name();

	/**
	 * (Required) The statement string written in native SQL.
	 */
	String statement();

	/**
	 * (Optional) Query properties and hints. May include
	 * vendor-specific query hints.
	 * @see Query#setHint
	 */
	QueryHint[] hints() default {};
}
