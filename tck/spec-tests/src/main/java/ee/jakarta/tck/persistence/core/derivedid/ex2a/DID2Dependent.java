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

package ee.jakarta.tck.persistence.core.derivedid.ex2a;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author Raja Perumal
 */
@Entity
@IdClass(DID2DependentId.class)
public class DID2Dependent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	String name;

	@Id
	@JoinColumns({ @JoinColumn(name = "FIRSTNAME", referencedColumnName = "firstname"),
			@JoinColumn(name = "LASTNAME", referencedColumnName = "lastname") })
	@ManyToOne
	DID2Employee emp;

	public DID2Dependent() {
	}

	public DID2Dependent(DID2DependentId dId, DID2Employee emp) {
		this.name = dId.getName();
		this.emp = emp;
	}

	public DID2Dependent(String name, DID2Employee emp) {
		this.name = name;
		this.emp = emp;
	}

	public DID2Employee getEmp() {
		return emp;
	}

	public void setEmp(DID2Employee emp) {
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
		hash += (name != null ? name.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof DID2Dependent)) {
			return false;
		}
		DID2Dependent other = (DID2Dependent) object;
		if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "derivedid2.DID2Dependent[name=" + name + "]";
	}
}
