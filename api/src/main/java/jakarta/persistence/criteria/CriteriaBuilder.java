/*
 * Copyright (c) 2008, 2026 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King           - 4.0
//     Christian Beikov     - 4.0
//     Henrique Dias Campos - 4.0
//     Gavin King           - 3.2
//     Linda DeMichiel      - 2.1
//     Linda DeMichiel      - 2.0


package jakarta.persistence.criteria;

import jakarta.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.StatementReference;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQueryReference;

/**
 * Used to construct criteria queries, compound selections, 
 * expressions, predicates, orderings.
 *
 * <p>This example demonstrates a simple select query with no joins:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var query = builder.createQuery(Book.class);
 * var book = query.from(Book.class);
 * query.select(book)
 *      .where(book.get(Book_.title).like(titlePattern))
 *      .orderBy(book.get(Book_.title).asc());
 * var books = agent.createQuery(query).getResultList();
 * }
 *
 * <p>This example demonstrates a select query with a join:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var query = builder.createQuery(Book.class);
 * var book = query.from(Book.class);
 * var author = book.join(Book_.authors);
 * query.select(book)
 *      .where(author.get(Author_.lastName).equalTo(lastName))
 *      .orderBy(book.get(Book_.title).asc());
 * var books = agent.createQuery(query).getResultList();
 * }
 *
 * <p>This example demonstrates a select query with a fetch join:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var query = builder.createQuery(Book.class);
 * var book = query.from(Book.class);
 * book.fetch(Book_.authors, JoinType.LEFT);
 * query.select(book)
 *      .where(book.get(Book_.title).like(titlePattern))
 *      .orderBy(book.get(Book_.title).asc());
 * var books = agent.createQuery(query).getResultList();
 * }
 *
 * <p>This example demonstrates a select query with a subquery:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var query = builder.createQuery(Book.class);
 * var book = query.from(Book.class);
 *
 * var authorCount = query.subquery(Long.class);
 * var bookInSubquery = authorCount.correlate(book);
 * var author = bookInSubquery.join(Book_.authors);
 * authorCount.select(builder.count(author));
 *
 * query.select(book)
 *      .where(builder.gt(authorCount, minimumAuthors))
 *      .orderBy(book.get(Book_.title).asc());
 * var books = agent.createQuery(query).getResultList();
 * }
 *
 * <p>This example demonstrates a bulk update statement:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var update = builder.createCriteriaUpdate(Book.class);
 * var book = update.from(Book.class);
 * update.set(Book_.outOfPrint, true)
 *       .where(book.get(Book_.publicationDate).lessThan(cutoffDate));
 * int updated = agent.createStatement(update).execute();
 * }
 *
 * <p>This example demonstrates a bulk delete statement:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var delete = builder.createCriteriaDelete(Book.class);
 * var book = delete.from(Book.class);
 * delete.where(book.get(Book_.outOfPrint));
 * int deleted = agent.createStatement(delete).execute();
 * }
 *
 * @apiNote {@link Predicate} is used instead of
 * {@code Expression<Boolean>} in this API to work around the
 * fact that Java generics are not compatible with varargs.
 *
 * @since 2.0
 */
public interface CriteriaBuilder {

    /**
     * Create a {@link CriteriaQuery} object.
     * @return criteria query object
     */
    @Nonnull
    CriteriaQuery<Object> createQuery();

    /**
     * Create a {@link CriteriaQuery} object with the given
     * result type.
     * @param resultClass  type of the query result
     * @param <T> the query result type
     * @return criteria query object
     */
    @Nonnull
    <T> CriteriaQuery<T> createQuery(@Nonnull Class<T> resultClass);

    /**
     * Create a {@link CriteriaQuery} object representing the
     * given Jakarta Persistence query language {@code SELECT}
     * query with the given result type.
     * @param resultClass type of the query result
     * @param jpql A Jakarta Persistence query language
     *             {@code SELECT} query
     * @param <T> the query result type
     * @return criteria query object
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid, or if the query result is
     *         found to not be assignable to the specified type
     *         and the specified type does not have a suitable
     *         constructor
     * @since 4.0
     */
    @Nonnull
    <T> CriteriaQuery<T> createQuery(@Nonnull Class<T> resultClass,
                                     @Nonnull String jpql);

    /**
     * Create a {@link CriteriaQuery} object representing the
     * given Jakarta Persistence query language {@code SELECT}
     * query.
     * @param jpql A Jakarta Persistence query language
     *             {@code SELECT} query
     * @return criteria query object
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid
     * @since 4.0
     */
    @Nonnull
    CriteriaQuery<?> createQuery(@Nonnull String jpql);

    /**
     * Create a {@link CriteriaQuery} object that returns a
     * tuple of objects as its result.
     * @return criteria query object
     */
    @Nonnull
    CriteriaQuery<Tuple> createTupleQuery();

    // methods to construct queries for bulk updates and deletes:

    /**
     * Create a {@link CriteriaUpdate} object to perform a bulk
     * update operation.
     * @param targetEntity  target type for update operation
     * @param <T> the target entity type
     * @return the criteria statement object
     * @since 2.1
     */
    @Nonnull
    <T> CriteriaUpdate<T> createCriteriaUpdate(@Nonnull Class<T> targetEntity);

    /**
     * Create a {@link CriteriaUpdate} object representing the
     * given Jakarta Persistence query language {@code UPDATE}
     * query with the given target entity type.
     * @param jpql A Jakarta Persistence query language
     *             {@code UPDATE} query
     * @param targetEntity target type for update operation
     * @param <T> the target entity type
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid, or if the entity type updated
     *         by the query is not exactly the same as the
     *         specified target type
     * @return the criteria statement object
     * @since 4.0
     */
    @Nonnull
    <T> CriteriaUpdate<T> createCriteriaUpdate(@Nonnull Class<T> targetEntity,
                                               @Nonnull String jpql);

    /**
     * Create a {@link CriteriaUpdate} object representing the
     * given Jakarta Persistence query language {@code UPDATE}
     * query.
     * @param jpql A Jakarta Persistence query language
     *             {@code UPDATE} query
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid
     * @return the criteria statement object
     * @since 4.0
     */
    @Nonnull
    CriteriaUpdate<?> createCriteriaUpdate(@Nonnull String jpql);

    /**
     * Create a {@link CriteriaDelete} object to perform a bulk
     * delete operation.
     * @param targetEntity  target type for delete operation
     * @param <T> the target entity type
     * @return the criteria statement object
     * @since 2.1
     */
    @Nonnull
    <T> CriteriaDelete<T> createCriteriaDelete(@Nonnull Class<T> targetEntity);

    /**
     * Create a {@link CriteriaDelete} object representing the
     * given Jakarta Persistence query language {@code DELETE}
     * query with the given target entity type.
     * @param jpql A Jakarta Persistence query language
     *             {@code DELETE} query
     * @param targetEntity target type for delete operation
     * @param <T> the target entity type
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid, or if the entity type deleted
     *         by the query is not exactly the same as the
     *         specified target type
     * @return the criteria statement object
     * @since 4.0
     */
    @Nonnull
    <T> CriteriaDelete<T> createCriteriaDelete(@Nonnull Class<T> targetEntity,
                                               @Nonnull String jpql);

    /**
     * Create a {@link CriteriaDelete} object representing the
     * given Jakarta Persistence query language {@code DELETE}
     * query.
     * @param jpql A Jakarta Persistence query language
     *             {@code DELETE} query
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid
     * @return the criteria statement object
     * @since 4.0
     */
    @Nonnull
    CriteriaDelete<?> createCriteriaDelete(@Nonnull String jpql);

    /**
     * Modify the Jakarta Persistence query language query
     * represented by the given reference, returning a
     * reference to the modified query inheriting all the
     * options of the given reference. This operation never
     * modifies the query represented by the given reference.
     * @param reference A reference to a Jakarta Persistence
     *                  query language {@code SELECT} query
     * @param augmentation a consumer that modifies the query
     * @param <T> the query result type
     * @return a reference to the modified query
     * @throws IllegalArgumentException if the given reference
     *         does not represent a named Jakarta Persistence
     *         query language {@code SELECT} query belonging
     *         to the owning factory
     * @since 4.0
     */
    @Nonnull
    <T> TypedQueryReference<T> augment(@Nonnull TypedQueryReference<T> reference,
                                       @Nonnull Consumer<CriteriaQuery<T>> augmentation);

