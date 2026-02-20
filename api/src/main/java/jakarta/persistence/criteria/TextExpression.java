/*
 * Copyright (c) 2025, 2026 Contributors to the Eclipse Foundation
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
//     Christian Beikov - 4.0


package jakarta.persistence.criteria;

/**
 * Type for string query expressions.
 *
 * @since 4.0
 */
public interface TextExpression extends ComparableExpression<String> {

	// pattern matching

	/**
	 * Create a predicate for testing whether this expression
	 * satisfies the given pattern.
	 * @param pattern  string expression
	 * @return like predicate
	 * @see CriteriaBuilder#like(Expression, Expression)
	 */
	Predicate like(Expression<String> pattern);

	/**
	 * Create a predicate for testing whether this expression
	 * satisfies the given pattern.
	 * @param pattern  string
	 * @return like predicate
	 * @see CriteriaBuilder#like(Expression, String)
	 */
	Predicate like(String pattern);

	/**
	 * Create a predicate for testing whether this expression
	 * satisfies the given pattern.
	 * @param pattern  string expression
	 * @param escapeChar  escape character expression
	 * @return like predicate
	 * @see CriteriaBuilder#like(Expression, Expression, Expression)
	 */
	Predicate like(Expression<String> pattern, Expression<Character> escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * satisfies the given pattern.
	 * @param pattern  string expression
	 * @param escapeChar  escape character
	 * @return like predicate
	 * @see CriteriaBuilder#like(Expression, Expression, char)
	 */
	Predicate like(Expression<String> pattern, char escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * satisfies the given pattern.
	 * @param pattern  string
	 * @param escapeChar  escape character expression
	 * @return like predicate
	 * @see CriteriaBuilder#like(Expression, String, Expression)
	 */
	Predicate like(String pattern, Expression<Character> escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * satisfies the given pattern.
	 * @param pattern  string
	 * @param escapeChar  escape character
	 * @return like predicate
	 * @see CriteriaBuilder#like(Expression, String, char)
	 */
	Predicate like(String pattern, char escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * does not satisfy the given pattern.
	 * @param pattern  string expression
	 * @return not-like predicate
	 * @see CriteriaBuilder#notLike(Expression, Expression)
	 */
	Predicate notLike(Expression<String> pattern);

	/**
	 * Create a predicate for testing whether this expression
	 * does not satisfy the given pattern.
	 * @param pattern  string
	 * @return not-like predicate
	 * @see CriteriaBuilder#notLike(Expression, String)
	 */
	Predicate notLike(String pattern);

	/**
	 * Create a predicate for testing whether this expression
	 * does not satisfy the given pattern.
	 * @param pattern  string expression
	 * @param escapeChar  escape character expression
	 * @return not-like predicate
	 * @see CriteriaBuilder#notLike(Expression, Expression, Expression)
	 */
	Predicate notLike(Expression<String> pattern, Expression<Character> escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * does not satisfy the given pattern.
	 * @param pattern  string expression
	 * @param escapeChar  escape character
	 * @return not-like predicate
	 * @see CriteriaBuilder#notLike(Expression, Expression, char)
	 */
	Predicate notLike(Expression<String> pattern, char escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * does not satisfy the given pattern.
	 * @param pattern  string
	 * @param escapeChar  escape character expression
	 * @return not-like predicate
	 * @see CriteriaBuilder#notLike(Expression, String, Expression)
	 */
	Predicate notLike(String pattern, Expression<Character> escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * does not satisfy the given pattern.
	 * @param pattern  string
	 * @param escapeChar  escape character
	 * @return not-like predicate
	 * @see CriteriaBuilder#notLike(Expression, String, char)
	 */
	Predicate notLike(String pattern, char escapeChar);

	/**
	 * Create a predicate for testing whether this expression
	 * contains the given substring.
	 * @param substring  string
	 * @return contains predicate
	 */
	Predicate contains(String substring);

	/**
	 * Create a predicate for testing whether this expression
	 * does not contain the given substring.
	 * @param substring  string
	 * @return not-contains predicate
	 */
	Predicate notContains(String substring);

	/**
	 * Create a predicate for testing whether this expression
	 * starts with the given prefix.
	 * @param prefix  string
	 * @return starts-with predicate
	 */
	Predicate startsWith(String prefix);

	/**
	 * Create a predicate for testing whether this expression
	 * does not start with the given prefix.
	 * @param prefix  string
	 * @return not-starts-with predicate
	 */
	Predicate notStartsWith(String prefix);

	/**
	 * Create a predicate for testing whether this expression
	 * ends with the given suffix.
	 * @param suffix  string
	 * @return ends-with predicate
	 */
	Predicate endsWith(String suffix);

	/**
	 * Create a predicate for testing whether this expression
	 * does not end with the given suffix.
	 * @param suffix  string
	 * @return not-ends-with predicate
	 */
	Predicate notEndsWith(String suffix);

	//string functions

	/**
	 *  Create an expression for string concatenation.
	 *  @param y  string expression
	 *  @return expression corresponding to concatenation
	 * @see CriteriaBuilder#concat(Expression, Expression)
	 */
	TextExpression append(Expression<String> y);

	/**
	 *  Create an expression for string concatenation.
	 *  @param y  string
	 *  @return expression corresponding to concatenation
	 * @see CriteriaBuilder#concat(Expression, String)
	 */
	TextExpression append(String y);

	/**
	 *  Create an expression for string concatenation.
	 *  @param y  string expression
	 *  @return expression corresponding to concatenation
	 * @see CriteriaBuilder#concat(Expression, Expression)
	 */
	TextExpression prepend(Expression<String> y);

	/**
	 *  Create an expression for string concatenation.
	 *  @param y  string
	 *  @return expression corresponding to concatenation
	 * @see CriteriaBuilder#concat(Expression, String)
	 */
	TextExpression prepend(String y);

	/**
	 *  Create an expression for substring extraction.
	 *  Extracts a substring starting at the specified position
	 *  through to end of the string.
	 *  First position is 1.
	 *  @param from  start position expression
	 *  @return expression corresponding to substring extraction
	 * @see CriteriaBuilder#substring(Expression, Expression)
	 */
	TextExpression substring(Expression<Integer> from);

	/**
	 *  Create an expression for substring extraction.
	 *  Extracts a substring starting at the specified position
	 *  through to end of the string.
	 *  First position is 1.
	 *  @param from  start position
	 *  @return expression corresponding to substring extraction
	 * @see CriteriaBuilder#substring(Expression, int)
	 */
	TextExpression substring(int from);

	/**
	 *  Create an expression for substring extraction.
	 *  Extracts a substring of given length starting at the
	 *  specified position.
	 *  First position is 1.
	 *  @param from  start position expression
	 *  @param len  length expression
	 *  @return expression corresponding to substring extraction
	 * @see CriteriaBuilder#substring(Expression, Expression, Expression)
	 */
	TextExpression substring(Expression<Integer> from, Expression<Integer> len);

	/**
	 *  Create an expression for substring extraction.
	 *  Extracts a substring of given length starting at the
	 *  specified position.
	 *  First position is 1.
	 *  @param from  start position
	 *  @param len  length
	 *  @return expression corresponding to substring extraction
	 * @see CriteriaBuilder#substring(Expression, int, int)
	 */
	TextExpression substring(int from, int len);

	/**
	 * Create expression to trim blanks from both ends of
	 * this string.
	 * @return trim expression
	 * @see CriteriaBuilder#trim(Expression)
	 */
	TextExpression trim();

	/**
	 * Create expression to trim blanks from this string.
	 * @param ts  trim specification
	 * @return trim expression
	 * @see CriteriaBuilder#trim(CriteriaBuilder.Trimspec, Expression)
	 */
	TextExpression trim(CriteriaBuilder.Trimspec ts);

	/**
	 * Create expression to trim character from both ends of
	 * this string.
	 * @param t  expression for character to be trimmed
	 * @return trim expression
	 * @see CriteriaBuilder#trim(Expression, Expression)
	 */
	TextExpression trim(Expression<Character> t);

	/**
	 * Create expression to trim character from this string.
	 * @param ts  trim specification
	 * @param t  expression for character to be trimmed
	 * @return trim expression
	 * @see CriteriaBuilder#trim(CriteriaBuilder.Trimspec, Expression, Expression)
	 */
	TextExpression trim(CriteriaBuilder.Trimspec ts, Expression<Character> t);

	/**
	 * Create expression to trim character from both ends of
	 * this string.
	 * @param t  character to be trimmed
	 * @return trim expression
	 * @see CriteriaBuilder#trim(char, Expression)
	 */
	TextExpression trim(char t);

	/**
	 * Create expression to trim character from this string.
	 * @param ts  trim specification
	 * @param t  character to be trimmed
	 * @return trim expression
	 * @see CriteriaBuilder#trim(CriteriaBuilder.Trimspec, char, Expression)
	 */
	TextExpression trim(CriteriaBuilder.Trimspec ts, char t);

	/**
	 * Create expression for converting this string to lowercase.
	 * @return expression to convert to lowercase
	 * @see CriteriaBuilder#lower(Expression)
	 */
	TextExpression lower();

	/**
	 * Create expression for converting this string to uppercase.
	 * @return expression to convert to uppercase
	 * @see CriteriaBuilder#upper(Expression)
	 */
	TextExpression upper();

	/**
	 * Create expression to return length of this string.
	 * @return length expression
	 * @see CriteriaBuilder#length(Expression)
	 */
	Expression<Integer> length();

	/**
	 * Create an expression for the leftmost substring of this string.
	 * @param len  length of the substring to return
	 * @return expression for the leftmost substring
	 * @see CriteriaBuilder#left(Expression, int)
	 */
	TextExpression left(int len);

	/**
	 * Create an expression for the rightmost substring of this string.
	 * @param len  length of the substring to return
	 * @return expression for the rightmost substring
	 * @see CriteriaBuilder#right(Expression, int)
	 */
	TextExpression right(int len);

	/**
	 * Create an expression for the leftmost substring of this string.
	 * @param len  length of the substring to return
	 * @return expression for the leftmost substring
	 * @see CriteriaBuilder#left(Expression, Expression)
	 */
	TextExpression left(Expression<Integer> len);

	/**
	 * Create an expression for the rightmost substring of this string.
	 * @param len  length of the substring to return
	 * @return expression for the rightmost substring
	 * @see CriteriaBuilder#right(Expression, Expression)
	 */
	TextExpression right(Expression<Integer> len);

	/**
	 * Create an expression replacing every occurrence of a substring
	 * within this string.
	 * @param substring  the literal substring to replace
	 * @param replacement  the replacement string
	 * @return expression for the resulting string
	 * @see CriteriaBuilder#replace(Expression, Expression, Expression)
	 */
	TextExpression replace(Expression<String> substring, Expression<String> replacement);

	/**
	 * Create an expression replacing every occurrence of a substring
	 * within this string.
	 * @param substring  the literal substring to replace
	 * @param replacement  the replacement string
	 * @return expression for the resulting string
	 * @see CriteriaBuilder#replace(Expression, String, Expression)
	 */
	TextExpression replace(String substring, Expression<String> replacement);

	/**
	 * Create an expression replacing every occurrence of a substring
	 * within this string.
	 * @param substring  the literal substring to replace
	 * @param replacement  the replacement string
	 * @return expression for the resulting string
	 * @see CriteriaBuilder#replace(Expression, Expression, String)
	 */
	TextExpression replace(Expression<String> substring, String replacement);

	/**
	 * Create an expression replacing every occurrence of a substring
	 * within this string.
	 * @param substring  the literal substring to replace
	 * @param replacement  the replacement string
	 * @return expression for the resulting string
	 * @see CriteriaBuilder#replace(Expression, String, String)
	 */
	TextExpression replace(String substring, String replacement);


	/**
	 * Create expression to locate the position of one string
	 * within this, returning position of first character
	 * if found.
	 * The first position in a string is denoted by 1.  If the
	 * string to be located is not found, 0 is returned.
	 * <p><strong>Warning:</strong> the order of the parameters
	 * of this method is reversed compared to the corresponding
	 * function in JPQL.
	 * @param pattern  expression for string to be located
	 * @return expression corresponding to position
	 * @see CriteriaBuilder#locate(Expression, Expression)
	 */
	NumericExpression<Integer> locate(Expression<String> pattern);

	/**
	 * Create expression to locate the position of one string
	 * within this, returning position of first character
	 * if found.
	 * The first position in a string is denoted by 1.  If the
	 * string to be located is not found, 0 is returned.
	 * <p><strong>Warning:</strong> the order of the parameters
	 * of this method is reversed compared to the corresponding
	 * function in JPQL.
	 * @param pattern  string to be located
	 * @return expression corresponding to position
	 * @see CriteriaBuilder#locate(Expression, String)
	 */
	NumericExpression<Integer> locate(String pattern);

	/**
	 * Create expression to locate the position of one string
	 * within this, returning position of first character
	 * if found.
	 * The first position in a string is denoted by 1.  If the
	 * string to be located is not found, 0 is returned.
	 * <p><strong>Warning:</strong> the order of the first two
	 * parameters of this method is reversed compared to the
	 * corresponding function in JPQL.
	 * @param pattern  expression for string to be located
	 * @param from  expression for position at which to start search
	 * @return expression corresponding to position
	 * @see CriteriaBuilder#locate(Expression, Expression, Expression)
	 */
	NumericExpression<Integer> locate(Expression<String> pattern, Expression<Integer> from);

	/**
	 * Create expression to locate the position of one string
	 * within this, returning position of first character
	 * if found.
	 * The first position in a string is denoted by 1.  If the
	 * string to be located is not found, 0 is returned.
	 * <p><strong>Warning:</strong> the order of the first two
	 * parameters of this method is reversed compared to the
	 * corresponding function in JPQL.
	 * @param pattern  string to be located
	 * @param from  position at which to start search
	 * @return expression corresponding to position
	 * @see CriteriaBuilder#locate(Expression, String, int)
	 */
	NumericExpression<Integer> locate(String pattern, int from);

	// overrides

	@Override
	TextExpression coalesce(String y);

	@Override
	TextExpression coalesce(Expression<? extends String> y);

	@Override
	TextExpression nullif(String y);

	@Override
	TextExpression nullif(Expression<? extends String> y);
}
