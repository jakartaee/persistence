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

package ee.jakarta.tck.persistence.core.annotations.convert;

import java.io.Serializable;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/*
 * FullTimeEmployee entity extends an MappedSuperClass while overriding
 * mapping information.
 */

@Entity
@Table(name = "EMPLOYEE")
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "ID")),
		@AttributeOverride(name = "firstName", column = @Column(name = "FIRSTNAME")),
		@AttributeOverride(name = "lastName", column = @Column(name = "LASTNAME")) })
@Access(AccessType.PROPERTY)
public class FullTimeEmployee extends Employee2 implements Serializable {

	private String salary;

	public FullTimeEmployee() {
	}

	public FullTimeEmployee(int id, String firstName, char[] lastName, String salary) {
		super(id, firstName, lastName);
		this.salary = salary;
	}

	@Column(name = "SALARY")
	@Convert(converter = SalaryConverter.class)
	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", first: " + getFirstName());
		StringBuffer sb = new StringBuffer();
		for (char c : getLastName()) {
			sb.append(c);
		}
		result.append(", last: " + sb.toString());
		result.append(", salary: " + getSalary());
		result.append("]");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getFirstName().hashCode() + this.getLastName().hashCode()
				+ this.getSalary().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof FullTimeEmployee))
			return false;

		FullTimeEmployee o1 = (FullTimeEmployee) o;

		boolean result = false;

		StringBuffer sb = new StringBuffer();
		for (char c : this.getLastName()) {
			sb.append(c);
		}

		StringBuffer sb1 = new StringBuffer();
		for (char c : o1.getLastName()) {
			sb1.append(c);
		}

		if (this.getId() == o1.getId() && this.getFirstName().equals(o1.getFirstName())
				&& sb.toString().equals(sb1.toString()) && this.getSalary().equals(o1.getSalary())) {
			result = true;
		}

		return result;
	}
}
