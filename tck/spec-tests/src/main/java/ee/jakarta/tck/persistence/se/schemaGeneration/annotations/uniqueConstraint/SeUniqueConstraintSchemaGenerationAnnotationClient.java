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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.uniqueConstraint;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeUniqueConstraintSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeUniqueConstraintSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_uniqueConstraint.jar", packageName,
                new String[] {packageName + ".Simple"});
    }

    /*
     * @testName: uniqueConstraintTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.22;
     *
     * @test_Strategy: Test the @UniqueConstraint annotation
     */
    @Test
    public void uniqueConstraintTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            // Distinct primary keys ensure that only the declared unique constraint can reject the duplicate.
            persist(new Simple(1, "name", "first"), new Simple(2, "name", "second"), new Simple(3, "other", "first"));
            assertEquals("first", getEntityManager().find(Simple.class, 1).getCategory());
            assertEquals("second", getEntityManager().find(Simple.class, 2).getCategory());
            executeSql("INSERT INTO SCHEMAGENSIMPLE (ID, NAME, CATEGORY) VALUES (4, 'other', 'second')");
            assertSqlRejected("INSERT INTO SCHEMAGENSIMPLE (ID, NAME, CATEGORY) VALUES (5, 'name', 'first')");
        }, "SCHEMAGENSIMPLE");
    }
}
