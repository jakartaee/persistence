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

package ee.jakarta.tck.persistence.jpa40.versioning;

import jakarta.persistence.Entity;
import jakarta.persistence.ExcludedFromVersioning;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity(name = "Jpa40VersionedBook")
@Table(name = "JPA40_VERSIONED_BOOK")
public class VersionedBook {

    @Id
    private Integer id;

    private String title;

    @ExcludedFromVersioning
    private String auditNote;

    @Version
    private int version;

    public VersionedBook() {
    }

    public VersionedBook(Integer id, String title, String auditNote) {
        this.id = id;
        this.title = title;
        this.auditNote = auditNote;
    }

    public int getVersion() {
        return version;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuditNote(String auditNote) {
        this.auditNote = auditNote;
    }
}
