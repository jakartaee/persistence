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

package ee.jakarta.tck.persistence.jpa40.xmlstatement;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Jpa40XmlStatementBook")
@Table(name = "JPA40_XML_STATEMENT_BOOK")
public class XmlStatementBook {
    public static final String XML_UPDATE_TITLE = "Jpa40XmlStatementBook.xmlUpdateTitle";
    public static final String XML_NATIVE_UPDATE_TITLE = "Jpa40XmlStatementBook.xmlNativeUpdateTitle";

    @Id
    private Integer id;

    private String title;

    public XmlStatementBook() {
    }

    public XmlStatementBook(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
