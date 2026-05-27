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

package ee.jakarta.tck.persistence.jpa40.namedgraph;

import jakarta.persistence.Entity;
import jakarta.persistence.Fetch;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;

@Entity(name = "Jpa40SameNameGraphRoot")
@Table(name = "JPA40_SAME_NAME_GRAPH_ROOT")
@NamedEntityGraph(name = SameNameGraphRoot.GRAPH)
public class SameNameGraphRoot {
    public static final String GRAPH = "Jpa40SameNameGraph.withChild";

    @Id
    private Integer id;

    @Fetch(graph = GRAPH)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(graph = GRAPH, subgraph = GRAPH)
    private SameNameGraphChild child;

    public SameNameGraphRoot() {
    }

    public SameNameGraphRoot(Integer id, String name, SameNameGraphChild child) {
        this.id = id;
        this.name = name;
        this.child = child;
    }

    public SameNameGraphChild getChild() {
        return child;
    }
}
