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

public class Client2 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	private DataTypes2 d10;

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2", pkgName + "DataTypes3",
				pkgName + "DataTypes4" };
		return createDeploymentJar("jpa_core_types_generator2.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @class.setup_props: db.supports.sequence;
	 */
	@BeforeEach
	public void setupDataTypes2() throws Exception {
		logger.log(Logger.Level.TRACE, "setupDataTypes2");
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
					createDataTypes2Data();
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
				throw new Exception("setupDataTypes2 failed");

			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupDataTypes2 failed:", e);
		}
	}

	/*
	 * @testName: generatorTypeSequenceTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:194; PERSISTENCE:JAVADOC:195;
	 * PERSISTENCE:JAVADOC:196; PERSISTENCE:SPEC:2107.2; PERSISTENCE:SPEC:2106;
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.SEQUENCE, indicates the persistence provider must assign
	 * primary keys for the entity using an underlying database sequence generator
	 * to ensure uniqueness.
	 *
	 * Using GenerationType.SEQUENCE, access a persisted entity and modify its'
	 * data.
	 */
	@Test
	public void generatorTypeSequenceTest() throws Exception {

		boolean pass = true;
		if (supports_sequence) {
			final Float newFloat = 3.0F;

			try {
				getEntityTransaction().begin();
				int id = d10.getId();
				logger.log(Logger.Level.TRACE, "Doing a find of id: " + id);
				DataTypes2 d = getEntityManager().find(DataTypes2.class, id);

				if (null != d) {
					Float f = d.getFloatData();
					if (f.equals(d10.getFloatData())) {
						logger.log(Logger.Level.TRACE, "find returned correct float value:" + f);
						d.setFloatData(newFloat);
					} else {
						logger.log(Logger.Level.ERROR,
								"find did not return correct float value, expected: 1.0, actual:" + f);
						pass = false;
					}

					getEntityManager().merge(d);
					getEntityManager().flush();
					f = d.getFloatData();
					if (f.equals(newFloat)) {
						logger.log(Logger.Level.TRACE, "Successfully set float value to:" + newFloat);
					} else {
						logger.log(Logger.Level.ERROR,
								"Could not update float value, expected: " + newFloat + ", actual:" + f);
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
			throw new Exception("generatorTypeSequenceTest failed");

	}

	public void createDataTypes2Data() {
		try {

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "in createDataTypes2Data");

			logger.log(Logger.Level.TRACE, "new DataType2");
			d10 = new DataTypes2('a', (short) 100, 500, 300L, 50D, 1.0F);
			logger.log(Logger.Level.TRACE, "Persist DataType2");
			getEntityManager().persist(d10);
			logger.log(Logger.Level.TRACE, "DataType2 id:" + d10.getId());

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
	}

}
