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

package ee.jakarta.tck.persistence.core.derivedid.ex6a;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class DID6Person implements Serializable {

	@EmbeddedId
	DID6PersonId id;

	String ssn;

	public DID6Person(DID6PersonId pId, String ssn) {
		this.id = pId;
		this.ssn = ssn;
	}

	public DID6Person() {
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
		final DID6Person other = (DID6Person) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		if ((this.ssn == null) ? (other.ssn != null) : !this.ssn.equals(other.ssn)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 41 * hash + (this.ssn != null ? this.ssn.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "derivedssn6.DID6Person[ssn=" + ssn + "]";
	}
}
