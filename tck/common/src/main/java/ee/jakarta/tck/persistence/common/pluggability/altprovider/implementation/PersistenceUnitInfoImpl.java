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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class PersistenceUnitInfoImpl implements PersistenceUnitInfo, Cloneable {

	public List<String> managedClassNames;

	public ClassTransformer classTransformer;

	public String puName;

	public String persistenceProviderClassName;

	public Properties properties;

	public PersistenceUnitTransactionType transactionType;

	public ClassLoader classLoader;

	public boolean excludeUnlistedClasses;

	public List<URL> jarFileUrls = null;

	public List<String> mappingFileNames = null;

	public DataSource nonJtaDataSource = null;

	public URL persistenceUnitRootUrl;

	public String persistenceXMLSchemaVersion;

	protected TSLogger logger;

	public PersistenceUnitInfoImpl() {
		logger = TSLogger.getInstance();
	}

	@Override
	public void addTransformer(ClassTransformer arg0) {
		classTransformer = arg0;
		logger.log("Called PersistenceUnitInfoImpl.addTransformer()");

	}

	@Override
	public boolean excludeUnlistedClasses() {
		logger.log("Called PersistenceUnitInfoImpl.excludeUnlistedClasses()");
		return excludeUnlistedClasses;
	}

	@Override
	public ClassLoader getClassLoader() {
		logger.log("Called PersistenceUnitInfoImpl.getClassLoader()");
		logger.log("ClassLoader:" + classLoader.toString());
		return classLoader;
	}

	@Override
	public List<URL> getJarFileUrls() {
		logger.log("Called PersistenceUnitInfoImpl.getJarFileUrls()");
		return jarFileUrls;
	}

	@Override
	public DataSource getJtaDataSource() {
		logger.log("Called PersistenceUnitInfoImpl.getJtaDataSource()");
		return nonJtaDataSource;
	}

	@Override
	public List<String> getManagedClassNames() {
		logger.log("Called PersistenceUnitInfoImpl.getManagedClassNames()");
		for (String s : managedClassNames) {
			logger.log("ManagedClassName:" + s);
		}
		return managedClassNames;
	}

	@Override
	public List<String> getMappingFileNames() {
		logger.log("Called PersistenceUnitInfoImpl.getMappingFileNames()");
		return mappingFileNames;
	}

	@Override
	public ClassLoader getNewTempClassLoader() {
		logger.log("Called PersistenceUnitInfoImpl.getNewTempClassLoader()");
		logger.log("NewTempClassLoader:" + classLoader.toString());
		return classLoader;
	}

	@Override
	public DataSource getNonJtaDataSource() {
		logger.log("Called PersistenceUnitInfoImpl.getNonJtaDataSource()");
		return null;
	}

	@Override
	public String getPersistenceProviderClassName() {
		logger.log("Called PersistenceUnitInfoImpl.getPersistenceProviderClassName()");
		logger.log("PersistenceProviderClassName:" + persistenceProviderClassName);
		return persistenceProviderClassName;
	}

	@Override
	public String getScopeAnnotationName() {
		return null;
	}

	@Override
	public List<String> getQualifierAnnotationNames() {
		return null;
	}

	@Override
	public String getPersistenceUnitName() {
		logger.log("Called PersistenceUnitInfoImpl.getPersistenceUnitName()");
		logger.log("PersistenceUnitName:" + puName);
		return puName;
	}

	@Override
	public URL getPersistenceUnitRootUrl() {
		logger.log("Called PersistenceUnitInfoImpl.getPersistenceUnitRootUrl()");
		return persistenceUnitRootUrl;
	}

	@Override
	public String getPersistenceXMLSchemaVersion() {
		logger.log("Called PersistenceUnitInfoImpl.getPersistenceXMLSchemaVersion()");
		return persistenceXMLSchemaVersion;
	}

	@Override
	public Properties getProperties() {
		logger.log("Called PersistenceUnitInfoImpl.getProperties()");
		for (Iterator iter = properties.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String val = (String) properties.get(key);
			logger.log("Property:[" + key + "," + val + "]");
		}

		return properties;
	}

	@Override
	public SharedCacheMode getSharedCacheMode() {
		logger.log("Called PersistenceUnitInfoImpl.getSharedCacheMode()");
		return SharedCacheMode.NONE;
	}

	@Override
	public PersistenceUnitTransactionType getTransactionType() {
		logger.log("Called PersistenceUnitInfoImpl.getTransactionType()");
		logger.log("TransactionType:" + transactionType);
		return transactionType;
	}

	@Override
	public ValidationMode getValidationMode() {
		logger.log("Called PersistenceUnitInfoImpl.getValidationMode()");
		return ValidationMode.NONE;
	}

	/**
	 * returns a shallow clone
	 */
	public PersistenceUnitInfoImpl clone() {
		PersistenceUnitInfoImpl puii = new PersistenceUnitInfoImpl();
		puii.managedClassNames = this.managedClassNames;
		puii.classTransformer = this.classTransformer;
		puii.puName = this.puName;
		puii.persistenceProviderClassName = this.persistenceProviderClassName;
		puii.properties = this.properties;
		puii.transactionType = this.transactionType;
		puii.classLoader = this.classLoader;
		puii.excludeUnlistedClasses = this.excludeUnlistedClasses;
		puii.jarFileUrls = this.jarFileUrls;
		puii.mappingFileNames = this.mappingFileNames;
		puii.nonJtaDataSource = this.nonJtaDataSource;
		puii.persistenceUnitRootUrl = this.persistenceUnitRootUrl;
		puii.persistenceXMLSchemaVersion = this.persistenceXMLSchemaVersion;

		return puii;
	}

}
