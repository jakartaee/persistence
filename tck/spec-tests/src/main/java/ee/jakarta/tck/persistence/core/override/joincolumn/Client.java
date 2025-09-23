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

package ee.jakarta.tck.persistence.core.override.joincolumn;

import java.lang.System.Logger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {
	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long COMPANY_ID = 676l;

	private static final String COMPANY_NAME = "Regal";

	private static final long LOCATION_ID = 42l;

	private static final String LOCATION_CODE = "KXTN";

	private static final long CITY_ID = 47l;

	private static final String CITY_CODE = "ICIO";

	private static final int STUDENT1_ID = 420;

	private static final String STUDENT1_NAME = "Charsoubees";

	private static final int STUDENT2_ID = 421;

	private static final String STUDENT2_NAME = "CharsouIkkis";

	private static final int STUDENT3_ID = 422;

	private static final String STUDENT3_NAME = "CharsouBais";

	private static final int STUDENT4_ID = 423;

	private static final String STUDENT4_NAME = "CharsouTeis";

	private static final int STUDENT5_ID = 424;

	private static final String STUDENT5_NAME = "CharsouChoubees";

	private static final int MATH_ID = 104;

	private static final String MATH_COURSE = "Math";

	private static final int CHEM_ID = 204;

	private static final String CHEM_COURSE = "Chemistry";

	private static final int Hardware1_ID = 9030;

	private static final String Hardware1_CODE = "Connoi-LapTop43";

	private static final int Hardware2_ID = 9031;

	private static final String Hardware2_CODE = "Connoi-DeskTop02";

	private static final int CUBICLE1_ID = 199;

	private static final String CUBICLE1_NAME = "Z678";

	private static final long ORDER1_ID = 786l;

	private static final long ORDER2_ID = 787l;

	private static final long ORDER3_ID = 788l;

	private static final long ORDER4_ID = 789l;

	private static final double COST1 = 53;

	private static final double COST2 = 540;

	private static final double COST3 = 155;

	private static final double COST4 = 256;

	private static final long CUST1_ID = 2l;

	private static final long CUST2_ID = 4l;

	private static final String CUST1_NAME = "Ross";

	private static final String CUST2_NAME = "Joey";

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { ORM_XML };
		String[] classes = { pkgName + "Course", pkgName + "Cubicle", pkgName + "CubiclePK", pkgName + "Customer1",
				pkgName + "Hardware", pkgName + "RetailOrder1", pkgName + "Student", pkgName + "TheatreCompany1",
				pkgName + "TheatreLocation1" };
		return createDeploymentJar("jpa_core_override_joincolumn.jar", pkgNameWithoutSuffix, classes, xmlFiles);

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
	 * @testName: testNoJoinColumnAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039;
	 * PERSISTENCE:SPEC:1040; PERSISTENCE:SPEC:1041; PERSISTENCE:SPEC:1042;
	 * PERSISTENCE:SPEC:1043; PERSISTENCE:SPEC:1044; PERSISTENCE:SPEC:1045;
	 * PERSISTENCE:SPEC:1046; PERSISTENCE:SPEC:1048; PERSISTENCE:SPEC:1097;
	 * PERSISTENCE:SPEC:1214; PERSISTENCE:SPEC:1243;
	 * 
	 * @test_Strategy: The two entities "TheatreCompany1" and "Theatre Location1"
	 * have Many-to-One relationship and are joined by the primary key. The
	 * relationships are specified in orm.xml rather than using annotations.
	 * 
	 */
	@Test
	public void testNoJoinColumnAnnotation() throws Exception {

		TheatreCompany1 regal = createTheatreCompany(COMPANY_ID, COMPANY_NAME);
		TheatreLocation1 knoxville = createTheatreLocation(LOCATION_ID, LOCATION_CODE);
		TheatreLocation1 iowacity = createTheatreLocation(CITY_ID, CITY_CODE);
		knoxville.setCompany(regal);
		iowacity.setCompany(regal);
		Set<TheatreLocation1> regalLocations = new HashSet();
		regalLocations.add(knoxville);
		regalLocations.add(iowacity);
		regal.setLocations(regalLocations);
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(regal);
			getEntityManager().flush();
			getEntityManager().persist(knoxville);
			getEntityManager().flush();
			getEntityManager().persist(iowacity);
			getEntityManager().flush();
			List result = getEntityManager().createQuery("SELECT l FROM TheatreLocation1 l").getResultList();
			if (result.size() == 2) {
				logger.log(Logger.Level.TRACE, "testNoJoinColumnAnnotation passed");
			} else {
				throw new Exception("Expected the size to be 1 " + " but it is -" + result.size());
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNoJoinColumnAnnotation" + e);
		} finally {
			getEntityManager().remove(regal);
			getEntityManager().remove(knoxville);
			getEntityManager().remove(iowacity);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testNoJoinTableAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039;
	 * PERSISTENCE:SPEC:1040; PERSISTENCE:SPEC:1041; PERSISTENCE:SPEC:1042;
	 * PERSISTENCE:SPEC:1046; PERSISTENCE:SPEC:1048; PERSISTENCE:SPEC:1097;
	 * PERSISTENCE:SPEC:1214; PERSISTENCE:SPEC:1243;
	 * 
	 * @test_Strategy: The two entities "Course" and "Student" have Many-to-Many
	 * relationship. The relationship and the join table are specified in orm.xml
	 * rather than using annotations.
	 * 
	 */
	@Test
	public void testNoJoinTableAnnotation() throws Exception {

		Course mathCourse = createCourse(MATH_ID, MATH_COURSE);
		Course chemCourse = createCourse(CHEM_ID, CHEM_COURSE);
		Student student1 = createStudent(STUDENT1_ID, STUDENT1_NAME);
		student1.addCourse(mathCourse);
		Student student2 = createStudent(STUDENT2_ID, STUDENT2_NAME);
		student2.addCourse(mathCourse);
		Student student3 = createStudent(STUDENT3_ID, STUDENT3_NAME);
		student3.addCourse(chemCourse);
		Student student4 = createStudent(STUDENT4_ID, STUDENT4_NAME);
		student4.addCourse(mathCourse);
		Student student5 = createStudent(STUDENT5_ID, STUDENT5_NAME);
		student5.addCourse(chemCourse);

		Set mathStudents = new HashSet();
		mathStudents.add(student1);
		mathStudents.add(student2);
		mathStudents.add(student4);
		Set chemStudents = new HashSet();
		chemStudents.add(student3);
		chemStudents.add(student5);

		mathCourse.setStudents(mathStudents);
		chemCourse.setStudents(chemStudents);
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(mathCourse);
			getEntityManager().flush();
			getEntityManager().persist(chemCourse);
			getEntityManager().flush();
			logger.log(Logger.Level.TRACE, "testNoJoinTableAnnotation passed");
		} catch (Exception e) {

			throw new Exception(" Test failed -" + e);
		} finally {
			getEntityManager().remove(mathCourse);
			getEntityManager().remove(chemCourse);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testOverrideJoinColumns
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039;
	 * PERSISTENCE:SPEC:1040; PERSISTENCE:SPEC:1041; PERSISTENCE:SPEC:1042;
	 * PERSISTENCE:SPEC:1046; PERSISTENCE:SPEC:1048; PERSISTENCE:SPEC:1097;
	 * PERSISTENCE:SPEC:1214; PERSISTENCE:SPEC:1243; PERSISTENCE:SPEC:2032;
	 * 
	 * @test_Strategy: "Cubicle" is an entity with a primary key class "CubiclePK".
	 * Hardware is an entity that uses the primary key columns of Cubicle. These
	 * primary key columns are overriden in Orm.xml. The following test to check the
	 * above.
	 * 
	 */
	@Test
	public void testOverrideJoinColumns() throws Exception {

		Hardware equipment1 = new Hardware();
		equipment1.setId(Hardware1_ID);
		equipment1.setSalesCode(Hardware1_CODE);

		Hardware equipment2 = new Hardware();
		equipment2.setId(Hardware2_ID);
		equipment2.setSalesCode(Hardware2_CODE);

		Cubicle cubicle = new Cubicle();
		cubicle.setId(CUBICLE1_ID);
		cubicle.setLocation(CUBICLE1_NAME);

		equipment1.setCubicle(cubicle);
		equipment2.setCubicle(cubicle);
		cubicle.addEquipment(equipment1);
		cubicle.addEquipment(equipment2);
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(cubicle);
			getEntityManager().flush();
			getEntityManager().persist(equipment1);
			getEntityManager().flush();
			getEntityManager().persist(equipment2);
			getEntityManager().flush();
			logger.log(Logger.Level.TRACE, "Test Passed");
		} catch (Exception e) {

			throw new Exception("test failed" + e);
		} finally {
			getEntityManager().remove(cubicle);
			getEntityManager().remove(equipment1);
			getEntityManager().remove(equipment2);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testOverrideJoinTable
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039;
	 * PERSISTENCE:SPEC:1040; PERSISTENCE:SPEC:1041; PERSISTENCE:SPEC:1042;
	 * PERSISTENCE:SPEC:1046; PERSISTENCE:SPEC:1048; PERSISTENCE:SPEC:1097;
	 * PERSISTENCE:SPEC:1214; PERSISTENCE:SPEC:1243; PERSISTENCE:JAVADOC:97
	 * 
	 * @test_Strategy: The two entities "Customer1" and "RetailOrder1" have
	 * One-to-Many relationship. The relationship and the join table names are
	 * overriden in orm.xml.
	 * 
	 */
	@Test
	public void testOverrideJoinTable() throws Exception {

		Customer1 customer1 = createCustomer(CUST1_ID, CUST1_NAME);
		Customer1 customer2 = createCustomer(CUST2_ID, CUST2_NAME);

		RetailOrder1 order1 = createOrder(ORDER1_ID, COST1);
		RetailOrder1 order2 = createOrder(ORDER2_ID, COST2);
		RetailOrder1 order3 = createOrder(ORDER3_ID, COST3);
		RetailOrder1 order4 = createOrder(ORDER4_ID, COST4);

		customer1.addOrder(order1);
		customer1.addOrder(order2);
		customer2.addOrder(order3);
		customer2.addOrder(order4);
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(order1);
			getEntityManager().flush();
			getEntityManager().persist(order2);
			getEntityManager().flush();
			getEntityManager().persist(order3);
			getEntityManager().flush();
			getEntityManager().persist(order4);
			getEntityManager().flush();
			getEntityManager().persist(customer1);
			getEntityManager().flush();
			getEntityManager().persist(customer2);
			getEntityManager().flush();
			logger.log(Logger.Level.TRACE, "Test Passed");
		} catch (Exception e) {

			throw new Exception("Test failed" + e);
		} finally {
			getEntityManager().remove(order1);
			getEntityManager().remove(order2);
			getEntityManager().remove(order3);
			getEntityManager().remove(order4);
			getEntityManager().remove(customer1);
			getEntityManager().remove(customer2);
			getEntityTransaction().commit();
		}

	}

	private TheatreLocation1 createTheatreLocation(long id, String code) {
		TheatreLocation1 loc = new TheatreLocation1();
		loc.setId(id);
		loc.setCode(code);
		return loc;
	}

	private TheatreCompany1 createTheatreCompany(final long id, final String name) {
		TheatreCompany1 company = new TheatreCompany1();
		company.setId(id);
		company.setName(name);
		return company;
	}

	private Student createStudent(final int id, final String name) {
		Student student = new Student();
		student.setId(id);
		student.setName(name);
		return student;
	}

	private Course createCourse(final int id, final String courseName) {
		Course course = new Course();
		course.setId(id);
		course.setName(courseName);
		return course;
	}

	private RetailOrder1 createOrder(final long id, final double cost) {
		RetailOrder1 order = new RetailOrder1();
		order.setId(id);
		order.setCost(cost);
		return order;
	}

	private Customer1 createCustomer(final long id, final String name) {
		Customer1 customer = new Customer1();
		customer.setId(id);
		customer.setName(name);
		return customer;
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
			getEntityManager().createNativeQuery("DELETE FROM COURSE_2").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUBICLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUSTOMER1").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUST_ORDER").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM HARDWARE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM RETAILORDER1").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM STUDENT_2").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM STUDENT_2_COURSE_2").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM THEATRECOMPANY1").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM THEATRELOCATION1").executeUpdate();
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
