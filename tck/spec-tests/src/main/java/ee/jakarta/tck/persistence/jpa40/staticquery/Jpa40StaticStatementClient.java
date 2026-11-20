/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.persistence.jpa40.staticquery;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Statement;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Jpa40StaticStatementClient extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String[] classes = {
                StaticQueryBook.class.getName(),
                BookStatements.class.getName()
        };
        return createDeploymentJar("jpa_jpa40_staticstatement.jar", getClass().getPackageName(), classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new StaticQueryBook(1, "Alpha"));
        getEntityManager().persist(new StaticQueryBook(2, "Beta"));
        transaction.commit();
        getEntityManager().clear();
    }

    /**
     * Verifies that {@code @JakartaStatement} registers a JPQL update under
     * the annotated method's signature.
     */
    @Test
    public void jakartaStatementAnnotationExecutionTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int count = getEntityManager()
                .createNamedStatement(BookStatements.class.getName()
                        + "#renameBook(java.lang.String,java.lang.Integer)")
                .setParameter("title", "Renamed")
                .setParameter("id", 1)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, count);
        assertEquals("Renamed", getEntityManager().find(StaticQueryBook.class, 1).getTitle());
        assertEquals("Beta", getEntityManager().find(StaticQueryBook.class, 2).getTitle());
    }

    /**
     * Verifies that {@code @NativeStatement} registers a SQL delete under
     * the annotated method's signature and accepts a positional parameter.
     */
    @Test
    public void nativeStatementAnnotationExecutionTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int count = getEntityManager()
                .createNamedStatement(BookStatements.class.getName() + "#deleteBook(java.lang.Integer)")
                .setParameter(1, 1)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, count);
        assertNull(getEntityManager().find(StaticQueryBook.class, 1));
        assertEquals("Beta", getEntityManager().find(StaticQueryBook.class, 2).getTitle());
    }

    /**
     * Verifies that a JPQL update with {@code @StatementOptions} sees a pending title change.
     */
    @Test
    public void jakartaStatementOptionsFlushTest() {
        var em = getEntityManager();
        em.setFlushMode(FlushModeType.EXPLICIT);

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        em.find(StaticQueryBook.class, 1).setTitle("Pending update title");
        int count = em.createNamedStatement(BookStatements.class.getName()
                        + "#renameBooksByTitle(java.lang.String,java.lang.String)")
                .setParameter("title", "Updated")
                .setParameter("previousTitle", "Pending update title")
                .execute();
        transaction.rollback();

        assertEquals(1, count);
    }

    /**
     * Verifies that {@code @StatementOptions} configures the timeout and
     * flushes a pending title change before a native delete.
     */
    @Test
    public void nativeStatementOptionsFlushAndTimeoutTest() {
        var em = getEntityManager();
        em.setFlushMode(FlushModeType.EXPLICIT);
        Statement statement = em.createNamedStatement(BookStatements.class.getName()
                + "#deleteBooksByTitle(java.lang.String)");

        assertEquals(5000, statement.getTimeout());

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        em.find(StaticQueryBook.class, 1).setTitle("Pending delete title");
        int count = statement.setParameter(1, "Pending delete title").execute();
        transaction.rollback();

        assertEquals(1, count);
    }
}
