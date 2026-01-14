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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System.Logger;

public class Client2 extends PMClientBase {

    private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

    public Client2() {
    }

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client2.class.getPackageName();
        String pkgName = pkgNameWithoutSuffix + ".";
        String[] classes = {pkgName + "Order2"};
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
    public void callbackValidationNoGroupsOnInsertEntityManager() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_INSERT, "");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order2 entity = new Order2(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
            validatorFactory.assertNoValidationCalls();
        }
    }

    @Test
    public void callbackValidationNoGroupsOnInsertEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_INSERT, "");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order2 entity = new Order2(1, 111);
            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.insert(entity);
            });
            validatorFactory.assertNoValidationCalls();
        }
    }

    /*
     * by removing the validation groups from the predefined events we expect no validation to be performed
     */
    @Test
    public void callbackValidationNoGroupsEntityManager() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_INSERT, "");
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPDATE, "");
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPSERT, "");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order2 entity = new Order2(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
            validatorFactory.assertNoValidationCalls();
            Order2 detached = emf.callInTransaction(em -> {
                Order2 order2 = em.find(Order2.class, entity.getId());
                order2.setTotal(order2.getTotal() + 100);
                return order2;
            });
            emf.runInTransaction(em -> {
                em.merge(detached);
                detached.setTotal(detached.getTotal() + 100);
            });
            validatorFactory.assertNoValidationCalls();

            emf.runInTransaction(em -> {
                em.remove(em.getReference(Order2.class, entity.getId()));
            });
            validatorFactory.assertNoValidationCalls();
        }
    }

    @Test
    public void callbackValidationNoGroupsEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_INSERT, "");
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPDATE, "");
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPSERT, "");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order2 entity = new Order2(1, 111);
            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.insert(entity);
            });
            validatorFactory.assertNoValidationCalls();

            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entity.setTotal(entity.getTotal() + 100);
                entityAgent.upsert(entity);
            });
            validatorFactory.assertNoValidationCalls();

            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                Order2 forUpdate = entityAgent.get(Order2.class, entity.getId());
                forUpdate.setTotal(forUpdate.getTotal() + 100);
                entityAgent.update(forUpdate);
            });
            validatorFactory.assertNoValidationCalls();

            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.delete(entityAgent.get(Order2.class, entity.getId()));
            });
            validatorFactory.assertNoValidationCalls();
        }
    }

    @Test
    public void callbackValidationCustomGroupsEntityManager() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_PERSIST, PrePersistGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_MERGE, PreMergeGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_REMOVE, PreRemoveGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_INSERT, PreInsertGroup.class.getName() + "," + PreInsertOtherGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPDATE, PreUpdateGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPSERT, PreUpsertGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_DELETE, PreDeleteGroup.class.getName());
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order2 entity = new Order2(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PrePersistGroup.class));
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreInsertGroup.class, PreInsertOtherGroup.class));
            Order2 detached = emf.callInTransaction(em -> {
                Order2 order2 = em.find(Order2.class, entity.getId());
                order2.setTotal(order2.getTotal() + 100);
                return order2;
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreUpdateGroup.class));
            emf.runInTransaction(em -> {
                em.merge(detached);
                detached.setTotal(detached.getTotal() + 100);
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreMergeGroup.class));
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreUpdateGroup.class));

            emf.runInTransaction(em -> {
                em.remove(em.getReference(Order2.class, entity.getId()));
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreRemoveGroup.class));
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreDeleteGroup.class));
        }
    }

    @Test
    public void callbackValidationCustomGroupsEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_PERSIST, PrePersistGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_MERGE, PreMergeGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_REMOVE, PreRemoveGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_INSERT, PreInsertGroup.class.getName() + "," + PreInsertOtherGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPDATE, PreUpdateGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_UPSERT, PreUpsertGroup.class.getName());
        myProps.put(Persistence.ValidationProperties.VALIDATION_GROUP_PRE_DELETE, PreDeleteGroup.class.getName());
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order2 entity = new Order2(1, 111);
            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.insert(entity);
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreInsertGroup.class, PreInsertOtherGroup.class));

            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entity.setTotal(entity.getTotal() + 100);
                entityAgent.upsert(entity);
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreUpsertGroup.class));

            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                Order2 forUpdate = entityAgent.get(Order2.class, entity.getId());
                forUpdate.setTotal(forUpdate.getTotal() + 100);
                entityAgent.update(forUpdate);
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreUpdateGroup.class));

            emf.runInTransaction(EntityAgent.class, entityAgent -> {
                entityAgent.delete(entityAgent.get(Order2.class, entity.getId()));
            });
            validatorFactory.assertValidCallsContain(PersistenceTckValidatorFactory.validCall(entity, PreDeleteGroup.class));
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
