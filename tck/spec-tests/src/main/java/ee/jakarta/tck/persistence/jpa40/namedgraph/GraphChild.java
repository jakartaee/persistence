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
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;

@Entity(name = "Jpa40NamedGraphChild")
@Table(name = "JPA40_NAMED_GRAPH_CHILD")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedEntityGraph(name = GraphChild.GRAPH)
public class GraphChild {
    public static final String GRAPH = "Jpa40NamedGraphChild.details";

    @Id
    private Integer id;

    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @NamedEntityGraph.AttributeNode(graph = GRAPH)
    private GraphDetail graphDetail;

    public GraphChild() {
    }

    public GraphChild(Integer id, String detail, GraphDetail graphDetail) {
        this.id = id;
        this.detail = detail;
        this.graphDetail = graphDetail;
    }

    public Integer getId() {
        return id;
    }

    public String getDetail() {
        return detail;
    }

    public GraphDetail getGraphDetail() {
        return graphDetail;
    }
}
