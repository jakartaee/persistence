/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.orderby;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/*
 * Customer
 */

@Entity
@Table(name = "CUST_TABLE")
@Access(AccessType.FIELD)
public class Customer2 implements java.io.Serializable {

	// Instance variables
	@Id
	@Column(name = "CUST_ID")
	private String id;

	@Column(name = "NAME")
	private String name;

	@ElementCollection
	@CollectionTable(name = "PHONES", joinColumns = @JoinColumn(name = "ID"))
	@Column(name = "PHONE_NUMBER")
	@OrderBy("DESC")
	private List<String> phones = new ArrayList<String>();

	public Customer2() {
	}

	public Customer2(String id) {
		this.id = id;
	}

	public Customer2(String id, String name) {
		this.id = id;
		this.name = name;
	}

	// ===========================================================
	// getters and setters for CMP fields

	public String getId() {
		return id;
	}

	public void setId(String v) {
		this.id = v;
	}

	public String getName() {
		return name;
	}

	public void setName(String v) {
		this.name = v;
	}

	public List<String> getPhones() {
		return this.phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public boolean equals(Object o) {
		Customer2 other;
		boolean same = true;

		if (!(o instanceof Customer2)) {
			return false;
		}
		other = (Customer2) o;

		same &= this.id.equals(other.id);

		return same;
	}

	public int hashCode() {
		int myHash;

		myHash = this.id.hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getName() != null) {
			result.append(", name: " + getName());
		} else {
			result.append(", name: null");
		}
		if (phones.size() > 0) {
			int size = phones.size();
			result.append(", phones[");
			int i = 0;
			for (String s : phones) {
				result.append(s);
				i++;
				if (i < size) {
					result.append(",");
				}
			}
			result.append("]");

		} else {
			result.append(", phones: null");
		}
		result.append("]");
		return result.toString();
	}
}
