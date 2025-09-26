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

package ee.jakarta.tck.persistence.core.annotations.embeddable;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	public Client() {
	}

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "B", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_annotations_embeddable.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: EM1XMTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:553; PERSISTENCE:SPEC:557;
	 * PERSISTENCE:SPEC:1239; PERSISTENCE:SPEC:1190; PERSISTENCE:SPEC:1191;
	 * 
	 * @test_Strategy: Use Nested embeddable class in Query
	 *
	 */
	@Test
	public void EM1XMTest() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin EM1XMTest2");
		boolean pass = false;
		EntityManager em = getEntityManager();
		EntityTransaction et = getEntityTransaction();

		try {
			et.begin();
			logger.log(Logger.Level.TRACE, "New instances");

			final ZipCode z1 = new ZipCode("01801", "1234");

			Address addr1 = new Address("1 Network Drive", "Burlington", "MA");

			addr1.setZipCode(z1);

			B b1 = new B("1", "b1", 1);
			b1.setAddress(addr1);

			em.persist(b1);
			em.flush();

			B newB = findB("1");
			em.refresh(newB);

			final String newStreet = (String) em.createQuery("Select b.address.street from B b ").getSingleResult();
			final String newState = (String) em.createQuery("Select b.address.state from B b ").getSingleResult();
			final String newCity = (String) em.createQuery("Select b.address.city from B b ").getSingleResult();
			final String newPlusFour = (String) em.createQuery("Select b.address.zipCode.plusFour from B b ")
					.getSingleResult();
			final String newZip = (String) em.createQuery("Select b.address.zipCode.zip from B b ").getSingleResult();

			boolean pass1 = false;
			boolean pass2 = false;
			boolean pass3 = false;
			boolean pass4 = false;
			boolean pass5 = false;

			// Verify Embedded contents
			if (addr1.getStreet().equals(newStreet)) {
				pass1 = true;
				logger.log(Logger.Level.TRACE, "Received Street match");
			}

			if (addr1.getState().equals(newState)) {
				pass2 = true;
				logger.log(Logger.Level.TRACE, "Received State match");
			}

			if (addr1.getCity().equals(newCity)) {
				pass3 = true;
				logger.log(Logger.Level.TRACE, "Received City match");
			}

			if (addr1.getZipCode().getPlusFour().equals(newPlusFour)) {
				pass4 = true;
				logger.log(Logger.Level.TRACE, "Received zipCode PlusFour match");
			}

			if (addr1.getZipCode().getZip().equals(newZip)) {
				pass5 = true;
				logger.log(Logger.Level.TRACE, "Received zipCode zip match");
			}

			if (pass1 && pass2 && pass3 && pass4 && pass5) {
				pass = true;
				logger.log(Logger.Level.TRACE, "Received Address match");

			} else {
				logger.log(Logger.Level.TRACE, "Received incorrect data");

			}

			et.commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (et.isActive()) {
					et.rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}

		}
		if (!pass) {
			throw new Exception("EM1XMTest failed");
		}
	}

	private B findB(String id) {
		// logger.log(Logger.Level.TRACE,"Entered findB method");
		return getEntityManager().find(B.class, id);
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
			getEntityManager().createNativeQuery("Delete from B_EMBEDDABLE").executeUpdate();
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
