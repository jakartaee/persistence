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

package ee.jakarta.tck.persistence.core.derivedid.ex3a;

/**
 *
 * @author Raja Perumal
 */
public class DID3DependentId implements java.io.Serializable {

	String name2;

	DID3EmployeeId emp;

	public DID3DependentId() {
	}

	public DID3DependentId(String name2, DID3EmployeeId emp) {
		this.name2 = name2;
		this.emp = emp;
	}

	public DID3EmployeeId getEmp() {
		return emp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DID3DependentId other = (DID3DependentId) obj;
		if ((this.name2 == null) ? (other.name2 != null) : !this.name2.equals(other.name2)) {
			return false;
		}
		if (this.emp != other.emp && (this.emp == null || !this.emp.equals(other.emp))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + (this.name2 != null ? this.name2.hashCode() : 0);
		return hash;
	}

	public void setEmp(DID3EmployeeId emp) {
		this.emp = emp;
	}

	public String getName() {
		return name2;
	}

	public void setName(String name2) {
		this.name2 = name2;
	}

}