    /**
     * Modify the Jakarta Persistence query language statement
     * represented by the given reference, returning a reference
     * to the modified statement inheriting all the options of
     * the given reference. This operation never modifies the
     * query represented by the given reference.
     * @param reference A reference to a Jakarta Persistence
     *                  query language statement
     * @param augmentation a consumer that modifies the statement
     * @return a reference to the modified statement
     * @throws IllegalArgumentException if the given reference
     *         does not represent a named Jakarta Persistence
     *         query language statement belonging to the owning
     *         factory
     * @since 4.0
     */
    @Nonnull
    StatementReference augment(@Nonnull StatementReference reference,
                               @Nonnull Consumer<CriteriaStatement<?>> augmentation);


    // selection construction methods:
	
    /**
     * Create a selection item corresponding to a constructor.
     * This method is used to specify a constructor that is
     * applied to the results of the query execution. If the
     * constructor is for an entity class, the resulting entities
     * will be in the new state after the query is executed.
     * @param resultClass  class whose instance is to be constructed
     * @param selections  arguments to the constructor
     * @param <Y> the type of the constructed result
     * @return compound selection item
     * @throws IllegalArgumentException if an argument is a 
     *         tuple- or array-valued selection item
     */
    @Nonnull
    <Y> CompoundSelection<Y> construct(@Nonnull Class<Y> resultClass,
                                       @Nonnull Selection<?>... selections);

    /**
     * Create a tuple-valued selection item.
     * @param selections  selection items
     * @return tuple-valued compound selection
     * @throws IllegalArgumentException if an argument is a 
     *         tuple- or array-valued selection item
     */
    @Nonnull
    CompoundSelection<Tuple> tuple(@Nonnull Selection<?>... selections);

    /**
     * Create a tuple-valued selection item.
     * @param selections  list of selection items
     * @return tuple-valued compound selection
     * @throws IllegalArgumentException if an argument is a
     *         tuple- or array-valued selection item
     * @since 3.2
     */
    @Nonnull
    CompoundSelection<Tuple> tuple(@Nonnull List<Selection<?>> selections);

    /**
     * Create an array-valued selection item.
     * @param selections  selection items
     * @return array-valued compound selection
     * @throws IllegalArgumentException if an argument is a
     *         tuple- or array-valued selection item
     */
    @Nonnull
    CompoundSelection<Object[]> array(@Nonnull Selection<?>... selections);

    /**
     * Create an array-valued selection item.
     * @param selections  list of selection items
     * @return array-valued compound selection
     * @throws IllegalArgumentException if an argument is a
     *         tuple- or array-valued selection item
     * @since 3.2
     */
    @Nonnull
    CompoundSelection<Object[]> array(@Nonnull List<Selection<?>> selections);


    //ordering:
	
    /**
     * Create an ordering by the ascending value of the expression.
     * @param expression  expression used to define the ordering
     * @return ascending ordering corresponding to the expression
     */
    @Nonnull
    Order asc(@Nonnull Expression<?> expression);

    /**
     * Create an ordering by the descending value of the expression.
     * @param expression  expression used to define the ordering
     * @return descending ordering corresponding to the expression
     */
    @Nonnull
    Order desc(@Nonnull Expression<?> expression);

    /**
     * Create an ordering by the ascending value of the expression.
     * @param expression  expression used to define the ordering
     * @param nullPrecedence  the precedence of null values
     * @return ascending ordering corresponding to the expression
     * @since 3.2
     */
    @Nonnull
    Order asc(@Nonnull Expression<?> expression,
              @Nonnull Nulls nullPrecedence);

    /**
     * Create an ordering by the descending value of the expression.
     * @param expression  expression used to define the ordering
     * @param nullPrecedence  the precedence of null values
     * @return descending ordering corresponding to the expression
     * @since 3.2
     */
    @Nonnull
    Order desc(@Nonnull Expression<?> expression,
               @Nonnull Nulls nullPrecedence);


    //aggregate functions:
	
    /**
     * Create an aggregate expression applying the avg operation.
     * @param x  expression representing input value to avg operation
     * @param <N> the numeric type of the expression
     * @return avg expression
     */
    @Nonnull
    <N extends Number> Expression<Double> avg(@Nonnull Expression<N> x);

    /**
     * Create an aggregate expression applying the sum operation.
     * @param x  expression representing input value to sum operation
     * @param <N> the numeric type of the expression
     * @return sum expression
     */
    @Nonnull
    <N extends Number> Expression<N> sum(@Nonnull Expression<N> x);

    /**
     * Create an aggregate expression applying the sum operation to an
     * Integer-valued expression, returning a Long result.
     * @param x  expression representing input value to sum operation
     * @return sum expression
     */
    @Nonnull
    Expression<Long> sumAsLong(@Nonnull Expression<Integer> x);

    /**
     * Create an aggregate expression applying the sum operation to a
     * Float-valued expression, returning a Double result.
     * @param x  expression representing input value to sum operation
     * @return sum expression
     */
    @Nonnull
    Expression<Double> sumAsDouble(@Nonnull Expression<Float> x);
    
    /**
     * Create an aggregate expression applying the numerical max 
     * operation.
     * @param x  expression representing input value to max operation
     * @param <N> the numeric type of the expression
     * @return max expression
     */
    @Nonnull
    <N extends Number> Expression<N> max(@Nonnull Expression<N> x);
    
    /**
     * Create an aggregate expression applying the numerical min 
     * operation.
     * @param x  expression representing input value to min operation
     * @param <N> the numeric type of the expression
     * @return min expression
     */
    @Nonnull
    <N extends Number> Expression<N> min(@Nonnull Expression<N> x);

    /**
     * Create an aggregate expression for finding the greatest of
     * the values (strings, dates, etc).
     * @param x  expression representing input value to greatest
     *           operation
     * @param <X> the type of the expression
     * @return greatest expression
     */
    @Nonnull
    <X extends Comparable<? super X>> Expression<X> greatest(@Nonnull Expression<X> x);
    
    /**
     * Create an aggregate expression for finding the least of
     * the values (strings, dates, etc).
     * @param x  expression representing input value to least
     *           operation
     * @param <X> the type of the expression
     * @return least expression
     */
    @Nonnull
    <X extends Comparable<? super X>> Expression<X> least(@Nonnull Expression<X> x);

    /**
     * Create an aggregate expression applying the count operation.
     * @param x  expression representing input value to count 
     *           operation
     * @return count expression
     */
    @Nonnull
    Expression<Long> count(@Nonnull Expression<?> x);

    /**
     * Create an aggregate expression applying the count distinct 
     * operation.
     * @param x  expression representing input value to 
     *        count distinct operation
     * @return count distinct expression
     */
    @Nonnull
    Expression<Long> countDistinct(@Nonnull Expression<?> x);
	


    //subqueries:
	
    /**
     * Create a predicate testing the existence of a subquery result.
     * @param subquery  subquery whose result is to be tested
     * @return exists predicate
     */
    @Nonnull
    Predicate exists(@Nonnull Subquery<?> subquery);
	
    /**
     * Create an all expression over the subquery results.
     * @param subquery  subquery
     * @param <Y> the subquery result type
     * @return all expression
     */
    @Nonnull
    <Y> Expression<Y> all(@Nonnull Subquery<Y> subquery);
	
    /**
     * Create a some expression over the subquery results.
     * This expression is equivalent to an {@code any} expression.
     * @param subquery  subquery
     * @param <Y> the subquery result type
     * @return some expression
     */
    @Nonnull
    <Y> Expression<Y> some(@Nonnull Subquery<Y> subquery);
	
    /**
     * Create an any expression over the subquery results. 
     * This expression is equivalent to a {@code some} expression.
     * @param subquery  subquery
     * @param <Y> the subquery result type
     * @return any expression
     */
    @Nonnull
    <Y> Expression<Y> any(@Nonnull Subquery<Y> subquery);


    //boolean functions:
	
    /**
     * Create a conjunction of the given boolean expressions.
     * @param x  boolean expression
     * @param y  boolean expression
     * @return and predicate
     */
    @Nonnull
    Predicate and(@Nonnull Expression<Boolean> x,
                  @Nonnull Expression<Boolean> y);
    
    /**
     * Create a conjunction of the given restriction predicates.
     * A conjunction of zero predicates is true.
     * @param restrictions  zero or more restriction predicates
     * @return and predicate
     */
    @Nonnull
    Predicate and(@Nonnull BooleanExpression... restrictions);

    /**
     * Create a conjunction of the given restriction predicates.
     * A conjunction of zero predicates is true.
     * @param restrictions  a list of zero or more restriction predicates
     * @return and predicate
     * @since 3.2
     */
    @Nonnull
    Predicate and(@Nonnull List<? extends Expression<Boolean>> restrictions);

    /**
     * Create a disjunction of the given boolean expressions.
     * @param x  boolean expression
     * @param y  boolean expression
     * @return or predicate
     */
    @Nonnull
    Predicate or(@Nonnull Expression<Boolean> x,
                 @Nonnull Expression<Boolean> y);

