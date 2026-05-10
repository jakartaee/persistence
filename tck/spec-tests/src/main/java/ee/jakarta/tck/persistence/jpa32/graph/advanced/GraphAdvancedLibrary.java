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

package ee.jakarta.tck.persistence.jpa32.graph.advanced;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(name = "Jpa32GraphAdvancedLibrary")
@Table(name = "JPA32_GRAPH_LIBRARY")
public class GraphAdvancedLibrary {

    @Id
    private Integer id;

    private String name;

    @ManyToOne
    private GraphAdvancedPublication featuredPublication;

    @OneToMany
    @JoinTable(name = "JPA32_GRAPH_PUBS")
    private List<GraphAdvancedPublication> publications = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "JPA32_GRAPH_SELECTION")
    private Map<GraphAdvancedAuthor, GraphAdvancedPublication> selections = new HashMap<>();

    public GraphAdvancedLibrary() {
    }

    public GraphAdvancedLibrary(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GraphAdvancedPublication getFeaturedPublication() {
        return featuredPublication;
    }

    public List<GraphAdvancedPublication> getPublications() {
        return publications;
    }

    public Map<GraphAdvancedAuthor, GraphAdvancedPublication> getSelections() {
        return selections;
    }
}
