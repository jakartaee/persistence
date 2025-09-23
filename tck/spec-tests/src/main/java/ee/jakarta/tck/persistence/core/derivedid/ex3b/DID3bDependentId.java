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

package ee.jakarta.tck.persistence.core.derivedid.ex3b;

import jakarta.persistence.Embeddable;

/**
 *
 * @author Raja Perumal
 */
@Embeddable
public class DID3bDependentId implements java.io.Serializable {

	String name;

	DID3bEmployeeId empPK;

	public DID3bDependentId() {
	}

	public DID3bDependentId(String name, DID3bEmployeeId empPK) {
		this.name = name;
		this.empPK = empPK;
	}

	public DID3bEmployeeId getEmpPK() {
		return empPK;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DID3bDependentId other = (DID3bDependentId) obj;
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		if (this.empPK != other.empPK && (this.empPK == null || !this.empPK.equals(other.empPK))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

	public void setEmpPK(DID3bEmployeeId empPK) {
		this.empPK = empPK;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
