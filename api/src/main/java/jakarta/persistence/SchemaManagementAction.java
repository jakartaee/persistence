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
 * Represents an action that can be performed by the schema management tooling.
 *
 * @since 4.0
 */
public enum SchemaManagementAction {
    /**
     * No action.
     */
    NONE,
    /**
     * Create the generated database schema.
     *
     * @see SchemaManager#create(boolean)
     */
    CREATE,
    /**
     * Drop the generated database schema.
     *
     * @see SchemaManager#drop(boolean)
     */
    DROP,
    /**
     * Drop and then recreate the generated database schema.
     */
    DROP_AND_CREATE,
    /**
     * Validate the schema held in the database against the
     * generated schema.
     *
     * @see SchemaManager#validate()
     */
    VALIDATE,
    /**
     * Populate the database with data from the DML load script.
     *
     * @see Persistence.SchemaManagementProperties#SCHEMAGEN_LOAD_SCRIPT_SOURCE
     * @see SchemaManager#populate()
     */
    POPULATE
}
