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
 * Type for collection query expressions.
 *
 * @since 4.0
 */
public interface PluralExpression<C, E> extends Expression<C> {

	//collection operations:

	/**
	 * Create a predicate that tests whether this collection is empty.
	 * @return is-empty predicate
	 * @see CriteriaBuilder#isEmpty(Expression)
	 */
	Predicate isEmpty();

	/**
	 * Create a predicate that tests whether this collection is
	 * not empty.
	 * @return is-not-empty predicate
	 * @see CriteriaBuilder#isNotEmpty(Expression)
	 */
	Predicate isNotEmpty();

	/**
	 * Create an expression that tests the size of this collection.
	 * @return size expression
	 * @see CriteriaBuilder#size(Expression)
	 */
	NumericExpression<Integer> size();

	/**
	 * Create a predicate that tests whether an element is
	 * a member of this collection.
	 * If the collection is empty, the predicate will be false.
	 * @param elem element expression
	 * @return is-member predicate
	 */
	Predicate contains(Expression<? extends E> elem);

	/**
	 * Create a predicate that tests whether an element is
	 * a member of this collection.
	 * If the collection is empty, the predicate will be false.
	 * @param elem element
	 * @return is-member predicate
	 */
	Predicate contains(E elem);

	/**
	 * Create a predicate that tests whether an element is
	 * not a member of this collection.
	 * If the collection is empty, the predicate will be true.
	 * @param elem element expression
	 * @return is-not-member predicate
	 */
	Predicate notContains(Expression<? extends E> elem);

	/**
	 * Create a predicate that tests whether an element is
	 * not a member of this collection.
	 * If the collection is empty, the predicate will be true.
	 * @param elem element
	 * @return is-not-member predicate
	 */
	Predicate notContains(E elem);
}
