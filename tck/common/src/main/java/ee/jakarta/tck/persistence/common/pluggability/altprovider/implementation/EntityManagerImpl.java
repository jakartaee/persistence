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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.ConnectionConsumer;
import jakarta.persistence.ConnectionFunction;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FindOption;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.LockOption;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RefreshOption;
import jakarta.persistence.Statement;
import jakarta.persistence.StatementOrTypedQuery;
import jakarta.persistence.StatementReference;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaSelect;
import jakarta.persistence.criteria.CriteriaStatement;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.sql.ResultSetMapping;
import jakarta.annotation.Nonnull;

import static java.util.Collections.*;

@SuppressWarnings("removal")
public class EntityManagerImpl implements jakarta.persistence.EntityManager {
	/**
	 * Store if this entity manager has been closed for test
	 * closeAfterCloseCausesISException
	 */
	protected boolean isOpen;

	// state required for public methods
	public EntityManagerFactoryImpl emf;

	public Map properties;

	protected TSLogger logger;

	public EntityManagerImpl() {
		logger = TSLogger.getInstance();
		isOpen = true;
		logger.log("Called EntityManagerImpl()");

	}

	@Override
	public void clear() {
		logger.log("Called EntityManagerImpl.clear()");

	}

	@Override
	public void close() {
		logger.log("Called EntityManagerImpl.close()");
		verifyOpen();
		isOpen = false;
	}

	@Override
	public boolean contains(@Nonnull Object arg0) {
		return false;
	}

	@Override
	@Nonnull
	public StatementOrTypedQuery createNamedQuery(@Nonnull String arg0) {
		var query = new QueryImpl<>();
		query.name = arg0;
		return query;
	}

	@Override
	@Nonnull
	public <T> TypedQuery<T> createNamedQuery(@Nonnull String arg0, @Nonnull Class<T> arg1) {
		var query = new QueryImpl<T>();
		query.name = arg0;
		query.queryOnClass = arg1;
		return query;
	}

	@Override
	@Nonnull
	public <T> TypedQuery<T> createQuery(@Nonnull TypedQueryReference<T> reference) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public StatementOrTypedQuery createNativeQuery(@Nonnull String arg0) {
		var query = new QueryImpl<>();
		query.nativeSQL = arg0;
		return query;
	}

