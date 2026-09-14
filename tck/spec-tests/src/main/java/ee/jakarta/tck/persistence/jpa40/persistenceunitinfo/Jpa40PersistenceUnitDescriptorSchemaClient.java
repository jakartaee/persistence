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

package ee.jakarta.tck.persistence.jpa40.persistenceunitinfo;

import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifies the Jakarta Persistence 4.0 schema grammar for explicitly listed
 * ordinary Java types, package descriptors, and module descriptors.
 *
 * @author Steve Ebersole
 */
public class Jpa40PersistenceUnitDescriptorSchemaClient {

    private static final String VALID_DESCRIPTOR_LISTS = """
            <persistence xmlns="https://jakarta.ee/xml/ns/persistence" version="4.0">
                <persistence-unit name="descriptor-lists">
                    <class>com.example.model.Book</class>
                    <class>com.example.model.Author</class>
                    <package-descriptor>com.example.model</package-descriptor>
                    <package-descriptor>com.example.shared</package-descriptor>
                    <module-descriptor>com.example.persistence</module-descriptor>
                    <module-descriptor>com.example.persistence.extensions</module-descriptor>
                </persistence-unit>
            </persistence>
            """;

    private static final String INVALID_DESCRIPTOR_PLACEMENT = """
            <persistence xmlns="https://jakarta.ee/xml/ns/persistence" version="4.0">
                <persistence-unit name="descriptor-placement">
                    <package-descriptor>com.example.model</package-descriptor>
                    <class>com.example.model.Book</class>
                </persistence-unit>
            </persistence>
            """;

    @Test
    public void multipleCategorizedDescriptorElementsAreValid() throws Exception {
        schema().newValidator().validate(source(VALID_DESCRIPTOR_LISTS));
    }

    @Test
    public void descriptorElementsFollowSchemaOrdering() throws Exception {
        assertThrows(
                SAXException.class,
                () -> schema().newValidator().validate(source(INVALID_DESCRIPTOR_PLACEMENT))
        );
    }

    private static Schema schema() throws Exception {
        URL schemaUrl = Persistence.class.getResource("/jakarta/persistence/persistence_4_0.xsd");
        assertNotNull(schemaUrl, "Jakarta Persistence 4.0 schema resource");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return factory.newSchema(schemaUrl);
    }

    private static StreamSource source(String xml) {
        return new StreamSource(new StringReader(xml));
    }
}
