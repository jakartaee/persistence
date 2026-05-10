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

package ee.jakarta.tck.persistence.jpa32.query;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity(name = "Jpa32QueryBook")
@Table(name = "JPA32_QUERY_BOOK")
public class QueryBook {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "BIG_INTEGER_VALUE")
    private BigInteger bigIntegerValue;

    @Column(name = "BIG_DECIMAL_VALUE", precision = 10, scale = 2)
    private BigDecimal bigDecimalValue;

    public QueryBook() {
    }

    public QueryBook(Integer id, String title, Integer quantity, BigInteger bigIntegerValue,
            BigDecimal bigDecimalValue) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.bigIntegerValue = bigIntegerValue;
        this.bigDecimalValue = bigDecimalValue;
    }

    public Integer getId() {
        return id;
    }
}
