/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final String prePersistRuntimeExceptionTest = "prePersistRuntimeExceptionTest";

	public static final String prePersistTest = "prePersistTest";

	public static final String prePersistMultiTest = "prePersistMultiTest";

	public static final String preRemoveTest = "preRemoveTest";

	public static final String preRemoveMultiTest = "preRemoveMultiTest";

	public static final String preUpdateTest = "preUpdateTest";

	public static final String postLoadTest = "postLoadTest";

	public static final String postLoadMultiTest = "postLoadMultiTest";

	public static final String prePersistCascadeTest = "prePersistCascadeTest";

	public static final String prePersistMultiCascadeTest = "prePersistMultiCascadeTest";

	public static final String preRemoveCascadeTest = "preRemoveCascadeTest";

	public static final String preRemoveMultiCascadeTest = "preRemoveMultiCascadeTest";

	public static final String prePersistDefaultListenerTest = "prePersistDefaultListenerTest";

	public static final String prePersistRuntimeExceptionTest2 = "prePersistRuntimeExceptionTest2";

	public static final String prePersistTest2 = "prePersistTest2";

	public static final String preRemoveTest2 = "preRemoveTest2";

	public static final String preUpdateTest2 = "preUpdateTest2";

	public static final String postLoadTest2 = "postLoadTest2";

	public static final String LISTENER_A = "ListenerA";

	public static final String LISTENER_AA = "ListenerAA";

	public static final String LISTENER_B = "ListenerB";

	public static final String LISTENER_BB = "ListenerBB";

	public static final String LISTENER_C = "ListenerC";

	public static final String LISTENER_CC = "ListenerCC";

	public static final List LISTENER_ABC = Arrays.asList(LISTENER_A, LISTENER_B, LISTENER_C);

	public static final List LISTENER_AABBCC = Arrays.asList(LISTENER_AA, LISTENER_BB, LISTENER_CC);

	public static final List LISTENER_BC = Arrays.asList(LISTENER_B, LISTENER_C);

	public static final List LISTENER_BBCC = Arrays.asList(LISTENER_BB, LISTENER_CC);

	public static final String PRODUCT = "Product";

	public static final String ORDER = "Order";

	public static final String LINE_ITEM = "LineItem";

}
