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

public class Client1 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	private DataTypes d0;

	private DataTypes d1;

	private DataTypes d2;

	private DataTypes d3;

	private DataTypes d4;

	private DataTypes d5;

	private DataTypes d6;

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2", pkgName + "DataTypes3",
				pkgName + "DataTypes4" };
		return createDeploymentJar("jpa_core_types_generator1.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @class.setup_props: db.supports.sequence;
	 */

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
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
					createTestData();
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
				throw new Exception("setupDataTypes2 failed");

			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: generatorTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057;
	 * PERSISTENCE:JAVADOC:206; PERSISTENCE:JAVADOC:209; PERSISTENCE:JAVADOC:210;
	 * PERSISTENCE:JAVADOC:211; PERSISTENCE:JAVADOC:213; PERSISTENCE:JAVADOC:215;
	 * PERSISTENCE:JAVADOC:81; PERSISTENCE:SPEC:1136; PERSISTENCE:JAVADOC:208;
	 * PERSISTENCE:SPEC:2158; PERSISTENCE:SPEC:2189; PERSISTENCE:SPEC:2023;
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.TABLE, indicates the persistence provider must assign primary
	 * keys for the entity using an underlying database strategy table to ensure
	 * uniqueness.
	 *
	 * Using GenerationType.TABLE, access a persisted entity and modify its' data.
	 */
	@Test
	public void generatorTypeTest1() throws Exception {

		boolean pass = false;
		if (supports_sequence) {

			final Character newChar = 'b';

			try {
				getEntityTransaction().begin();
				int id = d0.getId();
				logger.log(Logger.Level.TRACE, "Doing a find of id: " + id);
				d1 = getEntityManager().find(DataTypes.class, id);

				if (null != d1) {
					if (d1.getCharacterData().equals('a')) {
						d1.setCharacterData(newChar);
					}

					getEntityManager().merge(d1);
					getEntityManager().flush();

					if (d1.getCharacterData().equals(newChar)) {
						pass = true;
					}

					getEntityTransaction().commit();
				} else {
					logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
			pass = true;
		}
		if (!pass)
			throw new Exception("generatorTypeTest1 failed");
	}

	/*
	 * @testName: generatorTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.TABLE, indicates the persistence provider must assign primary
	 * keys for the entity using an underlying database strategy table to ensure
	 * uniqueness.
	 *
	 * Using GenerationType.TABLE, access a persisted entity and modify its' data.
	 */
	@Test
	public void generatorTypeTest2() throws Exception {

		boolean pass = false;
		if (supports_sequence) {

			final Short newShort = (short) 101;

			try {
				getEntityTransaction().begin();
				int id = d0.getId();
				logger.log(Logger.Level.TRACE, "Doing a find of id: " + id);
				d2 = getEntityManager().find(DataTypes.class, id);

				if (null != d2) {
					if (d2.getShortData().equals((short) 100)) {
						d2.setShortData(newShort);
					}

					getEntityManager().merge(d2);
					getEntityManager().flush();

					if (d2.getShortData().equals(newShort)) {
						pass = true;
					}

					getEntityTransaction().commit();
				} else {
					logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
			pass = true;
		}
		if (!pass)
			throw new Exception("generatorTypeTest2 failed");
	}

	/*
	 * @testName: generatorTypeTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.TABLE, indicates the persistence provider must assign primary
	 * keys for the entity using an underlying database strategy table to ensure
	 * uniqueness.
	 *
	 * Using GenerationType.TABLE, access a persisted entity and modify its' data.
	 */
	@Test
	public void generatorTypeTest3() throws Exception {

		boolean pass = false;
		if (supports_sequence) {

			final Integer newInt = 500;

			try {
				getEntityTransaction().begin();
				int id = d0.getId();
				logger.log(Logger.Level.TRACE, "Doing a find of id: " + id);
				d3 = getEntityManager().find(DataTypes.class, id);

				if (null != d3) {
					if (d3.getIntegerData().equals(500)) {
						d3.setIntegerData(newInt);
					}

					getEntityManager().merge(d3);
					getEntityManager().flush();

					if (d3.getIntegerData().equals(newInt)) {
						pass = true;
					}
					getEntityTransaction().commit();
				} else {
					logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
			pass = true;
		}
		if (!pass)
			throw new Exception("generatorTypeTest3 failed");
	}

	/*
	 * @testName: generatorTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.TABLE, indicates the persistence provider must assign primary
	 * keys for the entity using an underlying database strategy table to ensure
	 * uniqueness.
	 *
	 * Using GenerationType.TABLE, access a persisted entity and modify its' data.
	 */
	@Test
	public void generatorTypeTest4() throws Exception {

		boolean pass = false;
		if (supports_sequence) {

			final Long newLong = 600L;

			try {
				getEntityTransaction().begin();
				int id = d0.getId();
				logger.log(Logger.Level.TRACE, "Doing a find of id: " + id);
				d4 = getEntityManager().find(DataTypes.class, id);

				if (null != d4) {
					if (d4.getLongData().equals(300L)) {
						d4.setLongData(newLong);
					}

					getEntityManager().merge(d4);
					getEntityManager().flush();

					if (d4.getLongData().equals(newLong)) {
						pass = true;
					}

					getEntityTransaction().commit();
				} else {
					logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

		} else {
			logger.log(Logger.Level.INFO, "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
			pass = true;
		}
		if (!pass)
			throw new Exception("generatorTypeTest4 failed");
	}

	/*
	 * @testName: generatorTypeTest5
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.TABLE, indicates the persistence provider must assign primary
	 * keys for the entity using an underlying database strategy table to ensure
	 * uniqueness.
	 *
	 * Using GenerationType.TABLE, access a persisted entity and modify its' data.
	 */
	@Test
	public void generatorTypeTest5() throws Exception {

		boolean pass = false;
		if (supports_sequence) {

			final Double newDbl = 80D;

			try {
				getEntityTransaction().begin();
				int id = d0.getId();
				logger.log(Logger.Level.TRACE, "Doing a find of id: " + id);
				d5 = getEntityManager().find(DataTypes.class, id);

				if (null != d5) {
					if (d5.getDoubleData().equals(50D)) {
						d5.setDoubleData(newDbl);
					}

					getEntityManager().merge(d5);
					getEntityManager().flush();

					if (d5.getDoubleData().equals(newDbl)) {
						pass = true;
					}

					getEntityTransaction().commit();
				} else {
					logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
			pass = true;
		}
		if (!pass)
			throw new Exception("generatorTypeTest5 failed");
	}

	/*
	 * @testName: generatorTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1052; PERSISTENCE:SPEC:1057
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.TABLE, indicates the persistence provider must assign primary
	 * keys for the entity using an underlying database strategy table to ensure
	 * uniqueness.
	 *
	 * Using GenerationType.TABLE, access a persisted entity and modify its' data.
	 */
	@Test
	public void generatorTypeTest6() throws Exception {

		boolean pass = false;
		if (supports_sequence) {

			final Float newFloat = 3.0F;

			try {
				getEntityTransaction().begin();
				int id = d0.getId();
				logger.log(Logger.Level.TRACE, "Doing a find of id: " + id);
				d6 = getEntityManager().find(DataTypes.class, id);

				if (null != d6) {
					if (d6.getFloatData().equals(1.0F)) {
						d6.setFloatData(newFloat);
					}

					getEntityManager().merge(d6);
					getEntityManager().flush();

					if (d6.getFloatData().equals(newFloat)) {
						pass = true;
					}

					getEntityTransaction().commit();
				} else {
					logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
			pass = true;
		}
		if (!pass)
			throw new Exception("generatorTypeTest6 failed");

	}

	// Methods used for Tests

	public void createTestData() {
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "in createTestData");

			logger.log(Logger.Level.TRACE, "new DataType");
			d0 = new DataTypes('a', (short) 100, 500, 300L, 50D, 1.0F);
			logger.log(Logger.Level.TRACE, "Persist DataType");
			getEntityManager().persist(d0);
			logger.log(Logger.Level.TRACE, "DataType id:" + d0.getId());

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
	}

}
