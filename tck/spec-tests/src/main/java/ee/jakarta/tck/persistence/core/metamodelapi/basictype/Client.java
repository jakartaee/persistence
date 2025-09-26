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

package ee.jakarta.tck.persistence.core.metamodelapi.basictype;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.Type;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_core_metamodelapi_basictype.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: getJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1222
	 *
	 * @test_Strategy: Get the javaType of the ID
	 *
	 */
	@Test
	public void getJavaType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute attrib = mTypeOrder.getDeclaredAttribute("id");
				if (attrib != null) {
					String type = attrib.getJavaType().getName();
					if (type.equals("int")) {
						logger.log(Logger.Level.TRACE, "Received expected type: int");
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: int, actual:" + type);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredAttribute(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "managedType(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaType Test failed");
		}
	}

	/*
	 * @testName: getPersistenceType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1223
	 *
	 * @test_Strategy: Get the persistent type of the Order class
	 *
	 */
	@Test
	public void getPersistenceType() throws Exception {
		boolean pass = false;

		String expected = Type.PersistenceType.ENTITY.name();
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<Order> eType = metaModel.entity(Order.class);
			if (eType != null) {
				Type.PersistenceType type = eType.getPersistenceType();
				if (type != null) {
					String sType = type.name();
					if (sType.equals(expected)) {
						logger.log(Logger.Level.TRACE, "Received:" + sType);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: " + expected + ", actual:" + sType);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getPersistenceType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistenceType failed");
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
