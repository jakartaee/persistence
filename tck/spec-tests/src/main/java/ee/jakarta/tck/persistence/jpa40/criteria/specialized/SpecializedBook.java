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

package ee.jakarta.tck.persistence.jpa40.criteria.specialized;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity(name = "Jpa40SpecializedBook")
@Table(name = "JPA40_SPECIALIZED_BOOK")
public class SpecializedBook {
    @Id
    private Integer id;

    private String title;

    private Integer quantity;

    private LocalDate publishedOn;

    private Boolean available;

    private String category;

    public SpecializedBook() {
    }

    public SpecializedBook(Integer id, String title, Integer quantity, LocalDate publishedOn,
                           Boolean available, String category) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.publishedOn = publishedOn;
        this.available = available;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }
}
