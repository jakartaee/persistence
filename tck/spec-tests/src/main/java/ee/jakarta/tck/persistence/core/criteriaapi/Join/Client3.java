/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.Join;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Department;
import ee.jakarta.tck.persistence.common.schema30.Department_;
import ee.jakarta.tck.persistence.common.schema30.Employee;
import ee.jakarta.tck.persistence.common.schema30.Employee_;
import ee.jakarta.tck.persistence.common.schema30.UtilDepartmentEmployeeData;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.PluralAttribute;

public class Client3 extends UtilDepartmentEmployeeData {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_join.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: joinMapAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1047; PERSISTENCE:JAVADOC:1081;
	 * PERSISTENCE:JAVADOC:1078;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT e FROM Employee e JOIN e.department d JOIN d.lastNameEmployees e1
	 * WHERE (e1.id = 1)
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void joinMapAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
			Root<Employee> employee = cquery.from(Employee.class);
			Join<Employee, Department> department = employee.join(Employee_.department);
			MapJoin<Department, String, Employee> mEmployee = department.join(Department_.lastNameEmployees);
			PluralAttribute pa = mEmployee.getModel();
			String name = pa.getName();
			if (name.equals("lastNameEmployees")) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + name);
			} else {
				logger.log(Logger.Level.ERROR, "getModel - Expected: lastNameEmployees, actual:" + name);
			}
			cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
			TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
			List<Employee> clist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "5";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapAttributeTest failed");
		}
	}

	/*
	 * @testName: joinMapAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1051;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT e FROM Employee e JOIN e.department d INNER JOIN d.lastNameEmployees
	 * e1 WHERE (e1.id = 1)
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void joinMapAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
			Root<Employee> employee = cquery.from(Employee.class);
			Join<Employee, Department> department = employee.join(Employee_.department);
			MapJoin<Department, String, Employee> mEmployee = department.join(Department_.lastNameEmployees,
					JoinType.INNER);
			cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
			TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
			List<Employee> clist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "5";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinMapStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1064; PERSISTENCE:JAVADOC:1112;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT e FROM Employee e JOIN e.department d JOIN d.lastNameEmployees e1
	 * WHERE (e1.id = 1)
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void joinMapStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
			Root<Employee> employee = cquery.from(Employee.class);
			Join<Employee, Department> department = employee.join(Employee_.department);
			MapJoin<Department, String, Employee> mEmployee = department.joinMap("lastNameEmployees");
			String name = mEmployee.getModel().getName();
			if (name.equals("lastNameEmployees")) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + name);
			} else {
				logger.log(Logger.Level.ERROR, "getModel - Expected: lastNameEmployees, actual:" + name);
			}
			cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
			TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
			List<Employee> clist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "5";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapStringTest failed");
		}
	}

	/*
	 * @testName: joinMapStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1066;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT e FROM Employee e JOIN e.department d INNER JOIN d.lastNameEmployees
	 * e1 WHERE (e1.id = 1)
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void joinMapStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
			Root<Employee> employee = cquery.from(Employee.class);
			Join<Employee, Department> department = employee.join(Employee_.department);
			MapJoin<Department, String, Employee> mEmployee = department.joinMap("lastNameEmployees", JoinType.INNER);
			cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
			TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
			List<Employee> clist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "5";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: mapJoinValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1080;
	 * 
	 * @test_Strategy:
	 *
	 * SELECT e.id, e.firstname, d.LASTNAME FROM Employee e Join e.lastNameEmployees
	 * d
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void mapJoinValueTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();

		expected.add("1, Alan, Frechette");
		expected.add("1, Alan, McGowan");
		expected.add("1, Alan, DMilla");
		expected.add("3, Shelly, Frechette");
		expected.add("3, Shelly, McGowan");
		expected.add("3, Shelly, DMilla");
		expected.add("5, Stephen, Frechette");
		expected.add("5, Stephen, McGowan");
		expected.add("5, Stephen, DMilla");
		expected.add("2, Arthur, Frechette");
		expected.add("2, Arthur, Bissett");
		expected.add("4, Robert, Frechette");
		expected.add("4, Robert, Bissett");

		List<String> actual = new ArrayList<String>();

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			Root<Employee> employee = cquery.from(Employee.class);
			Join<Employee, Department> department = employee.join(Employee_.department);
			MapJoin<Department, String, Employee> mEmployee = department.join(Department_.lastNameEmployees);
			cquery.multiselect(employee.get(Employee_.id), employee.get(Employee_.firstName),
					mEmployee.value().<String>get("lastName"));
			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			List<Tuple> clist = tquery.getResultList();

			for (Tuple t : clist) {
				logger.log(Logger.Level.TRACE, "result:" + t.get(0) + ", " + t.get(1) + ", " + t.get(2));
				actual.add(t.get(0) + ", " + t.get(1) + ", " + t.get(2));
			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results:");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("mapJoinValueTest failed");
		}
	}
}
