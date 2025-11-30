/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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
import jakarta.persistence.QueryHint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface ReadQueryOptions {
    /**
     * The {@linkplain CacheStoreMode cache store mode} to use.
     * @see jakarta.persistence.Query#setCacheStoreMode
     */
    CacheStoreMode cacheStoreMode() default CacheStoreMode.USE;

    /**
     * The {@linkplain CacheRetrieveMode cache retrieve mode}
     * to use.
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
}