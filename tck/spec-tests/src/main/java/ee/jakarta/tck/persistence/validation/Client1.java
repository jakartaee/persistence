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

package ee.jakarta.tck.persistence.validation;

import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.common.validation.PersistenceTckValidatorFactory;
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.groups.Default;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System.Logger;

import static ee.jakarta.tck.persistence.common.validation.PersistenceTckValidatorFactory.validCall;

public class Client1 extends PMClientBase {

    private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

    public Client1() {
    }

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client1.class.getPackageName();
        String pkgName = pkgNameWithoutSuffix + ".";
        String[] classes = {pkgName + "Order1"};
        return createDeploymentJar("jpa_se_validation.jar", pkgNameWithoutSuffix, (String[]) classes);

    }

    @BeforeEach
    public void setupNoData() throws Exception {
        logger.log(Logger.Level.TRACE, "setupNoData");
        try {
            super.setup();
            createDeployment();
            removeTestData();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "Exception: ", e);
            throw new Exception("Setup failed:", e);
        }
    }

    @AfterEach
    public void cleanup() throws Exception {
        try {
            logger.log(Logger.Level.TRACE, "cleanupData");
            removeTestData();
        } finally {
            removeTestJarFromCP();
        }
    }

    @Test
    public void callbackValidationDefaultOnInsertEntityManager() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));
        }
    }

    @Test
    public void callbackValidationDefaultOnUpdateEntityManager() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
            // on insert:
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));

            emf.runInTransaction(em -> {
                Order1 forUpdate = em.find(Order1.class, entity.getId());
                forUpdate.setTotal(forUpdate.getTotal() + 100);
            });
            // on update:
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));
        }
    }

    @Test
    public void callbackValidationDefaultOnInsertEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.insert(entity);
            });
            // on insert:
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));
        }
    }

    @Test
    public void callbackValidationDefaultOnUpsertEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.upsert(entity);
            });
            // on upsert:
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));
        }
    }

    @Test
    public void callbackValidationDefaultOnUpdateEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.insert(entity);
            });
            // on insert:
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));

            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                Order1 forUpdate = entityAgent.get(Order1.class, entity.getId());
                forUpdate.setTotal(forUpdate.getTotal() + 100);
                entityAgent.update(forUpdate);
            });
            // on update:
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));
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
