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

package ee.jakarta.tck.persistence.core.annotations.access.property;

import java.lang.System.Logger;
import java.util.Arrays;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.core.types.common.Grade;

public class Client1 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = Client1.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"ee.jakarta.tck.persistence.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_property1.jar", pkgNameWithoutSuffix, classes);

	}

	public Client1() {
	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();
			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: propertyTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:514; PERSISTENCE:SPEC:515;
	 * PERSISTENCE:SPEC:516; PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:1327.4; PERSISTENCE:SPEC:1153;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Character
	 */
	@Test
	public void propertyTypeTest1() throws Exception {

		boolean pass = false;
		final Character newChar = 'b';

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getCharacterData().equals((char) 'a'))) {
				d1.setCharacterData(newChar);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getCharacterData().equals(newChar)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest1 failed");
	}

	/*
	 * @testName: propertyTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:534
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Short
	 */
	@Test
	public void propertyTypeTest2() throws Exception {

		boolean pass = false;
		final Short newShort = 101;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getShortData().equals((short) 100))) {
				d1.setShortData(newShort);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getShortData().equals(newShort)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest2 failed");
	}

	/*
	 * @testName: propertyTypeTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Integer
	 */
	@Test
	public void propertyTypeTest3() throws Exception {

		boolean pass = false;
		final Integer newInt = 500;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getIntegerData().equals(500))) {
				d1.setIntegerData(newInt);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getIntegerData().equals(newInt)) {
					pass = true;
				}
				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest3 failed");
	}

	/*
	 * @testName: propertyTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Long
	 */
	@Test
	public void propertyTypeTest4() throws Exception {

		boolean pass = false;
		final Long newLong = 600L;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getLongData().equals(300L))) {
				d1.setLongData(newLong);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getLongData().equals(newLong)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest4 failed");
	}

	/*
	 * @testName: propertyTypeTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Double
	 */
	@Test
	public void propertyTypeTest5() throws Exception {

		boolean pass = false;
		final Double newDbl = 80D;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getDoubleData().equals(50D))) {
				d1.setDoubleData(newDbl);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getDoubleData().equals(newDbl)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest5 failed");

	}

	/*
	 * @testName: propertyTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Float
	 */
	@Test
	public void propertyTypeTest6() throws Exception {

		boolean pass = false;
		final Float newFloat = 3.0F;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getFloatData().equals(1.0F))) {
				d1.setFloatData(newFloat);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getFloatData().equals(newFloat)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest6 failed");

	}

	/*
	 * @testName: propertyTypeTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:JAVADOC:72;
	 * PERSISTENCE:JAVADOC:73
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: enums
	 *
	 * Using the Enumerated annotation, with EnumType.ORDINAL.
	 */
	@Test
	public void propertyTypeTest7() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting enumData ");
				d1.setEnumData(Grade.C);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "Check results");
				if ((null != d1) && (d1.getEnumData().equals(Grade.C))) {
					logger.log(Logger.Level.TRACE, "Expected Grade of:" + d1.getEnumData() + "received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected results.  Expected C, got: " + d1.getEnumData());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest7 failed");
	}

	/*
	 * @testName: propertyTypeTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:536; PERSISTENCE:SPEC:540;
	 * PERSISTENCE:SPEC:550; PERSISTENCE:SPEC:1090.0; PERSISTENCE:SPEC:1079
	 * 
	 * @test_Strategy: The primary key should be one of the following types:
	 * java.util.Date
	 * 
	 * The application must not change the value of the primary key. The behavior is
	 * undefined if this occurs.
	 *
	 * Temporal.TemporalType.DATE
	 */
	@Test
	public void propertyTypeTest8() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			d2 = null;
			d2 = getEntityManager().find(DataTypes2.class, dateId);
			if (null != d2) {

				logger.log(Logger.Level.TRACE, "Check results");
				if (d2.getId().equals(dateId)) {
					logger.log(Logger.Level.TRACE, "Got expected PK of:" + d2.getId() + "received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected results. " + "Expected " + dateId + ", got: " + d2.getId());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest8 failed");
	}

	/*
	 * @testName: propertyTypeTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: Byte[]
	 *
	 */
	@Test
	public void propertyTypeTest9() throws Exception {

		boolean pass = false;
		final Byte[] b = { 31, 32, 33, 63, 64, 65 };
		final Byte bv = 5;
		Byte[] a = null;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting byteData ");
				d1.setByteArrayData(b);
				a = d1.getByteArrayData();
				a[0] = (byte) (a[0] + bv);
				d1.setByteArrayData(b);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "Check results");
				if ((null != d1) && (Arrays.equals(d1.getByteArrayData(), a))) {
					logger.log(Logger.Level.TRACE, "propertyTypeTest9: Expected results received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected result in array comparison.");
					for (Byte aByte : a) {
						logger.log(Logger.Level.TRACE, "Array a in propertyTest9 equals: " + aByte);
					}
					for (Byte bByte : b) {
						logger.log(Logger.Level.TRACE, "Array b in propertyTest9 equals: " + bByte);
					}
					pass = false;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest9 failed");
	}

	/*
	 * @testName: propertyTypeTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: Character[]
	 *
	 */
	@Test
	public void propertyTypeTest10() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Character[] charData = new Character[] { (char) 'C', (char) 'T', (char) 'S' };
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting CharacterData ");
				d1.setCharArrayData(charData);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "propertyTypeTest10:  Check results");
				if ((null != d1) && (Arrays.equals(d1.getCharArrayData(), charData))) {
					logger.log(Logger.Level.TRACE, "propertyTypeTest10: Expected Results Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results. " + "Expected "
							+ Arrays.toString(charData) + ", got: " + Arrays.toString(d1.getCharArrayData()));
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest10 failed");
	}

	/*
	 * @testName: propertyTypeTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Time
	 */
	@Test
	public void propertyTypeTest11() throws Exception {

		boolean pass = false;
		final java.sql.Time timeValue = getTimeData(18, 30, 15);

		try {
			getEntityTransaction().begin();
			clearCache();
			d2 = null;
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting TimeData ");
				d2.setTimeData(timeValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "propertyTypeTest11:  Check results");
				if ((null != d2) && (d2.getTimeData().equals(timeValue))) {
					logger.log(Logger.Level.TRACE, "propertyTypeTest11: Expected Time Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results. " + " Expected " + timeValue
							+ " , got: " + d2.getTimeData());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest11 failed");
	}

	/*
	 * @testName: propertyTypeTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Timestamp
	 */
	@Test
	public void propertyTypeTest12() throws Exception {

		boolean pass = false;
		final java.sql.Timestamp tsValue = getTimestampData(2006, 02, 11);

		try {
			getEntityTransaction().begin();
			clearCache();
			d2 = null;
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting TimestampData ");
				d2.setTsData(tsValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "propertyTypeTest12:  Check results");
				if ((null != d2) && (d2.getTsData().equals(tsValue))) {
					logger.log(Logger.Level.TRACE, "propertyTypeTest12: Expected Timestamp Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected results. " + " Expected " + tsValue + " , got: " + d2.getTsData());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest12 failed");
	}

	/*
	 * @testName: propertyTypeTest13
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: wrappers of the primitive types: Boolean
	 */
	@Test
	public void propertyTypeTest13() throws Exception {

		boolean pass = false;
		final Boolean newBoolean = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);

			if ((null != d1) && (d1.getBooleanData().equals(true))) {
				d1.setBooleanData(newBoolean);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getBooleanData().equals(newBoolean)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("propertyTypeTest13 failed");
	}

	// Methods used for Tests

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");

		try {
			getEntityTransaction().begin();
			Character[] cArray = { 'a' };
			Byte[] bArray = { (byte) 100 };
			d1 = new DataTypes(1, true, 'a', (short) 100, 500, 300L, 50D, 1.0F, cArray, bArray);

			d2 = new DataTypes2(dateId);

			getEntityManager().persist(d1);
			getEntityManager().persist(d2);

			getEntityManager().flush();

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", e);
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
