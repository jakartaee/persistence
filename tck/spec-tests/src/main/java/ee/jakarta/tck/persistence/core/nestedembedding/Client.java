/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.nestedembedding;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "B", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_override_nestedembedding.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: NE1XMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:623;
	 * PERSISTENCE:JAVADOC:129; PERSISTENCE:JAVADOC:132; PERSISTENCE:SPEC:566
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * Invoke persist on a OneToMany relationship from X annotated with cascade=ALL
	 * and ensure the persist operation is cascaded.
	 *
	 */
	@Test
	public void NE1XMTest1() throws Exception {
		boolean pass = false;
		A aRef;
		Collection newCol;

		getEntityTransaction().begin();
		try {
			logger.log(Logger.Level.TRACE, "New instances");

			ZipCode z1 = new ZipCode("01801", "1234");
			ZipCode z2 = new ZipCode("01803", "1234");

			Address addr1 = new Address("1 Network Drive", "Burlington", "MA");
			Address addr2 = new Address("Some Address", "Boston", "MA");

			addr1.setZipCode(z1);
			addr2.setZipCode(z2);

			B b1 = new B("1", "b1", 1);
			b1.setAddress(addr1);
			B b2 = new B("2", "b2", 1);
			b2.setAddress(addr2);
			B b3 = new B("3", "b3", 1);
			b3.setAddress(addr1);
			B b4 = new B("4", "b4", 1);
			b4.setAddress(addr2);

			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			aRef = new A("1", "bean1", 1, v1);
			getEntityManager().persist(aRef);
			getEntityManager().flush();
			clearCache();
			newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3) && newCol.contains(b4)) {

				B newB = getBFromCollection(newCol, b1);
				if (newB != null) {

					if (newB.getAddress().getStreet().equals("1 Network Drive")
							&& newB.getAddress().getCity().equals("Burlington")
							&& newB.getAddress().getState().equals("MA")
							&& newB.getAddress().getZipCode().getZip().equals("01801")
							&& newB.getAddress().getZipCode().getPlusFour().equals("1234")) {
						pass = true;
						logger.log(Logger.Level.TRACE, "verified nested embedded class contents ");
					} else {
						logger.log(Logger.Level.ERROR, "Expected address:" + addr1.toString());
						logger.log(Logger.Level.ERROR, "actual address:" + newB.getAddress().toString());
					}
				} else {
					logger.log(Logger.Level.ERROR, "b not found in Collection");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Collection did not contain all entries:");
				if (newCol.contains(b1)) {
					logger.log(Logger.Level.TRACE, "found b1");
				} else {
					logger.log(Logger.Level.ERROR, "b1 NOT FOUND");
				}
				if (newCol.contains(b2)) {
					logger.log(Logger.Level.TRACE, "found b2");
				} else {
					logger.log(Logger.Level.ERROR, "b2 NOT FOUND");
				}
				if (newCol.contains(b3)) {
					logger.log(Logger.Level.TRACE, "found b3");
				} else {
					logger.log(Logger.Level.ERROR, "b3 NOT FOUND");
				}
				if (newCol.contains(b4)) {
					logger.log(Logger.Level.TRACE, "found b4");
				} else {
					logger.log(Logger.Level.ERROR, "b4 NOT FOUND");
				}

			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}

		}

		if (!pass) {
			throw new Exception("cascadeAll1XMTest1 failed");
		}
	}

	/*
	 * @testName: NE1XMTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:326
	 * 
	 * @test_Strategy: Use Nested embeddable class in Query
	 *
	 */
	@Test
	public void NE1XMTest2() throws Exception {
		boolean pass = false;
		A aRef;
		Collection<B> newCol;

		getEntityTransaction().begin();

		try {
			logger.log(Logger.Level.TRACE, "New instances");

			ZipCode z1 = new ZipCode("01801", "1234");
			ZipCode z2 = new ZipCode("01803", "1234");

			Address addr1 = new Address("1 Network Drive", "Burlington", "MA");
			Address addr2 = new Address("Some Address", "Boston", "MA");

			addr1.setZipCode(z1);
			addr2.setZipCode(z2);

			B b1 = new B("1", "b1", 1);
			b1.setAddress(addr1);
			B b2 = new B("2", "b2", 1);
			b2.setAddress(addr2);

			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);

			aRef = new A("1", "bean1", 1, v1);
			getEntityManager().persist(aRef);
			getEntityManager().flush();
			clearCache();
			A newA = findA("1");

			if (newA != null) {

				newCol = aRef.getBCol();

				// Get the B collection in ResultList using Query
				B newB = (B) getEntityManager().createQuery("Select b from B b where b.address.zipcode.zip='01801'")
						.getSingleResult();

				// Verify Embedded contents
				if (newB != null) {
					logger.log(Logger.Level.TRACE, "newB:" + newB.toString());
					if (newCol.contains(newB)) {
						logger.log(Logger.Level.TRACE, "b contains the searched embeddable Address");
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected:" + newCol.toString() + ", actual:" + newB.toString());
					}

				} else {
					logger.log(Logger.Level.ERROR, "newB is null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "newA is null");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}

		}
		if (!pass) {
			throw new Exception("NE1XMTest2 failed");
		}
	}

	/*
	 * @testName: NE1XMTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:326
	 * 
	 * @test_Strategy: Use embedded class in Query
	 *
	 */
	@Test
	public void NE1XMTest3() throws Exception {
		boolean pass = false;
		A aRef;
		Collection newCol;

		getEntityTransaction().begin();

		try {
			logger.log(Logger.Level.TRACE, "New instances");

			ZipCode z1 = new ZipCode("01801", "1234");
			ZipCode z2 = new ZipCode("01803", "1234");

			Address addr1 = new Address("1 Network Drive", "Burlington", "MA");
			Address addr2 = new Address("Some Address", "Boston", "MA");

			addr1.setZipCode(z1);
			addr2.setZipCode(z2);

			B b1 = new B("1", "b1", 1);
			b1.setAddress(addr1);
			B b2 = new B("2", "b2", 1);
			b2.setAddress(addr2);

			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);

			aRef = new A("1", "bean1", 1, v1);
			getEntityManager().persist(aRef);
			getEntityManager().flush();
			clearCache();
			A newA = findA("1");

			if (newA != null) {

				newCol = aRef.getBCol();

				// Get the B using embeddable class in Query
				B newB = (B) getEntityManager()
						.createQuery("Select b from B b where b.address.street='1 Network Drive'").getSingleResult();

				// Verify Embedded contents
				if (newB != null) {
					if (newCol.contains(newB)) {
						logger.log(Logger.Level.TRACE, "b contains the searched embeddable Address");
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected:" + newCol.toString() + ", actual:" + newB.toString());
					}

				} else {
					logger.log(Logger.Level.ERROR, "newB is null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "newA is null");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}

		}
		if (!pass) {
			throw new Exception("NE1XMTest3 failed");
		}
	}

	/*
	 *
	 * Business Methods to set up data for Test Cases
	 *
	 */
	private void createA(final A a) {
		logger.log(Logger.Level.TRACE, "Entered createA method");
		getEntityTransaction().begin();
		getEntityManager().persist(a);
		getEntityTransaction().commit();
	}

	private A findA(final String id) {
		// logger.log(Logger.Level.TRACE,"Entered findA method");
		return getEntityManager().find(A.class, id);
	}

	private void createB(final B b) {
		logger.log(Logger.Level.TRACE, "Entered createB method");
		getEntityTransaction().begin();
		getEntityManager().persist(b);
		getEntityTransaction().commit();
	}

	private B findB(final String id) {
		// logger.log(Logger.Level.TRACE,"Entered findB method");
		return getEntityManager().find(B.class, id);
	}

	private List findByName(final String name) {
		logger.log(Logger.Level.TRACE, "Entered findByName method");
		return getEntityManager().createQuery("select a from A a where a.name = :name").setParameter("name", name)
				.getResultList();
	}

	private boolean getInstanceStatus(final Object o) {
		logger.log(Logger.Level.TRACE, "Entered getInstanceStatus method");
		return getEntityManager().contains(o);
	}

	private void dumpCollectionDataA(final Collection c) {
		logger.log(Logger.Level.TRACE, "collection Data");
		logger.log(Logger.Level.TRACE, "---------------");
		logger.log(Logger.Level.TRACE, "- size=" + c.size());
		Iterator i = c.iterator();
		int elem = 1;
		while (i.hasNext()) {
			A v = (A) i.next();
			logger.log(Logger.Level.TRACE, "- Element #" + elem++);
			logger.log(Logger.Level.TRACE,
					"  id=" + v.getAId() + ", name=" + v.getAName() + ", value=" + v.getAValue());
		}
	}

	private void dumpCollectionDataB(final Collection c) {
		logger.log(Logger.Level.TRACE, "collection Data");
		logger.log(Logger.Level.TRACE, "---------------");
		logger.log(Logger.Level.TRACE, "- size=" + c.size());
		Iterator i = c.iterator();
		int elem = 1;
		while (i.hasNext()) {
			B v = (B) i.next();
			logger.log(Logger.Level.TRACE, "- Element #" + elem++);
			logger.log(Logger.Level.TRACE,
					"  id=" + v.getBId() + ", name=" + v.getBName() + ", value=" + v.getBValue());
		}
	}

	public B getBFromCollection(final Collection c, final B b) {
		logger.log(Logger.Level.TRACE, "getBFromCollection");
		B resultB = null;
		if (c.size() != 0) {
			Iterator iterator = c.iterator();
			while (iterator.hasNext()) {
				B newB = (B) iterator.next();
				if (newB.getBId().equals(b.getBId()) && newB.getBName().equals(b.getBName())
						&& newB.getBValue() == b.getBValue()) {
					logger.log(Logger.Level.TRACE, "Found B in Collection");
					resultB = newB;
					return resultB;
				} else {
					logger.log(Logger.Level.TRACE, "b not found in Collection");
				}
			}
		}
		return resultB;
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
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM ANE_1XM_BI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BNE_1XM_BI_BTOB").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
