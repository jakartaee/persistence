/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

/**
 * Used as the value of the
 * <code>jakarta.persistence.cache.storeMode</code> property to specify
 * the behavior when data is read from the database and when data is
 * committed into the database.
 *
 * @since 2.0
 */
public enum CacheStoreMode {

    /**
     * Insert entity data into cache when read from database
     * and insert/update entity data when committed into database: 
     * this is the default behavior. Does not force refresh
     * of already cached items when reading from database.
     */
    USE,

    /**
     * Don't insert into cache. 
     */
    BYPASS,

    /**
     * Insert/update entity data into cache when read 
     * from database and when committed into database. 
     * Forces refresh of cache for items read from database.
     */
    REFRESH
}
