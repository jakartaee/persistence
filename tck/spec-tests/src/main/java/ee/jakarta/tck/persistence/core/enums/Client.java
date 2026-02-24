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
 * $Id: Client.java 63518 2011-09-16 11:36:26Z sdimilla $
 */

package ee.jakarta.tck.persistence.core.enums;

import java.lang.System.Logger;
import java.util.Arrays;
import java.util.Collection;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.AccessType;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.Query;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.PersistenceUnitTransactionType;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute.PersistentAttributeType;
import jakarta.persistence.metamodel.Bindable.BindableType;
import jakarta.persistence.metamodel.PluralAttribute.CollectionType;
import jakarta.persistence.metamodel.Type.PersistenceType;
import jakarta.persistence.spi.LoadState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_core_enums.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: setgetFlushModeEntityManagerTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:173
	 *
	 * @test_Strategy: Set and Get the various flushModes of the EntityManager
	 */
	@Test
	public void setgetFlushModeEntityManagerTest() throws Exception {
		boolean pass = true;
		try {
			EntityTransaction t = getEntityTransaction();
			t.begin();
			EntityManager em = getEntityManager();
			logger.log(Logger.Level.TRACE, "Checking Default mode");
			FlushModeType fmt = em.getFlushMode();
			if (fmt.equals(FlushModeType.AUTO)) {
				logger.log(Logger.Level.TRACE, "Checking COMMIT");
				em.setFlushMode(FlushModeType.COMMIT);
				fmt = em.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE, "Checking AUTO");
					em.setFlushMode(FlushModeType.AUTO);
					fmt = em.getFlushMode();
					if (!fmt.equals(FlushModeType.AUTO)) {
						logger.log(Logger.Level.ERROR,
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected a value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected a default value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			pass = false;

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass)
			throw new Exception("setgetFlushModeEntityManagerTest failed");
	}

	/*
	 * @testName: setgetFlushModeTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:46; PERSISTENCE:JAVADOC:56
	 *
	 * @test_Strategy: Set and Get the various flushModes of a Query
	 */
	@Test
	public void setgetFlushModeTest() throws Exception {
		boolean pass = true;
		try {
			EntityManager em = getEntityManager();
			Query q = em.createQuery("SELECT o FROM Order o WHERE o.id = 1");
			logger.log(Logger.Level.TRACE, "Getting mode from query");
			FlushModeType fmt = q.getFlushMode();
			if (fmt.equals(em.getFlushMode())) {
				logger.log(Logger.Level.TRACE, "Setting mode to return default mode");
				q.setFlushMode(fmt);
				logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.COMMIT");
				q.setFlushMode(FlushModeType.COMMIT);
				fmt = q.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.AUTO");
					q.setFlushMode(FlushModeType.AUTO);
					fmt = q.getFlushMode();
					if (fmt.equals(FlushModeType.AUTO)) {
						logger.log(Logger.Level.TRACE, "Received expected FlushModeType:" + fmt.name());
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected a default value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected EntityManager value of:" + em.getFlushMode() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("setgetFlushModeTest failed");
	}

	/*
	 * @testName: setgetFlushModeTQTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:441; PERSISTENCE:JAVADOC:56
	 *
	 * @test_Strategy: Set and Get the various flushModes of a TypedQuery
	 */
	@Test
	public void setgetFlushModeTQTest() throws Exception {
		boolean pass = true;
		try {
			EntityManager em = getEntityManager();
			TypedQuery<Order> q = em.createQuery("SELECT o FROM Order o WHERE o.id = 1", Order.class);

			FlushModeType fmt = q.getFlushMode();
			if (fmt.equals(em.getFlushMode())) {
				logger.log(Logger.Level.TRACE, "Setting mode to returned default mode");
				q.setFlushMode(fmt);
				logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.COMMIT");
				q.setFlushMode(FlushModeType.COMMIT);
				fmt = q.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.AUTO");
					q.setFlushMode(FlushModeType.AUTO);
					fmt = q.getFlushMode();
					if (!fmt.equals(FlushModeType.AUTO)) {
						logger.log(Logger.Level.ERROR,
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected a default value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected EntityManager value of:" + em.getFlushMode().name() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("setgetFlushModeTQTest failed");
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
			if (getEntityManager().isOpen()) {
				removeTestData();
			}
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
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
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
