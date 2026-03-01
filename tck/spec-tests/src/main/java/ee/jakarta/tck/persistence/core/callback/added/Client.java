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

package ee.jakarta.tck.persistence.core.callback.added;

import java.lang.System.Logger;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.jboss.shrinkwrap.api.spec.JavaArchive;

import ee.jakarta.tck.persistence.core.callback.common.Constants;
import ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase;
import jakarta.persistence.Query;

public class Client extends EntityCallbackClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 1L;

	private Product product;

	public Client() {
		super();
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "LineItem", pkgName + "Order", pkgName + "Product" };
		String[] xmlFiles = { ORM_XML };
		return createDeploymentJar("jpa_core_callback_added.jar", pkgNameWithoutSuffix, classes, xmlFiles);

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

	public static class Listener {
		private int count;

		public void capture(Product product) {
			count++;
		}

		public int getCount() {
			return count;
		}

		public void reset() {
			count = 0;
		}
	}

	@Test
	public void persistTest() throws Exception {
		String reason;
		final String testName = Constants.prePersistTest;
		try {
			var listener = new Listener();

			var prePersistRegistration = getEntityManagerFactory().addListener(
					Product.class,
					jakarta.persistence.PrePersist.class,
					listener::capture
			);

			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);

			if ( listener.getCount() == 1 ) {
				reason = "Added Listener (PrePersist): was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Added Listener (PrePersist): was not called.";
				throw new Exception(reason);
			}

			prePersistRegistration.cancel();

			// persist a second product after cancelling the listener registration

			Product product2 = newProduct(testName + "-second");
			getEntityManager().persist(product2);

			if ( listener.getCount() == 1 ) {
				reason = "Cancelled Listener (PrePersist): was not called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Cancelled Listener (PrePersist): was called.";
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

	@Test
	public void preRemoveTest() throws Exception {
		String reason;
		final String testName = Constants.preRemoveTest;
		try {
			var listener = new Listener();

			var preRemoveRegistration = getEntityManagerFactory().addListener(
					Product.class,
					jakarta.persistence.PreRemove.class,
					listener::capture
			);

			getEntityTransaction().begin();
			product = newProduct(testName);
			Product product2 = newProduct(testName + "-second");
			getEntityManager().persist(product);
			getEntityManager().persist(product2);

			getEntityManager().remove(product);

			if (listener.getCount() == 1) {
				reason = "Added Listener (PreRemove): was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Added Listener (PreRemove): was not called.";
				throw new Exception(reason);
			}
			product = null;

			preRemoveRegistration.cancel();

			getEntityManager().remove(product2);
			if ( listener.getCount() == 1 ) {
				reason = "Cancelled Listener (PreRemove): was not called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Cancelled Listener (PreRemove): was called.";
				throw new Exception(reason);
			}

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

	@Test
	public void preUpdateTest() throws Exception {
		String reason;
		final String testName = Constants.preUpdateTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			getEntityManager().persist(product);
			Product product2 = newProduct(testName+"-second");
			getEntityManager().persist(product2);

			getEntityManager().flush();

			var listener = new Listener();

			var preUpdateRegistration = getEntityManagerFactory().addListener(
					Product.class,
					jakarta.persistence.PreUpdate.class,
					listener::capture
			);

			product.setPrice(2D);
			getEntityManager().flush();

			if (listener.getCount() == 1) {
				reason = "Added Listener (PreUpdate): was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Added Listener (PreUpdate): was not called.";
				throw new Exception(reason);
			}

			preUpdateRegistration.cancel();

			product2.setPrice(2D);
			getEntityManager().flush();

			if ( listener.getCount() == 1 ) {
				reason = "Cancelled Listener (PreUpdate): was not called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Cancelled Listener (PreUpdate): was called.";
				throw new Exception(reason);
			}

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

	@Test
	public void postLoadTest() throws Exception {
		String reason;
		final String testName = Constants.postLoadTest;
		try {
			getEntityTransaction().begin();
			product = newProduct(testName);
			Product product2 = newProduct(testName+"-second");
			getEntityManager().persist(product);
			getEntityManager().persist(product2);
			getEntityManager().flush();
			getEntityManager().clear();

			var listener = new Listener();

			var postLoadRegistration = getEntityManagerFactory().addListener(
					Product.class,
					jakarta.persistence.PostLoad.class,
					listener::capture
			);

			product = getEntityManager().get(Product.class, testName);

			if (listener.getCount() == 1) {
				reason = "Added Listener (PostLoad): was called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Added Listener (PostLoad): was not called.";
				throw new Exception(reason);
			}

			postLoadRegistration.cancel();

			product2 = getEntityManager().get(Product.class, testName+"-second");

			if ( listener.getCount() == 1 ) {
				reason = "Cancelled Listener (PostLoad): was not called.";
				logger.log(Logger.Level.TRACE, reason);
			} else {
				reason = "Cancelled Listener (PostLoad): was called.";
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

	private Product newProduct(final String testName) {
		Product product = new Product();
		product.setId(testName);
		product.setName(testName);
		product.setPartNumber(1L);
		product.setPrice(1D);
		product.setQuantity(1);
		return product;
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
