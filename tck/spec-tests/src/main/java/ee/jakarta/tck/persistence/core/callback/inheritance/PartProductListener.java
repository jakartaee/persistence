/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.callback.inheritance;

import ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF;
import ee.jakarta.tck.persistence.core.callback.common.Constants;
import ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

/**
 * All callback methods except PrePersist in this class throw RuntimeException,
 * since they must not be called. This listener is intended to be used by
 * PartProduct entity, whose callback methods take precedence.
 */
public class PartProductListener {

	public PartProductListener() {
		super();
	}

	@PrePersist
	public void prePersist(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In PartProductListener.prePersist.", b);
		b.setPrePersistCalled(true);
		b.addPrePersistCall(this.getClass().getSimpleName());
		String testName = b.getTestName();
		if (Constants.prePersistRuntimeExceptionTest.equals(testName)) {
			throw new ArithmeticException("RuntimeException from PrePersist.");
		}
	}

	@PostPersist
	public void postPersist(Object b) {
		GenerictListenerImpl.logTrace("In PartProductListener.postPersist.", (CallbackStatusIF) b);
		((CallbackStatusIF) b).setPostPersistCalled(true);
		((CallbackStatusIF) b).addPostPersistCall(this.getClass().getSimpleName());
	}

	@PreRemove
	public void preRemove(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In PartProductListener.preRemove.", b);
		b.setPreRemoveCalled(true);
		b.addPreRemoveCall(this.getClass().getSimpleName());
	}

	@PostRemove
	public void postRemove(Object b) {
		GenerictListenerImpl.logTrace("In PartProductListener.postRemove.", (CallbackStatusIF) b);
		((CallbackStatusIF) b).setPostRemoveCalled(true);
		((CallbackStatusIF) b).addPostRemoveCall(this.getClass().getSimpleName());
	}

	@PreUpdate
	public void preUpdate(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In PartProductListener.preUpdate.", b);
		b.setPreUpdateCalled(true);
		b.addPreUpdateCall(this.getClass().getSimpleName());
	}

	@PostUpdate
	public void postUpdate(Object b) {
		GenerictListenerImpl.logTrace("In PartProductListener.postUpdate.", (CallbackStatusIF) b);
		((CallbackStatusIF) b).setPostUpdateCalled(true);
		((CallbackStatusIF) b).addPostUpdateCall(this.getClass().getSimpleName());
	}

	@PostLoad
	public void postLoad(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In PartProductListener.postLoad.", b);
		b.setPostLoadCalled(true);
		b.addPostLoadCall(this.getClass().getSimpleName());
	}

}
