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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EMPLOYEE")
public class Employee implements java.io.Serializable {

	private int id;

	private String firstName;

	private String lastName;

	private float salary;

	private Department department;

	public Employee() {
	}

	public Employee(int id, String firstName, String lastName, float salary) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
	}

	public Employee(int id, String firstName, String lastName, float salary, Department department) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
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

	@Column(name = "FIRSTNAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LASTNAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "SALARY")
	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	// ===========================================================
	// getters and setters for the association fields

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FK_DEPT")
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
		result.append("id: " + getId());
		result.append(", first: " + getFirstName());
		result.append(", last: " + getLastName());
		result.append(", salary: " + getSalary());
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
