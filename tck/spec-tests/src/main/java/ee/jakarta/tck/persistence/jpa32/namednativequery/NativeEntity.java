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

package ee.jakarta.tck.persistence.jpa32.namednativequery;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

@Entity(name = "Jpa32NativeQueryEntity")
@Table(name = "JPA32_NATIVE_QUERY_ENTITY")
@NamedNativeQuery(name = NativeEntity.QUERY_ENTITY,
        query = "SELECT ID, NAME FROM JPA32_NATIVE_QUERY_ENTITY WHERE ID = 1",
        resultClass = NativeEntity.class,
        entities = @EntityResult(entityClass = NativeEntity.class, lockMode = LockModeType.NONE))
@NamedNativeQuery(name = NativeEntity.QUERY_DTO,
        query = "SELECT ID AS ITEM_ID, NAME AS ITEM_NAME FROM JPA32_NATIVE_QUERY_ENTITY WHERE ID = 1",
        resultClass = NativeDto.class,
        classes = @ConstructorResult(targetClass = NativeDto.class,
                columns = {
                        @ColumnResult(name = "ITEM_ID", type = Integer.class),
                        @ColumnResult(name = "ITEM_NAME", type = String.class)
                }))
@NamedNativeQuery(name = NativeEntity.QUERY_COLUMN,
        query = "SELECT NAME AS ITEM_NAME FROM JPA32_NATIVE_QUERY_ENTITY WHERE ID = 1",
        resultClass = String.class,
        columns = @ColumnResult(name = "ITEM_NAME", type = String.class))
@SqlResultSetMapping(name = NativeEntity.MAPPING_DTO,
        classes = @ConstructorResult(targetClass = NativeDto.class,
                columns = {
                        @ColumnResult(name = "ITEM_ID", type = Integer.class),
                        @ColumnResult(name = "ITEM_NAME", type = String.class)
                }))
public class NativeEntity {

    public static final String QUERY_ENTITY = "Jpa32NativeQueryEntity.findEntity";

    public static final String QUERY_DTO = "Jpa32NativeQueryEntity.findDto";

    public static final String QUERY_COLUMN = "Jpa32NativeQueryEntity.findName";

    public static final String MAPPING_DTO = "Jpa32NativeQueryDtoMapping";

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    public NativeEntity() {
    }

    public NativeEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
