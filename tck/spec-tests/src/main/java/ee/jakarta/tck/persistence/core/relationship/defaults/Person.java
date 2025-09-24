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

package ee.jakarta.tck.persistence.core.relationship.defaults;

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

/*
 * Person
 */

@Entity
public class Person implements java.io.Serializable {

	private int personid;

	private String firstName;

	private String lastName;

	private Project project;

	private Team team;

	private Collection<AnnualReview> annualReviews = new java.util.ArrayList<AnnualReview>();

	private Collection<Insurance> carriers = new java.util.ArrayList<Insurance>();

	private Collection<Project> projects = new java.util.ArrayList<Project>();

	public Person() {
	}

	public Person(int personid, String firstName, String lastName) {
		this.personid = personid;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	public int getPersonId() {
		return personid;
	}

	public void setPersonId(int personid) {
		this.personid = personid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// ===========================================================
	// getters and setters for the association fields

	/* Uni-Directional Single-Valued Many(Persons)ToOne(Team) */
	@ManyToOne
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	/* Bi-Directional OneProjectLead(Person)ToOneProject */
	@OneToOne
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	/* Bi-Directional ManyPersonsToManyProjects */
	@ManyToMany(mappedBy = "persons")
	public Collection<Project> getProjects() {
		return projects;
	}

	public void setProjects(Collection<Project> projects) {
		this.projects = projects;
	}

	/* Uni-Directional Single-Valued OnePersonToManyReviews */
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<AnnualReview> getAnnualReviews() {
		return annualReviews;
	}

	public void setAnnualReviews(Collection<AnnualReview> annualReviews) {
		this.annualReviews = annualReviews;
	}

	/* Uni-Directional Multi-Valued Relationship ManyInsuranceToManyPersons */
	@ManyToMany(cascade = CascadeType.ALL)
	public Collection<Insurance> getInsurance() {
		return carriers;
	}

	public void setInsurance(Collection<Insurance> carriers) {
		this.carriers = carriers;
	}

}