    /**
     * Create a disjunction of the given restriction predicates.
     * A disjunction of zero predicates is false.
     * @param restrictions  zero or more restriction predicates
     * @return or predicate
     */
    @Nonnull
    Predicate or(@Nonnull BooleanExpression... restrictions);

    /**
     * Create a disjunction of the given restriction predicates.
     * A disjunction of zero predicates is false.
     * @param restrictions  a list of zero or more restriction predicates
     * @return or predicate
     * @since 3.2
     */
    @Nonnull
    Predicate or(@Nonnull List<? extends Expression<Boolean>> restrictions);

    /**
     * Create a negation of the given restriction. 
     * @param restriction  restriction expression
     * @return not predicate
     */
    @Nonnull
    Predicate not(@Nonnull Expression<Boolean> restriction);
	
    /**
     * Create a conjunction (with zero conjuncts).
     * A conjunction with zero conjuncts is true.
     * @return and predicate
     */
    @Nonnull
    Predicate conjunction();

    /**
     * Create a disjunction (with zero disjuncts).
     * A disjunction with zero disjuncts is false.
     * @return or predicate
     */
    @Nonnull
    Predicate disjunction();

	
    //turn Expression<Boolean> into a Predicate
    //useful for use with varargs methods

    /**
     * Create a predicate testing for a true value.
     * @param x  expression to be tested
     * @return predicate
     */
    @Nonnull
    Predicate isTrue(@Nonnull Expression<Boolean> x);

    /**
     * Create a predicate testing for a false value.
     * @param x  expression to be tested
     * @return predicate
     */
    @Nonnull
    Predicate isFalse(@Nonnull Expression<Boolean> x);

	
    //null tests:

    /**
     * Create a predicate to test whether the expression is null.
     * @param x expression
     * @return is-null predicate
     */
    @Nonnull
    Predicate isNull(@Nonnull Expression<?> x);

    /**
     * Create a predicate to test whether the expression is not null.
     * @param x expression
     * @return is-not-null predicate
     */
    @Nonnull
    Predicate isNotNull(@Nonnull Expression<?> x);

    //equality:
	
    /**
     * Create a predicate for testing the arguments for equality.
     * @param x  expression
     * @param y  expression
     * @return equality predicate
     */
    @Nonnull
    Predicate equal(@Nonnull Expression<?> x,
                    @Nonnull Expression<?> y);
	
    /**
     * Create a predicate for testing the arguments for equality.
     * @param x  expression
     * @param y  object
     * @return equality predicate
     */
    @Nonnull
    Predicate equal(@Nonnull Expression<?> x,
                    Object y);

    /**
     * Create a predicate for testing the arguments for inequality.
     * @param x  expression
     * @param y  expression
     * @return inequality predicate
     */
    @Nonnull
    Predicate notEqual(@Nonnull Expression<?> x,
                       @Nonnull Expression<?> y);
	
    /**
     * Create a predicate for testing the arguments for inequality.
     * @param x  expression
     * @param y  object
     * @return inequality predicate
     */
    @Nonnull
    Predicate notEqual(@Nonnull Expression<?> x,
                       Object y);

	
    //comparisons for generic (non-numeric) operands:

    /**
     * Create a predicate for testing whether the first argument is 
     * greater than the second.
     * @param x  expression
     * @param y  expression
     * @param <Y> the type of the compared expressions
     * @return greater-than predicate
     * @see #gt(Expression, Expression)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate greaterThan(@Nonnull Expression<? extends Y> x,
                                                            @Nonnull Expression<? extends Y> y);
	
    /**
     * Create a predicate for testing whether the first argument is 
     * greater than the second.
     * @param x  expression
     * @param y  value
     * @param <Y> the type of the compared values
     * @return greater-than predicate
     * @see #gt(Expression, Number)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate greaterThan(@Nonnull Expression<? extends Y> x,
                                                            Y y);
    
    /**
     * Create a predicate for testing whether the first argument is 
     * greater than or equal to the second.
     * @param x  expression
     * @param y  expression
     * @param <Y> the type of the compared expressions
     * @return greater-than-or-equal predicate
     * @see #ge(Expression, Expression)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(@Nonnull Expression<? extends Y> x,
                                                                     @Nonnull Expression<? extends Y> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * greater than or equal to the second.
     * @param x  expression
     * @param y  value
     * @param <Y> the type of the compared values
     * @return greater-than-or-equal predicate
     * @see #ge(Expression, Number)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(@Nonnull Expression<? extends Y> x,
                                                                     Y y);

    /**
     * Create a predicate for testing whether the first argument is 
     * less than the second.
     * @param x  expression
     * @param y  expression
     * @param <Y> the type of the compared expressions
     * @return less-than predicate
     * @see #lt(Expression, Expression)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate lessThan(@Nonnull Expression<? extends Y> x,
                                                         @Nonnull Expression<? extends Y> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * less than the second.
     * @param x  expression
     * @param y  value
     * @param <Y> the type of the compared values
     * @return less-than predicate
     * @see #lt(Expression, Number)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate lessThan(@Nonnull Expression<? extends Y> x,
                                                         Y y);
	
    /**
     * Create a predicate for testing whether the first argument is 
     * less than or equal to the second.
     * @param x  expression
     * @param y  expression
     * @param <Y> the type of the compared expressions
     * @return less-than-or-equal predicate
     * @see #le(Expression, Expression)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(@Nonnull Expression<? extends Y> x,
                                                                  @Nonnull Expression<? extends Y> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * less than or equal to the second.
     * @param x  expression
     * @param y  value
     * @param <Y> the type of the compared values
     * @return less-than-or-equal predicate
     * @see #le(Expression, Number)
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(@Nonnull Expression<? extends Y> x,
                                                                  Y y);

    /**
     * Create a predicate for testing whether the first argument is 
     * between the second and third arguments in value.
     * @param v  expression 
     * @param x  expression
     * @param y  expression
     * @param <Y> the type of the compared expressions
     * @return between predicate
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate between(@Nonnull Expression<? extends Y> v,
                                                        @Nonnull Expression<? extends Y> x,
                                                        @Nonnull Expression<? extends Y> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * between the second and third arguments in value.
     * @param v  expression 
     * @param x  value
     * @param y  value
     * @param <Y> the type of the compared values
     * @return between predicate
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate between(@Nonnull Expression<? extends Y> v,
                                                        Y x, Y y);
	
    /**
     * Create a predicate for testing whether the first argument is 
     * between the second and third arguments in value.
     * @param v  value 
     * @param x  expression
     * @param y  expression
     * @param <Y> the type of the compared values
     * @return between predicate
     * @since 4.0
     */
    @Nonnull
    <Y extends Comparable<? super Y>> Predicate between(Y v,
                                                        @Nonnull Expression<? extends Y> x,
                                                        @Nonnull Expression<? extends Y> y);

    //comparisons for numeric operands:
	
