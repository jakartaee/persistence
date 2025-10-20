/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.version;

import java.lang.System.Logger;
import java.sql.Timestamp;
import java.util.Date;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client4 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public Client4() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "Int_Field", pkgName + "Int_Property", pkgName + "Integer_Field",
				pkgName + "Integer_Property", pkgName + "Long_Field", pkgName + "Long_Property",
				pkgName + "LongClass_Field", pkgName + "LongClass_Property", pkgName + "Short_Field",
				pkgName + "Short_Property", pkgName + "ShortClass_Field", pkgName + "ShortClass_Property",
				pkgName + "Timestamp_Field", pkgName + "Timestamp_Property" };
		return createDeploymentJar("jpa_core_annotations_version4.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setupTimestampData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupTimestampData");
		try {
			super.setup();
			createDeployment();

			removeTestData();
			createTimestampTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: timestampFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.7
	 *
	 * @test_Strategy:
	 */
	@Test
	public void timestampFieldTest() throws Exception {

		boolean pass = false;
		try {
			Timestamp_Field a = getEntityManager().find(Timestamp_Field.class, "1");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				Timestamp version = a.getVersion();
				a.setName("two");
				// Sleep for 1 second
				try {
					Thread.sleep(1 * 1000L);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Timestamp_Field a1 = getEntityManager().find(Timestamp_Field.class, "1");
				if (a1 != null) {
					if (a1.getVersion().after(version)) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getVersion());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getVersion());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Second find returned null result");
				}
				/*
				 * } else {
				 * logger.log(Logger.Level.ERROR,"Did not get a version of 1 after find"); }
				 */
			} else {
				logger.log(Logger.Level.ERROR, "Find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("timestampFieldTest failed");
		}

	}

	/*
	 * @testName: timestampPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.7
	 *
	 * @test_Strategy:
	 */
	@Test
	public void timestampPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Timestamp_Property a = getEntityManager().find(Timestamp_Property.class, "2");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getBasicTimestamp());
				// if (a.getVersion() == 1) {
				Timestamp version = a.getBasicTimestamp();
				a.setName("two");
				// Sleep for 1 second
				try {
					Thread.sleep(1 * 1000L);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Timestamp_Property a1 = getEntityManager().find(Timestamp_Property.class, "2");
				if (a1 != null) {
					if (a1.getBasicTimestamp().after(version)) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getBasicTimestamp());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getBasicTimestamp());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Second find returned null result");
				}
				/*
				 * } else {
				 * logger.log(Logger.Level.ERROR,"Did not get a version of 1 after find"); }
				 */
			} else {
				logger.log(Logger.Level.ERROR, "Find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("timestampPropertyTest failed");
		}

	}

	public void createTimestampTestData() {
		logger.log(Logger.Level.TRACE, "createTimestampTestData");

		try {
			getEntityTransaction().begin();
			Timestamp currentTime = new Timestamp(new Date().getTime());
			getEntityManager().persist(new Timestamp_Field("1"));
			getEntityManager().persist(new Timestamp_Property("2"));
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTimestampTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

	}

}
