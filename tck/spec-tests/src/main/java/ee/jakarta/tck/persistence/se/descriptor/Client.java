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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.se.descriptor;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final B bRef[] = new B[5];

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFile = { MAPPING_FILE_XML };
		String[] classes = { pkgName + "A", pkgName + "B" };
		return createDeploymentJar("jpa_se_descriptor.jar", pkgNameWithoutSuffix, classes, PERSISTENCE_XML, xmlFile);

	}

	@BeforeEach
	public void setup() throws Exception {
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData();
		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:900; PERSISTENCE:SPEC:901;
	 * PERSISTENCE:SPEC:850; PERSISTENCE:SPEC:852; PERSISTENCE:SPEC:854;
	 * PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:952; PERSISTENCE:SPEC:968;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:910; PERSISTENCE:SPEC:893;
	 * PERSISTENCE:SPEC:939; PERSISTENCE:SPEC:953; PERSISTENCE:SPEC:945;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:969; PERSISTENCE:SPEC:970;
	 * PERSISTENCE:JAVADOC:162;
	 * 
	 * @test_Strategy: With the above archive, deploy bean, create entities,
	 * persist, then find.
	 *
	 */
	@Test
	public void test1() throws Exception {
		boolean pass = false;
		try {

			B anotherB = getEntityManager().find(B.class, "1");

			if (anotherB != null) {
				if (anotherB.equals(bRef[0])) {
					logger.log(Logger.Level.TRACE, "get expected B");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected B:" + anotherB.toString());
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("test1 failed");
		}
	}

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");

		try {

			logger.log(Logger.Level.TRACE, "Create 2 B Entities");
			bRef[0] = new B("1", "myB", 1);
			bRef[1] = new B("2", "yourB", 2);

			logger.log(Logger.Level.TRACE, "Start to persist Bs ");
			for (B b : bRef) {
				if (b != null) {
					getEntityManager().persist(b);
					logger.log(Logger.Level.TRACE, "persisted B " + b);
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception while creating test data:" + e);
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeTestData();
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
