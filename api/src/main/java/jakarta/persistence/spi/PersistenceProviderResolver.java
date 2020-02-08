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

package jakarta.persistence.spi;

import java.util.List;

/**
 * Determine the list of persistence providers available in the 
 * runtime environment.
 * 
 * <p> Implementations must be thread-safe.
 *
 * <p> Note that the <code>getPersistenceProviders</code> method can potentially
 * be called many times: it is recommended that the implementation
 * of this method make use of caching.
 *
 * @see PersistenceProvider
 * @since 2.0
 */
public interface PersistenceProviderResolver {

    /**
     * Returns a list of the <code>PersistenceProvider</code> implementations 
     * available in the runtime environment.
     *
     * @return list of the persistence providers available 
     *         in the environment
     */
    List<PersistenceProvider> getPersistenceProviders();

    /**
     * Clear cache of providers.
     *
     */
    void clearCachedProviders();
} 
