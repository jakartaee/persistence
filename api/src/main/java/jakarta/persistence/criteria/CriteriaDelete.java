/*
 * Copyright (c) 2011, 2026 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King - 4.0
//     Linda DeMichiel - 2.1

package jakarta.persistence.criteria;

/**
 * The {@code CriteriaDelete} interface defines functionality for 
 * performing bulk delete operations using the Criteria API
 *
 * <p>Criteria API bulk delete operations map directly to database 
 * delete operations. The persistence context is not synchronized 
 * with the result of the bulk delete.
 *
 * <p> A {@code CriteriaDelete} object must have a single root.
 *
 * @param <T>  the entity type that is the target of the DELETE
 *
 * @since 2.1
 */
public interface CriteriaDelete<T> extends CriteriaStatement<T> {

    /**
     * Modify the DELETE query to restrict the target of the deletion 
     * according to the specified boolean expression.
     * Replaces the previously added restriction(s), if any.
     * @param restriction  a simple or compound boolean expression
     * @return the modified delete query
     */
    CriteriaDelete<T> where(Expression<Boolean> restriction);

    /**
     * Modify the DELETE query to restrict the target of the deletion
     * according to the conjunction of the specified restriction 
     * predicates.
     * Replaces the previously added restriction(s), if any.
     * If no restrictions are specified, any previously added
     * restrictions are simply removed.
     * @param restrictions  zero or more restriction predicates
     * @return the modified delete query
     */
    CriteriaDelete<T> where(Predicate... restrictions);

}
