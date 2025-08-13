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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ee.jakarta.tck.persistence.common.schema30.CriteriaEntity;
import ee.jakarta.tck.persistence.common.schema30.UtilCriteriaEntityData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.LocalDateField;
import jakarta.persistence.criteria.LocalTimeField;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public class Client9 extends UtilCriteriaEntityData {

    private static final System.Logger logger = System.getLogger(Client9.class.getName());

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client9.class.getPackageName();
        String[] classes = getSchema30classes();
        return createDeploymentJar("jpa_core_criteriaapi_CriteriaBuilder9.jar", pkgNameWithoutSuffix, classes);

    }

    // Verify case 0: in the implementation code.
    // JPA spec: This shall be always evaluated as true and all existing entities must be returned
    @SetupMethod(name = "setupTrimData")
    @Test
    public void andPredicateAsListOf0Test() throws Exception {
        boolean pass = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.select(root);
            cQuery.where(cb.and(Collections.emptyList()));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            List<CriteriaEntity> result = query.getResultList();
            if (criteriaEntity.length == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + criteriaEntity.length + "|, received size= |" + result.size() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass) {
            throw new Exception("andPredicateAsListOf0Test failed");
        }
    }

    // Verify case 0: in the implementation code.
    // JPA spec: This shall be always evaluated as false and no entities must be returned
    @SetupMethod(name = "setupTrimData")
    @Test
    public void testOrPredicateAsListOf0() throws Exception {
        final int EXPECTED_SIZE = 0;
        boolean pass = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.select(root);
            cQuery.where(cb.or(Collections.emptyList()));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass) {
            throw new Exception("testOrPredicateAsListOf0 failed");
        }
    }

    // Verify case 1: in the implementation code
    @SetupMethod(name = "setupTrimData")
    @Test
    public void testAndPredicateAsListOf1() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.and(List.of(
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam1"))
            )));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam1", "LeftToken");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (3L == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[0].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testAndPredicateAsListOf1 failed");
        }
    }

    // Verify case 1: in the implementation code
    @SetupMethod(name = "setupTrimData")
    @Test
    public void testOrPredicateAsListOf1() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.or(List.of(
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam1"))
            )));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam1", "LeftToken");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (3L == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[0].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testOrPredicateAsListOf1 failed");
        }
    }

    // Verify case 2: in the implementation code
    @SetupMethod(name = "setupTrimData")
    @Test
    public void testAndPredicateAsListOf2() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.and(List.of(
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam1")),
                    cb.equal(root.get("strVal2"), cb.parameter(String.class, "strParam2"))
            )));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam1", "LeftToken");
            query.setParameter("strParam2", "TokenRight");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (3L == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[0].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testAndPredicateAsListOf2 failed");
        }
    }

    // Verify case 2: in the implementation code
    @SetupMethod(name = "setupTrimData")
    @Test
    public void testOrPredicateAsListOf2() throws Exception {
        final int EXPECTED_SIZE = 2;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.or(List.of(
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam1")),
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam2"))
            )));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam1", "Left");
            query.setParameter("strParam2", "right");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            Set<Long> resultSet = result.stream().map(CriteriaEntity::getId).collect(Collectors.toUnmodifiableSet());
            Set<Long> expectedSet = Set.of(1L, 2L);
            if (compareFromSet(expectedSet, resultSet) == null) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result:|" + setToString(resultSet) + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + setToString(expectedSet) + "|, received result= |" + setToString((Set) result) + "|");
            }
        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testOrPredicateAsListOf2 failed");
        }
    }

    // Verify default: in the implementation code with list of size 4
    @SetupMethod(name = "setupTrimData")
    @Test
    public void testAndPredicateAsListOfN() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.and(List.of(
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam1")),
                    cb.equal(root.get("strVal2"), cb.parameter(String.class, "strParam2")),
                    cb.isNull(root.get("intVal")),
                    cb.isNull(root.get("timeVal"))
            )));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam1", "LeftToken");
            query.setParameter("strParam2", "TokenRight");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (3L == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[0].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testAndPredicateAsListOfN failed");
        }
    }

    // Verify default: in the implementation code with list of size 4
    @SetupMethod(name = "setupTrimData")
    @Test
    public void testOrPredicateAsListOfN() throws Exception {
        final int EXPECTED_SIZE = 4;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.or(List.of(
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam1")),
                    cb.equal(root.get("strVal1"), cb.parameter(String.class, "strParam2")),
                    cb.equal(root.get("timeVal"), cb.parameter(LocalTime.class, "timeParam")),
                    cb.equal(root.get("dateVal"), cb.parameter(LocalDate.class, "dateParam"))
            )));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam1", "Left");
            query.setParameter("strParam2", "right");
            query.setParameter("timeParam", LocalTime.of(10, 11, 12));
            query.setParameter("dateParam", LocalDate.of(1918, 9, 28));
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            Set<Long> resultSet = result.stream().map(CriteriaEntity::getId).collect(Collectors.toUnmodifiableSet());
            Set<Long> expectedSet = Set.of(1L, 2L, 4L, 5L);
            if (compareFromSet(expectedSet, resultSet) == null) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result:|" + setToString(resultSet) + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + setToString(expectedSet) + "|, received result= |" + setToString((Set) result) + "|");
            }
        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testOrPredicateAsListOfN failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testLeftIntLen() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam1 = cb.parameter(String.class, "strParam1");
            cQuery.where(cb.equal(root.get("strVal1"), cb.left(strParam1, 4)));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam1, "Left substring to extract");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[0].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[0].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testLeftIntLen failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testLeftExprLen() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam1 = cb.parameter(String.class, "strParam1");
            cQuery.where(cb.equal(root.get("strVal1"), cb.left(strParam1, cb.literal(4))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam1, "Left substring to extract");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[0].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[0].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testLeftExprLen failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testRightIntLen() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam1 = cb.parameter(String.class, "strParam1");
            cQuery.where(cb.equal(root.get("strVal1"), cb.right(strParam1, 5)));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam1, "Extract substring from right");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[1].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[1].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testRightIntLen failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testRightExprLen() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam1 = cb.parameter(String.class, "strParam1");
            cQuery.where(cb.equal(root.get("strVal1"), cb.right(strParam1, cb.literal(5))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam1, "Extract substring from right");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[1].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[1].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testRightExprLen failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    //In case of Derby REPLACE function must be created
    public void testReplaceExprExpr() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam1 = cb.parameter(String.class, "strParam1");
            cQuery.where(cb.equal(root.get("strVal1"), cb.replace(strParam1, cb.literal("Unknown"), cb.literal("Left"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam1, "UnknownToken");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[2].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[2].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testRightExprLen failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    //In case of Derby REPLACE function must be created
    public void testReplaceExprStr() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam2 = cb.parameter(String.class, "strParam2");
            cQuery.where(cb.equal(root.get("strVal2"), cb.replace(strParam2, cb.literal("Unknown"), "Right")));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam2, "TokenUnknown");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[2].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[2].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testReplaceExprStr failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    //In case of Derby REPLACE function must be created
    public void testReplaceStrExpr() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam1 = cb.parameter(String.class, "strParam1");
            cQuery.where(cb.equal(root.get("strVal1"), cb.replace(strParam1, "Unknown", cb.literal("Left"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam1, "UnknownToken");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[2].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[2].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testReplaceStrExpr failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    //In case of Derby REPLACE function must be created
    public void testReplaceStrStr() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            ParameterExpression<String> strParam2 = cb.parameter(String.class, "strParam2");
            cQuery.where(cb.equal(root.get("strVal2"), cb.replace(strParam2, "Unknown", "Right")));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter(strParam2, "TokenUnknown");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[2].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[2].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testReplaceStrStr failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExtractHourFromTime() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "timeParam"),
                    cb.extract(LocalTimeField.HOUR, root.get("timeVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("timeParam", 10);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[3].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[3].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractHourFromTime failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExtractMinuteFromTime() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "timeParam"),
                    cb.extract(LocalTimeField.MINUTE, root.get("timeVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("timeParam", 11);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[3].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[3].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractMinuteFromTime failed");
        }
    }


    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExtractSecondFromTime() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "timeParam"),
                    cb.extract(LocalTimeField.SECOND, root.get("timeVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("timeParam", 12d);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[3].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[3].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractSecondFromTime failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExtractYearFromDate() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "dateParam"),
                    cb.extract(LocalDateField.YEAR, root.get("dateVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("dateParam", 1918);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[4].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[4].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractYearFromDate failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExtractMonthFromDate() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "dateParam"),
                    cb.extract(LocalDateField.MONTH, root.get("dateVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("dateParam", 9);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[4].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[4].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractMonthFromDate failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExtractDayFromDate() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "dateParam"),
                    cb.extract(LocalDateField.DAY, root.get("dateVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("dateParam", 28);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[4].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[4].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractDayFromDate failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExtractQuarterFromDate() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "dateParam"),
                    cb.extract(LocalDateField.QUARTER, root.get("dateVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("dateParam", 3);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[4].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[4].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractQuarterFromDate failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    //Derby only?
    public void testExtractWeekFromDate() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(cb.equal(
                    cb.parameter(Integer.class, "dateParam"),
                    cb.extract(LocalDateField.WEEK, root.get("dateVal"))));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            // Number of the week for 28th September 1918 is 39
            query.setParameter("dateParam", 39);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[4].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[4].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExtractWeekFromDate failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExpressionEqualToExpression() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(
                    root.get("strVal1").equalTo(cb.parameter(String.class, "strParam")));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam", "LeftToken");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[2].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[2].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExpressionEqualToExpression failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExpressionEqualToObject() throws Exception {
        final int EXPECTED_SIZE = 1;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(
                    root.get("strVal1").equalTo("LeftToken"));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            if (criteriaEntity[2].getId() == result.get(0).getId()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result.get(0).getId():|" + result.get(0).getId() + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + criteriaEntity[2].getId() + "|, received result= |" + result.get(0).getId() + "|");
            }

        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExpressionEqualToObject failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExpressionNotEqualToExpression() throws Exception {
        final int EXPECTED_SIZE = 2;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(
                    root.get("strVal1").notEqualTo(cb.parameter(String.class, "strParam")));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            query.setParameter("strParam", "LeftToken");
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            Set<Long> resultSet = result.stream().map(CriteriaEntity::getId).collect(Collectors.toUnmodifiableSet());
            Set<Long> expectedSet = Set.of(1L, 2L);
            if (compareFromSet(expectedSet, resultSet) == null) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result:|" + setToString(resultSet) + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + setToString(expectedSet) + "|, received result= |" + setToString((Set) result) + "|");
            }
        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExpressionNotEqualToExpression failed");
        }
    }

    @SetupMethod(name = "setupTrimData")
    @Test
    public void testExpressionNotEqualToObject() throws Exception {
        final int EXPECTED_SIZE = 2;
        boolean pass1 = false;
        boolean pass2 = false;

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        getEntityTransaction().begin();

        CriteriaQuery<CriteriaEntity> cQuery = cb.createQuery(CriteriaEntity.class);
        if (cQuery != null) {
            Root<CriteriaEntity> root = cQuery.from(CriteriaEntity.class);
            cQuery.where(
                    root.get("strVal1").notEqualTo("LeftToken"));
            TypedQuery<CriteriaEntity> query = getEntityManager().createQuery(cQuery);
            List<CriteriaEntity> result = query.getResultList();
            if (EXPECTED_SIZE == result.size()) {
                logger.log(System.Logger.Level.TRACE, "Received expected result size:|" + result.size() + "|");
                pass1 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected size= |" + EXPECTED_SIZE + "|, received size= |" + result.size() + "|");
            }
            Set<Long> resultSet = result.stream().map(CriteriaEntity::getId).collect(Collectors.toUnmodifiableSet());
            Set<Long> expectedSet = Set.of(1L, 2L);
            if (compareFromSet(expectedSet, resultSet) == null) {
                logger.log(System.Logger.Level.TRACE, "Received expected result result:|" + setToString(resultSet) + "|");
                pass2 = true;
            } else {
                logger.log(System.Logger.Level.ERROR,
                        "Mismatch in received results - expected result = |" + setToString(expectedSet) + "|, received result= |" + setToString((Set) result) + "|");
            }
        } else {
            logger.log(System.Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();

        if (!pass1 || !pass2) {
            throw new Exception("testExpressionNotEqualToObject failed");
        }
    }

    @Test
    public void testExpressionCast() throws Exception {
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;

        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        // Create expression with Integer java type
        Expression<? extends Number> source = cb.parameter(Integer.class, "intParam");
        if (Integer.class == source.getJavaType()) {
            logger.log(System.Logger.Level.TRACE, "Received result is |Integer.class|.");
            pass1 = true;
        } else {
            logger.log(System.Logger.Level.ERROR,
                    "Mismatch in received results - expected type= |Integer.class|, received type= |" + source.getJavaType().getName() + "|");
        }

        // Cast it to Long java type
        Expression<? extends Number> target = source.cast(Long.class);
        if (Long.class == target.getJavaType()) {
            logger.log(System.Logger.Level.TRACE, "Received result is |Long.class|.");
            pass2 = true;
        } else {
            logger.log(System.Logger.Level.ERROR,
                    "Mismatch in received results - expected type= |Long.class|, received type= |" + source.getJavaType().getName() + "|");
        }
        // Cast shall not return the same instance
        if (source != target) {
            logger.log(System.Logger.Level.TRACE, "Received instances are different.");
            pass3 = true;
        } else {
            logger.log(System.Logger.Level.ERROR,
                    "Received instances are same.");
        }

        if (!pass1 || !pass2 || !pass3) {
            throw new Exception("testExpressionCast failed");
        }
    }

    // Evaluate assertions on set of expected values
    private static <T> String compareFromSet(Set<T> expected, Collection<T> actual) {
        // Make sure to have mutable set
        Set<T> expectedInternal = new HashSet<>(expected);
        for (T value : actual) {
            if (expectedInternal.contains(value)) {
                expectedInternal.remove(value);
            } else {
                return String.format(
                        "Actual value %s is not from expected set of values %s",
                        value, setToString(expected));
            }
        }
        // Make sure that all values from set were checked
        if (!expectedInternal.isEmpty()) {
            return String.format("Missing values %s from expected set %s",
                    setToString(expectedInternal), setToString(expected));
        }
        return null;
    }

    private static <T> String setToString(Set<T> set) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (T item : set) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(item.toString());
        }
        sb.append(']');
        return sb.toString();
    }
}
