/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.ordercolumn;

import java.util.List;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "DEPARTMENT")
@Access(AccessType.FIELD)
public class Department2 implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	@OneToMany(mappedBy = "department")
	@OrderColumn()
	private List<Employee2> employees;

	public Department2() {
	}

	public Department2(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Employee2> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee2> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", first: " + getName());
		result.append("]");
		return result.toString();
	}

	public boolean equals(Object o) {
		Department2 other;
		boolean result = false;

		if (!(o instanceof Department2)) {
			return result;
		}
		other = (Department2) o;

		if (this.getId() == other.getId() && this.getName().equals(other.getName())) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getId() + this.getName().hashCode();

		return myHash;
	}
}
