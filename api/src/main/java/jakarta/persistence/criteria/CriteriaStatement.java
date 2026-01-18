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

package jakarta.persistence.criteria;

import jakarta.persistence.metamodel.EntityType;

/**
 * Declares operations common to {@link CriteriaUpdate} and
 * {@link CriteriaDelete}. A {@code CriteriaStatement} represents
 * a bulk operation affecting a certain entity type.
 *
 * @see CriteriaUpdate
 * @see CriteriaDelete
 * @see jakarta.persistence.EntityHandler#createStatement(CriteriaStatement)
 * 
 * @param <T> The affected entity type
 * @since 4.0
 */
public interface CriteriaStatement<T> extends CommonAbstractCriteria {

    /**
     * Create and add a statement root corresponding to the entity
     * that is affected by the statement.
     * A statement has a single root, the entity that is affected.
     * @param entityClass  the entity class
     * @return query root corresponding to the given entity
     */
    Root<T> from(Class<T> entityClass);

    /**
     * Create and add a statement root corresponding to the entity
     * that is affected by the statement.
     * A statement has a single root, the entity that is affected.
     * @param entity  metamodel object representing the entity
     * @return query root corresponding to the given entity
     */
    Root<T> from(EntityType<T> entity);

    /**
     * Return the statement root.
     * @return the statement root
     */
    Root<T> getRoot();

}
