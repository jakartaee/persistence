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
//     Christian Beikov - 3.2


package jakarta.persistence.criteria;

/**
 * Represents a simple or compound attribute path from a
 * bound type or collection, and is a "primitive" expression
 * of a number type.
 *
 * @param <X>  the type referenced by the path
 *
 * @since 3.2
 */
@SuppressWarnings("hiding")
public interface NumberPath<X extends Number> extends Path<X>, NumberExpression<X> {

}
