/*
 * Copyright (c) 2025,2026 Contributors to the Eclipse Foundation
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

import jakarta.annotation.Nonnull;

import java.util.Set;

/**
 * A reference to an executable named statement for an operation
 * that returns a row count. The named statement is declared via the
 * {@link NamedStatement} or {@link NamedNativeStatement} annotations,
 * or using {@link jakarta.persistence.query.JakartaStatement} or
 * {@link jakarta.persistence.query.NativeStatement}. An
 * instance of {@code StatementReference} is usually obtained
 * from the static metamodel of the annotated type.
 *
 * <p>In this example, a method is annotated, and the name of
 * the statement is determined by the name of the annotated
 * method:
 * {@snippet :
 * class Filer {
 *     @Inject EntityManager entityManager;
 *
 *     @JakartaStatement("delete from Record where temporary = true")
 *     int purgeTemporaryRecords() {
 *         return entityManager.createStatement(Filer_.purgeTemporaryRecords())
 *                 .execute();
 *     }
 * }
 * }
 *
 * <p>In this example, the entity class is annotated,
 * and the {@link NamedStatement} annotation explicitly specifies a
 * name:
 * {@snippet :
 * @NamedStatement(name = "updateSales",
 *             statement = "update Book set sales = ?1 where isbn = ?2")
 * @Entity class Book { .. }
 * }
 * <p>In this case the {@code StatementReference} obtained from
 * its static metamodel does not include arguments to the statement
 * parameters, and so they must be supplied via {@code setParameter}:
 * {@snippet :
 * int updated =
 *         entityManager.createStatement(Book_._updateSales_)
 *                 .setParameter(1, sales)
 *                 .setParameter(2, isbn)
 *                 .execute();
 * }
 *
 * <p>A {@code StatementReference} may include arguments to parameters
 * of the statement.
 * <ul>
 * <li>A reference representing a statement declared using an
 *     annotation of a type or field has no arguments, and so
 *     {@link #getArguments()}, {@link #getParameterNames()},
 *     and {@link #getParameterTypes()} all return {@code null}.
 * <li>A reference representing a statement declared using an
 *     annotation of a method holds information about the
 *     arguments passed to the method, making it available via
 *     {@link #getArguments()}, {@link #getParameterNames()},
 *     and {@link #getParameterTypes()}.
 * </ul>
 *
 * <p>In the Jakarta Persistence Query Language, a DELETE or
 * UPDATE statement is not a typed query and is always
 * represented by an instance of {@link StatementReference}.
 * A native SQL query is represented by an instance of
 * {@link StatementReference} if it returns a row count. A
 * Jakarta Persistence SELECT query, or a native SQL query
 * that returns a result set, is usually represented as an
 * instance of {@link TypedQueryReference}.
 *
 * @see EntityHandler#createStatement(StatementReference)
 *
 * @since 4.0
 */
public non-sealed interface StatementReference extends Reference {
    /**
     * All {@linkplain Statement.Option options} specified by
     * {@link jakarta.persistence.query.StatementOptions},
     * {@link NamedStatement}, or {@link NamedNativeStatement}, or an
     * empty set if no options were specified.
     *
     * @see Statement#addOption
     * @see jakarta.persistence.query.StatementOptions#timeout
     * @see jakarta.persistence.query.StatementOptions#flush
     */
    @Nonnull
    Set<Statement.Option> getOptions();
}
