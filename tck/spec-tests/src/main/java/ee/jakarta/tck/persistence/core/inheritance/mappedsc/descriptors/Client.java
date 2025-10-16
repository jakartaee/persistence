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

package ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors;

import java.lang.System.Logger;
import java.sql.Date;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final FullTimeEmployee ftRef[] = new FullTimeEmployee[5];

	private static final PartTimeEmployee ptRef[] = new PartTimeEmployee[5];

	private final Date d1 = getSQLDate(2000, 2, 14);

	private final Date d2 = getSQLDate(2001, 6, 27);

	private final Date d3 = getSQLDate(2002, 7, 7);

	private final Date d4 = getSQLDate(2003, 3, 3);

	private final Date d5 = getSQLDate(2004, 4, 10);

	private final Date d6 = getSQLDate(2005, 2, 18);

	private final Date d7 = getSQLDate(2000, 9, 17);

	private final Date d8 = getSQLDate(2001, 11, 14);

	private final Date d9 = getSQLDate(2002, 10, 4);

	private final Date d10 = getSQLDate(2003, 1, 25);

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { ORM_XML };
		String[] classes = { pkgName + "AbstractPersonnel", pkgName + "Department", pkgName + "Employee",
				pkgName + "FullTimeEmployee", pkgName + "PartTimeEmployee", pkgName + "Project" };
		return createDeploymentJar("jpa_core_mappedsc_descriptors.jar", pkgNameWithoutSuffix, classes, xmlFiles);

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
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:593; PERSISTENCE:SPEC:596;
	 * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
	 * PERSISTENCE:SPEC:1130; PERSISTENCE:SPEC:1131; PERSISTENCE:SPEC:1132;
	 * PERSISTENCE:SPEC:1133; PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:600
	 * 
	 * @test_Strategy: An entity may have a mapped superclass which provides
	 * persistent entity state and mapping information
	 */
	@Test
	public void test1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test1");
		boolean pass = false;

		try {
			FullTimeEmployee ftEmp1 = getEntityManager().find(FullTimeEmployee.class, 1);

			if (ftEmp1.getFullTimeRep().equals("Mabel Murray")) {
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("test1 failed");
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:593; PERSISTENCE:SPEC:596;
	 * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
	 * PERSISTENCE:SPEC:1130; PERSISTENCE:SPEC:1131; PERSISTENCE:SPEC:1132;
	 * PERSISTENCE:SPEC:1133; PERSISTENCE:SPEC:1144
	 * 
	 * @test_Strategy: An entity may have a mapped superclass which provides
	 * persistent entity state and mapping information
	 */
	@Test
	public void test2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test2");
		boolean pass = false;

		try {
			PartTimeEmployee ptEmp1 = getEntityManager().find(PartTimeEmployee.class, 6);

			if (ptEmp1.getPartTimeRep().equals("John Cleveland")) {
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("test2 failed");
	}

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");

		try {

			getEntityTransaction().begin();
			ftRef[0] = new FullTimeEmployee(1, "Jonathan", "Smith", d10, 40000.0F);
			ftRef[1] = new FullTimeEmployee(2, "Mary", "Macy", d9, 40000.0F);
			ftRef[2] = new FullTimeEmployee(3, "Sid", "Nee", d8, 40000.0F);
			ftRef[3] = new FullTimeEmployee(4, "Julie", "OClaire", d7, 60000.0F);
			ftRef[4] = new FullTimeEmployee(5, "Steven", "Rich", d6, 60000.0F);

			logger.log(Logger.Level.TRACE, "Persist full time employees ");
			for (FullTimeEmployee ft : ftRef) {
				if (ft != null) {
					getEntityManager().persist(ft);
					logger.log(Logger.Level.TRACE, "persisted employee " + ft);
				}
			}

			ptRef[0] = new PartTimeEmployee(6, "Kellie", "Lee", d5, 60000.0F);
			ptRef[1] = new PartTimeEmployee(7, "Nicole", "Martin", d4, 60000.0F);
			ptRef[2] = new PartTimeEmployee(8, "Mark", "Francis", d3, 60000.0F);
			ptRef[3] = new PartTimeEmployee(9, "Will", "Forrest", d2, 60000.0F);
			ptRef[4] = new PartTimeEmployee(10, "Katy", "Hughes", d1, 60000.0F);

			logger.log(Logger.Level.TRACE, "Persist part time employees ");
			for (PartTimeEmployee pt : ptRef) {
				if (pt != null) {
					getEntityManager().persist(pt);
					getEntityManager().flush();
					logger.log(Logger.Level.TRACE, "persisted employee " + pt);
				}
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
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
