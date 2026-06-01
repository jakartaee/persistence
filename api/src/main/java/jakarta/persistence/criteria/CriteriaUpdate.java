/*
 * Copyright (c) 2011, 2026 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King - 4.0
//     Linda DeMichiel - 2.1

package jakarta.persistence.criteria;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.List;

/**
 * The {@code CriteriaUpdate} interface defines functionality for
 * performing bulk update operations using the Criteria API.
 * <p>
 * For example, this criteria update marks old books as out of print:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var update = builder.createCriteriaUpdate(Book.class);
 * var book = update.from(Book.class);
 *
 * update.set(Book_.outOfPrint, true)
 *       .where(book.get(Book_.publicationDate).lessThan(cutoffDate));
 *
 * int updated = agent.createStatement(update).execute();
 * }
 *
 * <p>Criteria API bulk update operations map directly to database
 * update operations, bypassing any optimistic locking checks.
 * Portable applications using bulk update operations must manually
 * update the value of the version column, if desired, and/or manually
 * validate the value of the version column. The persistence context
 * is not automatically synchronized with the result of the bulk update.
 *
 * <p> A {@code CriteriaUpdate} object must have a single root.
 *
 * @param <T>  the entity type that is the target of the update
 *
 * @since 2.1
 */
public interface CriteriaUpdate<T> extends CriteriaStatement<T> {

    /**
     * Update the value of the specified attribute.
     * @param attribute the attribute to be updated
     * @param value the new value to set
     * @param <Y> the type of the attribute
     * @param <X> the type of the value
     * @return the modified update query
     */
    @Nonnull
    <Y, X extends Y> CriteriaUpdate<T> set(@Nonnull SingularAttribute<? super T, Y> attribute,
                                           X value);

    /**
     * Update the value of the specified attribute.
     * @param attribute the attribute to be updated
     * @param value the new value to set
     * @param <Y> the type of the attribute
     * @return the modified update query
     */
    @Nonnull
    <Y> CriteriaUpdate<T> set(@Nonnull SingularAttribute<? super T, Y> attribute,
                              @Nonnull Expression<? extends Y> value);

    /**
     * Update the value of the specified attribute.
     * @param attribute the attribute to be updated
     * @param value the new value to set
     * @param <Y> the type of the attribute
     * @param <X> the type of the value
     * @return the modified update query
     */
    @Nonnull
    <Y, X extends Y> CriteriaUpdate<T> set(@Nonnull Path<Y> attribute,
                                           X value);

    /**
     * Update the value of the specified attribute.
     * @param attribute the attribute to be updated
     * @param value the new value to set
     * @param <Y> the type of the attribute
     * @return the modified update query
     */
    @Nonnull
    <Y> CriteriaUpdate<T> set(@Nonnull Path<Y> attribute,
                              @Nonnull Expression<? extends Y> value);

    /**
     * Update the value of the specified attribute.
     * @param attributeName the name of the attribute to be updated
     * @param value the new value to set
     * @return the modified update query
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #set(SingularAttribute,Expression)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    CriteriaUpdate<T> set(@Nonnull String attributeName, Object value);

    /**
     * Modify the update query to restrict the target of the
     * update according to the specified boolean expression.
     * Replaces the previously added restriction(s), if any.
     * @param restriction  a simple or compound boolean expression
     * @return the modified update query
     */
    @Nonnull
    CriteriaUpdate<T> where(@Nonnull Expression<Boolean> restriction);

    /**
     * Modify the update query to restrict the target of the
     * update according to the conjunction of the specified
     * restriction predicates.
     * Replaces the previously added restriction(s), if any.
     * If no restrictions are specified, any previously added
     * restrictions are simply removed.
     * @param restrictions  zero or more restriction predicates
     * @return the modified update query
     */
    @Nonnull
    CriteriaUpdate<T> where(@Nonnull BooleanExpression... restrictions);

    /**
     * Modify the update query to restrict the target of the
     * update according to the conjunction of the specified
     * restriction predicates.
     * Replaces the previously added restriction(s), if any.
     * If no restrictions are specified, any previously added
     * restrictions are simply removed.
     * @param restrictions  zero or more restriction predicates
     * @return the modified update query
     * @since 4.0
     */
    @Nonnull
    CriteriaUpdate<T> where(@Nonnull List<? extends Expression<Boolean>> restrictions);
}
