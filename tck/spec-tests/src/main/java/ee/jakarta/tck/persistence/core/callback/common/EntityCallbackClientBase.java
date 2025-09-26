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
import java.sql.SQLException;

import ee.jakarta.tck.persistence.common.PMClientBase;

public abstract class EntityCallbackClientBase extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(EntityCallbackClientBase.class.getName());

	protected EntityCallbackClientBase() {
		super();
	}

	protected Object txShouldRollback(Object b, String testName) throws Exception {
		String reason = "";
		try {
			logger.log(Logger.Level.TRACE, "Persisting: " + b.getClass().getName());
			getEntityManager().persist(b);
			logger.log(Logger.Level.TRACE, "Committing: " + b.getClass().getName() + " changes");
			getEntityTransaction().commit();
			reason = "Expecting ArithmeticException from callback method, but got none.";
			throw new Exception(reason);
		} catch (ArithmeticException e) {
			reason = "EntityCallbackClientBase: Got expected exception: " + e.toString();
			logger.log(Logger.Level.TRACE, reason);
			if (!getEntityTransaction().isActive()) {
				reason = "No Transaction was active, even though one was previously started";
				throw new Exception(reason, e);
			}
		} catch (Exception e) {
			reason = "EntityCallbackClientBase: Expecting ArithmeticException, but got unexpected exception: ["
					+ e.toString() + "]";
			throw new Exception(reason, e);
		}
		logger.log(Logger.Level.TRACE, "Clearing cache");
		clearCache();
		logger.log(Logger.Level.TRACE, "Executing find");

		Object p2 = null;
		try {
			// Transaction is marked as rollback. TransactionManger is in an undefined
			// state. SQLException is a possibility
			p2 = getEntityManager().find(b.getClass(), testName);
		} catch (RuntimeException e) {
			Throwable cause = e;
			while (cause != null && !SQLException.class.isInstance(cause)) {
				cause = cause.getCause();
			}
			if (cause == null) {
				throw new Exception(e);
			}
		}
		if (p2 == null) {
			reason = "EntityCallbackClientBase: Got expected result: entity with id " + testName + " was not found.";
			logger.log(Logger.Level.TRACE, reason);
		} else {
			reason = "EntityCallbackClientBase: Unexpected result: found entity with id " + testName;
			throw new Exception(reason);
		}
		return b;
	}
}
