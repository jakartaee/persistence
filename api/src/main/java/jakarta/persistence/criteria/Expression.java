/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
     *  Create a predicate to test whether the expression is null.
     *  @return predicate testing whether the expression is null
     */
    Predicate isNull();

    /**
     *  Create a predicate to test whether the expression is 
     *  not null.
     *  @return predicate testing whether the expression is not null
     */
    Predicate isNotNull();

    /**
     * Create a predicate to test whether the expression is equal to
     * the argument.
     * @param value  expression to be tested against
     * @return predicate testing for equality
     */
    Predicate equalTo(Expression<?> value);

    /**
     * Create a predicate to test whether the expression is equal to
     * the argument.
     * @param value  value to be tested against
     * @return predicate testing for equality
     */
    Predicate equalTo(Object value);

    /**
     * Create a predicate to test whether the expression is unequal
     * to the argument.
     * @param value  expression to be tested against
     * @return predicate testing for inequality
     */
    Predicate notEqualTo(Expression<?> value);

    /**
     * Create a predicate to test whether the expression is unequal
     * to the argument.
     * @param value  value to be tested against
     * @return predicate testing for inequality
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
     * This method does not cause type conversion:
     * the runtime type is not changed.
     * Warning: may result in a runtime failure.
     * @param type  intended type of the expression
     * @return new expression of the given type
     */
    <X> Expression<X> as(Class<X> type);
}
