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

package ee.jakarta.tck.persistence.se.entityManager;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "get-id-firstname-lastname", procedureName = "GetEmpIdFNameLNameFromRS", hints = {
				@QueryHint(name = "fooname", value = "barvalue"),
				@QueryHint(name = "fooname2", value = "barvalue2") }, parameters = {
						@StoredProcedureParameter(type = Integer.class, mode = ParameterMode.IN) }, resultSetMappings = "id-firstname-lastname"

		),
		@NamedStoredProcedureQuery(name = "get-id-firstname-lastname-refcursor", procedureName = "GetEmpIdFNameLNameFromRS", hints = {
				@QueryHint(name = "fooname", value = "barvalue"),
				@QueryHint(name = "fooname2", value = "barvalue2") }, parameters = {
						@StoredProcedureParameter(type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(type = void.class, mode = ParameterMode.REF_CURSOR) }, resultSetMappings = "id-firstname-lastname") })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "id-firstname-lastname", classes = {
		@ConstructorResult(targetClass = ee.jakarta.tck.persistence.se.entityManager.Employee.class, columns = {
				@ColumnResult(name = "ID"), @ColumnResult(name = "FIRSTNAME"), @ColumnResult(name = "LASTNAME") }) }) })

@Entity
@Table(name = "EMPLOYEE")
public class Employee implements java.io.Serializable, Comparable<Employee> {
	private int id;

	private String firstName;

	private String lastName;

	private Date hireDate;

	private float salary;

	public Employee() {
	}

	public Employee(int id, String firstName, String lastName, Date hireDate, float salary) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = hireDate;
		this.salary = salary;
	}

	public Employee(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
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
				&& this.getLastName().equals(o1.getLastName()) && this.getHireDate().equals(o1.getHireDate())
				&& this.getSalary() == o1.getSalary()) {
			result = true;
		}

		return result;
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getFirstName().hashCode() + this.getLastName().hashCode()
				+ this.getHireDate().hashCode() + new Float(this.getSalary()).hashCode();
	}

	public int compareTo(Employee emp) {
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
		result.append(", country: " + getSalary());

		result.append("]");
		return result.toString();
	}
}
