/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
//     Gavin King      - 4.0

package jakarta.persistence;

/**
 * Enumerates the possible flush modes for execution of a
 * {@link Query}. An explicitly-specified
 * {@linkplain Query#setQueryFlushMode(QueryFlushMode) query-level
 * flush mode} overrides the current
 * {@linkplain EntityManager#getFlushMode() flush mode of the
 * persistence context}.
 *
 * @since 4.0
 *
 * @see Query#setQueryFlushMode(QueryFlushMode)
 * @see NamedQuery#flush
 * @see NamedNativeQuery#flush
 * @see jakarta.persistence.query.ReadQueryOptions#flush
 * @see jakarta.persistence.query.WriteQueryOptions#flush
 *
 * @author Gavin King
 */
public enum QueryFlushMode {
	/**
	 * The persistence provider is required to flush pending
	 * modifications to the database before executing the query.
	 * Pending modifications to entities held in the persistence
	 * context are always visible to the processing of the query.
	 *
	 * @see EntityManager#flush()
	 */
	FLUSH,

	/**
	 * The persistence provider is not required to flush pending
	 * modifications to the database before executing the query.
	 * Pending modifications to entities held in the persistence
	 * context might not be visible to the processing of the query.
	 * <p>
	 * When the current {@linkplain EntityManager#getFlushMode
	 * flush mode} of the persistence context is
	 * {@link FlushModeType#EXPLICIT}, the persistence provider
	 * is not permitted to flush pending modifications before
	 * executing a query with {@code NO_FLUSH}. Otherwise, this
	 * mode is considered a hint to the persistence provider.
	 * <p>
	 * Equivalent to:
	 * <ul>
	 * <li>{@link FlushModeType#EXPLICIT} if the current flush
	 *     mode of the perssistence context is {@code EXPLICIT},
	 *     or
	 * <li>{@link FlushModeType#COMMIT} otherwise.
	 * </ul>
	 */
	NO_FLUSH,

	/**
	 * The persistence provider must determine whether to flush
	 * before executing the query by considering the current
	 * {@linkplain EntityManager#getFlushMode flush mode} of
	 * the persistence context.
	 * <p>
	 * If the current flush mode is {@link FlushModeType#AUTO},
	 * the persistence provider must ensure that every modification
	 * to the state of every entity associated with the persistence
	 * context which could possibly affect the result of the query
	 * is visible to the processing of the query. The persistence
	 * provider implementation might guarantee this by flushing
	 * pending modifications to the database before executing the
	 * query.
	 * <p>
	 * Equivalent to the current flush mode of the persistence
	 * context.
	 *
	 * @see EntityManager#getFlushMode()
	 */
	DEFAULT
}