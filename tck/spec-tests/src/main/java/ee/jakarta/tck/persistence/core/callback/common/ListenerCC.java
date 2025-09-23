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
 * $Id: ListenerC.java 65615 2012-03-29 19:02:39Z sdimilla $
 */

package ee.jakarta.tck.persistence.core.callback.common;

import jakarta.persistence.PostLoad;

public class ListenerCC extends ListenerBase {

	public ListenerCC() {
		super();
	}

	@Override
	public void prePersist(CallbackStatusIF b) {
		super.prePersist(b);
	}

	@Override
	public void postPersist(Object b) {
		super.postPersist(b);
	}

	@Override
	public void preRemove(CallbackStatusIF b) {
		super.preRemove(b);
	}

	@Override
	public void postRemove(Object b) {
		super.postRemove(b);
	}

	@Override
	public void preUpdate(CallbackStatusIF b) {
		super.preUpdate(b);
	}

	@Override
	public void postUpdate(Object b) {
		super.postUpdate(b);
	}

	@PostLoad
	public void postLoad(CallbackStatusIF b) {
		super.postLoad(b);
	}

}
