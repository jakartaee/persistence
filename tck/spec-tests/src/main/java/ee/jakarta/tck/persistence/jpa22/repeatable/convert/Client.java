/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.repeatable.convert;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 22L;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "B", pkgName + "DotConverter", pkgName + "Employee3",
				pkgName + "NumberToStateConverter" };
		return createDeploymentJar("jpa_jpa22_repeatable_convert.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: convertsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3316;
	 * 
	 * @test_Strategy: try @Convert works when annotated multiple times
	 * without @Converts
	 */
	@Test
	public void convertsTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		try {
			getEntityTransaction().begin();
			String street = "500.Oracle.Parkway";
			String city = "Redwood Shores";
			// 1 - MA , 2 - CA
			B b = new B("1", "name1", 1000, new Address(street, city, 1));
			getEntityManager().persist(b);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			B b1 = getEntityManager().find(B.class, b.id);
			logger.log(Logger.Level.TRACE, "B:" + b1.toString());
			if (b1.getBValue().equals(1000)) {
				logger.log(Logger.Level.TRACE, "Received expected value:" + b1.getBValue());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Converter was not properly applied, expected value:1000, actual" + b1.getBValue());
			}
			Address a = b1.getAddress();
			if (a.getStreet().equals(street.replace(".", "_"))) {
				logger.log(Logger.Level.TRACE, "Received expected street:" + a.getStreet());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Converter was not properly applied, expected street:" + street + ", actual:" + a.getStreet());
			}
			if (a.getState() == 1) {
				logger.log(Logger.Level.TRACE, "Received expected state:" + a.getState());
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Converter was not properly applied, expected state: 1, actual: " + a.getState());
			}
			getEntityTransaction().rollback();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception while rolling back TX:", re);
			}
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("convertsTest failed");
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM B_EMBEDDABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PHONES").executeUpdate();
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
