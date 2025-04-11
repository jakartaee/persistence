/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.joincolumn;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;

@IdClass(ee.jakarta.tck.persistence.core.override.joincolumn.CubiclePK.class)
@Entity
public class Cubicle implements Serializable {

	@Id
	private Integer id;

	@Id
	private String location;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cubicle")
	private Set<Hardware> equipment = new HashSet();

	public Cubicle() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int hashCode() {
		int hash = 0;
		hash += (this.getId() != null ? this.getId().hashCode() : 0);
		hash += (this.getLocation() != null ? this.getLocation().hashCode() : 0);
		return hash;
	}

	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof Cubicle)) {
			return false;
		}
		Cubicle other = (Cubicle) object;
		if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
			return false;
		}
		if (this.getLocation() != other.getLocation()
				&& (this.getLocation() == null || !this.getLocation().equals(other.getLocation()))) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "ee.jakarta.tck.persistence.core.override.joincolumn." + "Cubicle[id=" + getId() + " - " + getLocation() + "]";
	}

	public Set<Hardware> getEquipment() {
		return equipment;
	}

	public void setEquipment(Set<Hardware> equipment) {
		this.equipment = equipment;
	}

	public void addEquipment(Hardware equipment) {
		this.equipment.add(equipment);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
