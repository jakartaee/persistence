/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.lock.query;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * Employee
 */

@Entity
@Table(name = "EMPLOYEE")
public class Employee implements java.io.Serializable {

	private int id;

	private String firstName;

	private String lastName;

	private Date hireDate;

	private float salary;

	private Department department;

	private Insurance insurance;

	public Employee() {
	}

	public Employee(int id, String firstName, String lastName, Date hireDate, float salary) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = hireDate;
		this.salary = salary;
	}

	public Employee(int id, String firstName, String lastName, Date hireDate, float salary, Department department,
			Insurance insurance) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = hireDate;
		this.salary = salary;
		this.department = department;
		this.insurance = insurance;
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

	@Column(name = "HIREDATE")
	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
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

	@ManyToOne
	@JoinColumn(name = "FK_DEPT")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@ManyToOne
	@JoinColumn(name = "FK_INS")
	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", first: " + getFirstName());
		result.append(", last: " + getLastName());
		result.append(", hireDate: " + getHireDate());
		result.append(", salary: " + getSalary());
		result.append(", Dept_id: " + getDepartment().getId());
		result.append(", Ins_id: " + getInsurance().getId());
		result.append("]");
		return result.toString();
	}
}
