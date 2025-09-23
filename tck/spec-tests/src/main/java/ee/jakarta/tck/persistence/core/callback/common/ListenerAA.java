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
 * $Id: ListenerA.java 65615 2012-03-29 19:02:39Z sdimilla $
 */

package ee.jakarta.tck.persistence.core.callback.common;

import jakarta.persistence.PostLoad;

public class ListenerAA extends ListenerBase {

	public ListenerAA() {
		super();
	}

	@Override
	protected void prePersist(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerAA.prePersist." + this, b);
		super.prePersist(b);
	}

	@Override
	protected void postPersist(Object b) {
		GenerictListenerImpl.logTrace("In ListenerAA.postPersist." + this, (CallbackStatusIF) b);
		super.postPersist(b);
	}

	@Override
	protected void preRemove(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerAA.prePersist." + this, b);
		super.preRemove(b);
	}

	@Override
	protected void postRemove(Object b) {
		GenerictListenerImpl.logTrace("In ListenerAA.preRemove." + this, (CallbackStatusIF) b);
		super.postRemove(b);
	}

	@Override
	protected void preUpdate(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerAA.preUpdate." + this, b);
		super.preUpdate(b);
	}

	@Override
	protected void postUpdate(Object b) {
		GenerictListenerImpl.logTrace("In ListenerAA.postUpdate." + this, (CallbackStatusIF) b);
		super.postUpdate(b);
	}

	@PostLoad
	protected void postLoad(CallbackStatusIF b) {
		GenerictListenerImpl.logTrace("In ListenerAA.postLoad." + this, b);
		super.postLoad(b);
	}

}
