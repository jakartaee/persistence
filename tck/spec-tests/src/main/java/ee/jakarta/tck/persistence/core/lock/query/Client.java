/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.lock.query;

import java.lang.System.Logger;
import java.sql.Date;
import java.util.Collection;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private final Date d1 = getSQLDate("2000-02-14");

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Department", pkgName + "Employee", pkgName + "Insurance" };
		return createDeploymentJar("jpa_core_lock_query.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			logger.log(Logger.Level.TRACE, "Create Test Data");
			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * @testName: getResultListTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:413; PERSISTENCE:JAVADOC:402;
	 * PERSISTENCE:JAVADOC:329; PERSISTENCE:JAVADOC:639; PERSISTENCE:JAVADOC:443;
	 * PERSISTENCE:JAVADOC:687; PERSISTENCE:SPEC:1507;
	 * 
	 * @test_Strategy: 1. Create a Query 2. Obtain Entity Manager 3. Select
	 * Employees with id <10 with lock mode set to PESSIMISTIC_READ 4. Find Employee
	 * 1 with lock mode set to PESSIMISTIC_WRITE 5. Update Employee 1 6. Create a
	 * TypedQuery and do the same above
	 *
	 */
	@Test
	public void getResultListTest1() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin getResultListTest1");
		try {
			logger.log(Logger.Level.TRACE, "Testing Query version");

			getEntityTransaction().begin();
			EntityManager em = getEntityManager();
			// Apply pessimistic locking and wait

			Query query = em.createQuery("select e from Employee e where e.id < 10 ");
			LockModeType lm = query.getLockMode();
			if (lm == null || lm.equals(LockModeType.NONE)) {
				if (lm == null) {
					logger.log(Logger.Level.TRACE, "Received null when no LockModeType had been set");
				} else {
					logger.log(Logger.Level.TRACE,
							"Received " + lm.toString() + " when no LockModeType had been specifically set");

				}
				query.setLockMode(LockModeType.PESSIMISTIC_READ);
				lm = query.getLockMode();
				if (lm.equals(LockModeType.PESSIMISTIC_READ)) {
					logger.log(Logger.Level.TRACE, "Received LockModeType:" + lm.name());
				} else if (lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
					logger.log(Logger.Level.TRACE,
							"Received LockModeType:" + lm + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
				} else {
					logger.log(Logger.Level.ERROR, "Expected LockModeType:" + LockModeType.PESSIMISTIC_READ.name()
							+ " or " + LockModeType.PESSIMISTIC_WRITE.name() + ", Actual:" + lm);
					pass = false;
				}

				Collection<Employee> c = query.getResultList();

				for (Employee e : c) {
					lm = em.getLockMode(e);
					if (lm.equals(LockModeType.PESSIMISTIC_READ)) {
						logger.log(Logger.Level.TRACE, "Received LockModeType:" + lm.name());
					} else if (lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
						logger.log(Logger.Level.TRACE,
								"Received LockModeType:" + lm + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
					} else {
						logger.log(Logger.Level.ERROR, "LockMoteType for the Employee[" + e.getId()
								+ "] was wrong - Expected:" + LockModeType.PESSIMISTIC_READ.name() + ", Actual:" + lm);
						pass = false;
					}
				}

				// Try to change the contents of Employee Entity (in locked state)
				Employee employeeFound = getEntityManager().find(Employee.class, 1, LockModeType.PESSIMISTIC_WRITE);
				employeeFound.setSalary(90000F);
			} else {
				logger.log(Logger.Level.ERROR, "Expected null when no LockModeType had been set, Actual:" + lm);
				pass = false;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
				pass = false;
			}
		}
		logger.log(Logger.Level.TRACE, "Testing TypedQuery version");

		try {

			getEntityTransaction().begin();
			EntityManager em = getEntityManager();
			// Apply pessimistic locking and wait

			TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.id < 10 ", Employee.class);
			LockModeType lm = query.getLockMode();
			if (lm == null || lm.equals(LockModeType.NONE)) {
				if (lm == null) {
					logger.log(Logger.Level.TRACE, "Received null when no LockModeType had been set");
				} else {
					logger.log(Logger.Level.TRACE,
							"Received " + lm.toString() + " when no LockModeType had been specifically set");

				}
				query.setLockMode(LockModeType.PESSIMISTIC_READ);
				lm = query.getLockMode();
				if (lm.equals(LockModeType.PESSIMISTIC_READ)) {
					logger.log(Logger.Level.TRACE, "Received LockModeType:" + lm.name());
				} else if (lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
					logger.log(Logger.Level.TRACE,
							"Received LockModeType:" + lm + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
				} else {
					logger.log(Logger.Level.ERROR, "Expected LockModeType:" + LockModeType.PESSIMISTIC_READ.name()
							+ " or " + LockModeType.PESSIMISTIC_WRITE.name() + ", Actual:" + lm);
					pass = false;
				}
				query.setLockMode(LockModeType.PESSIMISTIC_READ);
				lm = query.getLockMode();
				if (lm.equals(LockModeType.PESSIMISTIC_READ)) {
					logger.log(Logger.Level.TRACE, "Received LockModeType:" + lm.name());
				} else if (lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
					logger.log(Logger.Level.TRACE,
							"Received LockModeType:" + lm + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
				} else {
					logger.log(Logger.Level.ERROR, "Expected LockModeType:" + LockModeType.PESSIMISTIC_READ.name()
							+ " or " + LockModeType.PESSIMISTIC_WRITE.name() + ", Actual:" + lm);
					pass = false;
				}
				Collection<Employee> c = query.getResultList();

				for (Employee e : c) {
					lm = em.getLockMode(e);
					if (lm.equals(LockModeType.PESSIMISTIC_READ)) {
						logger.log(Logger.Level.TRACE, "Received LockModeType:" + lm.name());
					} else if (lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
						logger.log(Logger.Level.TRACE,
								"Received LockModeType:" + lm + " inplace of " + LockModeType.PESSIMISTIC_READ.name());
					} else {
						logger.log(Logger.Level.ERROR, "LockMoteType for the Employee[" + e.getId()
								+ "] was wrong - Expected:" + LockModeType.PESSIMISTIC_READ.name() + ", Actual:" + lm);
						pass = false;
					}
				}

				// Try to change the contents of Employee Entity (in locked state)
				Employee employeeFound = getEntityManager().find(Employee.class, 1, LockModeType.PESSIMISTIC_WRITE);
				employeeFound.setSalary(90000F);
			} else {
				logger.log(Logger.Level.ERROR, "query.getLockMode() returned a null instead of " + LockModeType.NONE
						+ ", when no LockModeType had been set");
				pass = false;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("getResultListTest1 failed");
		}
	}

	/*
	 * @testName: getLockModeNONSELECTIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:555;
	 * 
	 * @test_Strategy: Get the LockModeType of Non Select JPQL query, a
	 * IllegalStateException should be thrown
	 *
	 */
	@Test
	public void getLockModeNONSELECTIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing Query");
			getEntityManager().createQuery("UPDATE Employee e SET e.salary = e.salary * 10.0").getLockMode();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
			getEntityTransaction().commit();
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in while rolling back TX:", re);
			}
		}
		if (!pass) {
			throw new Exception("getLockModeNONSELECTIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: getLockModeObjectIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:329
	 * 
	 * @test_Strategy: Get the LockModeType of an entity that has not been
	 * persisted.
	 *
	 *
	 */
	@Test
	public void getLockModeObjectIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		logger.log(Logger.Level.TRACE, "Begin getLockModeObjectIllegalArgumentExceptionTest");
		try {

			getEntityTransaction().begin();
			EntityManager em = getEntityManager();

			Employee e = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);

			try {
				em.getLockMode(e);
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("getLockModeObjectIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getLockModeObjectTransactionRequiredException1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:329; PERSISTENCE:JAVADOC:483
	 * 
	 * @test_Strategy: Get an entity, commit the transaction then try to access the
	 * LockModeType and TransactionRequiredException should be thrown
	 *
	 *
	 */
	@Test
	public void getLockModeObjectTransactionRequiredException1Test() throws Exception {
		boolean pass = false;

		int expected = 9;
		try {

			getEntityTransaction().begin();
			EntityManager em = getEntityManager();
			// Apply pessimistic locking and wait

			Query query = em.createQuery("select e from Employee e where e.id < 10 ");

			query.setLockMode(LockModeType.PESSIMISTIC_READ);
			Collection<Employee> c = query.getResultList();
			getEntityTransaction().commit();
			logger.log(Logger.Level.TRACE, "isActive=" + getEntityTransaction().isActive());
			int found = 0;
			for (Employee e : c) {
				try {
					em.getLockMode(e);
					logger.log(Logger.Level.ERROR, "Did not get TransactionRequiredException for employee:" + e);
				} catch (TransactionRequiredException tre) {
					found++;
				}
			}
			if (found == expected) {
				logger.log(Logger.Level.TRACE, "Got expected number of TransactionRequiredExceptions:" + expected);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Number of TransactionRequiredException Expected:" + c.size() + ", Actual:" + found);
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("getLockModeObjectTransactionRequiredException1Test failed");
		}
	}

	/*
	 * @testName: getLockModeObjectIllegalArgumentException1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:484
	 * 
	 * @test_Strategy: Get an entity, detached the entity and then try to access the
	 * LockModeType and TransactionRequiredException should be thrown
	 *
	 *
	 */
	@Test
	public void getLockModeObjectIllegalArgumentException1Test() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			EntityManager em = getEntityManager();
			// Apply pessimistic locking and wait

			Query query = em.createQuery("select e from Employee e where e.id < 10 ");

			logger.log(Logger.Level.TRACE, "Setting lock mode to PESSIMISTIC_READ");
			query.setLockMode(LockModeType.PESSIMISTIC_READ);
			Collection<Employee> c = query.getResultList();
			int found = 0;
			for (Employee e : c) {
				try {
					logger.log(Logger.Level.TRACE, "Detaching Employee:" + e.getId());
					em.detach(e);
					logger.log(Logger.Level.TRACE, "Calling getLockMode()");
					em.getLockMode(e);
				} catch (IllegalArgumentException iae) {
					found++;
				}
			}
			if (found == c.size()) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Number of IllegalArgumentException Expected:" + c.size() + ", Actual:" + found);
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("getLockModeObjectIllegalArgumentException1Test failed");
		}
	}

	/*
	 * @testName: setLockModeIllegalStateException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:587; PERSISTENCE:JAVADOC:620;
	 * 
	 * @test_Strategy: Set the LockModeType of a NativeQuery, an
	 * IllegalStateException should be thrown Set the LockModeType of Non Select
	 * JPQL Query, an IllegalStateException should be thrown
	 *
	 */
	@Test
	public void setLockModeIllegalStateException() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Testing Native version");
		try {
			Query nativeQuery = getEntityManager().createNativeQuery("Select ID from DEPARTMENT where ID=1");
			nativeQuery.setLockMode(LockModeType.PESSIMISTIC_READ);
			logger.log(Logger.Level.ERROR, "IllegalStateException should have been thrown after setLockMode");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "Got IllegalStateException as expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		}

		logger.log(Logger.Level.INFO, "Testing Query version");

		try {
			Query q = getEntityManager().createQuery("UPDATE Employee e SET e.salary = e.salary * 10.0");
			q.setLockMode(LockModeType.PESSIMISTIC_READ);
			logger.log(Logger.Level.ERROR, "IllegalStateException should have been thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "Got IllegalStateException as expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("setLockModeIllegalStateException failed");
		}
	}

	/*
	 * @testName: getResultListTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:573; PERSISTENCE:JAVADOC:2701;
	 * 
	 * @test_Strategy:
	 *
	 * 1. Create an EntityManager 2. Execute a query setting the lock mode to
	 * PEsSIMISTIC_READ without starting a transaction 3. Validate a
	 * TransactionRequiredException is thrown
	 *
	 */
	@Test
	public void getResultListTest2() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Testing Query version");

		try {
			logger.log(Logger.Level.TRACE, "Invoking query for getResultListTest2");
			Query query = getEntityManager()
					.createQuery("select e.department from Employee e where e.id < 10 order by e.department.id")
					.setFirstResult(3);
			query.setLockMode(LockModeType.PESSIMISTIC_READ);
			query.getResultList();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
			pass = false;
		} catch (TransactionRequiredException e) {
			logger.log(Logger.Level.TRACE, "Got TransactionRequiredException as expected");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		logger.log(Logger.Level.TRACE, "Testing TypedQuery version");

		try {
			logger.log(Logger.Level.TRACE, "Invoking query for getResultListTest2");
			TypedQuery<Department> query = getEntityManager()
					.createQuery("select e.department from Employee e where e.id < 10 order by e.department.id",
							Department.class)
					.setFirstResult(3);
			query.setLockMode(LockModeType.PESSIMISTIC_READ);
			query.getResultList();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
			pass = false;
		} catch (TransactionRequiredException e) {
			logger.log(Logger.Level.TRACE, "Got TransactionRequiredException as expected");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("getResultListTest2 failed");
		}
	}

	/*
	 * @testName: getSingleResultTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:171; PERSISTENCE:JAVADOC:413;
	 * PERSISTENCE:JAVADOC:325; PERSISTENCE:SPEC:1508;
	 * 
	 * @test_Strategy:
	 * 
	 * 1. Obtain Entity Manager 2. get the name of Department 1 with lock mode set
	 * to PESSIMISTIC_READ 3. Find same Department with lock mode set to
	 * PESSIMISTIC_WRITE 4. Update Department
	 *
	 */
	@Test
	public void getSingleResultTest() throws Exception {
		boolean pass = false;

		logger.log(Logger.Level.TRACE, "Begin getSingleResultTest");
		try {

			getEntityTransaction().begin();

			// Apply pessimistic locking and wait
			Query query = getEntityManager().createQuery("select d.name from Department d where d.id = 1");
			query.setLockMode(LockModeType.PESSIMISTIC_READ);
			query.getSingleResult();

			// Try to change the contents of Department Entity (in locked state)
			Department departmentFound = getEntityManager().find(Department.class, 1, LockModeType.PESSIMISTIC_WRITE);
			departmentFound.setName("NewDepartment");

			getEntityTransaction().commit();
			pass = true;

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("getSingleResultTest failed");
		}
	}

	private void createTestData() throws Exception {
		logger.log(Logger.Level.TRACE, "createTestData");
		Employee empRef[] = new Employee[20];
		Department deptRef[] = new Department[10];
		Insurance insRef[] = new Insurance[5];

		final Date d2 = getSQLDate("2001-06-27");
		final Date d3 = getSQLDate("2002-07-07");
		final Date d4 = getSQLDate("2003-03-03");
		final Date d5 = getSQLDate("2004-04-10");
		final Date d6 = getSQLDate("2005-02-18");
		final Date d7 = getSQLDate("2000-09-17");
		final Date d8 = getSQLDate("2001-11-14");
		final Date d9 = getSQLDate("2002-10-04");
		final Date d10 = getSQLDate("2003-01-25");

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create 5 Departments");
			deptRef[0] = new Department(1, "Engineering");
			deptRef[1] = new Department(2, "Marketing");
			deptRef[2] = new Department(3, "Sales");
			deptRef[3] = new Department(4, "Accounting");
			deptRef[4] = new Department(5, "Training");

			logger.log(Logger.Level.TRACE, "Start to persist departments ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().persist(dept);
					doFlush();
					logger.log(Logger.Level.TRACE, "persisted department " + dept);
				}
			}

			logger.log(Logger.Level.TRACE, "Create 3 Insurance Carriers");
			insRef[0] = new Insurance(1, "Prudential");
			insRef[1] = new Insurance(2, "Cigna");
			insRef[2] = new Insurance(3, "Sentry");

			logger.log(Logger.Level.TRACE, "Start to persist insurance ");
			for (Insurance ins : insRef) {
				if (ins != null) {
					getEntityManager().persist(ins);
					doFlush();
					logger.log(Logger.Level.TRACE, "persisted insurance " + ins);
				}
			}

			logger.log(Logger.Level.TRACE, "Create 20 employees");
			empRef[0] = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef[0].setDepartment(deptRef[0]);
			empRef[0].setInsurance(insRef[0]);

			empRef[1] = new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0);
			empRef[1].setDepartment(deptRef[1]);
			empRef[1].setInsurance(insRef[1]);

			empRef[2] = new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0);
			empRef[2].setDepartment(deptRef[2]);
			empRef[2].setInsurance(insRef[2]);

			empRef[3] = new Employee(4, "Robert", "Bissett", d4, (float) 55000.0);
			empRef[3].setDepartment(deptRef[3]);
			empRef[3].setInsurance(insRef[0]);

			empRef[4] = new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0);
			empRef[4].setDepartment(deptRef[4]);
			empRef[4].setInsurance(insRef[1]);

			empRef[5] = new Employee(6, "Karen", "Tegan", d6, (float) 80000.0);
			empRef[5].setDepartment(deptRef[0]);
			empRef[5].setInsurance(insRef[2]);

			empRef[6] = new Employee(7, "Stephen", "Cruise", d7, (float) 90000.0);
			empRef[6].setDepartment(deptRef[1]);
			empRef[6].setInsurance(insRef[0]);

			empRef[7] = new Employee(8, "Irene", "Caruso", d8, (float) 20000.0);
			empRef[7].setDepartment(deptRef[2]);
			empRef[7].setInsurance(insRef[1]);

			empRef[8] = new Employee(9, "William", "Keaton", d9, (float) 35000.0);
			empRef[8].setDepartment(deptRef[3]);
			empRef[8].setInsurance(insRef[2]);

			empRef[9] = new Employee(10, "Kate", "Hudson", d10, (float) 20000.0);
			empRef[9].setDepartment(deptRef[4]);
			empRef[9].setInsurance(insRef[0]);

			empRef[10] = new Employee(11, "Jonathan", "Smith", d10, 40000.0F);
			empRef[10].setDepartment(deptRef[0]);
			empRef[10].setInsurance(insRef[1]);

			empRef[11] = new Employee(12, "Mary", "Macy", d9, 40000.0F);
			empRef[11].setDepartment(deptRef[1]);
			empRef[11].setInsurance(insRef[2]);

			empRef[12] = new Employee(13, "Cheng", "Fang", d8, 40000.0F);
			empRef[12].setDepartment(deptRef[2]);
			empRef[12].setInsurance(insRef[0]);

			empRef[13] = new Employee(14, "Julie", "OClaire", d7, 60000.0F);
			empRef[13].setDepartment(deptRef[3]);
			empRef[13].setInsurance(insRef[1]);

			empRef[14] = new Employee(15, "Steven", "Rich", d6, 60000.0F);
			empRef[14].setDepartment(deptRef[4]);
			empRef[14].setInsurance(insRef[2]);

			empRef[15] = new Employee(16, "Kellie", "Lee", d5, 60000.0F);
			empRef[15].setDepartment(deptRef[0]);
			empRef[15].setInsurance(insRef[0]);

			empRef[16] = new Employee(17, "Nicole", "Martin", d4, 60000.0F);
			empRef[16].setDepartment(deptRef[1]);
			empRef[16].setInsurance(insRef[1]);

			empRef[17] = new Employee(18, "Mark", "Francis", d3, 60000.0F);
			empRef[17].setDepartment(deptRef[2]);
			empRef[17].setInsurance(insRef[2]);

			empRef[18] = new Employee(19, "Will", "Forrest", d2, 60000.0F);
			empRef[18].setDepartment(deptRef[3]);
			empRef[18].setInsurance(insRef[0]);

			empRef[19] = new Employee(20, "Katy", "Hughes", d1, 60000.0F);
			empRef[19].setDepartment(deptRef[4]);
			empRef[19].setInsurance(insRef[1]);

			logger.log(Logger.Level.TRACE, "Start to persist employees ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					doFlush();
					logger.log(Logger.Level.TRACE, "persisted employee " + emp);
				}
			}
			getEntityTransaction().commit();

		} catch (Exception re) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData while rolling back TX:", re);
			}
		}

	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM INSURANCE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
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
