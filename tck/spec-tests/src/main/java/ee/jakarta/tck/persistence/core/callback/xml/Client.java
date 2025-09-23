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
 * $Id: Client.java 68465 2012-11-08 14:05:31Z sdimilla $
 */

package ee.jakarta.tck.persistence.core.callback.xml;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.core.callback.common.Constants;
import ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase;
import jakarta.persistence.Query;

public class Client extends EntityCallbackClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 1L;

	private Product product;

	private Order order;

	private LineItem lineItem;

	private Customer customer;

	public Client() {
		super();
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Customer", pkgName + "LineItem", pkgName + "LineItemSuper", pkgName + "Order",
				pkgName + "Product" };
		String[] xmlFiles = { ORM_XML };
		return createDeploymentJar("jpa_core_callback_xml.jar", pkgNameWithoutSuffix, classes, xmlFiles);
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

	/*
	 * @testName: prePersistTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:695; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:JAVADOC:34; PERSISTENCE:SPEC:1464;
	 * PERSISTENCE:SPEC:1465; PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void prePersistTest() throws Exception {
		String reason;
		final String testName = Constants.prePersistTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			getEntityManager().flush();

			if (product.isPrePersistCalled()) {
				reason = "Product: prePersist was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Product: prePersist was not called.";
				throw new Exception(reason);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during prePersistTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: prePersistMultiTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:697;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465;
	 * PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void prePersistMultiTest() throws Exception {
		final String testName = Constants.prePersistMultiTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);

			final List actual = product.getPrePersistCalls();
			compareResultList(Constants.LISTENER_AABBCC, actual);
			getEntityTransaction().commit();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: prePersistCascadeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:695; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:725; PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465;
	 * PERSISTENCE:SPEC:1467; PERSISTENCE:SPEC:1468; PERSISTENCE:SPEC:1469;
	 * PERSISTENCE:SPEC:1470;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void prePersistCascadeTest() throws Exception {
		String reason;
		final String testName = Constants.prePersistCascadeTest;
		try {
			getEntityTransaction().begin();
			order = newOrder(testName);
			product = newProduct(testName);
			lineItem = newLineItem(testName);
			lineItem.setOrder(order);
			lineItem.setProduct(product);
			order.addLineItem(lineItem);
			getEntityManager().persist(product);
			getEntityManager().persist(order);
			getEntityManager().flush();

			if (order.isPrePersistCalled()) {
				reason = "Order: prePersist was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Order: prePersist was not called.";
				throw new Exception(reason);
			}

			if (lineItem.isPrePersistCalled()) {
				reason = "LineItem: prePersist was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "LineItem: prePersist was not called.";
				throw new Exception(reason);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during prePersistCascadeTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: prePersistMultiCascadeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:697;
	 * PERSISTENCE:SPEC:708; PERSISTENCE:JAVADOC:34; PERSISTENCE:SPEC:723;
	 * PERSISTENCE:SPEC:724; PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465;
	 * PERSISTENCE:SPEC:1467; PERSISTENCE:SPEC:1468; PERSISTENCE:SPEC:1469;
	 * PERSISTENCE:SPEC:1470;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void prePersistMultiCascadeTest() throws Exception {
		final String testName = Constants.prePersistMultiCascadeTest;
		try {
			getEntityTransaction().begin();
			order = newOrder(testName);
			product = newProduct(testName);
			lineItem = newLineItem(testName);
			lineItem.setOrder(order);
			lineItem.setProduct(product);
			order.addLineItem(lineItem);
			getEntityManager().persist(product);
			getEntityManager().persist(order);

			List actual = order.getPrePersistCalls();
			compareResultList(Constants.LISTENER_AABBCC, actual);

			actual = lineItem.getPrePersistCalls();
			compareResultList(Constants.LISTENER_BBCC, actual);

			getEntityTransaction().commit();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: preRemoveTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:695; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465; PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void preRemoveTest() throws Exception {
		String reason;
		final String testName = Constants.preRemoveTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			getEntityManager().remove(product);

			if (product.isPreRemoveCalled()) {
				reason = "Product: preRemove was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Product: preRemove was not called.";
				throw new Exception(reason);
			}
			product = null;
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during preRemoveTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: preRemoveMultiTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:709;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465;
	 * PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void preRemoveMultiTest() throws Exception {
		final String testName = Constants.preRemoveMultiTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			getEntityManager().remove(product);

			final List actual = product.getPreRemoveCalls();
			compareResultList(Constants.LISTENER_AABBCC, actual);

			product = null;
			getEntityTransaction().commit();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: preRemoveCascadeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:695; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465; PERSISTENCE:SPEC:1467;
	 * PERSISTENCE:SPEC:1468; PERSISTENCE:SPEC:1469; PERSISTENCE:SPEC:1470;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void preRemoveCascadeTest() throws Exception {
		String reason;
		final String testName = Constants.preRemoveCascadeTest;
		try {
			getEntityTransaction().begin();
			order = newOrder(testName);
			product = newProduct(testName);
			lineItem = newLineItem(testName);
			lineItem.setOrder(order);
			lineItem.setProduct(product);
			order.addLineItem(lineItem);
			getEntityManager().persist(product);
			getEntityManager().persist(order);
			getEntityManager().remove(order);
			final boolean b = order.isPreRemoveCalled();
			order = null;

			if (b) {
				reason = "Order: preRemove was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Order: preRemove was not called.";
				throw new Exception(reason);
			}

			if (lineItem.isPreRemoveCalled()) {
				reason = "LineItem: preRemove was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "LineItem: preRemove was not called.";
				throw new Exception(reason);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during preRemoveCascadeTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: preRemoveMultiCascadeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:726; PERSISTENCE:SPEC:727;
	 * PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465; PERSISTENCE:SPEC:1467;
	 * PERSISTENCE:SPEC:1468; PERSISTENCE:SPEC:1469; PERSISTENCE:SPEC:1470;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void preRemoveMultiCascadeTest() throws Exception {
		final String testName = Constants.preRemoveMultiCascadeTest;
		try {
			getEntityTransaction().begin();
			order = newOrder(testName);
			product = newProduct(testName);
			lineItem = newLineItem(testName);
			lineItem.setOrder(order);
			lineItem.setProduct(product);
			order.addLineItem(lineItem);
			getEntityManager().persist(product);
			getEntityManager().persist(order);
			getEntityManager().remove(order);
			final boolean b = order.isPreRemoveCalled();

			List actual = order.getPreRemoveCalls();
			compareResultList(Constants.LISTENER_AABBCC, actual);

			actual = lineItem.getPreRemoveCalls();
			compareResultList(Constants.LISTENER_BBCC, actual);

			order = null;
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during preRemoveMultiCascadeTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: preUpdateTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:695; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465; PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void preUpdateTest() throws Exception {
		final String testName = Constants.preUpdateTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			product.setPrice(2D);
			getEntityManager().persist(product);
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during preUpdateTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: postLoadTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:695; PERSISTENCE:SPEC:719;
	 * PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465;
	 * PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void postLoadTest() throws Exception {
		String reason;
		final String testName = Constants.postLoadTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			getEntityManager().flush();
			getEntityManager().refresh(product);
			final Query q = getEntityManager().createQuery("select distinct p from Product p");
			final java.util.List results = q.getResultList();
			// for(int i = 0, n = results.size(); i < n; i++) {
			//
			// }
			logger.log(Logger.Level.TRACE, results.toString());

			if (product.isPostLoadCalled()) {
				reason = "Product: postLoad was called after the query result was returned.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Product: postLoad was not called even after the query result was returned.";
				throw new Exception(reason);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during postLoadTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: postLoadMultiTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:719;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465;
	 * PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void postLoadMultiTest() throws Exception {
		final String testName = Constants.postLoadMultiTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			getEntityManager().flush();
			getEntityManager().refresh(product);
			final Query q = getEntityManager().createQuery("select distinct p from Product p");
			final java.util.List results = q.getResultList();
			logger.log(Logger.Level.TRACE, results.toString());

			final List actual = product.getPostLoadCalls();
			compareResultList(Constants.LISTENER_AABBCC, actual);

			getEntityTransaction().commit();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: prePersistRuntimeExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:695; PERSISTENCE:SPEC:704;
	 * PERSISTENCE:SPEC:1464; PERSISTENCE:SPEC:1465; PERSISTENCE:SPEC:1468;
	 * 
	 * @test_Strategy: xml elements are used to define behavior instead of
	 * annotations
	 */
	@Test
	public void prePersistRuntimeExceptionTest() throws Exception {
		final String testName = Constants.prePersistRuntimeExceptionTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			product = (Product) txShouldRollback(product, testName);
			List actual = product.getPrePersistCalls();
			List expected = new ArrayList(Arrays.asList("ListenerA"));
			if (!product.isPrePersistCalled() || !expected.equals(actual)) {
				logger.log(Logger.Level.ERROR, "Expected: " + expected.toString() + ", actual:" + actual.toString());
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during prePersistRuntimeExceptionTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	/*
	 * @testName: prePersistDefaultListenerTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1466
	 * 
	 * @test_Strategy: make use of the default listener defined in the orm.xml
	 */
	@Test
	public void prePersistDefaultListenerTest() throws Exception {
		String reason;
		final String testName = Constants.prePersistDefaultListenerTest;
		try {
			getEntityTransaction().begin();
			customer = newCustomer(testName);
			getEntityManager().persist(customer);
			getEntityManager().flush();

			if (customer.isPrePersistCalled()) {
				reason = "Customer: prePersist was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Customer: prePersist was not called.";
				throw new Exception(reason);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught during prePersistTest", e);
			throw new Exception(e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Exception caught while rolling back TX", re);
			}
		}
	}

	private Product newProduct(final String testName) {
		Product product = new Product();
		product.setTestName(testName);
		product.setId(testName);
		product.setName(testName);
		product.setPartNumber(1L);
		product.setPrice(1D);
		product.setQuantity(1);
		return product;
	}

	private Order newOrder(final String testName) {
		Order order = new Order(testName, 1D);
		order.setTestName(testName);
		return order;
	}

	private LineItem newLineItem(final String testName) {
		LineItem lineItem = new LineItem();
		lineItem.setTestName(testName);
		lineItem.setId(testName);
		lineItem.setQuantity(1);
		return lineItem;
	}

	private Customer newCustomer(final String testName) {
		Customer customer = new Customer(testName, testName);
		return customer;
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
			getEntityManager().createNativeQuery("DELETE FROM LINEITEM_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ORDER_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUSTOMER_TABLE").executeUpdate();
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
