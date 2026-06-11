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

package ee.jakarta.tck.persistence.jpa40.emflistener;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityListenerRegistration;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PostPersist;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".FactoryListenerBook"};
        return createDeploymentJar("jpa_jpa40_emflistener.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
        removeTestData();
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
     * Tests Jakarta Persistence 4.0 entity manager factory listener
     * registration. The test verifies {@code addListener()} receives a
     * lifecycle event for the requested entity and callback type and verifies
     * the returned {@link EntityListenerRegistration} can cancel the listener.
     */
    @Test
    public void entityManagerFactoryAddListenerTest() {
        List<Integer> observed = new ArrayList<>();
        EntityListenerRegistration registration = getEntityManagerFactory()
                .addListener(FactoryListenerBook.class, PostPersist.class,
                        book -> observed.add(book.getId()));

        persistBook(1, "Alpha");
        assertEquals(List.of(1), observed);

        registration.cancel();
        persistBook(2, "Beta");
        assertEquals(List.of(1), observed);
    }

    private void persistBook(Integer id, String title) {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new FactoryListenerBook(id, title));
        transaction.commit();
        getEntityManager().clear();
    }
}
