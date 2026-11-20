/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.jpa40.staticquery;

import jakarta.persistence.query.JakartaQuery;
import jakarta.persistence.query.NativeQuery;

import java.util.List;
import java.util.stream.Stream;

public interface BookQueries {

    @JakartaQuery("SELECT b.id, b.title FROM Jpa40StaticQueryBook b ORDER BY b.id")
    List<BookSummary> bookSummaries();

    @JakartaQuery("SELECT b.id, b.title FROM Jpa40StaticQueryBook b WHERE b.id = :id")
    BookSummary bookSummary(Integer id);

    @JakartaQuery("SELECT b.id, b.title FROM Jpa40StaticQueryBook b ORDER BY b.id")
    Stream<BookSummary> bookSummaryStream();

    @NativeQuery("SELECT ID, TITLE FROM JPA40_STATIC_QUERY_BOOK ORDER BY ID")
    List<BookSummary> nativeBookSummaries();

    @NativeQuery("SELECT ID, TITLE FROM JPA40_STATIC_QUERY_BOOK WHERE TITLE = ?")
    BookSummary bookSummary(String title);
}
