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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.metamodel.Type;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Interface used to control the execution of executable statements.
 * In the Jakarta Persistence Query Language only {@code UPDATE} and
 * {@code DELETE} statements are executable statements. On the other
 * hand, a native SQL statement is considered executable if it returns
 * a row count instead of a result set.
 *
 * @apiNote Just like any other data access API, including JDBC
 * itself, native SQL statement strings and Jakarta Persistence
 * Query Language strings executed via this API must never be
 * composed by concatenating user input or other untrusted data.
 * User input should be properly validated and passed as a
 * {@linkplain #setParameter(String, Object) parameter} to a
 * parameterized statement string. In particular, native SQL
 * statement strings executed via this API are typically executed
 * verbatim with no additional validation or sanitization beyond
 * that performed by the JDBC driver.
 *
 * @see EntityHandler#createStatement(String)
 * @see EntityHandler#createStatement(jakarta.persistence.criteria.CriteriaStatement)
 * @see EntityHandler#createStatement(StatementReference)
 * @see EntityHandler#createNativeStatement(String)
 * @see EntityHandler#createNamedStatement(String)
 * @see StatementOrTypedQuery#asStatement()
 *
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
     *         Persistence Query Language SELECT statement or for
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
     * Set a query property or hint. Properties defined by this
     * specification must be observed by the persistence provider.
     * Vendor-specific hints that are not recognized by a provider
     * must be silently ignored. Portable applications should not
     * rely on the standard timeout hint; depending on the database
     * in use and the locking mechanisms used by the provider,
     * this hint may or may not be observed.
     *
     * @param hintName The name of the property or hint
     * @param value The value for the property or hint
     * @return the same statement instance
     * @throws IllegalArgumentException if the given property or
     *         hint name is recognized by the provider, but the
     *         second argument is not a legal value for the given
     *         property or hint
     *
     * @apiNote The use of named hints lacks type safety compared
     *          to the use of {@linkplain Option options}.
     */
    @Override
    @Nonnull
    Statement setHint(@Nonnull String hintName, @Nullable Object value);

    /**
     * Set the query timeout, in milliseconds. This is a hint,
     * and is an alternative to {@linkplain #setHint setting
     * the hint} {@code jakarta.persistence.query.timeout}.
     *
     * @param timeout The timeout, in milliseconds, or null to
     *                indicate no timeout
     * @return the same statement instance
     * @since 3.2
     */
    @Override
    @Nonnull
    Statement setTimeout(@Nullable Integer timeout);

    /**
     * Set the query timeout. This is a hint.
     *
     * @param timeout the timeout, or null to indicate no timeout
     * @return the same statement instance
     * @since 4.0
     */
    @Override
    @Nonnull
    Statement setTimeout(@Nullable Timeout timeout);

    /**
     * Set the {@linkplain QueryFlushMode query flush mode} to be
     * used when the query is executed. This flush mode overrides
     * the {@linkplain EntityManager#getFlushMode flush mode type
     * of the entity manager}.
     *
     * @param flushMode The new flush mode
     * @return the same statement instance
     *
     * @since 4.0
     */
    @Override
    @Nonnull
    Statement setQueryFlushMode(@Nonnull QueryFlushMode flushMode);

    /**
     * Set the {@linkplain FlushModeType flush mode type} to be
     * used when the query is executed. This flush mode overrides
     * the {@linkplain EntityManager#getFlushMode flush mode type
     * of the entity manager}.
     *
     * @param flushMode The new flush mode
     * @return the same statement instance
     *
     * @deprecated Use {@link #setQueryFlushMode(QueryFlushMode)}.
     */
    @Override @Deprecated
    @Nonnull
    Statement setFlushMode(@Nonnull FlushModeType flushMode);

    /**
     * Bind an argument to a parameter of this query respresented as
     * a {@link Parameter} object.
     *
     * @param parameter The parameter object
     * @param value The argument to the parameter
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter
     *         does not correspond to a parameter of the
     *         query
     * @since 2.0
     */
    @Override
    @Nonnull
    <T> Statement setParameter(@Nonnull Parameter<T> parameter,
                               @Nullable T value);

    /**
     * Bind an instance of {@link Calendar} to a {@link Parameter} object.
     *
     * @param param The parameter object
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @since 2.0
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    @Nonnull
    Statement setParameter(@Nonnull Parameter<Calendar> param,
                           @Nullable Calendar value,
                           @Nonnull TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a {@link Parameter} object.
     * 
     * @param param The parameter object
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @since 2.0
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    @Nonnull
    Statement setParameter(@Nonnull Parameter<Date> param,
                           @Nullable Date value,
                           @Nonnull TemporalType temporalType);

    /**
     * Bind an argument value to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     */
    @Override
    @Nonnull
    Statement setParameter(@Nonnull String name, @Nullable Object value);

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
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    @Nonnull
    <P> Statement setParameter(@Nonnull String name, @Nullable P value,
                               @Nonnull Class<P> type);

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
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    @Nonnull
    <P> Statement setParameter(@Nonnull String name, @Nullable P value,
                               @Nonnull Type<P> type);

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
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    @Nonnull
    <P> Statement setConvertedParameter(@Nonnull String name, @Nullable P value,
                                        @Nonnull Class<? extends AttributeConverter<P,?>> converter);

    /**
     * Bind an instance of {@link Calendar} to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the value argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    @Nonnull
    Statement setParameter(@Nonnull String name, @Nullable Calendar value,
                           @Nonnull TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same statement instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the value argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    @Nonnull
    Statement setParameter(@Nonnull String name, @Nullable Date value,
                           @Nonnull TemporalType temporalType);

    /**
     * Bind an argument value to a positional parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @return the same statement instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     */
    @Override
    @Nonnull
    Statement setParameter(int position, @Nullable Object value);

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
     * @return the same statement instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    @Nonnull
    <P> Statement setParameter(int position, @Nullable P value,
                               @Nonnull Class<P> type);

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
     * @return the same statement instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @since 4.0
     */
    @Override
    @Nonnull
    <P> Statement setParameter(int position, @Nullable P value,
                               @Nonnull Type<P> type);

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
     * @return the same statement instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    @Nonnull
    <P> Statement setConvertedParameter(int position, @Nullable P value,
                                        @Nonnull Class<? extends AttributeConverter<P,?>> converter);

    /**
     * Bind an instance of {@link Calendar} to a positional
     * parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same statement instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    @Nonnull
    Statement setParameter(int position, @Nullable Calendar value,
                           @Nonnull TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a positional
     * parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same statement instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    @Nonnull
    Statement setParameter(int position, @Nullable Date value,
                           @Nonnull TemporalType temporalType);

    /**
     * Bind arguments to every positional parameter of the statement.
     * <p>
     * The <em>n</em>th argument is bound to the positional
     * parameter {@code ?n}.
     * @param arguments The arguments to positional parameters
     * @return the same statement instance
     * @throws IllegalArgumentException if the number of arguments
     *         is not exactly the same as the number of positional
     *         parameters, or if one of the arguments is of an
     *         incompatible type
     * @since 4.0
     */
    @Nonnull
    @Override
    Statement setParameters(@Nonnull Object... arguments);

    /**
     * Specify an {@linkplain Option option} influencing execution
     * of this statement, overwriting any existing option of the
     * same type.
     *
     * @param option the option
     * @return the same statement instance
     * @since 4.0
     */
    @Nonnull
    Statement addOption(@Nonnull Option option);

    /**
     * Get the {@linkplain Option options} influencing execution of
     * this statement. The returned set includes options set via
     * {@link #addOption}, along with options specified via
     * {@link #setTimeout} or {@link #setQueryFlushMode}. Mutation of
     * the returned set does not affect the options of the statement.
     *
     * @return the options for this statement
     * @since 4.0
     */
    @Nonnull
    Set<Option> getOptions();

    /**
     * An option influencing execution of a statement. This provides
     * a more type safe alternative to the use of {@linkplain #setHint
     * hints}.
     *
     * <p>This interface may be implemented by custom provider-specific
     * options which extend the options defined by the specification.
     *
     * @see QueryFlushMode
     * @see Timeout
     *
     * @since 4.0
     */
    interface Option {
    }
}
