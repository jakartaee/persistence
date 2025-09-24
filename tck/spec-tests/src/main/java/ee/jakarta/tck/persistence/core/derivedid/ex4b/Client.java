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

package ee.jakarta.tck.persistence.core.derivedid.ex4b;

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
		String[] classes = { pkgName + "DID4bMedicalHistory", pkgName + "DID4bPerson" };
		return createDeploymentJar("jpa_core_derivedid_ex4b.jar", pkgNameWithoutSuffix, classes);
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
	 * @assertion_ids: PERSISTENCE:SPEC:1339;
	 * @test_Strategy: Derived Identifier
	 *                 <p/>
	 *                 The parent entity has a simple primary key Case (b): The
	 *                 primary key consists of a single attribute corresponding to
	 *                 the simple primary key of the parent entity. The dependent
	 *                 entity has a primary key attribute in addition to the
	 *                 relationship attribute corresponding to the primary key. This
	 *                 attribute is mapped to the primary key by the
	 *                 MappedByIdannotation applied to the relationship.
	 */
	@Test
	public void DIDTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			final DID4bPerson person = new DID4bPerson("123456789", "DUKE");
			final DID4bMedicalHistory mHistory = new DID4bMedicalHistory("123456789", person, "drFoo");

			getEntityManager().persist(person);
			getEntityManager().persist(mHistory);

			logger.log(Logger.Level.TRACE, "persisted Patient and MedicalHistory");
			getEntityManager().flush();

			// Refresh MedicalHistory
			DID4bMedicalHistory newMHistory = getEntityManager().find(DID4bMedicalHistory.class, "123456789");
			if (newMHistory != null) {
				getEntityManager().refresh(newMHistory);
			}

			final List depList = getEntityManager()
					.createQuery("Select m from DID4bMedicalHistory m where m.patient.ssn='123456789'").getResultList();
			newMHistory = null;
			if (depList.size() > 0) {
				newMHistory = (DID4bMedicalHistory) depList.get(0);
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
			getEntityManager().createNativeQuery("DELETE FROM DID4BMEDICALHISTORY").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID4BPERSON").executeUpdate();
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
