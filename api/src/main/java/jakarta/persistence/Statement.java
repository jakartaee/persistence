/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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

import jakarta.persistence.metamodel.Type;

import java.util.Calendar;
import java.util.Date;

/**
 * Interface used to control the execution of executable statements.
 * In the Jakarta Persistence query language only {@code UPDATE} and
 * {@code DELETE} statements are executable statements. On the other
 * hand, a native SQL statement is considered executable if it returns
 * a row count instead of a result set.
 * @since 4.0
 */
public interface Statement extends Query {
    /**
     * Execute an {@code UPDATE} or {@code DELETE} statement or a
     * native SQL statement that returns a row count.
     * <p>
     * After execution of a bulk update or delete operation, the
     * persistence provider is not required to resynchronize state
     * held in memory with the effects of the operation on data
     * held in the database. However, when this method is called
     * within a transaction, the persistence context is joined to
     * the transaction, and {@link FlushModeType#AUTO} is in effect,
     * the persistence provider must ensure that every modification
     * to the state of every entity associated with the persistence
     * context which could possibly alter the effects of the bulk
     * update or delete operation is visible to the processing of
     * the operation.
     *
     * @return the number of entities updated or deleted, or the
     *         row count of the native SQL statement
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
     * @throws PersistenceException if the flush fails
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected during the flush
     */
    int execute();

    /**
     * Set a query property or hint. The hints elements may be used
     * to specify query properties and hints. Properties defined by
     * this specification must be observed by the provider.
     * Vendor-specific hints that are not recognized by a provider
     * must be silently ignored. Portable applications should not
     * rely on the standard timeout hint. Depending on the database
     * in use and the locking mechanisms used by the provider,
     * this hint may or may not be observed.
     *
     * @param hintName The name of the property or hint
     * @param value The value for the property or hint
     * @return the same query instance
     * @throws IllegalArgumentException if the second argument is not
     *         valid for the implementation
     */
    @Override
    Statement setHint(String hintName, Object value);

    /**
     * Set the query timeout, in milliseconds. This is a hint,
     * and is an alternative to {@linkplain #setHint setting
     * the hint} {@code jakarta.persistence.query.timeout}.
     *
     * @param timeout The timeout, in milliseconds, or null to
     *                indicate no timeout
     * @return the same query instance
     * @since 3.2
     */
    @Override
    Statement setTimeout(Integer timeout);

    /**
     * Set the query timeout. This is a hint.
     *
     * @param timeout the timeout, or null to indicate no timeout
     * @return the same query instance
     * @since 4.0
     */
    @Override
    Statement setTimeout(Timeout timeout);

    /**
     * Set the {@linkplain FlushModeType flush mode type} to be
     * used when the query is executed. This flush mode overrides
     * the {@linkplain EntityManager#getFlushMode flush mode type
     * of the entity manager}.
     *
     * @param flushMode The new flush mode
     * @return the same query instance
     */
    @Override
    Statement setFlushMode(FlushModeType flushMode);

    /**
     * Bind an argument to a parameter of this query respresented as
     * a {@link Parameter} object.
     *
     * @param parameter The parameter object
     * @param value The argument to the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter
     *         does not correspond to a parameter of the
     *         query
     * @since 2.0
     */
    @Override
    <T> Statement setParameter(Parameter<T> parameter, T value);

    /**
     * Bind an instance of {@link Calendar} to a {@link Parameter} object.
     *
     * @param param The parameter object
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @since 2.0
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Statement setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a {@link Parameter} object.
     * 
     * @param param The parameter object
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @since 2.0
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Statement setParameter(Parameter<Date> param, Date value, TemporalType temporalType);

    /**
     * Bind an argument value to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     */
    @Override
    Statement setParameter(String name, Object value);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be {@code null}, especially in the case
     * of a native query.
     * {@snippet :
     * em.createNativeStatement("update books set pub_date = :date where isbn = :ISBN")
     *     .setParameter("date", optionalPublicationDate, LocalDate.class)
     *     .setParameter("ISBN", isbn)
     *     .execute();
     * }
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param type A class object representing the parameter type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> Statement setParameter(String name, P value, Class<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the binding is affected by an attribute converter.
     * {@snippet :
     * em.createNativeStatement("update books set pub_date = :date where isbn = :ISBN")
     *     .setParameter("date", optionalPublicationDate, Book_.publicationDate.getType())
     *     .setParameter("ISBN", isbn, Book_.isbn.getType())
     *     .execute();
     * }
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param type The {@link Type} of the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> Statement setParameter(String name, P value, Type<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying an {@linkplain AttributeConverter attribute
     * converter} to use.
     * {@snippet :
     * em.createNativeStatement("update books set pub_date = :date where isbn = :ISBN")
     *     .setParameter("date", publicationDate)
     *     .setParameter("ISBN", isbn, IsbnConverter.class)
     *     .execute();
     * }
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param converter The class of the attribute converter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> Statement setConvertedParameter(String name, P value,
                                        Class<? extends AttributeConverter<P,?>> converter);

    /**
     * Bind an instance of {@link Calendar} to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the value argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Statement setParameter(String name, Calendar value, TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the value argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Statement setParameter(String name, Date value, TemporalType temporalType);

    /**
     * Bind an argument value to a positional parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     */
    @Override
    Statement setParameter(int position, Object value);

    /**
     * Bind an argument value to a positional parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be {@code null}, especially in the case
     * of a native SQL query.
     * {@snippet :
     * em.createNativeStatement("update books set pub_date = ?1 where isbn = ?2")
     *     .setParameter(1, optionalPublicationDate, LocalDate.class)
     *     .setParameter(2, isbn)
     *     .execute();
     * }
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param type A class object representing the parameter type
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> Statement setParameter(int position, P value, Class<P> type);

    /**
     * Bind an argument value to a positional parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the binding is affected by an attribute converter.
     * {@snippet :
     * em.createNativeStatement("update books set pub_date = ?1 where isbn = ?2")
     *     .setParameter(1, optionalPublicationDate, Book_.publicationDate.getType())
     *     .setParameter(2, isbn, Book_.isbn.getType())
     *     .execute();
     * }
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param type The {@link Type} of the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> Statement setParameter(int position, P value, Type<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying an {@linkplain AttributeConverter attribute
     * converter} to use.
     * {@snippet :
     * em.createNativeStatement("update books set pub_date = ?1 where isbn = ?2")
     *     .setParameter(1, publicationDate)
     *     .setParameter(2, isbn, IsbnConverter.class)
     *     .execute();
     * }
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param converter The class of the attribute converter
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> Statement setConvertedParameter(int position, P value,
                                        Class<? extends AttributeConverter<P,?>> converter);

    /**
     * Bind an instance of {@link Calendar} to a positional
     * parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Statement setParameter(int position, Calendar value, TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a positional
     * parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Statement setParameter(int position, Date value, TemporalType temporalType);
}
