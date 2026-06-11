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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.QueryFlushMode;
import jakarta.persistence.StatementOrTypedQuery;
import jakarta.persistence.Statement;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Timeout;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.Type;
import jakarta.persistence.sql.ResultSetMapping;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

@SuppressWarnings({"removal", "deprecation"})
public class QueryImpl<X> implements TypedQuery<X>, StatementOrTypedQuery {
	public String jpQL;

	public String name;

	public String nativeSQL;

	public Class<X> queryOnClass;

	public String resultsetMapping;

	protected TSLogger logger;

	public QueryImpl() {
		logger = TSLogger.getInstance();
	}

	@Override
	@Nonnull
	public Statement asStatement() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <R> TypedQuery<R> ofType(@Nonnull Class<R> resultType) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <R> TypedQuery<R> withEntityGraph(@Nonnull EntityGraph<R> graph) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <R> TypedQuery<R> withResultSetMapping(@Nonnull ResultSetMapping<R> mapping) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate() {
		return 0;
	}

	@Override
	public int getFirstResult() {
		return 0;
	}

	@Override
	@Nonnull
	public FlushModeType getFlushMode() {
		return FlushModeType.AUTO;
	}

	@Override
	@Nonnull
	public Map<String, Object> getHints() {
		return emptyMap();
	}

	@Override
	@Nullable
	public LockModeType getLockMode() {
		return null;
	}

	@Override
	@Nullable
	public PessimisticLockScope getLockScope() {
		return null;
	}

	@Override
	public int getMaxResults() {
		return Integer.MAX_VALUE;
	}

	@Override
	@Nonnull
	public Parameter<?> getParameter(@Nonnull String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Parameter<?> getParameter(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <T> Parameter<T> getParameter(@Nonnull String arg0, @Nonnull Class<T> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <T> Parameter<T> getParameter(int arg0, @Nonnull Class<T> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getParameterValue(@Nonnull Parameter<T> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getParameterValue(@Nonnull String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getParameterValue(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public Set<Parameter<?>> getParameters() {
		return emptySet();
	}

	@Override
	@Nonnull
	public List<X> getResultList() {
		throw new UnsupportedOperationException();
	}

    @Override
    public long getResultCount() {
        return 0;
    }

    @Override
	public X getSingleResult() {
		throw new UnsupportedOperationException();
	}

	@Override
	public X getSingleResultOrNull() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isBound(@Nonnull Parameter<?> arg0) {
		return false;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setFirstResult(int arg0) {
		return this;
	}

    @Override
	@Nonnull
	public TypedQuery<X> setFlushMode(@Nonnull FlushModeType arg0) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setQueryFlushMode(@Nonnull QueryFlushMode flushMode) {
		return this;
	}

	@Override
	@Nonnull
	public QueryFlushMode getQueryFlushMode() {
		return QueryFlushMode.DEFAULT;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setHint(@Nonnull String arg0, Object arg1) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setLockMode(@Nonnull LockModeType arg0) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setLockScope(@Nonnull PessimisticLockScope lockScope) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setCacheRetrieveMode(@Nonnull CacheRetrieveMode cacheRetrieveMode) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setCacheStoreMode(@Nonnull CacheStoreMode cacheStoreMode) {
		return this;
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
	public TypedQuery<X> setTimeout(Integer timeout) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setTimeout(Timeout timeout) {
		return this;
	}

	@Override
	public Integer getTimeout() {
		return null;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setMaxResults(int arg0) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(@Nonnull String arg0, Object arg1) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(int arg0, Object arg1) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(@Nonnull String arg0, Calendar arg1, @Nonnull TemporalType arg2) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(@Nonnull String arg0, Date arg1, @Nonnull TemporalType arg2) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(int arg0, Calendar arg1, @Nonnull TemporalType arg2) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(int arg0, Date arg1, @Nonnull TemporalType arg2) {
		return this;
	}

	@Override
	@Nonnull
	public <P> TypedQuery<X> setParameter(@Nonnull String name, P value, @Nonnull Type<P> type) {
		return this;
	}

	@Override
	@Nonnull
	public <P> TypedQuery<X> setParameter(int position, P value, @Nonnull Type<P> type) {
		return this;
	}

	@Override
	@Nonnull
	public <P> TypedQuery<X> setParameter(@Nonnull String name, P value, @Nonnull Class<P> type) {
		return this;
	}

	@Override
	@Nonnull
	public <P> TypedQuery<X> setParameter(int position, P value, @Nonnull Class<P> type) {
		return this;
	}

	@Override
	@Nonnull
	public <P> TypedQuery<X> setConvertedParameter(@Nonnull String name, P value,
                                                   @Nonnull Class<? extends AttributeConverter<P, ?>> converter) {
		return this;
	}

	@Override
	@Nonnull
	public <P> TypedQuery<X> setConvertedParameter(int position, P value,
                                                   @Nonnull Class<? extends AttributeConverter<P, ?>> converter) {
		return this;
	}

	@Override
	@Nonnull
	public <T> T unwrap(@Nonnull Class<T> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public <T> TypedQuery<X> setParameter(@Nonnull Parameter<T> arg0, T arg1) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(@Nonnull Parameter<Calendar> arg0, Calendar arg1, @Nonnull TemporalType arg2) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> setParameter(@Nonnull Parameter<Date> arg0, Date arg1, @Nonnull TemporalType arg2) {
		return this;
	}

	@Override
	@Nonnull
	public TypedQuery<X> addOption(@Nonnull Option option) {
		return this;
	}

	@Override
	@Nonnull
	public Set<Option> getOptions() {
		return emptySet();
	}

	@Override
	public @Nonnull TypedQuery<X> setParameters(@Nonnull Object... arguments) {
		return this;
	}
}
