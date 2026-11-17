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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.index;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeIndexSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeIndexSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_index.jar", packageName,
                new String[] {packageName + ".Simple"});
    }

    /*
     * @testName: indexTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.7
     *
     * @test_Strategy: Test the @Index annotation
     */
    @Test
    public void indexTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            persist(new Simple(1, "shared", "shared", "unique1"), new Simple(2, "shared", "shared", "unique2"));
            assertEquals("unique1", getEntityManager().find(Simple.class, 1).getSValue3());
            assertEquals("unique2", getEntityManager().find(Simple.class, 2).getSValue3());
            assertIndex("SCHEMAGENSIMPLE", "SCHEMAGENSIMPLE_SVALUE_ASC", "SVALUE", "A");
            assertIndex("SCHEMAGENSIMPLE", "SCHEMAGENSIMPLE_SVALUE2_DESC", "SVALUE2", "D");
            executeSql("INSERT INTO SCHEMAGENSIMPLE (ID, SVALUE, SVALUE2, SVALUE3) VALUES (3, 'shared', 'shared', 'unique3')");
            assertSqlRejected("INSERT INTO SCHEMAGENSIMPLE (ID, SVALUE, SVALUE2, SVALUE3) VALUES (4, 'shared', 'shared', 'unique1')");
        }, "SCHEMAGENSIMPLE");
    }
}
