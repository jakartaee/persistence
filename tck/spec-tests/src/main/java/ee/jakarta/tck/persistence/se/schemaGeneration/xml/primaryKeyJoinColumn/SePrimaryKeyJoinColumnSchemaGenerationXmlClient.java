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

package ee.jakarta.tck.persistence.se.schemaGeneration.xml.primaryKeyJoinColumn;

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

public class SePrimaryKeyJoinColumnSchemaGenerationXmlClient extends PMClientBase {

    private static final String TEST_CASE = "jpa_se_schemaGeneration_xml_primaryKeyJoinColumn";

    private String schemaGenerationDir;

    public JavaArchive createDeployment() throws Exception {
        String packageName = SePrimaryKeyJoinColumnSchemaGenerationXmlClient.class.getPackageName();
        String[] classes = {
                packageName + ".Parent",
                packageName + ".Child",
                packageName + ".Child2"};
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
     * Verifies the {@code primary-key-foreign-key} subelement of {@code entity}
     * and the {@code foreign-key} subelement of {@code primary-key-join-column}
     * in {@code orm.xml} are honored by schema generation for a {@code JOINED}
     * inheritance hierarchy. The generated create script must contain the named
     * foreign key constraints from the subclass tables to the root table, the
     * generated scripts must be executable, and the drop script must remove the
     * tables.
     */
    @Test
    public void primaryKeyJoinColumnForeignKeyXmlTest() throws Exception {
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

        // Step 2: the create script must contain the tables and the named foreign keys from each subclass
        // table to the root table. The trailing space in "...CHILD " prevents also matching the CHILD2 table.
        assertAll("create script",
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENXMLPKJCPARENT", "PARENTID", "PRIMARY KEY (PARENTID)")),
                        "SCHEMAGENXMLPKJCPARENT table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENXMLPKJCCHILD ", "CHILDID", "CHILDNAME", "PRIMARY KEY (CHILDID)")),
                        "SCHEMAGENXMLPKJCCHILD table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("CREATE TABLE SCHEMAGENXMLPKJCCHILD2", "CHILD2ID", "CHILD2NAME", "PRIMARY KEY (CHILD2ID)")),
                        "SCHEMAGENXMLPKJCCHILD2 table not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("ALTER TABLE", "SCHEMAGENXMLPKJCCHILD ADD",
                                        "CONSTRAINT XMLPKJCCONSTRAINT FOREIGN KEY (CHILDID) REFERENCES SCHEMAGENXMLPKJCPARENT (PARENTID)")),
                        "<primary-key-foreign-key> foreign key XMLPKJCCONSTRAINT not created"),
                () -> assertTrue(findDataInFile(createScript,
                                List.of("ALTER TABLE", "SCHEMAGENXMLPKJCCHILD2 ADD",
                                        "CONSTRAINT XMLPKJCSCONSTRAINT FOREIGN KEY (CHILD2ID) REFERENCES SCHEMAGENXMLPKJCPARENT (PARENTID)")),
                        "<primary-key-join-column><foreign-key> foreign key XMLPKJCSCONSTRAINT not created"));

        // Step 3: the drop script must remove each foreign key, either explicitly (ALTER TABLE ... DROP)
        // or implicitly (DROP TABLE ... CASCADE), both are valid, and must drop the tables.
        assertAll("drop script",
                () -> assertTrue(findDataInFile(dropScript, List.of("ALTER TABLE", "SCHEMAGENXMLPKJCCHILD DROP"))
                                || findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLPKJCCHILD ", "CASCADE")),
                        "SCHEMAGENXMLPKJCCHILD foreign key not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("ALTER TABLE", "SCHEMAGENXMLPKJCCHILD2 DROP"))
                                || findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLPKJCCHILD2", "CASCADE")),
                        "SCHEMAGENXMLPKJCCHILD2 foreign key not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLPKJCPARENT")),
                        "SCHEMAGENXMLPKJCPARENT table not dropped"),
                () -> assertTrue(findDataInFile(dropScript, List.of("DROP TABLE", "SCHEMAGENXMLPKJCCHILD2")),
                        "SCHEMAGENXMLPKJCCHILD2 table not dropped"));

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
            var child = new Child(1, "child");
            var child2 = new Child2(2, "child2");
            var transaction = getEntityTransaction(true);
            transaction.begin();
            getEntityManager().persist(child);
            getEntityManager().persist(child2);
            transaction.commit();
            clearCache();
            getEntityManager().clear();

            var foundChild = getEntityManager().find(Child.class, 1);
            assertNotNull(foundChild);
            assertEquals(child, foundChild);
            assertEquals("child", foundChild.getChildName());
            var foundChild2 = getEntityManager().find(Child2.class, 2);
            assertNotNull(foundChild2);
            assertEquals(child2, foundChild2);
            assertEquals("child2", foundChild2.getChild2Name());
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
                getEntityManager().persist(new Child(3, "child3"));
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
