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

package ee.jakarta.tck.persistence.core.criteriaapi.Root;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Department;
import ee.jakarta.tck.persistence.common.schema30.Department_;
import ee.jakarta.tck.persistence.common.schema30.Employee;
import ee.jakarta.tck.persistence.common.schema30.UtilDepartmentEmployeeData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Root;

public class Client4 extends UtilDepartmentEmployeeData {

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client4.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_root4.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: joinMapAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1139;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d FROM Department d JOIN d.lastNameEmployees e WHERE (e.id = 1)
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
			CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
			Root<Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> employee = department.join(Department_.lastNameEmployees);
			cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
			TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
			List<Department> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1143;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d FROM Department d INNER JOIN d.lastNameEmployees e WHERE (e.id = 1)
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void joinMapAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
			Root<Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> employee = department.join(Department_.lastNameEmployees,
					JoinType.INNER);
			cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
			TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
			List<Department> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1156;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d FROM Department d JOIN d.lastNameEmployees e WHERE (e.id = 1)
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void joinMapStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();

			CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
			Root<Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> employee = department.joinMap("lastNameEmployees");
			cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
			TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
			List<Department> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1158;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d FROM Department d INNER JOIN d.lastNameEmployees e WHERE (e.id = 1)
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void joinMapStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();

			CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
			Root<Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> employee = department.joinMap("lastNameEmployees", JoinType.INNER);
			cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
			TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
			List<Department> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
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
}
