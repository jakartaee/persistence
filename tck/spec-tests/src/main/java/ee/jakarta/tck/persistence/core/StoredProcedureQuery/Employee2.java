/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.StoredProcedureQuery;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "EMPLOYEE")
public class Employee2 implements java.io.Serializable, Comparable<Employee2> {
	private int id;

	private String firstName;

	private String lastName;

	private Calendar hireDate;

	private float salary;

	public Employee2() {
	}

	public Employee2(int id, String firstName, String lastName, Calendar hireDate, float salary) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = hireDate;
		this.salary = salary;
	}

	public Employee2(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Employee2(int id, String firstName) {
		this.id = id;
		this.firstName = firstName;
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
	@Temporal(TemporalType.DATE)
	public Calendar getHireDate() {
		return hireDate;
	}

	public void setHireDate(Calendar hireDate) {
		this.hireDate = hireDate;
	}

	@Column(name = "SALARY")
	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof Employee2))
			return false;

		Employee2 o1 = (Employee2) o;

		boolean result = false;

		if (this.getId() == o1.getId() && this.getSalary() == o1.getSalary() &&

				((this.getFirstName() == null && o1.getFirstName() == null)
						|| this.getFirstName().equals(o1.getFirstName()))
				&&

				((this.getLastName() == null && o1.getLastName() == null)
						|| this.getLastName().equals(o1.getLastName()))
				&&

				((this.getHireDate() == null && o1.getHireDate() == null)
						|| this.getHireDate().equals(o1.getHireDate()))) {
			result = true;
		}

		return result;
	}

	@Override
	public int hashCode() {
		int i = this.getId() + new Float(this.getSalary()).hashCode();
		if (this.getFirstName() != null) {
			i += this.getFirstName().hashCode();
		}
		if (this.getLastName() != null) {
			i += this.getLastName().hashCode();
		}
		if (this.getHireDate() != null) {
			i += this.getHireDate().hashCode();
		}
		return i;
	}

	public int compareTo(Employee2 emp) {
		int lastCmp = Integer.valueOf(getId()).compareTo(Integer.valueOf(emp.getId()));
		return (lastCmp != 0 ? lastCmp : Integer.valueOf(getId()).compareTo(Integer.valueOf(emp.getId())));
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getFirstName() != null) {
			result.append(", first: " + getFirstName());
		} else {
			result.append(", first: null");
		}
		if (getLastName() != null) {
			result.append(", last: " + getLastName());
		} else {
			result.append(", last: null");
		}
		if (getHireDate() != null) {
			result.append(", hire: " + getHireDate());
		} else {
			result.append(", hire: null");
		}
		result.append(", salary: " + getSalary());

		result.append("]");
		return result.toString();
	}
}
