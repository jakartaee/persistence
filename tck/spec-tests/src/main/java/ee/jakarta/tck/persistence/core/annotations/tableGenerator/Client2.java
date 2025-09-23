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

package ee.jakarta.tck.persistence.core.annotations.tableGenerator;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client2 extends Client {

	private DataTypes2 d2;

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2", pkgName + "DataTypes3",
				pkgName + "DataTypes4" };
		return createDeploymentJar("jpa_core_annotations_tableGenerator2.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setup2() throws Exception {
		logger.log(Logger.Level.TRACE, "setup2");
		try {

			super.setup();
			createDeployment();
			removeTestData();
			createTestData2();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: generatorOnPropertyTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2111; PERSISTENCE:SPEC:2111.3;
	 * PERSISTENCE:SPEC:2113;
	 * 
	 * @test_Strategy: use a generator specified on a property
	 */
	@Test
	public void generatorOnPropertyTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = d2.getId();
			logger.log(Logger.Level.TRACE, "find id: " + id);
			DataTypes2 d = getEntityManager().find(DataTypes2.class, id);
			if (d != null) {
				if (d.getStringData().equals(d2.getStringData())) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("generatorOnPropertyTest failed");
	}

	// Methods used for Tests

	public void createTestData2() {
		try {
			getEntityTransaction().begin();
			d2 = new DataTypes2();
			d2.setStringData("testData2");
			logger.log(Logger.Level.TRACE, "DataType2:" + d2.toString());
			getEntityManager().persist(d2);

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
	}

}
