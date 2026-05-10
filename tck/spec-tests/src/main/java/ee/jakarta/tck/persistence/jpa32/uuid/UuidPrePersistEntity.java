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

package ee.jakarta.tck.persistence.jpa32.uuid;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity(name = "Jpa32UuidPrePersistEntity")
@Table(name = "JPA32_UUID_PRE_PERSIST_ENTITY")
public class UuidPrePersistEntity {

    private static volatile UUID prePersistId;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    public UuidPrePersistEntity() {
    }

    public UuidPrePersistEntity(String name) {
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        prePersistId = id;
    }

    public UUID getId() {
        return id;
    }

    public static UUID getPrePersistId() {
        return prePersistId;
    }

    public static void resetPrePersistId() {
        prePersistId = null;
    }
}
