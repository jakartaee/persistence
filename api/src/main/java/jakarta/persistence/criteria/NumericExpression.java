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

import jakarta.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Type for number query expressions.
 *
 * @param <N> the type of the expression
 *
 * @since 4.0
 */
public interface NumericExpression<N extends Number & Comparable<N>>
        extends ComparableExpression<N> {

    // comparisons

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  expression
     * @return greater-than predicate
     * @see CriteriaBuilder#gt(Expression, Expression)
     */
    @Nonnull
    Predicate gt(@Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  value
     * @return greater-than predicate
     * @see CriteriaBuilder#gt(Expression, Number)
     */
    @Nonnull
    Predicate gt(Number y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  expression
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#ge(Expression, Expression)
     */
    @Nonnull
    Predicate ge(@Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  value
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#ge(Expression, Number)
     */
    @Nonnull
    Predicate ge(Number y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  expression
     * @return less-than predicate
     * @see CriteriaBuilder#lt(Expression, Expression)
     */
    @Nonnull
    Predicate lt(@Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  value
     * @return less-than predicate
     * @see CriteriaBuilder#lt(Expression, Number)
     */
    @Nonnull
    Predicate lt(Number y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  expression
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#le(Expression, Expression)
     */
    @Nonnull
    Predicate le(@Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  value
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#le(Expression, Number)
     */
    @Nonnull
    Predicate le(Number y);

    // numeric functions

    /**
     * Create an expression that returns the sign of this
     * expression, that is, {@code 1} if this expression is
     * positive, {@code -1} if this expression is negative,
     * or {@code 0} if this expression is exactly zero.
     * @return sign
     * @see CriteriaBuilder#sign(Expression)
     */
    @Nonnull
    NumericExpression<Integer> sign();

    /**
     * Create an expression that returns the arithmetic negation
     * of this expression.
     * @return arithmetic negation
     * @see CriteriaBuilder#neg(Expression)
     */
    @Nonnull
    NumericExpression<N> negated();

    /**
     * Create an expression that returns the absolute value
     * of this expression.
     * @return absolute value
     * @see CriteriaBuilder#abs(Expression)
     */
    @Nonnull
    NumericExpression<N> abs();

    /**
     * Create an expression that returns the ceiling of this
     * expression, that is, the smallest integer greater than
     * or equal to this expression.
     * @return ceiling
     * @see CriteriaBuilder#ceiling(Expression)
     */
    @Nonnull
    NumericExpression<N> ceiling();

    /**
     * Create an expression that returns the floor of this
     * expression, that is, the largest integer smaller than
     * or equal to this expression.
     * @return floor
     * @see CriteriaBuilder#floor(Expression)
     */
    @Nonnull
    NumericExpression<N> floor();

    // arithmetic operations

    /**
     * Create an expression that returns the sum
     * of this expression and its argument.
     * @param y expression
     * @return sum
     * @see CriteriaBuilder#sum(Expression, Expression)
     */
    @Nonnull
    NumericExpression<N> plus(@Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the sum
     * of this expression and its argument.
     * @param y value
     * @return sum
     * @see CriteriaBuilder#sum(Expression, Number)
     */
    @Nonnull
    NumericExpression<N> plus(N y);

    /**
     * Create an expression that returns the product
     * of this expression multiplied by the argument.
     * @param y expression
     * @return product
     * @see CriteriaBuilder#prod(Expression, Expression)
     */
    @Nonnull
    NumericExpression<N> times(@Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the product
     * of this expression multiplied by the argument.
     * @param y value
     * @return product
     * @see CriteriaBuilder#prod(Expression, Number)
     */
    @Nonnull
    NumericExpression<N> times(N y);

    /**
     * Create an expression that returns the difference
     * between this expression subtracted by the argument.
     * @param y expression
     * @return difference
     * @see CriteriaBuilder#diff(Expression, Expression)
     */
    @Nonnull
    NumericExpression<N> minus(@Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the difference
     * between this expression subtracted by the argument.
     * @param y value
     * @return difference
     * @see CriteriaBuilder#diff(Expression, Number)
     */
    @Nonnull
    NumericExpression<N> minus(N y);

    /**
     * Create an expression that returns the quotient
     * of this expression divided by the argument.
     * @param y expression
     * @return quotient
     * @see CriteriaBuilder#quot(Expression, Expression)
     */
    @Nonnull
    NumericExpression<N> dividedBy(@Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the quotient
     * of this expression divided by the argument.
     * @param y value
     * @return quotient
     * @see CriteriaBuilder#quot(Expression, Number)
     */
    @Nonnull
    NumericExpression<N> dividedBy(N y);

    /**
     * Create an expression that returns the difference
     * between this expression subtracted from the argument.
     * @param y value
     * @return difference
     */
    @Nonnull
    NumericExpression<N> subtractedFrom(N y);

    /**
     * Create an expression that returns the quotient
     * of this expression dividing the argument.
     * @param y value
     * @return quotient
     */
    @Nonnull
    NumericExpression<N> dividedInto(N y);

    // floating point functions

    /**
     * Create an expression that returns the square root
     * of this expression.
     * @return square root
     * @see CriteriaBuilder#sqrt(Expression)
     */
    @Nonnull
    NumericExpression<Double> sqrt();

    /**
     * Create an expression that returns the exponential
     * of this expression, that is, Euler's number <i>e</i>
     * raised to the power of its argument.
     * @return exponential
     * @see CriteriaBuilder#exp(Expression)
     */
    @Nonnull
    NumericExpression<Double> exp();

    /**
     * Create an expression that returns the natural logarithm
     * of this expression.
     * @return natural logarithm
     * @see CriteriaBuilder#ln(Expression)
     */
    @Nonnull
    NumericExpression<Double> ln();

    /**
     * Create an expression that returns this expression
     * raised to the power of its first argument.
     * @param y exponent
     * @return the base raised to the power of the exponent
     * @see CriteriaBuilder#power(Expression, Expression)
     */
    @Nonnull
    NumericExpression<Double> power(@Nonnull Expression<? extends Number> y);

    /**
     * Create an expression that returns this number expression
     * raised to the power of its first argument.
     * @param y exponent
     * @return the base raised to the power of the exponent
     * @see CriteriaBuilder#power(Expression, Number)
     */
    @Nonnull
    NumericExpression<Double> power(Number y);

    /**
     * Create an expression that returns this number expression
     * rounded to the number of decimal places given by the
     * first argument.
     * @param n number of decimal places
     * @return the rounded value
     * @see CriteriaBuilder#round(Expression, Integer)
     */
    @Nonnull
    NumericExpression<N> round(@Nonnull Integer n);

    // aggregate functions

    /**
     * Create an aggregate expression applying the avg operation.
     * @return avg expression
     * @see CriteriaBuilder#avg(Expression)
     */
    @Nonnull
    NumericExpression<Double> avg();

    /**
     * Create an aggregate expression applying the sum operation.
     * @return sum expression
     * @see CriteriaBuilder#sum(Expression)
     */
    @Nonnull
    NumericExpression<N> sum();

    /**
     * Create an aggregate expression applying the sum operation,
     * returning a Long result.
     * @return sum expression
     * @see CriteriaBuilder#sumAsLong(Expression)
     */
    @Nonnull
    NumericExpression<Long> sumAsLong();

    /**
     * Create an aggregate expression applying the sum operation,
     * returning a Double result.
     * @return sum expression
     * @see CriteriaBuilder#sumAsDouble(Expression)
     */
    @Nonnull
    NumericExpression<Double> sumAsDouble();

    /**
     * Create an aggregate expression applying the numerical max
     * operation.
     * @return max expression
     * @see CriteriaBuilder#max(Expression)
     */
    @Override
    @Nonnull
    NumericExpression<N> max();

    /**
     * Create an aggregate expression applying the numerical min
     * operation.
     * @return min expression
     * @see CriteriaBuilder#min(Expression)
     */
    @Override
    @Nonnull
    NumericExpression<N> min();

    // typecasts

    /**
     * Typecast.
     * @return Expression&#060;Long&#062;
     * @see CriteriaBuilder#toLong(Expression)
     */
    @Nonnull
    NumericExpression<Long> toLong();

    /**
     * Typecast.
     * @return Expression&#060;Integer&#062;
     * @see CriteriaBuilder#toInteger(Expression)
     */
    @Nonnull
    NumericExpression<Integer> toInteger();

    /**
     * Typecast.
     * @return Expression&#060;Float&#062;
     * @see CriteriaBuilder#toFloat(Expression)
     */
    @Nonnull
    NumericExpression<Float> toFloat();

    /**
     * Typecast.
     * @return Expression&#060;Double&#062;
     * @see CriteriaBuilder#toDouble(Expression)
     */
    @Nonnull
    NumericExpression<Double> toDouble();

    /**
     * Typecast.
     * @return Expression&#060;BigDecimal&#062;
     * @see CriteriaBuilder#toBigDecimal(Expression)
     */
    @Nonnull
    NumericExpression<BigDecimal> toBigDecimal();

    /**
     * Typecast.
     * @return Expression&#060;BigInteger&#062;
     * @see CriteriaBuilder#toBigInteger(Expression)
     */
    @Nonnull
    NumericExpression<BigInteger> toBigInteger();

    // overrides

    @Override
    @Nonnull
    NumericExpression<N> coalesce(N y);

    @Override
    @Nonnull
    NumericExpression<N> coalesce(@Nonnull Expression<? extends N> y);

    @Override
    @Nonnull
    NumericExpression<N> nullif(N y);

    @Override
    @Nonnull
    NumericExpression<N> nullif(@Nonnull Expression<? extends N> y);
}
