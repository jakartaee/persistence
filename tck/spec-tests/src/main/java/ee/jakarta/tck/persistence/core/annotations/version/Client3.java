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

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client3 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public Client3() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "Int_Field", pkgName + "Int_Property", pkgName + "Integer_Field",
				pkgName + "Integer_Property", pkgName + "Long_Field", pkgName + "Long_Property",
				pkgName + "LongClass_Field", pkgName + "LongClass_Property", pkgName + "Short_Field",
				pkgName + "Short_Property", pkgName + "ShortClass_Field", pkgName + "ShortClass_Property",
				pkgName + "Timestamp_Field", pkgName + "Timestamp_Property" };
		return createDeploymentJar("jpa_core_annotations_version3.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setupLongData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupLongData");
		try {
			super.setup();
			createDeployment();

			removeTestData();
			createLongTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: longFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.5
	 *
	 * @test_Strategy:
	 */
	@Test
	public void longFieldTest() throws Exception {

		boolean pass = false;
		try {
			Long_Field a = getEntityManager().find(Long_Field.class, "1");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				long version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Long_Field a1 = getEntityManager().find(Long_Field.class, "1");
				if (a1 != null) {
					if (a1.getVersion() > version) {
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
			throw new Exception("shortFieldTest failed");
		}

	}

	/*
	 * @testName: longPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.5
	 *
	 * @test_Strategy:
	 */
	@Test
	public void longPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Long_Property a = getEntityManager().find(Long_Property.class, "2");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getBasicLong());
				// if (a.getVersion() == 1) {
				long version = a.getBasicLong();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Long_Property a1 = getEntityManager().find(Long_Property.class, "2");
				if (a1 != null) {
					if (a1.getBasicLong() > version) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getBasicLong());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getBasicLong());
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
			throw new Exception("shortPropertyTest failed");
		}

	}

	/*
	 * @testName: longClassFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.6
	 *
	 * @test_Strategy:
	 */
	@Test
	public void longClassFieldTest() throws Exception {

		boolean pass = false;
		try {
			LongClass_Field a = getEntityManager().find(LongClass_Field.class, "3");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				Long version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				LongClass_Field a1 = getEntityManager().find(LongClass_Field.class, "3");
				if (a1 != null) {
					if (a1.getVersion() > version) {
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
			throw new Exception("shortClassFieldTest failed");
		}

	}

	/*
	 * @testName: longClassPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.6
	 *
	 * @test_Strategy:
	 */
	@Test
	public void longClassPropertyTest() throws Exception {
		boolean pass = false;
		try {
			LongClass_Property a = getEntityManager().find(LongClass_Property.class, "4");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getBasicLong());
				// if (a.getVersion() == 1) {
				Long version = a.getBasicLong();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				LongClass_Property a1 = getEntityManager().find(LongClass_Property.class, "4");
				if (a1 != null) {
					if (a1.getBasicLong() > version) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getBasicLong());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getBasicLong());
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
			throw new Exception("shortClassPropertyTest failed");
		}

	}

	public void createLongTestData() {
		logger.log(Logger.Level.TRACE, "createLongTestData");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(new Long_Field("1"));
			getEntityManager().persist(new Long_Property("2"));
			getEntityManager().persist(new LongClass_Field("3"));
			getEntityManager().persist(new LongClass_Property("4"));
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createLongTestData:", e);
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
