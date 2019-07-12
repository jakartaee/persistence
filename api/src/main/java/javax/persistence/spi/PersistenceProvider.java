/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Linda DeMichiel - Java Persistence 2.1
//     Linda DeMichiel - Java Persistence 2.0

package javax.persistence.spi;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import java.util.Map;

/**
 * Interface implemented by the persistence provider.
 *
 * <p> It is invoked by the container in Java EE environments and
 * by the {@link javax.persistence.Persistence} class in Java SE environments to
 * create an {@link javax.persistence.EntityManagerFactory} and/or to cause
 * schema generation to occur.
 *
 * @since Java Persistence 1.0
 */
public interface PersistenceProvider {

    /**
     * Called by <code>Persistence</code> class when an
     * <code>EntityManagerFactory</code> is to be created.
     *
     * @param emName  the name of the persistence unit
     * @param map  a Map of properties for use by the 
     * persistence provider. These properties may be used to
     * override the values of the corresponding elements in 
     * the <code>persistence.xml</code> file or specify values for 
     * properties not specified in the <code>persistence.xml</code>
     * (and may be null if no properties are specified).
     * @return EntityManagerFactory for the persistence unit, 
     * or null if the provider is not the right provider 
     */
    public EntityManagerFactory createEntityManagerFactory(String emName, Map map);

    /**
     * Called by the container when an <code>EntityManagerFactory</code>
     * is to be created. 
     *
     * @param info  metadata for use by the persistence provider
     * @param map  a Map of integration-level properties for use 
     * by the persistence provider (may be null if no properties
     * are specified).  These properties may include properties to
     * control schema generation.
     * If a Bean Validation provider is present in the classpath,
     * the container must pass the <code>ValidatorFactory</code> instance in
     * the map with the key <code>"javax.persistence.validation.factory"</code>.
     * If the containing archive is a bean archive, the container
     * must pass the BeanManager instance in the map with the key
     * <code>"javax.persistence.bean.manager"</code>.
     * @return EntityManagerFactory for the persistence unit 
     * specified by the metadata
     */
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map map);


    /**
     * Create database schemas and/or tables and/or create DDL
     * scripts as determined by the supplied properties.
     * <p>
     * Called by the container when schema generation is to
     * occur as a separate phase from creation of the entity
     * manager factory.
     * <p>
     * @param info metadata for use by the persistence provider
     * @param map properties for schema generation;  these may
     *             also include provider-specific properties
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided of if schema
     *         generation otherwise fails
     *
     * @since Java Persistence 2.1
     */
    public void generateSchema(PersistenceUnitInfo info, Map map);

    /**
     * Create database schemas and/or tables and/or create DDL
     * scripts as determined by the supplied properties.
     * <p>
     * Called by the Persistence class when schema generation is to
     * occur as a separate phase from creation of the entity
     * manager factory.
     * <p>
     * @param persistenceUnitName the name of the persistence unit
     * @param map properties for schema generation;  these may
     *             also contain provider-specific properties.  The
     *             value of these properties override any values that
     *             may have been configured elsewhere.
     * @return true  if schema was generated, otherwise false
     * @throws PersistenceException if insufficient or inconsistent
     *         configuration information is provided or if schema
     *         generation otherwise fails
     *
     * @since Java Persistence 2.1
     */
    public boolean generateSchema(String persistenceUnitName, Map map); 

    /**
     * Return the utility interface implemented by the persistence
     * provider.
     * @return ProviderUtil interface
     *
     * @since Java Persistence 2.0
     */
    public ProviderUtil getProviderUtil();
}

