/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.assocoverride;

import java.lang.System.Logger;
import java.sql.Date;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static PartTimeEmployee ptRef[] = new PartTimeEmployee[5];

	private static Address aRef[] = new Address[5];

	final private Date d1 = getSQLDate(2000, 2, 14);

	final private Date d2 = getSQLDate(2001, 6, 27);

	final private Date d3 = getSQLDate(2002, 7, 7);

	final private Date d4 = getSQLDate(2003, 3, 3);

	final private Date d5 = getSQLDate(2004, 4, 10);

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "Employee", pkgName + "PartTimeEmployee" };
		return createDeploymentJar("jpa_core_annotations_access_property.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();

			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: associationOverride
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:593; PERSISTENCE:SPEC:596;
	 * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
	 * PERSISTENCE:SPEC:1130; PERSISTENCE:SPEC:1131; PERSISTENCE:SPEC:1132;
	 * PERSISTENCE:SPEC:1133; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1062;
	 * PERSISTENCE:JAVADOC:4; PERSISTENCE:JAVADOC:5; PERSISTENCE:JAVADOC:6;
	 * PERSISTENCE:JAVADOC:2; PERSISTENCE:JAVADOC:1; PERSISTENCE:SPEC:1964;
	 * PERSISTENCE:SPEC:1965; PERSISTENCE:SPEC:1969;
	 * 
	 * @test_Strategy: An entity may have a mapped superclass which provides
	 * persistent entity state and mapping information. Use AssociationOverride
	 * annotation
	 */
	@Test
	public void associationOverride() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin AssociationOverride");
		boolean pass = false;

		try {
			PartTimeEmployee ptEmp1 = getEntityManager().find(PartTimeEmployee.class, 6);

			if (ptEmp1.getAddress().getStreet().equals("1 Network Drive")) {
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("test2 failed");
		}
	}

	public void createTestData() {

		try {

			getEntityTransaction().begin();

			ptRef[0] = new PartTimeEmployee(6, "Kellie", "Lee", d5, 60000.0F);
			ptRef[1] = new PartTimeEmployee(7, "Nicole", "Martin", d4, 60000.0F);
			ptRef[2] = new PartTimeEmployee(8, "Mark", "Francis", d3, 60000.0F);
			ptRef[3] = new PartTimeEmployee(9, "Will", "Forrest", d2, 60000.0F);
			ptRef[4] = new PartTimeEmployee(10, "Katy", "Hughes", d1, 60000.0F);

			// create Addresses
			aRef[0] = new Address("100", "1 Network Drive", "Burlington", "MA", "01803");
			aRef[1] = new Address("200", "4150 Network Drive", "Santa Clara", "CA", "95054");
			aRef[2] = new Address("300", "2 Network Drive", "Burlington", "MA", "01803");
			aRef[3] = new Address("400", "5150 Network Drive", "Santa Clara", "CA", "95054");
			aRef[4] = new Address("500", "3 Network Drive", "Burlington", "MA", "01803");

			// Set Addresse to Employee
			ptRef[0].setAddress(aRef[0]);
			ptRef[1].setAddress(aRef[1]);
			ptRef[2].setAddress(aRef[2]);
			ptRef[3].setAddress(aRef[3]);
			ptRef[4].setAddress(aRef[4]);

			logger.log(Logger.Level.TRACE, "Persist part time employees ");
			for (int i = 0; i < 5; i++) {
				getEntityManager().persist(aRef[i]);
				logger.log(Logger.Level.TRACE, "persisted Address " + aRef[i]);
				getEntityManager().persist(ptRef[i]);
				logger.log(Logger.Level.TRACE, "persisted employee " + ptRef[i]);
			}
			getEntityTransaction().commit();
		} catch (Exception re) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception creating test data:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX:", re);
			}
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
			getEntityManager().createNativeQuery("Delete from EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("Delete from PARTTIMEEMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("Delete from ADDRESS").executeUpdate();
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
