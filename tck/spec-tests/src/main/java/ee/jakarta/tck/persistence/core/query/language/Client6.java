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

package ee.jakarta.tck.persistence.core.query.language;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Department;
import ee.jakarta.tck.persistence.common.schema30.UtilDepartmentEmployeeData;

public class Client6 extends UtilDepartmentEmployeeData {

	private static final Logger logger = (Logger) System.getLogger(Client6.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_query_language6.jar", pkgNameWithoutSuffix, classes);
	}
	/* Run test */

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
	@Disabled
	public void resultContainsFetchReference() throws Exception {
		boolean pass = false;
		List<Department> result;

		getEntityTransaction().begin();

		result = getEntityManager()
				.createQuery("SELECT d FROM Department d LEFT JOIN FETCH d.lastNameEmployees WHERE d.id = 1")
				.getResultList();

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

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("resultContainsFetchReference test failed");

		}
	}

}
