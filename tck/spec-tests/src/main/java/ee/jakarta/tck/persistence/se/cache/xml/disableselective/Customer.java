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

package ee.jakarta.tck.persistence.se.cache.xml.disableselective;

public class Customer implements java.io.Serializable {
	// Instance variables
	private String id;

	private String name;

	public Customer() {
	}

	public Customer(String id, String name) {
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

	public boolean equals(Object o) {
		Customer other;
		boolean same = true;

		if (!(o instanceof Customer)) {
			return false;
		}
		other = (Customer) o;

		same &= this.id.equals(other.id);

		return same;
	}

	public int hashCode() {
		int myHash;

		myHash = this.id.hashCode() + this.getName().hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(",  name: " + getName());
		result.append("]");
		return result.toString();
	}
}
