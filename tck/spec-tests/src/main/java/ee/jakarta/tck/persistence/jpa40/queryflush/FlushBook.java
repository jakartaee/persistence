/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa40.queryflush;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryFlushMode;
import jakarta.persistence.Table;

@Entity(name = "Jpa40FlushBook")
@Table(name = "JPA40_FLUSH_BOOK")
@NamedQuery(
        name = FlushBook.NO_FLUSH_QUERY,
        query = "SELECT COUNT(b) FROM Jpa40FlushBook b WHERE b.title = 'NoFlush'",
        resultClass = Long.class,
        flush = QueryFlushMode.NO_FLUSH)
@NamedQuery(
        name = FlushBook.FLUSH_QUERY,
        query = "SELECT COUNT(b) FROM Jpa40FlushBook b WHERE b.title = 'Flush'",
        resultClass = Long.class,
        flush = QueryFlushMode.FLUSH)
@NamedNativeQuery(
        name = FlushBook.NATIVE_FLUSH_QUERY,
        query = "SELECT COUNT(*) FROM JPA40_FLUSH_BOOK WHERE TITLE = 'NativeFlush'",
        resultClass = Long.class,
        flush = QueryFlushMode.FLUSH)
public class FlushBook {
    public static final String NO_FLUSH_QUERY = "Jpa40FlushBook.noFlush";
    public static final String FLUSH_QUERY = "Jpa40FlushBook.flush";
    public static final String NATIVE_FLUSH_QUERY = "Jpa40FlushBook.nativeFlush";

    @Id
    private Integer id;

    private String title;

    public FlushBook() {
    }

    public FlushBook(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
