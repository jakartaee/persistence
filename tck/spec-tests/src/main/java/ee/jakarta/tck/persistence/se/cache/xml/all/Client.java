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

package ee.jakarta.tck.persistence.se.cache.xml.all;

import java.lang.System.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.DriverManagerConnection;
import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Cache;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	Properties jpaprops = new Properties();

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFile = { ORM_XML };
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_se_cache_xml_all.jar", pkgNameWithoutSuffix, (String[]) classes,
				PERSISTENCE_XML, xmlFile);

	}

	/*
	 * @class.testArgs: -ap tssql.stmt
	 */
	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			jpaprops.putAll(myProps);
			jpaprops.put("Insert_Jpa_Purchase_Order", System.getProperty("Insert_Jpa_Purchase_Order"));
			jpaprops.put("Select_Jpa_Purchase_Order", System.getProperty("Select_Jpa_Purchase_Order"));
			displayMap(jpaprops);
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: containsTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1496;
	 * 
	 * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
	 * persist an entity and verify it is in the cache
	 */
	@Test
	public void containsTest() throws Exception {
		Cache cache;
		boolean pass = false;
		if (cachingSupported) {
			try {

				EntityManager em2 = getEntityManager();
				EntityTransaction et = getEntityTransaction();

				et.begin();

				Order order = new Order(1, 101);
				em2.persist(order);
				logger.log(Logger.Level.TRACE, "persisted order " + order);

				em2.flush();
				et.commit();

				EntityManagerFactory emf = getEntityManagerFactory();
				cache = emf.getCache();

				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache contains order " + order);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "Cache not supported, bypassing test");
			pass = true;
		}
		if (!pass) {
			throw new Exception("containsTest failed");
		}

	}

	/*
	 * @testName: cacheStoreModeBYPASSTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1501;
	 * 
	 * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
	 * and a Persistence Context property of CacheStoreMode.BYPASS, persist an
	 * entity and verify it is not in the cache
	 */
	@Test
	public void cacheStoreModeBYPASSTest() throws Exception {
		Cache cache;
		boolean pass1, pass2, pass3;
		pass1 = pass2 = pass3 = false;
		if (cachingSupported) {
			try {
				logger.log(Logger.Level.TRACE, "Persist an order");
				getEntityTransaction().begin();
				getEntityManager().setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);
				Order order = new Order(1, 101);
				getEntityManager().persist(order);
				logger.log(Logger.Level.TRACE, "persisted order " + order);

				getEntityManager().flush();
				getEntityTransaction().commit();
				logger.log(Logger.Level.TRACE, "Verify the order persisted successfully, but it is not in the cache");
				cache = getEntityManagerFactory().getCache();

				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (!b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}

				int[] result = selectDataVIAJDBC(this.jpaprops, 1);
				Order order2 = new Order(result[0], result[1]);
				if (order.equals(order2)) {
					logger.log(Logger.Level.TRACE, "Entity was persisted correctly");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Entity was not persisted correctly - expected:" + order + ", actual:" + order2);
				}

				logger.log(Logger.Level.TRACE, "Find the order and verify it is not loaded into the cache");

				getEntityManager().find(Order.class, 1);
				cache = getEntityManagerFactory().getCache();
				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (!b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
						pass3 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "Cache not supported, bypassing test");
			pass1 = pass2 = pass3 = true;
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("cacheStoreModeBYPASSTest failed");
		}

	}

	/*
	 * @testName: cacheStoreModeUSETest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1501; PERSISTENCE:SPEC:1866;
	 * 
	 * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
	 * and a Persistence Context property of CacheStoreMode.USE, persist an entity
	 * and verify it is in the cache
	 */
	@Test
	public void cacheStoreModeUSETest() throws Exception {
		Cache cache;
		boolean pass1, pass2, pass3;
		pass1 = pass2 = pass3 = false;
		if (cachingSupported) {
			try {

				logger.log(Logger.Level.TRACE, "Persist an order");
				getEntityTransaction().begin();
				getEntityManager().setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.USE);
				Order order = new Order(1, 101);
				getEntityManager().persist(order);
				logger.log(Logger.Level.TRACE, "persisted order " + order);

				getEntityManager().flush();
				getEntityTransaction().commit();
				logger.log(Logger.Level.TRACE, "Verify the order persisted successfully, and it is in the cache");
				cache = getEntityManagerFactory().getCache();

				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}

				int[] result = selectDataVIAJDBC(this.jpaprops, 1);
				Order order2 = new Order(result[0], result[1]);
				if (order.equals(order2)) {
					logger.log(Logger.Level.TRACE, "Entity was persisted correctly");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Entity was not persisted correctly - expected:" + order + ", actual:" + order2);
				}

				logger.log(Logger.Level.TRACE, "Find the order and verify it is loaded into the cache");

				getEntityManager().find(Order.class, 1);
				cache = getEntityManagerFactory().getCache();
				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
						pass3 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "Cache not supported, bypassing test");
			pass1 = pass2 = pass3 = true;
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("cacheStoreModeUSETest failed");
		}

	}

	/*
	 * @testName: cacheStoreModeREFRESHTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1501;
	 * 
	 * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
	 * and a Persistence Context property of CacheStoreMode.REFRESH, persist an
	 * entity and verify it is in the cache
	 */
	@Test
	public void cacheStoreModeREFRESHTest() throws Exception {
		Cache cache;
		boolean pass1, pass2, pass3;
		pass1 = pass2 = pass3 = false;
		if (cachingSupported) {
			try {

				logger.log(Logger.Level.TRACE, "Persist an order");
				getEntityTransaction().begin();
				getEntityManager().setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.REFRESH);
				Order order = new Order(1, 101);
				getEntityManager().persist(order);
				logger.log(Logger.Level.TRACE, "persisted order " + order);

				getEntityManager().flush();
				getEntityTransaction().commit();
				logger.log(Logger.Level.TRACE, "Verify the order persisted successfully, and it is in the cache");
				cache = getEntityManagerFactory().getCache();

				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}

				int[] result = selectDataVIAJDBC(this.jpaprops, 1);
				Order order2 = new Order(result[0], result[1]);
				if (order.equals(order2)) {
					logger.log(Logger.Level.TRACE, "Entity was persisted correctly");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Entity was not persisted correctly - expected:" + order + ", actual:" + order2);
				}

				logger.log(Logger.Level.TRACE, "Find the order and verify it is loaded into the cache");

				getEntityManager().find(Order.class, 1);
				cache = getEntityManagerFactory().getCache();
				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
						pass3 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "Cache not supported, bypassing test");
			pass1 = pass2 = pass3 = true;
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("cacheStoreModeREFRESHTest failed");
		}

	}

	/*
	 * @testName: cacheRetrieveModeBYPASSTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1501;
	 * 
	 * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
	 * and a Persistence Context property of CacheRetrieveMode.BYPASS and
	 * CacheStoreMode.BYPASS, persist an entity and verify it is not in the cache
	 */
	@Test
	public void cacheRetrieveModeBYPASSTest() throws Exception {
		Cache cache;
		boolean pass1, pass2;
		pass1 = pass2 = false;
		if (cachingSupported) {
			try {

				logger.log(Logger.Level.TRACE, "Persist an order");
				createDataVIAJDBC(this.jpaprops);

				logger.log(Logger.Level.TRACE, "Verify order is not in Cache before executing find");
				cache = getEntityManagerFactory().getCache();
				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (!b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does not contain order ");
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does contain order ");
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}

				logger.log(Logger.Level.TRACE, "Find the order and verify it is not loaded into the cache");
				getEntityManager().setProperty("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
				getEntityManager().setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);

				Order order = getEntityManager().find(Order.class, 1);

				cache = getEntityManagerFactory().getCache();
				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (!b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
						pass2 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "Cache not supported, bypassing test");
			pass1 = pass2 = true;
		}
		if (!pass1 || !pass2) {
			throw new Exception("cacheRetrieveModeBYPASSTest failed");
		}

	}

	/*
	 * @testName: cacheRetrieveModeUSETest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1501;
	 * 
	 * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
	 * and a Persistence Context property of CacheRetrieveMode.USE and
	 * CacheStoreMode.BYPASS, persist an entity and verify it is in the cache
	 */
	@Test
	public void cacheRetrieveModeUSETest() throws Exception {
		Cache cache;
		boolean pass1, pass2;
		pass1 = pass2 = false;
		if (cachingSupported) {
			try {

				logger.log(Logger.Level.TRACE, "Persist an order");
				createDataVIAJDBC(this.jpaprops);

				logger.log(Logger.Level.TRACE, "Verify order is not in Cache before executing find");
				cache = getEntityManagerFactory().getCache();
				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (!b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does not contain order ");
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does contain order ");
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}

				logger.log(Logger.Level.TRACE, "Find the order and verify it is not loaded into the cache");
				getEntityManager().setProperty("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
				getEntityManager().setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);
				Order order = getEntityManager().find(Order.class, 1);

				cache = getEntityManagerFactory().getCache();
				if (cache != null) {
					boolean b = cache.contains(Order.class, 1);
					if (!b) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b + ", therefore cache does not contain order " + order);
						pass2 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b + ", therefore cache does contain order " + order);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "Cache not supported, bypassing test");
			pass1 = pass2 = true;
		}
		if (!pass1 || !pass2) {
			throw new Exception("cacheRetrieveModeUSETest failed");
		}

	}

	/*
	 * testName: findOverridesWithBYPASSTest assertion_ids: test_Strategy: Using the
	 * xml shared-cache-mode element with a value of ALL and a Persistence Context
	 * property of CacheRetrieveMode.USE but find overrides it with BYPASS, verify
	 * order is not in the cache
	 *
	 * public void findOverridesWithBYPASSTest() throws Exception { Cache cache;
	 * boolean pass1, pass2; pass1 = pass2 = false; if (cachingSupported) { try {
	 * 
	 * logger.log(Logger.Level.TRACE,"Persist an order");
	 * createDataVIAJDBC(this.props);
	 * 
	 * logger.log(Logger.Level.
	 * TRACE,"Verify order is not in Cache before executing find"); cache =
	 * getEntityManagerFactory().getCache(); if (cache != null) { boolean b =
	 * cache.contains(Order.class, 1); if (!b) {
	 * logger.log(Logger.Level.TRACE,"Cache returned: " + b +
	 * ", therefore cache does not contain order "); pass1 = true; } else {
	 * logger.log(Logger.Level.ERROR,"Cache returned: " + b +
	 * ", therefore cache does contain order "); } } else {
	 * logger.log(Logger.Level.ERROR,"Cache returned was null"); }
	 * getEntityManager().setProperty("jakarta.persistence.cache.retrieveMode",
	 * CacheRetrieveMode.USE);
	 * getEntityManager().setProperty("jakarta.persistence.cache.storeMode",
	 * CacheStoreMode.BYPASS); Map map = new Properties();
	 * map.put("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
	 * Order order = getEntityManager().find(Order.class, 1, map); cache =
	 * getEntityManagerFactory().getCache();
	 * 
	 * if (cache != null) { boolean b = cache.contains(Order.class, 1); if (!b) {
	 * logger.log(Logger.Level.TRACE,"Cache returned: " + b +
	 * ", therefore cache does not contain order " + order); pass2 = true; } else {
	 * logger.log(Logger.Level.ERROR,"Cache returned: " + b +
	 * ", therefore cache does contain order " + order); } } else {
	 * logger.log(Logger.Level.ERROR,"Cache returned was null"); } } catch
	 * (Exception e) {
	 * logger.log(Logger.Level.ERROR,"Unexpected exception occurred", e); } } else {
	 * logger.log(Logger.Level.INFO,"Cache not supported, bypassing test"); pass1 =
	 * pass2 = true; } if (!pass1 || !pass2) { throw new
	 * Exception("findOverridesWithBYPASSTest failed"); }
	 * 
	 * }
	 */
	public int[] selectDataVIAJDBC(Properties p, int id) {
		int[] params = new int[2];
		displayMap(p);
		Connection conn = null;
		try {

			DriverManagerConnection dmCon = new DriverManagerConnection();
			conn = dmCon.getConnection(p);
			String selectString = p.getProperty("Select_Jpa_Purchase_Order", "");
			logger.log(Logger.Level.TRACE, "ASDF:" + selectString);
			PreparedStatement pStmt = conn.prepareStatement(selectString);
			pStmt.setInt(1, id);
			logger.log(Logger.Level.TRACE, "SQL to be executed:" + pStmt.toString());
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				params[0] = rs.getInt(1);
				params[1] = rs.getInt(2);
			} else {
				throw new SQLException("Data not found");
			}
		} catch (SQLException sqlex) {
			logger.log(Logger.Level.ERROR, "Received SQLException", sqlex);
		} catch (ClassNotFoundException cnfe) {
			logger.log(Logger.Level.ERROR, "Received ClassNotFoundException", cnfe);
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received Exception", ex);
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
			}
		}
		return params;
	}

	public void createDataVIAJDBC(Properties p) {
		Connection conn = null;

		try {
			// displayMap(p);
			DriverManagerConnection dmCon = new DriverManagerConnection();
			conn = dmCon.getConnection(p);

			String insertString = p.getProperty("Insert_Jpa_Purchase_Order", "");
			PreparedStatement pStmt = conn.prepareStatement(insertString);
			pStmt.setInt(1, 1);
			pStmt.setInt(2, 101);
			logger.log(Logger.Level.TRACE, "SQL to be executed:" + pStmt.toString());
			int rows = pStmt.executeUpdate();
			logger.log(Logger.Level.TRACE, "Row inserted:" + rows);
		} catch (SQLException sqlex) {
			logger.log(Logger.Level.ERROR, "Received SQLException", sqlex);
		} catch (ClassNotFoundException cnfe) {
			logger.log(Logger.Level.ERROR, "Received ClassNotFoundException", cnfe);
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received Exception", ex);
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
			}
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
