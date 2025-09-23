/*
 * Copyright (c) 2009, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;

import ee.jakarta.tck.persistence.common.PMClientBase;

public abstract class Util extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Util.class.getName());

	protected final Phone phone[] = new Phone[50];

	protected final Address address[] = new Address[50];

	protected final Country country[] = new Country[20];

	protected final CreditCard creditCard[] = new CreditCard[24];

	protected final LineItem lineItem[] = new LineItem[60];

	protected final Spouse spouse[] = new Spouse[6];

	protected final Info info[] = new Info[6];

	protected final Customer customerRef[] = new Customer[20];

	protected final Order orderRef[] = new Order[20];

	protected final Alias aliasRef[] = new Alias[30];

	protected final Product productRef[] = new Product[18];

	protected final HardwareProduct hardwareRef[] = new HardwareProduct[10];

	protected final SoftwareProduct softwareRef[] = new SoftwareProduct[10];

	protected final ShelfLife shelfRef[] = new ShelfLife[20];

	protected final Department deptRef[] = new Department[5];

	protected final Employee empRef[] = new Employee[10];

	protected final Trim trimRef[] = new Trim[20];

	protected final CriteriaEntity criteriaEntity[] = new CriteriaEntity[5];

	private final String[] schema30classes = { "Address_", "Address", "Alias_", "Alias", "Country_", "Country",
			"CreditCard_", "CreditCard", "CriteriaEntity", "Customer_", "Customer", "Department_", "Department", "Employee_", "Employee",
			"HardwareProduct_", "HardwareProduct", "Info_", "Info", "LineItem_", "LineItem", "LineItemException",
			"Order_", "Order", "Phone_", "Phone", "Product_", "Product", "ShelfLife_", "ShelfLife", "SoftwareProduct_",
			"SoftwareProduct", "Spouse_", "Spouse", "Trim_", "Trim" };

	protected String[] getSchema30classes() {
		String[] classes = new String[schema30classes.length];
		for (int i = 0; i < schema30classes.length; i++) {
			classes[i] = Util.class.getPackageName() + "." + schema30classes[i];
		}
		return classes;
	}

	public static String[] concat(String[] first, String[] second) {
		String[] both = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, both, first.length, second.length);
		return both;
	}

	public String toString(Collection c) {
		StringBuilder result = new StringBuilder();
		if (c != null) {
			result.append(Arrays.toString(c.toArray()));
		} else {
			result.append("null");
		}
		return result.toString();
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

	public void createTrimData() throws Exception {
		logger.log(Logger.Level.TRACE, "createTrimData");
		getEntityTransaction().begin();
		try {
			trimRef[0] = new Trim("1", "Alan E. Frechette");
			trimRef[1] = new Trim("2", "Arthur D. Frechette");
			trimRef[2] = new Trim("3", "Shelly D. McGowan");
			trimRef[3] = new Trim("4", "Robert E. Bissett");
			trimRef[4] = new Trim("5", "Stephen S. D'Milla");
			trimRef[5] = new Trim("6", "Karen R. Tegan");
			trimRef[6] = new Trim("7", "Stephen J. Caruso");
			trimRef[7] = new Trim("8", "Irene M. Caruso");
			trimRef[8] = new Trim("9", "William P. Keaton");
			trimRef[9] = new Trim("10", "Kate P. Hudson");
			trimRef[10] = new Trim("11", "Jonathan K. Smith");
			trimRef[11] = new Trim("12", "Douglas A. Donahue");
			trimRef[12] = new Trim("13", "Kellie A. Sanborn");
			trimRef[13] = new Trim("14", "Margaret Mills");
			trimRef[14] = new Trim("15", "Sonya C. Sanders");
			trimRef[15] = new Trim("16", "Jack B. Grace");
			trimRef[16] = new Trim("17", "Ron F. Bender");
			trimRef[17] = new Trim("18", "Lisa M. Presley");
			trimRef[18] = new Trim("19", " David R. Vincent ");
			for (Trim t : trimRef) {
				if (t != null) {
					getEntityManager().persist(t);
					logger.log(Logger.Level.TRACE, "persisted trim " + t);
					doFlush();
				}
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createTrimData failed:", e);
		}

	}

	public void createProductData() throws Exception {

		// logger.log(Logger.Level.TRACE,"persisting product entities");
		logger.log(Logger.Level.TRACE, "createProductData");
		getEntityTransaction().begin();
		try {
			productRef[0] = new Product("1", "Java 2 Unleashed Programming", (double) 54.95, 100, (long) 987654321);
			productRef[0].setWareHouse("WH1");
			productRef[1] = new Product("2", "Java 2 Network Programming", (double) 37.95, 100, (long) 876543219);
			productRef[1].setWareHouse("WH1");
			productRef[2] = new Product("3", "CORBA Programming", (double) 44.95, 55, (long) 765432198);
			productRef[2].setWareHouse("WH1");
			productRef[3] = new Product("4", "WEB Programming with JSP's & Servlet's", (double) 33.95, 25,
					(long) 654321987);
			productRef[3].setWareHouse("WH1");
			productRef[4] = new Product("5", "Dell Laptop PC", (double) 1095.95, 50, (long) 543219876);
			productRef[4].setWareHouse("WH2");
			productRef[5] = new Product("6", "Compaq Laptop PC", (double) 995.95, 33, (long) 432198765);
			productRef[5].setWareHouse("WH2");
			productRef[6] = new Product("7", "Toshiba Laptop PC", (double) 1210.95, 22, (long) 321987654);
			productRef[6].setWareHouse("WH2");
			productRef[7] = new Product("8", "Gateway Laptop PC", (double) 1100.95, 11, (long) 219876543);
			productRef[7].setWareHouse("WH2");
			productRef[8] = new Product("9", "Free Samples", (double) 0.00, 10, (long) 000000000);
			productRef[8].setWareHouse("WH3");
			productRef[9] = new Product("10", "Designing Enterprise Applications", (double) 39.95, 500,
					(long) 123456789);
			productRef[9].setWareHouse("WH3");
			productRef[10] = new Product("11", "Complete Guide to XML", (double) 38.85, 300, (long) 234567891);
			productRef[10].setWareHouse("WH3");
			productRef[11] = new Product("12", "Programming for Dummies", (double) 24.95, 45, (long) 345678912);
			productRef[11].setWareHouse("WH3");
			productRef[12] = new Product("13", "Introduction to Java", (double) 60.95, 95, (long) 456789123);
			productRef[12].setWareHouse("WH3");
			productRef[13] = new Product("14", "Ultra System", (double) 5095.95, 250, (long) 567891234);
			productRef[13].setWareHouse("WH4");
			productRef[14] = new Product("15", "Very Best Tutorial", (double) 25.99, 0, (long) 678912345);
			productRef[14].setWareHouse("WH4");
			productRef[15] = new Product("16", "Home Grown Programming Examples", (double) 10.95, 25, (long) 789123456);
			productRef[15].setWareHouse("WH4");
			productRef[16] = new Product("17", "Programming in ANSI C", (double) 23.95, 10, (long) 891234567);
			productRef[16].setWareHouse("WH4");
			productRef[17] = new Product("18", "Trial Software", (double) 10.00, 75, (long) 912345678);
			productRef[17].setWareHouse("WH5");

			for (Product p : productRef) {
				if (p != null) {
					getEntityManager().persist(p);
					logger.log(Logger.Level.TRACE, "persisting product " + p);
					doFlush();
				}
			}

			final Date d1 = getSQLDate("2000-02-14");
			final Date d2 = getSQLDate("2001-06-27");
			final Date d3 = getSQLDate("2002-07-07");
			final Date d4 = getSQLDate("2003-03-03");
			final Date d5 = getSQLDate("2004-04-10");
			final Date d6 = getSQLDate("2005-02-18");
			final Date d7 = getSQLDate("2000-09-17");
			final Date d8 = getSQLDate("2001-11-14");
			final Date d9 = getSQLDate("2002-10-04");
			final Date d10 = getSQLDate("2003-01-25");

			logger.log(Logger.Level.TRACE, "create ShelfLife Data");

			shelfRef[0] = new ShelfLife(d1, null);
			shelfRef[1] = new ShelfLife(d2, null);
			shelfRef[2] = new ShelfLife(d3, null);
			shelfRef[3] = new ShelfLife(d4, null);
			shelfRef[4] = new ShelfLife(d5, null);
			shelfRef[5] = new ShelfLife(null, null);
			shelfRef[6] = new ShelfLife(null, d6);
			shelfRef[7] = new ShelfLife(null, d7);
			shelfRef[8] = new ShelfLife(d8, d9);
			shelfRef[9] = new ShelfLife(null, d10);

			logger.log(Logger.Level.TRACE, "create Hardware Data");
			hardwareRef[0] = new HardwareProduct();
			hardwareRef[0].setId("19");
			hardwareRef[0].setName("Gateway E Series");
			hardwareRef[0].setPrice((double) 600.00);
			hardwareRef[0].setQuantity(25);
			hardwareRef[0].setPartNumber((long) 238945678);
			hardwareRef[0].setShelfLife(shelfRef[0]);
			hardwareRef[0].setWareHouse("Columbia");
			hardwareRef[0].setModelNumber(2578);
			getEntityManager().persist(hardwareRef[0]);
			hardwareRef[1] = new HardwareProduct();
			hardwareRef[1].setId("20");
			hardwareRef[1].setName("Java Desktop Systems");
			hardwareRef[1].setPrice((double) 890.00);
			hardwareRef[1].setQuantity(50);
			hardwareRef[1].setPartNumber((long) 304506708);
			hardwareRef[1].setModelNumber(10050);
			hardwareRef[1].setWareHouse("Lowell");
			getEntityManager().persist(hardwareRef[1]);
			hardwareRef[2] = new HardwareProduct();
			hardwareRef[2].setId("21");
			hardwareRef[2].setName("Dell Inspiron");
			hardwareRef[2].setPrice((double) 1100.00);
			hardwareRef[2].setQuantity(5);
			hardwareRef[2].setPartNumber((long) 373767373);
			hardwareRef[2].setModelNumber(01100);
			hardwareRef[2].setWareHouse("Richmond");
			hardwareRef[2].setShelfLife(shelfRef[1]);
			getEntityManager().persist(hardwareRef[2]);
			hardwareRef[3] = new HardwareProduct();
			hardwareRef[3].setId("22");
			hardwareRef[3].setName("Toshiba");
			hardwareRef[3].setPrice((double) 250.00);
			hardwareRef[3].setQuantity(40);
			hardwareRef[3].setPartNumber((long) 285764839);
			hardwareRef[3].setModelNumber(00720);
			hardwareRef[3].setWareHouse("Richmond");
			getEntityManager().persist(hardwareRef[3]);
			hardwareRef[4] = new HardwareProduct();
			hardwareRef[4].setId("23");
			hardwareRef[4].setName("SunBlade");
			hardwareRef[4].setPrice((double) 450.00);
			hardwareRef[4].setQuantity(80);
			hardwareRef[4].setPartNumber((long) 987290102);
			hardwareRef[4].setModelNumber(00150);
			getEntityManager().persist(hardwareRef[4]);
			hardwareRef[5] = new HardwareProduct();
			hardwareRef[5].setId("24");
			hardwareRef[5].setName("Opteron");
			hardwareRef[5].setPrice((double) 800.00);
			hardwareRef[5].setQuantity(33);
			hardwareRef[5].setPartNumber((long) 725109484);
			hardwareRef[5].setModelNumber(00050);
			hardwareRef[5].setWareHouse("Lowell");
			hardwareRef[5].setShelfLife(shelfRef[2]);
			getEntityManager().persist(hardwareRef[5]);
			hardwareRef[6] = new HardwareProduct();
			hardwareRef[6].setId("25");
			hardwareRef[6].setName("Sun Enterprise");
			hardwareRef[6].setPrice((double) 15000.00);
			hardwareRef[6].setQuantity(100);
			hardwareRef[6].setPartNumber((long) 773620626);
			hardwareRef[6].setModelNumber(10000);
			getEntityManager().persist(hardwareRef[6]);
			hardwareRef[7] = new HardwareProduct();
			hardwareRef[7].setId("26");
			hardwareRef[7].setName("Dell Dimension");
			hardwareRef[7].setPrice((double) 950.00);
			hardwareRef[7].setQuantity(70);
			hardwareRef[7].setPartNumber((long) 927262628);
			hardwareRef[7].setModelNumber(3000);
			getEntityManager().persist(hardwareRef[7]);
			hardwareRef[8] = new HardwareProduct();
			hardwareRef[8].setId("27");
			hardwareRef[8].setName("Dell Dimension");
			hardwareRef[8].setPrice((double) 795.00);
			hardwareRef[8].setQuantity(20);
			hardwareRef[8].setPartNumber((long) 482726166);
			hardwareRef[8].setModelNumber(04500);
			hardwareRef[8].setShelfLife(shelfRef[3]);
			hardwareRef[8].setWareHouse("Columbia");
			getEntityManager().persist(hardwareRef[8]);
			hardwareRef[9] = new HardwareProduct();
			hardwareRef[9].setId("28");
			hardwareRef[9].setName("SunBlade");
			hardwareRef[9].setPrice((double) 1000.00);
			hardwareRef[9].setQuantity(20);
			hardwareRef[9].setPartNumber((long) 312010108);
			hardwareRef[9].setModelNumber(00100);
			hardwareRef[9].setShelfLife(shelfRef[4]);
			getEntityManager().persist(hardwareRef[9]);
			doFlush();

			logger.log(Logger.Level.TRACE, "create Software Data");
			softwareRef[0] = new SoftwareProduct();
			softwareRef[0].setId("29");
			softwareRef[0].setName("SunOS 9");
			softwareRef[0].setPrice((double) 500.00);
			softwareRef[0].setQuantity(500);
			softwareRef[0].setPartNumber((long) 837373379);
			softwareRef[0].setRevisionNumber((double) 1.0);
			softwareRef[0].setShelfLife(shelfRef[5]);
			getEntityManager().persist(softwareRef[0]);
			softwareRef[1] = new SoftwareProduct();
			softwareRef[1].setId("30");
			softwareRef[1].setName("Patch 590-009");
			softwareRef[1].setPrice((double) 55.00);
			softwareRef[1].setQuantity(23);
			softwareRef[1].setPartNumber((long) 285764891);
			softwareRef[1].setRevisionNumber((double) 1.1);
			getEntityManager().persist(softwareRef[1]);
			softwareRef[2] = new SoftwareProduct();
			softwareRef[2].setId("31");
			softwareRef[2].setName("NetBeans");
			softwareRef[2].setPrice((double) 35.00);
			softwareRef[2].setQuantity(15);
			softwareRef[2].setPartNumber((long) 174983901);
			softwareRef[2].setRevisionNumber((double) 4.0);
			softwareRef[2].setShelfLife(shelfRef[6]);
			softwareRef[2].setWareHouse("Lowell");
			getEntityManager().persist(softwareRef[2]);
			softwareRef[3] = new SoftwareProduct();
			softwareRef[3].setId("32");
			softwareRef[3].setName("J2SE");
			softwareRef[3].setPrice((double) 150.00);
			softwareRef[3].setQuantity(100);
			softwareRef[3].setPartNumber((long) 173479765);
			softwareRef[3].setRevisionNumber((double) 5.0);
			softwareRef[3].setShelfLife(shelfRef[7]);
			getEntityManager().persist(softwareRef[3]);
			softwareRef[4] = new SoftwareProduct();
			softwareRef[4].setId("33");
			softwareRef[4].setName("Creator");
			softwareRef[4].setPrice((double) 125.00);
			softwareRef[4].setQuantity(60);
			softwareRef[4].setPartNumber((long) 847651234);
			softwareRef[4].setRevisionNumber((double) 4.0);
			softwareRef[4].setShelfLife(shelfRef[8]);
			getEntityManager().persist(softwareRef[4]);
			softwareRef[5] = new SoftwareProduct();
			softwareRef[5].setId("34");
			softwareRef[5].setName("Java Programming Examples");
			softwareRef[5].setPrice((double) 175.00);
			softwareRef[5].setQuantity(200);
			softwareRef[5].setPartNumber((long) 376512908);
			softwareRef[5].setRevisionNumber((double) 1.5);
			getEntityManager().persist(softwareRef[5]);
			softwareRef[6] = new SoftwareProduct();
			softwareRef[6].setId("35");
			softwareRef[6].setName("Tutorial");
			softwareRef[6].setPrice((double) 250.00);
			softwareRef[6].setQuantity(35);
			softwareRef[6].setPartNumber((long) 837462890);
			softwareRef[6].setRevisionNumber((double) 1.4);
			softwareRef[6].setWareHouse(null);
			getEntityManager().persist(softwareRef[6]);
			softwareRef[7] = new SoftwareProduct();
			softwareRef[7].setId("36");
			softwareRef[7].setName("Testing Tools");
			softwareRef[7].setPrice((double) 300.00);
			softwareRef[7].setQuantity(20);
			softwareRef[7].setPartNumber((long) 372615467);
			softwareRef[7].setRevisionNumber((double) 1.0);
			getEntityManager().persist(softwareRef[7]);
			softwareRef[8] = new SoftwareProduct();
			softwareRef[8].setId("37");
			softwareRef[8].setName("Patch 395-478");
			softwareRef[8].setPrice((double) 55.00);
			softwareRef[8].setQuantity(25);
			softwareRef[8].setPartNumber((long) 847628901);
			softwareRef[8].setRevisionNumber((double) 1.1);
			softwareRef[8].setShelfLife(shelfRef[9]);
			softwareRef[8].setWareHouse("Lowell");
			getEntityManager().persist(softwareRef[8]);
			softwareRef[9] = new SoftwareProduct();
			softwareRef[9].setId("38");
			softwareRef[9].setName("Appserver 8");
			softwareRef[9].setPrice((double) 0.00);
			softwareRef[9].setQuantity(150);
			softwareRef[9].setPartNumber((long) 873657891);
			softwareRef[9].setRevisionNumber((double) 1.1);
			getEntityManager().persist(softwareRef[9]);
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createProductData failed:", e);
		}
	}

	private void createAddressData() throws Exception {
		logger.log(Logger.Level.TRACE, "createAddressData");

		// DO NOT PERSIST THIS ENTITY, it will be done via the Cascade.ALL
		// in the other entities that use it.
		address[0] = new Address("1", "1 Oak Road", "Bedford", "MA", "02155");
		address[1] = new Address("2", "1 Network Drive", "Burlington", "MA", "00252");

		address[2] = new Address("3", "10 Griffin Road", "Lexington", "MA", "02277");
		address[3] = new Address("4", "1 Network Drive", "Burlington", "MA", "00252");

		address[4] = new Address("5", "125 Moxy Lane", "Swansea", "MA", "11345");
		address[5] = new Address("6", "1 Network Drive", "Burlington", "MA", "11345");

		address[6] = new Address("7", "2654 Brookline Avenue", "Brookline", "MA", "11678");
		address[7] = new Address("8", "1 Network Drive", "Burlington", "MA", "00252");

		address[8] = new Address("9", "100 Forrest Drive", "Hudson", "NH", "78654");
		address[9] = new Address("10", "1 Network Drive", "Burlington", "MA", "00252");

		address[10] = new Address("11", "200 Elliot Road", "Nashua", "NH", "65447");
		address[11] = new Address("12", "1 Network Drive", "Burlington", "MA", "00252");

		address[12] = new Address("13", "634 Goldstar Road", "Peabody", "MA", "88444");
		address[13] = new Address("14", "1 Network Drive", "Burlington", "MA", "00252");

		address[14] = new Address("15", "100 Forrest Drive", "Peabody", "MA", "88444");
		address[15] = new Address("16", "1 Network Drive", "Burlington", "MA", "00252");

		address[16] = new Address("17", "18 Rosewood Avenue", null, "MA", "87653");
		address[17] = new Address("18", "1 Network Drive", "Burlington", "MA", "00252");

		address[18] = new Address("19", null, "Belmont", "VT", "23083");
		address[19] = new Address("20", "1 Network Drive", "Burlington", "MA", "00252");

		address[20] = new Address("21", "3212 Boston Road", "Chelmsford", "MA", "01824");
		address[21] = new Address("22", "1 Network Drive", "Burlington", "MA", "00252");

		address[22] = new Address("23", "212 Edgewood Drive", "Claremont", "NH", "58976");
		address[23] = new Address("24", "1 Network Drive", "Burlington", null, "00252");

		address[24] = new Address("25", "47 Skyline Drive", "Attleboro", "MA", "76656");
		address[25] = new Address("26", "1 Network Drive", "Burlington", "MA", null);

		address[26] = new Address("27", "4 Rangeway Road", "Lawrence", "RI", "53026");
		address[27] = new Address("28", "1 Network Drive", "Burlington", "MA", "00252");

		address[28] = new Address("29", "48 Sears Street", "Boston", "MA", "02110");
		address[29] = new Address("30", "1 Network Drive", "Burlington", "MA", "00252");

		address[30] = new Address("31", "1240 Davis Drive", "Northwood", "NH", "03260");
		address[31] = new Address("32", "1 Network Drive", "Burlington", "MA", "00252");

		address[32] = new Address("33", "455 James Avenue", "Roslindale", "NY", "57550");
		address[33] = new Address("34", "1 Network Drive", "Burlington", "MA", "00252");

		address[34] = new Address("35", "8 Beverly Lane", "Burlington", "MA", "00252");
		address[35] = new Address("36", "1 Network Drive", "Burlington", "MA", "00252");

	}

	public void createCustomerData() throws Exception {
		logger.log(Logger.Level.TRACE, "createCustomerData");
		createAddressData();

		getEntityTransaction().begin();
		try {
			// logger.log(Logger.Level.TRACE,"Create " + NUMOFCOUNTRIES + " countries");
			country[0] = new Country("United States", "USA");
			country[1] = new Country("United States", "USA");
			country[2] = new Country("United States", "USA");
			country[3] = new Country("United States", "USA");
			country[4] = new Country("United States", "USA");
			country[5] = new Country("United States", "USA");
			country[6] = new Country("United States", "USA");
			country[7] = new Country("United States", "USA");
			country[8] = new Country("United States", "USA");
			country[9] = new Country("United States", "USA");
			country[10] = new Country("England", "GBR");
			country[11] = new Country("Ireland", "IRE");
			country[12] = new Country("China", "CHA");
			country[13] = new Country("Japan", "JPN");
			country[14] = new Country("United States", "USA");
			country[15] = new Country("England", "GBR");
			country[16] = new Country("Ireland", "IRE");
			country[17] = new Country("China", "CHA");
			country[18] = new Country("China", "CHA");
			country[19] = new Country("China", "CHA");

			// logger.log(Logger.Level.TRACE,"persisting customer entities");
			customerRef[0] = new Customer("1", "Alan E. Frechette", address[0], address[1], country[0]);
			customerRef[1] = new Customer("2", "Arthur D. Frechette", address[2], address[3], country[1]);
			customerRef[2] = new Customer("3", "Shelly D. McGowan", address[4], address[5], country[2]);
			customerRef[3] = new Customer("4", "Robert E. Bissett", address[6], address[7], country[3]);
			customerRef[4] = new Customer("5", "Stephen S. D'Milla", address[8], address[9], country[4]);
			customerRef[5] = new Customer("6", "Karen R. Tegan", address[10], address[11], country[5]);
			customerRef[6] = new Customer("7", "Stephen J. Caruso", address[12], address[13], country[6]);
			customerRef[7] = new Customer("8", "Irene M. Caruso", address[14], address[15], country[7]);
			customerRef[8] = new Customer("9", "William P. Keaton", address[16], address[17], country[8]);
			customerRef[9] = new Customer("10", "Kate P. Hudson", address[18], address[19], country[9]);
			customerRef[10] = new Customer("11", "Jonathan K. Smith", address[20], address[21], country[10]);
			customerRef[11] = new Customer("12", null, address[22], address[23], country[11]);
			customerRef[12] = new Customer("13", "Douglas A. Donahue", address[24], address[25], country[12]);
			customerRef[13] = new Customer("14", "Kellie A. Sanborn", address[26], address[27], country[13]);
			customerRef[14] = new Customer("15", "Margaret Mills", address[28], address[29], country[14]);
			customerRef[15] = new Customer("16", "Sonya C. Sanders", address[30], address[31], country[15]);
			customerRef[16] = new Customer("17", "Jack B. Grace", address[32], address[33], country[16]);
			customerRef[17] = new Customer("18", "Ron F. Bender", address[34], address[35], country[17]);
			customerRef[18] = new Customer("19", "Lisa M. Presley", country[18]);
			customerRef[19] = new Customer("20", " David R. Vincent", country[19]);

			for (Customer c : customerRef) {
				if (c != null) {
					getEntityManager().persist(c);
					logger.log(Logger.Level.TRACE, "persisting customer " + c);
					doFlush();
				}
			}

			info[0] = new Info();
			info[0].setId("1");
			info[0].setStreet("634 Goldstar Road");
			info[0].setCity("Peabody");
			info[0].setState("MA");
			info[0].setZip("88444");
			info[1] = new Info();
			info[1].setId("2");
			info[1].setStreet("3212 Boston Road");
			info[1].setCity("Chelmsford");
			info[1].setState("MA");
			info[1].setZip("01824");
			info[2] = new Info();
			info[2].setId("3");
			info[2].setStreet("47 Skyline Drive");
			info[2].setCity("Attleboro");
			info[2].setState("MA");
			info[2].setZip("76656");
			info[3] = new Info();
			info[3].setId("4");
			info[3].setStreet(null);
			info[3].setCity("Belmont");
			info[3].setState("VT");
			info[3].setZip("23083");
			info[4] = new Info();
			info[4].setId("5");
			info[4].setStreet("212 Edgewood Drive");
			info[4].setCity("Claremont");
			info[4].setState("NH");
			info[4].setZip("58976");
			info[5] = new Info();
			info[5].setId("6");
			info[5].setStreet("11 Richmond Lane");
			info[5].setCity("Chatham");
			info[5].setState("NJ");
			info[5].setZip("65490");

			// logger.log(Logger.Level.TRACE,"Create " + NUMOFSPOUSES + " spouses");
			spouse[0] = new Spouse("1", "Kathleen", "Jones", "Porter", "034-58-0988", info[0], customerRef[6]);
			spouse[1] = new Spouse("2", "Judith", "Connors", "McCall", "074-22-6431", info[1], customerRef[10]);
			spouse[2] = new Spouse("3", "Linda", "Kelly", "Morrison", "501-22-5940", info[2], customerRef[12]);
			spouse[3] = new Spouse("4", "Thomas", null, "Mullen", "210-23-3456", info[3], customerRef[9]);
			spouse[4] = new Spouse("5", "Mitchell", null, "Jackson", "476-44-3349", info[4], customerRef[11]);
			spouse[5] = new Spouse("6", "Cynthia", "White", "Allen", "508-908-7765", info[5]);

			for (Spouse s : spouse) {
				if (s != null) {
					getEntityManager().persist(s);
					logger.log(Logger.Level.TRACE, "persisting spouse " + s);
					doFlush();
				}
			}

			customerRef[6].setSpouse(spouse[0]);
			getEntityManager().merge(customerRef[6]);
			customerRef[9].setSpouse(spouse[3]);
			getEntityManager().merge(customerRef[9]);
			customerRef[10].setSpouse(spouse[1]);
			getEntityManager().merge(customerRef[10]);
			customerRef[11].setSpouse(spouse[4]);
			getEntityManager().merge(customerRef[11]);
			customerRef[12].setSpouse(spouse[2]);
			getEntityManager().merge(customerRef[12]);
			doFlush();

			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createCustomerData failed:", e);
		}

	}

	public void createAliasOnlyData() throws Exception {
		logger.log(Logger.Level.TRACE, "createAliasOnlyData");
		getEntityTransaction().begin();
		try {
			aliasRef[0] = new Alias("1", "aef");
			aliasRef[1] = new Alias("2", "al");
			aliasRef[2] = new Alias("3", "fish");
			aliasRef[3] = new Alias("4", "twin");
			aliasRef[4] = new Alias("5", "adf");
			aliasRef[5] = new Alias("6", "art");
			aliasRef[6] = new Alias("7", "sdm");
			aliasRef[7] = new Alias("8", "sh_ll");
			aliasRef[8] = new Alias("9", "reb");
			aliasRef[9] = new Alias("10", "bobby");
			aliasRef[10] = new Alias("11", "bb");
			aliasRef[11] = new Alias("12", "ssd");
			aliasRef[12] = new Alias("13", "steved");
			aliasRef[13] = new Alias("14", "stevie");
			aliasRef[14] = new Alias("15", "");
			aliasRef[15] = new Alias("16", "");
			aliasRef[16] = new Alias("17", "sjc");
			aliasRef[17] = new Alias("18", "stevec");
			aliasRef[18] = new Alias("19", "imc");
			aliasRef[19] = new Alias("20", "iris");
			aliasRef[20] = new Alias("21", "bro");
			aliasRef[21] = new Alias("22", "sis");
			aliasRef[22] = new Alias("23", "kell");
			aliasRef[23] = new Alias("24", "bill");
			aliasRef[24] = new Alias("25", "suzy");
			aliasRef[25] = new Alias("26", "jon");
			aliasRef[26] = new Alias("27", "jk");
			aliasRef[27] = new Alias("28", "kellieann");
			aliasRef[28] = new Alias("29", "smitty");
			aliasRef[29] = new Alias("30", null);

			for (Alias a : aliasRef) {
				if (a != null) {
					getEntityManager().persist(a);
					logger.log(Logger.Level.TRACE, "persisting alias " + a);
					doFlush();
				}
			}

			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createAliasOnlyData failed:", e);
		}

	}

	public void createAliasData() throws Exception {
		logger.log(Logger.Level.TRACE, "createAliasData");
		createAliasOnlyData();
		getEntityTransaction().begin();
		try {

			aliasRef[0].getCustomers().add(customerRef[0]);
			getEntityManager().merge(aliasRef[0]);

			aliasRef[1].getCustomers().add(customerRef[0]);
			getEntityManager().merge(aliasRef[1]);

			aliasRef[2].getCustomers().add(customerRef[0]);
			getEntityManager().merge(aliasRef[2]);

			aliasRef[3].getCustomers().add(customerRef[0]);
			getEntityManager().merge(aliasRef[3]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 2");
			aliasRef[2].getCustomers().add(customerRef[1]);
			getEntityManager().merge(aliasRef[2]);

			aliasRef[3].getCustomers().add(customerRef[1]);
			getEntityManager().merge(aliasRef[3]);

			aliasRef[4].getCustomers().add(customerRef[1]);
			getEntityManager().merge(aliasRef[4]);

			aliasRef[5].getCustomers().add(customerRef[1]);
			getEntityManager().merge(aliasRef[5]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 3");
			aliasRef[6].getCustomers().add(customerRef[2]);
			getEntityManager().merge(aliasRef[6]);

			aliasRef[7].getCustomers().add(customerRef[2]);
			getEntityManager().merge(aliasRef[7]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 4");
			aliasRef[8].getCustomers().add(customerRef[3]);
			getEntityManager().merge(aliasRef[8]);

			aliasRef[9].getCustomers().add(customerRef[3]);
			getEntityManager().merge(aliasRef[9]);

			aliasRef[10].getCustomers().add(customerRef[3]);
			getEntityManager().merge(aliasRef[10]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 5");
			aliasRef[11].getCustomers().add(customerRef[4]);
			getEntityManager().merge(aliasRef[11]);

			aliasRef[12].getCustomers().add(customerRef[4]);
			getEntityManager().merge(aliasRef[12]);

			aliasRef[13].getCustomers().add(customerRef[4]);
			getEntityManager().merge(aliasRef[13]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 7");
			aliasRef[13].getCustomers().add(customerRef[6]);
			getEntityManager().merge(aliasRef[13]);

			aliasRef[16].getCustomers().add(customerRef[6]);
			getEntityManager().merge(aliasRef[16]);

			aliasRef[17].getCustomers().add(customerRef[6]);
			getEntityManager().merge(aliasRef[17]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 8");
			aliasRef[18].getCustomers().add(customerRef[7]);
			getEntityManager().merge(aliasRef[18]);

			aliasRef[19].getCustomers().add(customerRef[7]);
			getEntityManager().merge(aliasRef[19]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 9");
			aliasRef[23].getCustomers().add(customerRef[8]);
			getEntityManager().merge(aliasRef[23]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 10");
			aliasRef[21].getCustomers().add(customerRef[9]);
			getEntityManager().merge(aliasRef[21]);

			aliasRef[29].getCustomers().add(customerRef[9]);
			getEntityManager().merge(aliasRef[29]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 11");
			aliasRef[25].getCustomers().add(customerRef[10]);
			getEntityManager().merge(aliasRef[25]);

			aliasRef[26].getCustomers().add(customerRef[10]);
			getEntityManager().merge(aliasRef[26]);

			aliasRef[28].getCustomers().add(customerRef[10]);
			getEntityManager().merge(aliasRef[28]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 12");
			aliasRef[24].getCustomers().add(customerRef[11]);
			getEntityManager().merge(aliasRef[24]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 13");
			aliasRef[20].getCustomers().add(customerRef[12]);
			getEntityManager().merge(aliasRef[20]);

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 14");
			aliasRef[22].getCustomers().add(customerRef[13]);
			getEntityManager().merge(aliasRef[22]);

			aliasRef[27].getCustomers().add(customerRef[13]);
			getEntityManager().merge(aliasRef[27]);
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createAliasData failed:", e);
		}

	}

	public void createOrderData() throws Exception {
		logger.log(Logger.Level.TRACE, "createOrderData");
		getEntityTransaction().begin();
		double totalPrice;

		try {

			// logger.log(Logger.Level.TRACE,"persisting creditcard entities");

			creditCard[0] = new CreditCard("1", "1234-2567-1222-9999", "VISA", "04/02", true, (double) 5579);
			creditCard[1] = new CreditCard("2", "3455-9876-1221-0060", "MCARD", "10/03", false, (double) 15000);
			creditCard[2] = new CreditCard("3", "1210-1449-2200-3254", "AXP", "11/02", true, (double) 3000);
			creditCard[3] = new CreditCard("4", "0002-1221-0078-0890", "VISA", "05/03", true, (double) 8000);
			creditCard[4] = new CreditCard("5", "1987-5555-8733-0011", "VISA", "05/03", true, (double) 2500);
			creditCard[5] = new CreditCard("6", "0000-0011-2200-3087", "MCARD", "11/02", true, (double) 23000);
			creditCard[6] = new CreditCard("7", "3341-7610-8880-9910", "AXP", "10/04", true, (double) 13000);
			creditCard[7] = new CreditCard("8", "2222-3333-4444-5555", "MCARD", "12/03", true, (double) 2000);
			creditCard[8] = new CreditCard("9", "8888-2222-0090-1348", "AXP", "01/02", true, (double) 4500);
			creditCard[9] = new CreditCard("10", "1762-5094-8769-3117", "VISA", "06/01", true, (double) 14000);
			creditCard[10] = new CreditCard("11", "1234-1234-1234-9999", "MCARD", "09/03", true, (double) 7000);
			creditCard[11] = new CreditCard("12", "9876-9876-1234-5678", "VISA", "04/04", false, (double) 1000);
			creditCard[12] = new CreditCard("13", "7777-8888-9999-0012", "MCARD", "01/02", true, (double) 3500);
			creditCard[13] = new CreditCard("14", "9099-8808-7718-4455", "AXP", "03/05", true, (double) 4400);
			creditCard[14] = new CreditCard("15", "7653-7901-2397-1768", "AXP", "02/04", true, (double) 5000);
			creditCard[15] = new CreditCard("16", "8760-8618-9263-3322", "VISA", "04/05", false, (double) 750);
			creditCard[16] = new CreditCard("17", "9870-2309-6754-3210", "MCARD", "03/03", true, (double) 500);
			creditCard[17] = new CreditCard("18", "8746-8754-9090-1234", "AXP", "08/04", false, (double) 1500);
			creditCard[18] = new CreditCard("19", "8736-0980-8765-4869", "MCARD", "09/02", true, (double) 5500);
			creditCard[19] = new CreditCard("20", "6745-0979-0970-2345", "VISA", "02/05", true, (double) 1400);
			creditCard[20] = new CreditCard("21", "8033-5896-9901-4566", "AXP", "09/07", true, (double) 400);
			creditCard[21] = new CreditCard("22", "4390-5671-4385-0091", "MCARD", "03/06", false, (double) 7400);
			creditCard[22] = new CreditCard("23", "3456-0909-3434-2134", "VISA", "04/08", true, (double) 9500);
			creditCard[23] = new CreditCard("24", "5643-2090-4569-2323", "MCARD", "01/06", false, (double) 1000);

			for (CreditCard c : creditCard) {
				if (c != null) {
					getEntityManager().persist(c);
					logger.log(Logger.Level.TRACE, "persisting creditCard " + c);
					doFlush();
				}
			}
			// logger.log(Logger.Level.TRACE,"persisting creditCard entities");

			lineItem[0] = new LineItem("1", 1);
			lineItem[1] = new LineItem("2", 1);
			lineItem[2] = new LineItem("3", 1);
			lineItem[3] = new LineItem("4", 1);
			lineItem[4] = new LineItem("5", 1);
			lineItem[5] = new LineItem("6", 1);
			lineItem[6] = new LineItem("7", 1);
			lineItem[7] = new LineItem("8", 1);
			lineItem[8] = new LineItem("9", 1);
			lineItem[9] = new LineItem("10", 1);
			lineItem[10] = new LineItem("11", 1);
			lineItem[11] = new LineItem("12", 1);
			lineItem[12] = new LineItem("13", 1);
			lineItem[13] = new LineItem("14", 1);
			lineItem[14] = new LineItem("15", 1);
			lineItem[15] = new LineItem("16", 1);
			lineItem[16] = new LineItem("17", 1);
			lineItem[17] = new LineItem("18", 1);
			lineItem[18] = new LineItem("19", 1);
			lineItem[19] = new LineItem("20", 1);
			lineItem[20] = new LineItem("21", 1);
			lineItem[21] = new LineItem("22", 1);
			lineItem[22] = new LineItem("23", 1);
			lineItem[23] = new LineItem("24", 1);
			lineItem[24] = new LineItem("25", 1);
			lineItem[25] = new LineItem("26", 1);
			lineItem[26] = new LineItem("27", 1);
			lineItem[27] = new LineItem("28", 1);
			lineItem[28] = new LineItem("29", 1);
			lineItem[29] = new LineItem("30", 5);
			lineItem[30] = new LineItem("31", 3);
			lineItem[31] = new LineItem("32", 8);
			lineItem[32] = new LineItem("33", 1);
			lineItem[33] = new LineItem("34", 1);
			lineItem[34] = new LineItem("35", 6);
			lineItem[35] = new LineItem("36", 1);
			lineItem[36] = new LineItem("37", 2);
			lineItem[37] = new LineItem("38", 3);
			lineItem[38] = new LineItem("39", 5);
			lineItem[39] = new LineItem("40", 3);
			lineItem[40] = new LineItem("41", 2);
			lineItem[41] = new LineItem("42", 1);
			lineItem[42] = new LineItem("43", 1);
			lineItem[43] = new LineItem("44", 3);
			lineItem[44] = new LineItem("45", 1);
			lineItem[45] = new LineItem("46", 2);
			lineItem[46] = new LineItem("47", 3);
			lineItem[47] = new LineItem("48", 3);
			lineItem[48] = new LineItem("49", 4);
			lineItem[49] = new LineItem("50", 5);
			lineItem[50] = new LineItem("51", 2);
			lineItem[51] = new LineItem("52", 1);
			lineItem[52] = new LineItem("53", 3);
			lineItem[53] = new LineItem("54", 1);
			lineItem[54] = new LineItem("55", 3);
			lineItem[55] = new LineItem("56", 1);

			for (LineItem l : lineItem) {
				if (l != null) {
					getEntityManager().persist(l);
					logger.log(Logger.Level.TRACE, "persisting lineItem " + l);
					doFlush();
				}
			}

			// logger.log(Logger.Level.TRACE,"persisting customer entities ");

			// logger.log(Logger.Level.TRACE,"Create " + NUMOFORDERS + " Orders");
			orderRef[0] = new Order("1", customerRef[0]);
			orderRef[1] = new Order("2", customerRef[1]);
			orderRef[2] = new Order("3", customerRef[2]);
			orderRef[3] = new Order("4", customerRef[3]);
			orderRef[4] = new Order("5", customerRef[4]);
			orderRef[5] = new Order("6", customerRef[5]);
			orderRef[6] = new Order("7", customerRef[6]);
			orderRef[7] = new Order("8", customerRef[7]);
			orderRef[8] = new Order("9", customerRef[3]);
			orderRef[9] = new Order("10", customerRef[8]);
			orderRef[10] = new Order("11", customerRef[9]);
			orderRef[11] = new Order("12", customerRef[10]);
			orderRef[12] = new Order("13", customerRef[11]);
			orderRef[13] = new Order("14", customerRef[12]);
			orderRef[14] = new Order("15", customerRef[13]);
			orderRef[15] = new Order("16", customerRef[13]);
			orderRef[16] = new Order("17", customerRef[14]);
			orderRef[17] = new Order("18", customerRef[15]);
			orderRef[18] = new Order("19");
			orderRef[19] = new Order("20");

			for (Order o : orderRef) {
				if (o != null) {
					getEntityManager().persist(o);
					logger.log(Logger.Level.TRACE, "persisting order " + o);
					doFlush();
				}
			}

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 1");
			lineItem[0].setProduct(productRef[0]);
			lineItem[0].setOrder(orderRef[0]);
			getEntityManager().merge(lineItem[0]);

			lineItem[1].setProduct(productRef[1]);
			lineItem[1].setOrder(orderRef[0]);
			getEntityManager().merge(lineItem[1]);

			lineItem[2].setProduct(productRef[7]);
			lineItem[2].setOrder(orderRef[0]);
			getEntityManager().merge(lineItem[2]);

			lineItem[28].setProduct(productRef[8]);
			lineItem[28].setOrder(orderRef[0]);
			getEntityManager().merge(lineItem[28]);

			orderRef[0].getLineItemsCollection().add(lineItem[0]);
			orderRef[0].getLineItemsCollection().add(lineItem[1]);
			orderRef[0].getLineItemsCollection().add(lineItem[2]);
			orderRef[0].setSampleLineItem(lineItem[28]);
			totalPrice = productRef[0].getPrice() + productRef[1].getPrice() + productRef[7].getPrice()
					+ productRef[8].getPrice();
			orderRef[0].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[0]);

			creditCard[1].setOrder(orderRef[0]);
			creditCard[1].setCustomer(customerRef[0]);
			getEntityManager().merge(creditCard[1]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done with Order 1 relationships");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 2");
			lineItem[3].setProduct(productRef[0]);
			lineItem[3].setOrder(orderRef[1]);
			getEntityManager().merge(lineItem[3]);

			lineItem[4].setProduct(productRef[1]);
			lineItem[4].setOrder(orderRef[1]);
			getEntityManager().merge(lineItem[4]);

			lineItem[5].setProduct(productRef[2]);
			lineItem[5].setOrder(orderRef[1]);
			getEntityManager().merge(lineItem[5]);

			lineItem[6].setProduct(productRef[3]);
			lineItem[6].setOrder(orderRef[1]);
			getEntityManager().merge(lineItem[6]);

			lineItem[7].setProduct(productRef[4]);
			lineItem[7].setOrder(orderRef[1]);
			getEntityManager().merge(lineItem[7]);

			orderRef[1].getLineItemsCollection().add(lineItem[3]);
			orderRef[1].getLineItemsCollection().add(lineItem[4]);
			orderRef[1].getLineItemsCollection().add(lineItem[5]);
			orderRef[1].getLineItemsCollection().add(lineItem[6]);
			orderRef[1].getLineItemsCollection().add(lineItem[7]);
			totalPrice = productRef[0].getPrice() + productRef[1].getPrice() + productRef[2].getPrice()
					+ productRef[3].getPrice() + productRef[4].getPrice();
			orderRef[1].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[1]);

			creditCard[3].setOrder(orderRef[1]);
			creditCard[3].setCustomer(customerRef[1]);
			getEntityManager().merge(creditCard[3]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting relationships for Order 2");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 3");
			lineItem[8].setProduct(productRef[2]);
			lineItem[8].setOrder(orderRef[2]);
			getEntityManager().merge(lineItem[8]);

			lineItem[9].setProduct(productRef[5]);
			lineItem[9].setOrder(orderRef[2]);
			getEntityManager().merge(lineItem[9]);

			orderRef[2].getLineItemsCollection().add(lineItem[8]);
			orderRef[2].getLineItemsCollection().add(lineItem[9]);
			totalPrice = productRef[2].getPrice() + productRef[5].getPrice();
			orderRef[2].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[2]);

			creditCard[4].setOrder(orderRef[2]);
			creditCard[4].setCustomer(customerRef[2]);
			getEntityManager().merge(creditCard[4]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting Relationships for Order 3");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 4");
			lineItem[10].setProduct(productRef[6]);
			lineItem[10].setOrder(orderRef[3]);
			getEntityManager().merge(lineItem[10]);

			orderRef[3].getLineItemsCollection().add(lineItem[10]);
			totalPrice = productRef[6].getPrice();
			orderRef[3].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[3]);

			creditCard[5].setOrder(orderRef[3]);
			creditCard[5].setCustomer(customerRef[3]);
			getEntityManager().merge(creditCard[5]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting Relationships for Order 4");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 5");
			lineItem[11].setProduct(productRef[0]);
			lineItem[11].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[11]);

			lineItem[12].setProduct(productRef[1]);
			lineItem[12].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[12]);

			lineItem[13].setProduct(productRef[2]);
			lineItem[13].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[13]);

			lineItem[14].setProduct(productRef[3]);
			lineItem[14].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[14]);

			lineItem[15].setProduct(productRef[4]);
			lineItem[15].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[15]);

			lineItem[16].setProduct(productRef[5]);
			lineItem[16].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[16]);

			lineItem[17].setProduct(productRef[6]);
			lineItem[17].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[17]);

			lineItem[18].setProduct(productRef[7]);
			lineItem[18].setOrder(orderRef[4]);
			getEntityManager().merge(lineItem[18]);

			orderRef[4].getLineItemsCollection().add(lineItem[11]);
			orderRef[4].getLineItemsCollection().add(lineItem[12]);
			orderRef[4].getLineItemsCollection().add(lineItem[13]);
			orderRef[4].getLineItemsCollection().add(lineItem[14]);
			orderRef[4].getLineItemsCollection().add(lineItem[15]);
			orderRef[4].getLineItemsCollection().add(lineItem[16]);
			orderRef[4].getLineItemsCollection().add(lineItem[17]);
			orderRef[4].getLineItemsCollection().add(lineItem[18]);
			totalPrice = productRef[0].getPrice() + productRef[1].getPrice() + productRef[2].getPrice()
					+ productRef[3].getPrice() + productRef[4].getPrice() + productRef[5].getPrice()
					+ productRef[6].getPrice() + productRef[7].getPrice();
			orderRef[4].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[4]);

			creditCard[7].setOrder(orderRef[4]);
			creditCard[7].setCustomer(customerRef[4]);
			getEntityManager().merge(creditCard[7]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting Relationships for Order 5");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 6");
			lineItem[19].setProduct(productRef[3]);
			lineItem[19].setOrder(orderRef[5]);
			getEntityManager().merge(lineItem[19]);

			lineItem[20].setProduct(productRef[6]);
			lineItem[20].setOrder(orderRef[5]);
			getEntityManager().merge(lineItem[20]);

			lineItem[29].setProduct(productRef[8]);
			lineItem[29].setOrder(orderRef[5]);
			getEntityManager().merge(lineItem[29]);

			orderRef[5].getLineItemsCollection().add(lineItem[19]);
			orderRef[5].getLineItemsCollection().add(lineItem[20]);
			orderRef[5].setSampleLineItem(lineItem[29]);
			totalPrice = productRef[3].getPrice() + productRef[6].getPrice() + productRef[8].getPrice();
			orderRef[5].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[5]);

			creditCard[10].setOrder(orderRef[5]);
			creditCard[10].setCustomer(customerRef[5]);
			getEntityManager().merge(creditCard[10]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting Relationships for Order 6");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 7");
			lineItem[21].setProduct(productRef[2]);
			lineItem[21].setOrder(orderRef[6]);
			getEntityManager().merge(lineItem[21]);

			lineItem[22].setProduct(productRef[3]);
			lineItem[22].setOrder(orderRef[6]);
			getEntityManager().merge(lineItem[22]);

			lineItem[23].setProduct(productRef[7]);
			lineItem[23].setOrder(orderRef[6]);
			getEntityManager().merge(lineItem[23]);

			orderRef[6].getLineItemsCollection().add(lineItem[21]);
			orderRef[6].getLineItemsCollection().add(lineItem[22]);
			orderRef[6].getLineItemsCollection().add(lineItem[23]);
			totalPrice = productRef[2].getPrice() + productRef[3].getPrice() + productRef[7].getPrice();
			orderRef[6].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[6]);

			creditCard[11].setOrder(orderRef[6]);
			creditCard[11].setCustomer(customerRef[6]);
			getEntityManager().merge(creditCard[11]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order 7");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 8");
			lineItem[24].setProduct(productRef[0]);
			lineItem[24].setOrder(orderRef[7]);
			getEntityManager().merge(lineItem[24]);

			lineItem[25].setProduct(productRef[4]);
			lineItem[25].setOrder(orderRef[7]);
			getEntityManager().merge(lineItem[25]);

			orderRef[7].getLineItemsCollection().add(lineItem[24]);
			orderRef[7].getLineItemsCollection().add(lineItem[25]);
			totalPrice = productRef[0].getPrice() + productRef[4].getPrice();
			orderRef[7].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[7]);

			creditCard[13].setOrder(orderRef[7]);
			creditCard[13].setCustomer(customerRef[7]);
			getEntityManager().merge(creditCard[13]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order 8");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 9");
			lineItem[26].setProduct(productRef[0]);
			lineItem[26].setOrder(orderRef[8]);
			getEntityManager().merge(lineItem[26]);

			lineItem[27].setProduct(productRef[1]);
			lineItem[27].setOrder(orderRef[8]);
			getEntityManager().merge(lineItem[27]);

			orderRef[8].getLineItemsCollection().add(lineItem[26]);
			orderRef[8].getLineItemsCollection().add(lineItem[27]);
			totalPrice = productRef[0].getPrice() + productRef[1].getPrice();
			orderRef[8].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[8]);

			creditCard[6].setOrder(orderRef[8]);
			creditCard[6].setCustomer(customerRef[3]);
			getEntityManager().merge(creditCard[6]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order 9");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 10");
			lineItem[30].setProduct(productRef[9]);
			lineItem[30].setOrder(orderRef[9]);
			getEntityManager().merge(lineItem[30]);

			lineItem[31].setProduct(productRef[16]);
			lineItem[31].setOrder(orderRef[9]);
			getEntityManager().merge(lineItem[31]);

			orderRef[9].getLineItemsCollection().add(lineItem[30]);
			orderRef[9].getLineItemsCollection().add(lineItem[31]);
			totalPrice = productRef[9].getPrice() + productRef[16].getPrice();
			orderRef[9].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[9]);

			creditCard[14].setOrder(orderRef[9]);
			creditCard[14].setCustomer(customerRef[8]);
			getEntityManager().merge(creditCard[14]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 10");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 11");
			lineItem[32].setProduct(productRef[13]);
			lineItem[32].setOrder(orderRef[10]);
			getEntityManager().merge(lineItem[32]);

			orderRef[10].getLineItemsCollection().add(lineItem[32]);
			totalPrice = productRef[13].getPrice();
			orderRef[10].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[10]);

			creditCard[15].setOrder(orderRef[10]);
			creditCard[15].setCustomer(customerRef[9]);
			getEntityManager().merge(creditCard[15]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 11");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 12");
			lineItem[33].setProduct(productRef[10]);
			lineItem[33].setOrder(orderRef[11]);
			getEntityManager().merge(lineItem[33]);

			lineItem[34].setProduct(productRef[12]);
			lineItem[34].setOrder(orderRef[11]);
			getEntityManager().merge(lineItem[34]);

			orderRef[11].getLineItemsCollection().add(lineItem[33]);
			orderRef[11].getLineItemsCollection().add(lineItem[34]);
			totalPrice = productRef[10].getPrice() + productRef[12].getPrice();
			orderRef[11].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[11]);

			creditCard[16].setOrder(orderRef[11]);
			creditCard[16].setCustomer(customerRef[10]);
			getEntityManager().merge(creditCard[16]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 12");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 13");
			lineItem[35].setProduct(productRef[17]);
			lineItem[35].setOrder(orderRef[12]);
			getEntityManager().merge(lineItem[35]);

			orderRef[12].getLineItemsCollection().add(lineItem[35]);
			totalPrice = productRef[17].getPrice();
			orderRef[12].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[12]);

			creditCard[17].setOrder(orderRef[12]);
			creditCard[17].setCustomer(customerRef[11]);
			getEntityManager().merge(creditCard[17]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 13");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 14");
			lineItem[36].setProduct(productRef[7]);
			lineItem[36].setOrder(orderRef[13]);
			getEntityManager().merge(lineItem[36]);

			lineItem[37].setProduct(productRef[14]);
			lineItem[37].setOrder(orderRef[13]);
			getEntityManager().merge(lineItem[37]);

			lineItem[38].setProduct(productRef[15]);
			lineItem[38].setOrder(orderRef[13]);
			getEntityManager().merge(lineItem[38]);

			orderRef[13].getLineItemsCollection().add(lineItem[36]);
			orderRef[13].getLineItemsCollection().add(lineItem[37]);
			orderRef[13].getLineItemsCollection().add(lineItem[38]);
			totalPrice = productRef[7].getPrice() + productRef[14].getPrice() + productRef[15].getPrice();
			orderRef[13].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[13]);

			creditCard[18].setOrder(orderRef[13]);
			creditCard[18].setCustomer(customerRef[12]);
			getEntityManager().merge(creditCard[18]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 14");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 15");
			lineItem[39].setProduct(productRef[1]);
			lineItem[39].setOrder(orderRef[14]);
			getEntityManager().merge(lineItem[39]);

			lineItem[40].setProduct(productRef[2]);
			lineItem[40].setOrder(orderRef[14]);
			getEntityManager().merge(lineItem[40]);

			lineItem[41].setProduct(productRef[12]);
			lineItem[41].setOrder(orderRef[14]);
			getEntityManager().merge(lineItem[41]);

			lineItem[42].setProduct(productRef[15]);
			lineItem[42].setOrder(orderRef[14]);
			getEntityManager().merge(lineItem[42]);

			orderRef[14].getLineItemsCollection().add(lineItem[39]);
			orderRef[14].getLineItemsCollection().add(lineItem[40]);
			orderRef[14].getLineItemsCollection().add(lineItem[41]);
			orderRef[14].getLineItemsCollection().add(lineItem[42]);
			totalPrice = productRef[1].getPrice() + productRef[2].getPrice() + productRef[12].getPrice()
					+ productRef[15].getPrice();
			orderRef[14].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[14]);

			creditCard[19].setOrder(orderRef[14]);
			creditCard[19].setCustomer(customerRef[13]);
			getEntityManager().merge(creditCard[19]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 15");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 16");
			lineItem[43].setProduct(productRef[13]);
			lineItem[43].setOrder(orderRef[15]);
			getEntityManager().merge(lineItem[43]);

			orderRef[15].getLineItemsCollection().add(lineItem[43]);
			totalPrice = productRef[13].getPrice();
			orderRef[15].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[15]);

			creditCard[19].setOrder(orderRef[15]);
			creditCard[19].setCustomer(customerRef[13]);
			getEntityManager().merge(creditCard[19]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 16");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 17");
			lineItem[44].setProduct(hardwareRef[0]);
			lineItem[44].setOrder(orderRef[16]);
			getEntityManager().merge(lineItem[44]);

			lineItem[45].setProduct(hardwareRef[1]);
			lineItem[45].setOrder(orderRef[16]);
			getEntityManager().merge(lineItem[45]);

			lineItem[46].setProduct(softwareRef[0]);
			lineItem[46].setOrder(orderRef[16]);
			getEntityManager().merge(lineItem[46]);

			orderRef[16].getLineItemsCollection().add(lineItem[44]);
			orderRef[16].getLineItemsCollection().add(lineItem[45]);
			orderRef[16].getLineItemsCollection().add(lineItem[46]);
			totalPrice = hardwareRef[0].getPrice() + hardwareRef[1].getPrice() + softwareRef[0].getPrice();
			orderRef[16].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[16]);

			creditCard[20].setOrder(orderRef[16]);
			creditCard[20].setCustomer(customerRef[14]);
			getEntityManager().merge(creditCard[20]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 17");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 18");
			lineItem[47].setProduct(hardwareRef[2]);
			lineItem[47].setOrder(orderRef[17]);
			getEntityManager().merge(lineItem[47]);

			lineItem[48].setProduct(softwareRef[1]);
			lineItem[48].setOrder(orderRef[17]);
			getEntityManager().merge(lineItem[48]);

			lineItem[49].setProduct(hardwareRef[3]);
			lineItem[49].setOrder(orderRef[17]);
			getEntityManager().merge(lineItem[49]);

			lineItem[50].setProduct(softwareRef[2]);
			lineItem[50].setOrder(orderRef[17]);
			getEntityManager().merge(lineItem[50]);

			orderRef[17].getLineItemsCollection().add(lineItem[47]);
			orderRef[17].getLineItemsCollection().add(lineItem[48]);
			orderRef[17].getLineItemsCollection().add(lineItem[49]);
			orderRef[17].getLineItemsCollection().add(lineItem[50]);
			totalPrice = hardwareRef[2].getPrice() + hardwareRef[3].getPrice() + softwareRef[1].getPrice()
					+ softwareRef[2].getPrice();
			orderRef[17].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[17]);

			creditCard[21].setOrder(orderRef[17]);
			creditCard[21].setCustomer(customerRef[15]);
			getEntityManager().merge(creditCard[21]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 18");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 19");
			lineItem[51].setProduct(hardwareRef[4]);
			lineItem[51].setOrder(orderRef[18]);
			getEntityManager().merge(lineItem[51]);

			lineItem[52].setProduct(softwareRef[3]);
			lineItem[52].setOrder(orderRef[18]);
			getEntityManager().merge(lineItem[52]);

			lineItem[53].setProduct(softwareRef[4]);
			lineItem[53].setOrder(orderRef[18]);
			getEntityManager().merge(lineItem[53]);

			orderRef[18].getLineItemsCollection().add(lineItem[51]);
			orderRef[18].getLineItemsCollection().add(lineItem[52]);
			orderRef[18].getLineItemsCollection().add(lineItem[53]);
			totalPrice = hardwareRef[4].getPrice() + softwareRef[3].getPrice() + softwareRef[4].getPrice();
			orderRef[18].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[18]);

			creditCard[22].setOrder(orderRef[18]);
			creditCard[22].setCustomer(customerRef[16]);
			getEntityManager().merge(creditCard[22]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 19");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Order
			// 20");
			lineItem[54].setProduct(hardwareRef[5]);
			lineItem[54].setOrder(orderRef[19]);
			getEntityManager().merge(lineItem[54]);

			lineItem[55].setProduct(softwareRef[5]);
			lineItem[55].setOrder(orderRef[19]);
			getEntityManager().merge(lineItem[55]);

			orderRef[19].getLineItemsCollection().add(lineItem[54]);
			orderRef[19].getLineItemsCollection().add(lineItem[55]);
			totalPrice = hardwareRef[5].getPrice() + softwareRef[5].getPrice();
			orderRef[19].setTotalPrice((double) totalPrice);
			getEntityManager().merge(orderRef[19]);

			creditCard[23].setOrder(orderRef[19]);
			creditCard[23].setCustomer(customerRef[17]);
			getEntityManager().merge(creditCard[23]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"done Setting additional relationships for
			// Order
			// 20");

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 1");

			orderRef[0].setCustomer(customerRef[0]);
			getEntityManager().merge(orderRef[0]);

			creditCard[0].setCustomer(customerRef[0]);
			getEntityManager().merge(creditCard[0]);

			creditCard[1].setCustomer(customerRef[0]);
			getEntityManager().merge(creditCard[1]);

			creditCard[2].setCustomer(customerRef[0]);
			getEntityManager().merge(creditCard[2]);
			doFlush();

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 2");

			orderRef[1].setCustomer(customerRef[1]);
			getEntityManager().merge(orderRef[1]);

			creditCard[3].setCustomer(customerRef[1]);
			getEntityManager().merge(creditCard[3]);
			doFlush();

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 3");

			orderRef[2].setCustomer(customerRef[2]);
			getEntityManager().merge(orderRef[2]);

			creditCard[4].setCustomer(customerRef[2]);
			getEntityManager().merge(creditCard[4]);
			doFlush();

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 4");

			orderRef[3].setCustomer(customerRef[3]);
			getEntityManager().merge(orderRef[3]);

			creditCard[5].setCustomer(customerRef[3]);
			getEntityManager().merge(creditCard[5]);

			creditCard[6].setCustomer(customerRef[3]);
			getEntityManager().merge(creditCard[6]);
			doFlush();

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 5");

			orderRef[4].setCustomer(customerRef[4]);
			getEntityManager().merge(orderRef[4]);

			creditCard[7].setCustomer(customerRef[4]);
			getEntityManager().merge(creditCard[7]);

			creditCard[8].setCustomer(customerRef[4]);
			getEntityManager().merge(creditCard[8]);
			doFlush();

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 6");
			orderRef[5].setCustomer(customerRef[5]);
			getEntityManager().merge(orderRef[5]);

			creditCard[9].setCustomer(customerRef[5]);
			getEntityManager().merge(creditCard[9]);

			creditCard[10].setCustomer(customerRef[5]);
			getEntityManager().merge(creditCard[10]);
			doFlush();

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 7");

			orderRef[6].setCustomer(customerRef[6]);
			getEntityManager().merge(orderRef[6]);

			creditCard[11].setCustomer(customerRef[6]);
			getEntityManager().merge(creditCard[11]);

			doFlush();

			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 8");

			orderRef[7].setCustomer(customerRef[7]);
			getEntityManager().merge(orderRef[7]);

			creditCard[12].setCustomer(customerRef[7]);
			getEntityManager().merge(creditCard[12]);

			creditCard[13].setCustomer(customerRef[7]);
			getEntityManager().merge(creditCard[13]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 9");;

			orderRef[9].setCustomer(customerRef[8]);
			getEntityManager().merge(orderRef[9]);

			creditCard[14].setCustomer(customerRef[8]);
			getEntityManager().merge(creditCard[14]);

			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 10");

			orderRef[10].setCustomer(customerRef[9]);
			getEntityManager().merge(orderRef[10]);

			creditCard[15].setCustomer(customerRef[9]);
			getEntityManager().merge(creditCard[15]);

			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 11");

			orderRef[11].setCustomer(customerRef[10]);
			getEntityManager().merge(orderRef[11]);

			creditCard[16].setCustomer(customerRef[10]);
			getEntityManager().merge(creditCard[16]);

			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 12");

			orderRef[12].setCustomer(customerRef[11]);
			getEntityManager().merge(orderRef[12]);

			creditCard[17].setCustomer(customerRef[11]);
			getEntityManager().merge(creditCard[17]);

			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 13");

			orderRef[13].setCustomer(customerRef[12]);
			getEntityManager().merge(orderRef[13]);

			creditCard[18].setCustomer(customerRef[12]);
			getEntityManager().merge(creditCard[18]);

			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 14");

			orderRef[14].setCustomer(customerRef[13]);
			getEntityManager().merge(orderRef[14]);

			orderRef[15].setCustomer(customerRef[13]);
			getEntityManager().merge(orderRef[15]);

			creditCard[19].setCustomer(customerRef[13]);
			getEntityManager().merge(creditCard[19]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 15");
			// No Aliases
			orderRef[16].setCustomer(customerRef[14]);
			getEntityManager().merge(orderRef[16]);

			creditCard[20].setCustomer(customerRef[14]);
			getEntityManager().merge(creditCard[20]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 16");
			// No Aliases
			orderRef[17].setCustomer(customerRef[15]);
			getEntityManager().merge(orderRef[17]);

			creditCard[21].setCustomer(customerRef[15]);
			getEntityManager().merge(creditCard[21]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 17");
			// No Aliases
			orderRef[18].setCustomer(customerRef[16]);
			getEntityManager().merge(orderRef[18]);

			creditCard[22].setCustomer(customerRef[16]);
			getEntityManager().merge(creditCard[22]);
			doFlush();
			// logger.log(Logger.Level.TRACE,"setting additional relationships for Customer
			// 18");
			// No Aliases
			orderRef[19].setCustomer(customerRef[17]);
			getEntityManager().merge(orderRef[19]);

			creditCard[23].setCustomer(customerRef[17]);
			getEntityManager().merge(creditCard[23]);
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createOrderData failed:", e);
		}
	}

	public void createDepartmentEmployeeData() throws Exception {
		logger.log(Logger.Level.TRACE, "createDepartmentEmployeeData");
		getEntityTransaction().begin();

		try {
			logger.log(Logger.Level.TRACE, "Create 2 Departments");
			deptRef[0] = new Department(1, "Marketing");
			deptRef[1] = new Department(2, "Administration");

			logger.log(Logger.Level.TRACE, "Start to persist departments ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department " + dept.getName());
				}
			}

			logger.log(Logger.Level.TRACE, "Create 5 employees");
			empRef[0] = new Employee(1, "Alan", "Frechette");
			empRef[0].setDepartment(deptRef[0]);

			empRef[1] = new Employee(2, "Arthur", "Frechette");
			empRef[1].setDepartment(deptRef[1]);

			empRef[2] = new Employee(3, "Shelly", "McGowan");
			empRef[2].setDepartment(deptRef[0]);

			empRef[3] = new Employee(4, "Robert", "Bissett");
			empRef[3].setDepartment(deptRef[1]);

			empRef[4] = new Employee(5, "Stephen", "DMilla");
			empRef[4].setDepartment(deptRef[0]);

			Map<String, Employee> link = new HashMap<String, Employee>();
			link.put(empRef[0].getLastName(), empRef[0]);
			link.put(empRef[2].getLastName(), empRef[2]);
			link.put(empRef[4].getLastName(), empRef[4]);
			deptRef[0].setLastNameEmployees(link);

			Map<String, Employee> link1 = new HashMap<String, Employee>();
			link1.put(empRef[1].getLastName(), empRef[1]);
			link1.put(empRef[3].getLastName(), empRef[3]);
			deptRef[1].setLastNameEmployees(link1);

			logger.log(Logger.Level.TRACE, "Start to persist employees ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted employee " + emp.getId());
				}
			}
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createDepartmentEmployeeData failed:", e);
		}
	}

	public void createPhoneData() throws Exception {
		logger.log(Logger.Level.TRACE, "createPhoneData");
		getEntityTransaction().begin();

		try {

			phone[0] = new Phone("1", "617", "664-8122", address[0]);
			phone[1] = new Phone("2", "781", "442-8122", address[1]);
			phone[2] = new Phone("3", "508", "662-7117", address[2]);
			phone[3] = new Phone("4", "781", "442-4488", address[3]);
			phone[4] = new Phone("5", "992", "223-8888", address[4]);
			phone[5] = new Phone("6", "781", "442-1134", address[5]);

			phone[6] = new Phone("7", "442", "883-1597", address[6]);
			phone[7] = new Phone("8", "781", "442-6699", address[7]);
			phone[8] = new Phone("9", "603", "777-7890", address[8]);
			phone[9] = new Phone("10", "781", "442-2323", address[9]);
			phone[10] = new Phone("11", "603", "889-2355", address[10]);

			phone[11] = new Phone("12", "781", "442-9876", address[11]);
			phone[12] = new Phone("13", "222", "767-3124", address[12]);
			phone[13] = new Phone("14", "781", "442-1111", address[13]);
			phone[14] = new Phone("15", "222", "767-8898", address[14]);
			phone[15] = new Phone("16", "781", "442-4444", address[15]);

			phone[16] = new Phone("17", null, "564-9087", address[16]);
			phone[17] = new Phone("18", "781", "442-5341", address[17]);
			phone[18] = new Phone("19", null, null, address[18]);
			phone[19] = new Phone("20", "781", "442-1585", address[19]);
			phone[20] = new Phone("21", "207", "532-6354", address[20]);

			phone[21] = new Phone("22", "781", "442-0845", address[21]);
			phone[22] = new Phone("23", "913", null, address[22]);
			phone[23] = new Phone("24", "781", "442-7465", address[23]);
			phone[24] = new Phone("25", "678", "663-6091", address[24]);
			phone[25] = new Phone("26", "781", "442-2139", address[25]);

			phone[26] = new Phone("27", "890", "670-9138", address[26]);
			phone[27] = new Phone("28", "781", "442-0230", address[27]);
			phone[28] = new Phone("29", "450", "876-9087", address[28]);
			phone[29] = new Phone("30", "781", "442-6766", address[29]);
			phone[30] = new Phone("31", "908", "458-0980", address[30]);

			phone[31] = new Phone("32", "781", "442-6251", address[31]);
			phone[32] = new Phone("33", "432", "435-0909", address[32]);
			phone[33] = new Phone("34", "781", "442-8790", address[33]);
			phone[34] = new Phone("35", "415", "355-9008", address[34]);
			phone[35] = new Phone("36", "781", "442-2879", address[35]);

			for (Phone p : phone) {
				if (p != null) {
					getEntityManager().persist(p);
					logger.log(Logger.Level.TRACE, "persisting phone " + p);
					doFlush();
				}
			}
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createPhoneData failed:", e);
		}
	}

	public void createCriteriaEntityData() throws Exception {
		logger.log(Logger.Level.TRACE, "createCriteriaEntityData");
		getEntityTransaction().begin();

		try {

			criteriaEntity[0] = new CriteriaEntity(1L, "Left", null, null, null, null, null);
			criteriaEntity[1] = new CriteriaEntity(2L, "right", null, null, null, null, null);
			criteriaEntity[2] = new CriteriaEntity(3L, "LeftToken", "TokenRight", null, null, null, null);
			criteriaEntity[3] = new CriteriaEntity(4L, null, null, null, LocalTime.of(10, 11, 12), null, null);
			criteriaEntity[4] = new CriteriaEntity(5L, null, null, null, null, LocalDate.of(1918, 9, 28), null);

			for (CriteriaEntity c : criteriaEntity) {
				if (c != null) {
					getEntityManager().persist(c);
					logger.log(Logger.Level.TRACE, "persisting CriteriaEntity " + c);
					doFlush();
				}
			}
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createCriteriaEntityData failed:", e);
		}
	}

	public void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM FKS_ANOOP_CNOOP").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM FKS_ALIAS_CUSTOMER").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ALIAS_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CREDITCARD_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM SPOUSE_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM INFO_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PHONE_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("UPDATE ORDER_TABLE SET FK4_FOR_CUSTOMER_TABLE= NULL").executeUpdate();
			getEntityManager()
					.createNativeQuery("UPDATE LINEITEM_TABLE SET FK_FOR_PRODUCT_TABLE= NULL, FK1_FOR_ORDER_TABLE=NULL")
					.executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUSTOMER_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ADDRESS").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_DETAILS").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ORDER_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM LINEITEM_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM TRIM_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CRITERIA_TEST_TABLE").executeUpdate();
			getEntityTransaction().commit();
			logger.log(Logger.Level.TRACE, "done removeTestData");

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

	public abstract JavaArchive createDeployment() throws Exception;

}
