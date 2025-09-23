/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.types.field;

import ee.jakarta.tck.persistence.core.types.common.Grade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES")
public class DataTypes implements java.io.Serializable {

	@Id
	protected int id;

	@Column(name = "BOOLEANDATA")
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

	public Integer getIdData() {
		return id;
	}

	public boolean getBooleanData() {
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

	public void setIntData(int intData) {
		this.intData = intData;
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

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getIdData());
		result.append(", boolean: " + getBooleanData());
		result.append(", byte: " + getByteData());
		result.append(", char: " + getCharacterData());
		result.append(", short: " + getShortData());
		result.append(", int: " + getIntData());
		result.append(", long: " + getLongData());
		result.append(", double: " + getDoubleData());
		result.append(", float: " + getFloatData());

		if (getCharArrayData() != null && getCharArrayData().length > 0) {
			StringBuilder sb = new StringBuilder(", CharArrayData: [");
			for (int i = 0; i < charArrayData.length; i++) {
				sb.append(charArrayData[i]);
				if (i < charArrayData.length - 1) {
					sb.append(", ");
				}
			}
			sb.append("],");
			result.append(sb.toString());
		} else {
			result.append(", CharArrayData: null");
		}
		if (getByteArrayData() != null && getByteArrayData().length > 0) {
			StringBuilder sb = new StringBuilder(", ByteArrayData: [");
			for (int i = 0; i < byteArrayData.length; i++) {
				sb.append(byteArrayData[i]);
				if (i < byteArrayData.length - 1) {
					sb.append(", ");
				}
			}
			sb.append("],");
			result.append(sb.toString());
		} else {
			result.append(", ByteArrayData: null");
		}
		result.append("]");
		return result.toString();
	}
}
