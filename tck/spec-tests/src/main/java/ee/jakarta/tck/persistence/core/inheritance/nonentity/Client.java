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

package ee.jakarta.tck.persistence.core.inheritance.nonentity;

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
		String[] classes = { pkgName + "Department", pkgName + "Employee", pkgName + "FTEmployee",
				pkgName + "FullTimeEmployee", pkgName + "PartTimeEmployee", pkgName + "Personnel",
				pkgName + "Project" };
		return createDeploymentJar("jpa_core_inheritance_nonentity.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: nonEntityTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:589; PERSISTENCE:SPEC:590;
	 * PERSISTENCE:SPEC:591; PERSISTENCE:SPEC:588; PERSISTENCE:SPEC:603;
	 * PERSISTENCE:JAVADOC:25; PERSISTENCE:SPEC:1126; PERSISTENCE:SPEC:1126.1;
	 * PERSISTENCE:SPEC:1126.3; PERSISTENCE:SPEC:1126.4; PERSISTENCE:JAVADOC:26;
	 * PERSISTENCE:JAVADOC:28; PERSISTENCE:SPEC:1112; PERSISTENCE:SPEC:509;
	 * PERSISTENCE:SPEC:1113; PERSISTENCE:SPEC:1116; PERSISTENCE:SPEC:1118;
	 * PERSISTENCE:SPEC:1119; PERSISTENCE:SPEC:510; PERSISTENCE:SPEC:1217;
	 * PERSISTENCE:SPEC:1219; PERSISTENCE:SPEC:2029;
	 * 
	 * @test_Strategy: An entity may have a non-entity superclass which may be
	 * either abstract or concrete.
	 */
	@Test
	public void nonEntityTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin nonEntityTest1");
		boolean pass = false;
		String reason = null;

		try {
			FullTimeEmployee ftEmp1 = getEntityManager().find(FullTimeEmployee.class, 1);

			if (ftEmp1.getFullTimeRep().equals("Mabel Murray")) {
				if (ftEmp1.getHireDate() == null) {
					pass = true;
				} else {
					reason = "hireDate is declared in non-entity superclass"
							+ " and it should not be persisted. Expected null but" + " got " + ftEmp1.getHireDate();
				}
			}

		} catch (Exception e) {
			// @todo should we also fail the test here?
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("nonEntityTest1 failed, reason: " + reason);
	}

	/*
	 * @testName: nonEntityTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:589; PERSISTENCE:SPEC:590;
	 * PERSISTENCE:SPEC:591; PERSISTENCE:SPEC:588; PERSISTENCE:SPEC:603;
	 * PERSISTENCE:JAVADOC:25; PERSISTENCE:SPEC:1126; PERSISTENCE:SPEC:1126.1;
	 * PERSISTENCE:SPEC:1126.3; PERSISTENCE:SPEC:1126.4; PERSISTENCE:JAVADOC:26;
	 * PERSISTENCE:JAVADOC:28; PERSISTENCE:SPEC:1112; PERSISTENCE:SPEC:1113;
	 * PERSISTENCE:SPEC:1116; PERSISTENCE:SPEC:1118; PERSISTENCE:SPEC:2029;
	 * 
	 * @test_Strategy: An entity may have a non-entity superclass which may be
	 * either abstract or concrete.
	 */
	@Test
	public void nonEntityTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin nonEntityTest2");
		boolean pass = false;
		String reason = null;

		try {
			PartTimeEmployee ptEmp1 = getEntityManager().find(PartTimeEmployee.class, 6);

			if (ptEmp1.getPartTimeRep().equals("John Cleveland")) {
				if (ptEmp1.getHireDate() == null) {
					pass = true;
				} else {
					reason = "hireDate is declared in non-entity superclass"
							+ " and it should not be persisted. Expected null but" + " got " + ptEmp1.getHireDate();
				}
			}

		} catch (Exception e) {
			// @todo should we also fail here?
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("nonEntityTest2 failed, reason: " + reason);
	}

	/*
	 * @testName: nonEntityTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:53; PERSISTENCE:SPEC:2029;
	 * 
	 * @test_Strategy: An IllegalArgumentException is thrown trying to persist an
	 * Object that is not an entity. Exercise this assertion in an inheritance
	 * hierarchy attempting to persist a non-entity class extended from an entity
	 * class.
	 */
	@Test
	public void nonEntityTest3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin nonEntityTest3");
		boolean pass = false;
		String reason = null;
		FTEmployee ftEmp = new FTEmployee(99, "Joe", "Jones", d6, 20000.0F);

		try {

			getEntityTransaction().begin();
			getEntityManager().persist(ftEmp);
			getEntityTransaction().commit();

		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException caught as expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			reason = "Did not get the Expected Exception";
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}

		}

		if (!pass)
			throw new Exception("nonEntityTest3 failed, reason: " + reason);
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
					getEntityManager().flush();
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

			clearCache();

		} catch (Exception re) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception creating test data:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
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
