/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.persistence.jpa40.sqlresultmapping;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.query.NativeQuery;

import java.util.List;
import java.util.stream.Stream;

public interface NativeQueries {

    @NativeQuery("SELECT ID, TITLE FROM JPA40_SQL_BOOK ORDER BY ID")
    List<SqlMappingDto> bookRecords();

    @NativeQuery("SELECT ID, TITLE FROM JPA40_SQL_BOOK WHERE ID = ?1")
    SqlMappingDto bookRecord(Integer id);

    @NativeQuery("SELECT ID, TITLE FROM JPA40_SQL_BOOK ORDER BY ID")
    Stream<SqlMappingDto> bookRecordStream();

    @NativeQuery("SELECT TITLE AS BOOK_TITLE, ID AS BOOK_ID FROM JPA40_SQL_BOOK ORDER BY ID")
    @ConstructorResult(targetClass = SqlMappingDto.class, columns = {
            @ColumnResult(name = "BOOK_ID", type = Integer.class),
            @ColumnResult(name = "BOOK_TITLE", type = String.class)
    })
    List<SqlMappingDto> bookRecordsWithMapping();
}
