/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.metamodelapi.attribute;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_core_metamodelapi_attribute.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: getName
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1216
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getName() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
					if (attrib.getName() != null) {

						if (attrib.getName().equals("total")) {
							logger.log(Logger.Level.TRACE, "Received expected result:" + attrib.getName());
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: " + Attribute.PersistentAttributeType.BASIC.toString() + ", actual:"
											+ attrib.getName());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getName Test  failed");
		}
	}

	/*
	 * @testName: getPersistentAttributeType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1217
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPersistentAttributeType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
					Attribute.PersistentAttributeType pAttribType = attrib.getPersistentAttributeType();
					if (pAttribType == Attribute.PersistentAttributeType.BASIC) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + pAttribType);
						pass = true;

					} else {
						logger.log(Logger.Level.ERROR, "Expected: " + Attribute.PersistentAttributeType.BASIC.toString()
								+ ", actual:" + pAttribType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistentAttributeType Test  failed");
		}
	}

	/*
	 * @testName: getDeclaringType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1213
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaringType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
					ManagedType<Order> newTypeOrder = attrib.getDeclaringType();
					if (newTypeOrder != null) {
						Class javaType = newTypeOrder.getJavaType();
						if (javaType.getName().equals("ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order")) {
							logger.log(Logger.Level.TRACE, "Received expected result:" + javaType.getName());
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order, actual:"
											+ javaType.getName());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaringType Test  failed");
		}
	}

	/*
	 * @testName: getJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1215
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getJavaType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute JavaType = " + attrib.getJavaType());
					Class pAttribJavaType = attrib.getJavaType();
					if (pAttribJavaType.getName().equals("int")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + pAttribJavaType);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: int, actual:" + pAttribJavaType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaType Test  failed");
		}
	}

	/*
	 * @testName: getJavaMember
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1214
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getJavaMember() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute JavaMember = " + attrib.getJavaMember().getName());
					java.lang.reflect.Member javaMember = attrib.getJavaMember();
					if (javaMember.getName().equals("getTotal")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + javaMember.getName());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: getTotal, actual:" + javaMember.getName());
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaMember Test  failed");
		}
	}

	/*
	 * @testName: isAssociation
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1218
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isAssociation() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute IsAssociation = " + attrib.isAssociation());
					if (!attrib.isAssociation()) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + attrib.isAssociation());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Received unexpected result: " + attrib.isAssociation());
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isAssociation Test  failed");
		}
	}

	/*
	 * @testName: isCollection
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1219
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isCollection() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.attribute.Order.class);
			if (mTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute IsCollection = " + attrib.isCollection());
					if (!attrib.isCollection()) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + attrib.isCollection());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Received unexpected result: " + attrib.isCollection());
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isCollection Test  failed");
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
