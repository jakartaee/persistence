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

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.query.JakartaQuery;

import java.util.List;

@Entity(name = "Jpa40StaticQueryBook")
@Table(name = "JPA40_STATIC_QUERY_BOOK")
public class StaticQueryBook {
    @Id
    private Integer id;

    private String title;
    private String category;

    public StaticQueryBook() {
    }

    public StaticQueryBook(Integer id, String title, String category) {
        this.id = id;
        this.title = title;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @JakartaQuery("SELECT b FROM Jpa40StaticQueryBook b "
            + "WHERE (:title IS NULL OR b.title = :title) "
            + "AND (:category IS NULL OR b.category = :category) ORDER BY b.id")
    public static native List<StaticQueryBook> search(String title, String category);

    @JakartaQuery("UPDATE Jpa40StaticQueryBook b SET b.title = ?2 WHERE b.id = ?1")
    public static native int updateTitle(Integer id, String title);
}
