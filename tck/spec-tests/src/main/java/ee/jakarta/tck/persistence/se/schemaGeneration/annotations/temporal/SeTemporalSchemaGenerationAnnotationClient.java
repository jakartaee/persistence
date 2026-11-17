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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.temporal;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import jakarta.persistence.TemporalType;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SeTemporalSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeTemporalSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_temporal.jar", packageName,
                new String[] {packageName + ".Simple"});
    }

    /*
     * @testName: temporalTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.20;
     *
     * @test_Strategy: Test the @Temporal annotation
     */
    @Test
    public void temporalTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            var entity = new Simple(1, java.sql.Timestamp.valueOf("2013-10-01 15:16:17"));
            persist(entity);
            var found = getEntityManager().find(Simple.class, 1);
            assertNotNull(found);
            // TemporalType.DATE specifies a date mapping, not the milliseconds of the returned Date.
            assertEquals(java.time.LocalDate.of(2013, 10, 1),
                    new java.sql.Date(found.getUtilDate().getTime()).toLocalDate());
            // A date-only query must also find the value originally supplied with a time of day.
            assertEquals(1, getEntityManager().createQuery(
                    "SELECT s FROM Simple s WHERE s.utilDate = :date", Simple.class)
                    .setParameter("date", java.sql.Date.valueOf("2013-10-01"), TemporalType.DATE)
                    .getSingleResult().getId());
        }, "SCHEMAGENSIMPLE");
    }
}
