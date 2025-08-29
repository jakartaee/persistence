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
 * Type for boolean query expressions.
 *
 * @since 3.2
 */
@SuppressWarnings("hiding")
public interface BooleanExpression extends ComparableExpression<Boolean> {

	//boolean functions:

	/**
	 * Create a conjunction of this and the given boolean expressions.
	 * @param y  boolean expression
	 * @return and predicate
	 * @see CriteriaBuilder#and(Expression, Expression)
	 */
	Predicate and(Expression<Boolean> y);

	/**
	 * Create a disjunction of this and the given boolean expressions.
	 * @param y  boolean expression
	 * @return or predicate
	 * @see CriteriaBuilder#or(Expression, Expression)
	 */
	Predicate or(Expression<Boolean> y);

	/**
	 * Create a negation of this restriction.
	 * @return not predicate
	 * @see CriteriaBuilder#not(Expression)
	 */
	Predicate not();


	//turn Expression<Boolean> into a Predicate
	//useful for use with varargs methods

	/**
	 * Create a predicate testing for a true value.
	 * @return predicate
	 * @see CriteriaBuilder#isTrue(Expression)
	 */
	Predicate isTrue();

	/**
	 * Create a predicate testing for a false value.
	 * @return predicate
	 * @see CriteriaBuilder#isFalse(Expression)
	 */
	Predicate isFalse();
}
