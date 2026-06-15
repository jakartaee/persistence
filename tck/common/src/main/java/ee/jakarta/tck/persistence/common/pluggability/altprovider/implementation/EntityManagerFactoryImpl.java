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

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityHandler;
import jakarta.persistence.EntityListenerRegistration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PersistenceUnitTransactionType;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.Query;
import jakarta.persistence.SchemaManager;
import jakarta.persistence.SchemaValidationException;
import jakarta.persistence.Statement;
import jakarta.persistence.StatementReference;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.sql.ResultSetMapping;

import static java.util.Collections.emptyMap;

public class EntityManagerFactoryImpl implements jakarta.persistence.EntityManagerFactory {

	public static final SchemaManager NOOP_SCHEMA_MANAGER = new SchemaManager() {
		@Override
		public void create(boolean createSchemas) {
		}

		@Override
		public void drop(boolean dropSchemas) {
		}

		@Override
		public void validate() throws SchemaValidationException {
		}

		@Override
		public void truncate() {
		}

		@Override
		public void populate() {
		}
	};
	public Map properties;

	public boolean isOpen;

	public PersistenceUnitInfo puInfo;

	public boolean containerFactory = false;

	public ClassTransformerImpl transformer;

	public ClassLoader newTempClassloader;

	protected TSLogger logger;

	public EntityManagerFactoryImpl() {
		isOpen = true;
		logger = TSLogger.getInstance();
		logger.log("Called EntityManagerFactoryImpl()");
	}

	public EntityManagerFactoryImpl(boolean containerFactory) {
		super();
		logger = TSLogger.getInstance();
		logger.log("Called EntityManagerFactoryImpl(boolean)");

		isOpen = true;
		this.containerFactory = containerFactory;
	}

	public void addNamedQuery(String s, Query q) {
	}

    @Override
	@Nonnull
	public <R> TypedQueryReference<R> addNamedQuery(@Nonnull String name, @Nonnull TypedQuery<R> query) {
        return null;
    }

	@Override
	@Nonnull
	public StatementReference addNamedStatement(@Nonnull String name, @Nonnull Statement statement) {
		return null;
	}

	public void close() {
		verifyOpen();
		isOpen = false;
	}

	@Override
	@Nonnull
	public String getName() {
		return "";
	}

	@Nonnull
	public EntityManager createEntityManager(EntityManager.CreationOption... options) {
		logger.log("Called EntityManagerFactoryImpl.createEntityManager()");
		verifyOpen();
		EntityManagerImpl em = new EntityManagerImpl();
		em.emf = this;

		return em;
	}

	@Nonnull
	public EntityManager createEntityManager(Map<?,?> properties) {
		logger.log("Called EntityManagerFactoryImpl.createEntityManager(Map)");
		verifyOpen();
		EntityManagerImpl em = new EntityManagerImpl();
		em.emf = this;
		em.properties = new HashMap<>(properties);
		return em;
	}

    @Override
	@Nonnull
	public EntityAgent createEntityAgent(EntityAgent.CreationOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
	@Nonnull
	public EntityAgent createEntityAgent(Map<?, ?> map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Nonnull
	public EntityManager createEntityManager(@Nonnull SynchronizationType st, Map<?,?> map) {
		logger.log("Called EntityManagerFactoryImpl.createEntityManager(Map)");
		verifyOpen();
		EntityManagerImpl em = new EntityManagerImpl();
		em.emf = this;
		em.properties = new HashMap<>(map);
		return em;
	}

	public Cache getCache() {
		return new CacheImpl();
	}

	@Nonnull
	public CriteriaBuilder getCriteriaBuilder() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	public Metamodel getMetamodel() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public PersistenceUnitTransactionType getTransactionType() {
		return PersistenceUnitTransactionType.RESOURCE_LOCAL;
	}

	@Override
	@Nonnull
	public SchemaManager getSchemaManager() {
		return NOOP_SCHEMA_MANAGER;
	}

	@Nonnull
	public Map<String, Object> getProperties() {
		return emptyMap();
	}

	@Nonnull
	public <T> T unwrap(@Nonnull Class<T> cls) {
		if (EntityManagerImpl.class == cls) {
			return (T) this;
		}
		throw new PersistenceException();
	}

	public <T> void addNamedEntityGraph(@Nonnull String graphName, @Nonnull EntityGraph<T> entityGraph) {

	}

	@Override
	@Nonnull
	public Map<String, StatementReference> getNamedStatements() {
		return emptyMap();
	}

	@Override
	@Nonnull
	public <R> Map<String, TypedQueryReference<R>> getNamedQueries(@Nonnull Class<R> resultType) {
		return emptyMap();
	}

	@Override
	@Nonnull
	public <E> Map<String, EntityGraph<? extends E>> getNamedEntityGraphs(@Nonnull Class<E> entityType) {
		return emptyMap();
	}

    @Override
	@Nonnull
	public <R> Map<String, ResultSetMapping<R>> getResultSetMappings(@Nonnull Class<R> resultType) {
        return emptyMap();
    }

    @Override
	@Nonnull
	public <E> EntityListenerRegistration addListener(@Nonnull Class<E> entityType,
													  @Nonnull Class<? extends Annotation> callbackType,
													  @Nonnull Consumer<? super E> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
	public void runInTransaction(@Nonnull Consumer<EntityManager> work) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> R callInTransaction(@Nonnull Function<EntityManager, R> work) {
		throw new UnsupportedOperationException();
	}

    @Override
    public <H extends EntityHandler> void runInTransaction(@Nonnull Class<H> handlerClass, @Nonnull Consumer<H> work) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R, H extends EntityHandler> R callInTransaction(@Nonnull Class<H> handlerClass, @Nonnull Function<H, R> work) {
        throw new UnsupportedOperationException();
    }

	public boolean isOpen() {
		return isOpen;
	}

	// added to check isOpen/closed, etc.
	protected void verifyOpen() {
		if (!this.isOpen) {
			throw new IllegalStateException("operation_on_closed_entity_manager_factory");
		}
	}
}
