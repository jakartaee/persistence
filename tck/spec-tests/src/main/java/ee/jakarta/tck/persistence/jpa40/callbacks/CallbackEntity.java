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

package ee.jakarta.tck.persistence.jpa40.callbacks;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostDelete;
import jakarta.persistence.PostInsert;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PostUpsert;
import jakarta.persistence.PreDelete;
import jakarta.persistence.PreInsert;
import jakarta.persistence.PreMerge;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PreUpsert;
import jakarta.persistence.Table;

@Entity(name = "Jpa40CallbackEntity")
@Table(name = "JPA40_CALLBACK_ENTITY")
public class CallbackEntity {
    @Id
    private Integer id;

    private String title;

    public CallbackEntity() {
    }

    public CallbackEntity(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @PreMerge
    public void preMerge() {
        CallbackEventLog.record("entity-pre-merge");
    }

    @PreInsert
    public void preInsert() {
        CallbackEventLog.record("entity-pre-insert");
        if ("change-on-pre-insert".equals(title)) {
            title = "changed-by-pre-insert";
        }
        if ("throw-on-pre-insert".equals(title)) {
            CallbackEventLog.record("entity-pre-insert-throwing");
            throw new IllegalStateException("throwing from @PreInsert");
        }
    }

    @PostInsert
    public void postInsert() {
        CallbackEventLog.record("entity-post-insert");
    }

    @PreUpdate
    public void preUpdate() {
        CallbackEventLog.record("entity-pre-update");
        if ("change-on-pre-update".equals(title)) {
            title = "changed-by-pre-update";
        }
    }

    @PostUpdate
    public void postUpdate() {
        CallbackEventLog.record("entity-post-update");
    }

    @PreUpsert
    public void preUpsert() {
        CallbackEventLog.record("entity-pre-upsert");
    }

    @PostUpsert
    public void postUpsert() {
        CallbackEventLog.record("entity-post-upsert");
    }

    @PreDelete
    public void preDelete() {
        CallbackEventLog.record("entity-pre-delete");
    }

    @PostDelete
    public void postDelete() {
        CallbackEventLog.record("entity-post-delete");
    }
}
