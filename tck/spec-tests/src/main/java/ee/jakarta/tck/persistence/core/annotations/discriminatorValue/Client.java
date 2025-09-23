/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.discriminatorValue;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "PartProduct", pkgName + "PartProduct2", pkgName + "PricedPartProduct2",
				pkgName + "Product", pkgName + "Product2" };
		return createDeploymentJar("jpa_core_annotations_discrinimatorValue.jar", pkgNameWithoutSuffix, classes);

	}

	public Client() {
		super();
	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	private PricedPartProduct2 newPricedPartProduct(final String testName) {
		PricedPartProduct2 product = new PricedPartProduct2();
		product.setId(testName);
		product.setName(testName);
		product.setPartNumber(1L);
		product.setPrice(1D);
		product.setQuantity(1);
		return product;
	}

	private PartProduct newPartProduct(final String testName) {
		PartProduct product = new PartProduct();
		product.setId(testName);
		product.setName(testName);
		product.setPartNumber(1L);
		product.setQuantity(1);
		return product;
	}

	private Product newProduct(final String testName) {
		Product product = new Product();
		product.setId(testName);
		product.setName(testName);
		product.setQuantity(1);
		return product;
	}

	/*
	 * @testName: integerDiscriminatorValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2006;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void integerDiscriminatorValueTest() throws Exception {
		boolean pass = false;
		final String testName = "integerDiscriminatorValueTest";
		try {
			getEntityTransaction().begin();
			PricedPartProduct2 p1 = newPricedPartProduct(testName);
			getEntityManager().persist(p1);
			getEntityManager().flush();
			clearCache();
			PricedPartProduct2 p2 = getEntityManager().find(PricedPartProduct2.class, testName);
			logger.log(Logger.Level.TRACE, "finding PricedPartProduct2 with id '" + testName + "'");

			if (p1.equals(p2)) {
				logger.log(Logger.Level.TRACE, "Received expected PricedPartProduct2:" + p2);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected result.");
				logger.log(Logger.Level.ERROR, "Expected:" + p1);
				logger.log(Logger.Level.ERROR, "Actual:" + p2);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("integerDiscriminatorValueTest Failed");
		}
	}

	/*
	 * @testName: discriminatorValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2005; PERSISTENCE:SPEC:2513;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void discriminatorValueTest() throws Exception {
		boolean pass1 = false;
		final String testName = "discriminatorValueTest";
		try {
			getEntityTransaction().begin();
			PartProduct p1 = newPartProduct(testName);
			getEntityManager().persist(p1);
			getEntityManager().flush();
			clearCache();
			PartProduct p2 = getEntityManager().find(PartProduct.class, testName);
			logger.log(Logger.Level.TRACE, "finding PartProduct with id '" + testName + "'");

			if (p1.equals(p2)) {
				logger.log(Logger.Level.TRACE, "Received expected PartProduct:" + p2);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected result.");
				logger.log(Logger.Level.ERROR, "Expected:" + p1);
				logger.log(Logger.Level.ERROR, "Actual:" + p2);
			}

			Product p3 = newProduct(testName);
			getEntityManager().persist(p3);
			getEntityManager().flush();
			clearCache();
			Product p4 = getEntityManager().find(Product.class, testName);
			logger.log(Logger.Level.TRACE, "finding Product with id '" + testName + "'");

			if (p3.equals(p4)) {
				logger.log(Logger.Level.TRACE, "Received expected Product:" + p2);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected result.");
				logger.log(Logger.Level.ERROR, "Expected:" + p3);
				logger.log(Logger.Level.ERROR, "Actual:" + p4);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1) {
			throw new Exception("discriminatorValueTest Failed");
		}
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
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE_DISCRIMINATOR").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
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
