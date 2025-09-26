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
import ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl;
import ee.jakarta.tck.persistence.core.callback.common.Constants;
import ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

/**
 * This class defines some entity callback methods for an entity class. This
 * class itself is not an entity. These callback methods are intended to be
 * inherited by subclass entities.
 */

@MappedSuperclass
abstract public class PricedPartProductCallback extends CallbackStatusImpl implements CallbackStatusIF {

	// @todo: for multiple callback listeners/methods, need to add to each
	// callback methods:
	// addPrePersistCalls(getEntityName());

	@PrePersist
	public void prePersist() {
		GenerictListenerImpl.logTrace("In prePersist.", this);
		this.setPrePersistCalled(true);
		String testName = this.getTestName();
		if (Constants.prePersistRuntimeExceptionTest2.equals(testName)) {
			throw new ArithmeticException("RuntimeException from PrePersist.");
		}
	}

	@PostPersist
	public void postPersist() {
		GenerictListenerImpl.logTrace("In postPersist.", this);
		if (!this.isPrePersistCalled()) {
			throw new IllegalStateException("When calling postPersist, prePersist has not been called.");
		}
		this.setPostPersistCalled(true);
	}

	@PreRemove
	public void preRemove() {
		GenerictListenerImpl.logTrace("In preRemove.", this);
		this.setPreRemoveCalled(true);
	}

	@PostRemove
	public void postRemove() {
		GenerictListenerImpl.logTrace("In postRemove.", this);
		if (!this.isPreRemoveCalled()) {
			throw new IllegalStateException("When calling postRemove, preRemove has not been called.");
		}
		this.setPostRemoveCalled(true);
	}

	@PreUpdate
	public void preUpdate() {
		GenerictListenerImpl.logTrace("In preUpdate.", this);
		this.setPreUpdateCalled(true);
	}

	@PostUpdate
	public void postUpdate() {
		GenerictListenerImpl.logTrace("In postUpdate.", this);
		if (!this.isPreUpdateCalled()) {
			throw new IllegalStateException("When calling postUpdate, preUpdate has not been called.");
		}
		this.setPostUpdateCalled(true);
	}

	@PostLoad
	public void postLoad() {
		GenerictListenerImpl.logTrace("In postLoad.", this);
		this.setPostLoadCalled(true);
	}

}
