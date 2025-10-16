/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.convert;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "EMPLOYEE")
@Access(AccessType.FIELD)
public class Employee implements java.io.Serializable {
	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;

	@Convert(converter = SalaryConverter.class)
	@Column(name = "SALARY")
	private String salary;

	public Employee() {
	}

	public Employee(int id, String firstName, String lastName, String salary) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
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

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof Employee))
			return false;

		Employee o1 = (Employee) o;

		boolean result = false;

		if (this.getId() == o1.getId() && this.getFirstName().equals(o1.getFirstName())
				&& this.getLastName().equals(o1.getLastName()) && this.getSalary().equals(o1.getSalary())) {
			result = true;
		}

		return result;
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getFirstName().hashCode() + this.getLastName().hashCode()
				+ this.getSalary().hashCode();
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
}
