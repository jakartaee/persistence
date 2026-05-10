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

package ee.jakarta.tck.persistence.jpa32.configuration;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".ProgrammaticEntity"};
        return createDeploymentJar("jpa_jpa32_configuration.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 programmatic persistence unit bootstrap via
     * {@link PersistenceConfiguration}. The test verifies configuration of a
     * provider, managed class, and properties without a persistence descriptor,
     * and then uses the resulting factory to persist and find an entity.
     */
    @Test
    public void persistenceConfigurationProgrammaticBootstrapTest() {
        Map<String, Object> properties = new HashMap<>();
        getPersistenceUnitProperties().forEach((key, value) -> properties.put((String) key, value));

        PersistenceConfiguration configuration =
                new PersistenceConfiguration("JPATCK-JPA32-PROGRAMMATIC")
                        .provider((String) properties.get(JAKARTA_PERSISTENCE_PROVIDER))
                        .managedClass(ProgrammaticEntity.class)
                        .properties(properties);

        assertEquals("JPATCK-JPA32-PROGRAMMATIC", configuration.name());
        assertEquals(List.of(ProgrammaticEntity.class), configuration.managedClasses());
        assertEquals("jakarta.persistence.jdbc.url", PersistenceConfiguration.JDBC_URL);
        assertEquals("jakarta.persistence.schema-generation.database.action",
                PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION);

        try (EntityManagerFactory emf = configuration.createEntityManagerFactory()) {
            try {
                emf.runInTransaction(em -> em.persist(new ProgrammaticEntity(1, "programmatic")));
                ProgrammaticEntity found =
                        emf.callInTransaction(em -> em.find(ProgrammaticEntity.class, 1));
                assertNotNull(found);
                assertEquals("programmatic", found.getName());
            } finally {
                emf.getSchemaManager().truncate();
            }
        }
    }
}
