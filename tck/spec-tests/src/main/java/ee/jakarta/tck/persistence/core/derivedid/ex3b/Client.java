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

package ee.jakarta.tck.persistence.core.derivedid.ex3b;

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
		String[] classes = { pkgName + "DID3bDependent", pkgName + "DID3bDependentId", pkgName + "DID3bEmployee",
				pkgName + "DID3bEmployeeId" };
		return createDeploymentJar("jpa_core_derivedid_ex3b.jar", pkgNameWithoutSuffix, classes);

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
	 * @assertion_ids: PERSISTENCE:SPEC:1335
	 *
	 * @test_Strategy: Derived Identifier The parent entity uses EmbeddedId Case
	 * (b): The dependent entity uses EmbeddedId
	 */
	@Test
	public void DIDTest() throws Exception {
		boolean pass = false;
		boolean pass1 = false;
		boolean pass2 = false;

		try {

			getEntityTransaction().begin();

			final DID3bEmployeeId eId1 = new DID3bEmployeeId("Java", "Duke");
			final DID3bEmployeeId eId2 = new DID3bEmployeeId("C", "foo");

			final DID3bEmployee employee1 = new DID3bEmployee(eId1);
			final DID3bEmployee employee2 = new DID3bEmployee(eId2);

			final DID3bDependentId depId1 = new DID3bDependentId("Obama", eId1);
			final DID3bDependentId depId2 = new DID3bDependentId("Michelle", eId1);
			final DID3bDependentId depId3 = new DID3bDependentId("John", eId2);
			final DID3bDependent dep1 = new DID3bDependent(depId1, employee1);
			final DID3bDependent dep2 = new DID3bDependent(depId2, employee1);
			final DID3bDependent dep3 = new DID3bDependent(depId3, employee2);

			getEntityManager().persist(employee1);
			getEntityManager().persist(employee2);
			getEntityManager().persist(dep1);
			getEntityManager().persist(dep2);
			getEntityManager().persist(dep3);

			logger.log(Logger.Level.TRACE, "persisted Employees and Dependents");
			getEntityManager().flush();

			// Refresh Dependents
			DID3bDependent newDependent = getEntityManager().find(DID3bDependent.class, depId1);
			if (newDependent != null) {
				getEntityManager().refresh(newDependent);
			}

			DID3bDependent newDependent2 = getEntityManager().find(DID3bDependent.class, depId2);
			if (newDependent2 != null) {
				getEntityManager().refresh(newDependent2);
			}

			List depList = getEntityManager()
					.createQuery(
							"Select d from DID3bDependent d where d.id.name='Obama' and d.emp.empId.firstName='Java'")
					.getResultList();
			newDependent = null;
			if (depList.size() > 0) {
				newDependent = (DID3bDependent) depList.get(0);
				if (newDependent == dep1) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received Expected Dependent");
				} else {
					logger.log(Logger.Level.ERROR, "Searched Dependent not found");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntityManager().createQuery returned null entry");
			}

			List depList2 = getEntityManager()
					.createQuery(
							"Select d from DID3bDependent d where d.id.name='Obama' and d.id.empPK.firstName='Java'")
					.getResultList();
			newDependent2 = null;
			if (depList2.size() > 0) {
				newDependent2 = (DID3bDependent) depList.get(0);
				if (newDependent2 == dep1) {
					pass2 = true;
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

		if (pass1 && pass2) {
			pass = true;
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
			getEntityManager().createNativeQuery("DELETE FROM DID3BDEPENDENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID3BEMPLOYEE").executeUpdate();
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
