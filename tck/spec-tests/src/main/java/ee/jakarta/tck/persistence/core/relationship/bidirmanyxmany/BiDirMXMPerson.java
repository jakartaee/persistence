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

package ee.jakarta.tck.persistence.core.relationship.bidirmanyxmany;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class BiDirMXMPerson implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String name;

	@JoinTable(name = "BIDIRMXMPERSON_BIDIRMXMPROJECT", joinColumns = @JoinColumn(name = "BIDIRMXMPerson_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "BIDIRMXMProject_ID", referencedColumnName = "ID"))
	@ManyToMany(cascade = CascadeType.ALL)
	private Collection<BiDirMXMProject> projects;

	public BiDirMXMPerson() {
	}

	public BiDirMXMPerson(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Collection<BiDirMXMProject> getProjects() {
		return projects;
	}

	public void setProjects(Collection<BiDirMXMProject> projects) {
		this.projects = projects;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof BiDirMXMPerson)) {
			return false;
		}
		BiDirMXMPerson other = (BiDirMXMPerson) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "bidirmanyxmany.bidirMXMPerson[id=" + id + "]";
	}
}
