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


package jakarta.persistence.criteria;

import java.time.temporal.Temporal;

/**
 * Type for temporal query expressions.
 *
 * @since 4.0
 */
public interface TemporalExpression<T extends Temporal & Comparable<? super T>> extends ComparableExpression<T> {

	/**
	 * Create an expression that returns the value of a
	 * field extracted from this date.
	 * @param field a temporal field type
	 * @return expression for the value of the extracted field
	 * @see CriteriaBuilder#extract(TemporalField, Expression)
	 */
	<N extends Number & Comparable<N>> NumericExpression<N> extract(TemporalField<N, T> field);
}
