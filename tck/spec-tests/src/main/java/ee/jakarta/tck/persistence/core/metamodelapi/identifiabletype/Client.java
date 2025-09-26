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

package ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.IdentifiableType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SetAttribute;
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
		return createDeploymentJar("jpa_core_metamodelapi_identifiabletype.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: getId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1301
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				SingularAttribute<? super A, String> idAttrib = iType.getId(String.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.String")) {
					logger.log(Logger.Level.TRACE, "Received expected: " + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1302
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained A Entity");
				try {
					iType.getId(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}

			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @testName: getVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1307
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				SingularAttribute<? super A, Integer> idAttrib = iType.getVersion(java.lang.Integer.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.Integer")) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected java.lang.Integer, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1308
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained A Entity");
				try {
					iType.getVersion(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @testName: getDeclaredId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1297
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				logger.log(Logger.Level.TRACE, "entityType Name = A");
				SingularAttribute<A, String> idAttrib = iType.getDeclaredId(java.lang.String.class);
				String name = idAttrib.getType().getJavaType().getName();
				if (name.equals("java.lang.String")) {
					logger.log(Logger.Level.TRACE, "Received expected name:" + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1298
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained A IdentifiableType");
				try {
					iType.getDeclaredId(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1299
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
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of EntityType");
				IdentifiableType<A> iType = metaModel.entity(A.class);

				logger.log(Logger.Level.TRACE, "entityType Name = " + ((EntityType) iType).getName());
				SingularAttribute<A, Integer> idAttrib = iType.getDeclaredVersion(Integer.class);
				String name = idAttrib.getName();
				if (name.equals("value")) {
					logger.log(Logger.Level.TRACE, "Received:" + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: value, actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getEntities(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1300
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
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of EntityType");
				IdentifiableType<A> iType = metaModel.entity(A.class);
				logger.log(Logger.Level.TRACE, "entityType Name = " + ((EntityType) iType).getName());
				try {
					iType.getDeclaredVersion(Date.class);
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}

			} else {
				logger.log(Logger.Level.ERROR, "getEntities(...) returned null");
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
	 * @testName: getSupertype
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1306
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSupertype() throws Exception {
		boolean pass = false;
		String expected = "ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.B";

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "entityType Name = " + ((EntityType) iType).getName());
				IdentifiableType<? super A> idType = iType.getSupertype();
				String name = idType.getJavaType().getName();
				if (name.equals(expected)) {
					logger.log(Logger.Level.TRACE, "getSuperType() returned:" + name);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected: " + expected + ", actual:" + name);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @testName: hasSingleIdAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1309
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {

				logger.log(Logger.Level.TRACE, "entityType Name = " + ((EntityType) iType).getName());
				boolean hasSingleIdAttribute = iType.hasSingleIdAttribute();
				if (hasSingleIdAttribute) {
					pass = true;
					logger.log(Logger.Level.TRACE, "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
				} else {
					logger.log(Logger.Level.ERROR, "Expected: false, actual:" + hasSingleIdAttribute);

				}

			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1310
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {

				logger.log(Logger.Level.TRACE, "entityType Name = " + ((EntityType) iType).getName());
				boolean hasVersionAttribute = iType.hasVersionAttribute();
				if (hasVersionAttribute) {
					pass = true;
					logger.log(Logger.Level.TRACE, "hasSingleIdAttribute() returned" + hasVersionAttribute);
				} else {
					logger.log(Logger.Level.ERROR, "Expected: false, actual:" + hasVersionAttribute);

				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @testName: getIdClassAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1303
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getIdClassAttributes() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			// Set<EntityType<?>> aSet = metaModel.getEntities();
			IdentifiableType<DID2Employee> iType = metaModel.entity(DID2Employee.class);
			if (iType != null) {

				Set<SingularAttribute<? super DID2Employee, ?>> idClassAttribSet = iType.getIdClassAttributes();
				if (idClassAttribSet != null) {
					for (SingularAttribute<? super DID2Employee, ?> attrib : idClassAttribSet) {
						logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
					}
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1304
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
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {

				try {
					iType.getIdClassAttributes();
					logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1305
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getIdType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Type idType = iType.getIdType();
				if (idType != null) {
					logger.log(Logger.Level.TRACE, "idType Name = " + idType.getJavaType());
					String name = idType.getJavaType().getName();

					if (name.equals("java.lang.String")) {
						logger.log(Logger.Level.TRACE, "Received expected: " + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
					}

				} else {
					logger.log(Logger.Level.ERROR, "getIdType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
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
	 * @testName: getAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1311;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Attribute attrib = iType.getAttribute("id");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
					String name = attrib.getName();
					if (name.equals("id")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: id, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getAttribute(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getAttribute failed");
		}
	}

	/*
	 * @testName: getAttributeIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1312;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttributeIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				try {
					iType.getAttribute("doesnotexist");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getAttributeIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1313;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cAddress");
		expected.add("cAddress_inherited");
		expected.add("id");
		expected.add("lAddress");
		expected.add("lAddress_inherited");
		expected.add("mAddress");
		expected.add("mAddress_inherited");
		expected.add("name");
		expected.add("sAddress");
		expected.add("sAddress_inherited");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Set set = iType.getAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logger.log(Logger.Level.TRACE, "Received expected attributes");
							for (String attribName : expected) {
								logger.log(Logger.Level.TRACE, "attrib:" + attribName);
							}
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected attributes");
							logger.log(Logger.Level.ERROR, "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
							logger.log(Logger.Level.ERROR, "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
						}
					} else {
						logger.log(Logger.Level.ERROR, "getAttributes() returned 0 results");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getAttributes() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getAttributes failed");
		}
	}

	/*
	 * @testName: getCollectionStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1314;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress", Address.class);
				if (cAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: cAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getCollection(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionStringClass failed");
		}
	}

	/*
	 * @testName: getCollectionStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1315;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getCollection("doesnotexist", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getCollectionString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1316;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress");
				if (cAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: cAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getCollection(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionString failed");
		}
	}

	/*
	 * @testName: getCollectionStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1317;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getCollection("doesnotexist");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getCollectionStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1318;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttribute() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Attribute attrib = iType.getDeclaredAttribute("cAddress");
				if (attrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
					String name = attrib.getName();
					if (name.equals("cAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: cAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredAttribute(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredAttribute failed");
		}
	}

	/*
	 * @testName: getDeclaredAttributeIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1319;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttributeIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredAttribute("cAddress_inherited");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredAttributeIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1320;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("id");
		expected.add("cAddress");
		expected.add("lAddress");
		expected.add("mAddress");
		expected.add("sAddress");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Set set = iType.getDeclaredAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logger.log(Logger.Level.TRACE, "Received expected attributes");
							for (String attribName : expected) {
								logger.log(Logger.Level.TRACE, "attrib:" + attribName);
							}
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected attributes");
							logger.log(Logger.Level.ERROR, "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
							logger.log(Logger.Level.ERROR, "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
						}
					} else {
						logger.log(Logger.Level.ERROR, "getAttributes() returned 0 results");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredAttributes() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredAttributes failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1321;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress", Address.class);
				if (cAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: cAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getCollection(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1322;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredCollection("cAddress_inherited", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1323;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				CollectionAttribute cAttrib = iType.getCollection("cAddress", Address.class);
				if (cAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + cAttrib.getName());
					String name = cAttrib.getName();
					if (name.equals("cAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: cAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getCollection(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionString failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1324;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredCollection("cAddress_inherited", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredCollectionStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1325;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getDeclaredList("lAddress", Address.class);
				if (lAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: lAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredList(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1326;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredList("lAddress_inherited", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredListString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1327;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getDeclaredList("lAddress");
				if (lAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: lAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredList(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListString failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1328;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredList("lAddress_inherited");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredListStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringClassClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1329;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringClassClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getDeclaredMap("mAddress", Address.class, String.class);
				if (mAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: mAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredMap(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapStringClassClass failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringClassClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1330;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringClassClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredMap("mAddress_inherited", Address.class, String.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapStringClassClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredMapString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1331;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getDeclaredMap("mAddress");
				if (mAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: mAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredMap(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapString failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1332;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredMap("mAddress_inherited");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredMapStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1334;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getDeclaredSet("sAddress", Address.class);
				if (sAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: sAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredSet(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1335;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredSet("sAddress_inherited", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSetString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1336;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getDeclaredSet("sAddress");
				if (sAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: sAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredSet(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetString failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1337;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredSet("sAddress_inherited");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSetStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1338;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SingularAttribute<A, Integer> singAttrib = iType.getDeclaredSingularAttribute("value", Integer.class);
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("value")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: value, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredSingularAttribute(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringClass failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1339;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				try {
					iType.getDeclaredSingularAttribute("name", String.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1340;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SingularAttribute singAttrib = iType.getDeclaredSingularAttribute("value");
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("value")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: value, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredSingularAttribute(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeString failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1341;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				try {
					iType.getDeclaredSingularAttribute("name");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1342;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	@Disabled
	public void getDeclaredSingularAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("id");
		expected.add("name");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Set set = iType.getDeclaredSingularAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logger.log(Logger.Level.TRACE, "Received expected attributes");
							for (String attribName : expected) {
								logger.log(Logger.Level.TRACE, "attrib:" + attribName);
							}
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected attributes");
							logger.log(Logger.Level.ERROR, "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
							logger.log(Logger.Level.ERROR, "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
						}
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredSingularAttributes() returned 0 results");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getDeclaredSingularAttributes() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredSingularAttributes failed");
		}
	}

	/*
	 * @testName: getListStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1343;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getList("lAddress", Address.class);
				if (lAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: lAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getList(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListStringClass failed");
		}
	}

	/*
	 * @testName: getListStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1344;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getList("doesnotexist", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getListString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1345;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				ListAttribute lAttrib = iType.getList("lAddress");
				if (lAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + lAttrib.getName());
					String name = lAttrib.getName();
					if (name.equals("lAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: lAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getList(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListString failed");
		}
	}

	/*
	 * @testName: getListStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1346;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getDeclaredList("doesnotexist");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getListStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getMapStringClassClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1347;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringClassClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getMap("mAddress", Address.class, String.class);
				if (mAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: mAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getMap(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapStringClassClass failed");
		}
	}

	/*
	 * @testName: getMapStringClassClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1348;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringClassClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getMap("doesnotexist", Address.class, String.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapStringClassClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getMapString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1349;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				MapAttribute mAttrib = iType.getMap("mAddress");
				if (mAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + mAttrib.getName());
					String name = mAttrib.getName();
					if (name.equals("mAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: mAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getMap(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapString failed");
		}
	}

	/*
	 * @testName: getMapStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1350;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getMap("doesnotexist");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getMapStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getPluralAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1351;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPluralAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cAddress");
		expected.add("cAddress_inherited");
		expected.add("lAddress");
		expected.add("lAddress_inherited");
		expected.add("mAddress");
		expected.add("mAddress_inherited");
		expected.add("sAddress");
		expected.add("sAddress_inherited");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Set set = iType.getPluralAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logger.log(Logger.Level.TRACE, "Received expected attributes");
							for (String attribName : expected) {
								logger.log(Logger.Level.TRACE, "attrib:" + attribName);
							}
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected attributes");
							logger.log(Logger.Level.ERROR, "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
							logger.log(Logger.Level.ERROR, "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
						}
					} else {
						logger.log(Logger.Level.ERROR, "getPluralAttributes() returned 0 results");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getPluralAttributes() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPluralAttributes failed");
		}
	}

	/*
	 * @testName: getDeclaredPluralAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1333;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredPluralAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("cAddress");
		expected.add("lAddress");
		expected.add("mAddress");
		expected.add("sAddress");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Set set = iType.getDeclaredPluralAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logger.log(Logger.Level.TRACE, "Received expected attributes");
							for (String attribName : expected) {
								logger.log(Logger.Level.TRACE, "attrib:" + attribName);
							}
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected attributes");
							logger.log(Logger.Level.ERROR, "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
							logger.log(Logger.Level.ERROR, "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
						}
					} else {
						logger.log(Logger.Level.ERROR, "getPluralAttributes() returned 0 results");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getPluralAttributes() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getDeclaredPluralAttributes failed");
		}
	}

	/*
	 * @testName: getSetStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1352;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getSet("sAddress", Address.class);
				if (sAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: sAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSet(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetStringClass failed");
		}
	}

	/*
	 * @testName: getSetStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1353;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getSet("doesnotexist", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSetString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1354;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SetAttribute sAttrib = iType.getSet("sAddress");
				if (sAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + sAttrib.getName());
					String name = sAttrib.getName();
					if (name.equals("sAddress")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: sAddress, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSet(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetString failed");
		}
	}

	/*
	 * @testName: getSetStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1355;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				try {
					iType.getSet("doesnotexist");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSetStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1356;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringClass() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SingularAttribute singAttrib = iType.getSingularAttribute("name", String.class);
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("name")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: name, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSingularAttribute(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeStringClass failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringClassIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1357;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringClassIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				try {
					iType.getSingularAttribute("doesnotexist", Address.class);
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeStringClassIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSingularAttributeString
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1358;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeString() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				SingularAttribute singAttrib = iType.getSingularAttribute("name");
				if (singAttrib != null) {
					logger.log(Logger.Level.TRACE, "attribute Name = " + singAttrib.getName());
					String name = singAttrib.getName();
					if (name.equals("name")) {
						logger.log(Logger.Level.TRACE, "Received expected result:" + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: name, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSingularAttribute(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeString failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1359;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				try {
					iType.getSingularAttribute("doesnotexist");
					logger.log(Logger.Level.ERROR, "Did not receive expected IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributeStringIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getSingularAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1360;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();

		expected.add("id");
		expected.add("name");
		expected.add("value");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			IdentifiableType<A> iType = metaModel.entity(A.class);
			if (iType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Entity A");
				Set set = iType.getSingularAttributes();
				if (set != null) {
					if (set.size() > 0) {
						for (Iterator i = set.iterator(); i.hasNext();) {
							Attribute attrib = (Attribute) i.next();
							actual.add(attrib.getName());
						}
						Collections.sort(actual);

						if (expected.containsAll(actual) && actual.containsAll(expected)
								&& expected.size() == actual.size()) {

							logger.log(Logger.Level.TRACE, "Received expected attributes");
							for (String attribName : expected) {
								logger.log(Logger.Level.TRACE, "attrib:" + attribName);
							}
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected attributes");
							logger.log(Logger.Level.ERROR, "Expected(" + expected.size() + "):");
							for (String attribName : expected) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
							logger.log(Logger.Level.ERROR, "Actual(" + actual.size() + "):");
							for (String attribName : actual) {
								logger.log(Logger.Level.ERROR, "attrib:" + attribName);
							}
						}
					} else {
						logger.log(Logger.Level.ERROR, "getSingularAttributes() returned 0 results");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSingularAttributes(...) returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "entity(...) returned null");
			}
		} else {
			logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getSingularAttributes failed");
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
