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

package ee.jakarta.tck.persistence.core.metamodelapi.setattribute;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.Type;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_setattribute.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: getSet
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1271;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSet() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<A> mType = metaModel.managedType(ee.jakarta.tck.persistence.core.metamodelapi.setattribute.A.class);
			if (mType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
						ee.jakarta.tck.persistence.core.metamodelapi.setattribute.Address.class);
				Type t = setAttrib.getElementType();
				if (t != null) {
					logger.log(Logger.Level.TRACE, "element Java Type  = " + t.getJavaType());
					if (t.getJavaType().getName()
							.equals("ee.jakarta.tck.persistence.core.metamodelapi.setattribute.Address")) {
						pass = true;
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSet Test  failed");
		}
	}

	/*
	 * @testName: getCollectionType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1455;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<A> mType = metaModel.managedType(ee.jakarta.tck.persistence.core.metamodelapi.setattribute.A.class);
			if (mType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
						ee.jakarta.tck.persistence.core.metamodelapi.setattribute.Address.class);

				SetAttribute.CollectionType setAttribColType = setAttrib.getCollectionType();
				logger.log(Logger.Level.TRACE, "collection Type = " + setAttrib.getCollectionType());
				if (setAttribColType == SetAttribute.CollectionType.SET) {
					logger.log(Logger.Level.TRACE, "Received expected result = " + setAttribColType);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Received unexpected result = " + setAttribColType);
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionType Test  failed");
		}
	}

	/*
	 * @testName: getElementType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1456;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getElementType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<A> mType = metaModel.managedType(ee.jakarta.tck.persistence.core.metamodelapi.setattribute.A.class);
			if (mType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
						ee.jakarta.tck.persistence.core.metamodelapi.setattribute.Address.class);

				logger.log(Logger.Level.TRACE,
						"collection Element Type = " + setAttrib.getElementType().getJavaType().getName());
				String elementTypeName = setAttrib.getElementType().getJavaType().getName();
				if (elementTypeName.equals("ee.jakarta.tck.persistence.core.metamodelapi.setattribute.Address")) {
					logger.log(Logger.Level.TRACE, "Received expected result = " + elementTypeName);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Received unexpected result = " + elementTypeName);
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getElementType Test  failed");
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
