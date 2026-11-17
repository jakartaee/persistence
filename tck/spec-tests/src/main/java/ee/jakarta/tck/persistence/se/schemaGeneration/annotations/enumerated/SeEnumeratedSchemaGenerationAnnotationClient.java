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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.enumerated;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeEnumeratedSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeEnumeratedSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_enumerated.jar", packageName,
                new String[] {packageName + ".Simple", packageName + ".Grade"});
    }

    /*
     * @testName: enumeratedTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.3;
     *
     * @test_Strategy: Test the @Enumerated annotation
     */
    @Test
    public void enumeratedTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            var entity = new Simple(1);
            entity.setEnumData(Grade.INCOMPLETE);
            persist(entity);
            assertEquals(Grade.INCOMPLETE, getEntityManager().find(Simple.class, 1).getEnumData());
            assertEquals("INCOMPLETE", querySingleValue("SELECT ENUMDATA FROM SCHEMAGENSIMPLE WHERE ID = 1").toString());
        }, "SCHEMAGENSIMPLE");
    }
}
