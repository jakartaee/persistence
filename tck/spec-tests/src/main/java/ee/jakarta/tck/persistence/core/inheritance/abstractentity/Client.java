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

package ee.jakarta.tck.persistence.core.inheritance.abstractentity;

import java.lang.System.Logger;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

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
		String[] classes = { pkgName + "AbstractPersonnel", pkgName + "Department", pkgName + "Employee",
				pkgName + "FullTimeEmployee", pkgName + "PartTimeEmployee", pkgName + "Project" };
		return createDeploymentJar("jpa_core_inheritance_abstractentity.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: abstractEntityTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:589; PERSISTENCE:SPEC:590;
	 * PERSISTENCE:SPEC:591; PERSISTENCE:SPEC:588; PERSISTENCE:SPEC:1126;
	 * PERSISTENCE:SPEC:1126.1; PERSISTENCE:SPEC:1126.3; PERSISTENCE:SPEC:1126.4;
	 * PERSISTENCE:JAVADOC:25; PERSISTENCE:JAVADOC:26; PERSISTENCE:JAVADOC:28;
	 * PERSISTENCE:SPEC:1112; PERSISTENCE:SPEC:1113; PERSISTENCE:SPEC:1116;
	 * PERSISTENCE:SPEC:1118; PERSISTENCE:JAVADOC:23; PERSISTENCE:JAVADOC:86;
	 * PERSISTENCE:JAVADOC:87
	 * 
	 * @test_Strategy: An entity may have a non-entity superclass which may be
	 * either abstract or concrete.
	 */
	@Test
	public void abstractEntityTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin abstractEntityTest1");
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
			throw new Exception("abstractEntityTest1 failed");
	}

	/*
	 * @testName: abstractEntityTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:589; PERSISTENCE:SPEC:590;
	 * PERSISTENCE:SPEC:591; PERSISTENCE:SPEC:588; PERSISTENCE:SPEC:1126;
	 * PERSISTENCE:SPEC:1126.1; PERSISTENCE:SPEC:1126.3; PERSISTENCE:SPEC:1126.4;
	 * PERSISTENCE:JAVADOC:25; PERSISTENCE:JAVADOC:26; PERSISTENCE:JAVADOC:28;
	 * PERSISTENCE:SPEC:1112; PERSISTENCE:SPEC:1113; PERSISTENCE:SPEC:1116;
	 * PERSISTENCE:SPEC:1118; PERSISTENCE:JAVADOC:35
	 * 
	 * @test_Strategy: An entity may have a non-entity superclass which may be
	 * either abstract or concrete.
	 */
	@Test
	public void abstractEntityTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin abstractEntityTest2");
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
			throw new Exception("abstractEntityTest2 failed");
	}

	/*
	 * @testName: abstractEntityTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:588.1;PERSISTENCE:SPEC:1352;
	 * PERSISTENCE:SPEC:1353; PERSISTENCE:SPEC:1219;
	 * 
	 * @test_Strategy: An abstract entity is mapped as an entity and can be the
	 * target of queries (which will operate over and/or retrieve instances of its
	 * concrete subclasses).
	 */
	@Test
	public void abstractEntityTest3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin abstractEntityTest3");
		boolean pass1 = true;
		boolean pass2 = true;
		List result;
		int foundEmp = 0;

		try {
			// getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find Employees By ID");
			result = getEntityManager().createQuery("Select e from Employee e where e.id between :param1 and :param2")
					.setParameter("param1", 4).setParameter("param2", 7).setMaxResults(10).getResultList();

			if (result.size() != 4) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 4 references, got: " + result.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i = result.iterator();
				while (i.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check List for Expected Employees");
					Employee e = (Employee) i.next();
					logger.log(Logger.Level.TRACE, "Got Employee: " + e.getLastName());

					if (e.getLastName().equals("Lee") || e.getLastName().equals("Martin")
							|| e.getLastName().equals("OClaire") || e.getLastName().equals("Rich")) {
						foundEmp++;
					}
				}
			}

			if (foundEmp != 4) {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				pass2 = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
			}

			// getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("abstractEntityTest3 failed");
	}

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");

		try {
			getEntityTransaction().begin();
			ftRef[0] = new FullTimeEmployee(1, "Jonathan", "Smith", d10, 40000.0F);
			ftRef[1] = new FullTimeEmployee(2, "Mary", "Macy", d9, 40000.0F);
			ftRef[2] = new FullTimeEmployee(3, "Sid", "Nee", d8, 40000.0F);
			ftRef[3] = new FullTimeEmployee(4, "Julie", "OClaire", d7, 21000.0F);
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
			ptRef[3] = new PartTimeEmployee(9, "Will", "Forrest", d2, 250000.0F);
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
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}
		logger.log(Logger.Level.TRACE, "done creating test data ");

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
