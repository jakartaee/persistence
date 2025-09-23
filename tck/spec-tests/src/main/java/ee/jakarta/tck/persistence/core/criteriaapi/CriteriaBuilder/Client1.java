/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
import java.util.ArrayList;
import java.util.Collection;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.UtilSetup;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

public class Client1 extends UtilSetup {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaBuilder1.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @testName: createQuery
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:756; PERSISTENCE:SPEC:1701;
	 * PERSISTENCE:SPEC:1703; PERSISTENCE:SPEC:1704;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createQuery() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery cquery = cbuilder.createQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createQuery Test  failed");
		}
	}

	/*
	 * @testName: createQuery2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:757; PERSISTENCE:SPEC:1703;
	 * PERSISTENCE:SPEC:1704;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createQuery2() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createQuery2 Test  failed");
		}
	}

	/*
	 * @testName: createTuple
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:758; PERSISTENCE:SPEC:1703;
	 * PERSISTENCE:SPEC:1704;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createTuple() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query Tuple");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query Tuple");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createTuple Test  failed");
		}
	}

	/*
	 * @testName: tupleSelectionArrayIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:873
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void tupleSelectionArrayIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
			if (cquery != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
				Root<Customer> cust = cquery.from(Customer.class);

				Selection[] s = { cust.get("id"), cust.get("name") };

				logger.log(Logger.Level.INFO, "Testing tuple");
				try {
					qbuilder.tuple(qbuilder.tuple(s));
					logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass1 = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}

				logger.log(Logger.Level.INFO, "Testing array");
				try {
					qbuilder.tuple(qbuilder.array(s));
					logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass2 = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("tupleSelectionArrayIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: literalIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:807
	 *
	 * @test_Strategy:
	 */
	@Test
	public void literalIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			cbuilder.literal(null);
			logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("literalIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: trimspecTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:915; PERSISTENCE:JAVADOC:916
	 *
	 * @test_Strategy:
	 *
	 *
	 */
	@Test
	public void trimspecTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		Collection<CriteriaBuilder.Trimspec> expected = new ArrayList();
		expected.add(CriteriaBuilder.Trimspec.BOTH);
		expected.add(CriteriaBuilder.Trimspec.LEADING);
		expected.add(CriteriaBuilder.Trimspec.TRAILING);

		try {
			CriteriaBuilder.Trimspec[] ts = CriteriaBuilder.Trimspec.values();
			if (ts.length == 3) {
				for (CriteriaBuilder.Trimspec tspec : ts) {
					pass1 = true;
					if (expected.contains(tspec)) {
						logger.log(Logger.Level.INFO, "Testing valueOf:" + tspec);
						CriteriaBuilder.Trimspec.valueOf(tspec.toString());
					} else {
						pass2 = false;
						logger.log(Logger.Level.ERROR, "values() returned incorrect value:" + tspec);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected number of values:3, actual:" + ts.length);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("trimspecTest failed");

		}
	}

	/*
	 * @testName: createCriteriaDeleteTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1676; PERSISTENCE:SPEC:1701;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void createCriteriaDeleteTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
			if (cd != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null CriteriaDelete");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null CriteriaDelete");
			}
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass) {
			throw new Exception("createCriteriaDeleteTest test failed");
		}
	}

	/*
	 * @testName: createCriteriaUpdateTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1677; PERSISTENCE:SPEC:1701;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void createCriteriaUpdateTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
			if (cd != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null CriteriaUpdate");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null CriteriaUpdate");
			}
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass) {
			throw new Exception("createCriteriaUpdateTest test failed");
		}
	}

	/*
	 * @testName: coalesceTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:747
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void coalesceTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		CriteriaBuilder.Coalesce col = cbuilder.coalesce();
		if (col != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Coalesce");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Coalesce");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("coalesceTest failed");
		}
	}

}
