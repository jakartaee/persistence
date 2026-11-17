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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.orderColumn;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeOrderColumnSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeOrderColumnSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_orderColumn.jar", packageName,
                new String[] {packageName + ".Department", packageName + ".Employee"});
    }

    /*
     * @testName: orderColumnTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.14; PERSISTENCE:SPEC:2118.9;
     *
     * @test_Strategy: Test the @OrderColumn annotation
     */
    @Test
    public void orderColumnTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            var department = new Department(50);
            var first = new Employee(1, department);
            var second = new Employee(2, department);
            var third = new Employee(3, department);
            department.setEmployees(new ArrayList<>(List.of(third, first, second)));
            persist(department, first, second, third);
            var found = getEntityManager().find(Department.class, 50);
            assertEquals(List.of(3, 1, 2), found.getEmployees().stream().map(Employee::getEmpId).toList());
            assertEquals(1, getEntityManager().createQuery(
                    "SELECT e FROM Department d JOIN d.employees e WHERE d.deptId = 50 AND INDEX(e) = 1", Employee.class)
                    .getSingleResult().getEmpId());
            inTransaction(() -> Collections.swap(found.getEmployees(), 0, 2));
            clearPersistenceContext();
            assertEquals(List.of(2, 1, 3), getEntityManager().find(Department.class, 50)
                    .getEmployees().stream().map(Employee::getEmpId).toList());
            assertEquals(0, ((Number) querySingleValue("SELECT THEORDERCOLUMN FROM SCHEMAGENEMP WHERE EMPID = 2")).intValue());
            assertForeignKey("SCHEMAGENEMP", "MYCONSTRANT", "FK_DEPT", "SCHEMAGENDEPT", "DEPTID");
            executeSql("INSERT INTO SCHEMAGENEMP (EMPID, FK_DEPT, THEORDERCOLUMN) VALUES (98, 50, 3)");
            assertSqlRejected("INSERT INTO SCHEMAGENEMP (EMPID, FK_DEPT, THEORDERCOLUMN) VALUES (99, 999, 3)");
        }, "SCHEMAGENEMP", "SCHEMAGENDEPT");
    }
}
