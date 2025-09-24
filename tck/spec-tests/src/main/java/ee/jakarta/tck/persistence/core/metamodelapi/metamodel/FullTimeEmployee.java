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

package ee.jakarta.tck.persistence.core.metamodelapi.metamodel;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
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
		@AttributeOverride(name = "lastName", column = @Column(name = "LASTNAME")),
		@AttributeOverride(name = "hireDate", column = @Column(name = "HIREDATE")) })
@Access(AccessType.PROPERTY)
public class FullTimeEmployee extends Employee implements Serializable {

	private float salary;

	public FullTimeEmployee() {
	}

	public FullTimeEmployee(int id, String firstName, String lastName, Date hireDate, float salary) {
		super(id, firstName, lastName, hireDate);
		this.salary = salary;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Column(name = "SALARY")
	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

}
