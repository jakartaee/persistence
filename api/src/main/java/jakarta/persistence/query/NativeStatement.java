/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
//     Gavin King       - 4.0

package jakarta.persistence.query;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a statement expressed in the native SQL dialect of the
 * database. The SQL statement must return a row count. A SQL query that
 * returns a result set must be declared using {@link NativeQuery}.
 * This annotation may be applied to:
 * <ul>
 * <li>any method of any class or interface belonging to the persistence
 *     unit, or
 * <li>a query method of a Jakarta Data repository with an implementation
 *     backed by Jakarta Persistence.
 * </ul>
 * <p> The return type of the method must be {@code void}, {@code int},
 * or {@code long}. The parameter types of the method must agree with the
 * types of the statement parameters. It is not usually possible to
 * determine if the parameter types agree without executing the native
 * SQL statement.
 *
 * <p>
 * {@snippet :
 * interface Library {
 *     @NativeStatement("delete from documents"
 *             + " where id in (select document_id from trash)")
 *     @StatementOptions(timeout = 30_000)
 *     int emptyTrash();
 * }
 * }
 *
 * <p> A method parameter type <em>agrees</em> with a statement parameter
 * type if it is a type that could be assigned to the corresponding
 * statement parameter by passing an instance of the type to
 * {@code setParameter()}. In making this determination, method parameters
 * are correlated with statement parameters by position or by name,
 * depending on whether the statement uses positional or named parameters.
 *
 * <p> The parameter types of the annotated method must be basic types.
 * If this annotation occurs on a generic method, on a method with a
 * parameter type which is not a basic type, or on a method with a
 * parameter type involving a type variable, the behavior is undefined.
 *
 * <p> Any implementation of Jakarta Data backed by Jakarta Persistence
 * must support the use of this annotation as a Jakarta Data query
 * annotation. Thus, a repository method annotated {@code @NativeStatement}
 * is treated by the Jakarta Data provider as a query method, and an
 * implementation of the method is supplied by the Jakarta Data provider.
 *
 * <p> When this annotation is applied to a method of a class or interface
 * belonging to the persistence unit, a reference to a statement declared
 * using this annotation may be obtained from the static metamodel class
 * of the annotated class or interface.
 * {@snippet :
 * int deleted =
 *         em.createStatement(Library_.emptyTrash())
 *           .execute();
 * }
 *
 * <p> In addition, the statement is treated as a named statement, where
 * the statement name agrees with the convention for Javadoc links, that
 * is: the concatenation of the fully qualified name of the type, with
 * the string {@code #}, the name of the annotated member, and the raw
 * types of the method parameters, enclosed in parentheses and separated
 * by commas, for example, {@code "org.example.Library#emptyTrash()"}.
 *
 * @see NativeQuery
 * @see StatementOptions
 *
 * @since 4.0
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface NativeStatement {
    /**
     * The statement string in the native SQL dialect of the database.
     */
    String value();
}
