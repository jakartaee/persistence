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

package ee.jakarta.tck.persistence.jpa40.nameddescriptor;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".DescriptorBook", packageName + ".package-info"};
        return createDeploymentJar("jpa_jpa40_nameddescriptor.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests Jakarta Persistence 4.0 package descriptor named query discovery.
     * The test verifies provider discovery and execution of package-level
     * {@code @NamedQuery}, {@code @NamedNativeQuery}, and {@code @NamedStatement}
     * declarations by executing them against persisted data.
     */
    @Test
    public void packageDescriptorNamedQueryAndStatementExecutionTest() {
        DescriptorBook byJpql = getEntityManager()
                .createNamedQuery(DescriptorBook.QUERY, DescriptorBook.class)
                .setParameter("title", "Alpha")
                .getSingleResult();
        assertEquals(1, byJpql.getId());

        DescriptorBook byNative = getEntityManager()
                .createNamedQuery(DescriptorBook.NATIVE_QUERY, DescriptorBook.class)
                .setParameter(1, "Alpha")
                .getSingleResult();
        assertEquals(1, byNative.getId());

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int updated = getEntityManager()
                .createNamedStatement(DescriptorBook.STATEMENT)
                .setParameter("title", "Renamed")
                .setParameter("id", 1)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, updated);
        assertEquals("Renamed", getEntityManager().find(DescriptorBook.class, 1).getTitle());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new DescriptorBook(1, "Alpha"));
        transaction.commit();
        getEntityManager().clear();
    }
}
