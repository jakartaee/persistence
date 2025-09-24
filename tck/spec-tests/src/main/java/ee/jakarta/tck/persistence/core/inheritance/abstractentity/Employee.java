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

package ee.jakarta.tck.persistence.core.inheritance.abstractentity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * Employee
 */

@Entity
@Table(name = "EMPLOYEE")
@DiscriminatorColumn(name = "STATUS", discriminatorType = DiscriminatorType.STRING)
public abstract class Employee extends AbstractPersonnel implements java.io.Serializable {

	protected int id;

	protected String firstName;

	protected String lastName;

	protected Date hireDate;

	/** the project this Employee leads */
	protected Project project;

	/** the department this Employee belongs to */
	protected Department department;

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

	@ManyToOne
	@JoinColumn(name = "FK_DEPT")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@OneToOne
	@JoinColumn(name = "FK_PROJECT")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
