/*
 * Copyright (c) 2013, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.version;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeVersionSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeVersionSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_version.jar", packageName,
                new String[] {packageName + ".Simple"});
    }

    /*
     * @testName: versionTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.23;
     *
     * @test_Strategy: Test the @Version annotation
     */
    @Test
    public void versionTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            var entity = new Simple(1);
            persist(entity);
            int initialVersion = entity.getVersion();
            var found = getEntityManager().find(Simple.class, 1);
            assertEquals(initialVersion, found.getVersion());
            inTransaction(() -> getEntityManager().lock(found, LockModeType.OPTIMISTIC_FORCE_INCREMENT));
            clearPersistenceContext();
            var updated = getEntityManager().find(Simple.class, 1);
            assertTrue(updated.getVersion() > initialVersion, "Version must increase after a forced increment");
            assertEquals(updated.getVersion(), ((Number) querySingleValue("SELECT VERSION FROM SCHEMAGENSIMPLE WHERE ID = 1")).intValue());
            clearPersistenceContext();
            assertThrows(PersistenceException.class,
                    () -> inTransaction(() -> getEntityManager().merge(entity)),
                    "An entity with the old version must be rejected");
        }, "SCHEMAGENSIMPLE");
    }
}
