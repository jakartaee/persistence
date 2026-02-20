/*
 * Copyright (c) 2025, 2026 Contributors to the Eclipse Foundation
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
//     Christian Beikov - 4.0
//     Gavin King       - 4.0


package jakarta.persistence.criteria;

/**
 * Type for comparable query expressions.
 *
 * @param <C> the type of the expression
 *
 * @since 4.0
 */
public interface ComparableExpression<C extends Comparable<? super C>>
        extends Expression<C> {

    // comparisons

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  expression
     * @return greater-than predicate
     * @see CriteriaBuilder#greaterThan(Expression, Expression)
     */
    Predicate greaterThan(Expression<? extends C> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  value
     * @return greater-than predicate
     * @see CriteriaBuilder#greaterThan(Expression, Comparable)
     */
    Predicate greaterThan(C y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  expression
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#greaterThanOrEqualTo(Expression, Expression)
     */
    Predicate greaterThanOrEqualTo(Expression<? extends C> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  value
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#greaterThanOrEqualTo(Expression, Comparable)
     */
    Predicate greaterThanOrEqualTo(C y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  expression
     * @return less-than predicate
     * @see CriteriaBuilder#lessThan(Expression, Expression)
     */
    Predicate lessThan(Expression<? extends C> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  value
     * @return less-than predicate
     * @see CriteriaBuilder#lessThan(Expression, Comparable)
     */
    Predicate lessThan(C y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  expression
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#lessThanOrEqualTo(Expression, Expression)
     */
    Predicate lessThanOrEqualTo(Expression<? extends C> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  value
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#lessThanOrEqualTo(Expression, Comparable)
     */
    Predicate lessThanOrEqualTo(C y);

    /**
     * Create a predicate for testing whether this expression is
     * between the first and second arguments in value.
     * @param x  expression
     * @param y  expression
     * @return between predicate
     * @see CriteriaBuilder#between(Expression, Expression, Expression)
     */
    Predicate between(Expression<? extends C> x, Expression<? extends C> y);

    /**
     * Create a predicate for testing whether this expression is
     * between the first and second arguments in value.
     * @param x  value
     * @param y  value
     * @return between predicate
     * @see CriteriaBuilder#between(Expression, Comparable, Comparable)
     */
    Predicate between(C x, C y);

    //aggregate functions

    /**
     * Create an aggregate expression for finding the greatest of
     * the values (strings, dates, etc).
     * @return greatest expression
     * @see CriteriaBuilder#greatest(Expression)
     */
    ComparableExpression<C> max();

    /**
     * Create an aggregate expression for finding the least of
     * the values (strings, dates, etc).
     * @return least expression
     * @see CriteriaBuilder#least(Expression)
     */
    ComparableExpression<C> min();

    //sorting

    /**
     * Create an ordering by the ascending value of this expression.
     * @return ascending ordering corresponding to this expression
     * @since 4.0
     */
    Order asc();

    /**
     * Create an ordering by the descending value of this expression.
     * @return descending ordering corresponding to this expression
     */
    Order desc();

    /**
     * Create an ordering by the ascending value of this expression.
     * @param nullPrecedence  the precedence of null values
     * @return ascending ordering corresponding to this expression
     */
    Order asc(Nulls nullPrecedence);

    /**
     * Create an ordering by the descending value of this expression.
     * @param nullPrecedence  the precedence of null values
     * @return descending ordering corresponding to this expression
     */
    Order desc(Nulls nullPrecedence);

    // overrides

    @Override
    ComparableExpression<C> coalesce(C y);

    @Override
    ComparableExpression<C> coalesce(Expression<? extends C> y);

    @Override
    ComparableExpression<C> nullif(C y);

    @Override
    ComparableExpression<C> nullif(Expression<? extends C> y);
}
