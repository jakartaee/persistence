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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.joinTable;

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeJoinTableSchemaGenerationAnnotationClient extends SchemaGenerationClientBase {

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeJoinTableSchemaGenerationAnnotationClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_annotations_joinTable.jar", packageName,
                new String[] {packageName + ".Course", packageName + ".Student"});
    }

    /*
     * @testName: joinTableTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.10; PERSISTENCE:SPEC:2118.9;
     *
     * @test_Strategy: Test the @JoinTable annotation
     */
    @Test
    public void joinTableTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            var student = new Student(1, "Neo");
            var math = new Course(101, "Mathematics");
            var physics = new Course(102, "Physics");
            math.setStudents(new ArrayList<>(List.of(student)));
            physics.setStudents(new ArrayList<>(List.of(student)));
            student.setCourses(new ArrayList<>(List.of(math, physics)));
            persist(student);
            var found = getEntityManager().find(Student.class, 1);
            assertEquals(2, found.getCourses().size());
            assertEquals(Set.of(101, 102), found.getCourses().stream().map(Course::getCourseId).collect(Collectors.toSet()));
            assertEquals(student, getEntityManager().find(Course.class, 101).getStudents().get(0));
            assertForeignKey("SCHEMAGEN_COURSE_STUDENT", "COURSEIDCONSTRAINT", "COURSE_ID", "SCHEMAGENCOURSE", "COURSEID");
            assertForeignKey("SCHEMAGEN_COURSE_STUDENT", "STUDENTIDCONSTRAINT", "STUDENT_ID", "SCHEMAGENSTUDENT", "STUDENTID");
            executeSql("INSERT INTO SCHEMAGENCOURSE (COURSEID, COURSENAME) VALUES (103, 'Biology')");
            executeSql("INSERT INTO SCHEMAGEN_COURSE_STUDENT (COURSE_ID, STUDENT_ID) VALUES (103, 1)");
            assertSqlRejected("INSERT INTO SCHEMAGEN_COURSE_STUDENT (COURSE_ID, STUDENT_ID) VALUES (999, 1)");
            assertSqlRejected("INSERT INTO SCHEMAGEN_COURSE_STUDENT (COURSE_ID, STUDENT_ID) VALUES (101, 999)");
        }, "SCHEMAGENCOURSE", "SCHEMAGENSTUDENT", "SCHEMAGEN_COURSE_STUDENT");
    }
}
