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

package ee.jakarta.tck.persistence.core.annotations.access.field;

import ee.jakarta.tck.persistence.core.types.common.Grade;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "DATATYPES")
@Access(AccessType.FIELD)
public class DataTypes implements java.io.Serializable {

	@Id
	protected int id;

	@Basic(fetch = FetchType.EAGER)
	@Column(name = "BOOLEANDATA")
	@Access(AccessType.FIELD)
	protected boolean booleanData;

	@Column(name = "BYTEDATA")
	protected byte byteData;

	@Column(name = "CHARDATA")
	protected char characterData;

	@Column(name = "SHORTDATA")
	protected short shortData;

	@Column(name = "INTDATA")
	protected int intData;

	@Column(name = "LONGDATA")
	protected long longData;

	@Column(name = "DBLDATA")
	protected double doubleData;

	@Column(name = "FLOATDATA")
	protected float floatData;

	@Column(name = "ENUMSDATA")
	@Enumerated(EnumType.STRING)
	protected Grade enumData;

	@Column(name = "CHARARRAYDATA")
	protected char[] charArrayData;

	@Lob
	@Column(name = "BYTEARRAYDATA")
	protected byte[] byteArrayData;

	@Transient
	private int intData2;

	@Transient
	public byte trans;

	public DataTypes() {
	}

	public DataTypes(int id) {
		this.id = id;
	}

	public DataTypes(int id, boolean booleanData, byte byteData, char characterData, short shortData, int intData,
			long longData, double doubleData, float floatData, char[] charArrayData, byte[] byteArrayData) {
		this.id = id;
		this.booleanData = booleanData;
		this.byteData = byteData;
		this.characterData = characterData;
		this.shortData = shortData;
		this.intData = intData;
		this.longData = longData;
		this.doubleData = doubleData;
		this.floatData = floatData;
		this.charArrayData = charArrayData;
		this.byteArrayData = byteArrayData;

	}

	public DataTypes(int id, int intData2) {
		this.id = id;
		this.intData2 = intData2;
		// these values can not be null because of postgres
		this.characterData = ' ';
		byte[] bArray = { (byte) 32 };
		this.byteArrayData = bArray;
	}

	public DataTypes(int id, byte trans) {
		this.id = id;
		this.trans = trans;
		// these values can not be null because of postgres
		this.characterData = ' ';
		byte[] bArray = { (byte) 32 };
		this.byteArrayData = bArray;

	}

	public Integer getIdData() {
		return id;
	}

	public boolean isProperty() {
		return booleanData;
	}

	public void setBooleanData(boolean booleanData) {
		this.booleanData = booleanData;
	}

	public byte getByteData() {
		return byteData;
	}

	public void setByteData(byte byteData) {
		this.byteData = byteData;
	}

	public char getCharacterData() {
		return characterData;
	}

	public void setCharacterData(char characterData) {
		this.characterData = characterData;
	}

	public short getShortData() {
		return shortData;
	}

	public void setShortData(short shortData) {
		this.shortData = shortData;
	}

	public int getIntData() {
		return intData;
	}

	public void setIntData(int integerData) {
		this.intData = integerData;
	}

	public long getLongData() {
		return longData;
	}

	public void setLongData(long longData) {
		this.longData = longData;
	}

	public double getDoubleData() {
		return doubleData;
	}

	public void setDoubleData(double doubleData) {
		this.doubleData = doubleData;
	}

	public float getFloatData() {
		return floatData;
	}

	public void setFloatData(float floatData) {
		this.floatData = floatData;
	}

	public Grade getEnumData() {
		return enumData;
	}

	public void setEnumData(Grade enumData) {
		this.enumData = enumData;
	}

	public char[] getCharArrayData() {
		return charArrayData;
	}

	public void setCharArrayData(char[] charArrayData) {
		this.charArrayData = charArrayData;
	}

	public byte[] getByteArrayData() {
		return byteArrayData;
	}

	public void setByteArrayData(byte[] byteArrayData) {
		this.byteArrayData = byteArrayData;
	}

	@Access(AccessType.PROPERTY)
	@Column(name = "INTDATA2")
	public int getIntData2() {
		return intData2;
	}

	public void setIntData2(int intData2) {
		this.intData2 = intData2;
	}

	public byte getTransient() {
		return trans;
	}

	public void setTransient(byte trans) {
		this.trans = trans;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getIdData());
		result.append(", boolean: " + isProperty());
		result.append(", byte: " + getByteData());
		result.append(", char: " + getCharacterData());
		result.append(", short: " + getShortData());
		result.append(", integer: " + getIntData());
		result.append(", long: " + getLongData());
		result.append(", double: " + getDoubleData());
		result.append(", float: " + getFloatData());

		if (getCharArrayData() != null && getCharArrayData().length > 0) {
			StringBuilder sb = new StringBuilder(", CharData: [");
			for (int i = 0; i < charArrayData.length; i++) {
				sb.append(charArrayData[i]);
				if (i < charArrayData.length - 1) {
					sb.append(", ");
				}
			}
			sb.append("],");
			result.append(sb.toString());
		} else {
			result.append(", CharData: null");
		}
		if (getByteArrayData() != null && getByteArrayData().length > 0) {
			StringBuilder sb = new StringBuilder(", ByteData: [");
			for (int i = 0; i < byteArrayData.length; i++) {
				sb.append(byteArrayData[i]);
				if (i < byteArrayData.length - 1) {
					sb.append(", ");
				}
			}
			sb.append("],");
			result.append(sb.toString());
		} else {
			result.append(", ByteData: null");
		}

		result.append(", intData2: " + getIntData());

		result.append("]");
		return result.toString();
	}
}
