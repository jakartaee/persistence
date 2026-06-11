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
 * Defines annotations for expressing statically type safe queries by
 * annotating query methods of a
 * {@linkplain jakarta.persistence.spi.Discoverable discoverable} type
 * or Jakarta Data repository.
 * <ul>
 * <li>{@link jakarta.persistence.query.JakartaQuery} declares a query
 *     method and specifies the query or statement in the Jakarta
 *     Persistence Query Language.
 * <li>{@link jakarta.persistence.query.NativeQuery} declares a
 *     query method and specifies the query or statement in the native
 *     SQL dialect of the database.
 * <li>{@link jakarta.persistence.query.QueryOptions} specifies
 *     additional options affecting execution of the query or statement.
 * </ul>
 *
 * @since 4.0
 */
package jakarta.persistence.query;