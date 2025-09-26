/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.entityTransaction;

import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	Properties props = null;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = {};
		return createDeploymentJar("jpa_core_entityTransaction.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		// Nothing to cleanup
		try {
			logger.log(Logger.Level.TRACE, "done cleanup, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: beginIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:539; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call begin twice and verify IllegalStateException is thrown
	 */
	@Test
	public void beginIllegalStateExceptionTest() throws Exception {
		boolean pass = false;
		try {
			EntityTransaction t = getEntityTransaction();
			t.begin();
			if (t.isActive()) {
				try {
					t.begin();
					logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
				} catch (IllegalStateException ise) {
					logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "isActive() returned false when a transaction was active");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("beginIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: commitIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:540; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call commit without an active transaction verify
	 * IllegalStateException is thrown
	 */
	@Test
	public void commitIllegalStateExceptionTest() throws Exception {
		boolean pass = false;
		try {
			EntityTransaction t = getEntityTransaction();
			if (!t.isActive()) {
				try {
					t.commit();
					logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
				} catch (IllegalStateException ise) {
					logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "isActive() returened true when no transaction  was active");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("commitIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: getRollbackOnlyIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:542; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call getRollbackOnly without an active transaction verify
	 * IllegalStateException is thrown
	 */
	@Test
	public void getRollbackOnlyIllegalStateExceptionTest() throws Exception {
		boolean pass = false;
		try {
			EntityTransaction t = getEntityTransaction();
			if (!t.isActive()) {
				try {
					t.getRollbackOnly();
					logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
				} catch (IllegalStateException ise) {
					logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "isActive() returned true when no transaction  was active");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("getRollbackOnlyIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: rollbackIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:544; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call rollback without an active transaction verify
	 * IllegalStateException is thrown
	 */
	@Test
	public void rollbackIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		try {
			EntityTransaction t = getEntityTransaction();
			if (!t.isActive()) {
				try {
					t.rollback();
					logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
				} catch (IllegalStateException ise) {
					logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "isActive() returned true when no transaction  was active");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("rollbackIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: setRollbackOnlyIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:546; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call setRollbackOnly without an active transaction verify
	 * IllegalStateException is thrown
	 */
	@Test
	public void setRollbackOnlyIllegalStateExceptionTest() throws Exception {
		boolean pass = false;
		try {
			EntityTransaction t = getEntityTransaction();
			if (!t.isActive()) {
				try {
					t.setRollbackOnly();
					logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
				} catch (IllegalStateException ise) {
					logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "isActive() returned true when no transaction  was active");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("setRollbackOnlyIllegalStateExceptionTest failed");
		}
	}

}
