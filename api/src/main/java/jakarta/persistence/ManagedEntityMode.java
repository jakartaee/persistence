/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * A {@link FindOption} which specifies whether entities are
 * to be loaded in {@linkplain #READ_ONLY read only} mode or
 * in regular {@linkplain #READ_WRITE modifiable} mode.
 *
 * @since 4.0
 */
public enum ManagedEntityMode implements FindOption {
	/**
	 * Specifies that an entity should be loaded in read-only mode.
	 * <p>
	 * Read-only entities can be modified, but changes are not
	 * synchronized with the database.
	 */
	READ_ONLY,
	/**
	 * Specifies that an entity should be loaded in the default
	 * modifiable mode.
	 * <p>
	 * Changes made to modifiable entities are synchronized to
	 * with the database when the persistence context is flushed.
	 */
	READ_WRITE
}
