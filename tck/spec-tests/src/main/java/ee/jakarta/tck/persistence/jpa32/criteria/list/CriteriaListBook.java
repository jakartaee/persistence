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

package ee.jakarta.tck.persistence.jpa32.criteria.list;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Jpa32CriteriaListBook")
@Table(name = "JPA32_CRITERIA_LIST_BOOK")
public class CriteriaListBook {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "NULLABLE_LABEL")
    private String nullableLabel;

    @Column(name = "QUANTITY")
    private Integer quantity;

    public CriteriaListBook() {
    }

    public CriteriaListBook(Integer id, String title, String category, String nullableLabel,
            Integer quantity) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.nullableLabel = nullableLabel;
        this.quantity = quantity;
    }
}
