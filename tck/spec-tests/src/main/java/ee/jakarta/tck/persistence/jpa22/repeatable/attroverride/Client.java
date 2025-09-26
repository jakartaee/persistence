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

package ee.jakarta.tck.persistence.jpa22.repeatable.attroverride;

import java.lang.System.Logger;
import java.sql.Date;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

/*
 * @since 2.2
 */
public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 22L;

	private static FullTimeEmployee ftRef[] = new FullTimeEmployee[5];

	private static PartTimeEmployee ptRef[] = new PartTimeEmployee[5];

	final private Date d1 = getSQLDate(2000, 2, 14);

	final private Date d2 = getSQLDate(2001, 6, 27);

	final private Date d3 = getSQLDate(2002, 7, 7);

	final private Date d4 = getSQLDate(2003, 3, 3);

	final private Date d5 = getSQLDate(2004, 4, 10);

	final private Date d6 = getSQLDate(2005, 2, 18);

	final private Date d7 = getSQLDate(2000, 9, 17);

	final private Date d8 = getSQLDate(2001, 11, 14);

	final private Date d9 = getSQLDate(2002, 10, 4);

	final private Date d10 = getSQLDate(2003, 1, 25);

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "AbstractPersonnel", pkgName + "Department", pkgName + "Employee",
				pkgName + "FullTimeEmployee", pkgName + "PartTimeEmployee", pkgName + "Project" };
		return createDeploymentJar("jpa_jpa22_repeatable_attroverride.jar", pkgNameWithoutSuffix, (String[]) classes);

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
	 * @assertion_ids: PERSISTENCE:JAVADOC:4; PERSISTENCE:JAVADOC:5;
	 * PERSISTENCE:JAVADOC:6;
	 * 
	 * @test_Strategy: use core/annotations/mappedsc without @AttributeOverrides
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:4; PERSISTENCE:JAVADOC:5;
	 * PERSISTENCE:JAVADOC:6;
	 * 
	 * @test_Strategy: use core/annotations/mappedsc without @AttributeOverrides
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
			for (FullTimeEmployee fte : ftRef) {
				getEntityManager().persist(fte);
				logger.log(Logger.Level.TRACE, "persisted employee " + fte);
			}

			ptRef[0] = new PartTimeEmployee(6, "Kellie", "Lee", d5, 60000.0F);
			ptRef[1] = new PartTimeEmployee(7, "Nicole", "Martin", d4, 60000.0F);
			ptRef[2] = new PartTimeEmployee(8, "Mark", "Francis", d3, 60000.0F);
			ptRef[3] = new PartTimeEmployee(9, "Will", "Forrest", d2, 60000.0F);
			ptRef[4] = new PartTimeEmployee(10, "Katy", "Hughes", d1, 60000.0F);

			logger.log(Logger.Level.TRACE, "Persist part time employees ");
			for (PartTimeEmployee pte : ptRef) {
				getEntityManager().persist(pte);
				logger.log(Logger.Level.TRACE, "persisted employee " + pte);
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
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from PARTTIMEEMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("Delete from EMPLOYEE").executeUpdate();
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
