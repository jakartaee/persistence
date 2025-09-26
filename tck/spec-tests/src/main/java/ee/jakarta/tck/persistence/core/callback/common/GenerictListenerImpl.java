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

import java.lang.System.Logger;

/**
 * An annotation-free class that holds the logics for recording entity lifecycle
 * calls. This class is used by entities as a generic helper class, and also
 * extended with annotations to form a callback listener class.
 */
public class GenerictListenerImpl {

	private static final Logger logger = (Logger) System.getLogger(GenerictListenerImpl.class.getName());

	public GenerictListenerImpl() {
		super();
	}

	public static void logTrace(String s, CallbackStatusIF b) {
		String ss = b.getEntityName() + ": " + s;
		logger.log(Logger.Level.TRACE, ss);
	}

	public void prePersist(CallbackStatusIF b) {
		logTrace("In prePersist in class " + this, b);
		b.setPrePersistCalled(true);
		b.addPrePersistCall(b.getEntityName());
		String testName = b.getTestName();
		if (Constants.prePersistRuntimeExceptionTest.equals(testName)) {
			throw new ArithmeticException("RuntimeException from PrePersist.");
		}
	}

	public void postPersist(Object b) {
		CallbackStatusIF p = (CallbackStatusIF) b;
		logTrace("In postPersist.", p);
		if (!p.isPrePersistCalled()) {
			logger.log(Logger.Level.TRACE, "When calling postPersist, prePersist has not been called.");
			throw new IllegalStateException("When calling postPersist, prePersist has not been called.");
		}
		p.setPostPersistCalled(true);
		p.addPostPersistCall(p.getEntityName());
	}

	public void preRemove(CallbackStatusIF b) {
		logTrace("In preRemove.", b);
		b.setPreRemoveCalled(true);
		b.addPreRemoveCall(b.getEntityName());
	}

	public void postRemove(Object b) {
		CallbackStatusIF p = (CallbackStatusIF) b;
		logTrace("In postRemove.", p);
		if (!p.isPreRemoveCalled()) {
			logger.log(Logger.Level.TRACE, "When calling postRemove, preRemove has not been called.");
			throw new IllegalStateException("When calling postRemove, preRemove has not been called.");
		}
		p.setPostRemoveCalled(true);
		p.addPostRemoveCall(p.getEntityName());
	}

	public void preUpdate(CallbackStatusIF b) {
		logTrace("In preUpdate.", b);
		b.setPreUpdateCalled(true);
		b.addPreUpdateCall(b.getEntityName());
	}

	public void postUpdate(Object b) {
		CallbackStatusIF p = (CallbackStatusIF) b;
		logTrace("In postUpdate.", p);
		if (!p.isPreUpdateCalled()) {
			logger.log(Logger.Level.ERROR, "When calling postUpdate, preUpdate has not been called.");
			throw new IllegalStateException("When calling postUpdate, preUpdate has not been called.");
		}
		p.setPostUpdateCalled(true);
		p.addPostUpdateCall(p.getEntityName());
	}

	public void postLoad(CallbackStatusIF b) {
		logTrace("In postLoad.", b);
		b.setPostLoadCalled(true);
		b.addPostLoadCall(b.getEntityName());
	}

}
