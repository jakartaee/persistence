/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.persistence.se.schemaGeneration.xml.mapKeyJoinColumn;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeMapKeyJoinColumnSchemaGenerationXmlClient extends SchemaGenerationClientBase {

    private static final String TEST_CASE = "jpa_se_schemaGeneration_xml_mapKeyJoinColumn";

    @Override
    public JavaArchive createDeployment() throws Exception {
        String packageName = SeMapKeyJoinColumnSchemaGenerationXmlClient.class.getPackageName();
        String[] classes = {
                packageName + ".Course",
                packageName + ".Semester",
                packageName + ".Student"};
        return createDeploymentJar(TEST_CASE + ".jar", packageName, classes, new String[] {ORM_XML});
    }

    /**
     * Verifies the {@code map-key-foreign-key} subelement of {@code many-to-many}
     * in {@code orm.xml} is honored by schema generation. The generated schema
     * must contain the named foreign key constraint from the map key join column
     * of the join table to the map key entity table, and the database must reject
     * a join table row whose map key has no matching entity row.
     */
    @Test
    public void mapKeyJoinColumnForeignKeyXmlTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            var course1 = new Course(1, "course1");
            var course2 = new Course(2, "course2");
            var semester1 = new Semester(1, "semester1");
            var semester2 = new Semester(2, "semester2");
            var student = new Student(1);
            student.setEnrollments(Map.of(course1, semester1, course2, semester2));
            persist(course1, course2, semester1, semester2, student);

            // After reloading, each course key must still map to the semester it was
            // persisted with. Entities are compared by id because the provider may
            // return proxies for the map keys and values.
            var found = getEntityManager().find(Student.class, 1);

            var enrollments = semesterIdByCourseId(found.getEnrollments());
            assertEquals(2, enrollments.size());
            assertEquals(semester1.getId(), enrollments.get(course1.getId()));
            assertEquals(semester2.getId(), enrollments.get(course2.getId()));

            // The foreign key constraint must exist in the database metadata under the
            // name given in the mapping, on the map key join column of the join table,
            // referencing the primary key of the map key entity table.
            assertForeignKey("SCHEMAGENXMLMKJC_ENROLLMENTS", "XMLCOURSEKEYCONSTRAINT", "COURSE_KEY",
                    "SCHEMAGENXMLMKJCCOURSE", "COURSEID");

            // The foreign key constraint must be enforced: the database must reject a join
            // table row whose map key has no matching row in the map key entity table.
            assertSqlRejected("INSERT INTO SCHEMAGENXMLMKJC_ENROLLMENTS (STUDENT_ID, SEMESTER_ID, COURSE_KEY) VALUES (1, 1, 99)");
        }, "SCHEMAGENXMLMKJCCOURSE", "SCHEMAGENXMLMKJCSEMESTER", "SCHEMAGENXMLMKJCSTUDENT", "SCHEMAGENXMLMKJC_ENROLLMENTS");
    }

    /**
     * Returns a map from each course id to the id of the semester it is mapped to.
     */
    private static Map<Integer, Integer> semesterIdByCourseId(Map<Course, Semester> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getId(), e -> e.getValue().getId()));
    }
}
