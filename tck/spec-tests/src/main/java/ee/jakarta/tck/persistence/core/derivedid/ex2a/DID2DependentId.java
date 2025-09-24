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

package ee.jakarta.tck.persistence.core.derivedid.ex2a;

/**
 *
 * @author Raja Perumal
 */
public class DID2DependentId implements java.io.Serializable {

	String name;

	DID2EmployeeId emp;

	public DID2DependentId() {
	}

	public DID2DependentId(String name, DID2EmployeeId emp) {
		this.name = name;
		this.emp = emp;
	}

	public DID2EmployeeId getEmp() {
		return emp;
	}

	public void setEmp(DID2EmployeeId emp) {
		this.emp = emp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DID2DependentId other = (DID2DependentId) obj;
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		if (this.emp != other.emp && (this.emp == null || !this.emp.equals(other.emp))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 13 * hash + (this.emp != null ? this.emp.hashCode() : 0);
		return hash;
	}
}
