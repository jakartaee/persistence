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

package ee.jakarta.tck.persistence.core.derivedid.ex1b;

import jakarta.persistence.Embeddable;

/**
 *
 * @author Raja Perumal
 */

@Embeddable
public class DID1bDependentId implements java.io.Serializable {

	String name;

	long empPK;

	public DID1bDependentId() {
	}

	public DID1bDependentId(String name, long emp) {
		this.name = name;
		this.empPK = emp;
	}

	public long getEmpPK() {
		return empPK;
	}

	public void setEmpPK(long emp) {
		this.empPK = emp;
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
		hash += (int) empPK;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof DID1bDependentId)) {
			return false;
		}
		DID1bDependentId other = (DID1bDependentId) object;
		if (this.empPK != other.empPK || this.name == null || !(other.name.equals(this.name))) {
			return false;
		}
		return true;
	}
}
