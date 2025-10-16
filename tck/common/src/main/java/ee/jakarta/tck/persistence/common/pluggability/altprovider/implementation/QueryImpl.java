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

import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;

public class QueryImpl<X> implements TypedQuery<X> {
	public String jpQL;

	public String name;

	public String nativeSQL;

	public Class queryOnClass;

	public String resultsetMapping;

	protected TSLogger logger;

	public QueryImpl() {
		logger = TSLogger.getInstance();
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
	public FlushModeType getFlushMode() {
		return null;
	}

	@Override
	public Map<String, Object> getHints() {
		return null;
	}

	@Override
	public LockModeType getLockMode() {
		return null;
	}

	@Override
	public int getMaxResults() {
		return 0;
	}

	@Override
	public Parameter<?> getParameter(String arg0) {
		return null;
	}

	@Override
	public Parameter<?> getParameter(int arg0) {
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(String arg0, Class<T> arg1) {
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(int arg0, Class<T> arg1) {
		return null;
	}

	@Override
	public <T> T getParameterValue(Parameter<T> arg0) {
		return null;
	}

	@Override
	public Object getParameterValue(String arg0) {
		return null;
	}

	@Override
	public Object getParameterValue(int arg0) {
		return null;
	}

	@Override
	public Set<Parameter<?>> getParameters() {
		return null;
	}

	@Override
	public List getResultList() {
		return null;
	}

	@Override
	public X getSingleResult() {
		return null;
	}

	@Override
	public X getSingleResultOrNull() {
		return null;
	}

	@Override
	public boolean isBound(Parameter<?> arg0) {
		return false;
	}

	@Override
	public TypedQuery setFirstResult(int arg0) {
		return this;
	}

	@Override
	public TypedQuery setFlushMode(FlushModeType arg0) {
		return this;
	}

	@Override
	public TypedQuery setHint(String arg0, Object arg1) {
		return this;
	}

	@Override
	public TypedQuery setLockMode(LockModeType arg0) {
		return this;
	}

	@Override
	public TypedQuery<X> setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode) {
		return null;
	}

	@Override
	public TypedQuery<X> setCacheStoreMode(CacheStoreMode cacheStoreMode) {
		return null;
	}

	@Override
	public CacheRetrieveMode getCacheRetrieveMode() {
		return null;
	}

	@Override
	public CacheStoreMode getCacheStoreMode() {
		return null;
	}

	@Override
	public TypedQuery<X> setTimeout(Integer timeout) {
		return null;
	}

	@Override
	public Integer getTimeout() {
		return null;
	}

	@Override
	public TypedQuery setMaxResults(int arg0) {
		return this;
	}

	@Override
	public TypedQuery setParameter(String arg0, Object arg1) {
		return this;
	}

	@Override
	public TypedQuery setParameter(int arg0, Object arg1) {
		return this;
	}

	@Override
	public TypedQuery setParameter(String arg0, Calendar arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public TypedQuery setParameter(String arg0, Date arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public TypedQuery setParameter(int arg0, Calendar arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public TypedQuery setParameter(int arg0, Date arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) {
		return null;
	}

	@Override
	public <T> TypedQuery setParameter(Parameter<T> arg0, T arg1) {
		return this;
	}

	@Override
	public TypedQuery setParameter(Parameter<Calendar> arg0, Calendar arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public TypedQuery setParameter(Parameter arg0, Date arg1, TemporalType arg2) {
		return this;
	}

}
