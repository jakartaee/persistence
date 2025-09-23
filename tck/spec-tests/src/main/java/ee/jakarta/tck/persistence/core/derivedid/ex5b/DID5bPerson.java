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

package ee.jakarta.tck.persistence.core.derivedid.ex5b;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

/**
 *
 * @author Raja Perumal
 */
@Entity
@IdClass(DID5bPersonId.class)
public class DID5bPerson implements Serializable {

	@Id
	String firstName;

	@Id
	String lastName;

	String ssn;

	public DID5bPerson(String firstName, String lastName, String ssn) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
	}

	public DID5bPerson(DID5bPersonId pId, String ssn) {
		this.firstName = pId.getFirstName();
		this.lastName = pId.getLastName();
		this.ssn = ssn;
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

	public DID5bPerson() {
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DID5bPerson other = (DID5bPerson) obj;
		if ((this.firstName == null) ? (other.firstName != null) : !this.firstName.equals(other.firstName)) {
			return false;
		}
		if ((this.lastName == null) ? (other.lastName != null) : !this.lastName.equals(other.lastName)) {
			return false;
		}
		if ((this.ssn == null) ? (other.ssn != null) : !this.ssn.equals(other.ssn)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
		hash = 53 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
		hash = 53 * hash + (this.ssn != null ? this.ssn.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "DID5bPerson[ssn=" + ssn + "]";
	}
}
