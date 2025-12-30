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
//     Gavin King      - 4.0


package jakarta.persistence;

/**
 * An option influencing {@linkplain Query query execution}.
 * Built-in options control {@linkplain LockModeType locking},
 * {@linkplain CacheRetrieveMode cache interaction}, and
 * {@linkplain Timeout timeouts}.
 *
 * <p>This interface may be implemented by custom provider-specific
 * options which extend the options defined by the specification.
 *
 * @see LockModeType
 * @see PessimisticLockScope
 * @see CacheRetrieveMode
 * @see CacheStoreMode
 * @see Timeout
 *
 * @see Query#setOption(QueryOption)
 * @see TypedQueryReference#getOptions()
 *
 * @since 4.0
 */
public interface QueryOption {
}
