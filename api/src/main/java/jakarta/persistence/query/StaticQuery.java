/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
 * Specifies a query expressed in the Jakarta Persistence Query Language.
 * This annotation may be applied to:
 * <ul>
 * <li>any method of any class or interface belonging to the persistence
 *     unit, or
 * <li>a query method of a Jakarta Data repository with
 *     an implementation backed by Jakarta Persistence.
 * </ul>
 * <p> The return type of the method must agree with the type returned by
 * the query. The parameter types of the method must agree with the types
 * of the query parameters. An implementation of Jakarta Persistence or of
 * Jakarta Data may be able to determine if such agreement exists at
 * compilation time or when the persistence unit is initialized, but this
 * is not required.
 *
 * <p>
 * {@snippet :
 * interface Library {
 *
 *     @StaticQuery("from Book where title like :title")
 *     @ReadQueryOptions(cacheStoreMode = CacheStoreMode.BYPASS)
 *     List<Book> findBooksByTitle(String title);
 *
 *     @StaticQuery("from Book where isbn = :isbn")
 *     Book getBookWithIsbn(String isbn);
 *
 *     record Summary(String title, String isbn, LocalDate date) {}
 *     @StaticQuery("""
 *         select title, isbn, pubDate
 *         from Book
 *         where title like = ?1 and pubDate > ?2
 *     """)
 *     List<Summary> retrieveSummaries(String title, LocalDate fromDate);
 *
 * }
 *}
 *
 * <p> A method return type <em>agrees</em> with the type returned by the
 * query if either:
 * <ul>
 * <li>it is exactly {@code R}, {@link java.util.List List&lt;R&gt;}, or
 *     {@link java.util.stream.Stream Stream&lt;R&gt;} for some result
 *     type {@code R} which is compatible with the query, according to the
 *     rules specified below, or
 * <li>the method is a Jakarta Data repository method, and its return type
 *     is a legal query method return type for the given query, as specified
 *     by Jakarta Data.
 * </ul>
 * <p>A result type {@code R} is considered <em>compatible</em> with a query
 * if either:
 * <ol>
 * <li>the select list of the query contains only a single item, and the type
 *     of the item is assignable to the given result class, or
 * <li>the result class is a non-abstract class or record type that has a
 *     constructor with the same number of parameters as the query has items
 *     in its select list, and the constructor parameter types exactly match
 *     the types of the corresponding items in the select list.
 * </ol>
 * <p>In the first case, each query result is returned directly to the caller.
 * In the second case, each query result is automatically packaged in a new
 * instance of the result class by calling the matching constructor.
 *
 * <p> A method parameter type <em>agrees</em> with a query parameter type if
 * it is a type that could be assigned to the corresponding query parameter
 * by passing an instance of the type to {@code setParameter()}. In making
 * this determination, method parameters are correlated with query parameters
 * by position or by name, depending on whether the query uses positional or
 * named parameters.
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
 * {@code "Library.findBooksByTitle"}.
 *
 * <p> An implementation of Jakarta Data backed by Jakarta Persistence
 * must treat this annotation as a Jakarta Data query annotation.
 *
 * <p> The parameter types of the annotated method must be basic types.
 * If this annotation occurs on a generic method, on a method with a
 * parameter type which is not a basic type, or on a method with a
 * return type or parameter type involving a type variable, the behavior
 * is undefined.
 *
 * @since 4.0
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface StaticQuery {
    /**
     * The query string in the Jakarta Persistence Query Language.
     */
    String value();
}