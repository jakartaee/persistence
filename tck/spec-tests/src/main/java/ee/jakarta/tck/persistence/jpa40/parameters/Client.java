/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa40.parameters;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Parameter;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Type;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".ParameterBook",
                packageName + ".Code",
                packageName + ".CodeConverter"};
        return createDeploymentJar("jpa_jpa40_parameters.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    @AfterEach
    public void cleanup() throws Exception {
        try {
            removeTestData();
        } finally {
            try {
                super.cleanup();
            } finally {
                removeTestJarFromCP();
            }
        }
    }

    /**
     * Tests Jakarta Persistence 4.0 typed parameter binding overloads. The
     * test verifies {@code setParameter()} overloads accepting a Java
     * {@link Class} and a metamodel {@link Type}.
     */
    @Test
    public void typedSetParameterOverloadsTest() {
        TypedQuery<ParameterBook> classTyped = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.title = :title",
                        ParameterBook.class);
        ParameterBook byClass = classTyped
                .setParameter("title", "Alpha", String.class)
                .getSingleResult();
        assertEquals(1, byClass.getId());

        Type<String> titleType = getEntityManager()
                .getMetamodel()
                .entity(ParameterBook.class)
                .getSingularAttribute("title", String.class)
                .getType();
        ParameterBook byType = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.title = :title",
                        ParameterBook.class)
                .setParameter("title", "Alpha", titleType)
                .getSingleResult();
        assertEquals(1, byType.getId());
    }

    /**
     * Tests Jakarta Persistence 4.0 converted parameter binding. The test
     * verifies {@code Query.setConvertedParameter()} and
     * {@code CriteriaBuilder.convertedParameter()} bind values through an
     * attribute converter.
     */
    @Test
    public void convertedParameterBindingTest() {
        ParameterBook byQuery = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.code = :code",
                        ParameterBook.class)
                .setConvertedParameter("code", new Code("A"), CodeConverter.class)
                .getSingleResult();
        assertEquals(1, byQuery.getId());

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ParameterBook> criteria = builder.createQuery(ParameterBook.class);
        Root<ParameterBook> book = criteria.from(ParameterBook.class);
        ParameterExpression<Code> code = builder.convertedParameter(CodeConverter.class);
        criteria.select(book).where(book.get("code").equalTo(code));

        ParameterBook byCriteria = getEntityManager()
                .createQuery(criteria)
                .setParameter(code, new Code("A"))
                .getSingleResult();
        assertEquals(1, byCriteria.getId());
    }

    /**
     * Tests the Jakarta Persistence 4.0 stored procedure converted parameter
     * API. The test verifies the provider-created {@link StoredProcedureQuery}
     * accepts a converted parameter registration and exposes the resulting
     * typed parameter metadata.
     */
    @Test
    public void storedProcedureConvertedParameterApiTest() {
        Parameter<Code> parameter = getEntityManager()
                .createStoredProcedureQuery("JPA40_CODE_PROC")
                .registerConvertedParameter("code", CodeConverter.class, ParameterMode.IN);

        assertEquals("code", parameter.getName());
        assertEquals(Code.class, parameter.getParameterType());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new ParameterBook(1, "Alpha", new Code("A")));
        transaction.commit();
        getEntityManager().clear();
    }

    private void removeTestData() {
        EntityTransaction transaction = getEntityTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
        getEntityManagerFactory().getSchemaManager().truncate();
        getEntityManager().clear();
    }
}
