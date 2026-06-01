/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King     - 3.2
//     Lukas Jungmann - 3.2

package jakarta.persistence.criteria;

/**
 * Abstracts over {@linkplain CriteriaQuery top-level queries} and
 * {@linkplain CriteriaBuilder#union unions} and
 * {@linkplain CriteriaBuilder#intersect intersections} of top-level
 * queries.
 * <p>
 * For example, this criteria select is a union of two top-level
 * queries returning {@code Book}:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 *
 * var recentBooks = builder.createQuery(Book.class);
 * var recentBook = recentBooks.from(Book.class);
 * recentBooks.select(recentBook)
 *            .where(recentBook.get(Book_.publicationDate).after(startDate));
 *
 * var olderBooks = builder.createQuery(Book.class);
 * var olderBook = olderBooks.from(Book.class);
 * olderBooks.select(olderBook)
 *           .where(olderBook.get(Book_.publicationDate).before(cutoffDate));
 *
 * var selectedBooks = builder.union(recentBooks, olderBooks);
 * var books = agent.createQuery(selectedBooks).getResultList();
 * }
 *
 * @param <T>  the type returned by the query
 *
 * @since 3.2
 */
public interface CriteriaSelect<T> {
}
