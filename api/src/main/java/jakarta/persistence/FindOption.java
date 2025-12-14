/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
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


package jakarta.persistence;

import java.util.List;

/**
 * An option influencing the behavior of {@link EntityHandler#find},
 * {@link EntityHandler#findMultiple}, {@link EntityHandler#get},
 * or {@link EntityHandler#getMultiple}.
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
 * @see EntityHandler#find(Class, Object, FindOption...)
 * @see EntityHandler#find(EntityGraph, Object, FindOption...)
 * @see EntityHandler#findMultiple(Class, List, FindOption...)
 * @see EntityHandler#findMultiple(EntityGraph, List, FindOption...)
 * @see EntityHandler#get(Class, Object, FindOption...)
 * @see EntityHandler#get(EntityGraph, Object, FindOption...)
 * @see EntityHandler#getMultiple(Class, List, FindOption...)
 * @see EntityHandler#getMultiple(EntityGraph, List, FindOption...)
 *
 * @since 3.2
 */
public interface FindOption {
}
