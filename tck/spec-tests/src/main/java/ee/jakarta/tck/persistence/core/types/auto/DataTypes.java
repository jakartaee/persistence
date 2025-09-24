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

package ee.jakarta.tck.persistence.core.types.auto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES")
public class DataTypes implements java.io.Serializable {

	private Integer id;

	private Character characterData;

	private Short shortData;

	private Integer integerData;

	private Long longData;

	private Double doubleData;

	private Float floatData;

	public DataTypes() {
	}

	public DataTypes(Character characterData, Short shortData, Integer integerData, Long longData, Double doubleData,
			Float floatData) {
		this.characterData = characterData;
		this.shortData = shortData;
		this.integerData = integerData;
		this.longData = longData;
		this.doubleData = doubleData;
		this.floatData = floatData;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "CHARDATA")
	public Character getCharacterData() {
		return characterData;
	}

	public void setCharacterData(Character characterData) {
		this.characterData = characterData;
	}

	@Column(name = "SHORTDATA")
	public Short getShortData() {
		return shortData;
	}

	public void setShortData(Short shortData) {
		this.shortData = shortData;
	}

	@Column(name = "INTDATA")
	public Integer getIntegerData() {
		return integerData;
	}

	public void setIntegerData(Integer integerData) {
		this.integerData = integerData;
	}

	@Column(name = "LONGDATA")
	public Long getLongData() {
		return longData;
	}

	public void setLongData(Long longData) {
		this.longData = longData;
	}

	@Column(name = "DBLDATA")
	public Double getDoubleData() {
		return doubleData;
	}

	public void setDoubleData(Double doubleData) {
		this.doubleData = doubleData;
	}

	@Column(name = "FLOATDATA")
	public Float getFloatData() {
		return floatData;
	}

	public void setFloatData(Float floatData) {
		this.floatData = floatData;
	}

}
