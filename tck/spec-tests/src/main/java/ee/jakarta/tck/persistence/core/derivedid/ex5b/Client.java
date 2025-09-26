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

package ee.jakarta.tck.persistence.core.derivedid.ex5b;

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
		String[] classes = { pkgName + "DID5bMedicalHistory", pkgName + "DID5bPerson", pkgName + "DID5bPersonId" };
		return createDeploymentJar("jpa_core_derivedid_ex5b.jar", pkgNameWithoutSuffix, classes);

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
	 * @assertion_ids: PERSISTENCE:SPEC:1339; PERSISTENCE:SPEC:1340;
	 *                 PERSISTENCE:SPEC:1341;
	 * @test_Strategy: Derived Identifier
	 *                 <p/>
	 *                 Case (b): The dependent entity uses the EmbeddedId and
	 *                 MappedById annotations. The PersonId class either needs to be
	 *                 annotated Embeddable or denoted as an embeddable class in the
	 *                 XML descriptor.
	 */
	@Test
	public void DIDTest() throws Exception {
		boolean pass = false;
		boolean pass1 = false;
		boolean pass2 = true;

		try {

			getEntityTransaction().begin();

			final DID5bPersonId personId = new DID5bPersonId("Java", "DUKE");
			final DID5bPerson person = new DID5bPerson(personId, "123456789");
			final DID5bMedicalHistory mHistory = new DID5bMedicalHistory(personId, person, "drFoo");

			getEntityManager().persist(person);
			getEntityManager().persist(mHistory);

			logger.log(Logger.Level.TRACE, "persisted Patient and MedicalHistory");
			getEntityManager().flush();

			// Refresh MedicalHistory
			DID5bMedicalHistory newMHistory = getEntityManager().find(DID5bMedicalHistory.class,
					new DID5bPersonId("Java", "DUKE"));
			if (newMHistory != null) {
				getEntityManager().refresh(newMHistory);
			}

			final List depList = getEntityManager()
					.createQuery("Select m from DID5bMedicalHistory m where m.patient.firstName='Java'")
					.getResultList();
			newMHistory = null;
			if (depList.size() > 0) {
				newMHistory = (DID5bMedicalHistory) depList.get(0);
				if (newMHistory.getPatient() == person) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received Expected Patient");
				} else {
					logger.log(Logger.Level.ERROR, "Searched Patient not found");
				}
			}
			List depList2 = getEntityManager()
					.createQuery("Select m from DID5bMedicalHistory m where m.id.firstName='Java'").getResultList();
			DID5bMedicalHistory newMHistory2 = null;
			if (depList2.size() > 0) {
				newMHistory2 = (DID5bMedicalHistory) depList.get(0);
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
			getEntityManager().createNativeQuery("DELETE FROM DID5BMEDICALHISTORY").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID5BPERSON").executeUpdate();
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
