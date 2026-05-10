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

package ee.jakarta.tck.persistence.jpa32.enumeratedvalue;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;

@Entity(name = "Jpa32EnumeratedValueBook")
@Table(name = "JPA32_ENUMERATED_VALUE_BOOK")
@NamedNativeQuery(name = EnumeratedValueBook.QUERY_STATUS_CODE,
        query = "SELECT STATUS_CODE FROM JPA32_ENUMERATED_VALUE_BOOK WHERE ID = 1",
        resultClass = String.class,
        columns = @ColumnResult(name = "STATUS_CODE", type = String.class))
public class EnumeratedValueBook {

    public static final String QUERY_STATUS_CODE = "Jpa32EnumeratedValueBook.findStatusCode";

    @Id
    @Column(name = "ID")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", length = 1)
    private BookStatus status;

    public EnumeratedValueBook() {
    }

    public EnumeratedValueBook(Integer id, BookStatus status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public BookStatus getStatus() {
        return status;
    }
}
