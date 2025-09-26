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

package ee.jakarta.tck.persistence.core.entityManagerFactory;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Client1 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	Properties props = null;

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Member_", pkgName + "Member", pkgName + "Order_", pkgName + "Order" };
		return createDeploymentJar("jpa_core_entityManagerFactory1.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createOrderTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			removeTestData();
			logger.log(Logger.Level.TRACE, "done cleanup, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: addNamedQueryMaxResultTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1527; PERSISTENCE:SPEC:1311;
	 * PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.2;
	 *
	 * @test_Strategy: Test that max result of addNamedQuery is retained or can be
	 * overridden
	 */
	@Test
	public void addNamedQueryMaxResultTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		boolean pass6 = false;
		boolean pass7 = false;
		try {
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			logger.log(Logger.Level.TRACE, "Defining queries");
			Query nativeQuery = getEntityManager()
					.createNativeQuery("Select o.ID from PURCHASE_ORDER o ORDER BY o.ID ASC");
			nativeQuery.setMaxResults(1);
			getEntityManagerFactory().addNamedQuery("native_query", nativeQuery);

			Query query = getEntityManager().createQuery("Select o.id from Order o ORDER BY o.id ASC");
			query.setMaxResults(1);
			getEntityManagerFactory().addNamedQuery("query", query);

			CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order.get(Order_.id));
			cquery.orderBy(cbuilder.asc(order.get("id")));
			TypedQuery<Integer> typedQuery = getEntityManager().createQuery(cquery);
			typedQuery.setMaxResults(1);
			getEntityManagerFactory().addNamedQuery("typed_query", typedQuery);

			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing native query with different max result than the original");
				Query namedQuery = getEntityManager().createNamedQuery("native_query");
				boolean configOK = true;
				if (namedQuery.getMaxResults() == 1) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result before change:" + namedQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result before change:1, actual:" + namedQuery.getMaxResults());
					configOK = false;
				}
				namedQuery.setMaxResults(2);
				if (namedQuery.getMaxResults() == 2) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result after change:" + namedQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result after change:2, actual:" + namedQuery.getMaxResults());
					configOK = false;
				}
				List lResult = namedQuery.getResultList();
				List<Integer> iList = new ArrayList<Integer>();
				for (Object o : lResult) {
					iList.add(convertToInt(o));
				}
				boolean foundOne = false;
				boolean foundTwo = false;
				if (iList.size() == 2) {
					for (int i : iList) {
						if (i == 1) {
							foundOne = true;
							logger.log(Logger.Level.TRACE, "Found expected id:1");
						} else if (i == 2) {
							foundTwo = true;
							logger.log(Logger.Level.TRACE, "Found expected id:2");
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected result:" + i);
						}

					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected number of results, expected:2, actual:" + iList.size());
					for (Integer i : iList) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && foundTwo && configOK) {
					pass1 = true;
				}

				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing native query verify original max result is still active");
				Query namedQuery = getEntityManager().createNamedQuery("native_query");
				boolean configOK = true;
				if (namedQuery.getMaxResults() == 1) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result before change:" + namedQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result before change:1, actual:" + namedQuery.getMaxResults());
					configOK = false;
				}

				List lResult = namedQuery.getResultList();
				List<Integer> iList = new ArrayList<Integer>();
				for (Object o : lResult) {
					iList.add(convertToInt(o));
				}
				boolean foundOne = false;
				if (iList.size() == 1) {
					int result = iList.get(0);
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected Order");
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + iList.size());
					for (int i : iList) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass2 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query with different max result than the original");

				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean configOK = true;
				if (namedQuery.getMaxResults() == 1) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result before change:" + namedQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result before change:1, actual:" + namedQuery.getMaxResults());
					configOK = false;
				}
				namedQuery.setMaxResults(2);
				if (namedQuery.getMaxResults() == 2) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result after change:" + namedQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result after change:2, actual:" + namedQuery.getMaxResults());
					configOK = false;
				}
				List lResult = namedQuery.getResultList();
				List<Integer> iList = new ArrayList<Integer>();
				for (Object o : lResult) {
					iList.add(convertToInt(o));
				}
				boolean foundOne = false;
				boolean foundTwo = false;
				if (iList.size() == 2) {
					for (int i : iList) {
						if (i == 1) {
							foundOne = true;
							logger.log(Logger.Level.TRACE, "Found expected id:1");
						} else if (i == 2) {
							foundTwo = true;
							logger.log(Logger.Level.TRACE, "Found expected id:2");
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected result:" + i);
						}

					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected number of results, expected:2, actual:" + iList.size());
					for (Integer i : iList) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && foundTwo && configOK) {
					pass3 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query verify original max result is active");
				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean configOK = true;
				if (namedQuery.getMaxResults() == 1) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result before change:" + namedQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result before change:1, actual:" + namedQuery.getMaxResults());
					configOK = false;
				}

				List lResult = namedQuery.getResultList();
				List<Integer> iList = new ArrayList<Integer>();
				for (Object o : lResult) {
					iList.add(convertToInt(o));
				}
				boolean foundOne = false;
				if (iList.size() == 1) {
					int result = iList.get(0);
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + iList.size());
					for (int i : iList) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass4 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query definition can be replaced ");
				Query query2 = getEntityManager().createQuery("Select o.id from Order o where o.id in (1,2) ");
				query2.setMaxResults(2);
				getEntityManagerFactory().addNamedQuery("query", query2);
				Query namedQuery = getEntityManager().createNamedQuery("query");

				boolean configOK = true;
				if (namedQuery.getMaxResults() == 2) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result before change:" + namedQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result before change:2, actual:" + namedQuery.getMaxResults());
					configOK = false;
				}
				List lResult = namedQuery.getResultList();
				List<Integer> iList = new ArrayList<Integer>();
				for (Object o : lResult) {
					iList.add(convertToInt(o));
				}

				List<Integer> lExpected = new ArrayList<Integer>();
				lExpected.add(1);
				lExpected.add(2);
				if (iList.containsAll(lExpected) && (lExpected.containsAll(iList))
						&& iList.size() == lExpected.size()) {
					logger.log(Logger.Level.TRACE, "Received expected ids");
					if (configOK) {
						pass5 = true;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Did not receive expected results:");
					for (Integer i : lExpected) {
						logger.log(Logger.Level.ERROR, "Expected:" + i);
					}
					for (Integer i : iList) {
						logger.log(Logger.Level.ERROR, "Expected:" + i);
					}
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing TypedQuery with max result different than the original");
				TypedQuery<Integer> namedTypeQuery = getEntityManager().createNamedQuery("typed_query", Integer.class);
				boolean configOK = true;
				if (namedTypeQuery.getMaxResults() == 1) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result before change:" + namedTypeQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result before change:1, actual:" + namedTypeQuery.getMaxResults());
					configOK = false;
				}
				namedTypeQuery.setMaxResults(2);
				if (namedTypeQuery.getMaxResults() == 2) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result after change:" + namedTypeQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result after change:2, actual:" + namedTypeQuery.getMaxResults());
					configOK = false;
				}
				List<Integer> lResult = namedTypeQuery.getResultList();
				List<Integer> iList = new ArrayList<Integer>();
				for (Object o : lResult) {
					iList.add(convertToInt(o));
				}
				boolean foundOne = false;
				boolean foundTwo = false;
				if (iList.size() == 2) {
					for (int i : iList) {
						if (i == 1) {
							foundOne = true;
							logger.log(Logger.Level.TRACE, "Found expected id:1");
						} else if (i == 2) {
							foundTwo = true;
							logger.log(Logger.Level.TRACE, "Found expected id:2");
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected result:" + i);
						}

					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected number of results, expected:2, actual:" + iList.size());
					for (Integer i : iList) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && foundTwo && configOK) {
					pass6 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing TypedQuery verify original max result is active");
				TypedQuery<Integer> namedTypeQuery = getEntityManager().createNamedQuery("typed_query", Integer.class);
				boolean configOK = true;
				if (namedTypeQuery.getMaxResults() == 1) {
					logger.log(Logger.Level.TRACE,
							"Received expected Max Result before change:" + namedTypeQuery.getMaxResults());
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected Max Result before change:1, actual:" + namedTypeQuery.getMaxResults());
					configOK = false;
				}

				List<Integer> lResult = namedTypeQuery.getResultList();
				List<Integer> iList = new ArrayList<Integer>();
				for (Object o : lResult) {
					iList.add(convertToInt(o));
				}
				boolean foundOne = false;
				if (iList.size() == 1) {
					int result = iList.get(0);
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + iList.size());
					for (int i : iList) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass7 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7) {
			throw new Exception("addNamedQueryMaxResultTest failed");
		}
	}

	/*
	 * @testName: addNamedQueryFlushModeTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1527; PERSISTENCE:SPEC:1311;
	 * PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.2;
	 *
	 * @test_Strategy: Test that flush mode of addNamedQuery is retained or can be
	 * overridden
	 */
	@Test
	public void addNamedQueryFlushModeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		boolean pass6 = false;
		boolean pass7 = false;
		try {
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			logger.log(Logger.Level.TRACE, "Defining queries");
			Query nativeQuery = getEntityManager().createNativeQuery("Select ID from PURCHASE_ORDER where ID=1");
			nativeQuery.setFlushMode(FlushModeType.AUTO);
			getEntityManagerFactory().addNamedQuery("native_query", nativeQuery);

			Query query = getEntityManager().createQuery("select o.id from Order o where o.id=1");
			query.setFlushMode(FlushModeType.AUTO);
			getEntityManagerFactory().addNamedQuery("query", query);

			CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order.get(Order_.id));
			cquery.where(cbuilder.equal(order.get(Order_.id), 1));
			TypedQuery<Integer> typedQuery = getEntityManager().createQuery(cquery);
			typedQuery.setFlushMode(FlushModeType.AUTO);
			getEntityManagerFactory().addNamedQuery("typed_query", typedQuery);

			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing native query with different flush mode than the original");
				Query namedQuery = getEntityManager().createNamedQuery("native_query");
				boolean configOK = true;
				if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode before change:" + namedQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode before change:" + FlushModeType.AUTO
							+ ", actual:" + namedQuery.getFlushMode());
					configOK = false;
				}
				namedQuery.setFlushMode(FlushModeType.COMMIT);
				if (namedQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode after change:" + namedQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode after change:" + FlushModeType.AUTO
							+ ", actual:" + namedQuery.getFlushMode());
					configOK = false;
				}
				List<Integer> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					int result = convertToInt(lResult.get(0));
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (int i : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass1 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing native query verify original flush mode is still active");
				Query namedQuery = getEntityManager().createNamedQuery("native_query");
				boolean configOK = true;
				if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode before change:" + namedQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode before change:" + FlushModeType.AUTO.name()
							+ ", actual:" + namedQuery.getFlushMode());
					configOK = false;
				}

				List<Integer> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					int result = convertToInt(lResult.get(0));
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (int i : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass2 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query with different flush mode than the original");
				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean configOK = true;
				if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode before change:" + namedQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode before change:" + FlushModeType.AUTO
							+ ", actual:" + namedQuery.getFlushMode());
					configOK = false;
				}
				namedQuery.setFlushMode(FlushModeType.COMMIT);
				if (namedQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode after change:" + namedQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode after change:" + FlushModeType.AUTO
							+ ", actual:" + namedQuery.getFlushMode());
					configOK = false;
				}
				List<Integer> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					int result = lResult.get(0);
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (int i : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass3 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query verify original flush mode is active");
				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean configOK = true;
				if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode before change:" + namedQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode before change:" + FlushModeType.AUTO.name()
							+ ", actual:" + namedQuery.getFlushMode());
					configOK = false;
				}

				List<Integer> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					int result = lResult.get(0);
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (int i : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass4 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query definition can be replaced ");
				Query query2 = getEntityManager().createQuery("Select o.id from Order o where o.id = 2")
						.setFlushMode(FlushModeType.COMMIT);
				getEntityManagerFactory().addNamedQuery("query", query2);
				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean configOK = true;
				if (namedQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode before change:" + namedQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode before change:" + FlushModeType.COMMIT.name()
							+ ", actual:" + namedQuery.getFlushMode());
					configOK = false;
				}

				List<Integer> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					int result = lResult.get(0);
					if (result == 2) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:2, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (int i : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass5 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing TypedQuery with flush mode different than the original");
				TypedQuery<Integer> namedTypeQuery = getEntityManager().createNamedQuery("typed_query", Integer.class);
				boolean configOK = true;
				if (namedTypeQuery.getFlushMode().equals(FlushModeType.AUTO)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode before change:" + namedTypeQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode before change:" + FlushModeType.AUTO
							+ ", actual:" + namedTypeQuery.getFlushMode());
					configOK = false;
				}
				namedTypeQuery.setFlushMode(FlushModeType.COMMIT);
				if (namedTypeQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode after change:" + namedTypeQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode after change:" + FlushModeType.AUTO
							+ ", actual:" + namedTypeQuery.getFlushMode());
					configOK = false;
				}
				List<Integer> lResult = namedTypeQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					int result = lResult.get(0);
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (int i : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass6 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing TypedQuery verify original flush mode is active");
				TypedQuery<Integer> namedTypeQuery = getEntityManager().createNamedQuery("typed_query", Integer.class);
				boolean configOK = true;
				if (namedTypeQuery.getFlushMode().equals(FlushModeType.AUTO)) {
					logger.log(Logger.Level.TRACE,
							"Received expected flush mode before change:" + namedTypeQuery.getFlushMode());
				} else {
					logger.log(Logger.Level.ERROR, "Expected flush mode before change:" + FlushModeType.AUTO.name()
							+ ", actual:" + namedTypeQuery.getFlushMode());
					configOK = false;
				}

				List<Integer> lResult = namedTypeQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					int result = lResult.get(0);
					if (result == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result);
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (int i : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + i);
					}
				}
				if (foundOne && configOK) {
					pass7 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7) {
			throw new Exception("addNamedQueryFlushModeTest failed");
		}
	}

	private void createOrderTestData() {

		try {
			getEntityTransaction().begin();
			Order[] orders = new Order[5];
			orders[0] = new Order(1, 111);
			orders[1] = new Order(2, 222);
			orders[2] = new Order(3, 333);
			orders[3] = new Order(4, 444);
			orders[4] = new Order(5, 555);

			for (Order o : orders) {
				logger.log(Logger.Level.TRACE, "Persisting order:" + o.toString());
				getEntityManager().persist(o);
			}
			getEntityManager().flush();
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
			clearCache();
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM MEMBER").executeUpdate();
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
