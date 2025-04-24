/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 4.0

package jakarta.persistence;

/**
 * Interface used to control execution of an UPDATE or DELETE
 * query.
 */
public interface ExecutableQuery extends Query {
    /**
     * Execute an update or delete statement.
     * @return the number of entities updated or deleted
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language SELECT statement or for
     *         a criteria query
     * @throws TransactionRequiredException if there is
     *         no transaction or the persistence context has not
     *         been joined to the transaction
     * @throws QueryTimeoutException if the statement execution
     *         exceeds the query timeout value set and only
     *         the statement is rolled back
     * @throws PersistenceException if the query execution exceeds
     *         the query timeout value set and the transaction
     *         is rolled back
     */
    @Override
    int executeUpdate();

    @Override
    <T> ExecutableQuery setParameter(Parameter<T> param, T value);

    @Override
    ExecutableQuery setParameter(String name, Object value);

    @Override
    ExecutableQuery setParameter(int position, Object value);

    @Override
    ExecutableQuery setFlushMode(FlushModeType flushMode);

    @Override
    ExecutableQuery setHint(String hintName, Object value);

    @Override
    ExecutableQuery setTimeout(Integer timeout);

    /**
     * @throws UnsupportedOperationException because this method
     * should not be called on an {@code ExecutableQuery}
     */
    @Override @Deprecated
    default ExecutableQuery forExecution() {
        throw new UnsupportedOperationException();
    }
    /**
     * @throws UnsupportedOperationException because this method
     * should not be called on an {@code ExecutableQuery}
     */
    @Override @Deprecated
    default <R> TypedQuery<R> forType(Class<R> resultType) {
        throw new UnsupportedOperationException();
    }
}
