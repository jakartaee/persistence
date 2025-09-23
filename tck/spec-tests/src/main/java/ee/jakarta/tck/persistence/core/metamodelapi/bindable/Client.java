/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.metamodelapi.bindable;

import java.lang.System.Logger;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_bindable.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getBindableType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1438; PERSISTENCE:JAVADOC:1225
	 *
	 * @test_Strategy:
	 * 
	 */
	@Test
	public void getBindableType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of EntityType");
				for (EntityType eType : aSet) {
					logger.log(Logger.Level.TRACE, "entity's BindableType is  = " + eType.getBindableType());
					if (eType.getBindableType().equals(Bindable.BindableType.ENTITY_TYPE)) {
						logger.log(Logger.Level.TRACE, "as Expected BindableType is ENTITY_TYPE");
						pass = true;
					} else {
						logger.log(Logger.Level.TRACE, "bindableType is non ENTITY_TYPE");
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableType failed");
		}
	}

	/*
	 * @testName: getBindableJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1224
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getBindableJavaType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of EntityType");
				for (EntityType eType : aSet) {
					logger.log(Logger.Level.TRACE,
							"entity's BindableJavaType is  = " + eType.getBindableJavaType().getName());
					String bindableJavaType = eType.getBindableJavaType().getName();

					if (bindableJavaType != null) {
						if (bindableJavaType.equals("ee.jakarta.tck.persistence.core.metamodelapi.bindable.A")) {
							logger.log(Logger.Level.TRACE, "as Expected BindableJavaType for A is " + bindableJavaType);
							pass = true;
						} else {
							logger.log(Logger.Level.TRACE, "bindableJavaType is incorrect");
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableJavaType failed");
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
			removeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
	}
}
