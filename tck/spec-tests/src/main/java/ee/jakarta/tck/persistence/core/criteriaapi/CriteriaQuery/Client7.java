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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaQuery;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Department;
import ee.jakarta.tck.persistence.common.schema30.Department_;
import ee.jakarta.tck.persistence.common.schema30.UtilDepartmentEmployeeData;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class Client7 extends UtilDepartmentEmployeeData {

	private static final Logger logger = (Logger) System.getLogger(Client7.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client7.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A" };
		classes = concat(getSchema30classes(), classes);

		return createDeploymentJar("jpa_core_criteriaapi_CriteriaQuery7.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: resultContainsFetchReference
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1718;
	 * 
	 * @test_Strategy: SELECT d FROM Department d LEFT JOIN FETCH
	 * d.lastNameEmployees WHERE d.id = 1
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
	@Test
	public void resultContainsFetchReference() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
		if (cquery != null) {

			Root<Department> d = cquery.from(Department.class);
			d.fetch(Department_.lastNameEmployees, JoinType.LEFT);
			cquery.where(cbuilder.equal(d.get(Department_.id), 1)).select(d);
			cquery.distinct(true);
			Query q = getEntityManager().createQuery(cquery);
			List<Department> result = q.getResultList();

			List<Integer> expected = new ArrayList<Integer>();
			expected.add(deptRef[0].getId());

			if (result.size() == 1) {
				List<Integer> actual = new ArrayList<Integer>();
				actual.add(result.get(0).getId());
				if (!checkEntityPK(actual, expected)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
							+ " references, got: " + result.size());
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "More than 1 result got returned:");
				for (Department dept : result) {
					logger.log(Logger.Level.ERROR, "Dept:" + dept.toString());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("resultContainsFetchReference test failed");

		}
	}
}
