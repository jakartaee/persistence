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

package ee.jakarta.tck.persistence.core.annotations.assocoverride;

import java.sql.Date;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/*
 * PartTimeEmployee entity extends an MappedSuperClass while overriding
 * Association.
 */
@Entity
@Table(name = "PARTTIMEEMPLOYEE")
@AssociationOverride(name = "address", joinColumns = @JoinColumn(name = "ADDRESS_ID"))
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
	@Column(name = "SALARY")
	public float getWage() {
		return wage;
	}

	public void setWage(float wage) {
		this.wage = wage;
	}
}
