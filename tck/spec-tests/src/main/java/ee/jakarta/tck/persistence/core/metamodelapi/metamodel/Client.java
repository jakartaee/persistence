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

package ee.jakarta.tck.persistence.core.metamodelapi.metamodel;

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
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "B", pkgName + "Employee", pkgName + "FullTimeEmployee",
				pkgName + "Order", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_metamodel.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: getMetamodel
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:330
	 *
	 * @test_Strategy:
	 * 
	 */
	@Test
	public void getMetamodel() throws Exception {
		boolean pass = false;

		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			pass = true;
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
		}

		if (!pass) {
			throw new Exception("getMetamodeltest failed");
		}
	}

	/*
	 * @testName: getEntities
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1438
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getEntities() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			Set<EntityType<?>> orderSet = metaModel.getEntities();
			if (orderSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of EntityType");
				for (EntityType eType : orderSet) {
					logger.log(Logger.Level.TRACE, "entityType Name = " + eType.getName());
					pass = true;
				}

			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getEntities Test  failed");
		}
	}

	/*
	 * @testName: getManagedTypes
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1439
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getManagedTypes() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			Set<ManagedType<?>> orderSet = metaModel.getManagedTypes();
			if (orderSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of ManagedType");
				for (ManagedType mType : orderSet) {
					Set<Attribute<Order, ?>> attribSet = mType.getDeclaredAttributes();
					if (attribSet != null) {
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
						}
						pass = true;
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getManagedTypes Test  failed");
		}
	}

	/*
	 * @testName: getEmbeddables
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1437
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getEmbeddables() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			Set<EmbeddableType<?>> addrSet = metaModel.getEmbeddables();
			if (addrSet != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Set of EmbeddableType");
				for (EmbeddableType eType : addrSet) {
					Set<Attribute<Address, ?>> attribSet = eType.getDeclaredAttributes();
					if (attribSet != null) {
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
						}
						pass = true;
					}
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getEmbeddables Test  failed");
		}
	}

	/*
	 * @testName: managedType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1440
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void managedType() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {

			logger.log(Logger.Level.INFO, "Test entity");
			String expected = "ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Order";
			ManagedType mType = metaModel.managedType(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Order.class);
			if (mType != null) {
				ManagedType<Order> mTypeOrder = mType;
				String cActual = mType.getJavaType().getName();
				if (cActual.equals(expected)) {
					Set<Attribute<Order, ?>> attribSet = mTypeOrder.getDeclaredAttributes();
					if (attribSet != null) {
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
						}
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredAttributes() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + expected + ", actual:" + cActual);
				}
			}
			logger.log(Logger.Level.INFO, "Test embeddable");
			expected = "ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Address";
			mType = metaModel.managedType(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Address.class);
			if (mType != null) {
				ManagedType<Address> mTypeAddress = mType;
				String cActual = mType.getJavaType().getName();
				if (cActual.equals(expected)) {
					Set<Attribute<Address, ?>> attribSet = mTypeAddress.getDeclaredAttributes();
					if (attribSet != null) {
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
						}
						pass2 = true;
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredAttributes() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + expected + ", actual:" + cActual);
				}
			}

			logger.log(Logger.Level.INFO, "Test superclass");
			expected = "ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Employee";
			mType = metaModel.managedType(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Employee.class);
			if (mType != null) {
				ManagedType<Employee> mTypeEmployee = mType;
				String cActual = mType.getJavaType().getName();
				if (cActual.equals(expected)) {
					Set<Attribute<Employee, ?>> attribSet = mTypeEmployee.getDeclaredAttributes();
					if (attribSet != null) {
						for (Attribute attrib : attribSet) {
							logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
						}
						pass3 = true;
					} else {
						logger.log(Logger.Level.ERROR, "getDeclaredAttributes() returned null");
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + expected + ", actual:" + cActual);
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("managedType Test failed");
		}
	}

	/*
	 * @testName: managedTypeIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1441
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void managedTypeIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			try {
				metaModel.managedType(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Client.class);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("managedTypeIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: entity
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1435
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void entity() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
			EntityType<Order> eTypeOrder = metaModel
					.entity(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Order.class);
			if (eTypeOrder != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null EntityType");
				Set<Attribute<Order, ?>> attribSet = eTypeOrder.getDeclaredAttributes();
				if (attribSet != null) {
					for (Attribute attrib : attribSet) {
						logger.log(Logger.Level.TRACE, "attribute Name = " + attrib.getName());
					}
					pass = true;
				}
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("entity Test  failed");
		}
	}

	/*
	 * @testName: entityIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1436
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void entityIllegalArgumentException() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		Metamodel metaModel = getEntityManager().getMetamodel();
		if (metaModel != null) {
			try {
				metaModel.entity(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Client.class);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("entityIllegalArgumentException  failed");
		}
	}

	/*
	 * @testName: embeddable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1433
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void embeddable() throws Exception {
		boolean pass = true;
		Collection<String> expected = new ArrayList<String>();
		expected.add("zipcode");
		expected.add("street");
		expected.add("state");
		expected.add("city");

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Metamodel from EntityManager");
				EmbeddableType<Address> eTypeOrder = metaModel
						.embeddable(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Address.class);
				if (eTypeOrder != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null EmbeddableType");
					Set<Attribute<Address, ?>> attribSet = eTypeOrder.getDeclaredAttributes();
					if (attribSet != null) {
						if (attribSet.size() != expected.size()) {
							pass = false;
							logger.log(Logger.Level.ERROR, "Received wrong number of results");
						}
						for (Attribute attrib : attribSet) {
							String name = attrib.getName();
							if (expected.contains(name)) {
								logger.log(Logger.Level.TRACE, "received attribute Name = " + name);
							} else {
								logger.log(Logger.Level.ERROR, "Received unexpected result" + name);
								pass = false;
							}
						}
					} else {
						pass = false;
						logger.log(Logger.Level.ERROR, "getDeclaredAttributes() returned null");
					}
				} else {
					pass = false;
					logger.log(Logger.Level.ERROR, "embeddable() returned null");
				}
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "getMetamodel() returned null");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unxpected exception", e);
		}
	}

	/*
	 * @testName: embeddableIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1434
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void embeddableIllegalArgumentException() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			Metamodel metaModel = getEntityManager().getMetamodel();
			if (metaModel != null) {
				try {
					metaModel.embeddable(ee.jakarta.tck.persistence.core.metamodelapi.metamodel.Client.class);
					logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
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
			throw new Exception("embeddableIllegalArgumentException failed");
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
