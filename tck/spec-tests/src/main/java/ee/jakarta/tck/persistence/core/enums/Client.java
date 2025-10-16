/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id: Client.java 63518 2011-09-16 11:36:26Z sdimilla $
 */

package ee.jakarta.tck.persistence.core.enums;

import java.lang.System.Logger;
import java.util.Arrays;
import java.util.Collection;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.AccessType;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.Query;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute.PersistentAttributeType;
import jakarta.persistence.metamodel.Bindable.BindableType;
import jakarta.persistence.metamodel.PluralAttribute.CollectionType;
import jakarta.persistence.metamodel.Type.PersistenceType;
import jakarta.persistence.spi.LoadState;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_core_enums.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: accessTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:301
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void accessTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			AccessType at = AccessType.valueOf(AccessType.FIELD.name());
			if (!at.equals(AccessType.FIELD)) {
				logger.log(Logger.Level.ERROR, "expected:" + AccessType.FIELD.name() + ", actual:" + at.name());
				pass = false;
			}
			at = AccessType.valueOf(AccessType.PROPERTY.name());
			if (!at.equals(AccessType.PROPERTY)) {
				logger.log(Logger.Level.ERROR, "expected:" + AccessType.PROPERTY.name() + ", actual:" + at.name());
				pass = false;
			}
			try {
				AccessType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				AccessType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("accessTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: accessTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:302
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void accessTypeValuesTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin accessTypeValuesTest");
		try {

			Collection<AccessType> at = Arrays.asList(AccessType.values());
			if (at.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of AccessType expected:2, actual:" + at.size());
				pass = false;
			}

			if (at.contains(AccessType.FIELD)) {
				logger.log(Logger.Level.TRACE, "received:" + AccessType.FIELD.name());
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + AccessType.FIELD.name());
				pass = false;
			}
			if (at.contains(AccessType.PROPERTY)) {
				logger.log(Logger.Level.TRACE, "received:" + AccessType.PROPERTY.name());
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + AccessType.PROPERTY.name());
				pass = false;
			}

			for (AccessType a : at) {
				try {
					AccessType.valueOf(a.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("accessTypeValuesTest failed");
		}
	}

	/*
	 * @testName: cacheRetrieveModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:309
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void cacheRetrieveModeValueOfTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin cacheRetrieveModeValueOfTest");
		try {

			CacheRetrieveMode crm = CacheRetrieveMode.valueOf(CacheRetrieveMode.USE.name());
			if (!crm.equals(CacheRetrieveMode.USE)) {
				logger.log(Logger.Level.ERROR, "expected:" + CacheRetrieveMode.USE.name() + ", actual:" + crm.name());
				pass = false;
			}
			crm = CacheRetrieveMode.valueOf(CacheRetrieveMode.BYPASS.name());
			if (!crm.equals(CacheRetrieveMode.BYPASS)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + CacheRetrieveMode.BYPASS.name() + ", actual:" + crm.name());
				pass = false;
			}
			try {
				CacheRetrieveMode.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				CacheRetrieveMode.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheRetrieveModeValueOfTest failed");
		}
	}

	/*
	 * @testName: cacheRetrieveModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:310
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void cacheRetrieveModeValuesTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin cacheRetrieveModeValuesTest");
		try {

			Collection<CacheRetrieveMode> crm = Arrays.asList(CacheRetrieveMode.values());
			if (crm.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of CacheRetrieveMode expected:2, actual:" + crm.size());
				pass = false;
			}

			if (crm.contains(CacheRetrieveMode.USE)) {
				logger.log(Logger.Level.TRACE, "received:" + CacheRetrieveMode.USE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CacheRetrieveMode.USE);
				pass = false;
			}
			if (crm.contains(CacheRetrieveMode.BYPASS)) {
				logger.log(Logger.Level.TRACE, "received:" + CacheRetrieveMode.BYPASS);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CacheRetrieveMode.BYPASS);
				pass = false;
			}

			for (CacheRetrieveMode c : crm) {
				try {
					CacheRetrieveMode.valueOf(c.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheRetrieveModeValuesTest failed");
		}
	}

	/*
	 * @testName: cacheStoreModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:311
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void cacheStoreModeValueOfTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin cacheStoreModeValueOfTest");
		try {

			CacheStoreMode csm = CacheStoreMode.valueOf(CacheStoreMode.USE.name());
			if (!csm.equals(CacheStoreMode.USE)) {
				logger.log(Logger.Level.ERROR, "expected:" + CacheStoreMode.USE.name() + ", actual:" + csm.name());
				pass = false;
			}
			csm = CacheStoreMode.valueOf(CacheStoreMode.BYPASS.name());
			if (!csm.equals(CacheStoreMode.BYPASS)) {
				logger.log(Logger.Level.ERROR, "expected:" + CacheStoreMode.BYPASS.name() + ", actual:" + csm.name());
				pass = false;
			}
			csm = CacheStoreMode.valueOf(CacheStoreMode.REFRESH.name());
			if (!csm.equals(CacheStoreMode.REFRESH)) {
				logger.log(Logger.Level.ERROR, "expected:" + CacheStoreMode.REFRESH.name() + ", actual:" + csm.name());
				pass = false;
			}
			try {
				CacheStoreMode.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				CacheStoreMode.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheStoreModeValueOfTest failed");
		}
	}

	/*
	 * @testName: cacheStoreModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:312
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void cacheStoreModeValuesTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin cacheStoreModeValuesTest");
		try {

			Collection<CacheStoreMode> csm = Arrays.asList(CacheStoreMode.values());
			if (csm.size() != 3) {
				logger.log(Logger.Level.ERROR, "Number of CacheStoreMode expected:3, actual:" + csm.size());
				pass = false;
			}

			if (csm.contains(CacheStoreMode.USE)) {
				logger.log(Logger.Level.TRACE, "received:" + CacheStoreMode.USE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CacheStoreMode.USE);
				pass = false;
			}
			if (csm.contains(CacheStoreMode.BYPASS)) {
				logger.log(Logger.Level.TRACE, "received:" + CacheStoreMode.BYPASS);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CacheStoreMode.BYPASS);
				pass = false;
			}
			if (csm.contains(CacheStoreMode.REFRESH)) {
				logger.log(Logger.Level.TRACE, "received:" + CacheStoreMode.REFRESH);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CacheStoreMode.REFRESH);
				pass = false;
			}

			for (CacheStoreMode c : csm) {
				try {
					CacheStoreMode.valueOf(c.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cacheStoreModeValuesTest failed");
		}
	}

	/*
	 * @testName: cascadeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:9
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void cascadeTypeValueOfTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin cascadeTypeValueOfTest");
		try {

			CascadeType ct = CascadeType.valueOf(CascadeType.ALL.name());
			if (!ct.equals(CascadeType.ALL)) {
				logger.log(Logger.Level.ERROR, "expected:" + CascadeType.ALL.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.DETACH.name());
			if (!ct.equals(CascadeType.DETACH)) {
				logger.log(Logger.Level.ERROR, "expected:" + CascadeType.DETACH.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.MERGE.name());
			if (!ct.equals(CascadeType.MERGE)) {
				logger.log(Logger.Level.ERROR, "expected:" + CascadeType.MERGE.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.PERSIST.name());
			if (!ct.equals(CascadeType.PERSIST)) {
				logger.log(Logger.Level.ERROR, "expected:" + CascadeType.PERSIST.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.REFRESH.name());
			if (!ct.equals(CascadeType.REFRESH)) {
				logger.log(Logger.Level.ERROR, "expected:" + CascadeType.REFRESH.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CascadeType.valueOf(CascadeType.REMOVE.name());
			if (!ct.equals(CascadeType.REMOVE)) {
				logger.log(Logger.Level.ERROR, "expected:" + CascadeType.REMOVE.name() + ", actual:" + ct.name());
				pass = false;
			}
			try {
				CascadeType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				CascadeType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cascadeTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: cascadeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:10
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void cascadeTypeValuesTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin cascadeTypeValuesTest");
		try {

			Collection<CascadeType> ct = Arrays.asList(CascadeType.values());
			if (ct.size() != 6) {
				logger.log(Logger.Level.ERROR, "Number of CascadeType expected:6, actual:" + ct.size());
				pass = false;
			}

			if (ct.contains(CascadeType.ALL)) {
				logger.log(Logger.Level.TRACE, "received:" + CascadeType.ALL);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CascadeType.ALL);
				pass = false;
			}
			if (ct.contains(CascadeType.DETACH)) {
				logger.log(Logger.Level.TRACE, "received:" + CascadeType.DETACH);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CascadeType.DETACH);
				pass = false;
			}
			if (ct.contains(CascadeType.MERGE)) {
				logger.log(Logger.Level.TRACE, "received:" + CascadeType.MERGE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CascadeType.MERGE);
				pass = false;
			}
			if (ct.contains(CascadeType.PERSIST)) {
				logger.log(Logger.Level.TRACE, "received:" + CascadeType.PERSIST);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CascadeType.PERSIST);
				pass = false;
			}
			if (ct.contains(CascadeType.REFRESH)) {
				logger.log(Logger.Level.TRACE, "received:" + CascadeType.REFRESH);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CascadeType.REFRESH);
				pass = false;
			}
			if (ct.contains(CascadeType.REMOVE)) {
				logger.log(Logger.Level.TRACE, "received:" + CascadeType.REMOVE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + CascadeType.REMOVE);
				pass = false;
			}

			for (CascadeType c : ct) {
				try {
					CascadeType.valueOf(c.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("cascadeTypeValuesTest failed");
		}
	}

	/*
	 * @testName: discriminatorTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:26
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void discriminatorTypeValueOfTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin discriminatorTypeValueOfTest");
		try {

			DiscriminatorType dt = DiscriminatorType.valueOf(DiscriminatorType.CHAR.name());
			if (!dt.equals(DiscriminatorType.CHAR)) {
				logger.log(Logger.Level.ERROR, "expected:" + DiscriminatorType.CHAR.name() + ", actual:" + dt.name());
				pass = false;
			}
			dt = DiscriminatorType.valueOf(DiscriminatorType.INTEGER.name());
			if (!dt.equals(DiscriminatorType.INTEGER)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + DiscriminatorType.INTEGER.name() + ", actual:" + dt.name());
				pass = false;
			}
			dt = DiscriminatorType.valueOf(DiscriminatorType.STRING.name());
			if (!dt.equals(DiscriminatorType.STRING)) {
				logger.log(Logger.Level.ERROR, "expected:" + DiscriminatorType.STRING.name() + ", actual:" + dt.name());
				pass = false;
			}
			try {
				DiscriminatorType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				DiscriminatorType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("discriminatorTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: discriminatorTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:27
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void discriminatorTypeValuesTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin discriminatorTypeValuesTest");
		try {

			Collection<DiscriminatorType> dt = Arrays.asList(DiscriminatorType.values());
			if (dt.size() != 3) {
				logger.log(Logger.Level.ERROR, "Number of DiscriminatorType expected:3, actual:" + dt.size());
				pass = false;
			}

			if (dt.contains(DiscriminatorType.CHAR)) {
				logger.log(Logger.Level.TRACE, "received:" + DiscriminatorType.CHAR);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + DiscriminatorType.CHAR);
				pass = false;
			}
			if (dt.contains(DiscriminatorType.INTEGER)) {
				logger.log(Logger.Level.TRACE, "received:" + DiscriminatorType.INTEGER);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + DiscriminatorType.INTEGER);
				pass = false;
			}
			if (dt.contains(DiscriminatorType.STRING)) {
				logger.log(Logger.Level.TRACE, "received:" + DiscriminatorType.STRING);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + DiscriminatorType.STRING);
				pass = false;
			}

			for (DiscriminatorType d : dt) {
				try {
					DiscriminatorType.valueOf(d.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("discriminatorTypeValuesTest failed");
		}
	}

	/*
	 * @testName: enumTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:74
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void enumTypeValueOfTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin enumTypeValueOfTest");
		try {

			EnumType et = EnumType.valueOf(EnumType.ORDINAL.name());
			if (!et.equals(EnumType.ORDINAL)) {
				logger.log(Logger.Level.ERROR, "expected:" + EnumType.ORDINAL.name() + ", actual:" + et.name());
				pass = false;
			}
			et = EnumType.valueOf(EnumType.STRING.name());
			if (!et.equals(EnumType.STRING)) {
				logger.log(Logger.Level.ERROR, "expected:" + EnumType.STRING.name() + ", actual:" + et.name());
				pass = false;
			}
			try {
				EnumType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				EnumType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("enumTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: enumTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:73
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void enumTypeValuesTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin enumTypeValuesTest");
		try {

			Collection<EnumType> et = Arrays.asList(EnumType.values());
			if (et.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of EnumType expected:2, actual:" + et.size());
				pass = false;
			}

			if (et.contains(EnumType.ORDINAL)) {
				logger.log(Logger.Level.TRACE, "received:" + EnumType.ORDINAL);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + EnumType.ORDINAL);
				pass = false;
			}
			if (et.contains(EnumType.STRING)) {
				logger.log(Logger.Level.TRACE, "received:" + EnumType.STRING);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + EnumType.STRING);
				pass = false;
			}

			for (EnumType e : et) {
				try {
					EnumType.valueOf(e.name());
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", ex);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("enumTypeValuesTest failed");
		}
	}

	/*
	 * @testName: fetchTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:75
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void fetchTypeValueOfTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin fetchTypeValueOfTest");
		try {

			FetchType ft = FetchType.valueOf(FetchType.EAGER.name());
			if (!ft.equals(FetchType.EAGER)) {
				logger.log(Logger.Level.ERROR, "expected:" + FetchType.EAGER.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = FetchType.valueOf(FetchType.LAZY.name());
			if (!ft.equals(FetchType.LAZY)) {
				logger.log(Logger.Level.ERROR, "expected:" + FetchType.LAZY.name() + ", actual:" + ft.name());
				pass = false;
			}
			try {
				FetchType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				FetchType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("fetchTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: fetchTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:76
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void fetchTypeValuesTest() throws Exception {
		boolean pass = true;

		logger.log(Logger.Level.TRACE, "Begin fetchTypeValuesTest");
		try {

			Collection<FetchType> ft = Arrays.asList(FetchType.values());
			if (ft.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of FetchType expected:2, actual:" + ft.size());
				pass = false;
			}

			if (ft.contains(FetchType.EAGER)) {
				logger.log(Logger.Level.TRACE, "received:" + FetchType.EAGER);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + FetchType.EAGER);
				pass = false;
			}
			if (ft.contains(FetchType.LAZY)) {
				logger.log(Logger.Level.TRACE, "received:" + FetchType.LAZY);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + FetchType.LAZY);
				pass = false;
			}

			for (FetchType f : ft) {
				try {
					FetchType.valueOf(f.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("fetchTypeValuesTest failed");
		}
	}

	/*
	 * @testName: flushModeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:79
	 * 
	 * @test_Strategy: Verify the various values of FlushModeType can verified set
	 * using valueOf
	 *
	 */
	@Test
	public void flushModeTypeValueOfTest() throws Exception {
		boolean pass = true;
		try {

			FlushModeType fmt = FlushModeType.valueOf(FlushModeType.AUTO.name());
			if (!fmt.equals(FlushModeType.AUTO)) {
				logger.log(Logger.Level.ERROR, "expected:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
				pass = false;
			}
			fmt = FlushModeType.valueOf(FlushModeType.COMMIT.name());
			if (!fmt.equals(FlushModeType.COMMIT)) {
				logger.log(Logger.Level.ERROR, "expected:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
				pass = false;
			}
			try {
				FlushModeType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				FlushModeType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("flushModeTypeValueOfTest failed");
	}

	/*
	 * @testName: flushModeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:80
	 * 
	 * @test_Strategy: Verify the various values of FlushModeType using Values then
	 * try valueOf for the returned values
	 *
	 */
	@Test
	public void flushModeTypeValuesTest() throws Exception {
		boolean pass = true;
		int count = 0;
		try {
			Collection<FlushModeType> fmt = Arrays.asList(FlushModeType.values());
			if (fmt.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of FlushModeType expected:2, actual:" + fmt.size());
				pass = false;
			}

			if (fmt.contains(FlushModeType.COMMIT)) {
				logger.log(Logger.Level.TRACE, "received:" + FlushModeType.COMMIT);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + FlushModeType.COMMIT);
				pass = false;
			}
			if (fmt.contains(FlushModeType.AUTO)) {
				logger.log(Logger.Level.TRACE, "received:" + FlushModeType.AUTO);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + FlushModeType.AUTO);
				pass = false;
			}

			for (FlushModeType f : fmt) {
				try {
					FlushModeType.valueOf(f.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("flushModeTypeValuesTest failed");
	}

	/*
	 * @testName: setgetFlushModeEntityManagerTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:173
	 * 
	 * @test_Strategy: Set and Get the various flushModes of the EntityManager
	 */
	@Test
	public void setgetFlushModeEntityManagerTest() throws Exception {
		boolean pass = true;
		try {
			EntityTransaction t = getEntityTransaction();
			t.begin();
			EntityManager em = getEntityManager();
			logger.log(Logger.Level.TRACE, "Checking Default mode");
			FlushModeType fmt = em.getFlushMode();
			if (fmt.equals(FlushModeType.AUTO)) {
				logger.log(Logger.Level.TRACE, "Checking COMMIT");
				em.setFlushMode(FlushModeType.COMMIT);
				fmt = em.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE, "Checking AUTO");
					em.setFlushMode(FlushModeType.AUTO);
					fmt = em.getFlushMode();
					if (!fmt.equals(FlushModeType.AUTO)) {
						logger.log(Logger.Level.ERROR,
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected a value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected a default value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			pass = false;

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass)
			throw new Exception("setgetFlushModeEntityManagerTest failed");
	}

	/*
	 * @testName: setgetFlushModeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:46; PERSISTENCE:JAVADOC:56
	 * 
	 * @test_Strategy: Set and Get the various flushModes of a Query
	 */
	@Test
	public void setgetFlushModeTest() throws Exception {
		boolean pass = true;
		try {
			EntityManager em = getEntityManager();
			Query q = em.createQuery("SELECT o FROM Order o WHERE o.id = 1");
			logger.log(Logger.Level.TRACE, "Getting mode from query");
			FlushModeType fmt = q.getFlushMode();
			if (fmt.equals(em.getFlushMode())) {
				logger.log(Logger.Level.TRACE, "Setting mode to return default mode");
				q.setFlushMode(fmt);
				logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.COMMIT");
				q.setFlushMode(FlushModeType.COMMIT);
				fmt = q.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.AUTO");
					q.setFlushMode(FlushModeType.AUTO);
					fmt = q.getFlushMode();
					if (fmt.equals(FlushModeType.AUTO)) {
						logger.log(Logger.Level.TRACE, "Received expected FlushModeType:" + fmt.name());
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected a default value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected EntityManager value of:" + em.getFlushMode() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("setgetFlushModeTest failed");
	}

	/*
	 * @testName: setgetFlushModeTQTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:441; PERSISTENCE:JAVADOC:56
	 * 
	 * @test_Strategy: Set and Get the various flushModes of a TypedQuery
	 */
	@Test
	public void setgetFlushModeTQTest() throws Exception {
		boolean pass = true;
		try {
			EntityManager em = getEntityManager();
			TypedQuery<Order> q = em.createQuery("SELECT o FROM Order o WHERE o.id = 1", Order.class);

			FlushModeType fmt = q.getFlushMode();
			if (fmt.equals(em.getFlushMode())) {
				logger.log(Logger.Level.TRACE, "Setting mode to returned default mode");
				q.setFlushMode(fmt);
				logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.COMMIT");
				q.setFlushMode(FlushModeType.COMMIT);
				fmt = q.getFlushMode();
				if (fmt.equals(FlushModeType.COMMIT)) {
					logger.log(Logger.Level.TRACE, "Setting mode to FlushModeType.AUTO");
					q.setFlushMode(FlushModeType.AUTO);
					fmt = q.getFlushMode();
					if (!fmt.equals(FlushModeType.AUTO)) {
						logger.log(Logger.Level.ERROR,
								"Expected a value of:" + FlushModeType.AUTO.name() + ", actual:" + fmt.name());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Expected a default value of:" + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected EntityManager value of:" + em.getFlushMode().name() + ", actual:" + fmt.name());
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("setgetFlushModeTQTest failed");
	}

	/*
	 * @testName: generationTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:83
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void generationTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			GenerationType ft = GenerationType.valueOf(GenerationType.AUTO.name());
			if (!ft.equals(GenerationType.AUTO)) {
				logger.log(Logger.Level.ERROR, "expected:" + GenerationType.AUTO.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.IDENTITY.name());
			if (!ft.equals(GenerationType.IDENTITY)) {
				logger.log(Logger.Level.ERROR, "expected:" + GenerationType.IDENTITY.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.SEQUENCE.name());

			if (!ft.equals(GenerationType.SEQUENCE)) {
				logger.log(Logger.Level.ERROR, "expected:" + GenerationType.SEQUENCE.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.TABLE.name());
			if (!ft.equals(GenerationType.TABLE)) {
				logger.log(Logger.Level.ERROR, "expected:" + GenerationType.TABLE.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = GenerationType.valueOf(GenerationType.UUID.name());
			if (!ft.equals(GenerationType.UUID)) {
				logger.log(Logger.Level.ERROR, "expected:" + GenerationType.UUID.name() + ", actual:" + ft.name());
				pass = false;
			}
			try {
				GenerationType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				GenerationType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("generationTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: generationTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:84
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void generationTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<GenerationType> gt = Arrays.asList(GenerationType.values());
			if (gt.size() != 5) {
				logger.log(Logger.Level.ERROR, "Number of GenerationType expected:5, actual:" + gt.size());
				pass = false;
			}

			if (gt.contains(GenerationType.AUTO)) {
				logger.log(Logger.Level.TRACE, "received:" + GenerationType.AUTO);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + GenerationType.AUTO);
				pass = false;
			}
			if (gt.contains(GenerationType.IDENTITY)) {
				logger.log(Logger.Level.TRACE, "received:" + GenerationType.IDENTITY);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + GenerationType.IDENTITY);
				pass = false;
			}
			if (gt.contains(GenerationType.SEQUENCE)) {
				logger.log(Logger.Level.TRACE, "received:" + GenerationType.SEQUENCE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + GenerationType.SEQUENCE);
				pass = false;
			}
			if (gt.contains(GenerationType.TABLE)) {
				logger.log(Logger.Level.TRACE, "received:" + GenerationType.TABLE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + GenerationType.TABLE);
				pass = false;
			}
			if (gt.contains(GenerationType.UUID)) {
				logger.log(Logger.Level.TRACE, "received:" + GenerationType.UUID);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + GenerationType.UUID);
				pass = false;
			}

			for (GenerationType g : gt) {
				try {
					GenerationType.valueOf(g.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("generationTypeValuesTest failed");
		}
	}

	/*
	 * @testName: inheritanceTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:87
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void inheritanceTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			InheritanceType ft = InheritanceType.valueOf(InheritanceType.JOINED.name());
			if (!ft.equals(InheritanceType.JOINED)) {
				logger.log(Logger.Level.ERROR, "expected:" + InheritanceType.JOINED.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = InheritanceType.valueOf(InheritanceType.SINGLE_TABLE.name());
			if (!ft.equals(InheritanceType.SINGLE_TABLE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + InheritanceType.SINGLE_TABLE.name() + ", actual:" + ft.name());
				pass = false;
			}
			ft = InheritanceType.valueOf(InheritanceType.TABLE_PER_CLASS.name());

			if (!ft.equals(InheritanceType.TABLE_PER_CLASS)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + InheritanceType.TABLE_PER_CLASS.name() + ", actual:" + ft.name());
				pass = false;
			}
			try {
				InheritanceType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				InheritanceType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("inheritanceTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: inheritanceTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:88
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void inheritanceTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<InheritanceType> it = Arrays.asList(InheritanceType.values());
			if (it.size() != 3) {
				logger.log(Logger.Level.ERROR, "Number of InheritanceType expected:3, actual:" + it.size());
				pass = false;
			}

			if (it.contains(InheritanceType.JOINED)) {
				logger.log(Logger.Level.TRACE, "received:" + InheritanceType.JOINED);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + InheritanceType.JOINED);
				pass = false;
			}
			if (it.contains(InheritanceType.SINGLE_TABLE)) {
				logger.log(Logger.Level.TRACE, "received:" + InheritanceType.SINGLE_TABLE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + InheritanceType.SINGLE_TABLE);
				pass = false;
			}
			if (it.contains(InheritanceType.TABLE_PER_CLASS)) {
				logger.log(Logger.Level.TRACE, "received:" + InheritanceType.TABLE_PER_CLASS);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + InheritanceType.TABLE_PER_CLASS);
				pass = false;
			}

			for (InheritanceType i : it) {
				try {
					InheritanceType.valueOf(i.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("inheritanceTypeValuesTest failed");
		}
	}

	/*
	 * @testName: lockModeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:104
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void lockModeTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			LockModeType lm = LockModeType.valueOf(LockModeType.NONE.name());
			if (!lm.equals(LockModeType.NONE)) {
				logger.log(Logger.Level.ERROR, "expected:" + LockModeType.NONE + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.OPTIMISTIC.name());
			if (!lm.equals(LockModeType.OPTIMISTIC)) {
				logger.log(Logger.Level.ERROR, "expected:" + LockModeType.OPTIMISTIC.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.OPTIMISTIC_FORCE_INCREMENT.name());
			if (!lm.equals(LockModeType.OPTIMISTIC_FORCE_INCREMENT)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.PESSIMISTIC_READ.name());
			if (!lm.equals(LockModeType.PESSIMISTIC_READ)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + LockModeType.PESSIMISTIC_READ.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.PESSIMISTIC_WRITE.name());
			if (!lm.equals(LockModeType.PESSIMISTIC_WRITE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + LockModeType.PESSIMISTIC_WRITE.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.READ.name());
			if (!lm.equals(LockModeType.READ)) {
				logger.log(Logger.Level.ERROR, "expected:" + LockModeType.READ.name() + ", actual:" + lm.name());
				pass = false;
			}
			lm = LockModeType.valueOf(LockModeType.WRITE.name());
			if (!lm.equals(LockModeType.WRITE)) {
				logger.log(Logger.Level.ERROR, "expected:" + LockModeType.WRITE.name() + ", actual:" + lm.name());
				pass = false;
			}

			try {
				LockModeType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				LockModeType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("lockModeTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: lockModeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:105
	 * 
	 * @test_Strategy: Test each LockModeType value
	 *
	 */
	@Test
	public void lockModeTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<LockModeType> lm = Arrays.asList(LockModeType.values());
			if (lm.size() != 8) {
				logger.log(Logger.Level.ERROR, "Number of LockModeTypes expected:8, actual:" + lm.size());
				pass = false;
			}

			if (lm.contains(LockModeType.NONE)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.NONE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.NONE);
				pass = false;
			}
			if (lm.contains(LockModeType.OPTIMISTIC)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.OPTIMISTIC);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.OPTIMISTIC);
				pass = false;
			}
			if (lm.contains(LockModeType.OPTIMISTIC_FORCE_INCREMENT)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.OPTIMISTIC_FORCE_INCREMENT);
				pass = false;
			}
			if (lm.contains(LockModeType.PESSIMISTIC_FORCE_INCREMENT)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.PESSIMISTIC_FORCE_INCREMENT);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.PESSIMISTIC_FORCE_INCREMENT);
				pass = false;
			}
			if (lm.contains(LockModeType.PESSIMISTIC_READ)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.PESSIMISTIC_READ);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.PESSIMISTIC_READ);
				pass = false;
			}
			if (lm.contains(LockModeType.PESSIMISTIC_WRITE)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.PESSIMISTIC_WRITE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.PESSIMISTIC_WRITE);
				pass = false;
			}
			if (lm.contains(LockModeType.READ)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.READ);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.READ);
				pass = false;
			}
			if (lm.contains(LockModeType.WRITE)) {
				logger.log(Logger.Level.TRACE, "received:" + LockModeType.WRITE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + LockModeType.WRITE);
				pass = false;
			}

			for (LockModeType l : lm) {
				try {
					LockModeType.valueOf(l.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("lockModeTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistenceContextTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:154
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistenceContextTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistenceContextType pct = PersistenceContextType.valueOf(PersistenceContextType.EXTENDED.name());
			if (!pct.equals(PersistenceContextType.EXTENDED)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistenceContextType.EXTENDED.name() + ", actual:" + pct.name());
				pass = false;
			}
			pct = PersistenceContextType.valueOf(PersistenceContextType.TRANSACTION.name());
			if (!pct.equals(PersistenceContextType.TRANSACTION)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistenceContextType.TRANSACTION.name() + ", actual:" + pct.name());
				pass = false;
			}
			try {
				PersistenceContextType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				PersistenceContextType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("persistenceContextTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: persistenceContextTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:155
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistenceContextTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<PersistenceContextType> pct = Arrays.asList(PersistenceContextType.values());
			if (pct.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of PersistenceContextType expected:2, actual:" + pct.size());
				pass = false;
			}

			if (pct.contains(PersistenceContextType.EXTENDED)) {
				logger.log(Logger.Level.TRACE, "received:" + PersistenceContextType.EXTENDED);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceContextType.EXTENDED);
				pass = false;
			}
			if (pct.contains(PersistenceContextType.TRANSACTION)) {
				logger.log(Logger.Level.TRACE, "received:" + PersistenceContextType.TRANSACTION);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceContextType.TRANSACTION);
				pass = false;
			}

			for (PersistenceContextType p : pct) {
				try {
					PersistenceContextType.valueOf(p.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("persistenceContextTypeValuesTest failed");
		}
	}

	/*
	 * @testName: pessimisticLockScopeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:397
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void pessimisticLockScopeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PessimisticLockScope pls = PessimisticLockScope.valueOf(PessimisticLockScope.EXTENDED.name());
			if (!pls.equals(PessimisticLockScope.EXTENDED)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PessimisticLockScope.EXTENDED.name() + ", actual:" + pls.name());
				pass = false;
			}
			pls = PessimisticLockScope.valueOf(PessimisticLockScope.NORMAL.name());
			if (!pls.equals(PessimisticLockScope.NORMAL)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PessimisticLockScope.NORMAL.name() + ", actual:" + pls.name());
				pass = false;
			}
			try {
				PessimisticLockScope.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				PessimisticLockScope.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("pessimisticLockScopeValueOfTest failed");
		}
	}

	/*
	 * @testName: pessimisticLockScopeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:398
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void pessimisticLockScopeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<PessimisticLockScope> pls = Arrays.asList(PessimisticLockScope.values());
			if (pls.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of PessimisticLockScope expected:2, actual:" + pls.size());
				pass = false;
			}

			if (pls.contains(PessimisticLockScope.EXTENDED)) {
				logger.log(Logger.Level.TRACE, "received:" + PessimisticLockScope.EXTENDED);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + PessimisticLockScope.EXTENDED);
				pass = false;
			}
			if (pls.contains(PessimisticLockScope.NORMAL)) {
				logger.log(Logger.Level.TRACE, "received:" + PessimisticLockScope.NORMAL);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + PessimisticLockScope.NORMAL);
				pass = false;
			}

			for (PessimisticLockScope p : pls) {
				try {
					PessimisticLockScope.valueOf(p.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("pessimisticLockScopeValuesTest failed");
		}
	}

	/*
	 * @testName: sharedCacheModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:427; PERSISTENCE:SPEC:1910;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void sharedCacheModeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			SharedCacheMode scm = SharedCacheMode.valueOf(SharedCacheMode.ALL.name());
			if (!scm.equals(SharedCacheMode.ALL)) {
				logger.log(Logger.Level.ERROR, "expected:" + SharedCacheMode.ALL.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.DISABLE_SELECTIVE.name());
			if (!scm.equals(SharedCacheMode.DISABLE_SELECTIVE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + SharedCacheMode.DISABLE_SELECTIVE.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.ENABLE_SELECTIVE.name());
			if (!scm.equals(SharedCacheMode.ENABLE_SELECTIVE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + SharedCacheMode.ENABLE_SELECTIVE.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.NONE.name());
			if (!scm.equals(SharedCacheMode.NONE)) {
				logger.log(Logger.Level.ERROR, "expected:" + SharedCacheMode.NONE.name() + ", actual:" + scm.name());
				pass = false;
			}
			scm = SharedCacheMode.valueOf(SharedCacheMode.UNSPECIFIED.name());
			if (!scm.equals(SharedCacheMode.UNSPECIFIED)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + SharedCacheMode.UNSPECIFIED.name() + ", actual:" + scm.name());
				pass = false;
			}
			try {
				SharedCacheMode.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				SharedCacheMode.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("sharedCacheModeValueOfTest failed");
		}
	}

	/*
	 * @testName: sharedCacheModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:428; PERSISTENCE:SPEC:1910;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void sharedCacheModeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<SharedCacheMode> scm = Arrays.asList(SharedCacheMode.values());
			if (scm.size() != 5) {
				logger.log(Logger.Level.ERROR, "Number of SharedCacheMode expected:5, actual:" + scm.size());
				pass = false;
			}

			if (scm.contains(SharedCacheMode.ALL)) {
				logger.log(Logger.Level.TRACE, "received:" + SharedCacheMode.ALL);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + SharedCacheMode.ALL);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.DISABLE_SELECTIVE)) {
				logger.log(Logger.Level.TRACE, "received:" + SharedCacheMode.DISABLE_SELECTIVE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + SharedCacheMode.DISABLE_SELECTIVE);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.ENABLE_SELECTIVE)) {
				logger.log(Logger.Level.TRACE, "received:" + SharedCacheMode.ENABLE_SELECTIVE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + SharedCacheMode.ENABLE_SELECTIVE);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.NONE)) {
				logger.log(Logger.Level.TRACE, "received:" + SharedCacheMode.NONE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + SharedCacheMode.NONE);
				pass = false;
			}
			if (scm.contains(SharedCacheMode.UNSPECIFIED)) {
				logger.log(Logger.Level.TRACE, "received:" + SharedCacheMode.UNSPECIFIED);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + SharedCacheMode.UNSPECIFIED);
				pass = false;
			}

			for (SharedCacheMode s : scm) {
				try {
					SharedCacheMode.valueOf(s.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("sharedCacheModeValuesTest failed");
		}
	}

	/*
	 * @testName: validationModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:455
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void validationModeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			ValidationMode vm = ValidationMode.valueOf(ValidationMode.CALLBACK.name());
			if (!vm.equals(ValidationMode.CALLBACK)) {
				logger.log(Logger.Level.ERROR, "expected:" + ValidationMode.CALLBACK.name() + ", actual:" + vm.name());
				pass = false;
			}
			vm = ValidationMode.valueOf(ValidationMode.AUTO.name());
			if (!vm.equals(ValidationMode.AUTO)) {
				logger.log(Logger.Level.ERROR, "expected:" + ValidationMode.AUTO.name() + ", actual:" + vm.name());
				pass = false;
			}
			vm = ValidationMode.valueOf(ValidationMode.NONE.name());
			if (!vm.equals(ValidationMode.NONE)) {
				logger.log(Logger.Level.ERROR, "expected:" + ValidationMode.NONE.name() + ", actual:" + vm.name());
				pass = false;
			}
			try {
				ValidationMode.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				ValidationMode.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("validationModeValueOfTest failed");
		}
	}

	/*
	 * @testName: validationModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:456
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void validationModeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<ValidationMode> vm = Arrays.asList(ValidationMode.values());
			if (vm.size() != 3) {
				logger.log(Logger.Level.ERROR, "Number of ValidationMode expected:3, actual:" + vm.size());
				pass = false;
			}

			if (vm.contains(ValidationMode.CALLBACK)) {
				logger.log(Logger.Level.TRACE, "received:" + ValidationMode.CALLBACK);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + ValidationMode.CALLBACK);
				pass = false;
			}
			if (vm.contains(ValidationMode.AUTO)) {
				logger.log(Logger.Level.TRACE, "received:" + ValidationMode.AUTO);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + ValidationMode.AUTO);
				pass = false;
			}
			if (vm.contains(ValidationMode.NONE)) {
				logger.log(Logger.Level.TRACE, "received:" + ValidationMode.NONE);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + ValidationMode.NONE);
				pass = false;
			}

			for (ValidationMode v : vm) {
				try {
					ValidationMode.valueOf(v.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("validationModeValuesTest failed");
		}
	}

	/*
	 * @testName: temporalTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:218
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void temporalTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<TemporalType> tt = Arrays.asList(TemporalType.values());
		if (tt.size() != 3) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:3, actual:" + tt.size());
			pass = false;
		}

		if (tt.contains(TemporalType.DATE)) {
			logger.log(Logger.Level.TRACE, "received:" + TemporalType.DATE);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + TemporalType.DATE);
			pass = false;
		}
		if (tt.contains(TemporalType.TIME)) {
			logger.log(Logger.Level.TRACE, "received:" + TemporalType.TIME);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + TemporalType.TIME);
			pass = false;
		}
		if (tt.contains(TemporalType.TIMESTAMP)) {
			logger.log(Logger.Level.TRACE, "received:" + TemporalType.TIMESTAMP);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + TemporalType.TIMESTAMP);
			pass = false;
		}

		for (TemporalType t : tt) {
			try {
				TemporalType.valueOf(t.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("temporalTypeValuesTest failed");
		}

	}

	/*
	 * @testName: temporalTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:217
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void temporalTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			TemporalType tt = TemporalType.valueOf(TemporalType.DATE.name());
			if (!tt.equals(TemporalType.DATE)) {
				logger.log(Logger.Level.ERROR, "expected:" + TemporalType.DATE.name() + ", actual:" + tt.name());
				pass = false;
			}
			tt = TemporalType.valueOf(TemporalType.TIME.name());
			if (!tt.equals(TemporalType.TIME)) {
				logger.log(Logger.Level.ERROR, "expected:" + TemporalType.TIME.name() + ", actual:" + tt.name());
				pass = false;
			}
			tt = TemporalType.valueOf(TemporalType.TIMESTAMP.name());
			if (!tt.equals(TemporalType.TIMESTAMP)) {
				logger.log(Logger.Level.ERROR, "expected:" + TemporalType.TIMESTAMP.name() + ", actual:" + tt.name());
				pass = false;
			}

			try {
				TemporalType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				TemporalType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("temporalTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: joinTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1073
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void joinTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<JoinType> jt = Arrays.asList(JoinType.values());
		if (jt.size() != 3) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:3, actual:" + jt.size());
			pass = false;
		}

		if (jt.contains(JoinType.LEFT)) {
			logger.log(Logger.Level.TRACE, "received:" + JoinType.LEFT);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + JoinType.LEFT);
			pass = false;
		}
		if (jt.contains(JoinType.INNER)) {
			logger.log(Logger.Level.TRACE, "received:" + JoinType.INNER);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + JoinType.INNER);
			pass = false;
		}
		if (jt.contains(JoinType.RIGHT)) {
			logger.log(Logger.Level.TRACE, "received:" + JoinType.RIGHT);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + JoinType.RIGHT);
			pass = false;
		}

		for (JoinType j : jt) {
			try {
				JoinType.valueOf(j.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("joinTypeValuesTest failed");
		}
	}

	/*
	 * @testName: joinTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1072
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void joinTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			JoinType jt = JoinType.valueOf(JoinType.LEFT.name());
			if (!jt.equals(JoinType.LEFT)) {
				logger.log(Logger.Level.ERROR, "expected:" + JoinType.LEFT.name() + ", actual:" + jt.name());
				pass = false;
			}
			jt = JoinType.valueOf(JoinType.INNER.name());
			if (!jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.ERROR, "expected:" + JoinType.INNER.name() + ", actual:" + jt.name());
				pass = false;
			}
			jt = JoinType.valueOf(JoinType.RIGHT.name());
			if (!jt.equals(JoinType.RIGHT)) {
				logger.log(Logger.Level.ERROR, "expected:" + JoinType.RIGHT.name() + ", actual:" + jt.name());
				pass = false;
			}

			try {
				JoinType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				JoinType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("joinTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: persistentAttributeTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1221
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistentAttributeTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<PersistentAttributeType> pat = Arrays.asList(PersistentAttributeType.values());

		if (pat.size() != 7) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:7, actual:" + pat.size());
			pass = false;
		}

		if (pat.contains(PersistentAttributeType.BASIC)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistentAttributeType.BASIC);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistentAttributeType.BASIC);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.ELEMENT_COLLECTION)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistentAttributeType.ELEMENT_COLLECTION);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistentAttributeType.ELEMENT_COLLECTION);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.EMBEDDED)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistentAttributeType.EMBEDDED);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistentAttributeType.EMBEDDED);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.MANY_TO_MANY)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistentAttributeType.MANY_TO_MANY);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistentAttributeType.MANY_TO_MANY);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.MANY_TO_ONE)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistentAttributeType.MANY_TO_ONE);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistentAttributeType.MANY_TO_ONE);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.ONE_TO_MANY)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistentAttributeType.ONE_TO_MANY);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistentAttributeType.ONE_TO_MANY);
			pass = false;
		}
		if (pat.contains(PersistentAttributeType.ONE_TO_ONE)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistentAttributeType.ONE_TO_ONE);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistentAttributeType.ONE_TO_ONE);
			pass = false;
		}

		for (PersistentAttributeType p : pat) {
			try {
				PersistentAttributeType.valueOf(p.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("persistentAttributeTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistentAttributeTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1220
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistentAttributeTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistentAttributeType pat = PersistentAttributeType.valueOf(PersistentAttributeType.BASIC.name());
			if (!pat.equals(PersistentAttributeType.BASIC)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistentAttributeType.BASIC.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.ELEMENT_COLLECTION.name());
			if (!pat.equals(PersistentAttributeType.ELEMENT_COLLECTION)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistentAttributeType.ELEMENT_COLLECTION.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.EMBEDDED.name());
			if (!pat.equals(PersistentAttributeType.EMBEDDED)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistentAttributeType.EMBEDDED.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.MANY_TO_MANY.name());
			if (!pat.equals(PersistentAttributeType.MANY_TO_MANY)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistentAttributeType.MANY_TO_MANY.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.MANY_TO_ONE.name());
			if (!pat.equals(PersistentAttributeType.MANY_TO_ONE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistentAttributeType.MANY_TO_ONE.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.ONE_TO_MANY.name());
			if (!pat.equals(PersistentAttributeType.ONE_TO_MANY)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistentAttributeType.ONE_TO_MANY.name() + ", actual:" + pat.name());
				pass = false;
			}
			pat = PersistentAttributeType.valueOf(PersistentAttributeType.ONE_TO_ONE.name());
			if (!pat.equals(PersistentAttributeType.ONE_TO_ONE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistentAttributeType.ONE_TO_ONE.name() + ", actual:" + pat.name());
				pass = false;
			}
			try {
				PersistentAttributeType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				PersistentAttributeType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("persistentAttributeTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: bindableTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1227
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void bindableTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<BindableType> bt = Arrays.asList(BindableType.values());

		if (bt.size() != 3) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:3, actual:" + bt.size());
			pass = false;
		}

		if (bt.contains(BindableType.ENTITY_TYPE)) {
			logger.log(Logger.Level.TRACE, "received:" + BindableType.ENTITY_TYPE.name());
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + BindableType.ENTITY_TYPE.name());
			pass = false;
		}
		if (bt.contains(BindableType.PLURAL_ATTRIBUTE)) {
			logger.log(Logger.Level.TRACE, "received:" + BindableType.PLURAL_ATTRIBUTE.name());
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + BindableType.PLURAL_ATTRIBUTE.name());
			pass = false;
		}
		if (bt.contains(BindableType.SINGULAR_ATTRIBUTE)) {
			logger.log(Logger.Level.TRACE, "received:" + BindableType.SINGULAR_ATTRIBUTE.name());
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + BindableType.SINGULAR_ATTRIBUTE.name());
			pass = false;
		}

		for (BindableType b : bt) {
			try {
				BindableType.valueOf(b.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("bindableTypeValuesTest failed");
		}
	}

	/*
	 * @testName: bindableTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1226
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void bindableTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			BindableType bt = BindableType.valueOf(BindableType.ENTITY_TYPE.name());
			if (!bt.equals(BindableType.ENTITY_TYPE)) {
				logger.log(Logger.Level.ERROR, "expected:" + BindableType.ENTITY_TYPE.name() + ", actual:" + bt.name());
				pass = false;
			}
			bt = BindableType.valueOf(BindableType.PLURAL_ATTRIBUTE.name());
			if (!bt.equals(BindableType.PLURAL_ATTRIBUTE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + BindableType.PLURAL_ATTRIBUTE.name() + ", actual:" + bt.name());
				pass = false;
			}
			bt = BindableType.valueOf(BindableType.SINGULAR_ATTRIBUTE.name());
			if (!bt.equals(BindableType.SINGULAR_ATTRIBUTE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + BindableType.SINGULAR_ATTRIBUTE.name() + ", actual:" + bt.name());
				pass = false;
			}
			try {
				BindableType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				BindableType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("bindableTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: collectionTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1454
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void collectionTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<CollectionType> ct = Arrays.asList(CollectionType.values());

		if (ct.size() != 4) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:4, actual:" + ct.size());
			pass = false;
		}

		if (ct.contains(CollectionType.COLLECTION)) {
			logger.log(Logger.Level.TRACE, "received:" + CollectionType.COLLECTION);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + CollectionType.COLLECTION);
			pass = false;
		}
		if (ct.contains(CollectionType.LIST)) {
			logger.log(Logger.Level.TRACE, "received:" + CollectionType.LIST);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + CollectionType.LIST);
			pass = false;
		}
		if (ct.contains(CollectionType.MAP)) {
			logger.log(Logger.Level.TRACE, "received:" + CollectionType.MAP);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + CollectionType.MAP);
			pass = false;
		}
		if (ct.contains(CollectionType.SET)) {
			logger.log(Logger.Level.TRACE, "received:" + CollectionType.SET);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + CollectionType.SET);
			pass = false;
		}

		for (CollectionType c : ct) {
			try {
				CollectionType.valueOf(c.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("collectionTypeValuesTest failed");
		}
	}

	/*
	 * @testName: collectionTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1453
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void collectionTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			CollectionType ct = CollectionType.valueOf(CollectionType.COLLECTION.name());
			if (!ct.equals(CollectionType.COLLECTION)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + CollectionType.COLLECTION.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CollectionType.valueOf(CollectionType.LIST.name());
			if (!ct.equals(CollectionType.LIST)) {
				logger.log(Logger.Level.ERROR, "expected:" + CollectionType.LIST.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CollectionType.valueOf(CollectionType.MAP.name());
			if (!ct.equals(CollectionType.MAP)) {
				logger.log(Logger.Level.ERROR, "expected:" + CollectionType.MAP.name() + ", actual:" + ct.name());
				pass = false;
			}
			ct = CollectionType.valueOf(CollectionType.SET.name());
			if (!ct.equals(CollectionType.SET)) {
				logger.log(Logger.Level.ERROR, "expected:" + CollectionType.SET.name() + ", actual:" + ct.name());
				pass = false;
			}
			try {
				CollectionType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				CollectionType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("collectionTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: persistenceTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1474
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistenceTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<PersistenceType> pt = Arrays.asList(PersistenceType.values());

		if (pt.size() != 4) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:4, actual:" + pt.size());
			pass = false;
		}
		if (pt.contains(PersistenceType.BASIC)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistenceType.BASIC);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceType.BASIC);
			pass = false;
		}
		if (pt.contains(PersistenceType.EMBEDDABLE)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistenceType.EMBEDDABLE);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceType.EMBEDDABLE);
			pass = false;
		}
		if (pt.contains(PersistenceType.ENTITY)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistenceType.ENTITY);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceType.ENTITY);
			pass = false;
		}
		if (pt.contains(PersistenceType.MAPPED_SUPERCLASS)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistenceType.MAPPED_SUPERCLASS);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceType.MAPPED_SUPERCLASS);
			pass = false;
		}

		for (PersistenceType p : pt) {
			try {
				PersistenceType.valueOf(p.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("persistenceTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistenceTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1473
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistenceTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistenceType pt = PersistenceType.valueOf(PersistenceType.BASIC.name());
			if (!pt.equals(PersistenceType.BASIC)) {
				logger.log(Logger.Level.ERROR, "expected:" + PersistenceType.BASIC.name() + ", actual:" + pt.name());
				pass = false;
			}
			pt = PersistenceType.valueOf(PersistenceType.EMBEDDABLE.name());
			if (!pt.equals(PersistenceType.EMBEDDABLE)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistenceType.EMBEDDABLE.name() + ", actual:" + pt.name());
				pass = false;
			}
			pt = PersistenceType.valueOf(PersistenceType.ENTITY.name());
			if (!pt.equals(PersistenceType.ENTITY)) {
				logger.log(Logger.Level.ERROR, "expected:" + PersistenceType.ENTITY.name() + ", actual:" + pt.name());
				pass = false;
			}
			pt = PersistenceType.valueOf(PersistenceType.MAPPED_SUPERCLASS.name());
			if (!pt.equals(PersistenceType.MAPPED_SUPERCLASS)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistenceType.MAPPED_SUPERCLASS.name() + ", actual:" + pt.name());
				pass = false;
			}
			try {
				PersistenceType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				PersistenceType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("persistenceTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: loadStateValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1478
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void loadStateValuesTest() throws Exception {
		boolean pass = true;

		Collection<LoadState> ls = Arrays.asList(LoadState.values());

		if (ls.size() != 3) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:3, actual:" + ls.size());
			pass = false;
		}

		if (ls.contains(LoadState.LOADED)) {
			logger.log(Logger.Level.TRACE, "received:" + LoadState.LOADED);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + LoadState.LOADED);
			pass = false;
		}
		if (ls.contains(LoadState.NOT_LOADED)) {
			logger.log(Logger.Level.TRACE, "received:" + LoadState.NOT_LOADED);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + LoadState.NOT_LOADED);
			pass = false;
		}
		if (ls.contains(LoadState.UNKNOWN)) {
			logger.log(Logger.Level.TRACE, "received:" + LoadState.UNKNOWN);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + LoadState.UNKNOWN);
			pass = false;
		}

		for (LoadState l : ls) {
			try {
				LoadState.valueOf(l.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("loadStateValuesTest failed");
		}
	}

	/*
	 * @testName: loadStateValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1477
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void loadStateValueOfTest() throws Exception {
		boolean pass = true;

		try {

			LoadState ls = LoadState.valueOf(LoadState.LOADED.name());
			if (!ls.equals(LoadState.LOADED)) {
				logger.log(Logger.Level.ERROR, "expected:" + LoadState.LOADED.name() + ", actual:" + ls.name());
				pass = false;
			}
			ls = LoadState.valueOf(LoadState.NOT_LOADED.name());
			if (!ls.equals(LoadState.NOT_LOADED)) {
				logger.log(Logger.Level.ERROR, "expected:" + LoadState.NOT_LOADED.name() + ", actual:" + ls.name());
				pass = false;
			}
			ls = LoadState.valueOf(LoadState.UNKNOWN.name());
			if (!ls.equals(LoadState.UNKNOWN)) {
				logger.log(Logger.Level.ERROR, "expected:" + LoadState.UNKNOWN.name() + ", actual:" + ls.name());
				pass = false;
			}
			try {
				LoadState.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				LoadState.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception occurred", e);
		}

		if (!pass) {
			throw new Exception("loadStateValueOfTest failed");
		}
	}

	/*
	 * @testName: persistenceUnitTransactionTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1505; PERSISTENCE:SPEC:1909;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistenceUnitTransactionTypeValuesTest() throws Exception {
		boolean pass = true;

		Collection<PersistenceUnitTransactionType> putt = Arrays.asList(PersistenceUnitTransactionType.values());

		if (putt.size() != 2) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:2, actual:" + putt.size());
			pass = false;
		}

		if (putt.contains(PersistenceUnitTransactionType.JTA)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistenceUnitTransactionType.JTA);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceUnitTransactionType.JTA);
			pass = false;
		}
		if (putt.contains(PersistenceUnitTransactionType.RESOURCE_LOCAL)) {
			logger.log(Logger.Level.TRACE, "received:" + PersistenceUnitTransactionType.RESOURCE_LOCAL);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + PersistenceUnitTransactionType.RESOURCE_LOCAL);
			pass = false;
		}

		for (PersistenceUnitTransactionType p : putt) {
			try {
				PersistenceUnitTransactionType.valueOf(p.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("persistenceUnitTransactionTypeValuesTest failed");
		}
	}

	/*
	 * @testName: persistenceUnitTransactionTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1504
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void persistenceUnitTransactionTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			PersistenceUnitTransactionType putt = PersistenceUnitTransactionType
					.valueOf(PersistenceUnitTransactionType.JTA.name());
			if (!putt.equals(PersistenceUnitTransactionType.JTA)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistenceUnitTransactionType.JTA.name() + ", actual:" + putt.name());
				pass = false;
			}
			putt = PersistenceUnitTransactionType.valueOf(PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
			if (!putt.equals(PersistenceUnitTransactionType.RESOURCE_LOCAL)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + PersistenceUnitTransactionType.RESOURCE_LOCAL.name() + ", actual:" + putt.name());
				pass = false;
			}
			try {
				PersistenceUnitTransactionType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				PersistenceUnitTransactionType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception occurred", e);
		}

		if (!pass) {
			throw new Exception("persistenceUnitTransactionTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: parameterModeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1538
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void parameterModeValuesTest() throws Exception {
		boolean pass = true;

		Collection<ParameterMode> cpm = Arrays.asList(ParameterMode.values());

		if (cpm.size() != 4) {
			logger.log(Logger.Level.ERROR, "Number of TemporalType expected:4, actual:" + cpm.size());
			pass = false;
		}

		if (cpm.contains(ParameterMode.IN)) {
			logger.log(Logger.Level.TRACE, "received:" + ParameterMode.IN);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + ParameterMode.IN);
			pass = false;
		}
		if (cpm.contains(ParameterMode.INOUT)) {
			logger.log(Logger.Level.TRACE, "received:" + ParameterMode.INOUT);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + ParameterMode.INOUT);
			pass = false;
		}
		if (cpm.contains(ParameterMode.OUT)) {
			logger.log(Logger.Level.TRACE, "received:" + ParameterMode.OUT);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + ParameterMode.OUT);
			pass = false;
		}
		if (cpm.contains(ParameterMode.REF_CURSOR)) {
			logger.log(Logger.Level.TRACE, "received:" + ParameterMode.REF_CURSOR);
		} else {
			logger.log(Logger.Level.ERROR, "Expected value:" + ParameterMode.REF_CURSOR);
			pass = false;
		}

		for (ParameterMode pm : cpm) {
			try {
				ParameterMode.valueOf(pm.name());
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received Exception for valueOf", e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("parameterModeValuesTest failed");
		}
	}

	/*
	 * @testName: parameterModeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1537
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void parameterModeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			ParameterMode pm = ParameterMode.valueOf(ParameterMode.IN.name());
			if (!pm.equals(ParameterMode.IN)) {
				logger.log(Logger.Level.ERROR, "expected:" + ParameterMode.IN.name() + ", actual:" + pm.name());
				pass = false;
			}
			pm = ParameterMode.valueOf(ParameterMode.INOUT.name());
			if (!pm.equals(ParameterMode.INOUT)) {
				logger.log(Logger.Level.ERROR, "expected:" + ParameterMode.INOUT.name() + ", actual:" + pm.name());
				pass = false;
			}
			pm = ParameterMode.valueOf(ParameterMode.OUT.name());
			if (!pm.equals(ParameterMode.OUT)) {
				logger.log(Logger.Level.ERROR, "expected:" + ParameterMode.OUT.name() + ", actual:" + pm.name());
				pass = false;
			}
			pm = ParameterMode.valueOf(ParameterMode.REF_CURSOR.name());
			if (!pm.equals(ParameterMode.REF_CURSOR)) {
				logger.log(Logger.Level.ERROR, "expected:" + ParameterMode.REF_CURSOR.name() + ", actual:" + pm.name());
				pass = false;
			}
			try {
				ParameterMode.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				ParameterMode.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException npe) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception occurred", e);
		}

		if (!pass) {
			throw new Exception("parameterModeValueOfTest failed");
		}
	}

	/*
	 * @testName: synchronizationTypeValueOfTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1660;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void synchronizationTypeValueOfTest() throws Exception {
		boolean pass = true;

		try {

			SynchronizationType st = SynchronizationType.valueOf(SynchronizationType.SYNCHRONIZED.name());
			if (!st.equals(SynchronizationType.SYNCHRONIZED)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + SynchronizationType.SYNCHRONIZED.name() + ", actual:" + st.name());
				pass = false;
			}
			st = SynchronizationType.valueOf(SynchronizationType.UNSYNCHRONIZED.name());
			if (!st.equals(SynchronizationType.UNSYNCHRONIZED)) {
				logger.log(Logger.Level.ERROR,
						"expected:" + SynchronizationType.UNSYNCHRONIZED.name() + ", actual:" + st.name());
				pass = false;
			}
			try {
				SynchronizationType.valueOf("DOESNOTEXIST");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
				pass = false;
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			}
			try {
				SynchronizationType.valueOf(null);
				logger.log(Logger.Level.ERROR, "NullPointerException was not thrown");
				pass = false;
			} catch (NullPointerException iae) {
				logger.log(Logger.Level.TRACE, "Received expected NullPointerException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("synchronizationTypeValueOfTest failed");
		}
	}

	/*
	 * @testName: synchronizationTypeValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1661;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void synchronizationTypeValuesTest() throws Exception {
		boolean pass = true;

		try {

			Collection<SynchronizationType> st = Arrays.asList(SynchronizationType.values());
			if (st.size() != 2) {
				logger.log(Logger.Level.ERROR, "Number of SynchronizationType expected:2, actual:" + st.size());
				pass = false;
			}

			if (st.contains(SynchronizationType.SYNCHRONIZED)) {
				logger.log(Logger.Level.TRACE, "received:" + SynchronizationType.SYNCHRONIZED);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + SynchronizationType.SYNCHRONIZED);
				pass = false;
			}
			if (st.contains(SynchronizationType.UNSYNCHRONIZED)) {
				logger.log(Logger.Level.TRACE, "received:" + SynchronizationType.UNSYNCHRONIZED);
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + SynchronizationType.UNSYNCHRONIZED);
				pass = false;
			}

			for (SynchronizationType s : st) {
				try {
					SynchronizationType.valueOf(s.name());
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received exception for valueOf", e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("synchronizationTypeValuesTest failed");
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
			if (getEntityManager().isOpen()) {
				removeTestData();
			}
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
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
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
