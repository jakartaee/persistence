/*
 * Copyright (c) 2014, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkeyenumerated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EMP_MAPKEYCOL2")
public class Employee4 implements java.io.Serializable {

	private int id;

	private String lastName;

	private Department4 department;

	public Employee4() {
	}

	public Employee4(int id, String lastName) {
		this.id = id;
		this.lastName = lastName;
	}

	public Employee4(int id, String lastName, Department4 department) {
		this.id = id;
		this.lastName = lastName;
		this.department = department;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "LASTNAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// ===========================================================
	// getters and setters for the association fields

	@ManyToOne
	@JoinColumn(name = "FK_DEPT5")
	public Department4 getDepartment() {
		return department;
	}

	public void setDepartment(Department4 department) {
		this.department = department;
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getLastName().hashCode();
	}

	public boolean equals(Object o) {
		Employee4 other;
		boolean same = true;

		if (!(o instanceof Employee4)) {
			return false;
		}
		other = (Employee4) o;

		same &= (this.id == other.id);

		return same;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getLastName() != null) {
			result.append(", last: " + getLastName());
		} else {
			result.append(", last: null");
		}
		result.append("]");
		return result.toString();
	}
}
