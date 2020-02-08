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


package jakarta.persistence.metamodel;

/**
 * Instances of the type <code>SingularAttribute</code> represents persistent 
 * single-valued properties or fields.
 *
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 *
 * @since 2.0
 */
public interface SingularAttribute<X, T> 
		extends Attribute<X, T>, Bindable<T> {
	
    /**
     *  Is the attribute an id attribute.  This method will return
     *  true if the attribute is an attribute that corresponds to
     *  a simple id, an embedded id, or an attribute of an id class.
     *  @return boolean indicating whether the attribute is an id
     */
    boolean isId();

    /**
     *  Is the attribute a version attribute.
     *  @return boolean indicating whether the attribute is 
     *          a version attribute
     */
    boolean isVersion();

    /** 
     *  Can the attribute be null.
     *  @return boolean indicating whether the attribute can
     *          be null
     */
    boolean isOptional();

    /**
     * Return the type that represents the type of the attribute.
     * @return type of attribute
     */
    Type<T> getType();
}
