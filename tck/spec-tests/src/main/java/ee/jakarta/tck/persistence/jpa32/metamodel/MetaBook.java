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

package ee.jakarta.tck.persistence.jpa32.metamodel;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

@Entity(name = "Jpa32MetaBook")
@Table(name = "JPA32_META_BOOK")
@NamedQuery(name = MetaBook.QUERY_BY_CATEGORY,
        query = "SELECT b FROM Jpa32MetaBook b WHERE b.category = :category ORDER BY b.id",
        resultClass = MetaBook.class)
@NamedEntityGraph(name = MetaBook.GRAPH_WITH_PUBLISHER,
        attributeNodes = @NamedAttributeNode("publisher"))
@SqlResultSetMapping(name = MetaBook.MAPPING_TITLE,
        columns = @ColumnResult(name = "TITLE", type = String.class))
public class MetaBook {

    public static final String QUERY_BY_CATEGORY = "Jpa32MetaBook.findByCategory";

    public static final String GRAPH_WITH_PUBLISHER = "Jpa32MetaBook.withPublisher";

    public static final String MAPPING_TITLE = "Jpa32MetaBookTitleMapping";

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CATEGORY")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUBLISHER_ID")
    private MetaPublisher publisher;

    public MetaBook() {
    }

    public MetaBook(Integer id, String title, String category, MetaPublisher publisher) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.publisher = publisher;
    }

    public Integer getId() {
        return id;
    }
}
