/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.entityManager;

import java.lang.System.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "A_BASIC")
@Cacheable(true)
public class A implements java.io.Serializable {

	private static final Logger logger = (Logger) System.getLogger(A.class.getName());

	// ===========================================================
	// instance variables

	@Id
	protected String id;

	@Basic(optional = true)
	protected String name;

	@Basic
	protected int value;

	@Basic
	protected Integer basicInteger;

	@Basic
	protected short basicShort;

	@Basic
	protected Short basicBigShort;

	@Basic
	protected float basicFloat;

	@Basic
	protected Float basicBigFloat;

	@Basic
	protected long basicLong;

	@Basic
	protected Long basicBigLong;

	@Basic
	protected double basicDouble;

	@Basic
	protected Double basicBigDouble;

	@Basic
	protected char basicChar;

	@Basic
	protected char[] basicCharArray;

	@Basic
	protected Character[] basicBigCharArray;

	@Basic
	protected byte[] basicByteArray;

	@Basic
	protected Byte[] basicBigByteArray;

	@Basic
	protected BigInteger basicBigInteger;

	@Basic
	protected BigDecimal basicBigDecimal;

	@Basic
	@Temporal(TemporalType.DATE)
	protected Date basicDate;

	@Basic
	protected Time basicTime;

	@Basic
	@Temporal(TemporalType.DATE)
	protected Calendar basicCalendar;

	@Basic
	protected Timestamp basicTimestamp;

	// ===========================================================
	// constructors
	public A() {
		logger.log(Logger.Level.TRACE, "Entity A no arg constructor");
	}

	public A(String id, String name, int value, Integer basicInteger, short basicShort, Short basicBigShort,
			float basicFloat, Float basicBigFloat, long basicLong, Long basicBigLong, double basicDouble,
			Double basicBigDouble, char basicChar, char[] basicCharArray, Character[] basicBigCharArray,
			byte[] basicByteArray, Byte[] basicBigByteArray, BigInteger basicBigInteger, BigDecimal basicBigDecimal,
			Date basicDate, Time basicTime, Timestamp basicTimestamp, Calendar basicCalendar) {

		this.id = id;
		this.name = name;
		this.value = value;
		this.basicInteger = basicInteger;
		this.basicShort = basicShort;
		this.basicBigShort = basicBigShort;
		this.basicFloat = basicFloat;
		this.basicBigFloat = basicBigFloat;
		this.basicLong = basicLong;
		this.basicBigLong = basicBigLong;
		this.basicDouble = basicDouble;
		this.basicBigDouble = basicBigDouble;
		this.basicChar = basicChar;
		this.basicCharArray = basicCharArray;
		this.basicBigCharArray = basicBigCharArray;
		this.basicByteArray = basicByteArray;
		this.basicBigByteArray = basicBigByteArray;
		this.basicBigInteger = basicBigInteger;
		this.basicBigDecimal = basicBigDecimal;
		this.basicDate = basicDate;
		this.basicTime = basicTime;
		this.basicCalendar = basicCalendar;
		this.basicTimestamp = basicTimestamp;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Double getBasicBigDouble() {
		return basicBigDouble;
	}

	public void setBasicBigDouble(Double basicBigDouble) {
		this.basicBigDouble = basicBigDouble;
	}

	public Long getBasicBigLong() {
		return basicBigLong;
	}

	public void setBasicBigLong(Long basicBigLong) {
		this.basicBigLong = basicBigLong;
	}

	public Short getBasicBigShort() {
		return basicBigShort;
	}

	public void setBasicBigShort(Short basicBigShort) {
		this.basicBigShort = basicBigShort;
	}

	public double getBasicDouble() {
		return basicDouble;
	}

	public void setBasicDouble(double basicDouble) {
		this.basicDouble = basicDouble;
	}

	public float getBasicFloat() {
		return basicFloat;
	}

	public void setBasicFloat(float basicFloat) {
		this.basicFloat = basicFloat;
	}

	public Integer getBasicInteger() {
		return basicInteger;
	}

	public void setBasicInteger(Integer basicInteger) {
		this.basicInteger = basicInteger;
	}

	public long getBasicLong() {
		return basicLong;
	}

	public void setBasicLong(long basicLong) {
		this.basicLong = basicLong;
	}

	public short getBasicShort() {
		return basicShort;
	}

	public void setBasicShort(short basicShort) {
		this.basicShort = basicShort;
	}

	public Byte[] getBasicBigByteArray() {
		return basicBigByteArray;
	}

	public void setBasicBigByteArray(Byte[] basicBigByteArray) {
		this.basicBigByteArray = basicBigByteArray;
	}

	public Character[] getBasicBigCharArray() {
		return basicBigCharArray;
	}

	public void setBasicBigCharArray(Character[] basicBigCharArray) {
		this.basicBigCharArray = basicBigCharArray;
	}

	public BigDecimal getBasicBigDecimal() {
		return basicBigDecimal;
	}

	public void setBasicBigDecimal(BigDecimal basicBigDecimal) {
		this.basicBigDecimal = basicBigDecimal;
	}

	public BigInteger getBasicBigInteger() {
		return basicBigInteger;
	}

	public void setBasicBigInteger(BigInteger basicBigInteger) {
		this.basicBigInteger = basicBigInteger;
	}

	public byte[] getBasicByteArray() {
		return basicByteArray;
	}

	public void setBasicByteArray(byte[] basicByteArray) {
		this.basicByteArray = basicByteArray;
	}

	public Calendar getBasicCalendar() {
		return basicCalendar;
	}

	public void setBasicCalendar(Calendar basicCalendar) {
		this.basicCalendar = basicCalendar;
	}

	public char getBasicChar() {
		return basicChar;
	}

	public void setBasicChar(char basicChar) {
		this.basicChar = basicChar;
	}

	public char[] getBasicCharArray() {
		return basicCharArray;
	}

	public void setBasicCharArray(char[] basicCharArray) {
		this.basicCharArray = basicCharArray;
	}

	public Date getBasicDate() {
		return basicDate;
	}

	public void setBasicDate(Date basicDate) {
		this.basicDate = basicDate;
	}

	public Time getBasicTime() {
		return basicTime;
	}

	public void setBasicTime(Time basicTime) {
		this.basicTime = basicTime;
	}

	public Timestamp getBasicTimestamp() {
		return basicTimestamp;
	}

	public void setBasicTimestamp(Timestamp basicTimestamp) {
		this.basicTimestamp = basicTimestamp;
	}
}
