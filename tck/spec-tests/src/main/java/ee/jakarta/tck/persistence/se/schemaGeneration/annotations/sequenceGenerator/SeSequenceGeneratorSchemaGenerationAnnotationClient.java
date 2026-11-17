/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.sequenceGenerator;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeSequenceGeneratorSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeSequenceGeneratorSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_sequenceGenerator.jar", packageName,
                new String[] {packageName + ".Simple"});
    }

    /*
     * @testName: sequenceGeneratorTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.17; PERSISTENCE:SPEC:2118.5;
     *
     * @test_Strategy: Test the @SequenceGenerator annotation
     */
    @Test
    public void sequenceGeneratorTest() throws Throwable {
        org.junit.jupiter.api.Assumptions.assumeTrue(Boolean.parseBoolean(System.getProperty("db.supports.sequence")));
        // Recreating the schema must reset the generator as well as recreate the entity table.
        for (int generation = 0; generation < 2; generation++) {
            verifyGeneratedSchema(() -> {
                var first = new Simple();
                var second = new Simple();
                persist(first, second);
                assertEquals(10, first.getId(), "First generated identifier");
                assertEquals(11, second.getId(), "Next generated identifier");
                assertEquals(first, getEntityManager().find(Simple.class, first.getId()));
                assertEquals(second, getEntityManager().find(Simple.class, second.getId()));
            }, "SCHEMAGENSIMPLE");
        }
    }
}
