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

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES3")
public class PropertyBigIntegerId implements java.io.Serializable {

	// ===========================================================
	// instance variables

	protected BigInteger id;

	private BigInteger bigInteger;

	// ===========================================================
	// constructors
	public PropertyBigIntegerId() {
	}

	public PropertyBigIntegerId(BigInteger id, BigInteger bigInteger) {

		this.id = id;
		this.bigInteger = bigInteger;

	}

	@Id
	@Column(name = "ID")
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	@Column(name = "THEVALUE")
	public BigInteger getBigInteger() {
		return this.bigInteger;
	}

	public void setBigInteger(BigInteger bigInteger) {
		this.bigInteger = bigInteger;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getBigInteger() != null) {
			result.append(", BigInteger: " + getBigInteger());
		} else {
			result.append(", BigInteger: null");
		}
		result.append("]");
		return result.toString();
	}
}
