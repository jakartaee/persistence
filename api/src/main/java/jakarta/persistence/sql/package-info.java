/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
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
//     Gavin King - 4.0

/**
 * Defines an API for the programmatic definition of SQL result set
 * mappings to Java classes. This API is an alternative to defining
 * such mappings using the annotation
 * {@link jakarta.persistence.SqlResultSetMapping}.
 * <ul>
 * <li>{@link jakarta.persistence.sql.ResultSetMapping} declares
 *     static factory convenience methods for instantiating the
 *     elements of a result set mapping.
 * <li>{@link jakarta.persistence.sql.ColumnMapping} maps a single
 *     column of a SQL result set to a Java type.
 * <li>{@link jakarta.persistence.sql.FieldMapping} maps a single
 *     column of a SQL result set to a field or property of an
 *     entity or embeddable class.
 * <li>{@link jakarta.persistence.sql.EntityMapping} maps columns
 *     of a SQL result set to an entity class.
 * <li>{@link jakarta.persistence.sql.EmbeddedMapping} maps columns
 *     of a SQL result set to an embeddable class.
 * <li>{@link jakarta.persistence.sql.ConstructorMapping} allows a
 *     tuple of values to be packaged as an instance of a record
 *     or class defined by the application.
 * <li>{@link jakarta.persistence.sql.TupleMapping} allows a tuple
 *     of values to be packaged as an instance of the class
 *     {@link jakarta.persistence.Tuple}.
 * </ul>
 *
 * @since 4.0
 */
package jakarta.persistence.sql;