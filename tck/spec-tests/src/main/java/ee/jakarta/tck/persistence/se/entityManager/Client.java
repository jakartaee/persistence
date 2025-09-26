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

package ee.jakarta.tck.persistence.se.entityManager;

import java.lang.System.Logger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	Properties props = new Properties();

	Map<String, Object> map = new HashMap<String, Object>();

	Order[] orders = new Order[5];

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "Employee", pkgName + "Order" };
		return createDeploymentJar("jpa_se_entityManager.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	/*
	 * setupOrderData() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */
	@BeforeEach
	public void setupOrderData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupOrderData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createOrderData();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanupData() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanupData");
			removeTestData();
			cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: persistAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.persist() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void persistAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			Order order = new Order(6, 666, "desc6");
			getEntityManager().persist(order);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("persistAfterClose failed");
		}
	}

	/*
	 * @testName: mergeAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.merge() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void mergeAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			Order order = new Order(6, 666, "desc6");
			getEntityManager().merge(order);
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("mergeAfterClose failed");
		}
	}

	/*
	 * @testName: removeAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.remove() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void removeAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			Order order = new Order(6, 666, "desc6");
			getEntityManager().remove(order);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("removeAfterClose failed");
		}
	}

	/*
	 * @testName: findAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.find() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void findAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().find(Order.class, 0);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("findAfterClose failed");
		}
	}

	/*
	 * @testName: getReferenceAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.getReference() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void getReferenceAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().getReference(Order.class, 0);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("getReferenceAfterClose failed");
		}
	}

	/*
	 * @testName: flushAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.flush() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void flushAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().flush();

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("flushAfterClose failed");
		}
	}

	/*
	 * @testName: setFlushModeAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.setFlushMode() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void setFlushModeAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().setFlushMode(FlushModeType.AUTO);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("setFlushModeAfterClose failed");
		}
	}

	/*
	 * @testName: getFlushModeAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.getFlushMode() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void getFlushModeAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().getFlushMode();

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("getFlushModeAfterClose failed");
		}
	}

	/*
	 * @testName: lockAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.lock() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void lockAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			Order order = new Order(6, 666, "desc6");
			getEntityManager().lock(order, LockModeType.WRITE);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("lockAfterClose failed");
		}
	}

	/*
	 * @testName: refreshAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.refresh() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void refreshAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			Order order = new Order(6, 666, "desc6");
			getEntityManager().refresh(order);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("refreshAfterClose failed");
		}
	}

	/*
	 * @testName: clearAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.clear() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void clearAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().clear();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("clearAfterClose failed");
		}
	}

	/*
	 * @testName: containsAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.contains() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void containsAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			Order order = new Order(6, 666, "desc6");
			getEntityManager().contains(order);

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("containsAfterClose failed");
		}
	}

	/*
	 * @testName: createQueryAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.createQuery() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void createQueryAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createQuery("SELECT Object (orders) FROM Order orders");

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("createQueryAfterClose failed");
		}
	}

	/*
	 * @testName: createNamedQueryAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.createNamedQuery() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void createNamedQueryAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createNamedQuery("CTS NamedQuery");

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("createNamedQueryAfterClose failed");
		}
	}

	/*
	 * @testName: createNativeQueryAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.createNativeQuery() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void createNativeQueryAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createNativeQuery("SELECT o.ID from ORDER o WHERE (o.TOTALPRICE >100");

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("createNativeQueryAfterClose failed");
		}
	}

	/*
	 * @testName: joinTransactionAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.joinTransaction() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void joinTransactionAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().joinTransaction();

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("joinTransactionAfterClose failed");
		}
	}

	/*
	 * @testName: getDelegateAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.getDelegate() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void getDelegateAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().getDelegate();

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("getDelegateAfterClose failed");
		}
	}

	/*
	 * @testName: closeAfterClose
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: Call EntityManager.close() method after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void closeAfterClose() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().close();

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("closeAfterClose failed");
		}
	}

	/*
	 * @testName: getEntityManagerFactoryIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:482;
	 * 
	 * @test_Strategy: Get EntityManagerFactory from closed EntityManager
	 */
	@Test
	public void getEntityManagerFactoryIllegalStateExceptionTest() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().getEntityManagerFactory();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getEntityManagerFactoryIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: emGetMetamodelIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:485;
	 * 
	 * @test_Strategy: Close the EntityManager, then call em.getMetaModel()
	 */
	@Test
	public void emGetMetamodelIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().getMetamodel();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("emGetMetamodelIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: getCriteriaBuilderIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:481
	 * 
	 * @test_Strategy: access EntityManager.getCriteriaBuilder when manager is
	 * closed and verify exception is thrown
	 *
	 */
	@Test
	public void getCriteriaBuilderIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().getCriteriaBuilder();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException ise) {
			pass = true;
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getCriteriaBuilderIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsAfterClose1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createNamedQuery(String, Class) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose1Test() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createNamedQuery("foo", Employee.class);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose1Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsAfterClose2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createNamedStoredProcedureQuery(String)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose2Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().createNamedStoredProcedureQuery("foo");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose2Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose3Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createNativeQuery(String,Class) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose3Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().createNativeQuery("Select * from Employee", Employee.class);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose3Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose4Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createNativeQuery(String, String) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose4Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().createNativeQuery("Select * from Employee", "resultSetMapping");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose4Test failed");
		}

	}

	// asdf
	/*
	 * @testName: entityManagerMethodsAfterClose5Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(CriteriaDelete) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose5Test() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		CriteriaDelete<Employee> cd = cbuilder.createCriteriaDelete(Employee.class);
		getEntityManager().close();
		try {
			getEntityManager().createQuery(cd);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose5Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose6Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(CriteriaQuery) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose6Test() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cbuilder.createQuery(Employee.class);
		getEntityManager().close();
		try {
			getEntityManager().createQuery(cq);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose6Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose7Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(CriteriaUpdate) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose7Test() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		CriteriaUpdate<Employee> cu = cbuilder.createCriteriaUpdate(Employee.class);
		getEntityManager().close();
		try {
			getEntityManager().createQuery(cu);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose7Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose8Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(String, Class) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose8Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().createQuery("Select * from Employee", Employee.class);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose8Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose9Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose9Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().createStoredProcedureQuery("procedureName");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose9Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose10Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String, Class)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose10Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().createStoredProcedureQuery("procedureName", Employee.class);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose10Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose11Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String, String)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose11Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().createStoredProcedureQuery("procedureName", "resultSetMappings");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose11Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose12Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.detach(Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose12Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().close();
			Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);
			getEntityManager().detach(emp);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose12Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose13Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.find(Class, Object, LockModeType) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose13Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().find(Employee.class, 1, LockModeType.NONE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose13Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose14Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.find(Class, Object, LockModeType, Map)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose14Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().find(Employee.class, 1, LockModeType.NONE, map);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose14Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose15Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.find(Class, Object, Map) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose15Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().find(Employee.class, 1, map);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose15Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose16Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.getCriteriaBuilder() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose16Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().getCriteriaBuilder();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose16Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose17Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.getEntityManagerFactory() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose17Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().getEntityManagerFactory();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose17Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose18Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.getLockMode(Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose18Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);

		try {
			getEntityManager().getLockMode(emp);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose18Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose19Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.getMetamodel() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose19Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().getMetamodel();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose19Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose20Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.isJoinedToTransaction() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose20Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().isJoinedToTransaction();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose20Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose21Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.lock(Object, LockModeType, Map) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose21Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);

		try {
			getEntityManager().lock(emp, LockModeType.NONE, map);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose21Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose22Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.refresh(java.lang.Object, LockModeType)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose22Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);

		try {
			getEntityManager().refresh(emp, LockModeType.NONE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose22Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose23Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.refresh(Object, LockModeType, Map) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose23Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);

		try {
			getEntityManager().refresh(emp, LockModeType.NONE, map);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose23Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose24Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.refresh(Object, Map<String,Object) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose24Test() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);
		try {
			getEntityManager().refresh(emp, map);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose24Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsAfterClose25Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36;
	 * 
	 * @test_Strategy: Call EntityManager.setProperty(String, Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void entityManagerMethodsAfterClose25Test() throws Exception {
		boolean pass = false;
		getEntityManager().close();
		try {
			getEntityManager().setProperty("foo", "bar");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsAfterClose25Test failed");
		}

	}

	/*
	 * @testName: queryMethodsAfterClose1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.executeUpdate() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose1Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.executeUpdate();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose1Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getFirstResult() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose2Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.getFirstResult();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose2Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose3Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getFlushMode() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose3Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.getFlushMode();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose3Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose4Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getHints() after calling EntityManager.close()and
	 * expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose4Test() throws Exception {
		boolean pass = false;

		logger.log(Logger.Level.INFO, "Testing getHints() )");
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.getHints();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose4Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose5Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getLockMode() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose5Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.getLockMode();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose5Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose6Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getMaxResults() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose6Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.getMaxResults();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose6Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose7Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameter(int) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose7Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = ?1");
		query.setParameter(1, 1);
		getEntityManager().close();
		try {
			query.getParameter(1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose7Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose8Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameter(int position, Class) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose8Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = ?1");
		query.setParameter(1, 1);
		getEntityManager().close();
		try {
			query.getParameter(1, Integer.class);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose8Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose9Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameter(String) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose9Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.lastName = :name");
		query.setParameter("name", "Foo");
		getEntityManager().close();
		try {
			query.getParameter("name");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose9Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose10Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameter(String, Class) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose10Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.lastName = :name");
		query.setParameter("name", "Foo");
		getEntityManager().close();
		try {
			query.getParameter("name", String.class);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose10Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose11Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameters() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose11Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = ?1");
		query.setParameter(1, 1);
		getEntityManager().close();
		try {
			query.getParameters();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose11Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose12Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameterValue(int) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose12Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = ?1");
		query.setParameter(1, 1);
		getEntityManager().close();
		try {
			query.getParameterValue(1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose12Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose13Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameterValue(Parameter) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose13Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = ?1");
		query.setParameter(1, 1);
		Parameter p = query.getParameter(1);
		getEntityManager().close();
		try {
			query.getParameterValue(p);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose13Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose14Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getParameterValue(String) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose14Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = :id");
		query.setParameter("id", 1);
		getEntityManager().close();
		try {
			query.getParameterValue("id");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			if (getEntityTransaction().getRollbackOnly() == true) {
				logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
			} else {
				pass = true;
				logger.log(Logger.Level.TRACE, "Transaction was not marked for rollback");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose14Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose15Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getResultList() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose15Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.getResultList();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose15Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose16Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.getSingleResult() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose16Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.getSingleResult();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose16Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose17Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.isBound(Parameter) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose17Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = ?1");
		query.setParameter(1, 1);
		Parameter p = query.getParameter(1);
		getEntityManager().close();
		try {
			query.isBound(p);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose17Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose18Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setFirstResult(int) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose18Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.setFirstResult(1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose18Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose19Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setFlushMode(FlushModeType) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose19Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.setFlushMode(FlushModeType.AUTO);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose19Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose20Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setHint(String, Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose20Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.setHint("foo", "bar");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose20Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose21Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setLockMode(LockModeType) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose21Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.setLockMode(LockModeType.NONE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose21Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose22Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setMaxResults(int) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose22Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e");
		getEntityManager().close();
		try {
			query.setMaxResults(1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose22Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose23Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(int, Calendar, TemporalType) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose23Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.hireDate = ?1");
		getEntityManager().close();
		try {
			query.setParameter(1, getCalDate(), TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose23Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose24Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(int, Date, TemporalType) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose24Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.hireDate = ?1");
		getEntityManager().close();
		try {
			query.setParameter(1, getUtilDate(), TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose24Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose25Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(int, Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose25Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = ?1");
		getEntityManager().close();
		try {
			query.setParameter(1, 1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose25Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose26Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(Parameter<Calendar>, Calendar,
	 * TemporalType) after calling EntityManager.close()and expect
	 * IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose26Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.hireDate = :date");
		query.setParameter("date", getCalDate(), TemporalType.DATE);
		Parameter p = query.getParameter("date");
		getEntityManager().close();
		try {
			query.setParameter(p, getCalDate(), TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose26Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose27Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(Parameter<Date>, Date, TemporalType)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose27Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.hireDate = :date");
		query.setParameter("date", getCalDate(), TemporalType.DATE);
		Parameter p = query.getParameter("date", java.util.Date.class);
		getEntityManager().close();
		try {
			query.setParameter(p, getUtilDate(), TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose27Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose28Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(Parameter<T>, T) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose28Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = :id");
		query.setParameter("id", 1);
		Parameter p = query.getParameter("id");
		getEntityManager().close();
		try {
			query.setParameter(p, 1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose28Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose29Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(String, Calendar, TemporalType) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose29Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.hireDate = :date");
		getEntityManager().close();
		try {
			query.setParameter("date", getCalDate(), TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose29Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose30Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(String, Date, TemporalType) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose30Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.hireDate = :date");
		getEntityManager().close();
		try {
			query.setParameter("date", getUtilDate(), TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass) {
			throw new Exception("queryMethodsAfterClose30Test failed");
		}
	}

	/*
	 * @testName: queryMethodsAfterClose31Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
	 * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
	 * 
	 * @test_Strategy: Call Query.setParameter(String, Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void queryMethodsAfterClose31Test() throws Exception {
		boolean pass = false;

		Query query = getEntityManager().createQuery("select e.id from Employee e where e.id = :id");
		getEntityManager().close();
		try {
			query.setParameter("id", 1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("queryMethodsAfterClose31Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.getResultList() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose1Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.getResultList();

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose1Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.getResultList() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose2Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.getSingleResult();

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose2Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose3Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setFirstResult(int) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose3Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setFirstResult(1);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose3Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose4Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setFlushMode(FlushModeType) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose4Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setFlushMode(FlushModeType.AUTO);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose4Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose5Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setHint(String, Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose5Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setHint("foo", "bar");

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose5Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose6Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setLockMode(LockModeType) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose6Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setLockMode(LockModeType.NONE);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose6Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose7Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setMaxResults(int) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose7Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setMaxResults(1);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose7Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose8Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(int, Calendar, TemporalType)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose8Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		TypedQuery<A> tquery = getEntityManager().createQuery("SELECT a FROM A a WHERE (a.basicCalendar = ?1)",
				A.class);
		getEntityManager().close();
		try {
			tquery.setParameter(1, getCalDate(), TemporalType.DATE);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose8Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose9Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(int, Date, TemporalType) after
	 * calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose9Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		TypedQuery<A> tquery = getEntityManager().createQuery("SELECT a FROM A a WHERE (a.basicDate = ?1)", A.class);
		getEntityManager().close();
		try {
			tquery.setParameter(1, getUtilDate(), TemporalType.DATE);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose9Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose10Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(int, Object) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose10Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		TypedQuery<A> tquery = getEntityManager().createQuery("SELECT a FROM A a WHERE (a.id = ?1)", A.class);
		getEntityManager().close();
		try {
			tquery.setParameter(1, "1");

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose10Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose11Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(Parameter<Calendar>, Calendar,
	 * TemporalType) after calling EntityManager.close()and expect
	 * IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose11Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		ParameterExpression<Calendar> param1 = cbuilder.parameter(Calendar.class);
		cquery.where(cbuilder.equal(a.get("basicCalendar"), param1));
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setParameter(param1, getCalDate(), TemporalType.DATE);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose11Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose12Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(Parameter<Date>, Date,
	 * TemporalType) after calling EntityManager.close()and expect
	 * IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose12Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		ParameterExpression<Date> param1 = cbuilder.parameter(Date.class);
		cquery.where(cbuilder.equal(a.get("basicDate"), param1));
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setParameter(param1, getUtilDate(), TemporalType.DATE);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose12Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose13Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(Parameter<T>, T) after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose13Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		ParameterExpression<String> param1 = cbuilder.parameter(String.class);
		cquery.where(cbuilder.equal(a.get("id"), param1));
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setParameter(param1, "1");

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose13Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose14Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(String, Calendar, TemporalType)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose14Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		ParameterExpression<Calendar> param1 = cbuilder.parameter(Calendar.class, "calDate");
		cquery.where(cbuilder.equal(a.get("basicCalendar"), param1));
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setParameter("calDate", getCalDate(), TemporalType.DATE);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose14Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose15Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.setParameter(String, Date, TemporalType)
	 * after calling EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose15Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		ParameterExpression<Date> param1 = cbuilder.parameter(Date.class, "utilDate");
		cquery.where(cbuilder.equal(a.get("basicDate"), param1));
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setParameter("utilDate", getUtilDate(), TemporalType.DATE);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose15Test failed");
		}

	}

	/*
	 * @testName: typedQueryMethodsAfterClose16Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
	 * 
	 * @test_Strategy: Call TypedQuery.method_name() after calling
	 * EntityManager.close()and expect IllegalStateException
	 */
	@Test
	public void typedQueryMethodsAfterClose16Test() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
		Root<A> a = cquery.from(A.class);
		cquery.select(a);
		ParameterExpression<String> param1 = cbuilder.parameter(String.class, "idParam");
		cquery.where(cbuilder.equal(a.get("id"), param1));
		TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
		getEntityManager().close();
		try {
			tquery.setParameter("idParam", "1");

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("typedQueryMethodsAfterClose16Test failed");
		}

	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback18Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.getCriteriaBuilder() that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback18Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		getEntityManager().close();
		try {
			getEntityManager().getCriteriaBuilder();
			logger.log(Logger.Level.ERROR, "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logger.log(Logger.Level.TRACE, "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logger.log(Logger.Level.ERROR, "Transaction was not marked for rollback");
			} else {
				logger.log(Logger.Level.TRACE, "Transaction was marked for rollback");
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback18Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback19Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.getDelegate() that causes RuntimeException
	 * and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback19Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		getEntityManager().close();
		try {

			getEntityManager().getDelegate();
			logger.log(Logger.Level.ERROR, "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logger.log(Logger.Level.TRACE, "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logger.log(Logger.Level.ERROR, "Transaction was not marked for rollback");
			} else {
				logger.log(Logger.Level.TRACE, "Transaction was marked for rollback");
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback19Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback20Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.getEntityManagerFactory() that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback20Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		getEntityManager().close();
		try {

			getEntityManager().getEntityManagerFactory();
			logger.log(Logger.Level.ERROR, "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logger.log(Logger.Level.TRACE, "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logger.log(Logger.Level.ERROR, "Transaction was not marked for rollback");
			} else {
				logger.log(Logger.Level.TRACE, "Transaction was marked for rollback");
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback20Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback22Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.getMetamodel() that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback22Test() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		getEntityManager().close();
		try {
			getEntityManager().getMetamodel();
			logger.log(Logger.Level.ERROR, "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logger.log(Logger.Level.TRACE, "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logger.log(Logger.Level.ERROR, "Transaction was not marked for rollback");
			} else {
				logger.log(Logger.Level.TRACE, "Transaction was marked for rollback");
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback22Test failed");
		}
	}

	/*
	 * @testName: storedProcedureQueryMethodsAfterClose1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void storedProcedureQueryMethodsAfterClose1Test() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createNamedStoredProcedureQuery("get-id-firstname-lastname");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("storedProcedureQueryMethodsAfterClose1Test failed");
		}

	}

	/*
	 * @testName: storedProcedureQueryMethodsAfterClose2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void storedProcedureQueryMethodsAfterClose2Test() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS");
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("storedProcedureQueryMethodsAfterClose2Test failed");
		}

	}

	/*
	 * @testName: storedProcedureQueryMethodsAfterClose3Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void storedProcedureQueryMethodsAfterClose3Test() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("storedProcedureQueryMethodsAfterClose3Test failed");
		}

	}

	/*
	 * @testName: storedProcedureQueryMethodsAfterClose4Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:36
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void storedProcedureQueryMethodsAfterClose4Test() throws Exception {
		boolean pass = false;

		getEntityManager().close();
		try {
			getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS", "id-firstname-lastname");

			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("storedProcedureQueryMethodsAfterClose4Test failed");
		}

	}

	private void createOrderData() {

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Creating Orders");
			orders[0] = new Order(1, 111, "desc1");
			orders[1] = new Order(2, 222, "desc2");
			orders[2] = new Order(3, 333, "desc3");
			orders[3] = new Order(4, 444, "desc4");
			orders[4] = new Order(5, 555, "desc5");
			for (Order o : orders) {
				logger.log(Logger.Level.TRACE, "Persisting order:" + o.toString());
				getEntityManager().persist(o);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
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
