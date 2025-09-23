/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

import java.lang.System.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.HardwareProduct;
import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.ShelfLife;
import ee.jakarta.tck.persistence.common.schema30.SoftwareProduct;
import ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_;
import ee.jakarta.tck.persistence.common.schema30.UtilProductData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client5 extends UtilProductData {

	private static final Logger logger = (Logger) System.getLogger(Client5.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client5.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaBuilder5.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @testName: primaryKeyJoinColumnTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1120;
	 * PERSISTENCE:SPEC:1121;PERSISTENCE:SPEC:1121.1; PERSISTENCE:SPEC:2109;
	 * 
	 * @test_Strategy: Select p from Product p where p.whouse = "WH5"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void primaryKeyJoinColumnTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			cquery.select(product);
			cquery.where(cbuilder.equal(product.get("wareHouse"), "WH5"));
			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			if (result.size() == 1 && result.get(0).getWareHouse().equals("WH5")) {
				logger.log(Logger.Level.TRACE, "Expected results received:" + result.get(0).getWareHouse());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned: " + result.get(0).getWareHouse() + ", expected: WH5");
				for (Product p : result) {
					logger.log(Logger.Level.ERROR, "**id=" + p.getId() + ", model=" + p.getWareHouse());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("primaryKeyJoinColumnTest  failed");
		}
	}

	/*
	 * @testName: asc
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:741; PERSISTENCE:SPEC:1690;
	 * PERSISTENCE:SPEC:1774;
	 *
	 * @test_Strategy: Select hardwareProduct from HardwareProduct ORDER BY
	 * hardware.Product.modeNumber
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void asc() throws Exception {
		final int expectedModelNumber = 40;
		final int expectedResultSize = 10;
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<HardwareProduct> cquery = cbuilder.createQuery(HardwareProduct.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<HardwareProduct> hardProd = cquery.from(HardwareProduct.class);
			EntityType<HardwareProduct> HardwareProduct_ = hardProd.getModel();
			cquery.select(hardProd);
			cquery.orderBy(cbuilder.asc(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber"))));
			TypedQuery<HardwareProduct> tq = getEntityManager().createQuery(cquery);
			List<HardwareProduct> result = tq.getResultList();

			if (result.size() == expectedResultSize && result.get(0).getModelNumber() == expectedModelNumber) {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned: " + result.get(0).getModelNumber() + ", expected: " + expectedModelNumber);
				for (HardwareProduct o : result) {
					logger.log(Logger.Level.ERROR, "**id=" + o.getId() + ", model=" + o.getModelNumber());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("asc Test  failed");
		}
	}

	/*
	 * @testName: desc
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:762; PERSISTENCE:SPEC:1774;
	 *
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void desc() throws Exception {
		final int expectedModelNumber = 104;
		final int expectedResultSize = 10;
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<HardwareProduct> cquery = cbuilder.createQuery(HardwareProduct.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<HardwareProduct> hardProd = cquery.from(HardwareProduct.class);
			EntityType<HardwareProduct> HardwareProduct_ = hardProd.getModel();
			cquery.select(hardProd);
			cquery.orderBy(cbuilder.desc(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber"))));
			TypedQuery<HardwareProduct> tq = getEntityManager().createQuery(cquery);
			List<HardwareProduct> result = tq.getResultList();

			if (result.size() == expectedResultSize && result.get(7).getModelNumber() == expectedModelNumber) {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned: " + result.get(7).getModelNumber() + "expected: " + expectedModelNumber);
				for (HardwareProduct o : result) {
					logger.log(Logger.Level.ERROR, "**id=" + o.getId() + ", model=" + o.getModelNumber());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("desc Test  failed");
		}
	}

	/*
	 * @testName: sumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:853; PERSISTENCE:SPEC:1740;
	 * PERSISTENCE:SPEC:1746; PERSISTENCE:SPEC:1746.1;
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * Sum(p.price) FROM Product p
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumExpTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cbuilder.createQuery(Number.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("price", Double.class));
			cquery.select(cbuilder.sum(e));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			double d1 = 33387.14D;
			double d2 = 33387.15D;

			Number d3 = tq.getSingleResult();
			if (d3 instanceof Double) {
				logger.log(Logger.Level.TRACE, "Received expected type of Double");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected type Double, actual:" + d3);
			}
			double d4 = d3.doubleValue();
			if (((d4 >= d1) && (d4 < d2))) {
				logger.log(Logger.Level.TRACE, "sum returned expected results: " + d1);
				pass2 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("sumExpTest failed");
		}
	}

	/*
	 * @testName: sumExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:855
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * sum(p.price,100) FROM Product p WHERE p.id = 1
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumExpNumTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("quantity", Integer.class));
			cquery.select(cbuilder.sum(e, 100));
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id")), "1"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer actual = tq.getSingleResult();
			Integer expected = productRef[0].getQuantity() + 100;
			if (actual.equals(expected)) {
				logger.log(Logger.Level.TRACE, "sum returned expected results: " + actual);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + expected + ", actual value:" + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumExpNumTest failed");
		}
	}

	/*
	 * @testName: sumNumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:856
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * sum(100,p.price) FROM Product p WHERE p.id = 1
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumNumExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("quantity", Integer.class));
			cquery.select(cbuilder.sum(100, e));
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id")), "1"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer actual = tq.getSingleResult();
			Integer expected = productRef[0].getQuantity() + 100;
			if (actual.equals(expected)) {
				logger.log(Logger.Level.TRACE, "sum test returned expected results: " + actual);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + expected + ", actual value:" + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumNumExpTest failed");
		}
	}

	/*
	 * @testName: sumExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:854
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * sum(p.quantity,p.quantity) FROM Product p WHERE p.id = 1
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("quantity", Integer.class));
			cquery.select(cbuilder.sum(e, e));
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id")), "1"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer actual = tq.getSingleResult();
			Integer expected = productRef[0].getQuantity() + productRef[0].getQuantity();
			if (actual.equals(expected)) {
				logger.log(Logger.Level.TRACE, "sum test returned expected results: " + actual);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + expected + ", actual value:" + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumExpExpTest failed");
		}
	}

	/*
	 * @testName: exists
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:769; PERSISTENCE:JAVADOC:1190;
	 * PERSISTENCE:SPEC:1767;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery
	 *
	 * SELECT product FROM PRODUCT product WHERE EXISTS (Select hardProd From
	 * PRODUCT hardprod where hardprod.id = '19').
	 *
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void exists() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = "19";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();

			// create Subquery instance, with root Customer
			Subquery<Product> sq = cquery.subquery(Product.class);
			Root<Product> hardProd = sq.from(Product.class);
			if (hardProd.getModel().getName().equals(Product.class.getSimpleName())) {
				logger.log(Logger.Level.TRACE, "Received expected subquery root");
			} else {
				logger.log(Logger.Level.ERROR, "Expected subquery root:" + Product.class.getSimpleName() + ", actual:"
						+ hardProd.getModel().getName());
			}

			// the subquery references the root of the containing query
			sq.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "19"))
					.select(hardProd);

			// an exists condition is applied to the subquery result
			cquery.where(cbuilder.exists(sq));

			cquery.select(product);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("exists test failed");
		}
	}

	/*
	 * @testName: subqueryFromEntityTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1191; PERSISTENCE:SPEC:1765;
	 *
	 * @test_Strategy:
	 *
	 * SELECT product FROM PRODUCT product WHERE EXISTS (Select hardProd From
	 * PRODUCT hardprod where hardprod.id = '19').
	 *
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void subqueryFromEntityTypeTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = "19";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();

			// create Subquery instance, with root Customer
			Subquery<Product> sq = cquery.subquery(Product.class);
			Root<Product> hardProd = sq.from(Product_);
			if (hardProd.getModel().getName().equals(Product_.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected subquery root");
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected subquery root:" + Product_.getName() + ", actual:" + hardProd.getModel().getName());
			}

			// the subquery references the root of the containing query
			sq.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "19"))
					.select(hardProd);

			// an exists condition is applied to the subquery result
			cquery.where(cbuilder.exists(sq));

			cquery.select(product);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryFromEntityTypeTest failed");
		}
	}

	/*
	 * @testName: all
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:735; PERSISTENCE:SPEC:1766;
	 * PERSISTENCE:SPEC:1766.1;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery
	 *
	 * Select hardProd.modelNumber From HardwareProduct hardProd where
	 * hardProd.modelNumber > ALL ( Select subHardProd.modelNumber From
	 * HardwareProduct subHardProd where subHardProd.modelNumber < 10050 )
	 *
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void all() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(10050);
		expected.add(2578);
		expected.add(3000);
		expected.add(10000);
		expected.add(2368);
		Collections.sort(expected);

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<HardwareProduct> hardProd = cquery.from(HardwareProduct.class);

			// Get Metamodel from Root
			EntityType<HardwareProduct> HardwareProduct_ = hardProd.getModel();

			// create Subquery instance
			Subquery<Integer> sq = cquery.subquery(Integer.class);
			Root<HardwareProduct> subHardProd = sq.from(HardwareProduct.class);

			sq.select(subHardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)));
			sq.where(cbuilder.lt(subHardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)),
					1050));

			cquery.select(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)));
			cquery.where(cbuilder.gt(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)),
					cbuilder.all(sq)));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);
			List<Integer> actual = tq.getResultList();
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (Integer i : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + i);
				}
				for (Integer i : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + i);
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("all test failed");
		}
	}

	/*
	 * @testName: sumAsDoubleTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:857
	 *
	 * @test_Strategy:
	 * 
	 * Select sumAsDouble(p.quantity*0.5) From Product p WHERE p.quantity > 50 and
	 * p.quantity < 100
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumAsDoubleTest() throws Exception {
		boolean pass = false;
		double expected = 217.5d;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Double> cquery = cb.createQuery(Double.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.gt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50),
					cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 100));

			cquery.select(cb.sumAsDouble(
					cb.toFloat(cb.prod(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 0.5f))));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double actual = tq.getSingleResult();

			logger.log(Logger.Level.TRACE, "actual" + actual);

			if (expected == actual) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected + ", actual: " + actual);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumAsDoubleTest failed");

		}
	}

	/*
	 * @testName: sumAsLongTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:858
	 *
	 * @test_Strategy:
	 * 
	 * Select sumAsLong(p.quantity) From Product p WHERE p.quantity > 50 and
	 * p.quantity < 100
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumAsLongTest() throws Exception {
		boolean pass = false;
		long expected = 435;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Long> cquery = cb.createQuery(Long.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.gt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50),
					cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 100));
			cquery.select(cb.sumAsLong(prod.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);
			Long actual = tq.getSingleResult();

			logger.log(Logger.Level.TRACE, "actual" + actual);

			if (expected == actual) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected + ", actual: " + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumAsLongTest failed");

		}
	}

	/*
	 * @testName: lessThanExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:797
	 *
	 * @test_Strategy:
	 * 
	 * Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		;
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lessThan(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanExpNumTest failed");

		}
	}

	/*
	 * @testName: lessThanExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:796
	 *
	 * @test_Strategy:
	 * 
	 * Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		;
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(
					cb.lessThan(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), cb.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanExpExpTest failed");

		}
	}

	/*
	 * @testName: lessThanOrEqualToExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:799
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanOrEqualToExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		;
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);

		if (cquery != null) {
			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lessThanOrEqualTo(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanOrEqualToExpNumTest failed");

		}
	}

	/*
	 * @testName: lessThanOrEqualToExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:798
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanOrEqualToExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		;
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);

		if (cquery != null) {
			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lessThanOrEqualTo(prod.get(Product_.getSingularAttribute("quantity", Integer.class)),
					cb.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanOrEqualToExpExpTest failed");

		}
	}

	/*
	 * @testName: between
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:743; PERSISTENCE:JAVADOC:835
	 *
	 * @test_Strategy: SELECT p From Product p where p.shelfLife.soldDate BETWEEN
	 * :date1 AND :date6
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void between() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = "31";
		expected[1] = "32";
		expected[2] = "33";
		expected[3] = "37";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		Date date1 = getSQLDate("2000-02-14");
		Date date6 = getSQLDate("2005-02-18");

		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			EmbeddableType<ShelfLife> ShelfLife_ = mm.embeddable(ShelfLife.class);

			cquery.select(product);

			cquery.where(cbuilder.between(
					product.get(Product_.getSingularAttribute("shelfLife", ShelfLife.class))
							.get(ShelfLife_.getSingularAttribute("soldDate", Date.class)),
					cbuilder.parameter(Date.class, "date1"), cbuilder.parameter(Date.class, "date6")));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("date1", date1);
			tq.setParameter("date6", date6);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("between test failed");
		}
	}

	/*
	 * @testName: ltExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:814
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void ltExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("ltExpNumTest failed");

		}
	}

	/*
	 * @testName: ltExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:813
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void ltExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), cb.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("ltExpExpTest failed");

		}
	}

	/*
	 * @testName: leExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:793
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void leExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cbuilder.le(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("leExpNumTest failed");

		}
	}

	/*
	 * @testName: leExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:792
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void leExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cbuilder.le(prod.get(Product_.getSingularAttribute("quantity", Integer.class)),
					cbuilder.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("leExpExpTest failed");

		}
	}

	/*
	 * @testName: neg
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:820
	 *
	 * @test_Strategy: SELECT NEG(p.quantity) From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void neg() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.neg(product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = Integer.valueOf(-5);

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("neg test failed");
		}

	}

	/*
	 * @testName: prodExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:838; PERSISTENCE:SPEC:1746;
	 * PERSISTENCE:SPEC:1746.2;
	 *
	 * @test_Strategy: SELECT p.quantity *10F From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void prodExpNumTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cbuilder.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 10F));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);

			Number result = tq.getSingleResult();
			Float expectedResult = 5F * 10F;

			if (result instanceof Float) {
				logger.log(Logger.Level.TRACE, "Received expected type of Float");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected type Double, actual:" + result);
			}
			Float f = result.floatValue();
			if (f == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results:" + f);
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("prodExpNumTest failed");
		}
	}

	/*
	 * @testName: prodNumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:839
	 *
	 * @test_Strategy: SELECT 10 * p.quantity From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void prodNumExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.prod(10, product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = 10 * 5;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results:" + result);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("prodNumExpTest failed");
		}
	}

	/*
	 * @testName: prodExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:837
	 *
	 * @test_Strategy: SELECT p.quantity * p.quantity From Product p where
	 * p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void prodExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = 5 * 5;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results:" + result);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"expected: " + expectedResult.intValue() + ", actual result:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("prodExpExpTest failed");
		}
	}

	/*
	 * @testName: diffExpNumberTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:764
	 *
	 * @test_Strategy: SELECT DIFF(p.quantity, 2) From Product p where p.quantity=5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void diffExpNumberTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.diff(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 2));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expected = Integer.valueOf(3);

			if (result.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results" + result.intValue());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: " + expected.intValue() + ", actual:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("diffExpNumberTest failed");
		}
	}

	/*
	 * @testName: diffNumberExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:765
	 *
	 * @test_Strategy: SELECT DIFF(8, p.quantity) From Product p where p.quantity=5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void diffNumberExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.diff(8, product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expected = Integer.valueOf(3);

			if (result.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results" + result.intValue());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: " + expected.intValue() + ", actual:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("diffNumberExpTest failed");
		}
	}

	/*
	 * @testName: diffExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:763
	 *
	 * @test_Strategy: SELECT DIFF(p.quantity, p.quantity) From Product p where
	 * p.quantity=5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void diffExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.diff(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expected = Integer.valueOf(0);

			if (result.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results" + result.intValue());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: " + expected.intValue() + ", actual:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("diffExpExpTest failed");
		}
	}

	/*
	 * @testName: quotExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:841
	 *
	 * @test_Strategy: SELECT QUOT(p.quantity, 2) From Product p where p.quantity =
	 * 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void quotExpNumTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cb.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(Product.class);

			cquery.select(cb.quot(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 2));
			cquery.where(cb.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			Number actual = tq.getSingleResult();

			Integer expected = Integer.valueOf(2);

			if (actual.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected.intValue() + ", actual:" + actual.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("quotExpNumTest failed");
		}
	}

	/*
	 * @testName: quotNumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:842
	 *
	 * @test_Strategy: SELECT QUOT(10, p.quantity) From Product p where p.quantity =
	 * 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void quotNumExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cb.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(Product.class);

			cquery.select(cb.quot(10, product.get(Product_.getSingularAttribute("quantity", Integer.class))));
			cquery.where(cb.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			Number actual = tq.getSingleResult();

			Integer expected = Integer.valueOf(2);

			if (actual.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected.intValue() + ", actual:" + actual.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("quotNumExpTest failed");
		}
	}

	/*
	 * @testName: quotExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:840
	 *
	 * @test_Strategy: SELECT QUOT(2, p.quantity) From Product p where p.quantity =
	 * 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void quotExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cb.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(Product.class);

			cquery.select(cb.quot(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));
			cquery.where(cb.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			Number actual = tq.getSingleResult();

			Integer expected = Integer.valueOf(1);

			if (actual.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected.intValue() + ", actual:" + actual.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("quotExpExpTest failed");
		}
	}

	/*
	 * @testName: modExpIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:818
	 *
	 * @test_Strategy: Select Object(p) From Product p where MOD(550, 100) =
	 * p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void modExpIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = "5";
		expected[1] = "20";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.where(cbuilder.equal(cbuilder.mod(cbuilder.literal(550), 100),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("modExpIntTest failed");
		}
	}

	/*
	 * @testName: modExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:817
	 *
	 * @test_Strategy: Select Object(p) From Product p where MOD(550, 100) =
	 * p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void modExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = "5";
		expected[1] = "20";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.where(cbuilder.equal(cbuilder.mod(cbuilder.literal(550), cbuilder.literal(100)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("modExpExpTest failed");
		}
	}

	/*
	 * @testName: modIntExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:819
	 *
	 * @test_Strategy: Select Object(p) From Product p where MOD(550, 100) =
	 * p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void modIntExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = "5";
		expected[1] = "20";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.where(cbuilder.equal(cbuilder.mod(550, cbuilder.literal(100)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("modIntExpTest failed");
		}
	}

	/*
	 * @testName: sqrt
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:848
	 *
	 * @test_Strategy: SELECT SQRT(p.quantity) From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sqrt() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.sqrt(product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double result = tq.getSingleResult();
			Double expectedResult = 2.1D;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				// logger.log(Logger.Level.ERROR,"test returned:" + result.doubleValue() +
				// "expected:
				// " + expectedResult.doubleValue());
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sqrt test failed");
		}
	}

	/*
	 * @testName: toLong
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:864
	 *
	 * @test_Strategy: SELECT toLong(p.quantity * 5L) From Product p where
	 * p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toLong() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder
					.toLong(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5L)));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);

			Long result = tq.getSingleResult();
			Long expectedResult = Long.valueOf(25);

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toLong test failed");
		}
	}

	/*
	 * @testName: toInteger
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:863
	 *
	 * @test_Strategy: SELECT toInteger(p.quantity) From Product p where
	 * p.partNumber = 373767373
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toInteger() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.toInteger(product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(
					cbuilder.equal(product.get(Product_.getSingularAttribute("partNumber", Long.class)), 373767373));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = Integer.valueOf(5);

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toInteger test failed");
		}
	}

	/*
	 * @testName: toFloat
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:862
	 *
	 * @test_Strategy: SELECT p.quantity *1/2 From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toFloat() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Float> cquery = cbuilder.createQuery(Float.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.toFloat(
					cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 0.5f)));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Float> tq = getEntityManager().createQuery(cquery);

			Float result = tq.getSingleResult();
			Float expectedResult = 2.5F;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toFloat test failed");
		}

	}

	/*
	 * @testName: toDouble
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:861
	 *
	 * @test_Strategy: SELECT toDouble(SQRT(p.quantity)) From Product p where
	 * p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toDouble() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder
					.toDouble(cbuilder.sqrt(product.get(Product_.getSingularAttribute("quantity", Integer.class)))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double result = tq.getSingleResult();
			Double expectedResult = 2.1D;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toDouble test failed");
		}
	}

	/*
	 * @testName: toBigDecimal
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:859
	 *
	 * @test_Strategy: SELECT ToBigDecimal(p.quantity * BIGDECIMAL) From Product p
	 * where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toBigDecimal() throws Exception {
		final BigDecimal expectedResult = new BigDecimal("50.5");
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<BigDecimal> cquery = cbuilder.createQuery(BigDecimal.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.toBigDecimal(cbuilder.prod(
					product.get(Product_.getSingularAttribute("quantity", Integer.class)), new BigDecimal("10.1"))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<BigDecimal> tq = getEntityManager().createQuery(cquery);

			BigDecimal result = tq.getSingleResult();

			if (result.compareTo(expectedResult) == 0) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned:" + result + "expected: " + expectedResult);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toBigDecimal test failed");
		}
	}

	/*
	 * @testName: toBigInteger
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:860
	 *
	 * @test_Strategy: SELECT toBigInteger(p.quantity * BigInteger("10000000000"))
	 * From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toBigInteger() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<BigInteger> cquery = cbuilder.createQuery(BigInteger.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder
					.toBigInteger(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
							new BigInteger("10000000000"))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<BigInteger> tq = getEntityManager().createQuery(cquery);

			BigInteger result = tq.getSingleResult();
			BigInteger expectedResult = new BigInteger("50000000000");

			if (result.compareTo(expectedResult) == 0) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned:" + result + "expected: " + expectedResult);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toBigInteger test failed");
		}
	}

	/*
	 * @testName: toStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:865
	 *
	 * @test_Strategy: SELECT ToString(p.id) From Product p where p.id ='1'
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toStringTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(cbuilder.toString(cbuilder.literal('a')));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();
			String expectedResult = "a";

			if (result.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned:" + result + "expected: " + expectedResult);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toStringTest test failed");
		}
	}

	/*
	 * @testName: literal
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:806
	 *
	 * @test_Strategy: SELECT p.quantity From Product p where 5 = p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void literal() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(product.get(Product_.getSingularAttribute("quantity", Integer.class)));

			cquery.where(cbuilder.equal(cbuilder.literal(5),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = 5;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("literal test failed");
		}
	}

	/*
	 * @testName: currentDate
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:759
	 *
	 * @test_Strategy: SELECT current_date() From Product p where p.id = "1"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void currentDate() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Date> cquery = cbuilder.createQuery(Date.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			cquery.select(cbuilder.currentDate());
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<Date> tq = getEntityManager().createQuery(cquery);

			Date result = tq.getSingleResult();
			Date d = Date.valueOf(result.toString());
			// Use String.equals because java.sql.Date will compare milliseconds and current_date will contain these
			if (d.toString().equals(result.toString())) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get the expected Date object");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("currentDate test failed");
		}
	}

	/*
	 * @testName: currentTime
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:760
	 *
	 * @test_Strategy: SELECT current_time() From Product p where p.id = "1"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void currentTime() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Time> cquery = cbuilder.createQuery(Time.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			cquery.select(cbuilder.currentTime());
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<Time> tq = getEntityManager().createQuery(cquery);

			Time result = tq.getSingleResult();
			Time ts = new Time(result.getTime());
			if (result.equals(ts)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get the expected Time object");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("currentTimes test failed");
		}
	}

	/*
	 * @testName: currentTimestamp
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:761
	 *
	 * @test_Strategy: SELECT current_timestamp() From Product p where p.id = "1"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void currentTimestamp() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Timestamp> cquery = cbuilder.createQuery(Timestamp.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			cquery.select(cbuilder.currentTimestamp());
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<Timestamp> tq = getEntityManager().createQuery(cquery);

			Timestamp result = tq.getSingleResult();
			Timestamp ts = Timestamp.valueOf(result.toString());
			if (ts.equals(result)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get the expected Timestamp object");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("currentTimestamp test failed");
		}
	}

	/*
	 * @testName: treatPathClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1683; PERSISTENCE:SPEC:1734;
	 * PERSISTENCE:SPEC:1734; PERSISTENCE:SPEC:1734.2;
	 *
	 * @test_Strategy: SELECT p.id From Product p WHERE TREAT(p as
	 * SoftwareProduct).name LIKE "Java%"
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void treatPathClassTest() throws Exception {
		boolean pass = false;

		logger.log(Logger.Level.TRACE, "*****************************");
		logger.log(Logger.Level.TRACE, "SoftwareProducts:");
		logger.log(Logger.Level.TRACE, "--------------------");
		for (SoftwareProduct p : softwareRef) {
			logger.log(Logger.Level.TRACE, "ID:" + p.getId() + ":" + getEntityManager().find(Product.class, p.getId()));
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

			cquery.select(product.get(Product_.getSingularAttribute("id", String.class)));

			cquery.where(
					cbuilder.like(cbuilder.treat(product, SoftwareProduct.class).get(SoftwareProduct_.name), "Java%"));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			List<String> actual = tq.getResultList();
			List<String> expected = new ArrayList<String>();
			expected.add("34");
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("treatPathClassTest failed");
		}
	}

}
