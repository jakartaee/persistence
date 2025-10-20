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

public class Client1 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "Int_Field", pkgName + "Int_Property", pkgName + "Integer_Field",
				pkgName + "Integer_Property", pkgName + "Long_Field", pkgName + "Long_Property",
				pkgName + "LongClass_Field", pkgName + "LongClass_Property", pkgName + "Short_Field",
				pkgName + "Short_Property", pkgName + "ShortClass_Field", pkgName + "ShortClass_Property",
				pkgName + "Timestamp_Field", pkgName + "Timestamp_Property" };
		return createDeploymentJar("jpa_core_annotations_version1.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setupIntData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupIntData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createIntTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: intFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
	 * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
	 *
	 * @test_Strategy:
	 */
	@Test
	public void intFieldTest() throws Exception {

		boolean pass = false;
		try {
			Int_Field a = getEntityManager().find(Int_Field.class, "1");
			if (a != null) {
				// if (a.getVersion() == 1) {
				logger.log(Logger.Level.TRACE, "version:" + a.getVersion());
				int version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Int_Field a1 = getEntityManager().find(Int_Field.class, "1");
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
			throw new Exception("intFieldTest failed");
		}

	}

	/*
	 * @testName: intPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
	 * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
	 *
	 * @test_Strategy:
	 */
	@Test
	public void intPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Int_Property a = getEntityManager().find(Int_Property.class, "2");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getValue());
				// if (a.getVersion() == 1) {
				int version = a.getValue();

				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Int_Property a1 = getEntityManager().find(Int_Property.class, "2");
				if (a1 != null) {
					if (a1.getValue() > version) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getValue());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getValue());
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
			throw new Exception("intPropertyTest failed");
		}

	}

	/*
	 * @testName: integerFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
	 *
	 * @test_Strategy:
	 */
	@Test
	public void integerFieldTest() throws Exception {

		boolean pass = false;
		try {
			Integer_Field a = getEntityManager().find(Integer_Field.class, "3");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				Integer version = a.getVersion();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Integer_Field a1 = getEntityManager().find(Integer_Field.class, "3");
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
			throw new Exception("integerFieldTest failed");
		}

	}

	/*
	 * @testName: integerPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
	 *
	 * @test_Strategy:
	 */
	@Test
	public void integerPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Integer_Property a = getEntityManager().find(Integer_Property.class, "4");
			if (a != null) {
				logger.log(Logger.Level.TRACE, "version:" + a.getBasicInteger());
				// if (a.getVersion() == 1) {
				Integer version = a.getBasicInteger();
				a.setName("two");
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Integer_Property a1 = getEntityManager().find(Integer_Property.class, "4");
				if (a1 != null) {
					if (a1.getBasicInteger() > version) {
						logger.log(Logger.Level.TRACE, "version:" + a1.getBasicInteger());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get a greater version after a modification:" + a1.getBasicInteger());
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
			throw new Exception("integerPropertyTest failed");
		}

	}

	public void createIntTestData() {
		logger.log(Logger.Level.TRACE, "createIntTestData");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(new Int_Field("1"));
			getEntityManager().persist(new Int_Property("2"));
			getEntityManager().persist(new Integer_Field("3"));
			getEntityManager().persist(new Integer_Property("4"));

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createIntTestData:", e);
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
