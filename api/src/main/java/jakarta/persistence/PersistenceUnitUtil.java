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
 * Utility interface between the application and the persistence
 * provider managing the persistence unit.
 *
 * <p>The methods of this interface should only be invoked on entity
 * instances obtained from or managed by entity managers for this
 * persistence unit or on new entity instances.
 *
 * @since 2.0
 */
public interface PersistenceUnitUtil extends PersistenceUtil {

    /**
     * Determine the load state of a given persistent attribute
     * of an entity belonging to the persistence unit.
     * @param entity  entity instance containing the attribute
     * @param attributeName name of attribute whose load state is
     *        to be determined
     * @return false if entity's state has not been loaded or if 
     *         the attribute state has not been loaded, else true
     */
    public boolean isLoaded(Object entity, String attributeName);

    /**
     * Determine the load state of an entity belonging to the
     * persistence unit.  This method can be used to determine the
     * load state of an entity passed as a reference.  An entity is
     * considered loaded if all attributes for which
     * <code>FetchType.EAGER</code> has been specified have been
     * loaded.  
     * <p> The <code>isLoaded(Object, String)</code> method
     * should be used to determine the load state of an attribute.
     * Not doing so might lead to unintended loading of state.
     * @param entity   entity instance whose load state is to be determined
     * @return false if the entity has not been loaded, else true
     */
    public boolean isLoaded(Object entity);

    /**
     *  Return the id of the entity.
     *  A generated id is not guaranteed to be available until after
     *  the database insert has occurred.
     *  Returns null if the entity does not yet have an id.
     *  @param entity  entity instance
     *  @return id of the entity
     *  @throws IllegalArgumentException if the object is found not 
     *          to be an entity
     */
    public Object getIdentifier(Object entity);
} 
