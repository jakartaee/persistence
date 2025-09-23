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

package ee.jakarta.tck.persistence.core.annotations.mapsid;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DID1bDependent", pkgName + "DID1bDependentId", pkgName + "DID1bEmployee" };
		return createDeploymentJar("jpa_core_annotations_mapsid.jar", pkgNameWithoutSuffix, classes);
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
	 * BEGIN Test Cases
	 */

	/*
	 * @testName: persistMX1Test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1090; PERSISTENCE:SPEC:1091;
	 * PERSISTENCE:SPEC:1070; PERSISTENCE:SPEC:1071; PERSISTENCE:SPEC:618;
	 * PERSISTENCE:SPEC:622; PERSISTENCE:JAVADOC:372;
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=PERSIST annotation member.
	 *
	 * Invoke persist on a ManyToOne relationship from X annotated with
	 * cascade=PERSIST and ensure the persist operation is cascaded.
	 *
	 */
	@Test
	public void persistMX1Test1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin persistMX1Test1");
		boolean pass = false;
		EntityManager em = null;
		EntityTransaction et = null;
		em = getEntityManager();
		et = getEntityTransaction();
		et.begin();
		try {

			final DID1bEmployee employee1 = new DID1bEmployee(1L, "Duke");
			final DID1bEmployee employee2 = new DID1bEmployee(2L, "foo");

			final DID1bDependent dep1 = new DID1bDependent(new DID1bDependentId("Obama", 1L), employee1);
			final DID1bDependent dep2 = new DID1bDependent(new DID1bDependentId("Michelle", 1L), employee1);
			final DID1bDependent dep3 = new DID1bDependent(new DID1bDependentId("John", 2L), employee2);

			em.persist(employee1);
			em.persist(employee2);
			em.persist(dep1);
			em.persist(dep2);
			em.persist(dep3);

			logger.log(Logger.Level.TRACE, "persisted Employees and Dependents");
			em.flush();

			// Refresh dependent
			DID1bDependent newDependent = em.find(DID1bDependent.class, new DID1bDependentId("Obama", 1L));
			if (newDependent != null) {
				em.refresh(newDependent);
			}

			final List depList = em
					.createQuery("Select d from DID1bDependent d where d.id.name='Obama' and d.emp.name='Duke'")
					.getResultList();

			newDependent = null;
			if (depList.size() > 0) {
				newDependent = (DID1bDependent) depList.get(0);

			}

			if (newDependent == dep1) {
				pass = true;
				logger.log(Logger.Level.TRACE, "Received Expected Dependent");
			} else {
				logger.log(Logger.Level.TRACE, "searched Dependent not found");
			}

			et.commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			et.rollback();
		}

		if (!pass) {
			throw new Exception("persistMX1Test1 failed");
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
			getEntityManager().createNativeQuery("Delete from DID1BDEPENDENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DID1BEMPLOYEE").executeUpdate();
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
