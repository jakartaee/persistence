/*
 * Copyright (c) 2018, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.mapkey;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Query;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long DEPT1_ID = 777l;

	private static final String DEPT1_NAME = "Software";

	private static final long EMP1_ID = 220;

	private static final String EMP1_CODE = "SW";

	private static final long EMP2_ID = 35l;

	private static final String EMP2_CODE = "ME";

	private static final long EMP3_ID = 36l;

	private static final String EMP3_CODE = "HW";

	private Employee employee1;

	private Employee employee2;

	private Employee employee3;

	private List<Employee> employeeList;

	private static final long COMPANY_ID = 676l;

	private static final String COMPANY_NAME = "Regal";

	private static final long LOCATION_ID = 42l;

	private static final String LOCATION_CODE = "KXTN";

	private static final long CITY_ID = 47l;

	private static final String CITY_CODE = "ICIO";

	private static final int CUSTOMER1_ID = 420;

	private static final String CUSTOMER1_NAME = "Craig";

	private static final int CUSTOMER2_ID = 640;

	private static final String CUSTOMER2_NAME = "Russell";

	private static final int STORE_ID = 78600;

	private static final String STORE_NAME = "Sun";

	private static final long ORDER1_ID = 786l;

	private static final long ORDER2_ID = 787l;

	private static final long ORDER3_ID = 788l;

	private static final long ORDER4_ID = 789l;

	private static final double COST1 = 100;

	private static final double COST2 = 105;

	private static final double COST3 = 110;

	private static final double COST4 = 115;

	private static final long CUST1_ID = 2l;

	private List<RetailOrder> consumer1Orders;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFile = { ORM_XML };
		String[] classes = { pkgName + "Consumer", pkgName + "Customers", pkgName + "Department", pkgName + "Employee",
				pkgName + "EmployeeComparator", pkgName + "RetailOrder", pkgName + "RetailOrderCostComparatorDESC",
				pkgName + "Store", pkgName + "TheatreCompany", pkgName + "TheatreLocation" };

		return createDeploymentJar("jpa_core_override_mapkey.jar", pkgNameWithoutSuffix, classes, xmlFile);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception:test failed ", e);
		}
	}

	/*
	 * @testName: testNoOrderByAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1100; PERSISTENCE:SPEC:1101;
	 * PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104; PERSISTENCE:SPEC:1106;
	 * PERSISTENCE:SPEC:1107; PERSISTENCE:SPEC:1108; PERSISTENCE:SPEC:1110;
	 * PERSISTENCE:SPEC:1268;
	 * 
	 * @test_Strategy: Department and Employee are two entities and are related by
	 * "Many-to-One" but is specified only in orm.xml. All of the annotations like
	 * "mappedBy" and "orderBy" are overriden in orm.xml.
	 */
	@Test
	public void testNoOrderByAnnotation() throws Exception {

		Department dept = createDepartment(DEPT1_ID, DEPT1_NAME);
		employee1 = createEmployee(EMP1_ID, EMP1_CODE);
		employee2 = createEmployee(EMP2_ID, EMP2_CODE);
		employee3 = createEmployee(EMP3_ID, EMP3_CODE);

		// Not adding employees in Order of id
		employeeList = new ArrayList<Employee>();
		employeeList.add(employee3);
		employeeList.add(employee2);
		employeeList.add(employee1);

		dept.setEmployees(employeeList);
		employee1.setDepartment(dept);
		employee2.setDepartment(dept);
		employee3.setDepartment(dept);

		getEntityTransaction().begin();
		getEntityManager().persist(dept);
		getEntityManager().flush();
		clearCache();
		getEntityTransaction().commit();
		clearCache();
		getEntityTransaction().begin();

		Collections.sort(employeeList, new EmployeeComparator());

		try {
			Query q = getEntityManager().createQuery("select d from Department d where d.id= :dept");
			q.setParameter("dept", DEPT1_ID);
			Department dept1 = (Department) q.getSingleResult();
			List<Employee> actualList = dept1.getEmployees();

			if (employeeList.equals(actualList)) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("The expected Employee List is not equal to the " + "actual List read from the DB");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOrderByAnnotation" + e);
		}
	}

	/*
	 * @testName: testOverrideMapKey
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1100; PERSISTENCE:SPEC:1101;
	 * PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104; PERSISTENCE:SPEC:1106;
	 * PERSISTENCE:SPEC:1107; PERSISTENCE:SPEC:1108; PERSISTENCE:SPEC:1110;
	 * PERSISTENCE:SPEC:1268;
	 * 
	 * @test_Strategy: TheatreCompany is an entity with "id" specified as MapKey
	 * using annotation. It is overriden by code as MapKey in orm.xml. The following
	 * test checks for the overriden value.
	 *
	 */
	@Test
	public void testOverrideMapKey() throws Exception {

		TheatreCompany regal = createTheatreCompany(COMPANY_ID, COMPANY_NAME);
		TheatreLocation knoxville = createTheatreLocation(LOCATION_ID, LOCATION_CODE);
		TheatreLocation iowacity = createTheatreLocation(CITY_ID, CITY_CODE);

		Map<String, TheatreLocation> regalLocations = new HashMap<String, TheatreLocation>();
		regalLocations.put(LOCATION_CODE, knoxville);
		regalLocations.put(CITY_CODE, iowacity);
		regal.setLocations(regalLocations);
		knoxville.addCompany(regal);
		iowacity.addCompany(regal);
		getEntityTransaction().begin();
		getEntityManager().persist(regal);
		getEntityManager().flush();
		clearCache();
		getEntityTransaction().commit();
		clearCache();
		getEntityTransaction().begin();
		try {
			Query q = getEntityManager().createQuery("select t from TheatreCompany t where t.id= :id");
			q.setParameter("id", COMPANY_ID);
			TheatreCompany retrieveCompany = (TheatreCompany) q.getSingleResult();
			Map<String, TheatreLocation> retrieveLocations = retrieveCompany.getLocations();

			// CODE as the key !!

			if ((retrieveLocations.get(LOCATION_CODE).getId() == LOCATION_ID)
					&& (retrieveLocations.get(CITY_CODE).getId() == CITY_ID)) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("Expected to read the relationship as a Map with keys" + " - " + LOCATION_CODE
						+ " and " + CITY_CODE + "; Actual Locations Found - "
						+ retrieveLocations.get(LOCATION_CODE).getId() + " and "
						+ retrieveLocations.get(CITY_CODE).getId());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOverrideMapKey" + e);
		}

	}

	/*
	 * @testName: testNoMapKeyAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1100; PERSISTENCE:SPEC:1101;
	 * PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104; PERSISTENCE:SPEC:1106;
	 * PERSISTENCE:SPEC:1107; PERSISTENCE:SPEC:1108; PERSISTENCE:SPEC:1110;
	 * PERSISTENCE:SPEC:1268; PERSISTENCE:JAVADOC:19
	 * 
	 * @test_Strategy: Store and Customers are two entities whose relationship is
	 * defined by "Many-to-One" annotation. The MapKey is defined only in orm.xml
	 * and not by using annotation. The following test checks whether MapKey is read
	 * or not.
	 */
	@Test
	public void testNoMapKeyAnnotation() throws Exception {

		getEntityTransaction().begin();
		Store store = createStore(STORE_ID, STORE_NAME);
		Customers customer1 = createCustomers(CUSTOMER1_ID, CUSTOMER1_NAME, store);
		Customers customer2 = createCustomers(CUSTOMER2_ID, CUSTOMER2_NAME, store);

		Map customersOfStore = new HashMap();
		customersOfStore.put(CUSTOMER1_ID, customer1);
		customersOfStore.put(CUSTOMER2_ID, customer2);

		store.setCustomers(customersOfStore);

		getEntityManager().persist(store);
		getEntityManager().persist(customer1);
		getEntityManager().persist(customer2);
		getEntityManager().flush();
		clearCache();
		getEntityTransaction().commit();
		clearCache();
		getEntityTransaction().begin();

		try {
			Query q = getEntityManager().createQuery("select s from Store s where s.id= :id");
			q.setParameter("id", STORE_ID);
			Store retrieveStore = (Store) q.getSingleResult();
			// retrieveStore.setCustomers(new Customers());
			getEntityManager().refresh(retrieveStore);
			Map<String, Customers> retrieveCustomers = retrieveStore.getCustomers();
			if ((retrieveCustomers.get(CUSTOMER1_NAME).getId() == CUSTOMER1_ID)
					&& (retrieveCustomers.get(CUSTOMER2_NAME).getId() == CUSTOMER2_ID)) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("Expected to read relationship as a Map with " + "customers - " + CUSTOMER1_ID
						+ " and " + CUSTOMER2_ID + "Actual Customers in the Map - "
						+ retrieveCustomers.get(CUSTOMER1_NAME).getId() + " and "
						+ retrieveCustomers.get(CUSTOMER2_NAME).getId());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNoMapKeyAnnotation" + e);
		}
	}

	/*
	 * @testName: testOverrideOrderBy
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1100; PERSISTENCE:SPEC:1101;
	 * PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104; PERSISTENCE:SPEC:1106;
	 * PERSISTENCE:SPEC:1107; PERSISTENCE:SPEC:1108; PERSISTENCE:SPEC:1110;
	 * PERSISTENCE:SPEC:1268;
	 * 
	 * @test_Strategy: Consumer and RetailOrder are two entities whose relationship
	 * is defined by "Many-to-Many" and OrderBy of cost in ascending order using
	 * annotations. The OrderBy value is overriden in orm.xml to descending. The
	 * following test checks whether OrderBy is performed in descending or not.
	 *
	 */
	@Test
	public void testOverrideOrderBy() throws Exception {

		Consumer consumer1 = new Consumer();
		consumer1.setId(CUST1_ID);
		RetailOrder order1 = createOrder(ORDER1_ID, consumer1, COST4);
		RetailOrder order2 = createOrder(ORDER2_ID, consumer1, COST1);
		RetailOrder order3 = createOrder(ORDER3_ID, consumer1, COST3);
		RetailOrder order4 = createOrder(ORDER4_ID, consumer1, COST2);

		consumer1Orders = new ArrayList<RetailOrder>();
		consumer1Orders.add(order1);
		consumer1Orders.add(order2);
		consumer1Orders.add(order3);
		consumer1Orders.add(order4);

		consumer1.setOrders(consumer1Orders);
		getEntityTransaction().begin();
		getEntityManager().persist(consumer1);
		getEntityManager().persist(order1);
		getEntityManager().persist(order2);
		getEntityManager().persist(order3);
		getEntityManager().persist(order4);
		getEntityManager().flush();
		clearCache();
		getEntityTransaction().commit();
		clearCache();
		getEntityTransaction().begin();

		Collections.sort(consumer1Orders, new RetailOrderCostComparatorDESC());
		try {
			Query q = getEntityManager().createQuery("select c from Consumer c where c.id= :id");
			q.setParameter("id", CUST1_ID);
			Consumer consumer = (Consumer) q.getSingleResult();
			List<RetailOrder> actualRetailOrders = consumer.getOrders();

			if (consumer1Orders.equals(actualRetailOrders)) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("The expected Orders List is not equal to the actual List read from the DB");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOverrideOrderBy" + e);
		}
	}

	private Employee createEmployee(final long id, final String code) {
		Employee emp = new Employee();
		emp.setId(id);
		emp.setCode(code);
		return emp;
	}

	private Department createDepartment(final long id, final String name) {
		Department dept = new Department();
		dept.setId(id);
		dept.setName(name);
		return dept;
	}

	private TheatreLocation createTheatreLocation(final long id, final String code) {
		TheatreLocation loc = new TheatreLocation();
		loc.setId(id);
		loc.setCode(code);
		return loc;
	}

	private TheatreCompany createTheatreCompany(final long id, final String name) {
		TheatreCompany company = new TheatreCompany();
		company.setId(id);
		company.setName(name);
		return company;
	}

	private Customers createCustomers(final int id, final String name, final Store store) {
		Customers customer = new Customers();
		customer.setId(id);
		customer.setCustName(name);
		customer.setStore(store);
		return customer;
	}

	private Store createStore(final int id, final String storeName) {
		Store store = new Store();
		store.setId(id);
		store.setName(storeName);
		return store;
	}

	private RetailOrder createOrder(final long id, final Consumer consumer, final double cost) {
		RetailOrder order = new RetailOrder();
		order.setId(id);
		order.addConsumer(consumer);
		order.setCost(cost);
		return order;
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
			getEntityManager().createNativeQuery("Delete from  DEPARTMENT_2").executeUpdate();
			getEntityManager().createNativeQuery("Delete from EMPLOYEE_2").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  STORE").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  CUSTOMERS").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  THEATRELOCATION_THEATRECOMPANY").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  RETAILORDER_CONSUMER").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  RETAILORDER").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  CONSUMER").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  THEATRELOCATION").executeUpdate();
			getEntityManager().createNativeQuery("Delete from  THEATRECOMPANY").executeUpdate();
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
