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
 * An option influencing how an
 * {@linkplain AttributeNode attribute node} belonging
 * to an {@linkplain EntityGraph entity graph} is fetched.
 * The built-in option {@link CacheRetrieveMode} controls
 * whether an associated entity may be loaded from the
 * second-level cache.
 * <p>
 * Typical options are specific to a given persistence
 * provider and are often hints. A provider must ignore
 * unrecognized options. Applications requiring strict
 * portability between persistence providers should not
 * rely on vendor-specific fetch options.
 *
 * @see AttributeNode#addOption(FetchOption)
 * @see CacheRetrieveMode
 *
 * @since 4.0
 *
 */
public interface FetchOption {
}
