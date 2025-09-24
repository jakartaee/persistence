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

package ee.jakarta.tck.persistence.core.entityManager2;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.FindOption;
import jakarta.persistence.LockModeType;
import jakarta.persistence.RefreshOption;
import jakarta.persistence.Timeout;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Client3 extends PMClientBase {

    private static final Logger logger = System.getLogger(Client3.class.getName());

    Order[] orders = new Order[5];

    Map map = new HashMap<String, Object>();

    String dataBaseName = null;

    public Client3() {
    }

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client3.class.getPackageName();
        String pkgName = pkgNameWithoutSuffix + ".";
        String[] classes = {pkgName + "DoesNotExist", pkgName + "Employee", pkgName + "Order"};
        return createDeploymentJar("jpa_core_entityManager3.jar", pkgNameWithoutSuffix, classes);

    }

    /*
     * setupOrderData() is called before each test
     *
     * @class.setup_props: jdbc.db;
     */
    @BeforeEach
    public void setupOrderData() throws Exception {
        logger.log(Logger.Level.TRACE, "setupOrderData");
        try {
            super.setup();
            createDeployment();
            removeTestData();
            createOrderData();
            map.putAll(getEntityManager().getProperties());
            map.put("foo", "bar");
            displayMap(map);
            dataBaseName = System.getProperty("jdbc.db");
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Exception: ", e);
            throw new Exception("Setup failed:", e);
        }
    }

    @AfterEach
    public void cleanupData() throws Exception {
        try {
            logger.log(Logger.Level.TRACE, "Cleanup data");
            removeTestData();
            cleanup();
        } finally {
            removeTestJarFromCP();
        }
    }

    @Test
    public void getReferenceForExistingEntityTest() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        try {
            getEntityTransaction().begin();
            Order order = new Order(1, 111, "desc1");
            Order reference = getEntityManager().getReference(order);
            // Verify that access to entity attribute works
            String orderDescription = reference.getdescription();
            getEntityTransaction().commit();

            if (reference instanceof Order) {
                pass1 = true;
            } else {
                logger.log(Logger.Level.ERROR, "Fetched entity is not same as expected.");
            }
            if (orderDescription != null) {
                pass2 = true;
            } else {
                logger.log(Logger.Level.ERROR, "Access to entity attribute doesn't work.");
            }

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
        if (!pass1 || !pass2) {
            throw new Exception("getReferenceForExistingEntityTest failed");
        }
    }

    @Test
    public void getReferenceForNonExistingEntityTest() throws Exception {
        boolean pass = false;
        try {
            Order order = new Order(0, 000, "desc0");
            // Verify that getReference() to order fails
            // EntityNotFoundException shall be thrown on non-existing entity access
            Order referenceOrder = getEntityManager().getReference(order);
            String description = referenceOrder.getdescription();
        } catch (EntityNotFoundException enfe) {
            pass = true;
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("getReferenceForNonExistingEntityTest failed");
        }
    }

    @Test
    public void runWithConnectionTest() throws Exception {
        boolean pass = false;
        Order newOrder = new Order(50, 5555, "desc55");
        try {
            getEntityTransaction().begin();
            getEntityManager().<Connection>runWithConnection(
                    connection -> {
                        try (PreparedStatement stmt = connection.prepareStatement(
                                "INSERT INTO PURCHASE_ORDER(ID, TOTAL, DESCRIPTION) VALUES(?, ?, ?)")) {
                            stmt.setInt(1, newOrder.getId());
                            stmt.setInt(2, newOrder.getTotal());
                            stmt.setString(3, newOrder.getdescription());
                            stmt.executeUpdate();
                        }
                    }
            );
            getEntityTransaction().commit();
            Order foundOrder = getEntityManager().find(Order.class, newOrder.getId());
            if (foundOrder.equals(newOrder)) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "Fetched entity is not same as expected.");
            }
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
        if (!pass) {
            throw new Exception("runWithConnectionTest failed");
        }
    }

    @Test
    public void callWithConnectionTest() throws Exception {
        boolean pass = false;
        Order newOrder = new Order(60, 6666, "desc66");
        try {
            getEntityTransaction().begin();
            Order selectedOrder = getEntityManager().<Connection, Order>callWithConnection(
                    connection -> {
                        try (PreparedStatement stmt = connection.prepareStatement(
                                "INSERT INTO PURCHASE_ORDER(ID, TOTAL, DESCRIPTION) VALUES(?, ?, ?)")) {
                            stmt.setInt(1, newOrder.getId());
                            stmt.setInt(2, newOrder.getTotal());
                            stmt.setString(3, newOrder.getdescription());
                            stmt.executeUpdate();
                        }
                        Order order = new Order();
                        try (PreparedStatement stmt = connection.prepareStatement(
                                "SELECT * FROM PURCHASE_ORDER WHERE ID = ?")) {
                            stmt.setInt(1, newOrder.getId());
                            stmt.execute();
                            ResultSet rSet = stmt.getResultSet();
                            rSet.next();
                            order.setId(rSet.getInt(1));
                            order.setTotal(rSet.getInt(2));
                            order.setdescription(rSet.getString(3));
                            rSet.close();
                        }
                        return order;
                    }
            );
            getEntityTransaction().commit();
            Order foundOrder = getEntityManager().find(Order.class, newOrder.getId());
            if (foundOrder.equals(selectedOrder)) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "Fetched entity is not same as expected.");
            }
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
        if (!pass) {
            throw new Exception("callWithConnectionTest failed");
        }
    }

    @Test
    public void findOptionsTest() throws Exception {
        boolean pass = false;
        try {
            FindOption[] findOptions = new FindOption[]{CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE};
            Order foundOrder = getEntityManager().find(Order.class, orders[0].getId(), findOptions);
            if (foundOrder.equals(orders[0])) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "Fetched entity is not same as expected.");
            }
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("findOptionsTest failed");
        }
    }

    @Test
    public void refreshOptionsTest() throws Exception {
        boolean pass = false;
        try {
            Order order = getEntityManager().find(Order.class, orders[0].getId());
            RefreshOption[] refreshOptions = new RefreshOption[]{CacheStoreMode.BYPASS, LockModeType.NONE, Timeout.seconds(10)};
            getEntityManager().refresh(order, refreshOptions);
            if (order.equals(orders[0])) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "Fetched entity is not same as expected.");
            }
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("refreshOptionsTest failed");
        }
    }

    @Test
    public void setCacheRetrieveModeTest() throws Exception {
        boolean pass = false;
        try {
            EntityManager em = getEntityManager();
            em.setCacheRetrieveMode(CacheRetrieveMode.USE);
            if (CacheRetrieveMode.USE == em.getCacheRetrieveMode()) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "CacheRetrieveMode property is not set.");
            }
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("setCacheRetrieveModeTest failed");
        }
    }

    @Test
    public void setCacheStoreModeTest() throws Exception {
        boolean pass = false;
        try {
            EntityManager em = getEntityManager();
            em.setCacheStoreMode(CacheStoreMode.BYPASS);
            if (CacheStoreMode.BYPASS == em.getCacheStoreMode()) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "CacheStoreMode property is not set.");
            }
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("setCacheStoreModeTest failed");
        }
    }


    private void createOrderData() {

        try {
            getEntityTransaction().begin();
            logger.log(Logger.Level.INFO, "Creating Orders");
            orders[0] = new Order(1, 111, "desc1");
            orders[1] = new Order(2, 222, "desc2");
            orders[2] = new Order(3, 333, "desc3");
            orders[3] = new Order(4, 444, "desc4");
            orders[4] = new Order(5, 555, "desc5");
            for (Order o : orders) {
                logger.log(Logger.Level.TRACE, "Persisting order:" + o.toString());
                getEntityManager().persist(o);
            }
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
            getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
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
