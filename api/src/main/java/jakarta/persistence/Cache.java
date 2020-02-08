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
 * Interface used to interact with the second-level cache.
 * If a cache is not in use, the methods of this interface have
 * no effect, except for <code>contains</code>, which returns false.
 *
 * @since 2.0
 */
public interface Cache {

    /**
     * Whether the cache contains data for the given entity.
     * @param cls  entity class 
     * @param primaryKey  primary key
     * @return boolean indicating whether the entity is in the cache
     */
    public boolean contains(Class cls, Object primaryKey);

    /**
     * Remove the data for the given entity from the cache.
     * @param cls  entity class
     * @param primaryKey  primary key
     */
    public void evict(Class cls, Object primaryKey);

    /**
     * Remove the data for entities of the specified class (and its
     * subclasses) from the cache.
     * @param cls  entity class
     */
    public void evict(Class cls);

    /**
     * Clear the cache.
     */
    public void evictAll();

    /**
     * Return an object of the specified type to allow access to the
     * provider-specific API.  If the provider's Cache
     * implementation does not support the specified class, the
     * PersistenceException is thrown.
     * @param cls  the class of the object to be returned.  This is
     * normally either the underlying Cache implementation
     * class or an interface that it implements.
     * @return an instance of the specified class
     * @throws PersistenceException if the provider does not
     * support the call
     * @since 2.1
     */
    public <T> T unwrap(Class<T> cls);
}
