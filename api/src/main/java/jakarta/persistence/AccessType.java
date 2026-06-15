/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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
 * Determines how the persistence provider runtime reads and writes
 * the persistent state of an entity from and to an instance of the
 * entity class.
 * <p>
 * Used with the {@link Access @Access} annotation to specify the
 * access type of an entity class, mapped superclass, or embeddable
 * class, or of an attribute of such a class.
 * 
 * @see Access
 *
 * @since 2.0
 */
public enum AccessType {

    /**
     * Direct field access is used. Instance variables must have
     * private, protected, or package visibility. Property
     * accessor methods are not required. Mapping annotations are
     * placed directly on the instance variables.
     */
    FIELD,

    /**
     * Property access is used; that is, state is accessed via
     * getter and setter methods. Property accessor methods must
     * have public or protected visibility. Instance variables
     * must have private, protected, or package visibility.
     * Mapping annotations are placed on getter methods.
     */
    PROPERTY
}
