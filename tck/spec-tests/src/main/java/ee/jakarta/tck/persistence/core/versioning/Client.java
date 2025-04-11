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

/*
  * $Id$
 */

package ee.jakarta.tck.persistence.core.versioning;

import java.lang.System.Logger;
import java.math.BigInteger;

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
		String[] classes = { pkgName + "Member" };
		return createDeploymentJar("jpa_core_versioning.jar", pkgNameWithoutSuffix, (String[]) classes);

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
	 * @testName: versionTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1068; PERSISTENCE:SPEC:690;
	 * PERSISTENCE:SPEC:666; PERSISTENCE:JAVADOC:13; PERSISTENCE:JAVADOC:16;
	 * PERSISTENCE:JAVADOC:17; PERSISTENCE:JAVADOC:18; PERSISTENCE:SPEC:1400;
	 * 
	 * @test_Strategy: The version annotation specifies the version field or
	 * property of an entity class that serves as an optimistic lock value. The
	 * version is used to ensure integrity when performing the merge operation and
	 * for optimistic concurrency control.
	 *
	 * positive test with sequential tx
	 * 
	 */
	@Test
	public void versionTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin versionTest1");
		boolean pass1 = true;
		boolean pass2 = true;
		boolean pass3 = true;
		final BigInteger donation = new BigInteger("5000000");

		try {
			getEntityTransaction().begin();
			Member m = new Member(1, "Jie Leng", true);
			getEntityManager().persist(m);
			getEntityManager().flush();
			getEntityTransaction().commit();

			// prior to writing to database, Member may not have any version value.
			// After writing to database, version must have a value.

			Member newMember = getEntityManager().find(Member.class, 1);
			if (newMember.getVersion() == null) {
				logger.log(Logger.Level.ERROR, "version after persistence is null.");
				pass1 = false;
			} else {
				logger.log(Logger.Level.TRACE, "Correct non-null version after create: " + newMember.getVersion());
			}

			// update member
			getEntityTransaction().begin();
			Member newMember2 = getEntityManager().find(Member.class, 1);
			int oldVersion = newMember2.getVersion();
			newMember2.setDonation(donation);
			getEntityManager().merge(newMember2);
			getEntityManager().flush();
			getEntityTransaction().commit();

			Member newMember3 = getEntityManager().find(Member.class, 1);
			if (newMember3.getVersion() <= oldVersion) {
				logger.log(Logger.Level.ERROR,
						"Wrong version after update: " + newMember3.getVersion() + ", old version: " + oldVersion);
				pass2 = false;
			} else {
				logger.log(Logger.Level.TRACE,
						"Correct version after update: " + newMember3.getVersion() + ", old version: " + oldVersion);
			}

			oldVersion = newMember3.getVersion();
			// select member
			getEntityTransaction().begin();
			getEntityManager().createQuery("SELECT m FROM Member m where m.memberName = :name")
					.setParameter("name", "Jie Leng").getResultList();
			getEntityManager().flush();
			getEntityTransaction().commit();

			Member newMember4 = getEntityManager().find(Member.class, 1);
			if (newMember4.getVersion() != oldVersion) {
				logger.log(Logger.Level.ERROR,
						"Wrong version after query, expected " + oldVersion + ", got " + newMember4.getVersion());
				pass3 = false;
			} else {
				logger.log(Logger.Level.TRACE,
						"Correct version after query, expected " + oldVersion + ", got:" + newMember4.getVersion());
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass1 = false;
			pass2 = false;
			pass3 = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2 || !pass3)
			throw new Exception("versionTest1 failed");
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
			getEntityManager().createNativeQuery("DELETE FROM MEMBER").executeUpdate();
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
