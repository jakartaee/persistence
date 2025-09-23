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

package ee.jakarta.tck.persistence.core.derivedid.ex2b;

/**
 *
 * @author Raja Perumal
 */
public class DID2bEmployeeId implements java.io.Serializable {

	String firstName;

	String lastName;

	public DID2bEmployeeId() {
	}

	public DID2bEmployeeId(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DID2bEmployeeId other = (DID2bEmployeeId) obj;
		if ((this.firstName == null) ? (other.firstName != null) : !this.firstName.equals(other.firstName)) {
			return false;
		}
		if ((this.lastName == null) ? (other.lastName != null) : !this.lastName.equals(other.lastName)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 29 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
		hash = 29 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
		return hash;
	}
}
