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

package ee.jakarta.tck.persistence.core.relationship.annotations;

import java.math.BigDecimal;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

/*
 * Project
 */

@Entity
public class Project implements java.io.Serializable {

	// Instance Variables
	private long projId;

	private String name;

	private BigDecimal budget;

	private Person projectLead;

	private Collection<Person> persons = new java.util.ArrayList<Person>();

	public Project() {
	}

	public Project(long projId, String name, BigDecimal budget) {
		this.projId = projId;
		this.name = name;
		this.budget = budget;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	public long getProjId() {
		return projId;
	}

	public void setProjId(long projId) {
		this.projId = projId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	// ===========================================================
	// getters and setters for the association fields

	/* Bi-Directional OneProjectLeadToOnePerson */
	@OneToOne(mappedBy = "project")
	public Person getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(Person projectLead) {
		this.projectLead = projectLead;
	}

	/* Bi-Directional ManyProjectsToManyPersons */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "PROJECT_PERSON", joinColumns = @JoinColumn(name = "projects_PROJID", referencedColumnName = "PROJID"), inverseJoinColumns = @JoinColumn(name = "persons_PERSONID", referencedColumnName = "PERSONID"))
	public Collection<Person> getPersons() {
		return persons;
	}

	public void setPersons(Collection<Person> persons) {
		this.persons = persons;
	}

}
