package ee.jakarta.tck.persistence.core.annotations.access.field;

import java.lang.System.Logger;
import java.util.Arrays;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.core.types.common.Grade;
import jakarta.persistence.Query;

public class Client1 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = Client1.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"ee.jakarta.tck.persistence.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_field1.jar", pkgNameWithoutSuffix, (String[]) classes);

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
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: fieldTypeTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:524;
	 * PERSISTENCE:SPEC:534; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:JAVADOC:14; PERSISTENCE:JAVADOC:203; PERSISTENCE:JAVADOC:300;
	 * PERSISTENCE:SPEC:1239; PERSISTENCE:SPEC:1320; PERSISTENCE:SPEC:1327.4;
	 * PERSISTENCE:SPEC:1155; PERSISTENCE:SPEC:1976; PERSISTENCE:SPEC:1977;
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
			if ((null != d1) && (!d1.isProperty())) {
				d1.setBooleanData(true);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.isProperty()) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + d1.isProperty());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: true, actual:" + d1.isProperty());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			e.printStackTrace();
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
				re.printStackTrace();
			}
		}

		if (!pass)
			throw new Exception("fieldTypeTest1 failed");
	}

	/*
	 * @testName: fieldTypeTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:1319
	 * 
	 * @test_Strategy: The persistent field of an entity may be of the following
	 * type: Java primitive types: byte
	 */
	@Test
	public void fieldTypeTest2() throws Exception {

		boolean pass = false;
		byte newByte = (byte) 111;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (d1.getByteData() == (byte) 100)) {
				d1.setByteData(newByte);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getByteData() == newByte) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 100, actual:" + d1.getByteData());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			e.printStackTrace();
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
	 * PERSISTENCE:SPEC:1319
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

			if ((null != d1) && (d1.getCharacterData() == ('a'))) {
				d1.setCharacterData(newChar);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getCharacterData() == newChar) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: a, actual:" + d1.getCharacterData());
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
			throw new Exception("fieldTypeTest3 failed");
	}

	/*
	 * @testName: fieldTypeTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
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
			if ((null != d1) && (d1.getShortData() == (short) 100)) {
				d1.setShortData(newShort);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getShortData() == newShort) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 100, actual:" + d1.getShortData());
				}
				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
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
	 * PERSISTENCE:SPEC:1319
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
			if ((null != d1) && (d1.getIntData() == 300)) {
				d1.setIntData(newInt);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getIntData() == newInt) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 300, actual:" + d1.getIntData());
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
			throw new Exception("fieldTypeTest5 failed");
	}

	/*
	 * @testName: fieldTypeTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
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
			if ((null != d1) && (d1.getLongData() == 600L)) {
				d1.setLongData(newLong);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getLongData() == newLong) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 600, actual:" + d1.getLongData());
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
			throw new Exception("fieldTypeTest6 failed");
	}

	/*
	 * @testName: fieldTypeTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
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
			if ((null != d1) && (d1.getDoubleData() == (50D))) {
				d1.setDoubleData(newDbl);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				if (d1.getDoubleData() == newDbl) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 50, actual:" + d1.getDoubleData());
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
			throw new Exception("fieldTypeTest7 failed");
	}

	/*
	 * @testName: fieldTypeTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:1319
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

					getEntityManager().merge(d1);
					getEntityManager().flush();

					if ((d1.getFloatData() >= newFloat) && (d1.getFloatData() < newfloatRange)) {
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: >= " + newFloat + " and < " + newfloatRange
								+ ", actual:" + d1.getFloatData());
					}
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
			throw new Exception("fieldTypeTest8 failed");
	}

	/*
	 * @testName: fieldTypeTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:1319
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
			logger.log(Logger.Level.TRACE, "find DataTypes entity in fieldTypeTest9");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataTypes is not null, setting enumData");
				d1.setEnumData(Grade.B);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "Update performed, check results");
				if ((null != d1) && (d1.getEnumData().equals(Grade.B))) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: " + Grade.B.toString() + ", actual:" + d1.getEnumData());
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
			throw new Exception("fieldTypeTest9 failed");
	}

	/*
	 * @testName: fieldTypeTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:536; PERSISTENCE:SPEC:540;
	 * PERSISTENCE:SPEC:550; PERSISTENCE:SPEC:1090.0; PERSISTENCE:JAVADOC:216;
	 * PERSISTENCE:JAVADOC:217; PERSISTENCE:SPEC:1319
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
			logger.log(Logger.Level.TRACE, "FIND D2 IN fieldTypeTest10");
			d2 = getEntityManager().find(DataTypes2.class, dateId);
			if (null != d2) {

				logger.log(Logger.Level.TRACE, "fieldTypeTest10:  Check results");
				if (d2.getId().equals(dateId)) {
					logger.log(Logger.Level.TRACE, "Got expected PK of:" + d2.getId() + "received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: " + dateId + ", actual: " + d2.getId());
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
			throw new Exception("fieldTypeTest10 failed");
	}

	/*
	 * @testName: fieldTypeTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1089; PERSISTENCE:SPEC:1319
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
		byte[] a = null;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FIND D1 IN fieldTypeTest11");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting byteData ");
				d1.setByteArrayData(b);
				a = d1.getByteArrayData();
				a[0] = (byte) (a[0] + bv);
				d1.setByteArrayData(b);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "fieldTypeTest11:  Check results");
				if ((null != d2) && (Arrays.equals(d1.getByteArrayData(), a))) {
					logger.log(Logger.Level.TRACE, "fieldTypeTest11: Expected results received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected result in array comparison.");
					for (Byte aByte : a) {
						logger.log(Logger.Level.ERROR, "Array a in propertyTest9 equals: " + aByte);
					}
					for (Byte bByte : b) {
						logger.log(Logger.Level.ERROR, "Array b in propertyTest9 equals: " + bByte);
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
			throw new Exception("fieldTypeTest11 failed");
	}

	/*
	 * @testName: fieldTypeTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:1319
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
			logger.log(Logger.Level.TRACE, "FIND D1 IN fieldTypeTest12");
			d1 = getEntityManager().find(DataTypes.class, 1);

			if (null != d1) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting charData ");
				d1.setCharArrayData(charData);

				getEntityManager().merge(d1);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "fieldTypeTest12:  Check results");
				if ((null != d1) && (Arrays.equals(d1.getCharArrayData(), charData))) {
					logger.log(Logger.Level.TRACE, "fieldTypeTest12: Expected Results Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected " + Arrays.toString(charData) + ", actual: "
							+ Arrays.toString(d1.getCharArrayData()));
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
			throw new Exception("fieldTypeTest12 failed");
	}

	/*
	 * @testName: fieldTypeTest13
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1319
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
			logger.log(Logger.Level.TRACE, "FIND D2 IN fieldTypeTest13");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting TimeData ");
				d2.setTimeData(timeValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "fieldTypeTest13:  Check results");
				if ((null != d2) && (d2.getTimeData().equals(timeValue))) {
					logger.log(Logger.Level.TRACE, "fieldTypeTest13: Expected Time Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected " + timeValue + " , actual: " + d2.getTimeData());
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
			throw new Exception("fieldTypeTest13 failed");
	}

	/*
	 * @testName: fieldTypeTest14
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:527;
	 * PERSISTENCE:SPEC:1319
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
			logger.log(Logger.Level.TRACE, "FIND D2 IN fieldTypeTest14");
			d2 = getEntityManager().find(DataTypes2.class, dateId);

			if (null != d2) {
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting TimestampData ");
				d2.setTsData(tsValue);

				getEntityManager().merge(d2);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "fieldTypeTest14:  Check results");
				if ((null != d2) && (d2.getTsData().equals(tsValue))) {
					logger.log(Logger.Level.TRACE, "fieldTypeTest14: Expected Timestamp Received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected " + tsValue + " , actual: " + d2.getTsData());
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
			throw new Exception("fieldTypeTest14 failed");
	}

	/*
	 * @testName: fieldTypeTest15
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
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

				if (d1.equals(result)) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Received expected result:" + d1.toString());
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + d1.toString() + ", actual:" + result.toString());
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
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
			throw new Exception("fieldTypeTest15 failed");
	}

	/*
	 * @testName: fieldTypeTest16
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
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

				if (d1.equals(result)) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Received expected result:" + d1.toString());
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + d1.toString() + ", actual:" + result.toString());
				}
				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
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
			throw new Exception("fieldTypeTest16 failed");
	}

	/*
	 * @testName: fieldTypeTest17
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:428; PERSISTENCE:SPEC:529;
	 * PERSISTENCE:SPEC:1090.1; PERSISTENCE:SPEC:1090.2; PERSISTENCE:SPEC:1319
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

				if (d1.equals(result)) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Received expected result:" + d1.toString());
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + d1.toString() + ", actual:" + result.toString());
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
			throw new Exception("fieldTypeTest17 failed");
	}

	// Methods used for Tests

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");

		try {
			getEntityTransaction().begin();
			char[] cArray = { 'a' };
			byte[] bArray = { (byte) 100 };
			d1 = new DataTypes(1, false, (byte) 100, 'a', (short) 100, 300, 600L, 50D, 1.0F, cArray, bArray);

			logger.log(Logger.Level.TRACE, "dateId is: " + dateId);
			d2 = new DataTypes2(dateId);

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

}
