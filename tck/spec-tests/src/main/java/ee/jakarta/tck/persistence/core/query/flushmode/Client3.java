/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.query.flushmode;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.UtilProductData;

public class Client3 extends UtilProductData {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_query_flushmode3.jar", pkgNameWithoutSuffix, classes);
	}

	public Client3() {
	}

	/*
	 * @testName: secondaryTablesValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:192; PERSISTENCE:JAVADOC:193;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void secondaryTablesValueTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = "20";
		expected[1] = "24";
		expected[2] = "31";
		expected[3] = "37";

		try {
			getEntityTransaction().begin();

			List<Product> result = getEntityManager()
					.createQuery("SELECT p FROM Product p WHERE p.wareHouse = 'Lowell' ").getResultList();

			if (!checkEntityPK(result, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().rollback();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("secondaryTablesValueTest failed");
	}

}
