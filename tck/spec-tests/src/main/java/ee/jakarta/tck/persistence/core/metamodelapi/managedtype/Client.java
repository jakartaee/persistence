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

package ee.jakarta.tck.persistence.core.metamodelapi.managedtype;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.PluralAttribute;
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
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "B", pkgName + "BiDirMX1Person",
				pkgName + "BiDirMX1Project", pkgName + "Department", pkgName + "Employee", pkgName + "Order",
				pkgName + "Uni1XMPerson", pkgName + "Uni1XMProject", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_managedtype.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: managedtype
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1440
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void managedtype() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
				if (mTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("managedtype failed");
		}
	}

	/*
	 * @testName: getAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1365
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttributes() throws Exception {
		boolean pass = true;

		Collection<String> expected = new ArrayList<String>();
		expected.add("id");
		expected.add("total");
		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
				if (mTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Set<Attribute<? super Order, ?>> attribSet = mTypeOrder.getAttributes();
					if (attribSet != null) {
						if (attribSet.size() != expected.size()) {
							pass = false;
							logger.log(Logger.Level.ERROR, "Received wrong number of results");
						}
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
							if (expected.contains(attrib.getName())) {
								logger.log(Logger.Level.TRACE, "Received expected:" + attrib.getName());
							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected result" + attrib.getName());
								pass = false;
							}
						}
					} else {
						pass = false;
						logger.log(Logger.Level.ERROR, "getAttributes() returned null");
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getAttributes failed");
		}
	}

	/*
	 * @testName: getDeclaredAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1372
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttributes() throws Exception {
		boolean pass = true;

		Collection<String> expected = new ArrayList<String>();
		expected.add("id");
		expected.add("total");
		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
				if (mTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Set<Attribute<Order, ?>> attribSet = mTypeOrder.getDeclaredAttributes();
					if (attribSet != null) {
						if (attribSet.size() != expected.size()) {
							pass = false;
							logger.log(Logger.Level.ERROR, "Received wrong number of results");
						}
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
							if (expected.contains(attrib.getName())) {
								logger.log(Logger.Level.TRACE, "Received expected:" + attrib.getName());
							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected result" + attrib.getName());
								pass = false;
							}
						}
					} else {
						pass = false;
						logger.log(Logger.Level.ERROR, "getAttributes() returned null");
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getDeclaredAttributes failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1408; PERSISTENCE:JAVADOC:1413;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringClassTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SingularAttribute<? super B, Address> singAttrib = mTypeB.getSingularAttribute("address",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
					Class addrClass = singAttrib.getType().getJavaType();
					if (addrClass.getName().equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
						logger.log(Logger.Level.TRACE, "address class getName =" + addrClass.getName());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address, actual:"
										+ addrClass.getName());
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getSingularAttributeStringClassTest failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1409
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");

					try {
						mTypeB.getSingularAttribute("doesnotexist",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mTypeB.getSingularAttribute("addreess",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getSingularAttributeStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1410
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringTest() throws Exception {
		boolean pass = false;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SingularAttribute<? super B, ?> singAttrib = mTypeB.getSingularAttribute("address");
					logger.log(Logger.Level.TRACE, "singAttrib Type = " + singAttrib.getType());
					logger.log(Logger.Level.TRACE, "singAttrib Java Type = " + singAttrib.getJavaType());
					if (singAttrib.getJavaType().getName()
							.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected: address, actual:" + singAttrib.getJavaType().getName());
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getSingularAttributeStringTest failed");
		}
	}

	/*
	 * @testName: getSingularAttributeStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1411
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributeStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");

					try {
						mTypeB.getSingularAttribute("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getSingularAttributeStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1390
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringClassTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SingularAttribute<B, Address> singAttrib = mTypeB.getDeclaredSingularAttribute("address",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
					Class addrClass = singAttrib.getType().getJavaType();
					if (addrClass.getName().equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
						logger.log(Logger.Level.TRACE, "address class getName =" + addrClass.getName());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address, actual:"
										+ addrClass.getName());
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringClassTest failed");
		}
	}

	/*
	 * @testName:
	 * getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1391
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mTypeB.getDeclaredSingularAttribute("doesnotexist",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mTypeB.getDeclaredSingularAttribute("address",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1392
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SingularAttribute<B, ?> singAttrib = mTypeB.getDeclaredSingularAttribute("address");
					logger.log(Logger.Level.TRACE, "singAttrib Type = " + singAttrib.getType());
					logger.log(Logger.Level.TRACE, "singAttrib Java Type = " + singAttrib.getJavaType());
					if (singAttrib.getJavaType().getName()
							.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address, actual:"
										+ singAttrib.getJavaType().getName());
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributeStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1393
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributeStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					try {
						mTypeB.getDeclaredSingularAttribute("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredSingularAttributeStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSingularAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1394
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSingularAttributes() throws Exception {
		boolean pass = true;
		Collection<String> expected = new ArrayList<String>();
		expected.add("id");
		expected.add("address");
		expected.add("name");
		expected.add("value");

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Set<SingularAttribute<B, ?>> attribSet = mTypeB.getDeclaredSingularAttributes();
					if (attribSet != null) {
						if (attribSet.size() != expected.size()) {
							pass = false;
							logger.log(Logger.Level.ERROR, "Received wrong number of results");
						}
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
							if (expected.contains(attrib.getName())) {
								logger.log(Logger.Level.TRACE, "Received expected:" + attrib.getName());
							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected result" + attrib.getName());
								pass = false;
							}
						}
					} else {
						pass = false;
						logger.log(Logger.Level.ERROR, "getDeclaredSingularAttributes() returned null");
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getDeclaredSingularAttributes failed");
		}
	}

	/*
	 * @testName: getSingularAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1412
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSingularAttributes() throws Exception {
		boolean pass = true;
		Collection<String> expected = new ArrayList<String>();
		expected.add("id");
		expected.add("address");
		expected.add("name");
		expected.add("value");
		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<B> mTypeB = metaModel.managedType(B.class);
				if (mTypeB != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Set<SingularAttribute<? super B, ?>> attribSet = mTypeB.getSingularAttributes();
					if (attribSet != null) {
						if (attribSet.size() != expected.size()) {
							pass = false;
							logger.log(Logger.Level.ERROR, "Received wrong number of results");
						}
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
							if (expected.contains(attrib.getName())) {
								logger.log(Logger.Level.TRACE, "Received expected:" + attrib.getName());
							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected result" + attrib.getName());
								pass = false;
							}
						}
					} else {
						pass = false;
						logger.log(Logger.Level.ERROR, "getSingularAttributes() returned null");
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getSingularAttributes failed");
		}
	}

	/*
	 * @testName: getCollectionStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1368
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					CollectionAttribute<? super Uni1XMPerson, ?> colAttrib = mType.getCollection("projects");
					Type t = colAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject")) {
							logger.log(Logger.Level.TRACE,
									"Received expected ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject");
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getCollectionStringTest failed");
		}
	}

	/*
	 * @testName: getCollectionStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1369
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getCollection("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getCollectionStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getCollectionStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1366
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringClassTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					CollectionAttribute<? super Uni1XMPerson, Uni1XMProject> colAttrib = mType.getCollection("projects",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class);
					Type t = colAttrib.getElementType();
					if (t != null) {
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject")) {
							logger.log(Logger.Level.TRACE,
									"Received expected ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject");
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getCollectionStringClassTest failed");
		}
	}

	/*
	 * @testName: getCollectionStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1367
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getCollectionStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getCollection("doesnotexist",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getCollection("projects",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getCollectionStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getSetStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1404
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringClassTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
					Type t = setAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getSetStringClassTest failed");
		}
	}

	/*
	 * @testName: getSetStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1405
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getSet("doesnotexist", ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getCollection("address",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getSetStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getSetStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1406
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringTest() throws Exception {
		boolean pass = false;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SetAttribute<? super A, ?> setAttrib = mType.getSet("address");
					Type t = setAttrib.getElementType();
					logger.log(Logger.Level.TRACE, "element Type  = " + setAttrib.getElementType());
					if (t != null) {
						logger.log(Logger.Level.TRACE, "java Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getSetStringTest failed");
		}
	}

	/*
	 * @testName: getSetStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1407
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getSetStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getSet("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getSetStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getListStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1395
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringClassTest() throws Exception {
		boolean pass = false;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					ListAttribute<? super BiDirMX1Project, BiDirMX1Person> listAttrib = mType.getList("biDirMX1Persons",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class);
					Type t = listAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getListStringClassTest failed");
		}
	}

	/*
	 * @testName: getListStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1396
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");

					try {
						mType.getList("doesnotexist",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getCollection("biDirMX1Persons",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getListStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getListStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1397
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringTest() throws Exception {
		boolean pass = false;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					ListAttribute<? super BiDirMX1Project, ?> listAttrib = mType.getList("biDirMX1Persons");
					Type t = listAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getListStringTest failed");
		}
	}

	/*
	 * @testName: getListStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1398
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getListStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getList("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getListStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getMapStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1399
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringClassTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					MapAttribute<? super Department, String, Employee> mapAttrib = mType.getMap("lastNameEmployees",
							java.lang.String.class, Employee.class);
					Type t = mapAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getMapStringClassTest failed");
		}
	}

	/*
	 * @testName: getMapStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1400
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getMap("doesnotexist", java.lang.String.class, Employee.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getMap("lastNameEmployees", String.class,
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getMapStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getMapStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1401
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					MapAttribute<? super Department, ?, ?> mapAttrib = mType.getMap("lastNameEmployees");
					Type t = mapAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getMapStringTest failed");
		}
	}

	/*
	 * @testName: getMapStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1402
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getMapStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getMap("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getMapStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1373
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringClassTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					CollectionAttribute<Uni1XMPerson, Uni1XMProject> colAttrib = mType.getDeclaredCollection("projects",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class);
					Type t = colAttrib.getElementType();
					if (t != null) {
						String type = t.getJavaType().getName();
						logger.log(Logger.Level.TRACE, "element Type  = " + type);
						if (type.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject, actual:"
											+ type);
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredCollectionStringClassTest failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1374
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredCollection("doesnotexist",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getCollection("projects",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}

			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getDeclaredCollectionStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1375
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					CollectionAttribute<Uni1XMPerson, ?> colAttrib = mType.getDeclaredCollection("projects");
					Type t = colAttrib.getElementType();
					if (t != null) {
						String type = t.getJavaType().getName();
						logger.log(Logger.Level.TRACE, "element Type  = " + type);
						if (type.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject, actual:"
											+ type);
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredCollectionStringTest failed");
		}
	}

	/*
	 * @testName: getDeclaredCollectionStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1376
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredCollectionStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredCollection("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}

			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredCollectionStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1386
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringClassTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SetAttribute<A, Address> setAttrib = mType.getDeclaredSet("address",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
					Type t = setAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredSetStringClassTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1387
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredSet("doesnotexist",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getCollection("address",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getDeclaredSetStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1388
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					SetAttribute<A, ?> setAttrib = mType.getDeclaredSet("address");
					logger.log(Logger.Level.TRACE, "element Type  = " + setAttrib.getElementType());
					Type t = setAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredSetStringTest failed");
		}
	}

	/*
	 * @testName: getDeclaredSetStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1389
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredSetStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<A> mType = metaModel.managedType(A.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredSet("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredSetStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1377
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringClassTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					ListAttribute<BiDirMX1Project, BiDirMX1Person> listAttrib = mType.getDeclaredList("biDirMX1Persons",
							ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class);
					Type t = listAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredListStringClassTest failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1378
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");

					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredList("doesnotexist",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getCollection("biDirMX1Persons",
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getDeclaredListStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1379
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					ListAttribute<BiDirMX1Project, ?> listAttrib = mType.getDeclaredList("biDirMX1Persons");
					Type t = listAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredListStringTest failed");
		}
	}

	/*
	 * @testName: getDeclaredListStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1380
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredListStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<BiDirMX1Project> mType = metaModel.managedType(BiDirMX1Project.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredList("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredListStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringClassClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1381
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringClassClassTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					MapAttribute<Department, String, Employee> mapAttrib = mType.getDeclaredMap("lastNameEmployees",
							java.lang.String.class, Employee.class);
					Type t = mapAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredMapStringClassClassTest failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringClassClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1382
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringClassClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");

					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredMap("doesnotexist", java.lang.String.class, Employee.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}

					logger.log(Logger.Level.INFO, "Testing invalid type");

					try {
						mType.getDeclaredMap("lastNameEmployees", String.class,
								ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class);
						pass = false;
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					} catch (Exception e) {
						pass = false;
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getDeclaredMapStringClassClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1383
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					MapAttribute<Department, ?, ?> mapAttrib = mType.getDeclaredMap("lastNameEmployees");
					Type t = mapAttrib.getElementType();
					if (t != null) {
						logger.log(Logger.Level.TRACE, "element Type  = " + t.getJavaType());
						if (t.getJavaType().getName()
								.equals("ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected: ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee, actual:"
											+ t.getJavaType().getName());
						}
					} else {
						logger.log(Logger.Level.ERROR, "getElementType() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredMapStringTest failed");
		}
	}

	/*
	 * @testName: getDeclaredMapStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1384
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredMapStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Department> mType = metaModel.managedType(Department.class);
				if (mType != null) {
					logger.log(Logger.Level.INFO, "Testing invalid name");
					try {
						mType.getDeclaredMap("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredMapStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getPluralAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1403
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPluralAttributes() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Set<PluralAttribute<? super Uni1XMPerson, ?, ?>> pluralAttribSet = mType.getPluralAttributes();
					if (pluralAttribSet != null) {
						if (pluralAttribSet.size() != 1) {
							pass = false;
							logger.log(Logger.Level.ERROR, "Expected size:1, actual:" + pluralAttribSet.size());
						}
						for (Attribute attrib : pluralAttribSet) {
							if (attrib.getName().equals("projects")) {
								logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
							} else {
								pass = false;
								logger.log(Logger.Level.ERROR, "Expected: projects, actual:" + attrib.getName());
							}
						}
					} else {
						pass = false;
						logger.log(Logger.Level.ERROR, "getPluralAttributes() returned null");
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getPluralAttributes failed");
		}
	}

	/*
	 * @testName: getDeclaredPluralAttributes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1385
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredPluralAttributes() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Uni1XMPerson> mType = metaModel.managedType(Uni1XMPerson.class);
				if (mType != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Set<PluralAttribute<Uni1XMPerson, ?, ?>> pluralAttribSet = mType.getDeclaredPluralAttributes();
					if (pluralAttribSet != null) {
						if (pluralAttribSet.size() != 1) {
							pass = false;
							logger.log(Logger.Level.ERROR, "Expected size:1, actual:" + pluralAttribSet.size());
						}
						for (Attribute attrib : pluralAttribSet) {
							if (attrib.getName().equals("projects")) {
								logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
							} else {
								pass = false;
								logger.log(Logger.Level.ERROR, "Expected: projects, actual:" + attrib.getName());
							}
						}
					} else {
						pass = false;
						logger.log(Logger.Level.ERROR, "getPluralAttributes() returned null");
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getDeclaredPluralAttributes failed");
		}
	}

	/*
	 * @testName: getAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1363
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttribute() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
				if (mTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Attribute<? super Order, ?> attrib = mTypeOrder.getAttribute("total");
					if (attrib != null) {

						logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
						logger.log(Logger.Level.TRACE, "attribute Java Type =" + attrib.getJavaType());
						String name = attrib.getName();
						String type = attrib.getJavaType().getSimpleName();
						if (name.equals("total") && type.equals("int")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Did not get either:");
							logger.log(Logger.Level.ERROR, "Expected name:total, actual:" + name);
							logger.log(Logger.Level.ERROR, "Expected type:int, actual:" + type);
						}
					} else {
						logger.log(Logger.Level.ERROR, "getAttribute return null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getAttribute failed");
		}
	}

	/*
	 * @testName: getAttributeIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1364
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getAttributeIllegalArgumentException() throws Exception {
		boolean pass = false;
		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
				if (mTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					try {
						mTypeOrder.getAttribute("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getAttributeIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: getDeclaredAttribute
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1370
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttribute() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
				if (mTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
					if (attrib != null) {
						String name = attrib.getName();
						String type = attrib.getJavaType().getSimpleName();
						logger.log(Logger.Level.TRACE, "attribute Name = " + name);
						logger.log(Logger.Level.TRACE, "attribute Java Type =" + type);
						if (name.equals("total") && type.equals("int")) {
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected: total/int, actual:" + name + "/" + type);
						}
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredAttribute() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredAttribute failed");
		}
	}

	/*
	 * @testName: getDeclaredAttributeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1371
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getDeclaredAttributeIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
				if (mTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
					try {
						mTypeOrder.getDeclaredAttribute("doesnotexist");
						logger.log(Logger.Level.TRACE, "Did not receive IllegalArgumentException");
					} catch (IllegalArgumentException iae) {
						logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
						pass = true;
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
					}
				} else {
					logger.log(Logger.Level.ERROR, "managedType() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getDeclaredAttributeIllegalArgumentExceptionTest failed");
		}

	}

	/*
	 * @testName: getPersistenceType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1414;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPersistenceType() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
			if (mTypeOrder != null) {
				Type.PersistenceType type = mTypeOrder.getPersistenceType();
				logger.log(Logger.Level.TRACE, "Obtained Non-null ManagedType");
				if (type.equals(Type.PersistenceType.ENTITY)) {
					pass = true;
				} else {
					logger.log(Logger.Level.TRACE, "Persistence type = " + type.name());
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getPersistenceType Test  failed");
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
