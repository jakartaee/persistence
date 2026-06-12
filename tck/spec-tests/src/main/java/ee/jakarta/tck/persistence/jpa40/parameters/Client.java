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
import jakarta.persistence.Query;
import jakarta.persistence.Statement;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Type;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    /**
     * Tests Jakarta Persistence 4.0 typed parameter binding overloads. The
     * test verifies {@code setParameter()} overloads accepting a Java
     * {@link Class} and a metamodel {@link Type}, including positional
     * parameters and a converted attribute type.
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

        ParameterBook byPositionalClass = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.title = ?1",
                        ParameterBook.class)
                .setParameter(1, "Alpha", String.class)
                .getSingleResult();
        assertEquals(1, byPositionalClass.getId());

        ParameterBook byPositionalType = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.title = ?1",
                        ParameterBook.class)
                .setParameter(1, "Beta", titleType)
                .getSingleResult();
        assertEquals(2, byPositionalType.getId());

        Type<Code> codeType = getEntityManager()
                .getMetamodel()
                .entity(ParameterBook.class)
                .getSingularAttribute("code", Code.class)
                .getType();
        ParameterBook byConvertedAttributeType = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.code = :code",
                        ParameterBook.class)
                .setParameter("code", new Code("A"), codeType)
                .getSingleResult();
        assertEquals(1, byConvertedAttributeType.getId());

        ParameterBook byPositionalConvertedAttributeType = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.code = ?1",
                        ParameterBook.class)
                .setParameter(1, new Code("B"), codeType)
                .getSingleResult();
        assertEquals(2, byPositionalConvertedAttributeType.getId());
    }

    /**
     * Tests Jakarta Persistence 4.0 positional argument binding. The test
     * verifies {@code setParameters()} binds each argument to the positional
     * parameter with the same index for {@link Query}, {@link TypedQuery},
     * {@link Statement}, and {@link StoredProcedureQuery}, and validates the
     * required argument count.
     */
    @Test
    public void setParametersTest() {
        var query = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa40ParameterBook b WHERE b.id = ?2 AND b.title = ?1")
                .setParameters("Alpha", 1)
                .ofType(String.class);
        assertEquals("Alpha", query.getSingleResult());

        var typedQuery = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.id = ?2 AND b.title = ?1",
                        ParameterBook.class)
                        .setParameters("Beta", 2);
        assertEquals(2, typedQuery.getSingleResult().getId());

        var transaction = getEntityTransaction();
        transaction.begin();
        var statement = getEntityManager()
                .createStatement("UPDATE Jpa40ParameterBook b SET b.title = ?2 WHERE b.id = ?1")
                .setParameters(1, "Varargs");
        int updateCount = statement.execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, updateCount);
        assertEquals("Varargs", getEntityManager().get(ParameterBook.class, 1).getTitle());

        try (var storedProcedureQuery = getEntityManager()
                .createStoredProcedureQuery("JPA40_CODE_PROC")
                .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
                .setParameters("procedure")) {
            assertEquals("procedure", storedProcedureQuery.getParameterValue(1));
        }

        assertThrows(IllegalArgumentException.class, () -> getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.id = ?2 AND b.title = ?1")
                .setParameters("Alpha"));
        assertThrows(IllegalArgumentException.class, () -> getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.id = ?1")
                .setParameters(1, "Alpha"));
    }

    /**
     * Tests Jakarta Persistence 4.0 converted parameter binding. The test
     * verifies {@code Query.setConvertedParameter()} and
     * {@code CriteriaBuilder.convertedParameter()} bind values through an
     * attribute converter.
     */
    @Test
    public void convertedParameterBindingTest() {
        TypedQuery<ParameterBook> typedQuery = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.code = :code",
                        ParameterBook.class);
        TypedQuery<ParameterBook> returnedQuery =
                typedQuery.setConvertedParameter("code", new Code("A"), CodeConverter.class);
        assertSame(typedQuery, returnedQuery);

        ParameterBook byQuery = returnedQuery.getSingleResult();
        assertEquals(1, byQuery.getId());

        ParameterBook byPositionalQuery = getEntityManager()
                .createQuery("SELECT b FROM Jpa40ParameterBook b WHERE b.code = ?1",
                        ParameterBook.class)
                .setConvertedParameter(1, new Code("B"), CodeConverter.class)
                .getSingleResult();
        assertEquals(2, byPositionalQuery.getId());

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
     * Tests Jakarta Persistence 4.0 typed parameter binding overloads on
     * {@link Statement}. The test verifies named and positional executable
     * statements accept Java {@link Class} and metamodel {@link Type}
     * parameter declarations.
     */
    @Test
    public void statementTypedSetParameterOverloadsTest() {
        Type<String> titleType = getEntityManager()
                .getMetamodel()
                .entity(ParameterBook.class)
                .getSingularAttribute("title", String.class)
                .getType();
        Type<Integer> idType = getEntityManager()
                .getMetamodel()
                .entity(ParameterBook.class)
                .getSingularAttribute("id", Integer.class)
                .getType();

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        Statement namedStatement = getEntityManager()
                .createStatement("UPDATE Jpa40ParameterBook b SET b.title = :title WHERE b.id = :id");
        Statement returnedStatement = namedStatement
                .setParameter("title", "Typed named", String.class)
                .setParameter("id", 1, Integer.class);
        assertSame(namedStatement, returnedStatement);
        int namedCount = returnedStatement.execute();

        int positionalCount = getEntityManager()
                .createStatement("UPDATE Jpa40ParameterBook b SET b.title = ?1 WHERE b.id = ?2")
                .setParameter(1, "Typed positional", titleType)
                .setParameter(2, 2, idType)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, namedCount);
        assertEquals(1, positionalCount);
        assertEquals("Typed named", getEntityManager().find(ParameterBook.class, 1).getTitle());
        assertEquals("Typed positional", getEntityManager().find(ParameterBook.class, 2).getTitle());
    }

    /**
     * Tests Jakarta Persistence 4.0 converted parameter binding on
     * {@link Statement}. The test verifies executable statements accept
     * converted bindings by metamodel {@link Type} and by explicit converter
     * class, for both named and positional parameters.
     */
    @Test
    public void statementConvertedSetParameterOverloadsTest() {
        Type<Code> codeType = getEntityManager()
                .getMetamodel()
                .entity(ParameterBook.class)
                .getSingularAttribute("code", Code.class)
                .getType();

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int namedTypeCount = getEntityManager()
                .createStatement("UPDATE Jpa40ParameterBook b SET b.title = :title WHERE b.code = :code")
                .setParameter("title", "Converted type", String.class)
                .setParameter("code", new Code("A"), codeType)
                .execute();

        Statement positionalStatement = getEntityManager()
                .createStatement("UPDATE Jpa40ParameterBook b SET b.title = ?1 WHERE b.code = ?2");
        Statement returnedStatement = positionalStatement
                .setParameter(1, "Converted class", String.class)
                .setConvertedParameter(2, new Code("B"), CodeConverter.class);
        assertSame(positionalStatement, returnedStatement);
        int positionalConvertedCount = returnedStatement.execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, namedTypeCount);
        assertEquals(1, positionalConvertedCount);
        assertEquals("Converted type", getEntityManager().find(ParameterBook.class, 1).getTitle());
        assertEquals("Converted class", getEntityManager().find(ParameterBook.class, 2).getTitle());
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
        getEntityManager().persist(new ParameterBook(2, "Beta", new Code("B")));
        transaction.commit();
        getEntityManager().clear();
    }
}
