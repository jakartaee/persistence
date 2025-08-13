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

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class DID2bDependent implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	DID2bDependentId id;

	@MapsId("empPK")
	@JoinColumns({ @JoinColumn(name = "FIRSTNAME", referencedColumnName = "firstname"),
			@JoinColumn(name = "LASTNAME", referencedColumnName = "lastname") })
	@ManyToOne
	DID2bEmployee emp;

	public DID2bDependent() {
	}

	public DID2bDependent(DID2bDependentId dId, DID2bEmployee emp) {
		this.id = dId;
		this.emp = emp;

	}

	public DID2bDependent(String name, DID2bEmployee emp) {
		this.id.name = name;
		this.emp = emp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DID2bDependent other = (DID2bDependent) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
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
		hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 59 * hash + (this.emp != null ? this.emp.hashCode() : 0);
		return hash;
	}

	public DID2bEmployee getEmp() {
		return emp;
	}

	public void setEmp(DID2bEmployee emp) {
		this.emp = emp;
	}

	public String getName() {
		return this.id.name;
	}

	public void setName(String name) {
		this.id.name = name;
	}

}
