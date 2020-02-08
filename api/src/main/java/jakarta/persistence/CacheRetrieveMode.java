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
 * <code>jakarta.persistence.cache.retrieveMode</code> property to
 * specify the behavior when data is retrieved by the
 * <code>find</code> methods and by queries.
 *
 * @since 2.0
 */
public enum CacheRetrieveMode {

    /**
     * Read entity data from the cache: this is 
     * the default behavior.
     */
    USE,

    /**
     * Bypass the cache: get data directly from
     * the database.
     */
    BYPASS  
}
