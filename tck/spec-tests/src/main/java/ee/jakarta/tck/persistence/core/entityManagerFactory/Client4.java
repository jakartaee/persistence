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

package ee.jakarta.tck.persistence.core.entityManagerFactory;

import ee.jakarta.tck.persistence.common.PMClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System.Logger;

public class Client4 extends PMClientBase {

    private static final Logger logger = System.getLogger(Client4.class.getName());

    public Client4() {
    }

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client4.class.getPackageName();
        String pkgName = pkgNameWithoutSuffix + ".";
        String[] classes = {pkgName + "Member_", pkgName + "Member", pkgName + "Order_", pkgName + "Order"};
        return createDeploymentJar("jpa_core_entityManagerFactory4.jar", pkgNameWithoutSuffix, classes);

    }

    @BeforeEach
    public void setupMember() throws Exception {
        logger.log(Logger.Level.TRACE, "setup");
        try {
            super.setup();
            createDeployment();
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

    @Test
    public void callInTransactionTest() throws Exception {
        final int MEMBER_ID = 10;

        boolean pass = false;
        try {
            Member member = getEntityManager().getEntityManagerFactory().callInTransaction(em -> {
                Member newMember = new Member(MEMBER_ID, String.valueOf(MEMBER_ID));
                em.persist(newMember);
                return newMember;
            });
            Member foundMember = getEntityManager().find(Member.class, MEMBER_ID);
            if (member.equals(foundMember)) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "Stored entity data are not same as found");
            }
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("callInTransactionTest failed");
        }
    }

    @Test
    public void runInTransactionTest() throws Exception {
        final int MEMBER_ID = 11;

        boolean pass = false;
        try {
            getEntityManager().getEntityManagerFactory().runInTransaction(em -> {
                Member newMember = new Member(MEMBER_ID, String.valueOf(MEMBER_ID));
                em.persist(newMember);
            });
            Member foundMember = getEntityManager().find(Member.class, MEMBER_ID);
            if (foundMember != null) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "Stored entity data was not found");
            }
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("runInTransactionTest failed");
        }
    }

    @Test
    public void getNameTest() throws Exception {

        boolean pass = false;
        try {
            String puName = getEntityManager().getEntityManagerFactory().getName();
            if (getPersistenceUnitName().equals(puName)) {
                pass = true;
            } else {
                logger.log(Logger.Level.ERROR, "Persistence unit name |" + puName + "| doesn't match with expected |" + getPersistenceUnitName() + "|");
            }
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
        }
        if (!pass) {
            throw new Exception("getNameTest failed");
        }
    }
}
