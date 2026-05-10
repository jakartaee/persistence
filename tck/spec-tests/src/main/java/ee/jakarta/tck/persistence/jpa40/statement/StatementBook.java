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

package ee.jakarta.tck.persistence.jpa40.statement;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeStatement;
import jakarta.persistence.NamedStatement;
import jakarta.persistence.Table;

@Entity(name = "Jpa40StatementBook")
@Table(name = "JPA40_STATEMENT_BOOK")
@NamedStatement(
        name = StatementBook.UPDATE_TITLE,
        statement = "UPDATE Jpa40StatementBook b SET b.title = :title WHERE b.id = :id")
@NamedNativeStatement(
        name = StatementBook.NATIVE_UPDATE_TITLE,
        statement = "UPDATE JPA40_STATEMENT_BOOK SET TITLE = ? WHERE ID = ?")
public class StatementBook {
    public static final String UPDATE_TITLE = "Jpa40StatementBook.updateTitle";
    public static final String NATIVE_UPDATE_TITLE = "Jpa40StatementBook.nativeUpdateTitle";

    @Id
    private Integer id;

    private String title;

    public StatementBook() {
    }

    public StatementBook(Integer id, String title) {
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
