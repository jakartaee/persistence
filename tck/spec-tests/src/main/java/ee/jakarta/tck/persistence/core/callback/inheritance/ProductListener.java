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

import java.lang.System.Logger;

import ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

/**
 * None of the callbacks will be called, since subclass entities all have their
 * own callbacks. This listener is intended to be used by Product entity.
 */
public class ProductListener {

	private static final Logger logger = (Logger) System.getLogger(ProductListener.class.getName());

	public ProductListener() {
		super();
	}

	@PrePersist
	public void prePersist(CallbackStatusIF b) {
		logger.log(Logger.Level.TRACE, "In ProductListener.prePersist");
		b.setPrePersistCalled(true);
		b.addPrePersistCall(this.getClass().getSimpleName());
	}

	@PostPersist
	public void postPersist(Object b) {
		((CallbackStatusIF) b).setPostPersistCalled(true);
		((CallbackStatusIF) b).addPostPersistCall(this.getClass().getSimpleName());
	}

	@PreRemove
	public void preRemove(CallbackStatusIF b) {
		logger.log(Logger.Level.TRACE, "In ProductListener.preRemove");
		b.setPreRemoveCalled(true);
		b.addPreRemoveCall(this.getClass().getSimpleName());
	}

	@PostRemove
	public void postRemove(Object b) {
		logger.log(Logger.Level.TRACE, "In PartProductListener.postRemove.");
		((CallbackStatusIF) b).setPostRemoveCalled(true);
		((CallbackStatusIF) b).addPostRemoveCall(this.getClass().getSimpleName());
	}

	@PreUpdate
	public void preUpdate(CallbackStatusIF b) {
		logger.log(Logger.Level.TRACE, "In PartProductListener.preUpdate.");
		b.setPreUpdateCalled(true);
		b.addPreUpdateCall(this.getClass().getSimpleName());
	}

	@PostUpdate
	public void postUpdate(Object b) {
		logger.log(Logger.Level.TRACE, "In PartProductListener.postUpdate.");
		((CallbackStatusIF) b).setPostUpdateCalled(true);
		((CallbackStatusIF) b).addPostUpdateCall(this.getClass().getSimpleName());
	}

	@PostLoad
	public void postLoad(CallbackStatusIF b) {
		logger.log(Logger.Level.TRACE, "In PartProductListener.postLoad.");
		b.setPostLoadCalled(true);
		b.addPostLoadCall(this.getClass().getSimpleName());
	}

}
