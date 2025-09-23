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

package ee.jakarta.tck.persistence.core.callback.common;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class ListenerA extends ListenerAA {

	public ListenerA() {
		super();
	}

	@Override
	@PrePersist
	protected void prePersist(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerA.prePersist." + this, b);
		super.prePersist(b);
	}

	@Override
	@PostPersist
	protected void postPersist(Object b) {
		GenerictListenerImpl.logTrace("In ListenerA.postPersist." + this, (CallbackStatusIF) b);
		super.postPersist(b);
	}

	@Override
	@PreRemove
	protected void preRemove(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerA.prePersist." + this, b);
		super.preRemove(b);
	}

	@Override
	@PostRemove
	protected void postRemove(Object b) {
		GenerictListenerImpl.logTrace("In ListenerA.preRemove." + this, (CallbackStatusIF) b);
		super.postRemove(b);
	}

	@Override
	@PreUpdate
	protected void preUpdate(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerA.preUpdate." + this, b);
		super.preUpdate(b);
	}

	@Override
	@PostUpdate
	protected void postUpdate(Object b) {
		GenerictListenerImpl.logTrace("In ListenerA.postUpdate." + this, (CallbackStatusIF) b);
		super.postUpdate(b);
	}

	@PostLoad
	@Override
	protected void postLoad(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerA.postLoad." + this, b);
		super.postLoad(b);
	}

}
