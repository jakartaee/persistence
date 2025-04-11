/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.repeatable.attroverride;

import java.sql.Date;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;

/*
 * Employee as mapped superclass, which can be concrete or abstract.
 * Mapping may be overriden by subclass entities with annotation or descriptor.
 */

@MappedSuperclass()
@Access(AccessType.PROPERTY)
public abstract class Employee extends AbstractPersonnel {

	private int id;

	private String firstName;

	private String lastName;

	private Date hireDate;

	/** the project this Employee leads */
	protected Project project;

	/** the department this Employee belongs to */
	protected Department department;

	protected Employee() {
	}

	protected Employee(int id, String firstName, String lastName, Date hireDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = (Date) hireDate.clone();
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	@Column(name = "IDxx")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "FIRSTNAMExx")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LASTNAMExx")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "HIREDATExx")
	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
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
