/*
 * Copyright (c) 2009, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.derivedid.ex3a;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DID3Dependent", pkgName + "DID3DependentId", pkgName + "DID3Employee",
				pkgName + "DID3EmployeeId" };
		return createDeploymentJar("jpa_core_derivedid_ex3a.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: DIDTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:1341; PERSISTENCE:SPEC:1182;
	 * PERSISTENCE:SPEC:1183; PERSISTENCE:SPEC:1184; PERSISTENCE:SPEC:1185;
	 *
	 * @test_Strategy: Derived Identifier The parent entity uses EmbeddedId Case
	 * (a): The dependent entity uses IdClass
	 */
	@Test
	public void DIDTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			final DID3EmployeeId eId1 = new DID3EmployeeId("Java", "Duke");
			final DID3EmployeeId eId2 = new DID3EmployeeId("C", "foo");

			final DID3Employee employee1 = new DID3Employee(eId1);
			final DID3Employee employee2 = new DID3Employee(eId2);

			final DID3DependentId depId1 = new DID3DependentId("Obama", eId1);
			final DID3DependentId depId2 = new DID3DependentId("Michelle", eId1);
			final DID3DependentId depId3 = new DID3DependentId("John", eId2);

			final DID3Dependent dep1 = new DID3Dependent(depId1, employee1);
			final DID3Dependent dep2 = new DID3Dependent(depId2, employee1);
			final DID3Dependent dep3 = new DID3Dependent(depId3, employee2);

			getEntityManager().persist(employee1);
			getEntityManager().persist(employee2);
			getEntityManager().persist(dep1);
			getEntityManager().persist(dep2);
			getEntityManager().persist(dep3);

			getEntityManager().flush();
			logger.log(Logger.Level.TRACE, "persisted Employees and Dependents");

			// Refresh dependent
			DID3Dependent newDependent = getEntityManager().find(DID3Dependent.class, depId1);
			if (newDependent != null) {
				getEntityManager().refresh(newDependent);
			}

			List depList = getEntityManager()
					.createQuery("Select d from DID3Dependent d where d.name2='Obama' and d.emp.empId.firstName='Java'")
					.getResultList();
			newDependent = null;
			if (depList.size() > 0) {
				newDependent = (DID3Dependent) depList.get(0);
				if (newDependent == dep1) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Received Expected Dependent");
				} else {
					logger.log(Logger.Level.ERROR, "Searched Dependent not found");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntityManager().createQuery returned null entry");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			getEntityTransaction().rollback();
		}

		if (!pass) {
			throw new Exception("DTDTest failed");
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
			getEntityManager().createNativeQuery("DELETE FROM DID3DEPENDENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID3EMPLOYEE").executeUpdate();
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
