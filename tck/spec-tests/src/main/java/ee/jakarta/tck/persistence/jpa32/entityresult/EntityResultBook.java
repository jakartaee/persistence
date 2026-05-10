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

package ee.jakarta.tck.persistence.jpa32.entityresult;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@NamedNativeQuery(
        name = EntityResultBook.QUERY_WITH_LOCK_MODE,
        query = "SELECT ID, TITLE, VERSION FROM JPA32_ER_BOOK WHERE ID = 1",
        resultSetMapping = EntityResultBook.RESULT_MAPPING)
@SqlResultSetMapping(
        name = EntityResultBook.RESULT_MAPPING,
        entities = @EntityResult(
                entityClass = EntityResultBook.class,
                lockMode = LockModeType.OPTIMISTIC,
                fields = {
                        @FieldResult(name = "id", column = "ID"),
                        @FieldResult(name = "title", column = "TITLE"),
                        @FieldResult(name = "version", column = "VERSION")
                }))
@Entity(name = "Jpa32EntityResultBook")
@Table(name = "JPA32_ER_BOOK")
public class EntityResultBook {

    static final String QUERY_WITH_LOCK_MODE = "Jpa32EntityResultBook.withLockMode";

    static final String RESULT_MAPPING = "Jpa32EntityResultBook.lockModeMapping";

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    public EntityResultBook() {
    }

    public EntityResultBook(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getVersion() {
        return version;
    }
}
