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

package ee.jakarta.tck.persistence.jpa40.fetchdefaults;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceConfiguration;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".DefaultFetchBook",
                packageName + ".DefaultFetchPublisher",
                packageName + ".DefaultFetchCover"};
        return createDeploymentJar("jpa_jpa40_fetchdefaults.jar", packageName, classes, PERSISTENCE_XML, new String[0]);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
    }

    @AfterEach
    public void cleanup() throws Exception {
        removeTestJarFromCP();
    }

    /**
     * Tests Jakarta Persistence 4.0 default fetch type configuration for
     * to-one associations. The test bootstraps a persistence unit with
     * {@link PersistenceConfiguration#defaultToOneFetchType(FetchType)} set to
     * {@link FetchType#LAZY} and verifies provider-created mappings for
     * {@code @ManyToOne} and {@code @OneToOne} associations declared with the
     * default fetch type are not loaded by a plain find operation.
     */
    @Test
    public void defaultToOneFetchTypeLazyRuntimeTest() {
        Map<String, Object> properties = persistenceProperties();

        PersistenceConfiguration configuration =
                new PersistenceConfiguration("JPATCK-JPA40-FETCH-DEFAULTS")
                        .provider((String) properties.get(JAKARTA_PERSISTENCE_PROVIDER))
                        .managedClass(DefaultFetchBook.class)
                        .managedClass(DefaultFetchPublisher.class)
                        .managedClass(DefaultFetchCover.class)
                        .defaultToOneFetchType(FetchType.LAZY)
                        .properties(properties);

        try (EntityManagerFactory emf = configuration.createEntityManagerFactory()) {
            assertDefaultToOneAssociationsAreLazy(emf);
        }
    }

    /**
     * Tests Jakarta Persistence 4.0 {@code <default-to-one-fetch-type>} in
     * {@code persistence.xml}. The test bootstraps a persistence unit whose
     * descriptor sets the element to {@code LAZY} and verifies provider-created
     * mappings for {@code @ManyToOne} and {@code @OneToOne} associations
     * declared with the default fetch type are not loaded by a plain find
     * operation.
     */
    @Test
    public void defaultToOneFetchTypeLazyXmlTest() {
        try (EntityManagerFactory emf =
                     Persistence.createEntityManagerFactory(getPersistenceUnitName(),
                             persistenceProperties())) {
            assertDefaultToOneAssociationsAreLazy(emf);
        }
    }

    /**
     * Tests Jakarta Persistence 4.0 {@code <default-to-one-fetch-type>} in
     * {@code persistence.xml}. The test bootstraps a persistence unit whose
     * descriptor sets the element to {@code EAGER} and verifies
     * provider-created mappings for {@code @ManyToOne} and {@code @OneToOne}
     * associations declared with the default fetch type are loaded by a plain
     * find operation.
     */
    @Test
    public void defaultToOneFetchTypeEagerXmlTest() {
        try (EntityManagerFactory emf =
                     Persistence.createEntityManagerFactory(getSecondPersistenceUnitName(),
                             persistenceProperties())) {
            assertDefaultToOneAssociationsAreEager(emf);
        }
    }

    private Map<String, Object> persistenceProperties() {
        Map<String, Object> properties = new HashMap<>();
        getPersistenceUnitProperties().forEach((key, value) -> properties.put((String) key, value));
        return properties;
    }

    private void assertDefaultToOneAssociationsAreLazy(EntityManagerFactory emf) {
        assertDefaultToOneAssociationsLoadState(emf, false);
    }

    private void assertDefaultToOneAssociationsAreEager(EntityManagerFactory emf) {
        assertDefaultToOneAssociationsLoadState(emf, true);
    }

    private void assertDefaultToOneAssociationsLoadState(EntityManagerFactory emf, boolean expectedLoaded) {
        try {
            emf.runInTransaction(entityManager -> {
                DefaultFetchPublisher publisher = new DefaultFetchPublisher(1, "Publisher");
                DefaultFetchCover cover = new DefaultFetchCover(1, "Red");
                entityManager.persist(publisher);
                entityManager.persist(cover);
                entityManager.persist(new DefaultFetchBook(1, "Alpha", publisher, cover));
            });

            DefaultFetchBook book =
                    emf.callInTransaction(entityManager -> entityManager.find(DefaultFetchBook.class, 1));

            if (expectedLoaded) {
                assertTrue(Persistence.getPersistenceUtil().isLoaded(book, "publisher"));
                assertTrue(Persistence.getPersistenceUtil().isLoaded(book, "cover"));
            } else {
                assertFalse(Persistence.getPersistenceUtil().isLoaded(book, "publisher"));
                assertFalse(Persistence.getPersistenceUtil().isLoaded(book, "cover"));
            }
        } finally {
            emf.getSchemaManager().truncate();
        }
    }
}
