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
import jakarta.persistence.PersistenceException;
import jakarta.validation.groups.Default;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System.Logger;

import static ee.jakarta.tck.persistence.common.validation.PersistenceTckValidatorFactory.validCall;

public class Client3 extends PMClientBase {

    private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

    public Client3() {
    }

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client3.class.getPackageName();
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
    public void autoValidationModeWithSpecifiedValidationFactory() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "auto");
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
    public void autoValidationModeWithNoValidationFactory() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "auto");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
        }
        validatorFactory.assertNoValidationCalls();
    }

    @Test
    public void callbackValidationModeWithSpecifiedValidationFactory() throws Exception {
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
    public void callbackValidationModeWithNoValidationFactory() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
        } catch (PersistenceException pe) {
            validatorFactory.assertNoValidationCalls();
            return;
        }
        Assertions.fail("Persistence provider should've failed as no discoverable Validation factory is available!");
    }

    @Test
    public void noneValidationModeWithSpecifiedValidationFactory() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "none");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(em -> {
                em.persist(entity);
            });
            validatorFactory.assertNoValidationCalls();
        }
    }

    @Test
    public void autoValidationModeWithSpecifiedValidationFactoryEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "auto");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps);
             EntityAgent entityAgent = emf.createEntityAgent()) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class, em -> {
                em.insert(entity);
            });
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));
        }
    }

    @Test
    public void autoValidationModeWithNoValidationFactoryEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "auto");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class,em -> {
                em.insert(entity);
            });
        }
        validatorFactory.assertNoValidationCalls();
    }

    @Test
    public void callbackValidationModeWithSpecifiedValidationFactoryEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class,em -> {
                em.insert(entity);
            });
            validatorFactory.assertValidCallsContain(validCall(entity, Default.class));
        }
    }

    @Test
    public void callbackValidationModeWithNoValidationFactoryEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "callback");
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class,em -> {
                em.insert(entity);
            });
        } catch (PersistenceException pe) {
            validatorFactory.assertNoValidationCalls();
            return;
        }
        Assertions.fail("Persistence provider should've failed as no discoverable Validation factory is available!");
    }

    @Test
    public void noneValidationModeWithSpecifiedValidationFactoryEntityAgent() throws Exception {
        PersistenceTckValidatorFactory validatorFactory = new PersistenceTckValidatorFactory();
        myProps.put(Persistence.ValidationProperties.VALIDATION_MODE, "none");
        myProps.put(Persistence.ValidationProperties.VALIDATION_FACTORY, validatorFactory);
        displayMap(myProps);
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps)) {
            Order1 entity = new Order1(1, 111);
            emf.runInTransaction(EntityAgent.class,em -> {
                em.insert(entity);
            });
            validatorFactory.assertNoValidationCalls();
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
