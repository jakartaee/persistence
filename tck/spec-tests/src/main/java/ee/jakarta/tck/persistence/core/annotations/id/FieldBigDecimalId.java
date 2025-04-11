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

import java.lang.System.Logger;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES3")
public class FieldBigDecimalId implements java.io.Serializable {

	private static final Logger logger = (Logger) System.getLogger(FieldBigDecimalId.class.getName());

	// ===========================================================
	// instance variables

	@Id
	@Column(name = "ID")
	protected BigDecimal id;

	@Column(name = "THEVALUE")
	private BigDecimal bigDecimal;

	// ===========================================================
	// constructors
	public FieldBigDecimalId() {
		logger.log(Logger.Level.TRACE, "Entity A no arg constructor");
	}

	public FieldBigDecimalId(BigDecimal id, BigDecimal bigDecimal) {

		this.id = id;
		this.bigDecimal = bigDecimal;

	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

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
