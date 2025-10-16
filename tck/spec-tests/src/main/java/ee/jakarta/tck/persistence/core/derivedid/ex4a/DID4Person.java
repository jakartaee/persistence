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

package ee.jakarta.tck.persistence.core.derivedid.ex4a;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class DID4Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String ssn;

	private String name;

	public DID4Person() {
	}

	public DID4Person(String ssn, String name) {
		this.ssn = ssn;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ssn != null ? ssn.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the ssn fields are not
		// set
		if (!(object instanceof DID4Person)) {
			return false;
		}
		DID4Person other = (DID4Person) object;
		if ((this.ssn == null && other.ssn != null) || (this.ssn != null && !this.ssn.equals(other.ssn))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "derivedssn4.DID4Person[ssn=" + ssn + "]";
	}
}
