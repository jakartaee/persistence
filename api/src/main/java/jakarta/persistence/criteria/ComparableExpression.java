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
//     Christian Beikov - 3.2


package jakarta.persistence.criteria;

/**
 * Type for comparable query expressions.
 *
 * @param <X> the type of the expression
 *
 * @since 3.2
 */
@SuppressWarnings("hiding")
public interface ComparableExpression<X extends Comparable<? super X>> extends Expression<X> {

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  expression
     * @return greater-than predicate
     * @see CriteriaBuilder#greaterThan(Expression, Expression)
     */
    Predicate greaterThan(Expression<? extends Comparable<? super X>> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  value
     * @return greater-than predicate
     * @see CriteriaBuilder#greaterThan(Expression, Comparable)
     */
    <Y extends Comparable<? super X>> Predicate greaterThan(Y y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  expression
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#greaterThanOrEqualTo(Expression, Expression)
     */
    Predicate greaterThanOrEqualTo(Expression<? extends Comparable<? super X>> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  value
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#greaterThanOrEqualTo(Expression, Comparable)
     */
    <Y extends Comparable<? super X>> Predicate greaterThanOrEqualTo(Y y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  expression
     * @return less-than predicate
     * @see CriteriaBuilder#lessThan(Expression, Expression)
     */
    Predicate lessThan(Expression<? extends Comparable<? super X>> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  value
     * @return less-than predicate
     * @see CriteriaBuilder#lessThan(Expression, Comparable)
     */
    <Y extends Comparable<? super X>> Predicate lessThan(Y y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  expression
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#lessThanOrEqualTo(Expression, Expression)
     */
    Predicate lessThanOrEqualTo(Expression<? extends Comparable<? super X>> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  value
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#lessThanOrEqualTo(Expression, Comparable)
     */
    <Y extends Comparable<? super X>> Predicate lessThanOrEqualTo(Y y);

    /**
     * Create a predicate for testing whether this expression is
     * between the first and second arguments in value.
     * @param x  expression
     * @param y  expression
     * @return between predicate
     * @see CriteriaBuilder#between(Expression, Expression, Expression)
     */
    Predicate between(Expression<? extends Comparable<? super X>> x, Expression<? extends Comparable<? super X>> y);

    /**
     * Create a predicate for testing whether this expression is
     * between the first and second arguments in value.
     * @param x  value
     * @param y  value
     * @return between predicate
     * @see CriteriaBuilder#between(Expression, Comparable, Comparable)
     */
    <Y extends Comparable<? super X>> Predicate between(Y x, Y y);

    //aggregate functions:

    /**
     * Create an aggregate expression for finding the greatest of
     * the values (strings, dates, etc).
     * @return greatest expression
     * @see CriteriaBuilder#greatest(Expression)
     */
    ComparableExpression<X> max();

    /**
     * Create an aggregate expression for finding the least of
     * the values (strings, dates, etc).
     * @return least expression
     * @see CriteriaBuilder#least(Expression)
     */
    ComparableExpression<X> min();
}
