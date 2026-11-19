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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.mapKeyJoinColumn;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Persistence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeMapKeyJoinColumnSchemaGenerationAnnotationClient extends PMClientBase {

    private static final String TEST_CASE = "jpa_se_schemaGeneration_annotations_mapKeyJoinColumn";

    private String schemaGenerationDir;

    public JavaArchive createDeployment() throws Exception {
        String packageName = SeMapKeyJoinColumnSchemaGenerationAnnotationClient.class.getPackageName();
        String[] classes = {
                packageName + ".Course",
                packageName + ".Semester",
                packageName + ".Student"};
        return createDeploymentJar(TEST_CASE + ".jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();

        schemaGenerationDir = System.getProperty("user.dir");
        if (!schemaGenerationDir.endsWith(File.separator)) {
            schemaGenerationDir += File.separator;
        }
        schemaGenerationDir += "schemaGeneration";

        var dir = new File(schemaGenerationDir);
        deleteItem(dir);
        if (!dir.mkdir()) {
            throw new Exception("Could not mkdir:" + dir.getAbsolutePath());
        }
        removeTestData();
    }

    @Override
    protected boolean dropCreateSchemaOnStartByDefault() {
        return false;
    }

    /**
     * Verifies the {@code foreignKey} attribute of {@code @MapKeyJoinColumn} and
     * {@code @MapKeyJoinColumns} is honored by schema generation for
     * {@code @ManyToMany} relationships whose map key is an entity. The generated
     * create script must contain the named foreign key constraints from the map
     * key join column of each join table to the map key entity table, the
     * generated scripts must be executable, and the drop script must remove the
     * tables.
     */
    @Test
    public void mapKeyJoinColumnForeignKeyTest() throws Exception {
        var createScript = new File(schemaGenerationDir + File.separator + "create_" + TEST_CASE + ".sql");
        var dropScript = new File(schemaGenerationDir + File.separator + "drop_" + TEST_CASE + ".sql");
        deleteItem(createScript);
        deleteItem(dropScript);

        // Step 1: only write the create and drop scripts, nothing is executed against the database.
        var props = getPersistenceUnitProperties();
        props.put("jakarta.persistence.schema-generation.database.action", "none");
        props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
        props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
        props.put("jakarta.persistence.schema-generation.scripts.create-target", convertToURI(createScript.getPath()));
        props.put("jakarta.persistence.schema-generation.scripts.drop-target", convertToURI(dropScript.getPath()));
        Persistence.createEntityManagerFactory(getPersistenceUnitName(), props).close();

        // Step 2: the create script must contain the tables and the named foreign keys from the map key
        // column of each join table to the Course table: COURSEKEYCONSTRAINT comes from @MapKeyJoinColumn
        // (enrollments), COURSEKEYSCONSTRAINT from @MapKeyJoinColumns (retakes).
        assertAll("create script",
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENMKJCCOURSE", "COURSEID", "COURSENAME", "PRIMARY KEY (COURSEID)")),
                        "SCHEMAGENMKJCCOURSE table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENMKJCSEMESTER", "SEMESTERID", "SEMESTERNAME", "PRIMARY KEY (SEMESTERID)")),
                        "SCHEMAGENMKJCSEMESTER table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENMKJCSTUDENT", "STUDENTID", "PRIMARY KEY (STUDENTID)")),
                        "SCHEMAGENMKJCSTUDENT table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENMKJC_ENROLLMENTS", "STUDENT_ID", "SEMESTER_ID", "COURSE_KEY")),
                        "SCHEMAGENMKJC_ENROLLMENTS table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("ALTER TABLE", "SCHEMAGENMKJC_ENROLLMENTS ADD",
                                        "CONSTRAINT COURSEKEYCONSTRAINT FOREIGN KEY (COURSE_KEY) REFERENCES SCHEMAGENMKJCCOURSE (COURSEID)")),
                        "@MapKeyJoinColumn foreign key COURSEKEYCONSTRAINT not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENMKJC_RETAKES", "STUDENT_ID", "SEMESTER_ID", "COURSE_KEY")),
                        "SCHEMAGENMKJC_RETAKES table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("ALTER TABLE", "SCHEMAGENMKJC_RETAKES ADD",
                                        "CONSTRAINT COURSEKEYSCONSTRAINT FOREIGN KEY (COURSE_KEY) REFERENCES SCHEMAGENMKJCCOURSE (COURSEID)")),
                        "@MapKeyJoinColumns foreign key COURSEKEYSCONSTRAINT not created"));

        // Step 3: the drop script must remove each foreign key, either explicitly (ALTER TABLE ... DROP)
        // or implicitly (DROP TABLE ... CASCADE), both are valid, and must drop the tables.
        assertAll("drop script",
                () -> assertTrue(findDataInFile(dropScript, List.of("ALTER TABLE", "SCHEMAGENMKJC_ENROLLMENTS DROP"))
                                || findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENMKJC_ENROLLMENTS", "CASCADE")),
                        "SCHEMAGENMKJC_ENROLLMENTS foreign key not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENMKJC_ENROLLMENTS")),
                        "SCHEMAGENMKJC_ENROLLMENTS table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("ALTER TABLE", "SCHEMAGENMKJC_RETAKES DROP"))
                                || findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENMKJC_RETAKES", "CASCADE")),
                        "SCHEMAGENMKJC_RETAKES foreign key not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENMKJC_RETAKES")),
                        "SCHEMAGENMKJC_RETAKES table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENMKJCCOURSE")),
                        "SCHEMAGENMKJCCOURSE table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENMKJCSEMESTER")),
                        "SCHEMAGENMKJCSEMESTER table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENMKJCSTUDENT")),
                        "SCHEMAGENMKJCSTUDENT table not dropped"));

        // Step 4: execute the generated create script against the database.
        props = getPersistenceUnitProperties();
        props.put("jakarta.persistence.schema-generation.database.action", "create");
        props.put("jakarta.persistence.schema-generation.scripts.action", "none");
        props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
        props.put("jakarta.persistence.schema-generation.create-script-source", convertToURI(createScript.getPath()));
        Persistence.generateSchema(getPersistenceUnitName(), props);

        // Step 5: persist and reload the entities to verify the generated schema is usable.
        clearEMAndEMF();
        try {
            var course1 = new Course(1, "course1");
            var course2 = new Course(2, "course2");
            var semester1 = new Semester(1, "semester1");
            var semester2 = new Semester(2, "semester2");
            var student = new Student(1);
            student.getEnrollments().put(course1, semester1);
            student.getEnrollments().put(course2, semester2);
            student.getRetakes().put(course1, semester2);
            var transaction = getEntityTransaction(true);
            transaction.begin();
            getEntityManager().persist(course1);
            getEntityManager().persist(course2);
            getEntityManager().persist(semester1);
            getEntityManager().persist(semester2);
            getEntityManager().persist(student);
            transaction.commit();
            clearCache();
            getEntityManager().clear();

            var foundStudent = getEntityManager().find(Student.class, 1);
            assertNotNull(foundStudent);
            assertEquals(student, foundStudent);
            var enrollments = foundStudent.getEnrollments();
            assertEquals(2, enrollments.size());
            assertEquals(semester1, enrollments.get(course1));
            assertEquals(semester2, enrollments.get(course2));
            var retakes = foundStudent.getRetakes();
            assertEquals(1, retakes.size());
            assertEquals(semester2, retakes.get(course1));
        } finally {
            rollbackIfActive();
            clearEMAndEMF();
        }

        // Step 6: execute the generated drop script.
        props = getPersistenceUnitProperties();
        props.put("jakarta.persistence.schema-generation.database.action", "drop");
        props.put("jakarta.persistence.schema-generation.scripts.action", "none");
        props.put("jakarta.persistence.schema-generation.drop-script-source", convertToURI(dropScript.getPath()));
        Persistence.generateSchema(getPersistenceUnitName(), props);
        clearEMAndEMF();

        // Step 7: the tables no longer exist, so persisting must fail.
        try {
            var transaction = getEntityTransaction(true);
            transaction.begin();
            assertThrows(RuntimeException.class, () -> {
                getEntityManager().persist(new Course(3, "course3"));
                getEntityManager().flush();
            }, "persist should fail after the drop script was executed");
        } finally {
            rollbackIfActive();
            clearEMAndEMF();
        }
    }

    private void rollbackIfActive() {
        var transaction = getEntityTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}
