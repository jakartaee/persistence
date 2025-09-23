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

package ee.jakarta.tck.persistence.core.annotations.orderby;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client1 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	List<Address> addrRef;

	Address addr1 = null;

	Address addr2 = null;

	Address addr3 = null;

	List<Address2> addrRef2;

	Address2 addr11 = null;

	Address2 addr12 = null;

	Address2 addr13 = null;

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "A2", pkgName + "Address", pkgName + "Address2",
				pkgName + "Customer", pkgName + "Customer2", pkgName + "Department", pkgName + "Employee",
				pkgName + "Insurance", pkgName + "ZipCode", pkgName + "ZipCode2" };
		return createDeploymentJar("jpa_core_annotations_orderby1.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();

			removeTestData();
			createTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: orderByTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145;
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 * 
	 * The property name must correspond to that of a persistenct property of the
	 * associated class.
	 *
	 * The property used in the ordering must correspond to columns for which
	 * comparison operations are supported.
	 *
	 * If DESC is specified, the elements will be ordered in descending order.
	 * 
	 * Retrieve the Collection using getter property accessor.
	 */
	@Test
	public void orderByTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin orderByTest1");
		boolean pass1 = true;
		boolean pass2 = false;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Zoe", "Song", "Jie", "Ay" };

		try {
			getEntityTransaction().begin();

			Employee empChange = getEntityManager().find(Employee.class, 65);

			empChange.setFirstName("Ay");
			getEntityManager().merge(empChange);
			getEntityManager().flush();

			final Insurance newIns = getEntityManager().find(Insurance.class, 60);
			getEntityManager().refresh(newIns);

			final List insResult = newIns.getEmployees();

			if (insResult.size() != 4) {
				logger.log(Logger.Level.TRACE,
						"orderByTest1:  Did not get expected results.  Expected: 4, " + "got: " + insResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i1 = insResult.iterator();
				logger.log(Logger.Level.TRACE, "Check Employee Collection for expected first names");
				while (i1.hasNext()) {
					Employee e1 = (Employee) i1.next();
					resultsList.add((String) e1.getFirstName());
					logger.log(Logger.Level.TRACE,
							"orderByTest1: got Employee FirstName:" + (String) e1.getFirstName());
				}

				logger.log(Logger.Level.TRACE, "Compare first names received with expected first names ");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);

			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass2 = false;
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
			throw new Exception("orderByTest1 failed");
	}

	/*
	 * @testName: orderByTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:SPEC:1109; PERSISTENCE:SPEC:1110;
	 * PERSISTENCE:JAVADOC:145; PERSISTENCE:SPEC:653
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 *
	 * The property name must correspond to that of a persistenct property of the
	 * associated class.
	 *
	 * The property used in the ordering must correspond to columns for which
	 * comparison operations are supported.
	 *
	 * If ASC is specified, the elements will be ordered in ascending order.
	 * 
	 * Retrieve the Collection using getter property accessor.
	 */
	@Test
	public void orderByTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin orderByTest2");
		boolean pass1 = true;
		boolean pass2 = false;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Jie", "Song", "Yay", "Zoe" };

		try {
			getEntityTransaction().begin();

			Employee emp2Change = getEntityManager().find(Employee.class, 65);

			emp2Change.setFirstName("Yay");
			getEntityManager().merge(emp2Change);
			getEntityManager().flush();

			final Department newDept = getEntityManager().find(Department.class, 50);
			getEntityManager().refresh(newDept);

			final List deptResult = newDept.getEmployees();

			if (deptResult.size() != 4) {
				logger.log(Logger.Level.TRACE,
						"orderByTest2:  Did not get expected results.  Expected: 4, " + "got: " + deptResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i2 = deptResult.iterator();
				logger.log(Logger.Level.TRACE, "Check Employee Collection for expected first names");
				while (i2.hasNext()) {
					Employee e2 = (Employee) i2.next();
					resultsList.add((String) e2.getFirstName());
					logger.log(Logger.Level.TRACE,
							"orderByTest2: got Employee FirstName:" + (String) e2.getFirstName());
				}

				logger.log(Logger.Level.TRACE, "Compare first names received with expected first names ");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);

			}

			getEntityTransaction().commit();
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
			throw new Exception("orderByTest2 failed");
	}

	/*
	 * @testName: orderByTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 *
	 * If DESC is specified, the elements will be ordered in descending order.
	 * 
	 * Add to the Collection then retrieve the updated Collection and ensure the
	 * list is ordered.
	 * 
	 */
	@Test
	public void orderByTest3() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin orderByTest3");
		boolean pass1 = true;
		boolean pass2 = false;
		List insResult;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Zoe", "Song", "Penelope", "May", "Jie" };

		try {
			getEntityTransaction().begin();

			Employee emp3Change = getEntityManager().find(Employee.class, 85);
			Insurance ins = getEntityManager().find(Insurance.class, 60);

			emp3Change.setInsurance(ins);
			getEntityManager().merge(emp3Change);
			ins.getEmployees().add(emp3Change);
			getEntityManager().merge(ins);
			getEntityManager().flush();

			getEntityManager().refresh(ins);

			insResult = ins.getEmployees();

			if (insResult.size() != 5) {
				logger.log(Logger.Level.ERROR, "orderByTest3: Expected List Size of 5 " + "got: " + insResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i3 = insResult.iterator();
				logger.log(Logger.Level.TRACE, "Check Employee Collection for expected first names");
				while (i3.hasNext()) {
					Employee e3 = (Employee) i3.next();
					resultsList.add((String) e3.getFirstName());
					logger.log(Logger.Level.TRACE,
							"orderByTest3: got Employee FirstName:" + (String) e3.getFirstName());
				}

				logger.log(Logger.Level.TRACE, "orderByTest3: Expected size received, check ordering . . .");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);

			}

			getEntityTransaction().commit();

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
			throw new Exception("orderByTest3 failed");
	}

	/*
	 * @testName: orderByTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 * 
	 * If ASC is specified, the elements will be ordered in ascending order.
	 * 
	 * Retrieve the Collection, add to the Collection and retrieve it again making
	 * sure the list is ordered .
	 * 
	 */
	@Test
	public void orderByTest4() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin orderByTest4");
		boolean pass1 = true;
		boolean pass2 = false;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Jie", "May", "Penelope", "Song", "Zoe" };

		try {
			getEntityTransaction().begin();

			Employee emp4Change = getEntityManager().find(Employee.class, 85);
			Department dept = getEntityManager().find(Department.class, 50);

			emp4Change.setDepartment(dept);
			getEntityManager().merge(emp4Change);
			dept.getEmployees().add(emp4Change);
			getEntityManager().merge(dept);
			getEntityManager().flush();

			getEntityManager().refresh(dept);
			final List deptResult = dept.getEmployees();

			if (deptResult.size() != 5) {
				logger.log(Logger.Level.ERROR,
						"orderByTest4: Expected Collection Size of 5 " + "got: " + deptResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i4 = deptResult.iterator();
				logger.log(Logger.Level.TRACE, "Check Employee Collection for expected first names");
				while (i4.hasNext()) {
					Employee e4 = (Employee) i4.next();
					resultsList.add((String) e4.getFirstName());
					logger.log(Logger.Level.TRACE,
							"orderByTest4: got Employee FirstName:" + (String) e4.getFirstName());
				}

				logger.log(Logger.Level.TRACE, "orderByTest4: Expected size received, check ordering . . .");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);
			}

			getEntityTransaction().commit();

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
			throw new Exception("orderByTest4 failed");
	}

	private void createTestData() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "createTestData");
			getEntityTransaction().begin();
			final float salary = 10000.00F;

			Department d1 = new Department(50, "SJSAS Appserver");
			getEntityManager().persist(d1);

			Insurance s1 = new Insurance(60, "United");
			getEntityManager().persist(s1);

			final Employee e1 = new Employee(70, "Jie", "Leng", salary, d1, s1);
			final Employee e2 = new Employee(80, "Zoe", "Leng", salary, d1, s1);
			final Employee e3 = new Employee(90, "Song", "Leng", salary, d1, s1);
			final Employee e4 = new Employee(65, "May", "Leng", salary, d1, s1);
			final Employee e5 = new Employee(85, "Penelope", "Leng", salary);
			getEntityManager().persist(e1);
			getEntityManager().persist(e2);
			getEntityManager().persist(e3);
			getEntityManager().persist(e4);
			getEntityManager().persist(e5);

			List<Employee> link = new ArrayList<Employee>();
			link.add(e1);
			link.add(e2);
			link.add(e3);
			link.add(e4);

			d1.setEmployees(link);
			getEntityManager().merge(d1);

			s1.setEmployees(link);
			getEntityManager().merge(s1);

			logger.log(Logger.Level.TRACE, "persisted Entity Data");
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
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from INSURANCE").executeUpdate();
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
