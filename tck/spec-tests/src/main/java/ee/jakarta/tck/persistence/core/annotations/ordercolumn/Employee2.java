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

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EMPLOYEE")
@Access(AccessType.FIELD)
public class Employee2 implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;

	@Column(name = "SALARY")
	private float salary;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FK_DEPT")
	private Department2 department;

	public Employee2() {
	}

	public Employee2(int id, String firstName, String lastName, float salary) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
	}

	public Employee2(int id, String firstName, String lastName, float salary, Department2 department) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
		this.department = department;
	}

	// ===========================================================
	// getters and setters for the state fields

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	// ===========================================================
	// getters and setters for the association fields

	public Department2 getDepartment() {
		return department;
	}

	public void setDepartment(Department2 department) {
		this.department = department;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", first: " + getFirstName());
		result.append(", last: " + getLastName());
		result.append(", salary: " + getSalary());
		result.append("]");
		return result.toString();
	}

	public boolean equals(Object o) {
		Employee2 other;
		boolean result = false;

		if (!(o instanceof Employee2)) {
			return result;
		}
		other = (Employee2) o;

		if (this.getId() == other.getId() && this.getFirstName().equals(other.getFirstName())
				&& this.getLastName().equals(other.getLastName()) && this.getSalary() == other.getSalary()
				&& this.getDepartment().equals(other.getDepartment())) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getId() + this.getFirstName().hashCode() + this.getLastName().hashCode() + (int) this.getSalary();

		return myHash;
	}
}
