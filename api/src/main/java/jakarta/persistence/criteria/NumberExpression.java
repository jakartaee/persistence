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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Type for number query expressions.
 *
 * @param <X> the type of the expression
 *
 * @since 3.2
 */
@SuppressWarnings("hiding")
public interface NumberExpression<X extends Number> extends Expression<X> {

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  expression
     * @return greater-than predicate
     * @see CriteriaBuilder#gt(Expression, Expression)
     */
    Predicate gt(Expression<? extends X> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than the first argument.
     * @param y  value
     * @return greater-than predicate
     * @see CriteriaBuilder#gt(Expression, Number)
     */
    Predicate gt(X y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  expression
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#ge(Expression, Expression)
     */
    Predicate ge(Expression<? extends X> y);

    /**
     * Create a predicate for testing whether this expression is
     * greater than or equal to the first argument.
     * @param y  value
     * @return greater-than-or-equal predicate
     * @see CriteriaBuilder#ge(Expression, Number)
     */
    Predicate ge(X y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  expression
     * @return less-than predicate
     * @see CriteriaBuilder#lt(Expression, Expression)
     */
    Predicate lt(Expression<? extends X> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than the first argument.
     * @param y  value
     * @return less-than predicate
     * @see CriteriaBuilder#lt(Expression, Number)
     */
    Predicate lt(X y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  expression
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#le(Expression, Expression)
     */
    Predicate le(Expression<? extends X> y);

    /**
     * Create a predicate for testing whether this expression is
     * less than or equal to the first argument.
     * @param y  value
     * @return less-than-or-equal predicate
     * @see CriteriaBuilder#le(Expression, Number)
     */
    Predicate le(X y);

    /**
     * Create a predicate for testing whether this expression is
     * between the first and second arguments in value.
     * @param x  expression
     * @param y  expression
     * @return between predicate
     * @see CriteriaBuilder#between(Expression, Expression, Expression)
     */
    Predicate between(Expression<? extends X> x, Expression<? extends X> y);

    /**
     * Create a predicate for testing whether this expression is
     * between the first and second arguments in value.
     * @param x  value
     * @param y  value
     * @return between predicate
     * @see CriteriaBuilder#between(Expression, Comparable, Comparable)
     */
    Predicate between(X x, X y);


    /**
     * Create an expression that returns the sign of this
     * expression, that is, {@code 1} if this expression is
     * positive, {@code -1} if this expression is negative,
     * or {@code 0} if this expression is exactly zero.
     * @return sign
     * @see CriteriaBuilder#sign(Expression)
     */
    NumberExpression<Integer> sign();

    /**
     * Create an expression that returns the arithmetic negation
     * of this expression.
     * @return arithmetic negation
     * @see CriteriaBuilder#neg(Expression)
     */
    NumberExpression<X> neg();

    /**
     * Create an expression that returns the absolute value
     * of this expression.
     * @return absolute value
     * @see CriteriaBuilder#abs(Expression)
     */
    NumberExpression<X> abs();

    /**
     * Create an expression that returns the ceiling of this
     * expression, that is, the smallest integer greater than
     * or equal to this expression.
     * @return ceiling
     * @see CriteriaBuilder#ceiling(Expression)
     */
    NumberExpression<X> ceiling();

    /**
     * Create an expression that returns the floor of this
     * expression, that is, the largest integer smaller than
     * or equal to this expression.
     * @return floor
     * @see CriteriaBuilder#floor(Expression)
     */
    NumberExpression<X> floor();

    /**
     * Create an expression that returns the sum
     * of this expression and its argument.
     * @param y expression
     * @return sum
     * @see CriteriaBuilder#sum(Expression, Expression)
     */
    NumberExpression<X> add(Expression<? extends X> y);

    /**
     * Create an expression that returns the sum
     * of this expression and its argument.
     * @param y value
     * @return sum
     * @see CriteriaBuilder#sum(Expression, Number)
     */
    NumberExpression<X> add(X y);

    /**
     * Create an expression that returns the product
     * of this expression multiplied by the argument.
     * @param y expression
     * @return product
     * @see CriteriaBuilder#prod(Expression, Expression)
     */
    NumberExpression<X> multiply(Expression<? extends X> y);

    /**
     * Create an expression that returns the product
     * of this expression multiplied by the argument.
     * @param y value
     * @return product
     * @see CriteriaBuilder#prod(Expression, Number)
     */
    NumberExpression<X> multiply(X y);


    /**
     * Create an expression that returns the difference
     * between this expression subtracted by the argument.
     * @param y expression
     * @return difference
     * @see CriteriaBuilder#diff(Expression, Expression)
     */
    NumberExpression<X> subtract(Expression<? extends X> y);

    /**
     * Create an expression that returns the difference
     * between this expression subtracted by the argument.
     * @param y value
     * @return difference
     * @see CriteriaBuilder#diff(Expression, Number)
     */
    NumberExpression<X> subtract(X y);

    /**
     * Create an expression that returns the quotient
     * of this expression divided by the argument.
     * @param y expression
     * @return quotient
     * @see CriteriaBuilder#quot(Expression, Expression)
     */
    NumberExpression<Number> divide(Expression<? extends Number> y);

    /**
     * Create an expression that returns the quotient
     * of this expression divided by the argument.
     * @param y value
     * @return quotient
     * @see CriteriaBuilder#quot(Expression, Number)
     */
    NumberExpression<Number> divide(Number y);

    /**
     * Create an expression that returns the modulus
     * (remainder under integer division) of this
     * expression divided by the argument.
     * @param y expression
     * @return modulus
     * @see CriteriaBuilder#mod(Expression, Expression)
     */
    NumberExpression<Integer> mod(Expression<Integer> y);

    /**
     * Create an expression that returns the modulus
     * (remainder under integer division) of this
     * expression divided by the argument.
     * @param y value
     * @return modulus
     * @see CriteriaBuilder#mod(Expression, Integer)
     */
    NumberExpression<Integer> mod(Integer y);

    /**
     * Create an expression that returns the square root
     * of this expression.
     * @return square root
     * @see CriteriaBuilder#sqrt(Expression)
     */
    NumberExpression<Double> sqrt();

    /**
     * Create an expression that returns the exponential
     * of this expression, that is, Euler's number <i>e</i>
     * raised to the power of its argument.
     * @return exponential
     * @see CriteriaBuilder#exp(Expression)
     */
    NumberExpression<Double> exp();

    /**
     * Create an expression that returns the natural logarithm
     * of this expression.
     * @return natural logarithm
     * @see CriteriaBuilder#ln(Expression)
     */
    NumberExpression<Double> ln();

    /**
     * Create an expression that returns this expression
     * raised to the power of its first argument.
     * @param y exponent
     * @return the base raised to the power of the exponent
     * @see CriteriaBuilder#power(Expression, Expression)
     */
    NumberExpression<Double> power(Expression<? extends Number> y);

    /**
     * Create an expression that returns this number expression
     * raised to the power of its first argument.
     * @param y exponent
     * @return the base raised to the power of the exponent
     * @see CriteriaBuilder#power(Expression, Number)
     */
    NumberExpression<Double> power(Number y);

    /**
     * Create an expression that returns this number expression
     * rounded to the number of decimal places given by the
     * first argument.
     * @param n number of decimal places
     * @return the rounded value
     * @see CriteriaBuilder#round(Expression, Integer)
     */
    NumberExpression<X> round(Integer n);


    //aggregate functions:

    /**
     * Create an aggregate expression applying the avg operation.
     * @return avg expression
     * @see CriteriaBuilder#avg(Expression)
     */
    NumberExpression<Double> avg();

    /**
     * Create an aggregate expression applying the sum operation.
     * @return sum expression
     * @see CriteriaBuilder#sum(Expression)
     */
    NumberExpression<X> sum();

    /**
     * Create an aggregate expression applying the sum operation,
     * returning a Long result.
     * @return sum expression
     * @see CriteriaBuilder#sumAsLong(Expression)
     */
    NumberExpression<Long> sumAsLong();

    /**
     * Create an aggregate expression applying the sum operation,
     * returning a Double result.
     * @return sum expression
     * @see CriteriaBuilder#sumAsDouble(Expression)
     */
    NumberExpression<Double> sumAsDouble();

    /**
     * Create an aggregate expression applying the numerical max
     * operation.
     * @return max expression
     * @see CriteriaBuilder#max(Expression)
     */
    NumberExpression<X> max();

    /**
     * Create an aggregate expression applying the numerical min
     * operation.
     * @return min expression
     * @see CriteriaBuilder#min(Expression)
     */
    NumberExpression<X> min();

    //typecasts:

    /**
     * Typecast.  Returns same expression object.
     * @return Expression&#060;Long&#062;
     * @see CriteriaBuilder#toLong(Expression)
     */
    NumberExpression<Long> toLong();

    /**
     * Typecast.  Returns same expression object.
     * @return Expression&#060;Integer&#062;
     * @see CriteriaBuilder#toInteger(Expression)
     */
    NumberExpression<Integer> toInteger();

    /**
     * Typecast. Returns same expression object.
     * @return Expression&#060;Float&#062;
     * @see CriteriaBuilder#toFloat(Expression)
     */
    NumberExpression<Float> toFloat();

    /**
     * Typecast.  Returns same expression object.
     * @return Expression&#060;Double&#062;
     * @see CriteriaBuilder#toDouble(Expression)
     */
    NumberExpression<Double> toDouble();

    /**
     * Typecast.  Returns same expression object.
     * @return Expression&#060;BigDecimal&#062;
     * @see CriteriaBuilder#toBigDecimal(Expression)
     */
    NumberExpression<BigDecimal> toBigDecimal();

    /**
     * Typecast.  Returns same expression object.
     * @return Expression&#060;BigInteger&#062;
     * @see CriteriaBuilder#toBigInteger(Expression)
     */
    NumberExpression<BigInteger> toBigInteger();
}
