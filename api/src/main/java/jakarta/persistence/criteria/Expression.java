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
//     Christian Beikov - 4.0
//     Gavin King       - 3.2
//     Linda DeMichiel  - 2.0

package jakarta.persistence.criteria;

import jakarta.persistence.criteria.CriteriaBuilder.SimpleCase;

import java.util.Collection;

/**
 * Type for query expressions.
 *
 * @param <T> the type of the expression
 *
 * @since 2.0
 */
public interface Expression<T> extends Selection<T> {

    /**
     * Create a predicate to test whether the expression is null.
     * @return predicate testing whether the expression is null
     */
    Predicate isNull();

    /**
     * Create a predicate to test whether the expression is 
     * not null.
     * @return predicate testing whether the expression is not null
     */
    Predicate isNotNull();

    /**
     * Create a predicate to test whether the expression is equal to
     * the argument.
     * @param value  expression to be tested against
     * @return predicate testing for equality
     * @since 3.2
     */
    Predicate equalTo(Expression<?> value);

    /**
     * Create a predicate to test whether the expression is equal to
     * the argument.
     * @param value  value to be tested against
     * @return predicate testing for equality
     * @since 3.2
     */
    Predicate equalTo(Object value);

    /**
     * Create a predicate to test whether the expression is unequal
     * to the argument.
     * @param value  expression to be tested against
     * @return predicate testing for inequality
     * @since 3.2
     */
    Predicate notEqualTo(Expression<?> value);

    /**
     * Create a predicate to test whether the expression is unequal
     * to the argument.
     * @param value  value to be tested against
     * @return predicate testing for inequality
     * @since 3.2
     */
    Predicate notEqualTo(Object value);

    /**
     * Create a predicate to test whether the expression is a member
     * of the argument list.
     * @param values  values to be tested against
     * @return predicate testing for membership
     */
    Predicate in(Object... values);

    /**
     * Create a predicate to test whether the expression is a member
     * of the argument list.
     * @param values  expressions to be tested against
     * @return predicate testing for membership
     */
    Predicate in(Expression<?>... values);

    /**
     * Create a predicate to test whether the expression is a member
     * of the collection.
     * @param values  collection of values to be tested against
     * @return predicate testing for membership
     */
    Predicate in(Collection<?> values);

    /**
     * Create a predicate to test whether the expression is a member
     * of the collection.
     * @param values expression corresponding to collection to be
     *        tested against
     * @return predicate testing for membership
     */
    Predicate in(Expression<Collection<?>> values);

    /**
     * Perform a typecast upon the expression, returning a new
     * expression object.
     * Unlike {@link #cast(Class)}, this method does not cause
     * type conversion: the runtime type is not changed.
     * <p><em>Warning: may result in a runtime failure.</em>
     * @param type  intended type of the expression
     * @return new expression of the given type
     * @see #cast(Class)
     */
    <X> Expression<X> as(Class<X> type);

    /**
     * Cast this expression to the specified type, returning a
     * new expression object.
     * Unlike {@link #as(Class)}, this method <em>does</em>
     * result in a runtime type conversion.
     * <p><em>Providers are required to support casting
     * scalar expressions to {@link String}, and
     * {@code String} expressions to {@link Integer},
     * {@link Long}, {@link Float}, and {@link Double}.
     * Support for typecasts between other basic types is
     * not required.</em>
     * @param type  a basic type
     * @return a scalar expression of the given basic type
     * @since 3.2
     */
    <X> Expression<X> cast(Class<X> type);

    // coalesce, nullif:

    /**
     * Create an expression that returns null if this and the argument
     * evaluate to null, and the value of the first non-null expression
     * otherwise.
     * @param y expression
     * @return coalesce expression
     * @since 4.0
     */
    Expression<T> coalesce(Expression<? extends T> y);

    /**
     * Create an expression that returns null if this and the argument
     * evaluate to null, and the value of the first non-null expression
     * otherwise.
     * @param y value
     * @return coalesce expression
     * @since 4.0
     */
    Expression<T> coalesce(T y);

    /**
     * Create an expression that tests whether this expression
     * is equal to the argument, returning null if they are
     * and the value of the first expression if they are not.
     * @param y expression
     * @return nullif expression
     * @since 4.0
     */
    Expression<T> nullif(Expression<? extends T> y);

    /**
     * Create an expression that tests whether this expression
     * is equal to the argument, returning null if they are
     * and the value of the first expression if they are not.
     * @param y value
     * @return nullif expression
     * @since 4.0
     */
    Expression<T> nullif(T y);

    //case builders:

    /**
     * Create a simple case expression to test against this expression.
     * @return simple case expression
     * @since 4.0
     */
    <R> SimpleCase<T,R> selectCase();

    //collection operations:

    /**
     * Create a predicate that tests whether this expression is
     * a member of a collection.
     * If the collection is empty, the predicate will be false.
     * @param collection expression
     * @return is-member predicate
     * @since 4.0
     */
    <C extends Collection<T>> Predicate isMember(Expression<C> collection);

    /**
     * Create a predicate that tests whether this expression is
     * not a member of a collection.
     * If the collection is empty, the predicate will be true.
     * @param collection expression
     * @return is-not-member predicate
     * @since 4.0
     */
    <C extends Collection<T>> Predicate isNotMember(Expression<C> collection);

    //aggregate functions:

    /**
     * Create an aggregate expression applying the count operation.
     * @return count expression
     * @since 4.0
     */
    NumberExpression<Long> count();

    /**
     * Create an aggregate expression applying the count distinct
     * operation.
     * @return count distinct expression
     * @since 4.0
     */
    NumberExpression<Long> countDistinct();

}
