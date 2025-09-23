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

package ee.jakarta.tck.persistence.core.metamodelapi.singularattribute;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "B", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_singularattribute.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: isId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1458
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isId() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);

				if (!singAttrib.isId()) {
					logger.log(Logger.Level.TRACE,
							"Received expected result singular attribute isId =" + singAttrib.isId());
					pass = true;
				} else {
					logger.log(Logger.Level.TRACE,
							"Received UnExpected result singular attribute isId =" + singAttrib.isId());
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isId Test  failed");
		}
	}

	/*
	 * @testName: isVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1460
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isVersion() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);

				if (!singAttrib.isVersion()) {
					logger.log(Logger.Level.TRACE,
							"Received expected result singular attribute isVersion =" + singAttrib.isVersion());
					pass = true;
				} else {
					logger.log(Logger.Level.TRACE,
							"Received UnExpected result singular attribute isVersion =" + singAttrib.isVersion());
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isVersion Test  failed");
		}
	}

	/*
	 * @testName: isOptional
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1459
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isOptional() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);

				if (singAttrib.isOptional()) {
					logger.log(Logger.Level.TRACE,
							"Received expected result singular attribute isOptional =" + singAttrib.isOptional());
					pass = true;
				} else {
					logger.log(Logger.Level.TRACE,
							"Received UnExpected result singular attribute isOptional =" + singAttrib.isOptional());
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isOptional Test  failed");
		}
	}

	/*
	 * @testName: getType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1457
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);

				Type attributeType = singAttrib.getType();
				String attributeJavaTypeName = attributeType.getJavaType().getName();
				if (attributeJavaTypeName.equals("ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address")) {
					logger.log(Logger.Level.TRACE,
							"Received expected result singular attribute JavaType =" + attributeJavaTypeName);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Received Unexpected result singular attribute JavaType =" + attributeJavaTypeName);
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getType Test  failed");
		}
	}

	/*
	 * @testName: isCollection
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1467;
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);

				boolean b = singAttrib.isCollection();
				if (!b) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + b);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: false, actual: " + b);
				}
			} else {
				logger.log(Logger.Level.ERROR, "managedType() returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isCollection Test failed");
		}
	}

	/*
	 * @testName: isAssociation
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1466;
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);

				boolean b = singAttrib.isAssociation();
				if (!b) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + b);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: false, actual: " + b);
				}
			} else {
				logger.log(Logger.Level.ERROR, "managedType() returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isAssociation Test failed");
		}
	}

	/*
	 * @testName: getPersistentAttributeType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1465;
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);
				if (singAttrib != null) {
					Attribute.PersistentAttributeType pAttribType = singAttrib.getPersistentAttributeType();
					if (pAttribType == Attribute.PersistentAttributeType.EMBEDDED) {
						logger.log(Logger.Level.TRACE, "Received expected result " + pAttribType);
						pass = true;

					} else {
						logger.log(Logger.Level.ERROR, "Expected: "
								+ Attribute.PersistentAttributeType.EMBEDDED.toString() + ", actual:" + pAttribType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistentAttributeType Test failed");
		}
	}

	/*
	 * @testName: getName
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1464;
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);
				if (singAttrib != null) {
					String name = singAttrib.getName();
					if (name.equals("address")) {
						logger.log(Logger.Level.TRACE, "Received expected result" + name);
						pass = true;

					} else {
						logger.log(Logger.Level.ERROR, "Expected: address, actual:" + name);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getName Test failed");
		}
	}

	/*
	 * @testName: getJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1463;
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);
				if (singAttrib != null) {
					Class pSingAttribJavaType = singAttrib.getJavaType();
					if (pSingAttribJavaType.getName()
							.equals("ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address")) {
						logger.log(Logger.Level.TRACE, "Received expected result " + pSingAttribJavaType);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: address, actual:" + pSingAttribJavaType);
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getJavaType Test failed");
		}
	}

	/*
	 * @testName: getJavaMember
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1462;
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE,
							"Singular attribute JavaMember = " + singAttrib.getJavaMember().getName());
					java.lang.reflect.Member javaMember = singAttrib.getJavaMember();
					if (javaMember.getName().equals("address")) {
						logger.log(Logger.Level.TRACE, "Received expected result " + javaMember.getName());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: address, actual:" + javaMember.getName());
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
	 * @testName: getDeclaringType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1461
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + singAttrib.getName());
					ManagedType<B> newTypeOrder = singAttrib.getDeclaringType();
					if (newTypeOrder != null) {
						Class javaType = newTypeOrder.getJavaType();
						if (javaType.getName().equals("ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B")) {
							logger.log(Logger.Level.TRACE, "Received expected result:" + javaType.getName());
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B, actual:"
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
	 * @testName: getBindableType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1469;
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
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + singAttrib.getName());
					Bindable.BindableType bType = singAttrib.getBindableType();
					if (bType != null) {

						if (bType.name().equals(Bindable.BindableType.SINGULAR_ATTRIBUTE.name())) {
							logger.log(Logger.Level.TRACE, "Received expected result:" + bType.name());
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected: "
									+ Bindable.BindableType.SINGULAR_ATTRIBUTE.name() + ", actual:" + bType.name());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableType Test  failed");
		}
	}

	/*
	 * @testName: getBindableJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1468;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getBindableJavaType() throws Exception {
		boolean pass = false;
		String expected = "ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address";
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<B> mTypeB = metaModel
					.managedType(ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.B.class);
			if (mTypeB != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
						ee.jakarta.tck.persistence.core.metamodelapi.singularattribute.Address.class);
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + singAttrib.getName());
					Class cType = singAttrib.getBindableJavaType();
					if (cType != null) {
						if (cType.getName().equals(expected)) {
							logger.log(Logger.Level.TRACE, "Received expected result:" + cType.getName());
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected: " + expected + ", actual:" + cType.getName());
						}
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableJavaType Test  failed");
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
