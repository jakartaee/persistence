/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.elementcollection;

import java.util.ArrayList;
import java.util.List;

/*
 * Customer
 */

public class CustomerXML implements java.io.Serializable {

	// Instance variables
	private String id;

	private String name;

	private List<String> phones = new ArrayList<String>();

	public CustomerXML() {
	}

	public CustomerXML(String id) {
		this.id = id;
	}

	public CustomerXML(String id, String name) {
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
		CustomerXML other;
		boolean same = true;

		if (!(o instanceof CustomerXML)) {
			return false;
		}
		other = (CustomerXML) o;

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
