/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.query.language;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.UtilPhoneData;

public class Client5 extends UtilPhoneData {

	private static final Logger logger = (Logger) System.getLogger(Client5.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_query_language5.jar", pkgNameWithoutSuffix, classes);
	}
	/* Run test */

	/*
	 * @testName: queryTest55
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358
	 * 
	 * @test_Strategy: The LIKE expression uses an input parameter for the
	 * condition. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupPhoneData")
	@Test
	public void queryTest55() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine which customers have an area code beginning with 9");
			c = getEntityManager()
					.createQuery(
							"SELECT Distinct Object(c) From Customer c, IN(c.home.phones) p where p.area LIKE :area")
					.setParameter("area", "9%").getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "3";
			expectedPKs[1] = "12";
			expectedPKs[2] = "16";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest55 failed");
	}

}