	@Override
	@Nonnull
	public Statement createStatement(@Nonnull StatementReference reference) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Statement createNamedStatement(@Nonnull String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Statement createNativeStatement(@Nonnull String sqlString) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Statement createStatement(@Nonnull String qlString) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
    public <T> TypedQuery<T> createNativeQuery(@Nonnull String arg0, @Nonnull Class<T> arg1) {
		var query = new QueryImpl<T>();
		query.nativeSQL = arg0;
		query.queryOnClass = arg1;
		return query;
	}

	@Override
	@Nonnull
	public StatementOrTypedQuery createNativeQuery(@Nonnull String arg0, @Nonnull String arg1) {
		var query = new QueryImpl<>();
		query.nativeSQL = arg0;
		query.resultsetMapping = arg1;
		return query;
	}

    @Override
	@Nonnull
    public <T> TypedQuery<T> createNativeQuery(@Nonnull String sqlString, @Nonnull ResultSetMapping<T> resultSetMapping) {
        QueryImpl<T> query = new QueryImpl<>();
        query.nativeSQL = sqlString;
        query.resultsetMapping = Objects.toString(resultSetMapping);
        return query;
    }

    @Override
	@Nonnull
	public StatementOrTypedQuery createQuery(@Nonnull String arg0) {
		var query = new QueryImpl<>();
		query.jpQL = arg0;
		return query;
	}

	@Override
	@Nonnull
	public <T> TypedQuery<T> createQuery(@Nonnull CriteriaSelect<T> selectQuery) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <T> TypedQuery<T> createQuery(@Nonnull String arg0, @Nonnull Class<T> arg1) {
		var query = new QueryImpl<T>();
		query.jpQL = arg0;
		query.queryOnClass = arg1;
		return query;
	}

    @Override
	@Nonnull
    public <T> TypedQuery<T> createQuery(@Nonnull String qlString, @Nonnull EntityGraph<T> resultGraph) {
        QueryImpl<T> query = new QueryImpl<>();
        query.jpQL = qlString;
        return query;
    }

	@Override
	@Nonnull
	public Statement createStatement(@Nonnull CriteriaStatement<?> statement) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Statement createQuery(@Nonnull CriteriaStatement<?> statement) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public StoredProcedureQuery createNamedStoredProcedureQuery(@Nonnull String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public StoredProcedureQuery createStoredProcedureQuery(@Nonnull String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public StoredProcedureQuery createStoredProcedureQuery(@Nonnull String name, @Nonnull java.lang.Class<?>[] resultClasses) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public StoredProcedureQuery createStoredProcedureQuery(@Nonnull String name, @Nonnull String[] resultSetMappings) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach(@Nonnull Object arg0) {
	}

    @Override
	@Nonnull
    public <T> T get(@Nonnull Class<T> entityClass, @Nonnull Object id) {
		throw new UnsupportedOperationException();
    }

    @Override
	@Nonnull
    public <T> T get(@Nonnull Class<T> entityClass, @Nonnull Object id, FindOption... options) {
		throw new UnsupportedOperationException();
    }

    @Override
	@Nonnull
    public <T> T get(@Nonnull EntityGraph<T> graph, @Nonnull Object id, FindOption... options) {
		throw new UnsupportedOperationException();
    }

    @Override
	@Nonnull
    public <T> List<T> getMultiple(@Nonnull Class<T> entityClass, @Nonnull List<?> ids, FindOption... options) {
        return List.of();
    }

    @Override
	@Nonnull
    public <T> List<T> getMultiple(@Nonnull EntityGraph<T> graph, @Nonnull List<?> ids, FindOption... options) {
        return List.of();
    }

    @Override
	public <T> T find(@Nonnull Class<T> arg0, @Nonnull Object arg1) {
		return null;
	}

	@Override
	public <T> T find(@Nonnull Class<T> arg0, @Nonnull Object arg1, Map<String, Object> arg2) {
		return null;
	}

	@Override
	public <T> T find(@Nonnull Class<T> arg0, @Nonnull Object arg1, @Nonnull LockModeType arg2, Map<String, Object> arg3) {
		return null;
	}

	@Override
	public <T> T find(@Nonnull Class<T> entityClass, @Nonnull Object primaryKey, FindOption... options) {
		return null;
	}

	@Override
	public <T> T find(@Nonnull EntityGraph<T> entityGraph, @Nonnull Object primaryKey, FindOption... options) {
		return null;
	}

    @Override
	@Nonnull
    public <T> List<T> findMultiple(@Nonnull Class<T> entityClass, @Nonnull List<?> ids, FindOption... options) {
        return List.of();
    }

    @Override
	@Nonnull
    public <T> List<T> findMultiple(@Nonnull EntityGraph<T> graph, @Nonnull List<?> ids, FindOption... options) {
        return List.of();
    }

    @Override
	public void flush() {

	}

	@Override
	@Nonnull
	public CriteriaBuilder getCriteriaBuilder() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Object getDelegate() {
		verifyOpen();
		return this;
	}

	@Override
	@Nonnull
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	@Override
	@Nonnull
	public FlushModeType getFlushMode() {
		return FlushModeType.AUTO;
	}

	@Override
	@Nonnull
	public LockModeType getLockMode(@Nonnull Object arg0) {
		return LockModeType.NONE;
	}

	@Override
	public void setCacheRetrieveMode(@Nonnull CacheRetrieveMode cacheRetrieveMode) {
	}

	@Override
	public void setCacheStoreMode(@Nonnull CacheStoreMode cacheStoreMode) {
	}

	@Override
	@Nonnull
	public CacheRetrieveMode getCacheRetrieveMode() {
		return CacheRetrieveMode.USE;
	}

	@Override
	@Nonnull
	public CacheStoreMode getCacheStoreMode() {
		return CacheStoreMode.USE;
	}

	@Override
	@Nonnull
	public Metamodel getMetamodel() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Map<String, Object> getProperties() {
		return emptyMap();
	}

	@Override
	@Nonnull
	public <T> T getReference(@Nonnull Class<T> arg0, @Nonnull Object arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <T> T getReference(@Nonnull T entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public EntityTransaction getTransaction() {
		logger.log("Called EntityManagerImpl.getTransaction()");
		return new EntityTransactionImpl();
	}

	@Override
	public boolean isOpen() {
		logger.log("Called EntityManagerImpl.isOpen()");
		return isOpen;
	}

	@Override
	public boolean isJoinedToTransaction() {
		return false;
	}

	@Override
	public void joinTransaction() {

	}

	@Override
	public void lock(@Nonnull Object arg0, @Nonnull LockModeType arg1) {

	}

	@Override
	public void lock(@Nonnull Object arg0, @Nonnull LockModeType arg1, Map<String, Object> arg2) {

	}

	@Override
	public void lock(@Nonnull Object entity, @Nonnull LockModeType lockMode, LockOption... options) {

	}

	@Override
	@Nonnull
	public <T> T merge(@Nonnull T arg0) {
		return arg0;// not cloning it in case the object can't be cloned
	}

	@Override
	public void persist(@Nonnull Object arg0) {

	}

	@Override
	public void refresh(@Nonnull Object arg0) {

	}

	@Override
	public void refresh(@Nonnull Object arg0, Map<String, Object> arg1) {

	}

	@Override
	public void refresh(@Nonnull Object arg0, @Nonnull LockModeType arg1, Map<String, Object> arg2) {

	}

	@Override
	public void refresh(@Nonnull Object entity, RefreshOption... options) {

	}

	@Override
	public void remove(@Nonnull Object arg0) {

	}

	@Override
	public void setFlushMode(@Nonnull FlushModeType arg0) {

	}

	@Override
	public void setProperty(@Nonnull String arg0, Object arg1) {

	}

	@Override
	public void addOption(@Nonnull Option option) {

	}

	@Override
	@Nonnull
	public Set<Option> getOptions() {
		return emptySet();
	}

	@Override
	@Nonnull
	public <T> T unwrap(@Nonnull Class<T> arg0) {
		if (EntityManagerImpl.class == arg0) {
			return (T) this;
		}
		throw new PersistenceException();
	}

	@Nonnull
	public <T> List<EntityGraph<? super T>> getEntityGraphs(@Nonnull Class<T> entityClass) {
		return emptyList();
	}

	@Override
	public <C> void runWithConnection(@Nonnull ConnectionConsumer<C> action) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <C, T> T callWithConnection(@Nonnull ConnectionFunction<C, T> function) {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	public EntityGraph<?> getEntityGraph(@Nonnull String graphName) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <T> EntityGraph<T> getEntityGraph(@Nonnull Class<T> rootType, @Nonnull String graphName) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <T> EntityGraph<T> createEntityGraph(@Nonnull Class<T> rootType) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public EntityGraph<?> createEntityGraph(@Nonnull String graphName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * verify if this entity manager has been closed for test
	 * closeAfterCloseCausesISException
	 */
	public void verifyOpen() {
		if (!isOpen) {
			throw new IllegalStateException("Attempting to execute an operation on a closed EntityManager.");
		}
	}

}
