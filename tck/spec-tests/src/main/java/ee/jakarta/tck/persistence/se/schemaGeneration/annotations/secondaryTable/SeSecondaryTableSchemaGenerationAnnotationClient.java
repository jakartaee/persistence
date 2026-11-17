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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.secondaryTable;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeSecondaryTableSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeSecondaryTableSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_secondaryTable.jar", packageName,
                new String[] {packageName + ".Simple"});
    }

    /*
     * @testName: secondaryTableTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.16; PERSISTENCE:SPEC:2118.24;
     *
     * @test_Strategy: Test the @SecondaryTable annotation
     */
    @Test
    public void secondaryTableTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            var entity = new Simple(1);
            entity.setDetail("secondary table value");
            persist(entity);
            assertEquals("secondary table value", getEntityManager().find(Simple.class, 1).getDetail());
            assertEquals("secondary table value", querySingleValue("SELECT DETAIL FROM SCHEMAGENSIMPLE_SECOND WHERE SECONDARY_ID = 1"));
            assertForeignKey("SCHEMAGENSIMPLE_SECOND", "MYCONSTRAINT", "SECONDARY_ID", "SCHEMAGENSIMPLE", "SIMPLEID");
            executeSql("INSERT INTO SCHEMAGENSIMPLE (SIMPLEID, PRODUCT_TYPE) VALUES (2, 'Product')");
            executeSql("INSERT INTO SCHEMAGENSIMPLE_SECOND (SECONDARY_ID, DETAIL) VALUES (2, 'valid')");
            assertSqlRejected("INSERT INTO SCHEMAGENSIMPLE_SECOND (SECONDARY_ID, DETAIL) VALUES (999, 'orphan')");
        }, "SCHEMAGENSIMPLE", "SCHEMAGENSIMPLE_SECOND");
    }
}
