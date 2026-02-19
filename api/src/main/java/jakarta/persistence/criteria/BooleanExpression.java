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


package jakarta.persistence.criteria;

/**
 * Type for boolean query expressions.
 *
 * @since 4.0
 */
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

	// overrides

	@Override
	BooleanExpression coalesce(Boolean y);

	@Override
	BooleanExpression coalesce(Expression<? extends Boolean> y);

	@Override
	BooleanExpression nullif(Boolean y);

	@Override
	BooleanExpression nullif(Expression<? extends Boolean> y);
}
