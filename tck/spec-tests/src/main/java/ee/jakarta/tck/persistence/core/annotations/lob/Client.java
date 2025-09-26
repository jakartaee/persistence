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

package ee.jakarta.tck.persistence.core.annotations.lob;

import java.lang.System.Logger;
import java.util.Arrays;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private DataTypes dataTypes;

	private Byte[] smallByteArray = null;

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DataTypes" };
		return createDeploymentJar("jpa_core_annotations_lob.jar", pkgNameWithoutSuffix, classes);
	}

	public Client() {
	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: lobTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:528
	 * 
	 * @test_Strategy: The persistent property of an entity may be of the following
	 * type: Byte[]
	 *
	 */
	@Test
	public void lobTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;

		Byte[] largeByteArray = null;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "FIND DataTypes and verify initial value");
			dataTypes = getEntityManager().find(DataTypes.class, 1);
			if ((null != dataTypes) && (Arrays.equals(smallByteArray, dataTypes.getByteArrayData()))) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
				logger.log(Logger.Level.TRACE, "DataType Entity is not null, setting byteData ");
				largeByteArray = createLargeByteArray();
				dataTypes.setByteArrayData(largeByteArray);
				getEntityManager().merge(dataTypes);
				getEntityManager().flush();

				logger.log(Logger.Level.INFO, "FIND DataTypes again and verify updated value");
				dataTypes = getEntityManager().find(DataTypes.class, 1);

				logger.log(Logger.Level.TRACE, "Check results");
				if ((null != dataTypes) && (Arrays.equals(largeByteArray, dataTypes.getByteArrayData()))) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected result in array comparison.");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Unexpected result in array comparison.");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass1 || !pass2) {
			throw new Exception("lobTest failed");
		}
	}

	// Methods used for Tests
	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");

		try {
			getEntityTransaction().begin();
			smallByteArray = createSmallByteArray();
			dataTypes = new DataTypes(1, smallByteArray);
			getEntityManager().persist(dataTypes);

			getEntityManager().flush();

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

	}

	private Byte[] createSmallByteArray() {

		// Create a String of size 1MB
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < 1024; i++) {
			strbuf.append(i);
		}

		String value = strbuf.toString();
		System.out.println("String Buffer :" + value);

		// get byte array from the string
		final byte myByte[] = value.getBytes();

		// store primitive byte array to array of Byte objects
		Byte convertedByte[] = new Byte[myByte.length];
		for (int i = 0; i < myByte.length; i++) {
			convertedByte[i] = Byte.valueOf(myByte[i]);
		}

		return convertedByte;

	}

	private Byte[] createLargeByteArray() {

		// Create a String of size 4MB
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < 4096; i++) {
			strbuf.append(i);
		}

		String value = strbuf.toString();
		System.out.println("String Buffer :" + value);

		// get byte array from the string
		final byte myByte[] = value.getBytes();

		// store primitive byte array to array of Byte objects
		Byte convertedByte[] = new Byte[myByte.length];
		for (int i = 0; i < myByte.length; i++) {
			convertedByte[i] = Byte.valueOf(myByte[i]);
		}

		return convertedByte;

	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
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
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES").executeUpdate();
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
