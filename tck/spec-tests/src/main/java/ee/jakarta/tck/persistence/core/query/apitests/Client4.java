/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.query.apitests;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System.Logger;
import java.sql.Date;

public class Client4 extends PMClientBase {

    private static final Logger logger = System.getLogger(Client4.class.getName());

    private final Employee empRef[] = new Employee[21];

    private final Date d1 = getSQLDate("2000-02-14");

    final Department deptRef[] = new Department[5];

    public Client4() {
    }

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client4.class.getPackageName();
        String pkgName = pkgNameWithoutSuffix + ".";
        String[] classes = {pkgName + "DataTypes2", pkgName + "Department", pkgName + "Employee",
                pkgName + "Insurance"};
        return createDeploymentJar("jpa_core_query_apitests4.jar", pkgNameWithoutSuffix, classes);

    }

    @BeforeEach
    public void setupDataTypes2() throws Exception {
        logger.log(Logger.Level.TRACE, "setup");
        try {
            super.setup();
            createDeployment();
            removeTestData();
            createTestData();
            logger.log(Logger.Level.TRACE, "Done creating test data");
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected Exception caught in Setup: ", e);
            throw new Exception("Setup failed:", e);

        }
    }

    @AfterEach
    public void cleanupNoData() throws Exception {
        try {
            logger.log(Logger.Level.TRACE, "in cleanupNoData");
            super.cleanup();
        } finally {
            removeTestJarFromCP();
        }
    }

    /*
     * BEGIN Test Cases
     */

    /*
     * @testName: queryAPITest28
     *
     * @assertion_ids: PERSISTENCE:SPEC:527;
     *
     * @test_Strategy: Verify Query.getSingleResultOrNull() with value as a return
     *
     */
    @Test
    public void queryAPIGetSingleResultOrNullWithValueTest() throws Exception {
        final int ID = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        try {
            getEntityTransaction().begin();

            Query query = getEntityManager().createQuery("select d from Department d where d.id = :fId");
            query.setParameter("fId", ID);
            Department result = (Department) query.getSingleResultOrNull();

            if (result != null) {
                pass1 = true;
                logger.log(Logger.Level.TRACE, "Received expected result (not null).");
            } else {
                logger.log(Logger.Level.ERROR, "No any result received.");
            }

            if (deptRef[ID - 1].equals(result)) {
                pass2 = true;
                logger.log(Logger.Level.TRACE, "Received expected result: |" + result + "|");
            } else {
                logger.log(Logger.Level.ERROR, "No expected result received.");
            }

            getEntityTransaction().commit();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

        } finally {
            try {
                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
            } catch (Exception re) {
                logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
            }
        }

        if (!pass1 || !pass2) {
            throw new Exception("queryAPIGetSingleResultOrNullWithValueTest failed");
        }
    }

    /*
     * @testName: queryAPITest28
     *
     * @assertion_ids: PERSISTENCE:SPEC:527;
     *
     * @test_Strategy: Verify Query.getSingleResultOrNull() with null as a return
     *
     */
    @Test
    public void queryAPIGetSingleResultOrNullNullValueTest() throws Exception {
        final int ID = 0;
        boolean pass1 = false;

        try {
            getEntityTransaction().begin();

            Query query = getEntityManager().createQuery("select d from Department d where d.id = :fId");
            query.setParameter("fId", ID);
            Department result = (Department) query.getSingleResultOrNull();

            if (result == null) {
                pass1 = true;
                logger.log(Logger.Level.TRACE, "Received expected result (null).");
            } else {
                logger.log(Logger.Level.ERROR, "Not null result received: |" + result + "|");
            }

            getEntityTransaction().commit();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

        } finally {
            try {
                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
            } catch (Exception re) {
                logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
            }
        }

        if (!pass1) {
            throw new Exception("queryAPIGetSingleResultOrNullNullValueTest failed");
        }
    }

    /*
     * @testName: queryAPITest28
     *
     * @assertion_ids: PERSISTENCE:SPEC:527;
     *
     * @test_Strategy: Verify Query.getSingleResultOrNull() with value as a return
     *
     */
    @Test
    public void typedQueryAPIGetSingleResultOrNullWithValueTest() throws Exception {
        final int ID = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        try {
            getEntityTransaction().begin();

            TypedQuery<Department> typedQuery = getEntityManager().createQuery("select d from Department d where d.id = :fId", Department.class);
            typedQuery.setParameter("fId", ID);
            Department result = typedQuery.getSingleResultOrNull();

            if (result != null) {
                pass1 = true;
                logger.log(Logger.Level.TRACE, "Received expected result (not null).");
            } else {
                logger.log(Logger.Level.ERROR, "No any result received.");
            }

            if (deptRef[ID - 1].equals(result)) {
                pass2 = true;
                logger.log(Logger.Level.TRACE, "Received expected result: |" + result + "|");
            } else {
                logger.log(Logger.Level.ERROR, "No expected result received.");
            }

            getEntityTransaction().commit();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

        } finally {
            try {
                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
            } catch (Exception re) {
                logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
            }
        }

        if (!pass1 || !pass2) {
            throw new Exception("typedQueryAPIGetSingleResultOrNullWithValueTest failed");
        }
    }

    /*
     * @testName: queryAPITest28
     *
     * @assertion_ids: PERSISTENCE:SPEC:527;
     *
     * @test_Strategy: Verify Query.getSingleResultOrNull() with null as a return
     *
     */
    @Test
    public void typedQueryAPIGetSingleResultOrNullNullValueTest() throws Exception {
        final int ID = 0;
        boolean pass1 = false;

        try {
            getEntityTransaction().begin();

            TypedQuery<Department> typedQuery = getEntityManager().createQuery("select d from Department d where d.id = :fId", Department.class);
            typedQuery.setParameter("fId", ID);
            Department result = typedQuery.getSingleResultOrNull();

            if (result == null) {
                pass1 = true;
                logger.log(Logger.Level.TRACE, "Received expected result (null).");
            } else {
                logger.log(Logger.Level.ERROR, "Not null result received: |" + result + "|");
            }

            getEntityTransaction().commit();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

        } finally {
            try {
                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
            } catch (Exception re) {
                logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
            }
        }

        if (!pass1) {
            throw new Exception("typedQueryAPIGetSingleResultOrNullNullValueTest failed");
        }
    }

    private void createTestData() throws Exception {
        logger.log(Logger.Level.TRACE, "createTestData");

        final Insurance insRef[] = new Insurance[5];
        final Date d2 = getSQLDate("2001-06-27");
        final Date d3 = getSQLDate("2002-07-07");
        final Date d4 = getSQLDate("2003-03-03");
        final Date d5 = getSQLDate("2004-04-10");
        final Date d6 = getSQLDate("2005-02-18");
        final Date d7 = getSQLDate("2000-09-17");
        final Date d8 = getSQLDate("2001-11-14");
        final Date d9 = getSQLDate("2002-10-04");
        final Date d10 = getSQLDate("2003-01-25");
        final Date d11 = getSQLDate();

        try {

            getEntityTransaction().begin();

            // logger.log(Logger.Level.TRACE,"Create 5 Departments");
            deptRef[0] = new Department(1, "Engineering");
            deptRef[1] = new Department(2, "Marketing");
            deptRef[2] = new Department(3, "Sales");
            deptRef[3] = new Department(4, "Accounting");
            deptRef[4] = new Department(5, "Training");

            logger.log(Logger.Level.TRACE, "Start to persist departments ");
            for (Department d : deptRef) {
                if (d != null) {
                    getEntityManager().persist(d);
                    logger.log(Logger.Level.TRACE, "persisted department " + d);
                }
            }

            // logger.log(Logger.Level.TRACE,"Create 3 Insurance Carriers");
            insRef[0] = new Insurance(1, "Prudential");
            insRef[1] = new Insurance(2, "Cigna");
            insRef[2] = new Insurance(3, "Sentry");

            logger.log(Logger.Level.TRACE, "Start to persist insurance ");
            for (Insurance i : insRef) {
                if (i != null) {
                    getEntityManager().persist(i);
                    logger.log(Logger.Level.TRACE, "persisted insurance " + i);
                }
            }

            // logger.log(Logger.Level.TRACE,"Create 20 employees");
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

            empRef[20] = new Employee(21, "Jane", "Smmith", d11, 60000.0F);
            empRef[20].setDepartment(deptRef[0]);
            empRef[20].setInsurance(insRef[2]);

            // logger.log(Logger.Level.TRACE,"Start to persist employees ");
            for (Employee e : empRef) {
                if (e != null) {
                    getEntityManager().persist(e);
                    logger.log(Logger.Level.TRACE, "persisted employee " + e);
                }
            }

            getEntityTransaction().commit();
            logger.log(Logger.Level.TRACE, "Created TestData");

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

    private void removeTestData() {
        logger.log(Logger.Level.TRACE, "removeTestData");
        if (getEntityTransaction().isActive()) {
            getEntityTransaction().rollback();
        }
        try {
            getEntityTransaction().begin();
            getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
            getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
            getEntityManager().createNativeQuery("DELETE FROM INSURANCE").executeUpdate();
            getEntityManager().createNativeQuery("DELETE FROM DATATYPES2").executeUpdate();
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
