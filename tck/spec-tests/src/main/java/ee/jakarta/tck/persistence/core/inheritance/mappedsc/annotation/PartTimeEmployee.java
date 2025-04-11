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

package ee.jakarta.tck.persistence.core.inheritance.mappedsc.annotation;

import java.sql.Date;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/*
 * PartTimeEmployee entity extends an MappedSuperClass while overriding
 * mapping information.
 */

@Entity
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "ID")),
		@AttributeOverride(name = "firstName", column = @Column(name = "FIRSTNAME")),
		@AttributeOverride(name = "lastName", column = @Column(name = "LASTNAME")),
		@AttributeOverride(name = "hireDate", column = @Column(name = "HIREDATE")) })
public class PartTimeEmployee extends Employee {

	private float wage;

	public PartTimeEmployee() {
	}

	public PartTimeEmployee(int id, String firstName, String lastName, Date hireDate, float salary) {
		super(id, firstName, lastName, hireDate);
		this.wage = wage;
	}

	// ===========================================================
	// getters and setters for the state fields

	@ManyToOne
	@JoinColumn(name = "FK_DEPT2")
	public Department getDepartment() {
		return department;
	}

	@Column(name = "SALARY")
	public float getWage() {
		return wage;
	}

	public void setWage(float wage) {
		this.wage = wage;
	}

}
