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

package ee.jakarta.tck.persistence.jpa32.criteria.entitytype;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "Jpa32EntityTypeBook")
@Table(name = "JPA32_ENTITY_TYPE_BOOK")
public class EntityTypeBook {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CATEGORY")
    private String category;

    @ManyToOne
    @JoinColumn(name = "PUBLISHER_ID")
    private EntityTypePublisher publisher;

    public EntityTypeBook() {
    }

    public EntityTypeBook(Integer id, String title, String category, EntityTypePublisher publisher) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.publisher = publisher;
    }
}
