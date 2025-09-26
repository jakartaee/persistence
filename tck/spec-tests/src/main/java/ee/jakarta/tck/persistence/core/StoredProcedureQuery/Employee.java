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

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
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

/*
 * Employee
 */

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "getemplastnamefrominout", procedureName = "GetEmpLastNameFromInOut", hints = {
				@QueryHint(name = "fooname", value = "barvalue"),
				@QueryHint(name = "fooname2", value = "barvalue2") }, parameters = {
						@StoredProcedureParameter(type = String.class, mode = ParameterMode.INOUT) }),
		@NamedStoredProcedureQuery(name = "get-id-firstname-lastname", procedureName = "GetEmpIdFNameLNameFromRS", parameters = {
				@StoredProcedureParameter(type = Integer.class, mode = ParameterMode.IN) }, resultSetMappings = "id-firstname-lastname"

		),
		@NamedStoredProcedureQuery(name = "get-id-firstname-lastname-refcursor", procedureName = "GetEmpIdFNameLNameFromRS", parameters = {
				@StoredProcedureParameter(type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(type = void.class, mode = ParameterMode.REF_CURSOR) }, resultSetMappings = "id-firstname-lastname"),
		@NamedStoredProcedureQuery(name = "tobeoverridden1", procedureName = "DOESNOTEXIST", parameters = {
				@StoredProcedureParameter(type = String.class, mode = ParameterMode.INOUT) }) })

@SqlResultSetMappings({
		@SqlResultSetMapping(name = "id-firstname-lastname", classes = {
				@ConstructorResult(targetClass = Employee.class, columns = {
						@ColumnResult(name = "ID", type = Integer.class), @ColumnResult(name = "FIRSTNAME"),
						@ColumnResult(name = "LASTNAME") }) }),
		@SqlResultSetMapping(name = "tobeoverridden2", entities = {
				@EntityResult(entityClass = ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee.class, fields = {
						@FieldResult(name = "foo", column = "FOO"), @FieldResult(name = "bar", column = "BAR") }) }) })

@Entity
@Table(name = "EMPLOYEE")
public class Employee extends EmployeeMappedSC implements java.io.Serializable, Comparable<Employee> {
	private static final long serialVersionUID = 20L;

	private int id;

	private String lastName;

	private Date hireDate;

	private float salary;

	public Employee() {
		super();
	}

	public Employee(int id, String firstName, String lastName, Date hireDate, float salary) {
		super(firstName);
		this.id = id;
		this.lastName = lastName;
		this.hireDate = hireDate;
		this.salary = salary;
	}

	public Employee(int id, String firstName, String lastName) {
		super(firstName);
		this.id = id;
		this.lastName = lastName;
	}

	public Employee(int id, String firstName) {
		super(firstName);
		this.id = id;
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

		if (this.getId() == o1.getId() && this.getSalary() == o1.getSalary() &&

				((this.getFirstName() == null && o1.getFirstName() == null)
						|| this.getFirstName().equals(o1.getFirstName()))
				&&

				((this.getLastName() == null && o1.getLastName() == null)
						|| this.getLastName().equals(o1.getLastName()))
				&&

				((this.getHireDate() == null && o1.getHireDate() == null)
						|| (this.getHireDate().getMonth() == o1.getHireDate().getMonth()
								&& this.getHireDate().getDay() == o1.getHireDate().getDay()
								&& this.getHireDate().getYear() == o1.getHireDate().getYear()))

		) {
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
		result.append(", salary: " + getSalary());

		result.append("]");
		return result.toString();
	}
}
