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

package ee.jakarta.tck.persistence.core.annotations.id;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES3")
public class PropertyBigDecimalId implements java.io.Serializable {

	// ===========================================================
	// instance variables

	protected BigDecimal id;

	private BigDecimal bigDecimal;

	// ===========================================================
	// constructors
	public PropertyBigDecimalId() {
	}

	public PropertyBigDecimalId(BigDecimal id, BigDecimal bigDecimal) {

		this.id = id;
		this.bigDecimal = bigDecimal;

	}

	@Id
	@Column(name = "ID")
	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "THEVALUE")
	public BigDecimal getBigDecimal() {
		return this.bigDecimal;
	}

	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getBigDecimal() != null) {
			result.append(", BigDecimal: " + getBigDecimal());
		} else {
			result.append(", BigDecimal: null");
		}
		result.append("]");
		return result.toString();
	}
}
