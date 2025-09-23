/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.entitylistener;

import ee.jakarta.tck.persistence.core.override.util.CallBackCounts;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;

public class ListenerD {

	public ListenerD() {
	}

	@PrePersist
	public void prePersist(Object obj) {
		updateCallBackCount("prePersist");
	}

	@PostPersist
	public void postPersist(Object obj) {
		updateCallBackCount("postPersist");
	}

	protected void updateCallBackCount(String callBackKeyName) {
		CallBackCounts.updateCount(callBackKeyName);
	}
}
