/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.criteria;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Set;
import jakarta.persistence.metamodel.EntityType;

/**
 * The {@code AbstractQuery} interface defines functionality that is common
 * to both top-level queries and subqueries.
 * It is not intended to be used directly in query construction.
 *
 * <p> All queries must have:
 *         a set of root entities (which may in turn own joins).
 * <p> All queries may have:
 *         a conjunction of restrictions.
 *
 * @param <T>  the type of the result
 *
 * @since 2.0
 */
public interface AbstractQuery<T> extends CommonAbstractCriteria {

    /**
     * Create and add a query root corresponding to the given entity,
     * forming a cartesian product with any existing roots.
     * @param entityClass  the entity class
     * @param <X> the entity type
     * @return query root corresponding to the given entity
     */
    @Nonnull
    <X> Root<X> from(@Nonnull Class<X> entityClass);

    /**
     * Create and add a query root corresponding to the given entity,
     * forming a cartesian product with any existing roots.
     * @param entity  metamodel entity representing the entity
     *                of type X
     * @param <X> the entity type
     * @return query root corresponding to the given entity
     */
    @Nonnull
    <X> Root<X> from(@Nonnull EntityType<X> entity);

    /**
     * Modify the query to restrict the query results according
     * to the specified boolean expression.
     * Replaces the previously added restriction(s), if any.
     * @param restriction  a simple or compound boolean expression
     * @return the modified query
     */    
    @Nonnull
    AbstractQuery<T> where(@Nonnull Expression<Boolean> restriction);

    /**
     * Modify the query to restrict the query results according 
     * to the conjunction of the specified restriction predicates.
     * Replaces the previously added restriction(s), if any.
     * If no restrictions are specified, any previously added
     * restrictions are simply removed.
     * @param restrictions  zero or more restriction predicates
     * @return the modified query
     */
    @Nonnull
    AbstractQuery<T> where(@Nonnull BooleanExpression... restrictions);

    /**
     * Modify the query to restrict the query result according
     * to the conjunction of the specified restriction predicates.
     * Replaces the previously added restriction(s), if any.
     * If no restrictions are specified, any previously added
     * restrictions are simply removed.
     * @param restrictions  a list of zero or more restriction predicates
     * @return the modified query
     * @since 3.2
     */
    @Nonnull
    AbstractQuery<T> where(@Nonnull List<? extends Expression<Boolean>> restrictions);

    /**
     * Specify the expressions that are used to form groups over
     * the query results.
     * Replaces the previous specified grouping expressions, if any.
     * If no grouping expressions are specified, any previously 
     * added grouping expressions are simply removed.
     * @param grouping  zero or more grouping expressions
     * @return the modified query
     */
    @Nonnull
    AbstractQuery<T> groupBy(@Nonnull Expression<?>... grouping);

    /**
     * Specify the expressions that are used to form groups over
     * the query results.
     * Replaces the previous specified grouping expressions, if any.
     * If no grouping expressions are specified, any previously 
     * added grouping expressions are simply removed.
     * @param grouping  list of zero or more grouping expressions
     * @return the modified query
     */
    @Nonnull
    AbstractQuery<T> groupBy(@Nonnull List<Expression<?>> grouping);

    /**
     * Specify a restriction over the groups of the query.
     * Replaces the previous having restriction(s), if any.
     * @param restriction  a simple or compound boolean expression
     * @return the modified query
     */
    @Nonnull
    AbstractQuery<T> having(@Nonnull Expression<Boolean> restriction);

    /**
     * Specify restrictions over the groups of the query
     * according the conjunction of the specified restriction 
     * predicates.
     * Replaces the previously having added restriction(s), if any.
     * If no restrictions are specified, any previously added
     * restrictions are simply removed.
     * @param restrictions  zero or more restriction predicates
     * @return the modified query
     */
    @Nonnull
    AbstractQuery<T> having(@Nonnull BooleanExpression... restrictions);

    /**
     * Specify restrictions over the groups of the query
     * according the conjunction of the specified restriction
     * predicates.
     * Replaces the previously added having restriction(s), if any.
     * If no restrictions are specified, any previously added
     * restrictions are simply removed.
     * @param restrictions  a list of zero or more restriction predicates
     * @return the modified query
     * @since 3.2
     */
    @Nonnull
    AbstractQuery<T> having(@Nonnull List<? extends Expression<Boolean>> restrictions);

    /**
     * Specify whether duplicate query results are eliminated.
     * A true value will cause duplicates to be eliminated.
     * A false value will cause duplicates to be retained.
     * If distinct has not been specified, duplicate results must
     * be retained.
     * @param distinct  boolean value specifying whether duplicate
     *        results must be eliminated from the query result or
     *        whether they must be retained
     * @return the modified query
     */
    @Nonnull
    AbstractQuery<T> distinct(boolean distinct);

    /**
     * Return the query roots belonging to this {@link CriteriaQuery}
     * or {@link Subquery}, including any subquery roots defined via
     * {@linkplain Subquery#correlate(Root) correlation}. Modifying
     * the contents of the returned set does not affect the query.
     * @return the set of query roots, or an empty set if no roots
     *         have been defined
     */
    @Nonnull
    Set<Root<?>> getRoots();

    /**
     * Return the query roots belonging to this {@link CriteriaQuery}
     * or {@link Subquery}, including any subquery roots defined via
     * {@linkplain Subquery#correlate(Root) correlation}, in the same
     * order in which they were defined. If this object represents
     * a query written in the Jakarta Persistence Query Language,
     * the roots are returned in the order they occur in the query.
     * Modifying the contents of the returned list does not affect
     * the query.
     * @return a list of query roots, or an empty list if no roots
     *         have been defined
     * @since 4.0
     */
    @Nonnull
    List<Root<?>> getRootList();

    /**
     * Return the selection of the query, or null if no selection
     * has been set.
     * @return selection item 
     */
    @Nullable
    Selection<T> getSelection();

    /**
     * Return a list of the grouping expressions.  Returns empty
     * list if no grouping expressions have been specified.
     * Modifications to the list do not affect the query.
     * @return the list of grouping expressions
     */
    @Nonnull
    List<Expression<?>> getGroupList();

    /**
     * Return the predicate that corresponds to the restriction(s)
     * over the grouping items, or null if no restrictions have 
     * been specified.
     * @return having clause predicate
     */
    @Nullable
    Predicate getGroupRestriction();

    /**
     * Return whether duplicate query results must be eliminated or
     * retained.
     * @return boolean indicating whether duplicate query results 
     *         must be eliminated
     */
    boolean isDistinct();

    /**
     * Return the result type of the query or subquery. If
     * a result type was specified as an argument to the
     * {@code createQuery} or {@code subquery} method, that
     * type is returned. If the query was created using the
     * {@code createTupleQuery} method, the result type is
     * {@code Tuple}. Otherwise, the result type is
     * {@code Object}.
     * @return result type
     */
    @Nonnull
    Class<T> getResultType();  	
}
