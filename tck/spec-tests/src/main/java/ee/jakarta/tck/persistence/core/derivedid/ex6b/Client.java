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

package ee.jakarta.tck.persistence.core.derivedid.ex6b;

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
		String[] classes = { pkgName + "DID6bMedicalHistory", pkgName + "DID6bPerson", pkgName + "DID6bPersonId" };
		return createDeploymentJar("jpa_core_derivedid_ex6b.jar", pkgNameWithoutSuffix, classes);

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

	/**
	 * @testName: DIDTest
	 * @assertion_ids: PERSISTENCE:SPEC:1182;PERSISTENCE:SPEC:1183;
	 *                 PERSISTENCE:SPEC:1184; PERSISTENCE:SPEC:1185;
	 * @test_Strategy: Derived Identifier
	 *                 <p/>
	 *                 The parent entity uses EmbeddedId. The dependent's primary
	 *                 key is of the same type as that of the parent.
	 *                 <p/>
	 *                 Case (a): The dependent class uses EmbeddedId
	 */
	@Test
	public void DIDTest() throws Exception {
		boolean pass = false;
		boolean pass1 = false;
		boolean pass2 = false;

		try {

			getEntityTransaction().begin();

			final DID6bPersonId personId = new DID6bPersonId("Java", "DUKE");
			final DID6bPerson person = new DID6bPerson(personId, "123456789");
			final DID6bMedicalHistory mHistory = new DID6bMedicalHistory(personId, person, "drFoo");

			getEntityManager().persist(person);
			getEntityManager().persist(mHistory);

			logger.log(Logger.Level.TRACE, "persisted Patient and MedicalHistory");
			getEntityManager().flush();

			// Refresh MedicalHistory
			DID6bMedicalHistory newMHistory = getEntityManager().find(DID6bMedicalHistory.class, personId);
			if (newMHistory != null) {
				getEntityManager().refresh(newMHistory);
			}

			final List depList = getEntityManager()
					.createQuery("Select m from DID6bMedicalHistory m where m.patient.id.firstName='Java'")
					.getResultList();
			newMHistory = null;
			if (depList.size() > 0) {
				newMHistory = (DID6bMedicalHistory) depList.get(0);
				if (newMHistory.getPatient() == person) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received Expected Patient");
				} else {
					logger.log(Logger.Level.ERROR, "Searched Patient not found");
				}
			}

			List depList2 = getEntityManager()
					.createQuery("Select m from DID6bMedicalHistory m where m.id.firstName='Java'").getResultList();
			DID6bMedicalHistory newMHistory2 = null;
			if (depList2.size() > 0) {
				newMHistory2 = (DID6bMedicalHistory) depList.get(0);
				if (newMHistory2 != null) {
					if (newMHistory2.getPatient() == person) {
						pass = true;
						logger.log(Logger.Level.TRACE, "Received Expected Patient");
					} else {
						logger.log(Logger.Level.ERROR, "Searched Patient not found");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getEntityManager().createQuery returned null entry");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntityManager().createQuery returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			getEntityManager().getTransaction().rollback();
		}

		if (pass1 & pass2) {
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
			getEntityManager().createNativeQuery("DELETE FROM DID6BMEDICALHISTORY").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID6BPERSON").executeUpdate();
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
