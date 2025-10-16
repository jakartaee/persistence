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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.schema30.Address;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Spouse;
import ee.jakarta.tck.persistence.common.schema30.UtilCustAliasProductData;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.Trimspec;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client2 extends UtilCustAliasProductData {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaBuilder2.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @testName: construct
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:752; PERSISTENCE:JAVADOC:1470;
	 * PERSISTENCE:JAVADOC:1026; PERSISTENCE:SPEC:1705; PERSISTENCE:SPEC:1752;
	 * PERSISTENCE:SPEC:1754; PERSISTENCE:SPEC:1669;
	 *
	 * @test_Strategy: convert the following jpql to CriteriaQuery
	 *
	 * SELECT NEW ee.jakarta.tck.persistence.core.query.language.schema30.Customer (c.id,
	 * c.name) FROM Customer c where c.home.city = 'Roslindale'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void construct() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];

		expected[0] = customerRef[16].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(cbuilder.construct(ee.jakarta.tck.persistence.common.schema30.Customer.class,
					customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class))))
					.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), "Roslindale"));
			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> actual = tq.getResultList();
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
			throw new Exception("construct Test  failed");
		}
	}

	/*
	 * @testName: tupleIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:433
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleIntTest() throws Exception {
		boolean pass1 = true;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class)));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();

			for (Tuple t : result) {
				Integer id = Integer.valueOf((String) t.get(0));
				String name = (String) t.get(1);
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass1 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass1 = false;
					}
				}

			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleIntTest test failed");
		}
	}

	/*
	 * @testName: tupleToArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:435; PERSISTENCE:SPEC:1727;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c where (id = 3 or id = 4)
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleToArrayTest() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		expected.add(customerRef[2].getId());
		expected.add(customerRef[2].getName());
		expected.add(customerRef[3].getId());
		expected.add(customerRef[3].getName());

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class)));

			cquery.where(
					cbuilder.or(cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "3"),
							cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "4"))

			);

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			List<String> actual = new ArrayList<String>();
			for (Tuple t : result) {
				for (Object o : t.toArray()) {
					logger.log(Logger.Level.TRACE, "Object:" + o);
					actual.add((String) o);
				}
			}
			if (TestUtil.traceflag) {
				logger.log(Logger.Level.TRACE, "actual" + actual);
			}

			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
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
			throw new Exception("tupleToArrayTest test failed");
		}
	}

	/*
	 * @testName: tupleIntClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:432; PERSISTENCE:SPEC:1706
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleIntClassTest() throws Exception {
		boolean pass1 = true;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class)));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();

			for (Tuple t : result) {
				Integer id = Integer.valueOf((String) t.get(0));
				String name = (String) t.get(1);
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass1 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass1 = false;
					}
				}

			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleIntClassTest test failed");
		}
	}

	/*
	 * @testName: tupleGetIntClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:602; PERSISTENCE:SPEC:1303;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c Call Tuple.get() using a tuple element that does not
	 * exist
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleGetIntClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			logger.log(Logger.Level.TRACE, "Use Tuple Query");
			cquery.multiselect(customer.get("id"), customer.get("name"), customer.get("home"));
			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			Tuple t = result.get(0);
			logger.log(Logger.Level.INFO, "Testing valid index");
			logger.log(Logger.Level.TRACE, "value:" + t.get(1, String.class));

			logger.log(Logger.Level.INFO, "Testing invalid index");
			try {
				t.get(99, String.class);

				logger.log(Logger.Level.ERROR,
						"Did not get expected IllegalArgumentException for invalid index:" + t.get(99, String.class));
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
				if (getEntityTransaction().getRollbackOnly() != true) {
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			logger.log(Logger.Level.INFO, "Testing invalid type");

			try {
				t.get(2, Date.class);

				logger.log(Logger.Level.ERROR,
						"Did not get expected IllegalArgumentException for invalid type:" + t.get(2, Date.class));
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleGetIntClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: tupleElementGetJavaTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:437
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleElementGetJavaTypeTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			Path<String> idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			Class type = idPath.getJavaType();
			if (type.getSimpleName().equals("String")) {
				logger.log(Logger.Level.TRACE, "type=" + type.getSimpleName());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected element type of String, actual value:" + type.getSimpleName());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass) {
			throw new Exception("tupleElementGetJavaTypeTest failed");
		}
	}

	/*
	 * @testName: tupleSelectionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:872; PERSISTENCE:SPEC:1752;
	 * PERSISTENCE:SPEC:1754;
	 *
	 * @test_Strategy: Call tuple(Selection[]) and verify result Select c.id, c.name
	 * from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleSelectionArrayTest() throws Exception {
		boolean pass = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
			if (cquery != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
				Root<Customer> cust = cquery.from(Customer.class);

				// Get Metamodel from Root
				EntityType<Customer> Customer_ = cust.getModel();
				logger.log(Logger.Level.TRACE, "Use Tuple Query");

				cquery.where(qbuilder.equal(cust.get(Customer_.getSingularAttribute("id", String.class)), "4"));

				Selection[] s = { cust.get("id"), cust.get("name") };
				cquery.select(qbuilder.tuple(s));

				Query q = getEntityManager().createQuery(cquery);

				List result = q.getResultList();
				if (result.size() == 1) {
					Tuple t = (Tuple) result.get(0);
					String id = (String) t.get(0);

					if (id.equals(customerRef[3].getId())) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + id);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:" + customerRef[3].getId() + ", actual:" + id);
					}
					String name = (String) t.get(1);

					if (name.equals(customerRef[3].getName())) {
						logger.log(Logger.Level.TRACE, "Received expected name:" + name);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[3].getName() + ", actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Received incorrect result size - Expected: 1, Actual:" + result.size());
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {

			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("tupleSelectionArrayTest failed");
		}
	}

	/*
	 * @testName: array
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:739; PERSISTENCE:SPEC:1752;
	 * PERSISTENCE:SPEC:1754;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery and return the
	 * result as an object array
	 *
	 * Select c.id, c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void array() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery cquery = cbuilder.createQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.select(cbuilder.array(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class))));

			Query q = getEntityManager().createQuery(cquery);

			List<Object[]> result = q.getResultList();

			List<Integer> actual = new ArrayList<Integer>();

			pass1 = true;
			for (Object[] row : result) {
				Integer id = Integer.valueOf((String) row[0]);
				String name = (String) row[1];
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass2 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass2 = false;
					}
				}
			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("array test failed");
		}
	}

	/*
	 * @testName: arrayIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:740
	 *
	 * @test_Strategy: Create a CriteriaBuilder array and pass that to the
	 * CriteriaBuilder.array() method
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void arrayIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			Selection[] s = { customer.get("id"), customer.get("name") };

			logger.log(Logger.Level.INFO, "Testing tuple");
			try {
				qbuilder.array(qbuilder.tuple(s));
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

			logger.log(Logger.Level.INFO, "Testing array");
			try {
				qbuilder.array(qbuilder.array(s));
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

		if (!pass1 || !pass2) {
			throw new Exception("arrayIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: constructIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:753
	 *
	 * @test_Strategy: Create a CriteriaBuilder array and pass that to the
	 * CriteriaBuilder.construct(...) method
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void constructIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			Selection[] s = { customer.get("id"), customer.get("name") };

			logger.log(Logger.Level.INFO, "Testing tuple");
			try {
				qbuilder.construct(Customer.class, qbuilder.tuple(s));
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

			logger.log(Logger.Level.INFO, "Testing array");
			try {
				qbuilder.construct(Customer.class, qbuilder.array(s));
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

		if (!pass1 || !pass2) {
			throw new Exception("constructIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: countDistinct
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:755
	 *
	 * @test_Strategy: Select DISTINCT Count(c.home.city) from Customer c
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void countDistinct() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);
			logger.log(Logger.Level.TRACE, "count number of orders by customer");

			cquery.select(cbuilder.countDistinct(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("city", String.class))));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);

			Long countResult = tq.getSingleResult();
			Long expectedCount = 16L;

			if (countResult.equals(expectedCount)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "count test returned:" + countResult + "expected: " + expectedCount);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("countDistinct test failed");
		}
	}

	/*
	 * @testName: andPredicates
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:737; PERSISTENCE:SPEC:1729;
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.street = '125 Moxy
	 * Lane' AND c.home.city = 'Swansea' AND c.home.state = 'MA and c.home.zip =
	 * '11345'
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void andPredicates() throws Exception {
		boolean pass = false;

		Customer expectedCustomer = customerRef[2];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.and(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)), "125 Moxy Lane"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), "Swansea"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), "MA"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)), "11345")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			Customer result = tq.getSingleResult();

			if (result.equals(expectedCustomer)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expectedCustomer + ", actual:" + result);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("andPredicates test failed");

		}
	}

	/*
	 * @testName: orPredicates
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:834; PERSISTENCE:SPEC:1729;
	 *
	 * @test_Strategy: SELECT Distinct c from Customer c WHERE c.home.street = '47
	 * Skyline Drive' OR c.home.city = 'Chelmsford' OR c.home.state = 'VT' OR
	 * c.home.zip = '02155'
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void orPredicates() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[9].getId();
		expected[2] = customerRef[10].getId();
		expected[3] = customerRef[12].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)), "47 Skyline Drive"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), "Chelmsford"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), "VT"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)), "02155")));
			cquery.distinct(true);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("orPredicates test failed");

		}
	}

	/*
	 * @testName: isNull
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:789; PERSISTENCE:JAVADOC:957;
	 * PERSISTENCE:SPEC:1683; PERSISTENCE:SPEC:1728.2;
	 * 
	 * @test_Strategy: Select c FROM Customer c where c.name is null
	 *
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void isNull() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[11].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(customer);

			cquery.where(cbuilder.isNull(customer.get(Customer_.getSingularAttribute("name", String.class))));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("isNull test failed");

		}
	}

	/*
	 * @testName: isNotNull
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:788; PERSISTENCE:JAVADOC:956;
	 * PERSISTENCE:SPEC:1728.3;
	 *
	 * @test_Strategy: Select c FROM Customer c where c.work.zip IS NOT NULL
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void isNotNull() throws Exception {
		boolean pass = false;

		String[] expected = new String[17];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if (i != 12) {

				expected[j++] = customerRef[i].getId();
			}
		}

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cb.createQuery(Customer.class);

		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = mm.entity(Customer.class);
			EntityType<Address> Address_ = mm.entity(Address.class);
			cquery.where(cb.isNotNull(customer.get(Customer_.getSingularAttribute("work", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class))));

			cquery.select(customer);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("isNotNull test failed");

		}

	}

	/*
	 * @testName: parameter
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:836; PERSISTENCE:SPEC:1750;
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.street = :street OR
	 * c.home.city = :city OR c.home.state = :state OR c.home.zip = :zip
	 *
	 * where :street = '47 Skyline Drive' :city ='Chelmsford' :state ='VT' :zip =
	 * '02155'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameter() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[9].getId();
		expected[2] = customerRef[10].getId();
		expected[3] = customerRef[12].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			ParameterExpression<String> param1 = cbuilder.parameter(String.class, "streetParam");
			ParameterExpression<String> param2 = cbuilder.parameter(String.class, "cityParam");
			ParameterExpression<String> param3 = cbuilder.parameter(String.class, "stateParam");
			ParameterExpression<String> param4 = cbuilder.parameter(String.class, "zipParam");

			cquery.select(customer);
			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)), param1),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), param2),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), param3),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)), param4)));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			tq.setParameter("streetParam", "47 Skyline Drive");
			tq.setParameter("cityParam", "Chelmsford");
			tq.setParameter("stateParam", "VT");
			tq.setParameter("zipParam", "02155");

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("parameter test failed");

		}
	}

	/*
	 * @testName: parameterCaseSensitiveTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1306; PERSISTENCE:SPEC:1307
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.state = :state OR
	 * c.home.state = :STATE
	 *
	 * where :state ='RI' :STATE ='VT'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameterCaseSensitiveTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = customerRef[9].getId();
		expected[1] = customerRef[13].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			ParameterExpression<String> param1 = cbuilder.parameter(String.class, "stateParam");
			ParameterExpression<String> param2 = cbuilder.parameter(String.class, "STATEPARAM");

			cquery.select(customer);
			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), param1),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), param2)));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			tq.setParameter("stateParam", "RI");
			tq.setParameter("STATEPARAM", "VT");

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("parameterCaseSensitiveTest test failed");

		}
	}

	/*
	 * @testName: criteriaBuilderValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:898
	 *
	 * @test_Strategy: SELECT c.id from Customer c WHERE c.home.state IN (?1, ?2)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderValuesTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[12];

		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[6].getId();
		expected[5] = customerRef[7].getId();
		expected[6] = customerRef[8].getId();
		expected[7] = customerRef[9].getId();
		expected[8] = customerRef[10].getId();
		expected[9] = customerRef[12].getId();
		expected[10] = customerRef[14].getId();
		expected[11] = customerRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);
			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			CriteriaBuilder.In inExp = cbuilder.in(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("state", String.class)));
			inExp.value(cbuilder.parameter(String.class, "state1"));
			inExp.value(cbuilder.parameter(String.class, "state2"));
			cquery.where(inExp);

			List<Customer> result = getEntityManager().createQuery(cquery).setParameter("state1", "MA")
					.setParameter("state2", "VT").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + c.getId() + ", state:" + c.getHome().getState());
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("criteriaBuilderValuesTest failed");

		}
	}

	/*
	 * @testName: criteriaBuilderIn1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:780; PERSISTENCE:JAVADOC:898;
	 * PERSISTENCE:JAVADOC:899; PERSISTENCE:SPEC:1728.4;
	 *
	 * @test_Strategy: SELECT c.id from Customer c WHERE c.home.state IN (?1, ?2)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderIn1Test() throws Exception {
		boolean pass = false;

		String[] expected = new String[12];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[6].getId();
		expected[5] = customerRef[7].getId();
		expected[6] = customerRef[8].getId();
		expected[7] = customerRef[9].getId();
		expected[8] = customerRef[10].getId();
		expected[9] = customerRef[12].getId();
		expected[10] = customerRef[14].getId();
		expected[11] = customerRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);
			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			CriteriaBuilder.In inExp = cbuilder.in(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("state", String.class)));
			inExp.value(cbuilder.parameter(String.class, "state1"));
			inExp.value(cbuilder.parameter(String.class, "state2"));

			cquery.where(inExp);

			List<Customer> result = getEntityManager().createQuery(cquery).setParameter("state1", "MA")
					.setParameter("state2", "VT").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + c.getId() + ", state:" + c.getHome().getState());
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("criteriaBuilderIn1Test failed");

		}
	}

	/*
	 * @testName: criteriaBuilderIn2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:900; PERSISTENCE:JAVADOC:901;
	 * PERSISTENCE:JAVADOC:902; PERSISTENCE:SPEC:1698; PERSISTENCE:SPEC:1786;
	 * PERSISTENCE:SPEC:1786.3;
	 * 
	 * @test_Strategy: Testing not, getOperator and isNegated SELECT s.id from
	 * Spouse s WHERE s.id NOT IN (2,3)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderIn2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		String[] expected = new String[4];
		expected[0] = spouse[0].getId();
		expected[1] = spouse[3].getId();
		expected[2] = spouse[4].getId();
		expected[3] = spouse[5].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			CriteriaBuilder.In in = cbuilder.in(spouse.get("id"));
			for (String id : new String[] { "2", "3" }) {
				in.value(id);
			}
			Predicate pred = in.not();
			cquery.where(pred);

			if (!pred.getOperator().equals(Predicate.BooleanOperator.AND)) {
				logger.log(Logger.Level.ERROR,
						"Expected: " + Predicate.BooleanOperator.AND + ", actual:" + pred.getOperator().name());
			} else {
				pass1 = true;
			}
			if (in.not().isNegated() != true) {
				logger.log(Logger.Level.ERROR,
						"Expected in.not().isNegated() to return: true, actual:" + in.isNegated());
			} else {
				pass2 = true;
			}

			List<Spouse> result = getEntityManager().createQuery(cquery).getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("criteriaBuilderIn2Test failed");

		}
	}

	/*
	 * @testName: criteriaBuilderInValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:897
	 *
	 * @test_Strategy: SELECT c.id from Customer c WHERE c.home.state IN (MA, VT)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderInValueTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[12];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[6].getId();
		expected[5] = customerRef[7].getId();
		expected[6] = customerRef[8].getId();
		expected[7] = customerRef[9].getId();
		expected[8] = customerRef[10].getId();
		expected[9] = customerRef[12].getId();
		expected[10] = customerRef[14].getId();
		expected[11] = customerRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);
			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			CriteriaBuilder.In inExp = cbuilder.in(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("state", String.class)));
			inExp.value("MA");
			inExp.value("VT");
			cquery.where(inExp);

			List<Customer> result = getEntityManager().createQuery(cquery).getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + c.getId() + ", state:" + c.getHome().getState());
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("criteriaBuilderInValueTest failed");

		}
	}

	/*
	 * @testName: expressionInObjectArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:952
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2,3)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInObjectArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = spouse[1].getId();
		expected[1] = spouse[2].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			Expression exp = spouse.get("id");

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(exp.in(new Object[] { "2", "3" }));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
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
			throw new Exception("expressionInObjectArrayTest failed");

		}
	}

	/*
	 * @testName: expressionInExpressionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:953
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2,3)
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInExpressionArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = spouse[1].getId();
		expected[1] = spouse[2].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			Expression exp = spouse.get("id");

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(exp.in(new Expression[] { cbuilder.literal("2"), cbuilder.literal("3") }));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
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
			throw new Exception("expressionInExpressionArrayTest failed");

		}
	}

	/*
	 * @testName: expressionInExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:955
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInExpressionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = spouse[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);
			Expression exp = spouse.get("id");

			ParameterExpression<String> param = cbuilder.parameter(String.class);
			Expression e = cbuilder.literal("2");
			cquery.where(exp.in(e));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
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
			throw new Exception("expressionInExpressionTest failed");

		}
	}

	/*
	 * @testName: expressionInCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:954
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2,3)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInCollectionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = spouse[1].getId();
		expected[1] = spouse[2].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			Expression exp = spouse.get("id");
			ParameterExpression<String> param = cbuilder.parameter(String.class);
			Collection<String> col = new ArrayList<String>();
			col.add("2");
			col.add("3");

			cquery.where(exp.in(col));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
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
			throw new Exception("expressionInCollectionTest failed");

		}
	}

	/*
	 * @testName: parameterExpressionIsNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1091
	 *
	 * @test_Strategy: SELECT s.id from Spouse s where (2 IS NULL)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameterExpressionIsNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[0];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			cquery.from(Spouse.class);

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(param.isNull());
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.setParameter(param, "2").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
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
			throw new Exception("parameterExpressionIsNullTest failed");

		}
	}

	/*
	 * @testName: parameterExpressionIsNotNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1090
	 *
	 * @test_Strategy: SELECT s.id from Spouse s where (2 IS NOT NULL)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameterExpressionIsNotNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[6];
		expected[0] = spouse[0].getId();
		expected[1] = spouse[1].getId();
		expected[2] = spouse[2].getId();
		expected[3] = spouse[3].getId();
		expected[4] = spouse[4].getId();
		expected[5] = spouse[5].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			cquery.from(Spouse.class);

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(param.isNotNull());
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.setParameter(param, "2").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
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
			throw new Exception("parameterExpressionIsNotNullTest failed");

		}
	}

	/*
	 * @testName: likeExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:801
	 *
	 * @test_Strategy: SELECT Distinct c from Customer c WHERE c.home.zip LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void likeExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.like(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%77"));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("likeExpStringTest failed");

		}
	}

	/*
	 * @testName: likeExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:800
	 *
	 * @test_Strategy: SELECT Distinct c from Customer c WHERE c.home.zip LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void likeExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.like(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), cbuilder.literal("%77")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("likeExpExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:825
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[17];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if (i != 1) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%77"));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("notLikeExpStringTest failed");

		}
	}

	/*
	 * @testName: notLikeExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:824
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[17];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if (i != 1) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), cbuilder.literal("%77")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("notLikeExpExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:826
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(
					customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)),
					cbuilder.literal("%_7"), cbuilder.literal('\\')));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("notLikeExpExpExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpExpCharTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:827
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpExpCharTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), cbuilder.literal("%_7"), '\\'));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("notLikeExpExpCharTest failed");

		}
	}

	/*
	 * @testName: notLikeExpStringExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:828
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpStringExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%_7", cbuilder.literal('\\')));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("notLikeExpStringExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpStringCharTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:829
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpStringCharTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%_7", '\\'));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("notLikeExpStringCharTest failed");

		}
	}

	/*
	 * @testName: concatExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:749; PERSISTENCE:JAVADOC:806
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.work.street="1 Network"
	 * CONCAT " Drive"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void concatExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);
			cquery.select(customer);

			cquery.where(cbuilder.equal(
					customer.get(Customer_.getSingularAttribute("work", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)),
					cbuilder.concat(cbuilder.literal("1 Network"), " Drive")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("concatExpStringTest failed");

		}
	}

	/*
	 * @testName: concatStringExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:750
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.work.street="1 Network"
	 * CONCAT " Drive"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void concatStringExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);
			cquery.select(customer);

			cquery.where(cbuilder.equal(
					customer.get(Customer_.getSingularAttribute("work", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)),
					cbuilder.concat("1 Network", cbuilder.literal(" Drive"))));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("concatStringExpTest failed");

		}
	}

	/*
	 * @testName: concatExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:748
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.work.street="1 Network"
	 * CONCAT " Drive"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void concatExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Address.class);
			cquery.select(customer);

			cquery.where(cbuilder.equal(
					customer.get(Customer_.getSingularAttribute("work", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)),
					cbuilder.concat(cbuilder.literal("1 Network"), cbuilder.literal(" Drive"))));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
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
			throw new Exception("concatExpExpTest failed");

		}
	}

	/*
	 * @testName: trimBothExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:867
	 *
	 * @test_Strategy: Select trim(both from c.name) from Customer c where c.name= '
	 * David R. Vincent'
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void trimBothExpTest() throws Exception {
		boolean pass = false;
		final String expectedResult = "David R. Vincent";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Customer> cust = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = cust.getModel();

			cquery.where(cb.equal(cust.get(Customer_.getSingularAttribute("name", String.class)),
					cb.literal(" David R. Vincent")));
			cquery.select(cb.trim(Trimspec.BOTH, cust.get(Customer_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Received expected result:|" + result + "|");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Mismatch in received results - expected = |" + expectedResult
						+ "|, received = |" + result + "|");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("trimBothExpTest failed");

		}
	}

	/*
	 * @testName: lower
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:812
	 *
	 * @test_Strategy: Select lower(c.name) From Customer c a WHERE c.name = 'Lisa
	 * M. Presley'
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void lower() throws Exception {
		final String expectedResult = "lisa m. presley";
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Customer> cust = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = cust.getModel();

			cquery.where(cb.equal(cust.get(Customer_.getSingularAttribute("name", String.class)),
					cb.literal("Lisa M. Presley")));
			cquery.select(cb.lower(cust.get(Customer_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Mismatch in received results - expected = " + expectedResult + " received = " + result);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lower test failed");

		}

	}

	/*
	 * @testName: nullifExpressionExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:830
	 *
	 * @test_Strategy: SELECT c.ID, NULLIF(LCASE(c.home.city), LCASE(c.work.city))
	 * FROM CUSTOMER c WHERE (((c.home.city IS NOT NULL) AND (c.work.city IS NOT
	 * NULL)) ORDER BY c.ID ASC
	 *
	 *
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void nullifExpressionExpressionTest() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		for (Customer c : customerRef) {
			// logger.log(Logger.Level.TRACE,"Expected Data:"+c.toString());
			String id = c.getId();
			if (Integer.parseInt(id) <= 18 && Integer.parseInt(id) != 9) {
				if (c.getHome().getCity().equals(c.getWork().getCity())) {
					expected.add(c.getId() + ",null");
				} else {
					expected.add(c.getId() + "," + c.getHome().getCity().toLowerCase());
				}
			}
		}

		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Tuple> cquery = cbuilder.createQuery(Tuple.class);
		if (cquery != null)

		{
			Root<Customer> customer = cquery.from(Customer.class);

			Expression<String> expHomeCity = cbuilder.lower(customer.get("home").<String>get("city"));
			Expression<String> expWorkCity = cbuilder.lower(customer.get("work").<String>get("city"));

			cquery.multiselect(customer.get("id"), cbuilder.nullif(expHomeCity, expWorkCity));

			cquery.where(cbuilder.and(cbuilder.isNotNull(customer.get("home").<String>get("city")),
					cbuilder.isNotNull(customer.get("work").<String>get("city"))));
			cquery.orderBy(cbuilder.asc(customer.get("id")));

			List<Tuple> result = getEntityManager().createQuery(cquery).getResultList();

			for (Tuple t : result) {
				logger.log(Logger.Level.TRACE, "actual:" + t.toString());

				String id = (String) t.get(0);
				String city = (String) t.get(1);
				if (city != null) {
					actual.add(id + "," + city);
				} else {
					actual.add(id + ",null");
				}
			}

			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "actual:" + s);
			}
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
		} else

		{
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("nullifExpressionExpressionTest failed");
		}

	}

	/*
	 * @testName: nullifExpressionObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:831
	 *
	 * @test_Strategy: SELECT c.ID, NULLIF(LCASE(c.home.city), "burlington") FROM
	 * CUSTOMER c WHERE ((c.home.city IS NOT NULL) ORDER BY c.ID ASC
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void nullifExpressionObjectTest() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		for (Customer c : customerRef) {
			// logger.log(Logger.Level.TRACE,"Expected Data:"+c.toString());
			String id = c.getId();
			if (Integer.parseInt(id) <= 18 && Integer.parseInt(id) != 9) {
				if (c.getHome().getCity().equals(c.getWork().getCity())) {
					expected.add(c.getId() + ",null");
				} else {
					expected.add(c.getId() + "," + c.getHome().getCity().toLowerCase());
				}
			}
		}

		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Tuple> cquery = cbuilder.createQuery(Tuple.class);
		if (cquery != null)

		{
			Root<Customer> customer = cquery.from(Customer.class);

			Expression<String> expHomeCity = cbuilder.lower(customer.get("home").<String>get("city"));

			cquery.multiselect(customer.get("id"), cbuilder.nullif(expHomeCity, "burlington"));

			cquery.where(cbuilder.isNotNull(customer.get("home").<String>get("city")));

			cquery.orderBy(cbuilder.asc(customer.get("id")));

			List<Tuple> result = getEntityManager().createQuery(cquery).getResultList();

			for (Tuple t : result) {
				logger.log(Logger.Level.TRACE, "actual:" + t.toString());

				String id = (String) t.get(0);
				String city = (String) t.get(1);
				if (city != null) {
					actual.add(id + "," + city);
				} else {
					actual.add(id + ",null");
				}
			}

			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "actual:" + s);
			}
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
		} else

		{
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("nullifExpressionObjectTest failed");
		}

	}

	/*
	 * @testName: selectMultiSelectTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1707; PERSISTENCE:SPEC:1708;
	 * PERSISTENCE:SPEC:1709;
	 * 
	 * @test_Strategy: Execute a query and verify a single result can be returned
	 * when using cbuilder.createQuery() Execute a query and verify a multiple
	 * results can be returned when using cbuilder.createQuery()
	 *
	 * Select c.id, c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void selectMultiSelectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		logger.log(Logger.Level.INFO, "Testing select");

		try {

			getEntityTransaction().begin();
			CriteriaQuery cquery = cbuilder.createQuery();
			if (cquery != null) {
				Root<Customer> customer = cquery.from(Customer.class);

				// Get Metamodel from Root
				EntityType<Customer> Customer_ = customer.getModel();

				cquery.select(customer.get(Customer_.getSingularAttribute("id", String.class)));

				Query q = getEntityManager().createQuery(cquery);

				List result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();

				for (Object row : result) {
					Integer id = Integer.valueOf((String) row);
					logger.log(Logger.Level.TRACE, "id=" + id);
					actual.add(id);
				}
				Collections.sort(actual);

				if (!checkEntityPK(actual, expected)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
							+ " references, got: " + actual.size());
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass1 = true;
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}
		logger.log(Logger.Level.INFO, "Testing multiselect");
		try {

			getEntityTransaction().begin();
			CriteriaQuery cquery = cbuilder.createQuery();
			if (cquery != null) {
				Root<Customer> customer = cquery.from(Customer.class);

				// Get Metamodel from Root
				EntityType<Customer> Customer_ = customer.getModel();

				cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
						customer.get(Customer_.getSingularAttribute("name", String.class)));

				Query q = getEntityManager().createQuery(cquery);

				List<Object[]> result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();

				for (Object[] row : result) {
					Integer id = Integer.valueOf((String) row[0]);
					String name = (String) row[1];
					logger.log(Logger.Level.TRACE, "id=" + id);
					logger.log(Logger.Level.TRACE, "name=" + name);
					// some of the names have nulls so deal with them
					if (name != null && customerRef[id - 1].getName() != null) {
						if (customerRef[id - 1].getName().equals(name)) {
							actual.add(id);
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						}
					} else if (name == null && customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
					}
				}
				Collections.sort(actual);

				if (!checkEntityPK(actual, expected)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
							+ " references, got: " + actual.size());
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("selectMultiSelectTest test failed");
		}
	}

	/*
	 * @testName: multiRootTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1711
	 * 
	 * @test_Strategy: Test that using mulitple roots results in a cartesian product
	 *
	 * Select s1, s2 from Spouse s1, Spouse s2
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void multiRootTest() throws Exception {
		boolean pass = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= spouse.length; i++) {
			for (int j = 1; j <= spouse.length; j++) {
				list.add(Integer.toString(i) + "," + Integer.toString(j));
			}
		}

		try {

			getEntityTransaction().begin();
			CriteriaQuery cquery = cbuilder.createQuery();
			if (cquery != null) {
				Root<Spouse> spouse1 = cquery.from(Spouse.class);
				Root<Spouse> spouse2 = cquery.from(Spouse.class);

				// Get Metamodel from Root
				EntityType<Spouse> Spouse1_ = spouse1.getModel();
				EntityType<Spouse> Spouse2_ = spouse2.getModel();

				cquery.multiselect(spouse1.get(Spouse1_.getSingularAttribute("id", String.class)),
						spouse2.get(Spouse2_.getSingularAttribute("id", String.class)));

				Query q = getEntityManager().createQuery(cquery);

				List<Object[]> result = q.getResultList();

				for (Object[] row : result) {
					Integer sId1 = Integer.valueOf((String) row[0]);
					Integer sId2 = Integer.valueOf((String) row[1]);
					logger.log(Logger.Level.TRACE, "sId1=" + sId1 + ", sId2=" + sId2);
					list.remove(sId1 + "," + sId2);
				}
				logger.log(Logger.Level.TRACE, "Size:" + list.size());
				if (list.isEmpty()) {
					logger.log(Logger.Level.TRACE, "All PK's were found");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Not all PK's were returned");
					for (String s : list) {
						logger.log(Logger.Level.ERROR, "Not all PK's were returned:" + s);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass) {
			throw new Exception("multiRootTest test failed");
		}
	}

}
