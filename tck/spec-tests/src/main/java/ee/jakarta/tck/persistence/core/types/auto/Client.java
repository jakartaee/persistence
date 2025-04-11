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

package ee.jakarta.tck.persistence.core.types.auto;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private DataTypes d0;

	private DataTypes d1;

	private DataTypes d2;

	private DataTypes newD1;

	private DataTypes newD2;

	private DataTypes newD3;

	private DataTypes newD4;

	private DataTypes newD5;

	private DataTypes newD6;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DataTypes" };
		return createDeploymentJar("jpa_core_types_auto.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();
			removeTestData();
			createTestData();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: autoTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1055; PERSISTENCE:SPEC:1057;
	 * PERSISTENCE:SPEC:1050
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys. GenerationType.AUTO,
	 * indicates the persistence provider should pick an appropriate strategy for
	 * the particular database.
	 *
	 * If no Column annotation is specified, the primary key column name is assumed
	 * to be the name of the identifier property or field.
	 *
	 * Using GenerationType.AUTO and defaulting the column name to ID, try to find
	 * the primary key and modify the data.
	 * 
	 */
	@Test
	public void autoTypeTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin autoTypeTest1");
		boolean pass = false;
		final Character newChar = 'b';

		try {
			getEntityTransaction().begin();

			newD1 = getEntityManager().find(DataTypes.class, d0.getId());

			if (null != newD1) {
				if (newD1.getCharacterData().equals(Character.valueOf('a'))) {
					newD1.setCharacterData(newChar);
					newD1 = getEntityManager().merge(newD1);
					getEntityManager().flush();
					getEntityTransaction().commit();

					clearCache();

					getEntityTransaction().begin();
					newD1 = getEntityManager().find(DataTypes.class, newD1.getId());
					if (null != newD1) {
						if (newD1.getCharacterData().equals(newChar)) {
							pass = true;
						}
					} else {
						logger.log(Logger.Level.ERROR, "EntityManager.find (second) returned null result");
					}
				} else {
					logger.log(Logger.Level.ERROR, "EntityManager.find returned the wrong result, expected:"
							+ d0.getId() + ", actual:" + newD1.getId());
				}
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("autoTypeTest1 failed");
	}

	/*
	 * @testName: autoTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1055; PERSISTENCE:SPEC:1057;
	 * PERSISTENCE:SPEC:1050
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys. GenerationType.AUTO,
	 * indicates the persistence provider should pick an appropriate strategy for
	 * the particular database.
	 *
	 * If no Column annotation is specified, the primary key column name is assumed
	 * to be the name of the identifier property or field.
	 *
	 * Using GenerationType.AUTO and defaulting the column name to ID, try to find
	 * the primary key and modify the data.
	 */
	@Test
	public void autoTypeTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin autoTypeTest2");
		boolean pass = false;
		final Short newShort = (short) 101;

		try {
			getEntityTransaction().begin();
			newD2 = getEntityManager().find(DataTypes.class, d1.getId());

			if (null != newD2) {
				if (newD2.getShortData().equals((short) 100)) {
					newD2.setShortData(newShort);
				}

				getEntityManager().merge(newD2);
				getEntityManager().flush();

				if (newD2.getShortData().equals(newShort)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("autoTypeTest2 failed");
	}

	/*
	 * @testName: autoTypeTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1055; PERSISTENCE:SPEC:1057;
	 * PERSISTENCE:SPEC:1050
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys. GenerationType.AUTO,
	 * indicates the persistence provider should pick an appropriate strategy for
	 * the particular database.
	 *
	 * If no Column annotation is specified, the primary key column name is assumed
	 * to be the name of the identifier property or field.
	 *
	 * Using GenerationType.AUTO and defaulting the column name to ID, try to find
	 * the primary key and modify the data.
	 */
	@Test
	public void autoTypeTest3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin autoTypeTest3");
		boolean pass = false;
		final Integer newInt = 500;

		try {
			getEntityTransaction().begin();
			newD3 = getEntityManager().find(DataTypes.class, d2.getId());

			if (null != newD3) {
				if (newD3.getIntegerData().equals(500)) {

					newD3.setIntegerData(newInt);
				}

				getEntityManager().merge(newD3);
				getEntityManager().flush();

				if (newD3.getIntegerData().equals(newInt)) {
					pass = true;
				}
				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("autoTypeTest3 failed");
	}

	/*
	 * @testName: autoTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1055; PERSISTENCE:SPEC:1057;
	 * PERSISTENCE:SPEC:1050
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys. GenerationType.AUTO,
	 * indicates the persistence provider should pick an appropriate strategy for
	 * the particular database.
	 *
	 * If no Column annotation is specified, the primary key column name is assumed
	 * to be the name of the identifier property or field.
	 *
	 * Using GenerationType.AUTO and defaulting the column name to ID, try to find
	 * the primary key and modify the data.
	 */
	@Test
	public void autoTypeTest4() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin autoTypeTest4");
		boolean pass = false;
		final Long newLong = 600L;

		try {
			getEntityTransaction().begin();
			newD4 = getEntityManager().find(DataTypes.class, d0.getId());

			if (null != newD4) {
				if (newD4.getLongData().equals(300L)) {
					newD4.setLongData(newLong);
				}

				getEntityManager().merge(newD4);
				getEntityManager().flush();

				if (newD4.getLongData().equals(newLong)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("autoTypeTest4 failed");
	}

	/*
	 * @testName: autoTypeTest5
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1055; PERSISTENCE:SPEC:1057;
	 * PERSISTENCE:SPEC:1050
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys. GenerationType.AUTO,
	 * indicates the persistence provider should pick an appropriate strategy for
	 * the particular database.
	 *
	 * If no Column annotation is specified, the primary key column name is assumed
	 * to be the name of the identifier property or field.
	 *
	 * Using GenerationType.AUTO and defaulting the column name to ID, try to find
	 * the primary key and modify the data.
	 */
	@Test
	public void autoTypeTest5() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin autoTypeTest5");
		boolean pass = false;
		final Double newDbl = 80D;

		try {
			getEntityTransaction().begin();
			newD5 = getEntityManager().find(DataTypes.class, d1.getId());

			if (null != newD5) {
				if (newD5.getDoubleData().equals(50D)) {
					newD5.setDoubleData(newDbl);
				}

				getEntityManager().merge(newD5);
				getEntityManager().flush();

				if (newD5.getDoubleData().equals(newDbl)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("autoTypeTest5 failed");
	}

	/*
	 * @testName: autoTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:82; PERSISTENCE:JAVADOC:83;
	 * PERSISTENCE:SPEC:1051; PERSISTENCE:SPEC:1055; PERSISTENCE:SPEC:1057;
	 * PERSISTENCE:SPEC:1050
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys. GenerationType.AUTO,
	 * indicates the persistence provider should pick an appropriate strategy for
	 * the particular database.
	 *
	 * If no Column annotation is specified, the primary key column name is assumed
	 * to be the name of the identifier property or field.
	 *
	 * Using GenerationType.AUTO and defaulting the column name to ID, try to find
	 * the primary key and modify the data.
	 */
	@Test
	public void autoTypeTest6() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin autoTypeTest6");
		boolean pass = false;
		final Float newFloat = 3.0F;

		try {
			getEntityTransaction().begin();
			newD6 = getEntityManager().find(DataTypes.class, d2.getId());

			if (null != newD6) {
				if (newD6.getFloatData().equals(1.0F)) {
					newD6.setFloatData(newFloat);
				}

				getEntityManager().merge(newD6);
				getEntityManager().flush();

				if (newD6.getFloatData().equals(newFloat)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("autoTypeTest6 failed");

	}

	// Methods used for Tests

	public void createTestData() {
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "In createTestData");
			d0 = new DataTypes('a', (short) 100, 500, 300L, 50D, 1.0F);
			d1 = new DataTypes('a', (short) 100, 500, 300L, 50D, 1.0F);
			d2 = new DataTypes('a', (short) 100, 500, 300L, 50D, 1.0F);

			logger.log(Logger.Level.TRACE, "Persist DataType");
			getEntityManager().persist(d0);
			getEntityManager().persist(d1);
			getEntityManager().persist(d2);

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
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

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES").executeUpdate();
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
