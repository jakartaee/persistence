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

/**
 * Defines the Jakarta Persistence Metamodel API, allowing runtime
 * reflection on the managed types declared by a persistence unit.
 * <p>
 * Instances of metamodel types may be obtained either:
 * <ul>
 * <li>via programmatic lookup using an instance of
 *     {@link jakarta.persistence.metamodel.Metamodel} obtained by
 *     calling
 *     {@link jakarta.persistence.EntityManagerFactory#getMetamodel()},
 *     or
 * <li>in a typesafe way, using members of the generated
 *     {@linkplain jakarta.persistence.metamodel.StaticMetamodel
 *     static metamodel classes}.
 * </ul>
 * <p>
 * The {@code Metamodel} API allows programmatic lookup, but is not
 * in general typesafe.
 * {@snippet :
 * SingularAttribute<Book,String> isbnAttribute =
 *         factory.getMetamodel()
 *             .entity(Book.class)
 *             .getId(String.class);
 * }
 * <p>
 * A static metamodel class is a class with static members providing
 * direct type safe access to the metamodel objects representing the
 * persistent members of a given managed class.
 * {@snippet :
 * SingularAttribute<Book,String> isbnAttribute = Book_.isbn;
 * }
 *
 * @since 2.0
 */
package jakarta.persistence.metamodel;
