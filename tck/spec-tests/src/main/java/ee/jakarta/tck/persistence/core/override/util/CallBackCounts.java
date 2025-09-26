/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.util;

import java.util.HashMap;
import java.util.Map;

public class CallBackCounts {

	private static Map<String, Integer> counts = new HashMap<String, Integer>();

	public CallBackCounts() {
	}

	public static void updateCount(String callBackKeyName) {
		if (counts.containsKey(callBackKeyName)) {
			int currentCallBackCount = counts.get(callBackKeyName);
			System.out.println("callback key name is " + currentCallBackCount);
			counts.put(callBackKeyName, ++currentCallBackCount);
		} else {
			counts.put(callBackKeyName, 1);
		}

	}

	public static int getCount(String callBackKeyName) {
		Integer count = counts.get(callBackKeyName);
		return count == null ? 0 : count;
	}

	public static Map getCallBackCounts() {
		return counts;
	}

	public static void clearCountsMap() {
		counts.clear();
	}
}
