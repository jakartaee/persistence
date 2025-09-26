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

package ee.jakarta.tck.persistence.core.metamodelapi.mappedsuperclasstype;

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
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.IdentifiableType;
import jakarta.persistence.metamodel.MappedSuperclassType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Employee", pkgName + "Employee2", pkgName + "EmployeeId",
				pkgName + "FullTimeEmployee", pkgName + "FullTimeEmployee2" };
		return createDeploymentJar("jpa_core_metamodelapi_mappedsuperclasstype.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: mappedSuperclassType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1428;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void mappedSuperclassType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			pass = false;
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> aSet = metaModel.getEntities();
			if (aSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of EntityType");
				for (EntityType mType : aSet) {
					logger.log(Logger.Level.TRACE, "EntityType:" + mType.getJavaType().getName());

					IdentifiableType<? super FullTimeEmployee> idType = mType.getSupertype();
					if (idType != null) {
						logger.log(Logger.Level.TRACE, "IdentifiableType:" + idType.getJavaType().getName());
						if (idType instanceof MappedSuperclassType) {
							logger.log(Logger.Level.TRACE,
									"type is instance of MappedSuperClassType:" + idType.getJavaType().getName());
							pass = true;
						}
					}

				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("mappedSuperclassType failed");
		}
	}

	/*
	 * @testName: getDeclaredId
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1419;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType.getDeclaredId(String.class);
					if (idAttrib != null) {
						String name = idAttrib.getType().getJavaType().getName();
						if (name.equals("java.lang.String")) {
							logger.log(Logger.Level.TRACE, "Received:" + name);
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
						}
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredId(...) returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1420;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getDeclaredId(Date.class);
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1421;
	 *
	 * @test_Strategy:
	 *
	 */

	public void getDeclaredVersion() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType.getDeclaredVersion(Integer.class);
					if (idAttrib != null) {
						String name = idAttrib.getType().getJavaType().getName();
						if (name.equals("java.lang.Integer")) {
							logger.log(Logger.Level.TRACE, "Received:" + name);
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected java.lang.Integer, actual:" + name);
						}
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredId(...) returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1422;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getDeclaredVersion(Date.class);
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1423;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType.getId(String.class);
					if (idAttrib != null) {
						String name = idAttrib.getType().getJavaType().getName();
						if (name.equals("java.lang.String")) {
							logger.log(Logger.Level.TRACE, "Received:" + name);
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
						}
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredId(...) returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1424;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getId(Date.class);
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1425;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getIdClassAttributes() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		expected.add("id");
		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<FullTimeEmployee> eType = metaModel.entity(FullTimeEmployee.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					Set<SingularAttribute> idAttribSet = idType.getIdClassAttributes();
					if (idAttribSet != null) {
						if (idAttribSet.size() > 0) {
							for (Iterator i = idAttribSet.iterator(); i.hasNext();) {
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
								logger.log(Logger.Level.ERROR, "Received Unexpected attributes");
								logger.log(Logger.Level.ERROR, "Expected:");
								for (String attribName : expected) {
									logger.log(Logger.Level.ERROR, "attrib:" + attribName);
								}
								logger.log(Logger.Level.ERROR, "Actual:");
								for (String attribName : actual) {
									logger.log(Logger.Level.ERROR, "attrib:" + attribName);
								}
							}
						} else {
							logger.log(Logger.Level.ERROR, "getIdClassAttributes() returned 0 results");
						}
					} else {
						logger.log(Logger.Level.ERROR, "getIdClassAttributes() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1426;
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
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					try {
						idType.getIdClassAttributes();
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1427;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					String name = idType.getIdType().getJavaType().getName();

					if (name.equals("java.lang.String")) {
						logger.log(Logger.Level.TRACE, "Received expected: " + name);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected java.lang.String, actual:" + name);
					}

				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @testName: getVersion
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1429;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					SingularAttribute idAttrib = eType.getVersion(Integer.class);
					String name = idAttrib.getType().getJavaType().getName();
					if (name.equals("java.lang.Integer")) {
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected java.lang.Integer, actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1430;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
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
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @testName: hasSingleIdAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1431;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					boolean hasSingleIdAttribute = idType.hasSingleIdAttribute();
					if (hasSingleIdAttribute) {
						pass = true;
						logger.log(Logger.Level.TRACE, "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
					} else {
						logger.log(Logger.Level.ERROR, "Expected: false, actual:" + hasSingleIdAttribute);

					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
	 * @assertion_ids: PERSISTENCE:JAVADOC:1432;
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
			EntityType<FullTimeEmployee2> eType = metaModel.entity(FullTimeEmployee2.class);
			if (eType != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				IdentifiableType idType = eType.getSupertype();
				if (idType != null) {
					boolean hasVersionAttribute = idType.hasVersionAttribute();
					if (hasVersionAttribute) {
						pass = true;
						logger.log(Logger.Level.TRACE, "hasVersionAttribute() returned" + hasVersionAttribute);
					} else {
						logger.log(Logger.Level.ERROR, "Expected: false, actual:" + hasVersionAttribute);

					}
				} else {
					logger.log(Logger.Level.ERROR, "getSupertype() returned null");
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
