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
 * Specifies a batch size, that is, how many entities should be
 * fetched in each request to the database. This option is always
 * a hint, and might be ignored by the persistence provider.
 *
 * @since 4.0
 */
public record BatchSize(int batchSize) implements FindOption, FetchOption {
}