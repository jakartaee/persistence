/*
 * Copyright (c) 2011, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.PersistenceUnitTransactionType;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.LoadState;
import jakarta.persistence.spi.PersistenceProviderResolverHolder;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.ProviderUtil;

public class PersistenceProvider
		implements jakarta.persistence.spi.PersistenceProvider, jakarta.persistence.spi.ProviderUtil {

	// Used to store static PersistenceUnitInfo where this is the provider
	static Map<String, PersistenceUnitInfoImpl> puInfoMap;

	static private final PersistenceProvider provider = new PersistenceProvider();

	protected TSLogger logger;

	public PersistenceProvider() {
		// logger = TSLogger.getInstance();
	}

	private void callLogger(String s) {
		if (logger == null) {
			logger = TSLogger.getInstance();
		}
		logger.log(s);
	}

	public void generateSchema(@Nonnull PersistenceUnitInfo info, Map<?,?> map) {
		System.out.println("Called ALTERNATE_PROVIDER: PersistenceProvider.generateSchema(PersistenceUnitInfo, Map)");
		callLogger("Called generateSchema(PersistenceUnitInfo, Map)");
	}

	@Nonnull
	public EntityManagerFactory createContainerEntityManagerFactory(@Nonnull PersistenceUnitInfo puInfo, Map<?,?> properties) {
		//
		// Since this provider is packaged in the EAR and we are not overriding
		// the provider (specified in the persistence.xml file) via
		// jakarta.persistence.provider, therefore when this
		// method is called it is ok to return an instance of EMF. The only
		// case we won't is if the unitName passed in doesn't contain "CTS".
		//
		System.out.println(
				"Called ALTERNATE_PROVIDER: PersistenceProvider.createContainerEntityManagerFactory(PersistenceUnitInfo, Map)");

		var pr = PersistenceProviderResolverHolder.getPersistenceProviderResolver();
		var list = pr.getPersistenceProviders();
		System.out.println("LIST OF PROVIDERS:");
		System.out.println("------------------");
		for (Object o : list) {
			System.out.println("PROVIDER:" + o.getClass().getName());
		}
		System.out.println("------------------");

		String unitName = puInfo.getPersistenceUnitName();
		System.out.println("ALTERNATE_PROVIDER: createContainerEntityManagerFactory - unitName:" + unitName);

        if (unitName != null && !unitName.contains("CTS")) {
            return null;
        }
		displayMap(properties);

		callLogger("Called createContainerEntityManagerFactory(PersistenceUnitInfo, Map)");
		var emf = new EntityManagerFactoryImpl(true);
		emf.properties = properties;
		var transformer = new ClassTransformerImpl();
		emf.transformer = transformer;
		puInfo.addTransformer(transformer);
		emf.puInfo = puInfo;
		emf.newTempClassloader = puInfo.getNewTempClassLoader();
		System.out.println("returning ALTERNATE_PROVIDER factory:" + emf);
		return emf;
	}

	@Nullable
	public EntityManagerFactory createEntityManagerFactory(@Nonnull String puName, Map<?,?> properties) {
		System.out.println("Called ALTERNATE_PROVIDER: PersistenceProvider.createEntityManagerFactory(String, Map)");

		if (!puName.equals("ALTPROVIDERPU")) {
			System.out.println(
					"returning null from ALTERNATE_PROVIDER: PersistenceProvider.createEntityManagerFactory(String, Map) PU Names don't match");

			return null;
		}
        if (properties != null && !properties.isEmpty()) {
            String pp = (String) properties.get("jakarta.persistence.provider");
            if (pp == null) {
                System.out.println(
                        "returning null from ALTERNATE_PROVIDER: PersistenceProvider.createEntityManagerFactory(String, Map) no provider specified");
                return null;
            }
            if (!pp.equals(
                    "ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceProvider")) {
                System.out.println(
                        "returning null from ALTERNATE_PROVIDER: PersistenceProvider.createEntityManagerFactory(String, Map) different provider specified");
                return null;

            }
        }

		var pr = PersistenceProviderResolverHolder.getPersistenceProviderResolver();
		var list = pr.getPersistenceProviders();
		System.out.println("LIST OF PROVIDERS:");
		System.out.println("------------------");
		for (Object o : list) {
			System.out.println("PROVIDER:" + o.getClass().getName());
		}
		System.out.println("------------------");

		callLogger("Called createEntityManagerFactory(String, Map");
		PersistenceUnitInfoImpl puInfo = getPersistenceUnitInfoMap().get(puName);
		EntityManagerFactoryImpl emf = null;
		if (puInfo != null) {
			puInfo = puInfo.clone();
			puInfo.classLoader = Thread.currentThread().getContextClassLoader();
			emf = new EntityManagerFactoryImpl();
			emf.properties = properties;
			emf.puInfo = puInfo;
		}
		System.out.println("returning ALTERNATE_PROVIDER factory:" + emf.toString());
		return emf;
	}

	@Override
	@Nullable
	public EntityManagerFactory createEntityManagerFactory(@Nonnull PersistenceConfiguration configuration) {
		return null;
	}

    @Override
    public boolean generateSchema(@Nonnull PersistenceConfiguration configuration) {
        callLogger("Called generateSchema()");
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Nonnull
	public ProviderUtil getProviderUtil() {
		callLogger("Called getProviderUtil()");
		return provider;
	}

    @Override
	@Nonnull
    public ClassTransformer getClassTransformer(@Nonnull PersistenceUnitInfo info, Map<?, ?> map) {
        callLogger("Called getClassTransformer()");
        return null;
    }

	@Nonnull
	public LoadState isLoaded(@Nonnull Object entity) {
		callLogger("Called isLoaded()");
		return LoadState.UNKNOWN;
	}

	@Nonnull
	public LoadState isLoadedWithReference(@Nonnull Object entity, @Nonnull String attributeName) {
		callLogger("Called isLoadedWithReference()");
		return LoadState.UNKNOWN;
	}

	@Nonnull
	public LoadState isLoadedWithoutReference(@Nonnull Object entity, @Nonnull String attributeName) {
		callLogger("Called isLoadedWithoutReference()");
		return LoadState.UNKNOWN;
	}

	/**
	 * Builds static PersistenceUnitInfo instead searching for and parsing the
	 * persistence.xml to build them It might be better to make this generic and
	 * pull values from a property file so that it an be changed through
	 * configuration. For now, it builds 3 PersistenceUnitInfo's based on what was
	 * provided in the persistence.xml and persistence_ee.xml files.
	 *
	 * @return a Map of PersistenceUnitInfo objects keyed on the name of the
	 *         persistence unit
	 */
	private static Map<String, PersistenceUnitInfoImpl> getPersistenceUnitInfoMap() {
		if (puInfoMap == null) {
			Map<String, PersistenceUnitInfoImpl> m = new HashMap<>();

			// PU 1: ALTPROVIDERPU, resource_local,
			PersistenceUnitInfoImpl puinfo = new PersistenceUnitInfoImpl();
			puinfo.puName = "ALTPROVIDERPU";
			puinfo.persistenceProviderClassName = PersistenceProvider.class.getName();
			puinfo.managedClassNames = new ArrayList<>();
			puinfo.managedClassNames.add("com.foo");
			puinfo.transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
			puinfo.excludeUnlistedClasses = false;
			URL url = null;
			try {
				url = new URL("file:///pluggability_contracts_resource_local.jar");
			} catch (Exception ex) {
			}
			List<URL> lURL = new ArrayList<>();
			lURL.add(url);
			puinfo.jarFileUrls = lURL;

			List<String> lMappingFileNames = new ArrayList<>();
			lMappingFileNames.add("META-INF/myMappingFile1.xml");
			lMappingFileNames.add("META-INF/myMappingFile2.xml");
			puinfo.mappingFileNames = lMappingFileNames;

			try {
				url = new URL("pluggability_contracts_resource_local.jar");
			} catch (Exception ex) {
			}

			puinfo.persistenceUnitRootUrl = url;

			puinfo.persistenceXMLSchemaVersion = "3.2";

			// properties:
			Properties properties = new Properties();
			properties.setProperty("jakarta.persistence.provider", "foobar");

			puinfo.properties = properties;
			m.put(puinfo.puName, puinfo);

			puInfoMap = m;
		}
		return puInfoMap;

	}

	@Override
	public boolean generateSchema(@Nonnull String persistenceUnitName, Map<?,?> map) {
		return false;
	}

	private void displayMap(Map<?,?> map) {

		if (map != null) {
            for (var me : map.entrySet()) {
				if (me.getValue() instanceof String) {
					System.out.println(
							"PersistenceProvider.displayMap() - name:" + me.getKey() + ", value:" + me.getValue());
				} else {
					System.out.println("PersistenceProvider.displayMap() - name:" + me.getKey() + ", value:"
							+ me.getValue().getClass().getName());
				}
			}
		} else {
			System.out.println("PersistenceProvider.displayMap() - Map passed in to displayMap was null");
		}
	}
}
