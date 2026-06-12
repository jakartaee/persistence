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

package ee.jakarta.tck.persistence.jpa40.nonnull;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".NonnullBook"};
        return createDeploymentJar("jpa_jpa40_nonnull.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
    }

    /**
     * Verifies {@code @Nonnull} implies {@code optional=false} in the
     * metamodel.
     */
    @Test
    public void nonnullAttributeIsNotOptionalInMetamodelTest() {
        EntityType<NonnullBook> book = getEntityManager()
                .getMetamodel()
                .entity(NonnullBook.class);

        SingularAttribute<? super NonnullBook, String> title =
                book.getSingularAttribute("title", String.class);
        SingularAttribute<? super NonnullBook, String> subtitle =
                book.getSingularAttribute("subtitle", String.class);

        assertFalse(title.isOptional());
        assertTrue(subtitle.isOptional());
    }
}
