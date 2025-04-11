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

package ee.jakarta.tck.persistence.core.metamodelapi.entitytype;

import java.lang.System.Logger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.IdentifiableType;
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
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "B", pkgName + "DID2Employee",
				pkgName + "DID2EmployeeId", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_entitytype.jar", pkgNameWithoutSuffix, classes);

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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1280
	 *
	 * @test_Strategy:
	 * 
	 */
	@Test
	public void getName() throws Exception {
		boolean pass = false;

		String expected = "A";
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				String name = eType.getName();
				if (name.equals(expected)) {
					logger.log(Logger.Level.TRACE, "Received:" + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: " + expected + ", actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getName failed");
		}
	}

	/*
	 * @testName: getDeclaredId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1283;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredId() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				logger.log(Logger.Level.TRACE, "entityType Name = A");
				SingularAttribute<A, String> idAttrib = eType.getDeclaredId(java.lang.String.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.String")) {
					logger.log(Logger.Level.TRACE, "Received:" + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredId failed");
		}
	}

	/*
	 * @testName: getDeclaredIdIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1284;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredIdIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained A IdentifiableType");
				try {
					eType.getDeclaredId(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredIdIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1285;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredVersion() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				SingularAttribute<A, Integer> idAttrib = eType.getDeclaredVersion(Integer.class);
				String name = idAttrib.getName();
				if (name.equals("value")) {
					logger.log(Logger.Level.TRACE, "Received:" + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: value, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredVersion failed");
		}
	}

	/*
	 * @testName: getDeclaredVersionIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1286;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredVersionIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {

				try {
					eType.getDeclaredVersion(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredVersionIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1287;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getId() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				SingularAttribute<? super A, String> idAttrib = eType.getId(String.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.String")) {
					logger.log(Logger.Level.TRACE, "Received expected: " + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getId failed");
		}
	}

	/*
	 * @testName: getIdIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1288;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getIdIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained A Entity");
				try {
					eType.getId(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}

			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getIdClassAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1289;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getIdClassAttributes() throws Exception {
		boolean pass = false;

		Set<String> expected = new HashSet<String>();
		expected.add("firstName");
		expected.add("lastName");

		Set<String> actual = new HashSet<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<DID2Employee> eType = metaModel.entity(DID2Employee.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				Set<SingularAttribute<? super DID2Employee, ?>> idClassAttribSet = eType.getIdClassAttributes();
				if (idClassAttribSet != null) {
					if (idClassAttribSet.size() > 0) {
						for (SingularAttribute<? super DID2Employee, ?> attrib : idClassAttribSet) {
							actual.add(attrib.getName());
						}

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logger.log(Logger.Level.TRACE, "Received expected attributes");
							for (String id : expected) {
								logger.log(Logger.Level.TRACE, "id:" + id);
							}
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received Unexpected ids");
							logger.log(Logger.Level.ERROR, "Expected:");
							for (String id : expected) {
								logger.log(Logger.Level.ERROR, "id:" + id);
							}
							logger.log(Logger.Level.ERROR, "Actual:");
							for (String attribName : actual) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
						}
					} else {
						logger.log(Logger.Level.ERROR, "getIdClassAttributes() returned 0 items");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getIdClassAttributes() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdClassAttributes failed");
		}
	}

	/*
	 * @testName: getIdClassAttributesIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1290;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getIdClassAttributesIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained A Entity");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				try {
					eType.getIdClassAttributes();
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdClassAttributesIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getIdType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1291;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getIdType() throws Exception {
		boolean pass = false;

		String expected = "java.lang.String";
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				Type type = eType.getIdType();
				String sType = type.getJavaType().getName();
				if (sType.equals(expected)) {
					logger.log(Logger.Level.TRACE, "Received:" + sType);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: " + expected + ", actual:" + sType);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getIdType failed");
		}
	}

	/*
	 * @testName: getSupertype
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1292;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSupertype() throws Exception {
		boolean pass = false;
		String expected = "ee.jakarta.tck.persistence.core.metamodelapi.entitytype.B";

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				IdentifiableType<? super A> idType = eType.getSupertype();
				String name = idType.getJavaType().getName();
				if (name.equals(expected)) {
					logger.log(Logger.Level.TRACE, "getSuperType() returned:" + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: " + expected + ", actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSupertype failed");
		}
	}

	/*
	 * @testName: getVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1293;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getVersion() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				SingularAttribute<? super A, Integer> idAttrib = eType.getVersion(java.lang.Integer.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.Integer")) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected java.lang.Integer, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getVersion failed");
		}
	}

	/*
	 * @testName: getVersionIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1294;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getVersionIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained A Entity");
				try {
					eType.getVersion(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getVersionIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: hasSingleIdAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1295;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void hasSingleIdAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "entityType Name = " + ((EntityType) eType).getName());
				boolean hasSingleIdAttribute = eType.hasSingleIdAttribute();
				if (hasSingleIdAttribute) {
					pass = true;
					logger.log(Logger.Level.TRACE, "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
				} else {
					logger.log(Logger.Level.ERROR, "Expected: false, actual:" + hasSingleIdAttribute);

				}

			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("hasSingleIdAttribute failed");
		}
	}

	/*
	 * @testName: hasVersionAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1296;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void hasVersionAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "entityType Name = " + ((EntityType) eType).getName());
				boolean hasVersionAttribute = eType.hasVersionAttribute();
				if (hasVersionAttribute) {
					pass = true;
					logger.log(Logger.Level.TRACE, "hasSingleIdAttribute() returned" + hasVersionAttribute);
				} else {
					logger.log(Logger.Level.ERROR, "Expected: false, actual:" + hasVersionAttribute);

				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("hasVersionAttribute failed");
		}
	}

	/*
	 * @testName: getBindableJavaType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1281;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getBindableJavaType() throws Exception {
		boolean pass = false;

		String expected = "ee.jakarta.tck.persistence.core.metamodelapi.entitytype.A";
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				String bindableJavaType = eType.getBindableJavaType().getName();
				if (bindableJavaType != null) {
					if (bindableJavaType.equals(expected)) {
						logger.log(Logger.Level.TRACE, "Received expected result: " + bindableJavaType);
						pass = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected:" + expected + ", actual:" + bindableJavaType);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getName() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableJavaType failed");
		}
	}

	/*
	 * @testName: getBindableType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1282;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getBindableType() throws Exception {
		boolean pass = false;

		String expected = Bindable.BindableType.ENTITY_TYPE.name();
		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<A> eType = metaModel.entity(A.class);
			if (eType != null) {
				String bindableType = eType.getBindableType().name();
				if (bindableType != null) {
					if (bindableType.equals(expected)) {
						logger.log(Logger.Level.TRACE, "Received expected result: " + bindableType);
						pass = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected:" + expected + ", actual:" + bindableType);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getBindableType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getBindableType failed");
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
