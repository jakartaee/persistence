/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King       - 4.0

package jakarta.persistence.query;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a query expressed in the native SQL dialect of the database,
 * usually a SQL {@code SELECT} statement, which is executed by calling
 * {@link jakarta.persistence.Query#getResultList getResultList()},
 * {@link jakarta.persistence.Query#getResultStream getResultStream()}, or
 * {@link jakarta.persistence.Query#getSingleResult getSingleResult()}.
 *
 * <p> This annotation may be applied to any abstract method of any class
 * or interface belonging to the persistence unit, or to a method of a
 * Jakarta Data repository. The return type of the method must agree with
 * the type returned by the query.
 * {@snippet :
 * interface Library {
 *     @StaticNativeQuery("select * from books where title like ?")
 *     @ReadQueryOptions(cacheStoreMode = CacheStoreMode.BYPASS)
 *     List<Book> findBooksByTitle(String title);
 *
 *     @StaticNativeQuery("select * from books where isbn = ?")
 *     Book getBookWithIsbn(String isbn);
 * }
 *}
 * <p> A method return type <em>agrees</em> with the type returned by the
 * query if either:
 * <ul>
 * <li>it is exactly {@code R}, {@link java.util.List List&lt;R&gt;}, or
 *     {@link java.util.stream.Stream Stream&lt;R&gt;} where the query
 *     return type is assignable to {@code R}, or
 * <li>the method is a Jakarta Data repository method, and its return type
 *     is a legal query method return type for the given query, as specified
 *     by Jakarta Data.
 * </ul>
 *
 * <p> When this annotation is applied to a method of a class or interface
 * belonging to the persistence unit, a reference to a query declared using
 * this annotation may be obtained from the static metamodel class of the
 * annotated class or interface.
 * {@snippet :
 * List<Book> books =
 *         em.createQuery(Library_._findBooksByTitle_)
 *           .setParameter("title", "%Jakarta%")
 *           .getResultList();
 * }
 * {@snippet :
 * Book book =
 *         em.createQuery(Library_._getBookWithIsbn_)
 *           .setParameter("isbn", isbn)
 *           .getSingleResult();
 * }
 *
 * <p> In addition, the query is treated as a named query, where the query
 * name is the concatenation of the unqualified name of the type, with the
 * string {@code "."}, and the name of the annotated member, for example,
 * {@code "Library.getBookWithIsbn"}.
 *
 * <p> If a {@link jakarta.persistence.SqlResultSetMapping} exists with
 * the same name as the query, it is used to map the result set of the
 * query. If the {@code @SqlResultSetMapping} annotation occurs on the
 * same method as {@code @StaticNativeQuery} annotation, name defaulting
 * rules conspire to ensure that no name needs to be explicitly specified.
 * {@snippet :
 * @StaticNativeQuery("select * from books where isbn = ?")
 * @SqlResultSetMapping(entities =
 *         @EntityResult(entityClass = Book.class,
 *                 fields = {@FieldResult(name = "isbn", column = "isbn_13"),
 *                           @FieldResult(name = "title", column = "title_en"})
 * Book getBookWithIsbn(String isbn);
 * }
 *
 * <p> An implementation of Jakarta Data backed by Jakarta Persistence
 * must treat this annotation as a Jakarta Data query annotation.
 *
 * <p>The parameter types of the annotated method must be basic types.
 * If this annotation occurs on a generic method, on a method with a
 * parameter type which is not a basic type, or on a method with a
 * return type or parameter type involving a type variable, the behavior
 * is undefined.
 *
 * @since 4.0
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface StaticNativeQuery {
    /**
     * The query string in the native SQL dialect of the database.
     */
    String value();
}