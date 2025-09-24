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

import java.util.ArrayList;
import java.util.List;

public class CallbackStatusImpl implements CallbackStatusIF, java.io.Serializable {
	private String testName;

	private boolean prePersistCalled;

	private boolean postPersistCalled;

	private boolean preRemoveCalled;

	private boolean postRemoveCalled;

	private boolean preUpdateCalled;

	private boolean postUpdateCalled;

	private boolean postLoadCalled;

	private List prePersistCalls = new ArrayList();

	private List postPersistCalls = new ArrayList();

	private List preRemoveCalls = new ArrayList();

	private List postRemoveCalls = new ArrayList();

	private List preUpdateCalls = new ArrayList();

	private List postUpdateCalls = new ArrayList();

	private List postLoadCalls = new ArrayList();

	public String getEntityName() {
		String name = this.getClass().getName();
		name = name.substring(name.lastIndexOf('.') + 1);
		return name;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String s) {
		testName = s;
	}

	public boolean isPrePersistCalled() {
		return prePersistCalled;
	}

	public void setPrePersistCalled(boolean b) {
		prePersistCalled = b;
	}

	public boolean isPostPersistCalled() {
		return postPersistCalled;
	}

	public void setPostPersistCalled(boolean b) {
		postPersistCalled = b;
	}

	public boolean isPreRemoveCalled() {
		return preRemoveCalled;
	}

	public void setPreRemoveCalled(boolean b) {
		preRemoveCalled = b;
	}

	public boolean isPostRemoveCalled() {
		return postRemoveCalled;
	}

	public void setPostRemoveCalled(boolean b) {
		postRemoveCalled = b;
	}

	public boolean isPreUpdateCalled() {
		return preUpdateCalled;
	}

	public void setPreUpdateCalled(boolean b) {
		preUpdateCalled = b;
	}

	public boolean isPostUpdateCalled() {
		return postUpdateCalled;
	}

	public void setPostUpdateCalled(boolean b) {
		postUpdateCalled = b;
	}

	public boolean isPostLoadCalled() {
		return postLoadCalled;
	}

	public void setPostLoadCalled(boolean b) {
		postLoadCalled = b;
	}

	public List getPrePersistCalls() {
		return prePersistCalls;
	}

	public List getPostPersistCalls() {
		return postPersistCalls;
	}

	public List getPreRemoveCalls() {
		return preRemoveCalls;
	}

	public List getPostRemoveCalls() {
		return postRemoveCalls;
	}

	public List getPreUpdateCalls() {
		return preUpdateCalls;
	}

	public List getPostUpdateCalls() {
		return postUpdateCalls;
	}

	public List getPostLoadCalls() {
		return postLoadCalls;
	}

	public void addPreUpdateCall(String shortName) {
		preUpdateCalls.add(shortName);
	}

	public void addPreRemoveCall(String shortName) {
		preRemoveCalls.add(shortName);
	}

	public void addPrePersistCall(String shortName) {
		prePersistCalls.add(shortName);
	}

	public void addPostRemoveCall(String shortName) {
		postRemoveCalls.add(shortName);
	}

	public void addPostPersistCall(String shortName) {
		postPersistCalls.add(shortName);
	}

	public void addPostLoadCall(String shortName) {
		postLoadCalls.add(shortName);
	}

	public void addPostUpdateCall(String shortName) {
		postUpdateCalls.add(shortName);
	}

}
