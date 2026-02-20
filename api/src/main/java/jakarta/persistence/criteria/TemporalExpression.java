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

import java.time.temporal.Temporal;

/**
 * Type for temporal query expressions.
 *
 * @since 4.0
 */
public interface TemporalExpression<T extends Temporal & Comparable<? super T>>
		extends ComparableExpression<T> {

	/**
	 * Create an expression that returns the value of a
	 * field extracted from this date.
	 * @param field a temporal field type
	 * @return expression for the value of the extracted field
	 * @see CriteriaBuilder#extract(TemporalField, Expression)
	 */
	<N extends Number & Comparable<N>> NumericExpression<N> extract(TemporalField<N, T> field);

	// overrides

	@Override
	TemporalExpression<T> coalesce(T y);

	@Override
	TemporalExpression<T> coalesce(Expression<? extends T> y);

	@Override
	TemporalExpression<T> nullif(T y);

	@Override
	TemporalExpression<T> nullif(Expression<? extends T> y);
}
