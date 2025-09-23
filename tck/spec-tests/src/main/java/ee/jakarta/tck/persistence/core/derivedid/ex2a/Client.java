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

package ee.jakarta.tck.persistence.core.derivedid.ex2a;

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
		String[] classes = { pkgName + "DID2Dependent", pkgName + "DID2DependentId", pkgName + "DID2Employee",
				pkgName + "DID2EmployeeId" };
		return createDeploymentJar("jpa_core_derivedid_ex2a.jar", pkgNameWithoutSuffix, classes);

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
	 * @assertion_ids: PERSISTENCE:SPEC:1340; PERSISTENCE:SPEC:1341;
	 *
	 * @test_Strategy: Derived Identifier The parent entity uses IdClass Case (a):
	 * The dependent entity uses IdClass
	 */
	@Test
	public void DIDTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			final DID2EmployeeId eId1 = new DID2EmployeeId("Java", "Duke");
			final DID2EmployeeId eId2 = new DID2EmployeeId("C", "foo");
			final DID2Employee employee1 = new DID2Employee(eId1);
			final DID2Employee employee2 = new DID2Employee(eId2);

			final DID2DependentId dId1 = new DID2DependentId("Obama", eId1);
			final DID2DependentId dId2 = new DID2DependentId("Michelle", eId1);
			final DID2DependentId dId3 = new DID2DependentId("John", eId2);

			final DID2Dependent dep1 = new DID2Dependent(dId1, employee1);
			final DID2Dependent dep2 = new DID2Dependent(dId2, employee1);
			final DID2Dependent dep3 = new DID2Dependent(dId3, employee2);

			getEntityManager().persist(employee1);
			getEntityManager().persist(employee2);
			getEntityManager().persist(dep1);
			getEntityManager().persist(dep2);
			getEntityManager().persist(dep3);

			getEntityManager().flush();
			logger.log(Logger.Level.TRACE, "persisted Employees and Dependents");

			// Refresh Dependent
			DID2Dependent newDependent = getEntityManager().find(DID2Dependent.class,
					new DID2DependentId("Obama", new DID2EmployeeId("Java", "Duke")));
			if (newDependent != null) {
				getEntityManager().refresh(newDependent);
			}

			List depList = getEntityManager()
					.createQuery("Select d from DID2Dependent d where d.name='Obama' and d.emp.firstName='Java'")
					.getResultList();
			newDependent = null;
			if (depList.size() > 0) {
				newDependent = (DID2Dependent) depList.get(0);
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
			throw new Exception("DIDTest failed");
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
			getEntityManager().createNativeQuery("DELETE FROM DID2DEPENDENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID2EMPLOYEE").executeUpdate();
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
