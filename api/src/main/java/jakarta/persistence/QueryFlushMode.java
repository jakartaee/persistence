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
 * <p>When a Jakarta Persistence or native SQL query is executed
 * within a transaction via an instance of {@link Query} obtained
 * from an {@link EntityManager} joined to the transaction, the
 * effective flush mode is determined by the current flush mode
 * of the {@code Query} object, if any flush mode has been
 * specified by calling {@link Query#setFlushMode} or
 * {@link Query#setQueryFlushMode}, or, otherwise, by the current
 * flush mode of the persistence context.
 * <ul>
 * <li>If {@link QueryFlushMode#FLUSH} is set, the persistence
 *     provider is required to flush modifications to the database
 *     before executing the query.
 * <li>If {@link QueryFlushMode#NO_FLUSH} is set, the persistence
 *     provider is not required to flush modifications to the
 *     database before executing the query; if, in addition, the
 *     current flush mode of the entity manager is
 *     {@link FlushModeType#EXPLICIT}, the persistence provider
 *     is not permitted to flush modifications to the database.
 * </ul>
 * Otherwise, if {@link QueryFlushMode#DEFAULT} is set:
 * <ul>
 * <li>If {@link FlushModeType#AUTO} is in effect, the provider
 *     must ensure that every modification to the state of every
 *     entity associated with the persistence context which could
 *     possibly affect the result of the query is visible to the
 *     processing of the query. The provider implementation might
 *     guarantee this by flushing pending updates to modified
 *     entities to the database before executing the query.
 * <li>If {@link FlushModeType#EXPLICIT} is in effect, the
 *     provider is not permitted to flush modifications to the
 *     database before executing the query. Pending modifications
 *     to entities in the persistence context might not be visible
 *     to the processing of the query.
 * <li>If {@link FlushModeType#COMMIT} is in effect, it is
 *     unspecified how pending modifications to entities in the
 *     persistence context affect the results of the query.
 * </ul>
 *
 * <p>When there is no transaction active, or if the persistence
 * context has not been joined to the current transaction, the
 * persistence provider must not flush pending modifications to
 * the database, regardless of the flush mode.
 *
 * <p>An {@link EntityAgent} has no associated persistence context,
 * and so flush modes have no effect on an entity agent, nor on
 * {@code Query} objects created by an entity agent.
 *
 * @since 4.0
 *
 * @see Query#setQueryFlushMode(QueryFlushMode)
 * @see NamedQuery#flush
 * @see NamedNativeQuery#flush
 * @see jakarta.persistence.query.ReadQueryOptions#flush
 * @see jakarta.persistence.query.WriteQueryOptions#flush
 * @see FlushModeType
 *
 * @author Gavin King
 */
public enum QueryFlushMode {
	/**
	 * The persistence provider is required to flush pending
	 * modifications to the database before executing the query.
	 * Pending modifications to entities held in the persistence
	 * context are always visible to the processing of the query.
	 * The {@linkplain EntityManager#getFlushMode current flush
	 * mode} of the persistence context is completely overridden.
	 */
	FLUSH,

	/**
	 * The persistence provider is not required to flush pending
	 * modifications to the database before executing the query.
	 * Pending modifications to entities held in the persistence
	 * context might not be visible to the processing of the query.
	 * <ul>
	 * <li>If the {@linkplain EntityManager#getFlushMode current
	 *     flush mode} of the persistence context is
	 *     {@link FlushModeType#EXPLICIT}, the persistence provider
	 *     is not permitted to flush pending modifications before
	 *     executing the query.
	 * <li>Otherwise, this mode is considered a hint to the
	 *     persistence provider.
	 * </ul>
	 */
	NO_FLUSH,

	/**
	 * The persistence provider must determine whether to flush
	 * before executing the query by considering the current
	 * {@linkplain EntityManager#getFlushMode flush mode} of the
	 * persistence context.
	 */
	DEFAULT
}