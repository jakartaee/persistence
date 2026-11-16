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

public class SeMapKeyJoinColumnSchemaGenerationXmlClient extends PMClientBase {

    private static final String TEST_CASE = "jpa_se_schemaGeneration_xml_mapKeyJoinColumn";

    private String schemaGenerationDir;

    public JavaArchive createDeployment() throws Exception {
        String packageName = SeMapKeyJoinColumnSchemaGenerationXmlClient.class.getPackageName();
        String[] classes = {
                packageName + ".Course",
                packageName + ".Semester",
                packageName + ".Student"};
        return createDeploymentJar(TEST_CASE + ".jar", packageName, classes, new String[] {ORM_XML});
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
     * Verifies the {@code map-key-foreign-key} subelement of {@code many-to-many}
     * in {@code orm.xml} is honored by schema generation. The generated create
     * script must contain the named foreign key constraint from the map key join
     * column of the join table to the map key entity table, the generated scripts
     * must be executable, and the drop script must remove the tables.
     */
    @Test
    public void mapKeyJoinColumnForeignKeyXmlTest() throws Exception {
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

        // Step 2: the create script must contain the tables and the named foreign key, defined by
        // <map-key-foreign-key>, from the map key column of the join table to the Course table.
        assertAll("create script",
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENXMLMKJCCOURSE", "COURSEID", "COURSENAME", "PRIMARY KEY (COURSEID)")),
                        "SCHEMAGENXMLMKJCCOURSE table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENXMLMKJCSEMESTER", "SEMESTERID", "SEMESTERNAME", "PRIMARY KEY (SEMESTERID)")),
                        "SCHEMAGENXMLMKJCSEMESTER table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENXMLMKJCSTUDENT", "STUDENTID", "PRIMARY KEY (STUDENTID)")),
                        "SCHEMAGENXMLMKJCSTUDENT table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENXMLMKJC_ENROLLMENTS", "STUDENT_ID", "SEMESTER_ID", "COURSE_KEY")),
                        "SCHEMAGENXMLMKJC_ENROLLMENTS table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("ALTER TABLE", "SCHEMAGENXMLMKJC_ENROLLMENTS ADD",
                                        "CONSTRAINT XMLCOURSEKEYCONSTRAINT FOREIGN KEY (COURSE_KEY) REFERENCES SCHEMAGENXMLMKJCCOURSE (COURSEID)")),
                        "<map-key-foreign-key> foreign key XMLCOURSEKEYCONSTRAINT not created"));

        // Step 3: the drop script must remove each foreign key, either explicitly (ALTER TABLE ... DROP)
        // or implicitly (DROP TABLE ... CASCADE), both are valid, and must drop the tables.
        assertAll("drop script",
                () -> assertTrue(findDataInFile(dropScript, List.of("ALTER TABLE", "SCHEMAGENXMLMKJC_ENROLLMENTS DROP"))
                                || findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLMKJC_ENROLLMENTS", "CASCADE")),
                        "SCHEMAGENXMLMKJC_ENROLLMENTS foreign key not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLMKJC_ENROLLMENTS")),
                        "SCHEMAGENXMLMKJC_ENROLLMENTS table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLMKJCCOURSE")),
                        "SCHEMAGENXMLMKJCCOURSE table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLMKJCSEMESTER")),
                        "SCHEMAGENXMLMKJCSEMESTER table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLMKJCSTUDENT")),
                        "SCHEMAGENXMLMKJCSTUDENT table not dropped"));

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
