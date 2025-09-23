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

package ee.jakarta.tck.persistence.core.annotations.id;

import java.lang.System.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	public Client() {
	}

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "FieldBigDecimalId", pkgName + "FieldBigIntegerId", pkgName + "FieldIntId",
				pkgName + "FieldIntegerId", pkgName + "FieldSQLDateId", pkgName + "FieldStringId",
				pkgName + "FieldUtilDateId", pkgName + "PropertyBigDecimalId", pkgName + "PropertyBigIntegerId",
				pkgName + "PropertyIntId", pkgName + "PropertyIntegerId", pkgName + "PropertySQLDateId",
				pkgName + "PropertyStringId", pkgName + "PropertyUtilDateId" };
		return createDeploymentJar("jpa_core_annotations_id.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * @testName: FieldIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.2
	 * 
	 * @test_Strategy:
	 */

	@Test
	public void FieldIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Integer id = new Integer(1);

			FieldIntegerId expected = new FieldIntegerId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting IntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldIntegerId actual = getEntityManager().find(FieldIntegerId.class, id);
			if (actual != null) {
				if (actual.getIntegerData().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getIntegerData());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected Integer:" + id + ", actual: " + actual.getIntegerData());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldIntegerIdTest failed");
	}

	/*
	 * @testName: FieldIntIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.1
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void FieldIntIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = 1;

			FieldIntId expected = new FieldIntId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting IntId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldIntId actual = getEntityManager().find(FieldIntId.class, id);
			if (actual != null) {
				if (actual.getIntData() == id) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getIntData());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected int:" + id + ", actual: " + actual.getIntData());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldIntIdTest failed");
	}

	/*
	 * @testName: FieldBigIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.7
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void FieldBigIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigInteger id = new BigInteger("1");

			FieldBigIntegerId expected = new FieldBigIntegerId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting BigIntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldBigIntegerId actual = getEntityManager().find(FieldBigIntegerId.class, id);
			if (actual != null) {
				if (actual.getBigInteger().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getBigInteger());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getBigInteger());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldBigIntegerIdTest failed");
	}

	/*
	 * @testName: FieldBigDecimalIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.6
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void FieldBigDecimalIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigDecimal id = new BigDecimal(new BigInteger("1"));

			FieldBigDecimalId expected = new FieldBigDecimalId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting BigDecimalId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldBigDecimalId actual = getEntityManager().find(FieldBigDecimalId.class, id);
			if (actual != null) {
				if (actual.getBigDecimal().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getBigDecimal());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected value:" + id + ", actual: " + actual.getBigDecimal());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldBigDecimalIdTest failed");
	}

	/*
	 * @testName: FieldStringIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.3
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void FieldStringIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final String id = "1";

			FieldStringId expected = new FieldStringId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldStringId actual = getEntityManager().find(FieldStringId.class, id);
			if (actual != null) {
				if (actual.getName().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getName());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getName());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldStringIdTest failed");
	}

	/*
	 * @testName: FieldSQLDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.5
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void FieldSQLDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.sql.Date id = getSQLDate(2006, 04, 15);

			FieldSQLDateId expected = new FieldSQLDateId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldSQLDateId actual = getEntityManager().find(FieldSQLDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldSQLDateIdTest failed");
	}

	/*
	 * @testName: FieldUtilDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.4
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void FieldUtilDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.util.Date id = getPKDate(2006, 04, 15);

			FieldUtilDateId expected = new FieldUtilDateId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldUtilDateId actual = getEntityManager().find(FieldUtilDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldUtilDateIdTest failed");
	}

	/*
	 * @testName: PropertyIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.2
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void PropertyIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Integer id = new Integer(1);

			PropertyIntegerId expected = new PropertyIntegerId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting IntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyIntegerId actual = getEntityManager().find(PropertyIntegerId.class, id);
			if (actual != null) {
				if (actual.getIntegerData().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getIntegerData());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected Integer:" + id + ", actual: " + actual.getIntegerData());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyIntegerIdTest failed");
	}

	/*
	 * @testName: PropertyIntIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.1
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void PropertyIntIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = 1;

			PropertyIntId expected = new PropertyIntId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting IntId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyIntId actual = getEntityManager().find(PropertyIntId.class, id);
			if (actual != null) {
				if (actual.getIntData() == id) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getIntData());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected int:" + id + ", actual: " + actual.getIntData());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyIntIdTest failed");
	}

	/*
	 * @testName: PropertyBigIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.7
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void PropertyBigIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigInteger id = new BigInteger("1");

			PropertyBigIntegerId expected = new PropertyBigIntegerId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting BigIntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyBigIntegerId actual = getEntityManager().find(PropertyBigIntegerId.class, id);
			if (actual != null) {
				if (actual.getBigInteger().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getBigInteger());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getBigInteger());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyBigIntegerIdTest failed");
	}

	/*
	 * @testName: PropertyBigDecimalIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.6
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void PropertyBigDecimalIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigDecimal id = new BigDecimal(new BigInteger("1"));

			PropertyBigDecimalId expected = new PropertyBigDecimalId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting BigDecimalId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyBigDecimalId actual = getEntityManager().find(PropertyBigDecimalId.class, id);
			if (actual != null) {
				if (actual.getBigDecimal().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getBigDecimal());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected value:" + id + ", actual: " + actual.getBigDecimal());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyBigDecimalIdTest failed");
	}

	/*
	 * @testName: PropertyStringIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.3
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void PropertyStringIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final String id = "1";

			PropertyStringId expected = new PropertyStringId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyStringId actual = getEntityManager().find(PropertyStringId.class, id);
			if (actual != null) {
				if (actual.getName().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getName());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getName());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyStringIdTest failed");
	}

	/*
	 * @testName: PropertySQLDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.5
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void PropertySQLDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.sql.Date id = getSQLDate(2006, 04, 15);

			PropertySQLDateId expected = new PropertySQLDateId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertySQLDateId actual = getEntityManager().find(PropertySQLDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertySQLDateIdTest failed");
	}

	/*
	 * @testName: PropertyUtilDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.4
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void PropertyUtilDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.util.Date id = getPKDate(2006, 04, 15);

			PropertyUtilDateId expected = new PropertyUtilDateId(id, id);

			logger.log(Logger.Level.TRACE, "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyUtilDateId actual = getEntityManager().find(PropertyUtilDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyUtilDateIdTest failed");
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
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
			getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES2").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES3").executeUpdate();
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
