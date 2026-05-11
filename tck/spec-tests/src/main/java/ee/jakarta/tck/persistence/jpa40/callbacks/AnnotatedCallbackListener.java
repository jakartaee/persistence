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

import jakarta.persistence.EntityListener;
import jakarta.persistence.PostInsert;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreInsert;
import jakarta.persistence.PreUpdate;

@EntityListener
public class AnnotatedCallbackListener {

    @PreInsert
    public void preInsertCallbackEntity(CallbackEntity entity) {
        CallbackEventLog.record("annotated-pre-insert-callback-entity");
    }

    @PostInsert
    public void postInsertCallbackEntity(CallbackEntity entity) {
        CallbackEventLog.record("annotated-post-insert-callback-entity");
    }

    @PostInsert
    public void postInsertObject(Object entity) {
        CallbackEventLog.record("annotated-post-insert-object");
    }

    @PreUpdate
    public void preUpdateObject(Object entity) {
        if (entity instanceof CallbackEntity) {
            CallbackEventLog.record("annotated-pre-update-object");
        }
    }

    @PostUpdate
    public void postUpdateCallbackEntity(CallbackEntity entity) {
        CallbackEventLog.record("annotated-post-update-callback-entity");
    }
}
