/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
//     Gavin King       - 4.0

package jakarta.persistence.query;

import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.Query;
import jakarta.persistence.QueryFlushMode;
import jakarta.persistence.QueryHint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Configures options that affect the execution of a
 * database read operation. This annotation may be applied
 * to:
 * <ul>
 * <li>a method with a {@link StaticQuery} or Jakarta Data
 *     {@code jakarta.data.repository.Query} annotation
 *     whose {@code value} member specifies a {@code SELECT}
 *     statement,
 * <li>a method with a {@link StaticNativeQuery} annotation
 *     whose {@code value} member specifies a SQL operation
 *     which returns a result set, or
 * <li>a Jakarta Data repository method annotated
 *     {@code jakarta.data.repository.Find}.
 * </ul>
 *
 * <p>This annotation must be respected by an implementation
 * of Jakarta Data backed by Jakarta Persistence.
 *
 * @see WriteQueryOptions
 *
 * @since 4.0
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface ReadQueryOptions {
    /**
     * The {@linkplain CacheStoreMode cache store mode} to use.
     * The presence of this annotation overrides the default
     * cache store mode of the persistence context.
     * @see jakarta.persistence.Query#setCacheStoreMode
     */
    CacheStoreMode cacheStoreMode() default CacheStoreMode.USE;

    /**
     * The {@linkplain CacheRetrieveMode cache retrieve mode}
     * to use. The presence of this annotation overrides the
     * default cache retrieve mode of the persistence context.
     * @see jakarta.persistence.Query#setCacheRetrieveMode
     */
    CacheRetrieveMode cacheRetrieveMode() default CacheRetrieveMode.USE;

    /**
     * A query timeout in milliseconds.
     * By default, there is no timeout.
     * @see jakarta.persistence.Query#setTimeout
     */
    int timeout() default -1;

    /**
     * Query properties and hints.
     * May include vendor-specific query hints.
     * @see jakarta.persistence.Query#setHint(String, Object)
     */
    QueryHint[] hints() default {};

    /**
     * The lock mode type to use in query execution.
     * If a {@code lockMode} other than {@link LockModeType#NONE}
     * is specified, the query must be executed in a transaction
     * and the persistence context joined to the transaction.
     * <p> If a lock mode is explicitly specified for a
     * {@linkplain StaticNativeQuery native query}, the behavior
     * is undefined and unportable between persistence providers.
     * @see jakarta.persistence.Query#setLockMode
     */
    LockModeType lockMode() default LockModeType.NONE;

    /**
     * The pessimistic lock scope to use in query execution if a
     * pessimistic lock mode is specified via {@link #lockMode}.
     */
    PessimisticLockScope lockScope() default PessimisticLockScope.NORMAL;

    /**
     * The name of a {@link jakarta.persistence.NamedEntityGraph}
     * applied to the entity returned by the query. By default,
     * no entity graph is applied. The named entity graph must be
     * an entity graph compatible with the entity type returned
     * by the query. If the result type of the query is not an
     * entity type, the behavior is undefined. The entity graph
     * is interpreted as a load graph. The entity graph specified
     * here may be overridden by calling {@code setEntityGraph()}.
     *
     * @see jakarta.persistence.NamedEntityGraph#name
     */
    String entityGraph() default "";

    /**
     * The {@linkplain QueryFlushMode query flush mode} to use when
     * executing the query.
     * @see Query#setQueryFlushMode(QueryFlushMode)
     * @since 4.0
     */
    QueryFlushMode flush() default QueryFlushMode.DEFAULT;
}