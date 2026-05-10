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

package ee.jakarta.tck.persistence.jpa40.querygraph;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Jpa40QueryGraphAuthor")
@Table(name = "JPA40_QUERY_GRAPH_AUTHOR")
public class QueryGraphAuthor {
    @Id
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "authors")
    private Set<QueryGraphBook> books = new HashSet<>();

    public QueryGraphAuthor() {
    }

    public QueryGraphAuthor(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Set<QueryGraphBook> getBooks() {
        return books;
    }
}
