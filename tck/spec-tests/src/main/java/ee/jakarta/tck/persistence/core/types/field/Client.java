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

package ee.jakarta.tck.persistence.core.types.field;

import java.lang.System.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.types.common.Grade;
import jakarta.persistence.Query;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private DataTypes d1;

	private DataTypes2 d2;

	private final java.util.Date dateId = getPKDate(2006, 04, 15);

	private final java.sql.Date dateValue = getSQLDate(2006, 04, 15);

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2", pkgName + "UUIDType" };
		return createDeploymentJar("jpa_core_types_field.jar", pkgNameWithoutSuffix, classes);

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
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: fieldTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:524;
	 * PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:JAVADOC:14; PERSISTENCE:JAVADOC:203; PERSISTENCE:SPEC:1976
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: boolean
	 */
	@Test
	public void fieldTypeTest1() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if (!d1.getBooleanData()) {
					d1.setBooleanData(true);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getBooleanData()) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest1 failed");
	}

	/*
	 * @testName: fieldTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:1976
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: byte
	 */
	@Test
	public void fieldTypeTest2() throws Exception {

		boolean pass = false;
		final byte newByte = (byte) 111;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if (d1.getByteData() == (byte) 100) {
					d1.setByteData(newByte);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getByteData() == newByte) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest2 failed");
	}

	/*
	 * @testName: fieldTypeTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: char
	 */
	@Test
	public void fieldTypeTest3() throws Exception {

		boolean pass = false;
		final char newChar = 'b';

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				if (d1.getCharacterData() == ('a')) {
					d1.setCharacterData(newChar);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getCharacterData() == newChar) {
					pass = true;
				}
				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest3 failed");
	}

	/*
	 * @testName: fieldTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: short
	 */
	@Test
	public void fieldTypeTest4() throws Exception {

		boolean pass = false;
		final short newShort = (short) 101;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if (d1.getShortData() == (short) 100) {
					d1.setShortData(newShort);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getShortData() == newShort) {
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
				pass = false;
			}
		}

		if (!pass)
			throw new Exception("fieldTypeTest4 failed");
	}

	/*
	 * @testName: fieldTypeTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: int
	 */
	@Test
	public void fieldTypeTest5() throws Exception {

		boolean pass = false;
		final int newInt = 500;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if (d1.getIntData() == 300) {
					d1.setIntData(newInt);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getIntData() == newInt) {
					pass = true;
				}
				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest5 failed");
	}

	/*
	 * @testName: fieldTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: long
	 */
	@Test
	public void fieldTypeTest6() throws Exception {

		boolean pass = false;
		final long newLong = 600L;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if (d1.getLongData() == 600L) {
					d1.setLongData(newLong);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getLongData() == newLong) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest6 failed");
	}

	/*
	 * @testName: fieldTypeTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: double
	 */
	@Test
	public void fieldTypeTest7() throws Exception {

		boolean pass = false;
		final double newDbl = 80D;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if (d1.getDoubleData() == (50D)) {
					d1.setDoubleData(newDbl);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getDoubleData() == newDbl) {
					pass = true;
				}
				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest7 failed");
	}

	/*
	 * @testName: fieldTypeTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: float
	 */
	@Test
	public void fieldTypeTest8() throws Exception {

		boolean pass = false;
		final float expFloat = 1.0F;
		final float floatRange = 2.0F;
		final float newFloat = 6.0F;
		final float newfloatRange = 7.0F;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "float value is: " + d1.getFloatData());

				if ((d1.getFloatData() >= expFloat) && (d1.getFloatData() < floatRange)) {
					d1.setFloatData(newFloat);
				}

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if ((d1.getFloatData() >= newFloat) && (d1.getFloatData() < newfloatRange)) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest8 failed");
	}

	/*
	 * @testName: fieldTypeTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: enums
	 *
	 * With the Enumerated annotation and EnumType.STRING.
	 */
	@Test
	public void fieldTypeTest9() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FIND D1");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataTypes is not null, setting enumData");
				d1.setEnumData(Grade.B);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "Update performed, check results");
				if (d1.getEnumData().equals(Grade.B)) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results received");
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest9 failed");
	}

	/*
	 * @testName: fieldTypeTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:536; PERSISTENCE:SPEC:540;
	 * PERSISTENCE:SPEC:550; PERSISTENCE:SPEC:1090.0; PERSISTENCE:JAVADOC:216;
	 * PERSISTENCE:JAVADOC:217; PERSISTENCE:SPEC:1976;
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
	public void fieldTypeTest10() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FIND D2");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				if (d2.getId().equals(dateId)) {
					logger.log(Logger.Level.TRACE, "Got expected PK of:" + d2.getId() + "received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected results. " + "Expected " + dateId + ", got: " + d2.getId());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest10 failed");
	}

	/*
	 * @testName: fieldTypeTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: byte[]
	 *
	 */
	@Test
	public void fieldTypeTest11() throws Exception {

		boolean pass = false;
		final byte[] b = { 31, 32, 33, 63, 64, 65 };
		final byte bv = 5;
		byte[] a;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FIND D1");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting byteData ");
				d1.setByteArrayData(b);
				a = d1.getByteArrayData();
				a[0] = (byte) (a[0] + bv);
				d1.setByteArrayData(b);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (Arrays.equals(d1.getByteArrayData(), a)) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected result in array comparison.");
					for (byte aa : a) {
						logger.log(Logger.Level.TRACE, "Array a in propertyTest9 equals: " + aa);
					}
					for (byte bb : b) {
						logger.log(Logger.Level.TRACE, "Array b in propertyTest9 equals: " + bb);
					}
					pass = false;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest11 failed");
	}

	/*
	 * @testName: fieldTypeTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: char[]
	 *
	 */
	@Test
	public void fieldTypeTest12() throws Exception {

		boolean pass = false;
		final char[] charData = new char[] { 'c', 't', 's' };

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FIND D1");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting charData ");
				d1.setCharArrayData(charData);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (Arrays.equals(d1.getCharArrayData(), charData)) {
					logger.log(Logger.Level.TRACE, "Expected Results Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results. " + "Expected "
							+ Arrays.toString(charData) + ", got: " + Arrays.toString(d1.getCharArrayData()));
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest12 failed");
	}

	/*
	 * @testName: fieldTypeTest13
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Time
	 */
	@Test
	public void fieldTypeTest13() throws Exception {

		boolean pass = false;
		final java.sql.Time timeValue = getTimeData(18, 30, 15);

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FIND D2");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting TimeData ");
				d2.setTimeData(timeValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				if (d2.getTimeData().equals(timeValue)) {
					logger.log(Logger.Level.TRACE, "Expected Time Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results. " + " Expected " + timeValue
							+ " , got: " + d2.getTimeData());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest13 failed");
	}

	/*
	 * @testName: fieldTypeTest14
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1976;
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: java.sql.Timestamp
	 */
	@Test
	public void fieldTypeTest14() throws Exception {

		boolean pass = false;
		final java.sql.Timestamp tsValue = getTimestampData(2006, 02, 11);

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FIND D2");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting TimestampData ");
				d2.setTsData(tsValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				if (d2.getTsData().equals(tsValue)) {
					logger.log(Logger.Level.TRACE, "Expected Timestamp Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected results. " + " Expected " + tsValue + " , got: " + d2.getTsData());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest14 failed");
	}

	/*
	 * @testName: fieldTypeTest15
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1630;
	 * 
	 * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
	 * ::= statefield_path_expression | input parameter | enum_literal
	 *
	 * statefield_path_expression
	 */
	@Test
	public void fieldTypeTest15() throws Exception {

		boolean pass = false;
		Object result;
		Query q;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				d1.setEnumData(Grade.A);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				q = getEntityManager().createQuery(
						"SELECT dt FROM DataTypes dt WHERE dt.enumData = ee.jakarta.tck.persistence.core.types.common.Grade.A");

				result = (DataTypes) q.getSingleResult();

				if (d1 == result) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Expected results received");
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results.");
					pass = false;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest15 failed");
	}

	/*
	 * @testName: fieldTypeTest16
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2
	 * 
	 * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
	 * ::= state_field_path_expression | input parameter | enum_literal
	 *
	 * named parameter
	 */
	@Test
	public void fieldTypeTest16() throws Exception {

		boolean pass = false;
		Object result;
		Query q;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				d1.setEnumData(Grade.A);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				q = getEntityManager().createQuery("SELECT dt FROM DataTypes dt WHERE dt.enumData = :grade")
						.setParameter("grade", Grade.A);

				result = (DataTypes) q.getSingleResult();

				if (d1 == result) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Expected results received");
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results.");
					pass = false;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest16 failed");
	}

	/*
	 * @testName: fieldTypeTest17
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2
	 * 
	 * @test_Strategy: enum_expression ::= enum_primary | (subquery) enum_primary
	 * ::= state_field_path_expression | input parameter | enum_literal
	 *
	 * positional parameters
	 */
	@Test
	public void fieldTypeTest17() throws Exception {

		boolean pass = false;
		Object result;
		Query q;
		final Grade failingGrade = ee.jakarta.tck.persistence.core.types.common.Grade.F;
		final Grade incompleteGrade = ee.jakarta.tck.persistence.core.types.common.Grade.INCOMPLETE;

		try {

			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				d1.setEnumData(Grade.C);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				q = getEntityManager()
						.createQuery("SELECT dt FROM DataTypes dt WHERE (dt.enumData <> ?1) OR (dt.enumData <> ?2) ")
						.setParameter(1, failingGrade).setParameter(2, incompleteGrade);

				result = (DataTypes) q.getSingleResult();

				if (d1 == result) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Expected results received");
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results.");
					pass = false;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
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
			throw new Exception("fieldTypeTest17 failed");
	}

	/*
	 * @testName: testCreateUUIDType
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:527;
	 *
	 * @test_Strategy: Test EM create on entity class with UUID.
	 */
	@Test
	public void testCreateUUIDType() throws Exception {
		UUID id = UUID.randomUUID();
		UUIDType uuidType = new UUIDType(id, "Create UUID Type");
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(uuidType);
			getEntityManager().flush();
			getEntityTransaction().commit();
			getEntityManager().clear();
			UUIDType fromDb = getEntityManager().find(UUIDType.class, id);
			System.out.println("UUID: " + fromDb.getId().toString());
			if (fromDb == null) {
				throw new Exception("testCreateUUIDType: no UUID was found in database");
			}
			if (!id.equals(fromDb.getId())) {
				throw new Exception("testCreateUUIDType: UUID returned from database " + fromDb.getId()
						+ " does not match expected value " + id);
			}
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", ex);
			throw new Exception("Caught exception: ", ex);
		} finally {
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		}
	}

	/*
	 * @testName: scalarExpressionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2512; PERSISTENCE:SPEC:1643;
	 * 
	 * @test_Strategy: Test various scalar expressions test
	 *
	 */
	@Test
	public void scalarExpressionsTest() throws Exception {
		boolean pass1, pass2, pass3, pass4, pass5;
		pass1 = pass2 = pass3 = pass4 = pass5 = false;
		List<DataTypes> p;
		try {
			getEntityTransaction().begin();
			int expected = d1.getIntData() + 1;
			logger.log(Logger.Level.TRACE, "Testing arithmetic expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where ((d.intData + 1) = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() == 1) {
				DataTypes d = p.get(0);
				logger.log(Logger.Level.TRACE, "DataType:" + d.toString());
				int actual = d.getIntData() + 1;
				if (actual == expected) {
					logger.log(Logger.Level.TRACE, "Received expected Integer data:" + actual);
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			getEntityTransaction().begin();
			char expected = 'a';
			logger.log(Logger.Level.TRACE, "Testing string expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where (d.characterData = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() == 1) {
				DataTypes d = p.get(0);
				logger.log(Logger.Level.TRACE, "DataType:" + d.toString());
				Character actual = d.getCharacterData();
				if (actual.equals(expected)) {
					logger.log(Logger.Level.TRACE, "Received expected Character data:" + actual);
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		try {
			getEntityTransaction().begin();
			Grade expected = Grade.C;
			logger.log(Logger.Level.TRACE, "Testing enum expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where (d.enumData = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() == 1) {
				DataTypes d = p.get(0);
				logger.log(Logger.Level.TRACE, "DataType:" + d.toString());
				Grade actual = d.getEnumData();
				if (actual.equals(expected)) {
					logger.log(Logger.Level.TRACE, "Received expected Enum data:" + actual);
					pass3 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}

			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			final java.util.Date dateValue = getPKDate(2006, 01, 01);
			java.util.Date expected = dateId;
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Testing datetime expression:");
			List<DataTypes2> pp = getEntityManager().createQuery("Select d From DataTypes2 d where (d.dateData > ?1)")
					.setParameter(1, dateValue).getResultList();

			if (pp.size() == 1) {
				DataTypes2 d = pp.get(0);
				logger.log(Logger.Level.TRACE, "DataType2:" + d.toString());
				java.util.Date actual = d.getDateData();
				if (actual.equals(expected)) {
					logger.log(Logger.Level.TRACE, "Received expected Date data:" + actual);
					pass4 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}

			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected number of results, expected:1, actual:" + pp.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			getEntityTransaction().begin();
			boolean expected = false;
			logger.log(Logger.Level.TRACE, "Testing boolean expression:");
			p = getEntityManager().createQuery("Select d From DataTypes d where (d.booleanData = ?1)")
					.setParameter(1, expected).getResultList();

			if (p.size() > 0) {
				DataTypes d = p.get(0);
				logger.log(Logger.Level.TRACE, "DataType:" + d.toString());
				Boolean actual = d.getBooleanData();
				if (actual.equals(expected)) {
					logger.log(Logger.Level.TRACE, "Received expected Boolean data:" + actual);
					pass5 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected result, Expected: " + expected + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected number of results, expected:1, actual:" + p.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5)
			throw new Exception("scalarExpressionsTest failed");
	}

	// Methods used for Tests

	public void createTestData() {
		try {
			getEntityTransaction().begin();

			char[] cArray = { 'a' };
			byte[] bArray = { (byte) 100 };
			d1 = new DataTypes(1, false, (byte) 100, 'a', (short) 100, 300, 600L, 50D, 1.0F, cArray, bArray);
			d1.setEnumData(Grade.C);

			logger.log(Logger.Level.TRACE, "dateId is: " + dateId);
			d2 = new DataTypes2(dateId);
			d2.setDateData(dateValue);
			getEntityManager().persist(d1);
			getEntityManager().persist(d2);
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
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES2").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM UUIDTYPE").executeUpdate();
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
