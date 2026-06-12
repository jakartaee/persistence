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

package ee.jakarta.tck.persistence.jpa32.singleresult;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".SingleResultBook"};
        return createDeploymentJar("jpa_jpa32_singleresult.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests the Jakarta Persistence 3.2
     * {@link TypedQuery#getSingleResultOrNull()} method. The test verifies that
     * the method returns the single selected value or entity when one row
     * matches and returns {@code null} when no row matches.
     */
    @Test
    public void typedQueryGetSingleResultOrNullTest() {
        TypedQuery<String> queryWithResult = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa32SingleResultBook b WHERE b.id = :id", String.class)
                .setParameter("id", 1);
        assertEquals("Alpha", queryWithResult.getSingleResultOrNull());

        TypedQuery<String> queryWithoutResult = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa32SingleResultBook b WHERE b.id = :id", String.class)
                .setParameter("id", 99);
        assertNull(queryWithoutResult.getSingleResultOrNull());

        TypedQuery<SingleResultBook> queryWithEntityResult = getEntityManager()
                .createQuery("SELECT b FROM Jpa32SingleResultBook b WHERE b.id = :id", SingleResultBook.class)
                .setParameter("id", 1);
        assertEquals("Alpha", queryWithEntityResult.getSingleResultOrNull().getTitle());

        TypedQuery<SingleResultBook> queryWithoutEntityResult = getEntityManager()
                .createQuery("SELECT b FROM Jpa32SingleResultBook b WHERE b.id = :id", SingleResultBook.class)
                .setParameter("id", 99);
        assertNull(queryWithoutEntityResult.getSingleResultOrNull());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new SingleResultBook(1, "Alpha"));
        transaction.commit();
        getEntityManager().clear();
    }
}
