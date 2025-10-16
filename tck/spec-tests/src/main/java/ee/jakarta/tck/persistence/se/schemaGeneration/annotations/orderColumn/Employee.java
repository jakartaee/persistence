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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.orderColumn;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEMAGENEMP")
public class Employee implements java.io.Serializable {

	private int empId;

	private Department department;

	public Employee() {
	}

	public Employee(int id) {
		this.empId = id;
	}

	public Employee(int id, Department department) {
		this.empId = id;
		this.department = department;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int id) {
		this.empId = id;
	}

	// ===========================================================
	// getters and setters for the association fields

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FK_DEPT", foreignKey = @ForeignKey(name = "MYCONSTRANT", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (FK_DEPT) REFERENCES SCHEMAGENDEPT (DEPTID)"))
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getEmpId());
		result.append("]");
		return result.toString();
	}

	public boolean equals(Object o) {
		Employee other;
		boolean result = false;

		if (!(o instanceof Employee)) {
			return result;
		}
		other = (Employee) o;

		if (this.getEmpId() == other.getEmpId() && this.getDepartment().equals(other.getDepartment())) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getEmpId();

		return myHash;
	}
}
