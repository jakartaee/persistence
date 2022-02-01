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

package jakarta.persistence.criteria;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Each instance represents a type of field which can be
 * extracted from a {@link LocalDateTime}.
 *
 * @param <N> the resulting type of the extracted value
 *
 * @since 3.2
 */
public class LocalDateTimeField<N> implements TemporalField<N, LocalDateTime> {
	/**
	 * The calendar year.
	 */
	public static final LocalDateTimeField<Integer> YEAR = new LocalDateTimeField<>();
	/**
	 * The calendar quarter, numbered from 1 to 4.
	 */
	public static final LocalDateTimeField<Integer> QUARTER = new LocalDateTimeField<>();
	/**
	 * The calendar month of the year, numbered from 1.
	 */
	public static final LocalDateTimeField<Integer> MONTH = new LocalDateTimeField<>();
	/**
	 * The ISO-8601 week number.
	 */
	public static final LocalDateTimeField<Integer> WEEK = new LocalDateTimeField<>();
	/**
	 * The calendar day of the month, numbered from 1.
	 */
	public static final LocalDateTimeField<Integer> DAY = new LocalDateTimeField<>();

	/**
	 * The hour of the day in 24-hour time, numbered from 0 to 23.
	 */
	public static final LocalDateTimeField<Integer> HOUR = new LocalDateTimeField<>();
	/**
	 * The minute of the hour, numbered from 0 to 59.
	 */
	public static final LocalDateTimeField<Integer> MINUTE = new LocalDateTimeField<>();
	/**
	 * The second of the minute, numbered from 0 to 59, including a fractional
	 * part representing fractions of a second
	 */
	public static final LocalDateTimeField<Double> SECOND = new LocalDateTimeField<>();

	/**
	 * The {@linkplain LocalDate date} part of a datetime.
	 */
	public static final LocalDateTimeField<LocalDate> DATE = new LocalDateTimeField<>();
	/**
	 * The {@linkplain LocalTime time} part of a datetime.
	 */
	public static final LocalDateTimeField<LocalTime> TIME = new LocalDateTimeField<>();
}