    /**
     * Create a predicate for testing whether the first argument is 
     * greater than the second. This operation accepts arguments of
     * heterogeneous numeric types.
     * @param x  expression
     * @param y  expression
     * @return greater-than predicate
     */
    @Nonnull
    Predicate gt(@Nonnull Expression<? extends Number> x,
                 @Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * greater than the second. This operation accepts arguments of
     * heterogeneous numeric types.
     * @param x  expression
     * @param y  value
     * @return greater-than predicate
     */
    @Nonnull
    Predicate gt(@Nonnull Expression<? extends Number> x,
                 Number y);

    /**
     * Create a predicate for testing whether the first argument is 
     * greater than or equal to the second. This operation accepts
     * arguments of heterogeneous numeric types.
     * @param x  expression
     * @param y  expression
     * @return greater-than-or-equal predicate
     */
    @Nonnull
    Predicate ge(@Nonnull Expression<? extends Number> x,
                 @Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * greater than or equal to the second. This operation accepts
     * arguments of heterogeneous numeric types.
     * @param x  expression
     * @param y  value
     * @return greater-than-or-equal predicate
     */	
    @Nonnull
    Predicate ge(@Nonnull Expression<? extends Number> x,
                 Number y);

    /**
     * Create a predicate for testing whether the first argument is 
     * less than the second. This operation accepts arguments of
     * heterogeneous numeric types.
     * @param x  expression
     * @param y  expression
     * @return less-than predicate
     */
    @Nonnull
    Predicate lt(@Nonnull Expression<? extends Number> x,
                 @Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * less than the second. This operation accepts arguments of
     * heterogeneous numeric types.
     * @param x  expression
     * @param y  value
     * @return less-than predicate
     */
    @Nonnull
    Predicate lt(@Nonnull Expression<? extends Number> x,
                 Number y);

    /**
     * Create a predicate for testing whether the first argument is 
     * less than or equal to the second. This operation accepts
     * arguments of heterogeneous numeric types.
     * @param x  expression
     * @param y  expression
     * @return less-than-or-equal predicate
     */
    @Nonnull
    Predicate le(@Nonnull Expression<? extends Number> x,
                 @Nonnull Expression<? extends Number> y);

    /**
     * Create a predicate for testing whether the first argument is 
     * less than or equal to the second. This operation accepts
     * arguments of heterogeneous numeric types.
     * @param x  expression
     * @param y  value
     * @return less-than-or-equal predicate
     */
    @Nonnull
    Predicate le(@Nonnull Expression<? extends Number> x,
                 Number y);
	

    //numerical operations:

    /**
     * Create an expression that returns the sign of its
     * argument, that is, {@code 1} if its argument is
     * positive, {@code -1} if its argument is negative,
     * or {@code 0} if its argument is exactly zero.
     * @param x expression
     * @return sign
     */
    @Nonnull
    Expression<Integer> sign(@Nonnull Expression<? extends Number> x);
	
    /**
     * Create an expression that returns the arithmetic negation
     * of its argument.
     * @param x expression
     * @param <N> the numeric type of the expression
     * @return arithmetic negation
     */
    @Nonnull
    <N extends Number> Expression<N> neg(@Nonnull Expression<N> x);

    /**
     * Create an expression that returns the absolute value
     * of its argument.
     * @param x expression
     * @param <N> the numeric type of the expression
     * @return absolute value
     */
    @Nonnull
    <N extends Number> Expression<N> abs(@Nonnull Expression<N> x);

    /**
     * Create an expression that returns the ceiling of its
     * argument, that is, the smallest integer greater than
     * or equal to its argument.
     * @param x expression
     * @param <N> the numeric type of the expression
     * @return ceiling
     */
    @Nonnull
    <N extends Number> Expression<N> ceiling(@Nonnull Expression<N> x);

    /**
     * Create an expression that returns the floor of its
     * argument, that is, the largest integer smaller than
     * or equal to its argument.
     * @param x expression
     * @param <N> the numeric type of the expression
     * @return floor
     */
    @Nonnull
    <N extends Number> Expression<N> floor(@Nonnull Expression<N> x);
    /**
     * Create an expression that returns the sum
     * of its arguments.
     * @param x expression
     * @param y expression
     * @param <N> the numeric type of the expressions
     * @return sum
     */
    @Nonnull
    <N extends Number> Expression<N> sum(@Nonnull Expression<? extends N> x,
                                         @Nonnull Expression<? extends N> y);
	
    /**
     * Create an expression that returns the sum
     * of its arguments.
     * @param x expression
     * @param y value
     * @param <N> the numeric type of the arguments
     * @return sum
     */
    @Nonnull
    <N extends Number> Expression<N> sum(@Nonnull Expression<? extends N> x,
                                         N y);

    /**
     * Create an expression that returns the sum
     * of its arguments.
     * @param x value
     * @param y expression
     * @param <N> the numeric type of the arguments
     * @return sum
     */
    @Nonnull
    <N extends Number> Expression<N> sum(N x,
                                         @Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the product
     * of its arguments.
     * @param x expression
     * @param y expression
     * @param <N> the numeric type of the expressions
     * @return product
     */
    @Nonnull
    <N extends Number> Expression<N> prod(@Nonnull Expression<? extends N> x,
                                          @Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the product
     * of its arguments.
     * @param x expression
     * @param y value
     * @param <N> the numeric type of the arguments
     * @return product
     */
    @Nonnull
    <N extends Number> Expression<N> prod(@Nonnull Expression<? extends N> x,
                                          N y);

    /**
     * Create an expression that returns the product
     * of its arguments.
     * @param x value
     * @param y expression
     * @param <N> the numeric type of the arguments
     * @return product
     */
    @Nonnull
    <N extends Number> Expression<N> prod(N x,
                                          @Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the difference
     * between its arguments.
     * @param x expression
     * @param y expression
     * @param <N> the numeric type of the expressions
     * @return difference
     */
    @Nonnull
    <N extends Number> Expression<N> diff(@Nonnull Expression<? extends N> x,
                                          @Nonnull Expression<? extends N> y);

    /**
     * Create an expression that returns the difference
     * between its arguments.
     * @param x expression
     * @param y value
     * @param <N> the numeric type of the arguments
     * @return difference
     */
    @Nonnull
    <N extends Number> Expression<N> diff(@Nonnull Expression<? extends N> x,
                                          N y);

    /**
     * Create an expression that returns the difference
     * between its arguments.
     * @param x value
     * @param y expression
     * @param <N> the numeric type of the arguments
     * @return difference
     */
    @Nonnull
    <N extends Number> Expression<N> diff(N x,
                                          @Nonnull Expression<? extends N> y);
	
    /**
     * Create an expression that returns the quotient
     * of its arguments.
     * @param x expression
     * @param y expression
     * @return quotient
     */
    @Nonnull
    Expression<Number> quot(@Nonnull Expression<? extends Number> x,
                            @Nonnull Expression<? extends Number> y);

    /**
     * Create an expression that returns the quotient
     * of its arguments.
     * @param x expression
     * @param y value
     * @return quotient
     */
    @Nonnull
    Expression<Number> quot(@Nonnull Expression<? extends Number> x,
                            Number y);

    /**
     * Create an expression that returns the quotient
     * of its arguments.
     * @param x value
     * @param y expression
     * @return quotient
     */
    @Nonnull
    Expression<Number> quot(Number x,
                            @Nonnull Expression<? extends Number> y);
	
    /**
     * Create an expression that returns the modulus
     * (remainder under integer division) of its
     * arguments.
     * @param x expression
     * @param y expression
     * @return modulus
     */
    @Nonnull
    Expression<Integer> mod(@Nonnull Expression<Integer> x,
                            @Nonnull Expression<Integer> y);
	
    /**
     * Create an expression that returns the modulus
     * (remainder under integer division) of its
     * arguments.
     * @param x expression
     * @param y value
     * @return modulus
     */
    @Nonnull
    Expression<Integer> mod(@Nonnull Expression<Integer> x,
                            Integer y);

    /**
     * Create an expression that returns the modulus
     * (remainder under integer division) of its
     * arguments.
     * @param x value
     * @param y expression
     * @return modulus
     */
    @Nonnull
    Expression<Integer> mod(Integer x,
                            @Nonnull Expression<Integer> y);

    /**
     * Create an expression that returns the square root
     * of its argument.
     * @param x expression
     * @return square root
     */	
    @Nonnull
    Expression<Double> sqrt(@Nonnull Expression<? extends Number> x);

    /**
     * Create an expression that returns the exponential
     * of its argument, that is, Euler's number <i>e</i>
     * raised to the power of its argument.
     * @param x expression
     * @return exponential
     */
    @Nonnull
    Expression<Double> exp(@Nonnull Expression<? extends Number> x);

    /**
     * Create an expression that returns the natural logarithm
     * of its argument.
     * @param x expression
     * @return natural logarithm
     */
    @Nonnull
    Expression<Double> ln(@Nonnull Expression<? extends Number> x);

    /**
     * Create an expression that returns the first argument
     * raised to the power of its second argument.
     * @param x base
     * @param y exponent
     * @return the base raised to the power of the exponent
     */
    @Nonnull
    Expression<Double> power(@Nonnull Expression<? extends Number> x,
                             @Nonnull Expression<? extends Number> y);

    /**
     * Create an expression that returns the first argument
     * raised to the power of its second argument.
     * @param x base
     * @param y exponent
     * @return the base raised to the power of the exponent
     */
    @Nonnull
    Expression<Double> power(@Nonnull Expression<? extends Number> x,
                             Number y);

    /**
     * Create an expression that returns the first argument
     * rounded to the number of decimal places given by the
     * second argument.
     * @param x base
     * @param n number of decimal places
     * @param <T> the numeric type of the expression
     * @return the rounded value
     */
    @Nonnull
    <T extends Number> Expression<T> round(@Nonnull Expression<T> x,
                                           @Nonnull Integer n);


    //typecasts:
    
    /**
     * Typecast.
     * @param number  numeric expression
     * @return {@literal Expression<Long>}
     */
    @Nonnull
    Expression<Long> toLong(@Nonnull Expression<? extends Number> number);

    /**
     * Typecast.
     * @param number  numeric expression
     * @return {@literal Expression<Integer>}
     */
    @Nonnull
    Expression<Integer> toInteger(@Nonnull Expression<? extends Number> number);

    /**
     * Typecast.
     * @param number  numeric expression
     * @return {@literal Expression<Float>}
     */
    @Nonnull
    Expression<Float> toFloat(@Nonnull Expression<? extends Number> number);

    /**
     * Typecast.
     * @param number  numeric expression
     * @return {@literal Expression<Double>}
     */
    @Nonnull
    Expression<Double> toDouble(@Nonnull Expression<? extends Number> number);

    /**
     * Typecast.
     * @param number  numeric expression
     * @return {@literal Expression<BigDecimal>}
     */
    @Nonnull
    Expression<BigDecimal> toBigDecimal(@Nonnull Expression<? extends Number> number);

    /**
     * Typecast.
     * @param number  numeric expression
     * @return {@literal Expression<BigInteger>}
     */
    @Nonnull
    Expression<BigInteger> toBigInteger(@Nonnull Expression<? extends Number> number);
	
    /**
     * Typecast.
     * @param character expression
     * @return {@literal Expression<String>}
     */
    @Nonnull
    Expression<String> toString(@Nonnull Expression<Character> character);

	
    //literals:

    /**
     * Create an expression for a literal.
     * @param value  value represented by the expression
     * @param <T> the type of the literal value
     * @return expression literal
     * @throws IllegalArgumentException if value is null
     */
    @Nonnull
    <T> Expression<T> literal(@Nonnull T value);

    /**
     * Create a number expression for a literal.
     * @param value  value represented by the expression
     * @param <N> the numeric type of the literal value
     * @return expression literal
     * @throws IllegalArgumentException if value is null
     * @since 4.0
     */
    @Nonnull
    <N extends Number & Comparable<N>> NumericExpression<N> numericLiteral(@Nonnull N value);

    /**
     * Create a string expression for a literal.
     * @param value  value represented by the expression
     * @return expression literal
     * @throws IllegalArgumentException if value is null
     * @since 4.0
     */
    @Nonnull
    TextExpression stringLiteral(@Nonnull String value);

    /**
     * Create a local date expression for a literal.
     * @param value  value represented by the expression
     * @param <T> the temporal type of the literal value
     * @return expression literal
     * @throws IllegalArgumentException if value is null
     * @since 4.0
     */
    @Nonnull
    <T extends Temporal & Comparable<? super T>> TemporalExpression<T> temporalLiteral(@Nonnull T value);

    /**
     * Create a boolean expression for a literal.
     * @return expression literal
     * @since 4.0
     */
    @Nonnull
    BooleanExpression booleanLiteral(boolean value);

    /**
     * Create an expression for a null literal with the given type.
     * @param resultClass  type of the null literal
     * @param <T> the type of the null literal
     * @return null expression literal
     */
    @Nonnull
    <T> Expression<T> nullLiteral(@Nonnull Class<T> resultClass);

    //parameters:

    /**
     * Create a parameter expression.
     * @param paramClass parameter class
     * @param <T> the parameter type
     * @return parameter expression
     */
    @Nonnull
    <T> ParameterExpression<T> parameter(@Nonnull Class<T> paramClass);

    /**
     * Create a parameter expression with the given name.
     * @param paramClass parameter class
     * @param name  name that can be used to refer to 
     *              the parameter
     * @param <T> the parameter type
     * @return parameter expression
     */
    @Nonnull
    <T> ParameterExpression<T> parameter(@Nonnull Class<T> paramClass,
                                         @Nonnull String name);

    /**
     * Create a parameter expression whose value is bound via a
     * {@linkplain AttributeConverter converter}.
     * @param converter the class of the attribute converter
     * @param <T> the parameter type
     * @return parameter expression
     * @since 4.0
     */
    @Nonnull
    <T> ParameterExpression<T> convertedParameter(@Nonnull Class<? extends AttributeConverter<T,?>> converter);

    //collection operations:
	
    /**
     * Create a predicate that tests whether a collection is empty.
     * @param collection expression
     * @param <C> the collection type
     * @return is-empty predicate
     */
    @Nonnull
    <C extends Collection<?>> Predicate isEmpty(@Nonnull Expression<C> collection);

    /**
     * Create a predicate that tests whether a collection is
     * not empty.
     * @param collection expression
     * @param <C> the collection type
     * @return is-not-empty predicate
     */
    @Nonnull
    <C extends Collection<?>> Predicate isNotEmpty(@Nonnull Expression<C> collection);

    /**
     * Create an expression that tests the size of a collection.
     * @param collection expression
     * @param <C> the collection type
     * @return size expression
     */ 
    @Nonnull
    <C extends Collection<?>> Expression<Integer> size(@Nonnull Expression<C> collection);
	
    /**
     * Create an expression that tests the size of a collection.
     * @param collection collection
     * @param <C> the collection type
     * @return size expression
     */ 
    @Nonnull
    <C extends Collection<?>> Expression<Integer> size(@Nonnull C collection);

    /**
     * Create a predicate that tests whether an element is
     * a member of a collection.
     * If the collection is empty, the predicate will be false.
     * @param elem element expression
     * @param collection expression
     * @param <E> the element type
     * @param <C> the collection type
     * @return is-member predicate
     */
    @Nonnull
    <E, C extends Collection<E>> Predicate isMember(@Nonnull Expression<E> elem,
                                                    @Nonnull Expression<C> collection);

    /**
     * Create a predicate that tests whether an element is
     * a member of a collection.
     * If the collection is empty, the predicate will be false.
     * @param elem element
     * @param collection expression
     * @param <E> the element type
     * @param <C> the collection type
     * @return is-member predicate
     */
    @Nonnull
    <E, C extends Collection<E>> Predicate isMember(E elem,
                                                    @Nonnull Expression<C> collection);

    /**
     * Create a predicate that tests whether an element is
     * not a member of a collection.
     * If the collection is empty, the predicate will be true.
     * @param elem element expression
     * @param collection expression
     * @param <E> the element type
     * @param <C> the collection type
     * @return is-not-member predicate
     */
    @Nonnull
    <E, C extends Collection<E>> Predicate isNotMember(@Nonnull Expression<E> elem,
                                                       @Nonnull Expression<C> collection);
	
    /**
     * Create a predicate that tests whether an element is
     * not a member of a collection.
     * If the collection is empty, the predicate will be true.
     * @param elem element
     * @param collection expression
     * @param <E> the element type
     * @param <C> the collection type
     * @return is-not-member predicate
     */
    @Nonnull
    <E, C extends Collection<E>> Predicate isNotMember(E elem,
                                                       @Nonnull Expression<C> collection);


    //string functions:
	
    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     * @param x  string expression
     * @param pattern  string expression
     * @return like predicate
     */
    @Nonnull
    Predicate like(@Nonnull Expression<String> x,
                   @Nonnull Expression<String> pattern);
	
    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     * @param x  string expression
     * @param pattern  string 
     * @return like predicate
     */
    @Nonnull
    Predicate like(@Nonnull Expression<String> x,
                   @Nonnull String pattern);
	
    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     * @param x  string expression
     * @param pattern  string expression
     * @param escapeChar  escape character expression
     * @return like predicate
     */
    @Nonnull
    Predicate like(@Nonnull Expression<String> x,
                   @Nonnull Expression<String> pattern,
                   @Nonnull Expression<Character> escapeChar);
	
    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     * @param x  string expression
     * @param pattern  string expression
     * @param escapeChar  escape character
     * @return like predicate
     */
    @Nonnull
    Predicate like(@Nonnull Expression<String> x,
                   @Nonnull Expression<String> pattern,
                   char escapeChar);
	
    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     * @param x  string expression
     * @param pattern  string 
     * @param escapeChar  escape character expression
     * @return like predicate
     */
    @Nonnull
    Predicate like(@Nonnull Expression<String> x,
                   @Nonnull String pattern,
                   @Nonnull Expression<Character> escapeChar);

    /**
     * Create a predicate for testing whether the expression
     * satisfies the given pattern.
     * @param x  string expression
     * @param pattern  string 
     * @param escapeChar  escape character
     * @return like predicate
     */
    @Nonnull
    Predicate like(@Nonnull Expression<String> x,
                   @Nonnull String pattern,
                   char escapeChar);
	
    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     * @param x  string expression
     * @param pattern  string expression
     * @return not-like predicate
     */
    @Nonnull
    Predicate notLike(@Nonnull Expression<String> x,
                      @Nonnull Expression<String> pattern);
	
    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     * @param x  string expression
     * @param pattern  string 
     * @return not-like predicate
     */
    @Nonnull
    Predicate notLike(@Nonnull Expression<String> x,
                      @Nonnull String pattern);

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     * @param x  string expression
     * @param pattern  string expression
     * @param escapeChar  escape character expression
     * @return not-like predicate
     */
    @Nonnull
    Predicate notLike(@Nonnull Expression<String> x,
                      @Nonnull Expression<String> pattern,
                      @Nonnull Expression<Character> escapeChar);

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     * @param x  string expression
     * @param pattern  string expression
     * @param escapeChar  escape character
     * @return not-like predicate
     */
    @Nonnull
    Predicate notLike(@Nonnull Expression<String> x,
                      @Nonnull Expression<String> pattern,
                      char escapeChar);

    /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     * @param x  string expression
     * @param pattern  string 
     * @param escapeChar  escape character expression
     * @return not-like predicate
     */
    @Nonnull
    Predicate notLike(@Nonnull Expression<String> x,
                      @Nonnull String pattern,
                      @Nonnull Expression<Character> escapeChar);
	
   /**
     * Create a predicate for testing whether the expression
     * does not satisfy the given pattern.
     * @param x  string expression
     * @param pattern  string 
     * @param escapeChar  escape character
     * @return not-like predicate
     */
    @Nonnull
    Predicate notLike(@Nonnull Expression<String> x,
                      @Nonnull String pattern,
                      char escapeChar);

    /**
     * Create an expression for string concatenation.
     * If the given list of expressions is empty, returns
     * an expression equivalent to {@code literal("")}.
     * @param expressions  string expressions
     * @return expression corresponding to concatenation
     */
    @Nonnull
    Expression<String> concat(@Nonnull List<Expression<String>> expressions);

    /**
     * Create an expression for string concatenation.
     * @param x  string expression
     * @param y  string expression
     * @return expression corresponding to concatenation
     */
    @Nonnull
    Expression<String> concat(@Nonnull Expression<String> x,
                              @Nonnull Expression<String> y);
	
    /**
     * Create an expression for string concatenation.
     * @param x  string expression
     * @param y  string 
     * @return expression corresponding to concatenation
     */
    @Nonnull
    Expression<String> concat(@Nonnull Expression<String> x,
                              @Nonnull String y);

    /**
     * Create an expression for string concatenation.
     * @param x  string 
     * @param y  string expression
     * @return expression corresponding to concatenation
     */
    @Nonnull
    Expression<String> concat(@Nonnull String x,
                              @Nonnull Expression<String> y);
	
    /**
     * Create an expression for substring extraction.
     * Extracts a substring starting at the specified position
     * through to end of the string.
     * First position is 1.
     * @param x  string expression
     * @param from  start position expression 
     * @return expression corresponding to substring extraction
     */
    @Nonnull
    Expression<String> substring(@Nonnull Expression<String> x,
                                 @Nonnull Expression<Integer> from);
	
    /**
     * Create an expression for substring extraction.
     * Extracts a substring starting at the specified position
     * through to end of the string.
     * First position is 1.
     * @param x  string expression
     * @param from  start position 
     * @return expression corresponding to substring extraction
     */
    @Nonnull
    Expression<String> substring(@Nonnull Expression<String> x,
                                 int from);

    /**
     * Create an expression for substring extraction.
     * Extracts a substring of given length starting at the
     * specified position.
     * First position is 1.
     * @param x  string expression
     * @param from  start position expression 
     * @param len  length expression
     * @return expression corresponding to substring extraction
     */
    @Nonnull
    Expression<String> substring(@Nonnull Expression<String> x,
                                 @Nonnull Expression<Integer> from,
                                 @Nonnull Expression<Integer> len);
	
    /**
     * Create an expression for substring extraction.
     * Extracts a substring of given length starting at the
     * specified position.
     * First position is 1.
     * @param x  string expression
     * @param from  start position 
     * @param len  length
     * @return expression corresponding to substring extraction
     */
    @Nonnull
    Expression<String> substring(@Nonnull Expression<String> x,
                                 int from, int len);
	
    /**
     * Used to specify how strings are trimmed.
     */
    enum Trimspec {

        /**
         * Trim from leading end.
         */
        LEADING,
 
        /**
         * Trim from trailing end.
         */
        TRAILING, 

        /**
         * Trim from both ends.
         */
        BOTH 
    }
	
    /**
     * Create expression to trim blanks from both ends of
     * a string.
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Nonnull
    Expression<String> trim(@Nonnull Expression<String> x);
	
    /**
     * Create expression to trim blanks from a string.
     * @param ts  trim specification
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Nonnull
    Expression<String> trim(@Nonnull Trimspec ts,
                            @Nonnull Expression<String> x);

    /**
     * Create expression to trim character from both ends of
     * a string.
     * @param t  expression for character to be trimmed
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Nonnull
    Expression<String> trim(@Nonnull Expression<Character> t,
                            @Nonnull Expression<String> x);

    /**
     * Create expression to trim character from a string.
     * @param ts  trim specification
     * @param t  expression for character to be trimmed
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Nonnull
    Expression<String> trim(@Nonnull Trimspec ts,
                            @Nonnull Expression<Character> t,
                            @Nonnull Expression<String> x);
	
    /**
     * Create expression to trim character from both ends of
     * a string.
     * @param t  character to be trimmed
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Nonnull
    Expression<String> trim(char t,
                            @Nonnull Expression<String> x);
	
    /**
     * Create expression to trim character from a string.
     * @param ts  trim specification
     * @param t  character to be trimmed
     * @param x  expression for string to trim
     * @return trim expression
     */
    @Nonnull
    Expression<String> trim(@Nonnull Trimspec ts, char t,
                            @Nonnull Expression<String> x);
	
    /**
     * Create expression for converting a string to lowercase.
     * @param x  string expression
     * @return expression to convert to lowercase
     */
    @Nonnull
    Expression<String> lower(@Nonnull Expression<String> x);
	
    /**
     * Create expression for converting a string to uppercase.
     * @param x  string expression
     * @return expression to convert to uppercase
     */
    @Nonnull
    Expression<String> upper(@Nonnull Expression<String> x);
	
    /**
     * Create expression to return length of a string.
     * @param x  string expression
     * @return length expression
     */
    @Nonnull
    Expression<Integer> length(@Nonnull Expression<String> x);

    /**
     * Create an expression for the leftmost substring of a string,
     * @param x  string expression
     * @param len  length of the substring to return
     * @return expression for the leftmost substring
     */
    @Nonnull
    Expression<String> left(@Nonnull Expression<String> x,
                            int len);

    /**
     * Create an expression for the rightmost substring of a string,
     * @param x  string expression
     * @param len  length of the substring to return
     * @return expression for the rightmost substring
     */
    @Nonnull
    Expression<String> right(@Nonnull Expression<String> x,
                             int len);

    /**
     * Create an expression for the leftmost substring of a string,
     * @param x  string expression
     * @param len  length of the substring to return
     * @return expression for the leftmost substring
     */
    @Nonnull
    Expression<String> left(@Nonnull Expression<String> x,
                            @Nonnull Expression<Integer> len);

    /**
     * Create an expression for the rightmost substring of a string,
     * @param x  string expression
     * @param len  length of the substring to return
     * @return expression for the rightmost substring
     */
    @Nonnull
    Expression<String> right(@Nonnull Expression<String> x,
                             @Nonnull Expression<Integer> len);

    /**
     * Create an expression replacing every occurrence of a substring
     * within a string.
     * @param x  string expression
     * @param substring  the literal substring to replace
     * @param replacement  the replacement string
     * @return expression for the resulting string
     */
    @Nonnull
    Expression<String> replace(@Nonnull Expression<String> x,
                               @Nonnull Expression<String> substring,
                               @Nonnull Expression<String> replacement);

    /**
     * Create an expression replacing every occurrence of a substring
     * within a string.
     * @param x  string expression
     * @param substring  the literal substring to replace
     * @param replacement  the replacement string
     * @return expression for the resulting string
     */
    @Nonnull
    Expression<String> replace(@Nonnull Expression<String> x,
                               @Nonnull String substring,
                               @Nonnull Expression<String> replacement);

    /**
     * Create an expression replacing every occurrence of a substring
     * within a string.
     * @param x  string expression
     * @param substring  the literal substring to replace
     * @param replacement  the replacement string
     * @return expression for the resulting string
     */
    @Nonnull
    Expression<String> replace(@Nonnull Expression<String> x,
                               @Nonnull Expression<String> substring,
                               @Nonnull String replacement);

    /**
     * Create an expression replacing every occurrence of a substring
     * within a string.
     * @param x  string expression
     * @param substring  the literal substring to replace
     * @param replacement  the replacement string
     * @return expression for the resulting string
     */
    @Nonnull
    Expression<String> replace(@Nonnull Expression<String> x,
                               @Nonnull String substring,
                               @Nonnull String replacement);


    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     * <p><strong>Warning:</strong> the order of the parameters
     * of this method is reversed compared to the corresponding
     * function in JPQL.
     * @param x  expression for string to be searched
     * @param pattern  expression for string to be located
     * @return expression corresponding to position
     */
    @Nonnull
    Expression<Integer> locate(@Nonnull Expression<String> x,
                               @Nonnull Expression<String> pattern);
	
    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     * <p><strong>Warning:</strong> the order of the parameters
     * of this method is reversed compared to the corresponding
     * function in JPQL.
     * @param x  expression for string to be searched
     * @param pattern  string to be located
     * @return expression corresponding to position
     */
    @Nonnull
    Expression<Integer> locate(@Nonnull Expression<String> x,
                               @Nonnull String pattern);

    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     * <p><strong>Warning:</strong> the order of the first two
     * parameters of this method is reversed compared to the
     * corresponding function in JPQL.
     * @param x  expression for string to be searched
     * @param pattern  expression for string to be located
     * @param from  expression for position at which to start search
     * @return expression corresponding to position
     */
    @Nonnull
    Expression<Integer> locate(@Nonnull Expression<String> x,
                               @Nonnull Expression<String> pattern,
                               @Nonnull Expression<Integer> from);

    /**
     * Create expression to locate the position of one string
     * within another, returning position of first character
     * if found.
     * The first position in a string is denoted by 1.  If the
     * string to be located is not found, 0 is returned.
     * <p><strong>Warning:</strong> the order of the first two
     * parameters of this method is reversed compared to the
     * corresponding function in JPQL.
     * @param x  expression for string to be searched
     * @param pattern  string to be located
     * @param from  position at which to start search
     * @return expression corresponding to position
     */	
    @Nonnull
    Expression<Integer> locate(@Nonnull Expression<String> x,
                               @Nonnull String pattern,
                               int from);
	

    // Date/time/timestamp functions:

    /**
     * Create expression to return current date.
     * @return expression for current date
     */
    @Nonnull
    Expression<java.sql.Date> currentDate();

    /**
     * Create expression to return current timestamp.
     * @return expression for current timestamp
     */	
    @Nonnull
    Expression<java.sql.Timestamp> currentTimestamp();

    /**
     * Create expression to return current time.
     * @return expression for current time
     */	
    @Nonnull
    Expression<java.sql.Time> currentTime();

    /**
     * Create expression to return current local date.
     * @return expression for current date
     */
    @Nonnull
    Expression<java.time.LocalDate> localDate();

    /**
     * Create expression to return current local datetime.
     * @return expression for current timestamp
     */
    @Nonnull
    Expression<java.time.LocalDateTime> localDateTime();

    /**
     * Create expression to return current local time.
     * @return expression for current time
     */
    @Nonnull
    Expression<java.time.LocalTime> localTime();

    /**
     * Create an expression that returns the value of a
     * field extracted from a date, time, or datetime.
     * @param field a temporal field type
     * @param temporal a date, time, or datetime
     * @param <N> the type of the extracted value
     * @param <T> the temporal type
     * @return expression for the value of the extracted field
     * @since 3.2
     */
    @Nonnull
    <N,T extends Temporal> Expression<N> extract(@Nonnull TemporalField<N,T> field,
                                                 @Nonnull Expression<T> temporal);
	

    //in builders:
	
    /**
     * Interface used to build in predicates.
     *
     * @param <T> the type of the tested expression
     */
    interface In<T> extends Predicate {

         /**
          * Return the expression to be tested against the
          * list of values.
          * @return expression
          */
         @Nonnull
         Expression<T> getExpression();
	
         /**
          * Add to list of values to be tested against.
          * @param value value
          * @return in predicate
          */
         @Nonnull
         In<T> value(T value);

         /**
          * Add to list of values to be tested against.
          * @param value expression
          * @return in predicate
          */
         @Nonnull
         In<T> value(@Nonnull Expression<? extends T> value);
     }
	
    /**
     * Create predicate to test whether given expression
     * is contained in a list of values.
     * @param  expression to be tested against list of values
     * @param <T> the type of the expression
     * @return  in predicate
     */
    @Nonnull
    <T> In<T> in(@Nonnull Expression<? extends T> expression);
	

    // coalesce, nullif:

    /**
     * Create an expression that returns null if all its arguments
     * evaluate to null, and the value of the first non-null argument
     * otherwise.
     * @param x expression
     * @param y expression
     * @param <Y> the type of the expression
     * @return coalesce expression
     */
    @Nonnull
    <Y> Expression<Y> coalesce(@Nonnull Expression<? extends Y> x,
                               @Nonnull Expression<? extends Y> y);

    /**
     * Create an expression that returns null if all its arguments
     * evaluate to null, and the value of the first non-null argument
     * otherwise.
     * @param x expression
     * @param y value
     * @param <Y> the type of the expression
     * @return coalesce expression
     */
    @Nonnull
    <Y> Expression<Y> coalesce(@Nonnull Expression<? extends Y> x,
                               Y y);
    
    /**
     * Create an expression that tests whether its argument are
     * equal, returning null if they are and the value of the
     * first expression if they are not.
     * @param x expression
     * @param y expression
     * @param <Y> the type of the expression
     * @return nullif expression
     */
    @Nonnull
    <Y> Expression<Y> nullif(@Nonnull Expression<Y> x,
                             @Nonnull Expression<?> y);

    /**
     * Create an expression that tests whether its argument are
     * equal, returning null if they are and the value of the
     * first expression if they are not.
     * @param x expression
     * @param y value
     * @param <Y> the type of the expression
     * @return nullif expression 
     */
    @Nonnull
    <Y> Expression<Y> nullif(@Nonnull Expression<Y> x,
                             Y y);


    // coalesce builder:

    /**
     * Interface used to build coalesce expressions.  
     * <p>
     * A coalesce expression is equivalent to a case expression
     * that returns null if all its arguments evaluate to null,
     * and the value of its first non-null argument otherwise.
     *
     * @param <T> the type of the coalesce expression
     */
    interface Coalesce<T> extends Expression<T> {

         /**
          * Add an argument to the coalesce expression.
          * @param value  value
          * @return coalesce expression
          */
         @Nonnull
         Coalesce<T> value(T value);

         /**
          * Add an argument to the coalesce expression.
          * @param value expression
          * @return coalesce expression
          */
         @Nonnull
         Coalesce<T> value(@Nonnull Expression<? extends T> value);
	}
	
    /**
     * Create a coalesce expression.
     * @param <T> the type of the coalesce expression
     * @return coalesce expression
     */
    @Nonnull
    <T> Coalesce<T> coalesce();


    //case builders:

    /**
     * Interface used to build simple case expressions.
     * Case conditions are evaluated in the order in which
     * they are specified.
     *
     * @param <C> the type of the case expression
     * @param <R> the result type of the case expression
     */
    interface SimpleCase<C,R> extends Expression<R> {

		/**
		 * Return the expression to be tested against the
		 * conditions.
		 * @return expression
		 */
		@Nonnull
		Expression<C> getExpression();

		/**
		 * Add a when/then clause to the case expression.
		 * @param condition  "when" condition
		 * @param result  "then" result value
		 * @return simple case expression
		 */
		@Nonnull
		SimpleCase<C, R> when(C condition,
                              R result);

		/**
		 * Add a when/then clause to the case expression.
		 * @param condition  "when" condition
		 * @param result  "then" result expression
		 * @return simple case expression
		 */
		@Nonnull
		SimpleCase<C, R> when(C condition,
                              @Nonnull Expression<? extends R> result);

		/**
		 * Add a when/then clause to the case expression.
		 * @param condition  "when" condition
		 * @param result  "then" result value
		 * @return simple case expression
		 */
		@Nonnull
		SimpleCase<C, R> when(@Nonnull Expression<? extends C> condition,
                              R result);

		/**
		 * Add a when/then clause to the case expression.
		 * @param condition  "when" condition
		 * @param result  "then" result expression
		 * @return simple case expression
		 */
		@Nonnull
		SimpleCase<C, R> when(@Nonnull Expression<? extends C> condition,
                              @Nonnull Expression<? extends R> result);

		/**
		 * Add an "else" clause to the case expression.
		 * @param result  "else" result
		 * @return expression
		 */
		@Nonnull
		Expression<R> otherwise(R result);

		/**
		 * Add an "else" clause to the case expression.
		 * @param result  "else" result expression
		 * @return expression
		 */
		@Nonnull
		Expression<R> otherwise(@Nonnull Expression<? extends R> result);
	}

    /**
     * Create a simple case expression.
     * @param expression  to be tested against the case conditions
     * @param type the type of the result of the case expression
     * @return simple case expression
     */
    @Nonnull
    <C, R> SimpleCase<C,R> selectCase(@Nonnull Expression<? extends C> expression,
                                      @Nonnull Class<R> type);

    /**
     * Create a simple case expression.
     * @param expression  to be tested against the case conditions
     * @param <C> the type of the case expression
     * @param <R> the result type of the case expression
     * @return simple case expression
     * @deprecated This operation declares an unconstrained type variable.
     *             Use {@link #selectCase(Expression,Class)} instead.
     */
    @Deprecated(since = "4.0")
    @Nonnull
    <C, R> SimpleCase<C,R> selectCase(@Nonnull Expression<? extends C> expression);

    /**
     * Interface used to build general case expressions.
     * Case conditions are evaluated in the order in which
     * they are specified.
     *
     * @param <R> the result type of the case expression
     */
    interface Case<R> extends Expression<R> {

		/**
		 * Add a when/then clause to the case expression.
		 * @param condition  "when" condition
		 * @param result  "then" result value
		 * @return general case expression
		 */
		@Nonnull
		Case<R> when(@Nonnull Expression<Boolean> condition,
                     R result);

		/**
		 * Add a when/then clause to the case expression.
		 * @param condition  "when" condition
		 * @param result  "then" result expression
		 * @return general case expression
		 */
		@Nonnull
		Case<R> when(@Nonnull Expression<Boolean> condition,
                     @Nonnull Expression<? extends R> result);

		/**
		 * Add an "else" clause to the case expression.
		 * @param result  "else" result
		 * @return expression
		 */
		@Nonnull
		Expression<R> otherwise(R result);

		/**
		 * Add an "else" clause to the case expression.
		 * @param result  "else" result expression
		 * @return expression
		 */
		@Nonnull
		Expression<R> otherwise(@Nonnull Expression<? extends R> result);
	}

    /**
     * Create a general case expression.
     * @param type the type of the result of the case expression
     * @return general case expression
     */
    @Nonnull
    <R> Case<R> selectCase(@Nonnull Class<R> type);

    /**
     * Create a general case expression.
     * @param <R> the result type of the case expression
     * @return general case expression
     * @deprecated This operation declares an unconstrained type variable.
     *             Use {@link #selectCase(Class)} instead.
     */
    @Deprecated(since = "4.0")
    @Nonnull
    <R> Case<R> selectCase();

    /**
     * Create an expression for the execution of the database
     * function with the given name.
     *
     * @apiNote This operation allows invocation of arbitrary
     * SQL functions. The persistence provider is permitted to
     * pass the given function name verbatim to the database,
     * with no additional validation or sanitization. Therefore,
     * the client must never pass unvalidated user input nor
     * any other untrusted string value to the first parameter
     * of this method.
     *
     * @param name  function name
     * @param type  expected result type
     * @param args  function arguments
     * @param <T> the function result type
     * @return expression
     */
    @Nonnull
    <T> Expression<T> function(@Nonnull String name, @Nonnull Class<T> type,
                               @Nonnull Expression<?>... args);


    // methods for downcasting:

    /**
     * Downcast Join object to the specified type.
     * @param join  Join object
     * @param type type to be downcast to
     * @param <X> the source type of the join
     * @param <T> the target type of the join
     * @param <V> the subtype to treat the join as
     * @return  Join object of the specified type
     * @since 2.1
     */
    @Nonnull
    <X, T, V extends T> Join<X, V> treat(@Nonnull Join<X, T> join,
                                         @Nonnull Class<V> type);

    /**
     * Downcast CollectionJoin object to the specified type.
     * @param join  CollectionJoin object
     * @param type type to be downcast to
     * @param <X> the source type of the join
     * @param <T> the element type of the join
     * @param <E> the subtype to treat the join as
     * @return  CollectionJoin object of the specified type
     * @since 2.1
     */
    @Nonnull
    <X, T, E extends T> CollectionJoin<X, E> treat(@Nonnull CollectionJoin<X, T> join,
                                                   @Nonnull Class<E> type);

    /**
     * Downcast SetJoin object to the specified type.
     * @param join  SetJoin object
     * @param type type to be downcast to
     * @param <X> the source type of the join
     * @param <T> the element type of the join
     * @param <E> the subtype to treat the join as
     * @return  SetJoin object of the specified type
     * @since 2.1
     */
    @Nonnull
    <X, T, E extends T> SetJoin<X, E> treat(@Nonnull SetJoin<X, T> join,
                                            @Nonnull Class<E> type);

    /**
     * Downcast ListJoin object to the specified type.
     * @param join  ListJoin object
     * @param type type to be downcast to
     * @param <X> the source type of the join
     * @param <T> the element type of the join
     * @param <E> the subtype to treat the join as
     * @return  ListJoin object of the specified type
     * @since 2.1
     */
    @Nonnull
    <X, T, E extends T> ListJoin<X, E> treat(@Nonnull ListJoin<X, T> join,
                                             @Nonnull Class<E> type);

    /**
     * Downcast MapJoin object to the specified type.
     * @param join  MapJoin object
     * @param type type to be downcast to
     * @param <X> the source type of the join
     * @param <K> the key type of the join
     * @param <T> the value type of the join
     * @param <V> the subtype to treat the join as
     * @return  MapJoin object of the specified type
     * @since 2.1
     */
    @Nonnull
    <X, K, T, V extends T> MapJoin<X, K, V> treat(@Nonnull MapJoin<X, K, T> join,
                                                  @Nonnull Class<V> type);


    /**
     * Downcast Path object to the specified type.
     * @param path  path
     * @param type type to be downcast to
     * @param <X> the type of the path
     * @param <T> the subtype to treat the path as
     * @return  Path object of the specified type
     * @since 2.1
     */
    @Nonnull
    <X, T extends X> Path<T> treat(@Nonnull Path<X> path,
                                   @Nonnull Class<T> type);

    /**
     * Downcast Root or Join to the specified type.
     * @param from  root or join
     * @param type type to be downcast to
     * @param <X> the source type
     * @param <Y> the target type
     * @param <T> the subtype to treat the root or join as
     * @return  Root or Join of the specified type
     * @since 4.0
     */
    @Nonnull
    <X, Y, T extends Y> From<X, T> treat(@Nonnull From<X, Y> from,
                                         @Nonnull Class<T> type);

    /**
     * Downcast Root object to the specified type.
     * @param root  root
     * @param type type to be downcast to
     * @param <X> the type of the root
     * @param <T> the subtype to treat the root as
     * @return  Root object of the specified type
     * @since 2.1
     */
    @Nonnull
    <X, T extends X> Root<T> treat(@Nonnull Root<X> root,
                                   @Nonnull Class<T> type);

    /**
     * Create a query which is the union of the given queries.
     * @param <T> the result type of the query
     * @return a new criteria query which returns the union of
     *         the results of the given queries
     * @since 3.2
     */
    @Nonnull
    <T> CriteriaSelect<T> union(@Nonnull CriteriaSelect<? extends T> left,
                                @Nonnull CriteriaSelect<? extends T> right);

    /**
     * Create a query which is the union of the given queries,
     * without elimination of duplicate results.
     * @param <T> the result type of the query
     * @return a new criteria query which returns the union of
     *         the results of the given queries
     * @since 3.2
     */
    @Nonnull
    <T> CriteriaSelect<T> unionAll(@Nonnull CriteriaSelect<? extends T> left,
                                   @Nonnull CriteriaSelect<? extends T> right);

    /**
     * Create a query which is the intersection of the given queries.
     * @param <T> the result type of the query
     * @return a new criteria query which returns the intersection of
     *         the results of the given queries
     * @since 3.2
     */
    @Nonnull
    <T> CriteriaSelect<T> intersect(@Nonnull CriteriaSelect<? super T> left,
                                    @Nonnull CriteriaSelect<? super T> right);

    /**
     * Create a query which is the intersection of the given queries,
     * without elimination of duplicate results.
     * @param <T> the result type of the query
     * @return a new criteria query which returns the intersection of
     *         the results of the given queries
     * @since 3.2
     */
    @Nonnull
    <T> CriteriaSelect<T> intersectAll(@Nonnull CriteriaSelect<? super T> left,
                                       @Nonnull CriteriaSelect<? super T> right);

    /**
     * Create a query by (setwise) subtraction of the second query
     * from the first query.
     * @param <T> the result type of the query
     * @return a new criteria query which returns the result of
     *         subtracting the results of the second query from the
     *         results of the first query
     * @since 3.2
     */
    @Nonnull
    <T> CriteriaSelect<T> except(@Nonnull CriteriaSelect<T> left,
                                 @Nonnull CriteriaSelect<?> right);

    /**
     * Create a query by (setwise) subtraction of the second query
     * from the first query, without elimination of duplicate results.
     * @param <T> the result type of the query
     * @return a new criteria query which returns the result of
     *         subtracting the results of the second query from the
     *         results of the first query
     * @since 3.2
     */
    @Nonnull
    <T> CriteriaSelect<T> exceptAll(@Nonnull CriteriaSelect<T> left,
                                    @Nonnull CriteriaSelect<?> right);
}
