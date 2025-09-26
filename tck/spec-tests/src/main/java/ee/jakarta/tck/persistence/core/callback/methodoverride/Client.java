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

package ee.jakarta.tck.persistence.core.callback.methodoverride;

import java.lang.System.Logger;
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

	private Product product;

	private Order order;

	private LineItem lineItem;

	public Client() {
		super();
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "LineItem", pkgName + "Order", pkgName + "Product" };
		String[] xmlFiles = { ORM_XML };
		return createDeploymentJar("jpa_core_callback_methodoverride.jar", pkgNameWithoutSuffix, classes, xmlFiles);
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
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:699
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void prePersistTest() throws Exception {
		String reason;
		final String testName = Constants.prePersistTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);

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
	 * PERSISTENCE:SPEC:722
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void prePersistMultiTest() throws Exception {
		final String testName = Constants.prePersistMultiTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);

			final List expected = Arrays.asList(Constants.LISTENER_A, Constants.LISTENER_B, Constants.LISTENER_C,
					Constants.PRODUCT);
			final List actual = product.getPrePersistCalls();
			compareResultList(expected, actual);
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
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:697
	 * 
	 * @test_Strategy:
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
	 * PERSISTENCE:SPEC:708
	 * 
	 * @test_Strategy:
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

			List expected = Arrays.asList(Constants.LISTENER_A, Constants.LISTENER_B, Constants.LISTENER_C,
					Constants.ORDER);
			List actual = order.getPrePersistCalls();
			compareResultList(expected, actual);

			expected = Arrays.asList(Constants.LISTENER_A, Constants.LISTENER_B, Constants.LISTENER_C,
					Constants.LINE_ITEM);
			actual = lineItem.getPrePersistCalls();
			compareResultList(expected, actual);

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
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:709
	 * 
	 * @test_Strategy:
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
	 * PERSISTENCE:SPEC:722
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void preRemoveMultiTest() throws Exception {
		final String testName = Constants.preRemoveMultiTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			getEntityManager().remove(product);

			final List expected = Arrays.asList(Constants.LISTENER_A, Constants.LISTENER_B, Constants.LISTENER_C,
					Constants.PRODUCT);
			final List actual = product.getPreRemoveCalls();
			compareResultList(expected, actual);

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
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:708
	 * 
	 * @test_Strategy:
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
	 * PERSISTENCE:SPEC:722
	 * 
	 * @test_Strategy:
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

			List expected = Arrays.asList(Constants.LISTENER_A, Constants.LISTENER_B, Constants.LISTENER_C,
					Constants.ORDER);
			List actual = order.getPreRemoveCalls();
			compareResultList(expected, actual);

			expected = Arrays.asList(Constants.LISTENER_A, Constants.LISTENER_B, Constants.LISTENER_C,
					Constants.LINE_ITEM);
			actual = lineItem.getPreRemoveCalls();
			compareResultList(expected, actual);

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
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:716
	 * 
	 * @test_Strategy:
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
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:719
	 * 
	 * @test_Strategy:
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
	 * PERSISTENCE:SPEC:722
	 * 
	 * @test_Strategy:
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

			final List expected = Arrays.asList(Constants.LISTENER_A, Constants.LISTENER_B, Constants.LISTENER_C,
					Constants.PRODUCT);
			final List actual = product.getPostLoadCalls();
			compareResultList(expected, actual);

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
