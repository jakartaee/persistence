/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa40.xmlstatement;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".XmlStatementBook"};
        String[] xmlFiles = { ORM_XML };
        return createDeploymentJar("jpa_jpa40_xmlstatement.jar", packageName, classes, xmlFiles);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests Jakarta Persistence 4.0 XML named statement declarations. The test
     * verifies that {@code named-statement} and {@code named-native-statement}
     * elements define executable statements obtained through
     * {@code createNamedStatement()}.
     */
    @Test
    public void xmlNamedAndNativeStatementExecutionTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int namedCount = getEntityManager()
                .createNamedStatement(XmlStatementBook.XML_UPDATE_TITLE)
                .setParameter("title", "XML Named")
                .setParameter("id", 1)
                .execute();
        int nativeCount = getEntityManager()
                .createNamedStatement(XmlStatementBook.XML_NATIVE_UPDATE_TITLE)
                .setParameter(1, "XML Native")
                .setParameter(2, 2)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, namedCount);
        assertEquals(1, nativeCount);
        assertEquals("XML Named", getEntityManager().find(XmlStatementBook.class, 1).getTitle());
        assertEquals("XML Native", getEntityManager().find(XmlStatementBook.class, 2).getTitle());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new XmlStatementBook(1, "Alpha"));
        getEntityManager().persist(new XmlStatementBook(2, "Beta"));
        transaction.commit();
        getEntityManager().clear();
    }
}
