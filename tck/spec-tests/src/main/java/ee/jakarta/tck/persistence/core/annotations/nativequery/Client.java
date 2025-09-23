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

package ee.jakarta.tck.persistence.core.annotations.nativequery;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.TransactionRequiredException;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFile = { ORM_XML };
		String[] classes = { pkgName + "Item", pkgName + "Order1", pkgName + "Order2", pkgName + "PurchaseOrder" };
		return createDeploymentJar("jpa_core_annotations_nativequery.jar", pkgNameWithoutSuffix, classes, xmlFile);
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

	/*
	 * @testName: nativeQueryTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1010; PERSISTENCE:SPEC:1011;
	 * PERSISTENCE:JAVADOC:198; PERSISTENCE:JAVADOC:64; PERSISTENCE:JAVADOC:77;
	 * PERSISTENCE:JAVADOC:78; PERSISTENCE:JAVADOC:21; PERSISTENCE:JAVADOC:65;
	 * PERSISTENCE:SPEC:1370.2; PERSISTENCE:SPEC:1370.3;
	 * 
	 * @test_Strategy: When multiple entities are returned by a SQL query, the
	 * entities must be mapped to the column results of the SQL statement in a
	 * SqlResultSetMapping metadata definition.
	 *
	 * Scalar result types can be included in the query result by specifying the
	 * ColumnResult annotation in the metadata.
	 *
	 * 
	 */
	@Test
	public void nativeQueryTest2() throws Exception {

		boolean pass = false;
		int passCounter = 0;
		List q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			final Item i1 = new Item(7, "Loafers");
			final Item i2 = new Item(8, "High Heels");
			final Item i3 = new Item(9, "Socks");

			logger.log(Logger.Level.TRACE, "Create Orders");

			Order1 o7 = new Order1(7, 25.0D);
			o7.setItem(i1);
			getEntityManager().persist(o7);
			Order1 o8 = new Order1(8, 125.0D);
			o8.setItem(i2);
			getEntityManager().persist(o8);
			Order1 o9 = new Order1(9, 150.0D);
			o9.setItem(i3);
			getEntityManager().persist(o9);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery("Select o.ID AS \"OID\", o.TOTALPRICE AS \"OPRICE\", "
					+ "o.FK1_FOR_ITEM AS \"OITEM\", i.ITEMNAME AS \"INAME\" from ORDER1 o, ITEM i "
					+ "WHERE (o.TOTALPRICE < 100) AND (o.FK1_FOR_ITEM = i.ID)", "Order2ItemResults")
					.getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR, " Did not get expected results.  Expected: 1, " + "got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");

				for (Object obj : q) {
					// each element in the query result list should be an Object[], which
					// has exactly 2 elements. The first element is of type Order1, and
					// the
					// second of type String.
					if (obj instanceof Object[]) {
						Object[] objectArray = (Object[]) obj;

						for (Object o : objectArray) {

							if (o instanceof Order1) {
								Order1 orderReturned = (Order1) o;

								if (!orderReturned.equals(o7)) {
									logger.log(Logger.Level.ERROR, "Expected:" + o7 + ", actual:" + orderReturned);
								} else {
									logger.log(Logger.Level.TRACE, "Received expected order");
									passCounter++;
								}
								if (orderReturned.getItem() != i1) {
									logger.log(Logger.Level.ERROR,
											"Expected:" + i1 + ", actual:" + orderReturned.getItem());
								} else {
									logger.log(Logger.Level.TRACE, "Received expected item");
									passCounter++;
								}
							} else if (o instanceof String) {

								String itemName = (String) o;
								if (!itemName.equals(i1.getItemName())) {
									logger.log(Logger.Level.ERROR,
											"Expected:" + i1.getItemName() + ", actual:" + itemName);
								} else {
									logger.log(Logger.Level.TRACE, "Received expected itemName");
									passCounter++;
								}

							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected object:" + o);
							}
						}

					}
				}
			}
			if (passCounter == 3) {
				pass = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("nativeQueryTest2 failed");
	}

	/*
	 * @testName: nativeQueryTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1012; PERSISTENCE:JAVADOC:200;
	 * PERSISTENCE:SPEC:1014; PERSISTENCE:SPEC:1015; PERSISTENCE:SPEC:1016;
	 * PERSISTENCE:JAVADOC:201
	 * 
	 * @test_Strategy: When multiple entities are returned by a SQL query, the
	 * entities must be mapped to the column results of the SQL statement in a
	 * SqlResultSetMapping metadata definition.
	 *
	 * When multiple entity types that include aliases in the SQL statement are
	 * used, it is required that the column names be explicitly mapped to the entity
	 * fields. The FieldResult annotation is used for this purpose.
	 *
	 */
	@Test
	public void nativeQueryTest3() throws Exception {
		boolean pass = false;
		List q;
		int passCounter = 0;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			final Item i1 = new Item(1, "Boots");
			final Item i2 = new Item(2, "Sneakers");
			final Item i3 = new Item(3, "Slippers");

			logger.log(Logger.Level.TRACE, "Create Orders");
			Order1 o1 = new Order1(1, 25.0D);
			o1.setItem(i1);
			getEntityManager().persist(o1);
			Order1 o2 = new Order1(2, 125.0D);
			o2.setItem(i2);
			getEntityManager().persist(o2);
			Order1 o3 = new Order1(3, 150.0D);
			o3.setItem(i3);
			getEntityManager().persist(o3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager()
					.createNativeQuery("Select o.ID AS \"THISID\", o.TOTALPRICE AS \"THISPRICE\", "
							+ "o.FK1_FOR_ITEM AS \"THISITEM\", i.ID, i.ITEMNAME from ORDER1 o, ITEM i "
							+ "WHERE (o.TOTALPRICE > 140) AND (o.FK1_FOR_ITEM = i.ID)", "Order3ItemResults")
					.getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR, " Did not get expected results.  Expected: 1, " + "got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");

				for (Object obj : q) {
					// each element in the query result list should be an Object[], which
					// has exactly 2 elements. The first element is of type Order1, and
					// the
					// second of type String.
					if (obj instanceof Object[]) {
						Object[] objectArray = (Object[]) obj;

						for (Object o : objectArray) {

							if (o instanceof Order1) {
								Order1 orderReturned = (Order1) o;

								if (!orderReturned.equals(o3)) {
									logger.log(Logger.Level.ERROR, "Expected:" + o3 + ", actual:" + orderReturned);
								} else {
									logger.log(Logger.Level.TRACE, "Received expected order");
									passCounter++;
								}
								if (orderReturned.getItem() != i3) {
									logger.log(Logger.Level.ERROR,
											"Expected:" + i3 + ", actual:" + orderReturned.getItem());
								} else {
									logger.log(Logger.Level.TRACE, "Received expected item");
									passCounter++;
								}
							} else if (o instanceof Item) {
								Item item = (Item) o;
								if (item != i3) {
									logger.log(Logger.Level.ERROR, "Expected:" + i3 + ", actual:" + item);
								} else {
									logger.log(Logger.Level.TRACE, "Received expected item");
									passCounter++;
								}

							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected object:" + o);
							}
						}

					}
				}
			}
			if (passCounter == 3) {
				pass = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("nativeQueryTest3 failed");
	}

	/*
	 * @testName: nativeQueryColumnResultTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1511; PERSISTENCE:SPEC:1556;
	 * PERSISTENCE:SPEC:1954;
	 * 
	 * @test_Strategy: Using the jakarta.persistence.ColumnResult.type attribute,
	 * return the totalprice as a String
	 *
	 */
	@Test
	public void nativeQueryColumnResultTypeTest() throws Exception {

		boolean pass = false;
		List q;
		int passCounter = 0;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			final Item i1 = new Item(7, "Loafers");
			final Item i2 = new Item(8, "High Heels");
			final Item i3 = new Item(9, "Socks");

			logger.log(Logger.Level.TRACE, "Create Orders");

			Order1 o7 = new Order1(7, 25.0D);
			o7.setItem(i1);
			getEntityManager().persist(o7);
			Order1 o8 = new Order1(8, 125.0D);
			o8.setItem(i2);
			getEntityManager().persist(o8);
			Order1 o9 = new Order1(9, 150.0D);
			o9.setItem(i3);
			getEntityManager().persist(o9);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery("Select o.ID AS \"OID\", o.TOTALPRICE AS \"OPRICE\", "
					+ "o.FK1_FOR_ITEM AS \"OITEM\", i.ITEMNAME AS \"INAME\" from ORDER1 o, ITEM i "
					+ "WHERE (o.TOTALPRICE < 100) AND (o.FK1_FOR_ITEM = i.ID)", "Order4ItemResults")
					.getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR, " Did not get expected results.  Expected: 1, " + "got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");

				for (Object obj : q) {
					// each element in the query result list should be an Object[], which
					// has exactly 2 elements. The first element is of type Order1, and
					// the
					// second of type String.
					if (obj instanceof Object[]) {
						Object[] objectArray = (Object[]) obj;

						for (Object o : objectArray) {

							if (o instanceof Order1) {
								Order1 orderReturned = (Order1) o;

								if (!orderReturned.equals(o7)) {
									logger.log(Logger.Level.ERROR, "Expected:" + o7 + ", actual:" + orderReturned);
								} else {
									logger.log(Logger.Level.TRACE, "Received expected order");
									passCounter++;
								}
								if (orderReturned.getItem() != i1) {
									logger.log(Logger.Level.ERROR,
											"Expected:" + i1 + ", actual:" + orderReturned.getItem());
								} else {
									logger.log(Logger.Level.TRACE, "Received expected item");
									passCounter++;
								}
							} else if (o instanceof String) {

								String itemName = (String) o;
								if (!itemName.equals(i1.getItemName())) {
									logger.log(Logger.Level.ERROR,
											"Expected:" + i1.getItemName() + ", actual:" + itemName);
								} else {
									logger.log(Logger.Level.TRACE, "Received expected itemName");
									passCounter++;
								}

							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected object:" + o);
							}
						}

					}
				}
			}
			if (passCounter == 3) {
				pass = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("nativeQueryColumnResultTypeTest failed");
	}

	/*
	 * @testName: nativeQueryTestConstructorResult
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1512; PERSISTENCE:JAVADOC:1513;
	 * PERSISTENCE:SPEC:1951; PERSISTENCE:SPEC:1952; PERSISTENCE:SPEC:1953;
	 * PERSISTENCE:SPEC:1955;
	 * 
	 * @test_Strategy: Verify jakarta.persistence.ConstructorResult can call a class
	 * constructor passing in arg data and return the class.
	 */
	@Test
	public void nativeQueryTestConstructorResult() throws Exception {

		boolean pass = false;
		List<Order2> q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			final Item i1 = new Item(7, "Loafers");
			final Item i2 = new Item(8, "High Heels");
			final Item i3 = new Item(9, "Socks");

			logger.log(Logger.Level.TRACE, "Create Orders");

			Order1 o7 = new Order1(7, 25.0D);
			o7.setItem(i1);
			getEntityManager().persist(o7);
			Order1 o8 = new Order1(8, 125.0D);
			o8.setItem(i2);
			getEntityManager().persist(o8);
			Order1 o9 = new Order1(9, 150.0D);
			o9.setItem(i3);
			getEntityManager().persist(o9);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager()
					.createNativeQuery(
							"Select o.ID AS \"OID\", o.TOTALPRICE AS \"OPRICE\", "
									+ "i.ITEMNAME AS \"OITEMNAME\" from ORDER1 o, ITEM i "
									+ "WHERE (o.TOTALPRICE < 100) AND (o.FK1_FOR_ITEM = i.ID)",
							"Order5ItemResults")
					.getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				Order2 o = q.get(0);
				if (o.getId() == o7.getId()) {
					if (o.getTotalPrice() == o7.getTotalPrice()) {
						if (o.getItemName().equals(o7.getItem().getItemName())) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected:" + o7.getItem().getItemName() + ", actual:" + o.getItemName());
						}
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected:" + o7.getTotalPrice() + ", actual:" + o.getTotalPrice());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + o7.getId() + ", actual:" + o.getId());
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("nativeQueryTestConstructorResult failed");

	}

	/*
	 * @testName: nativeQueryTestConstructorResultWithId
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1557;
	 * 
	 * @test_Strategy: Verify jakarta.persistence.ConstructorResult can call a class
	 * constructor passing in arg data and return the class.
	 *
	 */
	@Test
	public void nativeQueryTestConstructorResultWithId() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		List<PurchaseOrder> q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Purchase Orders");

			PurchaseOrder p1 = new PurchaseOrder(7, 25);
			getEntityManager().persist(p1);
			PurchaseOrder p2 = new PurchaseOrder(8, 50);
			getEntityManager().persist(p2);
			PurchaseOrder p3 = new PurchaseOrder(9, 75);
			getEntityManager().persist(p3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery(
					"Select o.ID AS \"OID\", o.TOTAL AS \"PTOTAL\" from PURCHASE_ORDER o "
							+ "WHERE (o.TOTAL < 50)",
					"PurchaseOrder1Results").getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				PurchaseOrder p = q.get(0);
				if (p.getId() == p1.getId()) {
					if (p.getTotal() == p1.getTotal()) {
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected:" + p1.getTotal() + ", actual:" + p.getTotal());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected: " + p1.getId() + ", actual:" + p.getId());
				}
				if (!getEntityManager().contains(p)) {
					logger.log(Logger.Level.INFO, "Entity contains id and is managed.");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Entity contains an id and is not managed.");
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("nativeQueryTestConstructorResultWithId failed");

	}

	/*
	 * @testName: nativeQueryTestConstructorResultNoId
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1557;
	 * 
	 * @test_Strategy: Verify jakarta.persistence.ConstructorResult can call a class
	 * constructor passing in arg data except the id and return the class.
	 *
	 */
	@Test
	public void nativeQueryTestConstructorResultNoId() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		List<PurchaseOrder> q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Purchase Orders");

			PurchaseOrder p1 = new PurchaseOrder(7, 25);
			getEntityManager().persist(p1);
			PurchaseOrder p2 = new PurchaseOrder(8, 50);
			getEntityManager().persist(p2);
			PurchaseOrder p3 = new PurchaseOrder(9, 75);
			getEntityManager().persist(p3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery(
					"Select o.TOTAL AS \"PTOTAL\" from PURCHASE_ORDER o " + "WHERE (o.TOTAL < 50)",
					"PurchaseOrder2Results").getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				PurchaseOrder p = q.get(0);
				if (p.getId() == 0) {
					if (p.getTotal() == p1.getTotal()) {
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected:" + p1.getTotal() + ", actual:" + p.getTotal());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 0, actual:" + p.getId());
				}
				if (!getEntityManager().contains(p)) {
					logger.log(Logger.Level.INFO, "Entity does not contain id and is detached.");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Entity does not contain an id and is not detached.");
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("nativeQueryTestConstructorResultNoId failed");

	}

	/*
	 * @testName: createNativeQueryStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:39; PERSISTENCE:SPEC:1511;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createNativeQueryStringTest() throws Exception {
		boolean pass = false;
		List q = null;
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(2);

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			final Item i1 = new Item(1, "Boots");
			final Item i2 = new Item(2, "Sneakers");
			final Item i3 = new Item(3, "Slippers");

			logger.log(Logger.Level.TRACE, "Create Orders");
			Order1 o1 = new Order1(1, 25.0D);
			o1.setItem(i1);
			getEntityManager().persist(o1);
			Order1 o2 = new Order1(2, 125.0D);
			o2.setItem(i2);
			getEntityManager().persist(o2);
			Order1 o3 = new Order1(3, 150.0D);
			o3.setItem(i3);
			getEntityManager().persist(o3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery("Select o.ID from ORDER1 o WHERE o.ID = 2 ")
					.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Object o : q) {
				actual.add(convertToInt(o));
			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (Integer i : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + i);
				}
				for (Integer i : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + i);
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("createNativeQueryStringTest failed");
	}

	/*
	 * @testName: createNativeQueryResultClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:40
	 * 
	 * @test_Strategy: Use the createNativeQuery(String,Class)
	 *
	 */
	@Test
	public void createNativeQueryResultClassTest() throws Exception {
		boolean pass = false;
		List<Order1> q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			final Item i1 = new Item(1, "Boots");
			final Item i2 = new Item(2, "Sneakers");
			final Item i3 = new Item(3, "Slippers");

			logger.log(Logger.Level.TRACE, "Create Orders");
			Order1 o1 = new Order1(1, 25.0D);
			o1.setItem(i1);
			getEntityManager().persist(o1);
			Order1 o2 = new Order1(2, 125.0D);
			o2.setItem(i2);
			getEntityManager().persist(o2);
			Order1 o3 = new Order1(3, 150.0D);
			o3.setItem(i3);
			getEntityManager().persist(o3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery("Select * from ORDER1 o WHERE o.ID = 2 ", Order1.class)
					.getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR, " Did not get expected results.  Expected: 1, got: " + q.size());
			} else {
				int id = q.get(0).getId();
				if (id != 2) {
					logger.log(Logger.Level.ERROR, "id expected:2, actual:" + id);

				} else {
					pass = true;
				}

			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new

			Exception("createNativeQueryResultClassTest failed");

	}

	/*
	 * @testName: createNativeQueryResultClassTQTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:320
	 * 
	 * @test_Strategy: Use the createNativeQuery(String,Class) a TypedQuery
	 *
	 */
	@Test
	public void createNativeQueryResultClassTQTest() throws Exception {
		boolean pass = false;
		List<Order1> q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			Item i1 = new Item(1, "Boots");
			Item i2 = new Item(2, "Sneakers");
			Item i3 = new Item(3, "Slippers");

			logger.log(Logger.Level.TRACE, "Create Orders");
			Order1 o1 = new Order1(1, 25.0D);
			o1.setItem(i1);
			getEntityManager().persist(o1);
			Order1 o2 = new Order1(2, 125.0D);
			o2.setItem(i2);
			getEntityManager().persist(o2);
			Order1 o3 = new Order1(3, 150.0D);
			o3.setItem(i3);
			getEntityManager().persist(o3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery("Select * from ORDER1 o WHERE o.ID = 2 ", Order1.class)
					.getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR, " Did not get expected results.  Expected: 1, got: " + q.size());
			} else {
				int id = q.get(0).getId();
				if (id != 2) {
					logger.log(Logger.Level.ERROR, "id expected:2, actual:" + id);

				} else {
					pass = true;
				}

			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("createNativeQueryResultClassTQTest failed");
	}

	/*
	 * @testName: executeUpdateTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:552
	 * 
	 * @test_Strategy: Use the createNativeQuery(String).executeUpdate() when no
	 * transaction is in effect and verify TransactionRequiredException is thrown
	 *
	 */
	@Test
	public void executeUpdateTransactionRequiredExceptionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityManager().createNativeQuery("Delete from ORDER1").executeUpdate();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException was not thrown");
		} catch (TransactionRequiredException ise) {
			logger.log(Logger.Level.TRACE, "Received expected TransactionRequiredException");
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
		if (!pass)
			throw new Exception("executeUpdateTransactionRequiredExceptionTest failed");
	}

	/*
	 * @testName: setParameterTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1308; PERSISTENCE:SPEC:1309;
	 * 
	 * @test_Strategy: Use the createNativeQuery(String,Class) a TypedQuery
	 *
	 */
	@Test
	public void setParameterTest() throws Exception {
		boolean pass = false;
		List<Order1> q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			Item i1 = new Item(1, "Boots");
			Item i2 = new Item(2, "Sneakers");
			Item i3 = new Item(3, "Slippers");

			logger.log(Logger.Level.TRACE, "Create Orders");
			Order1 o1 = new Order1(1, 25.0D);
			o1.setItem(i1);
			getEntityManager().persist(o1);
			Order1 o2 = new Order1(2, 125.0D);
			o2.setItem(i2);
			getEntityManager().persist(o2);
			Order1 o3 = new Order1(3, 150.0D);
			o3.setItem(i3);
			getEntityManager().persist(o3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery("Select * from ORDER1 o WHERE o.ID = ? ", Order1.class)
					.setParameter(1, 2).getResultList();

			if (q.size() != 1) {
				logger.log(Logger.Level.ERROR, " Did not get expected results.  Expected: 1, got: " + q.size());
			} else {
				int id = q.get(0).getId();
				if (id != 2) {
					logger.log(Logger.Level.ERROR, "id expected:2, actual:" + id);

				} else {
					logger.log(Logger.Level.TRACE, "Received expected result");
					pass = true;
				}

			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("setParameterTest failed");
	}

	/*
	 * @testName: getSingleResultTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1512;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingleResultTest() throws Exception {
		boolean pass = false;
		Object q;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create Items");
			Item i1 = new Item(1, "Boots");
			Item i2 = new Item(2, "Sneakers");
			Item i3 = new Item(3, "Slippers");

			logger.log(Logger.Level.TRACE, "Create Orders");
			Order1 o1 = new Order1(1, 25.0D);
			o1.setItem(i1);
			getEntityManager().persist(o1);
			Order1 o2 = new Order1(2, 125.0D);
			o2.setItem(i2);
			getEntityManager().persist(o2);
			Order1 o3 = new Order1(3, 150.0D);
			o3.setItem(i3);
			getEntityManager().persist(o3);

			logger.log(Logger.Level.TRACE, "Execute Query");
			q = getEntityManager().createNativeQuery("Select COUNT(*) from ORDER1 o").getSingleResult();
			int i = 0;
			i = convertToInt(q);
			if (i == 3) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + i);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 3, actual:" + i);
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("getSingleResultTest failed");
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
			getEntityManager().createNativeQuery("Delete FROM ORDER2").executeUpdate();
			getEntityManager().createNativeQuery("Delete FROM ORDER1").executeUpdate();
			getEntityManager().createNativeQuery("Delete FROM ITEM").executeUpdate();
			getEntityManager().createNativeQuery("Delete FROM PURCHASE_ORDER").executeUpdate();

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