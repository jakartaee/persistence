/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkey;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client2 extends Client {

	public Client2() {
	}

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "Department", pkgName + "Employee", pkgName + "Employee2", pkgName + "Employee3",
				pkgName + "Employee4" };
		return createDeploymentJar("jpa_core_annotations_mapkey2.jar", pkgNameWithoutSuffix, classes);

	}

	private Employee2 empRef2;

	private Employee3 empRef3;

	private Employee4 empRef4;

	@BeforeEach
	public void setupCreateTestData2() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData2();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: joinColumnInsertable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:90
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of insertable
	 * used to specify the mapping for the fk column to a second entity Execute a
	 * query returning Employees objects.
	 */
	@Test
	public void joinColumnInsertable() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			logger.log(Logger.Level.TRACE, "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			Department dept = emp2.getDepartment();

			if (dept == null) {
				logger.log(Logger.Level.TRACE, "Received expected null department for employee2");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected null department, actual:" + dept.getName());
			}
			clearCache();

			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			logger.log(Logger.Level.TRACE, "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			dept = emp3.getDepartment();

			if (dept != null && dept.getName().equals(deptRef[0].getName())) {
				logger.log(Logger.Level.TRACE, "Received expected department for employee3:" + dept.getName());
			} else {
				pass = false;
				if (dept != null) {
					logger.log(Logger.Level.ERROR,
							"Expected department:" + deptRef[0].getName() + ", actual:" + dept.getName());
				} else {
					logger.log(Logger.Level.ERROR, "Expected department:" + deptRef[0].getName() + ", actual:null");
				}
			}

			clearCache();
			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			logger.log(Logger.Level.TRACE, "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			dept = emp4.getDepartment();

			if (dept == null) {
				logger.log(Logger.Level.TRACE, "Received expected null department for employee4");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected department: null, actual:" + dept.getName());
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("joinColumnInsertable Failed");
		}
	}

	/*
	 * @testName: joinColumnUpdatable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:96
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of updatable used
	 * to specify the mapping for the fk column to a second entity Execute a query
	 * returning Employees objects.
	 */
	@Test
	public void joinColumnUpdatable() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			logger.log(Logger.Level.TRACE, "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			logger.log(Logger.Level.TRACE, "set department to:" + deptRef[1].getId() + ", " + deptRef[1].getName());
			emp2.setDepartment(deptRef[1]);
			getEntityManager().merge(emp2);
			getEntityManager().flush();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee2 again");
			emp2 = getEntityManager().find(Employee2.class, 6);
			logger.log(Logger.Level.TRACE, "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			Department dept = emp2.getDepartment();
			if (dept == null) {
				logger.log(Logger.Level.TRACE, "Received expected null department");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected null department, actual:" + dept.getName());
			}

			clearCache();
			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			logger.log(Logger.Level.TRACE, "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			logger.log(Logger.Level.TRACE,
					"Department:" + emp3.getDepartment().getId() + ", " + emp3.getDepartment().getName());
			logger.log(Logger.Level.TRACE, "set department to:" + deptRef[1].getId() + ", " + deptRef[1].getName());
			emp3.setDepartment(deptRef[1]);
			getEntityManager().merge(emp3);
			getEntityManager().flush();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee3 again");
			emp3 = getEntityManager().find(Employee3.class, 7);
			logger.log(Logger.Level.TRACE, "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			dept = emp3.getDepartment();
			if (dept != null && dept.getName().equals(deptRef[0].getName())) {
				logger.log(Logger.Level.TRACE, "Received expected department:" + dept.getName());
			} else {
				pass = false;
				if (dept != null) {
					logger.log(Logger.Level.ERROR,
							"Expected department:" + deptRef[0].getName() + ", actual:" + dept.getName());
				} else {
					logger.log(Logger.Level.ERROR, "Expected department:" + deptRef[0].getName() + ", actual:null");
				}
			}

			clearCache();
			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			logger.log(Logger.Level.TRACE, "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			if (emp4.getFirstName() != null) {
				logger.log(Logger.Level.ERROR, "Expected first name to be null, actual:" + emp4.getFirstName());
				pass = false;
			}
			if (emp4.getDepartment() != null) {
				logger.log(Logger.Level.ERROR,
						"Expected Department to be null, actual:" + emp4.getDepartment().toString());
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "set department to:" + deptRef[1].getId() + ", " + deptRef[1].getName());
			emp4.setDepartment(deptRef[1]);
			getEntityManager().merge(emp4);
			getEntityManager().flush();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee4 again");
			emp4 = getEntityManager().find(Employee4.class, 8);
			logger.log(Logger.Level.TRACE, "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			dept = emp4.getDepartment();
			if (dept != null && dept.getName().equals(deptRef[1].getName())) {
				logger.log(Logger.Level.TRACE, "Received expected department:" + dept.getName());
			} else {
				pass = false;
				if (dept != null) {
					logger.log(Logger.Level.ERROR,
							"Expected " + deptRef[1].getName() + " department, actual:" + dept.getName());
				} else {
					logger.log(Logger.Level.ERROR, "Expected " + deptRef[1].getName() + " department, actual:null");
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("joinColumnUpdatable Failed");
		}
	}

	/*
	 * @testName: columnInsertable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:12
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of insertable
	 * used to specify the mapping for the fk column to a second entity Execute a
	 * query returning Employees objects.
	 */
	@Test
	public void columnInsertable() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			String firstName = emp2.getFirstName();
			logger.log(Logger.Level.TRACE, "Name:" + firstName + " " + emp2.getLastName());

			if (firstName == null) {
				logger.log(Logger.Level.TRACE, "Received expected null firstName");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected firstName: null, actual:" + firstName);
			}

			clearCache();
			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			firstName = emp3.getFirstName();
			logger.log(Logger.Level.TRACE, "Name:" + firstName + " " + emp3.getLastName());

			if (firstName != null && firstName.equals("Paul")) {
				logger.log(Logger.Level.TRACE, "Received expected firstName:" + firstName);
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected firstName: Paul, actual: null");
			}

			clearCache();
			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			firstName = emp4.getFirstName();
			logger.log(Logger.Level.TRACE, "Name:" + firstName + " " + emp4.getLastName());
			if (firstName == null) {
				logger.log(Logger.Level.TRACE, "Received expected null firstName");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected firstName: null, actual:" + firstName);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("columnInsertable Failed");
		}
	}

	/*
	 * @testName: columnUpdatable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:20
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of updatable used
	 * to specify the mapping for the fk column to a second entity Execute a query
	 * returning Employees objects.
	 */
	@Test
	public void columnUpdatable() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			logger.log(Logger.Level.TRACE, "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			logger.log(Logger.Level.TRACE, "set firstName and save");
			emp2.setFirstName("foo");
			getEntityManager().merge(emp2);
			getEntityManager().flush();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee2 again");
			emp2 = getEntityManager().find(Employee2.class, 6);
			logger.log(Logger.Level.TRACE, "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			String firstName = emp2.getFirstName();
			if (firstName == null) {
				logger.log(Logger.Level.TRACE, "Received expected null firstName");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected firstName: null, actual:" + firstName);
			}

			clearCache();
			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			logger.log(Logger.Level.TRACE, "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			logger.log(Logger.Level.TRACE, "set firstName and save");
			emp3.setFirstName("foo");
			getEntityManager().merge(emp3);
			getEntityManager().flush();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee3 again");
			emp3 = getEntityManager().find(Employee3.class, 7);
			logger.log(Logger.Level.TRACE, "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			firstName = emp3.getFirstName();
			if (firstName != null && firstName.equals("Paul")) {
				logger.log(Logger.Level.TRACE, "Received expected firstName:" + firstName);
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected firstName: Paul, actual: null");
			}

			clearCache();
			logger.log(Logger.Level.TRACE, "--------------");
			logger.log(Logger.Level.TRACE, "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			logger.log(Logger.Level.TRACE, "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			logger.log(Logger.Level.TRACE, "set firstName and save");
			emp4.setFirstName("foo");
			getEntityManager().merge(emp4);
			getEntityManager().flush();
			clearCache();
			logger.log(Logger.Level.TRACE, "find employee4 again");
			emp4 = getEntityManager().find(Employee4.class, 8);
			logger.log(Logger.Level.TRACE, "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			firstName = emp4.getFirstName();
			if (firstName != null && firstName.equals("foo")) {
				logger.log(Logger.Level.TRACE, "Received expected firstName:" + firstName);
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected firstName: foo, actual: null");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("columnUpdatable Failed");
		}

	}

	public void createTestData2() throws Exception {
		try {

			logger.log(Logger.Level.TRACE, "createTestData2");
			createTestDataCommon();
			getEntityTransaction().begin();

			// insertable = false, updatable = false
			logger.log(Logger.Level.TRACE, "Create and persist employee2 ");
			empRef2 = new Employee2(6, "John", "Smith");
			empRef2.setDepartment(deptRef[0]);
			getEntityManager().persist(empRef2);

			// insertable = true, updatable = false
			logger.log(Logger.Level.TRACE, "Create and persist employee3 ");
			empRef3 = new Employee3(7, "Paul", "Jones");
			empRef3.setDepartment(deptRef[0]);
			getEntityManager().persist(empRef3);

			// insertable = false, updatable = true
			logger.log(Logger.Level.TRACE, "Create and persist employee4 ");
			empRef4 = new Employee4(8, "Thomas", "Brady");
			empRef4.setDepartment(deptRef[0]);
			getEntityManager().persist(empRef4);
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception creating test data:", e);
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

}
