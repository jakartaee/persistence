/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.repeatable.convert;

import jakarta.persistence.Basic;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "B_EMBEDDABLE")
public class B implements java.io.Serializable {

	private static final long serialVersionUID = 22L;

	// ===========================================================
	// instance variables
	@Id
	public String id;

	@Basic
	protected String name;

	@Basic
	protected Integer value;

	@Embedded

	@Convert(attributeName = "street", converter = DotConverter.class)
	@Convert(attributeName = "state", converter = NumberToStateConverter.class)
	protected Address address;

	// ===========================================================
	// constructors
	public B() {
		// logger.log(Logger.Level.TRACE,"Entity B no arg constructor");
	}

	public B(String id, String name, int value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public B(String id, String name, int value, Address addr) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.address = addr;
	}

	public String getBId() {
		return id;
	}

	public String getBName() {
		return name;
	}

	public void setBName(String bName) {
		this.name = bName;
	}

	public Integer getBValue() {
		return value;
	}

	public void setBValue(Integer value) {
		this.value = value;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final B other = (B) obj;
		if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
			return false;
		}
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		if (this.value != other.value) {
			return false;
		}
		if (this.address != other.address && (this.address == null || !this.address.equals(other.address))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 89 * hash + this.value;
		hash = 89 * hash + (this.address != null ? this.address.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getBId());
		result.append(", name: " + getBName());
		result.append(", value: " + getBValue());
		result.append("]");
		return result.toString();
	}

}
