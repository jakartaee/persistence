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

public class Client2 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "Int_Field", pkgName + "Int_Property", pkgName + "Integer_Field",
				pkgName + "Integer_Property", pkgName + "Long_Field", pkgName + "Long_Property",
				pkgName + "LongClass_Field", pkgName + "LongClass_Property", pkgName + "Short_Field",
				pkgName + "Short_Property", pkgName + "ShortClass_Field", pkgName + "ShortClass_Property",
				pkgName + "Timestamp_Field", pkgName + "Timestamp_Property" };
		return createDeploymentJar("jpa_core_annotations_version2.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setupShortData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupShortData");
		try {
			super.setup();
			createDeployment();

			removeTestData();
			createShortTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: shortFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.3
	 *
	 * @test_Strategy:
	 */
	@Test
	public void shortFieldTest() throws Exception {

		boolean pass = false;
		try {
			Short_Field a = getEntityManager().find(Short_Field.class, "1");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				short version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Short_Field a1 = getEntityManager().find(Short_Field.class, "1");
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
	 * @testName: shortPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.3
	 *
	 * @test_Strategy:
	 */
	@Test
	public void shortPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Short_Property a = getEntityManager().find(Short_Property.class, "2");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getBasicShort());
				// if (a.getVersion() == 1) {
				short version = a.getBasicShort();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Short_Property a1 = getEntityManager().find(Short_Property.class, "2");
				if (a1 != null) {
					if (a1.getBasicShort() > version) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getBasicShort());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getBasicShort());
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
	 * @testName: shortClassFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.4
	 *
	 * @test_Strategy:
	 */
	@Test
	public void shortClassFieldTest() throws Exception {

		boolean pass = false;
		try {
			ShortClass_Field a = getEntityManager().find(ShortClass_Field.class, "3");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				Short version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				ShortClass_Field a1 = getEntityManager().find(ShortClass_Field.class, "3");
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
	 * @testName: shortClassPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.4
	 *
	 * @test_Strategy:
	 */
	@Test
	public void shortClassPropertyTest() throws Exception {
		boolean pass = false;
		try {
			ShortClass_Property a = getEntityManager().find(ShortClass_Property.class, "4");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getBasicShort());
				// if (a.getVersion() == 1) {
				Short version = a.getBasicShort();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				ShortClass_Property a1 = getEntityManager().find(ShortClass_Property.class, "4");
				if (a1 != null) {
					if (a1.getBasicShort() > version) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getBasicShort());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getBasicShort());
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

	public void createShortTestData() {
		logger.log(Logger.Level.TRACE, "createShortTestData");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(new Short_Field("1"));
			getEntityManager().persist(new Short_Property("2"));
			getEntityManager().persist(new ShortClass_Field("3"));
			getEntityManager().persist(new ShortClass_Property("4"));
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createShortTestData:", e);
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
