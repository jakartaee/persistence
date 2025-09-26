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

package ee.jakarta.tck.persistence.core.types.generator;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client3 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	private DataTypes3 d11;

	private boolean supports_sequence = false;

	public Client3() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2", pkgName + "DataTypes3",
				pkgName + "DataTypes4" };
		return createDeploymentJar("jpa_core_types_generator3.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @class.setup_props: db.supports.sequence;
	 */
	@BeforeEach
	public void setupDataTypes3() throws Exception {
		logger.log(Logger.Level.TRACE, "setupDataTypes3");
		try {

			super.setup();
			createDeployment();
			String s = System.getProperty("db.supports.sequence");
			if (s != null) {
				supports_sequence = Boolean.parseBoolean(s);
				logger.log(Logger.Level.INFO, "db.supports.sequence:" + supports_sequence);
				if (supports_sequence) {
					createSequenceGenerator();
					removeTestData();
					createDataTypes3Data();
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
				throw new Exception("setupDataTypes3 failed");

			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupDataTypes3 failed:", e);
		}
	}

	/*
	 * @testName: sequenceGeneratorOnEntityTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2107; PERSISTENCE:SPEC:2107.1;
	 * 
	 * @test_Strategy: A sequence generator may be specified on the entity class
	 */
	@Test
	public void sequenceGeneratorOnEntityTest() throws Exception {

		boolean pass = true;
		if (supports_sequence) {
			final Integer newInt = 1000;

			try {
				getEntityTransaction().begin();
				clearCache();
				logger.log(Logger.Level.INFO, "Doing a find of id: " + d11.getId());
				DataTypes3 d = getEntityManager().find(DataTypes3.class, d11.getId());

				if (d != null) {
					Integer i = d.getIntegerData();
					if (i.equals(d11.getIntegerData())) {
						logger.log(Logger.Level.TRACE, "find returned correct Integer value:" + i);
						d.setIntegerData(newInt);
					} else {
						logger.log(Logger.Level.ERROR, "find did not return correct Integer value, expected: "
								+ d11.getIntegerData() + ", actual:" + i);
						pass = false;
					}

					getEntityManager().merge(d);
					getEntityManager().flush();
					clearCache();
					logger.log(Logger.Level.INFO, "Doing a find of merged data for id: " + d.getId());
					DataTypes3 d2 = getEntityManager().find(DataTypes3.class, d.getId());
					i = d2.getIntegerData();
					if (i.equals(d2.getIntegerData())) {
						logger.log(Logger.Level.TRACE, "find returned correct merged Integer value:" + i);
					} else {
						logger.log(Logger.Level.ERROR, "find did not return correct Integer value, expected: "
								+ d.getIntegerData() + ", actual:" + i);
						pass = false;
					}

					getEntityTransaction().commit();
				} else {
					logger.log(Logger.Level.ERROR, "find returned null result");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				pass = false;
			}
		} else {
			logger.log(Logger.Level.INFO, "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
		}
		if (!pass)
			throw new Exception("sequenceGeneratorOnEntityTest failed");

	}

	// Methods used for Tests

	public void createDataTypes3Data() {
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "in createDataTypes3Data");

			logger.log(Logger.Level.TRACE, "new DataType3");
			d11 = new DataTypes3(500);
			logger.log(Logger.Level.TRACE, "Persist DataType3");
			getEntityManager().persist(d11);
			logger.log(Logger.Level.TRACE, "DataType3 id:" + d11.getId());

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
	}

}
