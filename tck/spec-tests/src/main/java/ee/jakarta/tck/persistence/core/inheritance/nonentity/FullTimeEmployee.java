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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.inheritance.nonentity;

import java.lang.System.Logger;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*
 * FullTimeEmployee
 */

@Entity
@DiscriminatorValue("EXEMPT")
public class FullTimeEmployee extends Employee {

	private static final Logger logger = (Logger) System.getLogger(FullTimeEmployee.class.getName());

	private float salary;

	public FullTimeEmployee() {
	}

	public FullTimeEmployee(int id, String firstName, String lastName, Date hireDate, float salary) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = hireDate;
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
