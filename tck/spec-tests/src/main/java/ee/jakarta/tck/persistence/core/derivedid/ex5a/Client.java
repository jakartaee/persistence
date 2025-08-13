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

package ee.jakarta.tck.persistence.core.derivedid.ex5a;

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
		String[] classes = { pkgName + "DID5MedicalHistory", pkgName + "DID5Person", pkgName + "DID5PersonId" };
		return createDeploymentJar("jpa_core_derivedid_ex5a.jar", pkgNameWithoutSuffix, classes);

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
	 * @assertion_ids: PERSISTENCE:SPEC:1182; PERSISTENCE:SPEC:1183;
	 *                 PERSISTENCE:SPEC:1184; PERSISTENCE:SPEC:1185;
	 * @test_Strategy: Derived Identifier
	 *                 <p/>
	 *                 The parent entity has a simple primary key Case (a): The
	 *                 dependent entity has a single relationship attribute
	 *                 corresponding to the parents primary key. The primary key of
	 *                 MedicalHistory is of type String.
	 */
	@Test
	public void DIDTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			final DID5PersonId personId = new DID5PersonId("Java", "DUKE");
			final DID5Person person = new DID5Person(personId, "123456789");
			final DID5MedicalHistory mHistory = new DID5MedicalHistory(person, "drFoo");

			getEntityManager().persist(person);
			getEntityManager().persist(mHistory);

			logger.log(Logger.Level.TRACE, "persisted Patient and MedicalHistory");
			getEntityManager().flush();

			// Refresh MedicalHistory and Person
			DID5MedicalHistory newMHistory = getEntityManager().find(DID5MedicalHistory.class, personId);
			if (newMHistory != null) {
				getEntityManager().refresh(newMHistory);
			}

			final List depList = getEntityManager()
					.createQuery("Select m from DID5MedicalHistory m where m.patient.firstName='Java'").getResultList();
			newMHistory = null;
			if (depList.size() > 0) {
				newMHistory = (DID5MedicalHistory) depList.get(0);
				if (newMHistory != null) {
					if (newMHistory.getPatient() == person) {
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
			getEntityManager().createNativeQuery("DELETE FROM DID5MEDICALHISTORY").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID5PERSON").executeUpdate();
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
