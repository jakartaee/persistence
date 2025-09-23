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
import java.util.List;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Client3 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	Properties props = null;

	public Client3() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Member_", pkgName + "Member", pkgName + "Order_", pkgName + "Order" };
		return createDeploymentJar("jpa_core_entityManagerFactory3.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setupMember() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createMemberTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanupNoData() throws Exception {
		try {
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: addNamedQueryLockModeTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1527; PERSISTENCE:SPEC:1311;
	 * PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.2;
	 *
	 * @test_Strategy: Test that lock mode of addNamedQuery is retained or can be
	 * overridden
	 */
	@Test
	public void addNamedQueryLockModeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;

		try {
			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			logger.log(Logger.Level.TRACE, "Defining queries");
			Query query = getEntityManager().createQuery("select m from Member m where m.memberId=1");
			query.setLockMode(LockModeType.NONE);
			getEntityManagerFactory().addNamedQuery("query", query);

			CriteriaQuery<Member> cquery = cbuilder.createQuery(Member.class);
			Root<Member> member = cquery.from(Member.class);
			cquery.select(member);
			cquery.where(cbuilder.equal(member.get(Member_.memberId), 1));
			TypedQuery<Member> typedQuery = getEntityManager().createQuery(cquery);
			typedQuery.setLockMode(LockModeType.NONE);
			getEntityManagerFactory().addNamedQuery("typed_query", typedQuery);

			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query with different lock mode than the original");
				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean ok1 = false;
				LockModeType lmt = namedQuery.getLockMode();
				if (lmt != null) {
					if (lmt.equals(LockModeType.NONE)) {
						logger.log(Logger.Level.TRACE, "Received expected lock mode before change:" + lmt.name());
						ok1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected lock mode before change:" + LockModeType.NONE.name()
								+ ", actual:" + lmt.name());
					}
				} else {
					logger.log(Logger.Level.ERROR, "getLockModeType returned null");
				}
				namedQuery.setLockMode(LockModeType.PESSIMISTIC_READ);
				lmt = namedQuery.getLockMode();
				boolean ok2 = false;
				if (lmt.equals(LockModeType.PESSIMISTIC_READ)) {
					logger.log(Logger.Level.TRACE, "Received LockModeType:" + lmt.name());
					ok2 = true;
				} else if (lmt.equals(LockModeType.PESSIMISTIC_WRITE)) {
					logger.log(Logger.Level.TRACE,
							"Received LockModeType:" + lmt + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
					ok2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected lock mode after change:"
							+ LockModeType.PESSIMISTIC_READ.name() + ", actual:" + lmt.name());
				}
				List<Member> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					Member result = lResult.get(0);
					if (result.getMemberId() == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result.getMemberId());
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result.getMemberId());
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (Member m : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + m.getMemberId());
					}
				}
				if (foundOne && ok1 && ok2) {
					pass1 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			} finally {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query verify original lock mode is active");
				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean ok1 = false;
				LockModeType lmt = namedQuery.getLockMode();
				if (lmt != null) {
					if (lmt.equals(LockModeType.NONE)) {
						logger.log(Logger.Level.TRACE, "Received expected lock mode before change:" + lmt.name());
						ok1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected lock mode before change:" + LockModeType.NONE.name()
								+ ", actual:" + lmt.name());
					}
				} else {
					logger.log(Logger.Level.ERROR, "getLockModeType returned null");
				}

				List<Member> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					Member result = lResult.get(0);
					if (result.getMemberId() == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result.getMemberId());
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result.getMemberId());
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (Member m : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + m.getMemberId());
					}
				}
				if (foundOne && ok1) {
					pass2 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			} finally {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing query definition can be replaced ");
				Query query2 = getEntityManager().createQuery("select m from Member m where m.memberId=2")
						.setLockMode(LockModeType.PESSIMISTIC_READ);
				getEntityManagerFactory().addNamedQuery("query", query2);
				Query namedQuery = getEntityManager().createNamedQuery("query");
				boolean ok1 = false;
				LockModeType lmt = namedQuery.getLockMode();
				if (lmt != null) {
					if (lmt.equals(LockModeType.PESSIMISTIC_READ)) {
						logger.log(Logger.Level.TRACE, "Received LockModeType:" + lmt.name());
						ok1 = true;
					} else if (lmt.equals(LockModeType.PESSIMISTIC_WRITE)) {
						logger.log(Logger.Level.TRACE,
								"Received LockModeType:" + lmt + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
						ok1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected lock mode after change:"
								+ LockModeType.PESSIMISTIC_READ.name() + ", actual:" + lmt.name());
					}
				} else {
					logger.log(Logger.Level.ERROR, "getLockModeType returned null");
				}
				List<Member> lResult = namedQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					Member result = lResult.get(0);
					if (result.getMemberId() == 2) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result.getMemberId());
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:2, actual:" + result.getMemberId());
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:2, actual:" + lResult.size());
					for (Member m : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + m.getMemberId());
					}
				}
				if (foundOne && ok1) {
					pass3 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			} finally {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing TypedQuery with lock mode different than the original");
				TypedQuery<Member> namedTypeQuery = getEntityManager().createNamedQuery("typed_query", Member.class);
				boolean ok1 = false;
				LockModeType lmt = namedTypeQuery.getLockMode();
				if (lmt != null) {
					if (lmt.equals(LockModeType.NONE)) {
						logger.log(Logger.Level.TRACE, "Received expected lock mode before change:" + lmt.name());
						ok1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected lock mode before change:" + LockModeType.NONE.name()
								+ ", actual:" + lmt.name());
					}
				} else {
					logger.log(Logger.Level.ERROR, "getLockModeType returned null");
				}
				namedTypeQuery.setLockMode(LockModeType.PESSIMISTIC_READ);
				lmt = namedTypeQuery.getLockMode();
				boolean ok2 = false;
				if (lmt.equals(LockModeType.PESSIMISTIC_READ)) {
					logger.log(Logger.Level.TRACE, "Received expected lock mode after change:" + lmt.name());
					ok2 = true;
				} else if (lmt.equals(LockModeType.PESSIMISTIC_WRITE)) {
					logger.log(Logger.Level.TRACE,
							"Received LockModeType:" + lmt + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
					ok2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected lock mode after change:"
							+ LockModeType.PESSIMISTIC_READ.name() + ", actual:" + lmt.name());
				}

				List<Member> lResult = namedTypeQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					Member result = lResult.get(0);
					if (result.getMemberId() == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result.getMemberId());
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result.getMemberId());
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (Member m : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + m.getMemberId());
					}
				}
				if (foundOne && ok1 && ok2) {
					pass4 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			} finally {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
			try {
				getEntityTransaction().begin();
				logger.log(Logger.Level.INFO, "*********************************");
				logger.log(Logger.Level.INFO, "Testing TypedQuery verify original lock mode is active");
				TypedQuery<Member> namedTypeQuery = getEntityManager().createNamedQuery("typed_query", Member.class);
				boolean ok1 = false;
				LockModeType lmt = namedTypeQuery.getLockMode();
				if (lmt != null) {
					if (lmt.equals(LockModeType.NONE)) {
						logger.log(Logger.Level.TRACE, "Received expected lock mode before change:" + lmt.name());
						ok1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected lock mode before change:" + LockModeType.NONE.name()
								+ ", actual:" + lmt.name());
					}
				} else {
					logger.log(Logger.Level.ERROR, "getLockModeType returned null");
				}
				List<Member> lResult = namedTypeQuery.getResultList();
				boolean foundOne = false;
				if (lResult.size() == 1) {
					Member result = lResult.get(0);
					if (result.getMemberId() == 1) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + result.getMemberId());
						foundOne = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:1, actual:" + result.getMemberId());
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get correct number of results, expected:1, actual:" + lResult.size());
					for (Member m : lResult) {
						logger.log(Logger.Level.ERROR, "Ids received:" + m.getMemberId());
					}
				}
				if (foundOne && ok1) {
					pass5 = true;
				}
				getEntityTransaction().commit();

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			} finally {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
			throw new Exception("addNamedQueryLockModeTest failed");
		}
	}

	private void createMemberTestData() {

		try {
			getEntityTransaction().begin();

			Member[] members = new Member[5];
			members[0] = new Member(1, "1");
			members[1] = new Member(2, "2");
			members[2] = new Member(3, "3");
			members[3] = new Member(4, "4");
			members[4] = new Member(5, "5");

			for (Member m : members) {
				logger.log(Logger.Level.TRACE, "Persisting member:" + m.toString());
				getEntityManager().persist(m);
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
