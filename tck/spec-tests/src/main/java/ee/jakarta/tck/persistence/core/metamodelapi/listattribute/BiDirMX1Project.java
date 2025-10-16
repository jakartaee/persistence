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

package ee.jakarta.tck.persistence.core.metamodelapi.listattribute;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class BiDirMX1Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private Float budget;

	public BiDirMX1Project() {
	}

	public BiDirMX1Project(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	BiDirMX1Project(long l, String string, Float aFloat) {
		this.id = l;
		this.name = string;
		this.budget = aFloat;
	}

	@OneToMany(mappedBy = "project")
	private List<BiDirMX1Person> biDirMX1Persons;

	public List<BiDirMX1Person> getBiDirMX1Persons() {
		return biDirMX1Persons;
	}

	public void setBiDirMX1Persons(List<BiDirMX1Person> biDirMX1Persons) {
		this.biDirMX1Persons = biDirMX1Persons;
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
		if (!(object instanceof BiDirMX1Project)) {
			return false;
		}
		BiDirMX1Project other = (BiDirMX1Project) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "unimanyxone.UniMX1Project[id=" + id + "]";
	}
}
