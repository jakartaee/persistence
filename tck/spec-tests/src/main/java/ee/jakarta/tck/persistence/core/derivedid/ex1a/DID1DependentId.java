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

package ee.jakarta.tck.persistence.core.derivedid.ex1a;

/**
 *
 * @author Raja Perumal
 */
public class DID1DependentId implements java.io.Serializable {

	String name;

	long emp;

	public DID1DependentId() {
	}

	public DID1DependentId(String name, long emp) {
		this.name = name;
		this.emp = emp;
	}

	public long getEmp() {
		return emp;
	}

	public void setEmp(long emp) {
		this.emp = emp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) emp;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof DID1DependentId)) {
			return false;
		}
		DID1DependentId other = (DID1DependentId) object;
		if (this.emp != other.emp || this.name == null || !(other.name.equals(this.name))) {
			return false;
		}
		return true;
	}
}
