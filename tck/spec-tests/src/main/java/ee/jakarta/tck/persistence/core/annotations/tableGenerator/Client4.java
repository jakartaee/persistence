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

public class Client4 extends Client {

	private DataTypes4 d4;

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public Client4() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2", pkgName + "DataTypes3",
				pkgName + "DataTypes4" };
		return createDeploymentJar("jpa_core_annotations_tableGenerator4.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setup4() throws Exception {
		logger.log(Logger.Level.TRACE, "setup4");
		try {

			super.setup();
			createDeployment();

			removeTestData();
			createTestData4();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: generatorGlobalTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2112; PERSISTENCE:SPEC:2113;
	 * 
	 * @test_Strategy: Use the generator defined by another entity
	 */
	@Test
	public void generatorGlobalTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = d4.getId();
			logger.log(Logger.Level.TRACE, "find id: " + id);
			DataTypes4 d = getEntityManager().find(DataTypes4.class, id);
			if (d != null) {
				if (d.getStringData().equals(d4.getStringData())) {
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
			throw new Exception("generatorGlobalTest failed");
	}

	// Methods used for Tests

	public void createTestData4() {
		try {
			getEntityTransaction().begin();

			d4 = new DataTypes4();
			d4.setStringData("testData4");
			logger.log(Logger.Level.TRACE, "DataType4:" + d4.toString());
			getEntityManager().persist(d4);

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
	}

}
