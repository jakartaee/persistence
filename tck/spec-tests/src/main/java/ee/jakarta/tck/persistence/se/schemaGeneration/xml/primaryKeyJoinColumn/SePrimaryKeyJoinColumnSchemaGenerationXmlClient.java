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

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SePrimaryKeyJoinColumnSchemaGenerationXmlClient extends SchemaGenerationClientBase {

    private static final String TEST_CASE = "jpa_se_schemaGeneration_xml_primaryKeyJoinColumn";

    @Override
    public JavaArchive createDeployment() throws Exception {
        String packageName = SePrimaryKeyJoinColumnSchemaGenerationXmlClient.class.getPackageName();
        String[] classes = {
                packageName + ".Parent",
                packageName + ".Child",
                packageName + ".Child2"};
        return createDeploymentJar(TEST_CASE + ".jar", packageName, classes, new String[] {ORM_XML});
    }

    /**
     * Verifies the {@code primary-key-foreign-key} subelement of {@code entity}
     * and the {@code foreign-key} subelement of {@code primary-key-join-column}
     * in {@code orm.xml} are honored by schema generation for a {@code JOINED}
     * inheritance hierarchy. The generated schema must contain the named foreign
     * key constraints from the subclass tables to the root table, and the
     * database must reject a subclass row without a matching root row.
     */
    @Test
    public void primaryKeyJoinColumnForeignKeyXmlTest() throws Throwable {
        verifyGeneratedSchema(() -> {
            persist(new Child(1, "child"), new Child2(2, "child2"));

            var child = getEntityManager().find(Child.class, 1);
            assertEquals("child", child.getChildName());
            var child2 = getEntityManager().find(Child2.class, 2);
            assertEquals("child2", child2.getChild2Name());

            // The foreign key constraints must exist in the database metadata under the
            // names given in the mapping, on the subclass primary key join columns,
            // referencing the primary key of the root entity table.
            assertForeignKey("SCHEMAGENXMLPKJCCHILD", "XMLPKJCCONSTRAINT", "CHILDID", "SCHEMAGENXMLPKJCPARENT", "PARENTID");
            assertForeignKey("SCHEMAGENXMLPKJCCHILD2", "XMLPKJCSCONSTRAINT", "CHILD2ID", "SCHEMAGENXMLPKJCPARENT", "PARENTID");

            // The foreign key constraints must be enforced: the database must reject a
            // subclass table row whose primary key has no matching row in the root table.
            assertSqlRejected("INSERT INTO SCHEMAGENXMLPKJCCHILD (CHILDID, CHILDNAME) VALUES (99, 'orphan')");
            assertSqlRejected("INSERT INTO SCHEMAGENXMLPKJCCHILD2 (CHILD2ID, CHILD2NAME) VALUES (99, 'orphan')");
        }, "SCHEMAGENXMLPKJCPARENT", "SCHEMAGENXMLPKJCCHILD", "SCHEMAGENXMLPKJCCHILD2");
    }
}
