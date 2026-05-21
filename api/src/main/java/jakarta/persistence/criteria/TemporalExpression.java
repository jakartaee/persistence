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

import jakarta.annotation.Nonnull;
import java.time.temporal.Temporal;

/**
 * Type for temporal query expressions.
 *
 * @param <T> the temporal type of the expression
 *
 * @since 4.0
 */
public interface TemporalExpression<T extends Temporal & Comparable<? super T>>
		extends ComparableExpression<T> {

	/**
	 * Create an expression that returns the value of a
	 * field extracted from this date.
	 * @param field a temporal field type
	 * @param <N> the type of the extracted value
	 * @return expression for the value of the extracted field
	 * @see CriteriaBuilder#extract(TemporalField, Expression)
	 */
	@Nonnull
	<N extends Number & Comparable<N>> NumericExpression<N> extract(@Nonnull TemporalField<N, T> field);

	// overrides

	@Override
	@Nonnull
	TemporalExpression<T> coalesce(T y);

	@Override
	@Nonnull
	TemporalExpression<T> coalesce(@Nonnull Expression<? extends T> y);

	@Override
	@Nonnull
	TemporalExpression<T> nullif(T y);

	@Override
    @Nonnull
	TemporalExpression<T> nullif(@Nonnull Expression<? extends T> y);

	/**
	 * Synonym for {@link #lessThan(Expression)}.
	 * @param y expression
	 * @return before predicate
	 */
    @Nonnull
    default Predicate before(@Nonnull Expression<? extends T> y) {
		return lessThan(y);
	}

	/**
	 * Synonym for {@link #lessThan(Comparable)}.
	 * @param y value
	 * @return before predicate
	 */
    @Nonnull
    default Predicate before(@Nonnull T y) {
		return lessThan(y);
	}

	/**
	 * Synonym for {@link #greaterThan(Expression)}.
	 * @param y expression
	 * @return after predicate
	 */
    @Nonnull
    default Predicate after(@Nonnull Expression<? extends T> y) {
		return greaterThan(y);
	}

	/**
	 * Synonym for {@link #greaterThan(Comparable)}.
	 * @param y value
	 * @return after predicate
	 */
    @Nonnull
    default Predicate after(@Nonnull T y) {
		return greaterThan(y);
	}
}
