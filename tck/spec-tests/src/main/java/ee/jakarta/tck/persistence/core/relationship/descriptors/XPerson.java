/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.relationship.descriptors;

import java.util.Collection;

/*
 * XPerson
 */

public class XPerson implements java.io.Serializable {

	private int xPersonId;

	private String firstName;

	private String lastName;

	private XProject xProject;

	private XTeam xTeam;

	private Collection<XAnnualReview> xAnnualReviews = new java.util.ArrayList<XAnnualReview>();

	private Collection<XInsurance> xCarriers = new java.util.ArrayList<XInsurance>();

	private Collection<XProject> xProjects = new java.util.ArrayList<XProject>();

	public XPerson() {
	}

	public XPerson(int xPersonId, String firstName, String lastName) {
		this.xPersonId = xPersonId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// ===========================================================
	// getters and setters for the state fields

	public int getXPersonId() {
		return xPersonId;
	}

	public void setXPersonId(int xPersonId) {
		this.xPersonId = xPersonId;
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
	public XTeam getXTeam() {
		return xTeam;
	}

	public void setXTeam(XTeam xTeam) {
		this.xTeam = xTeam;
	}

	/* Bi-Directional OneProjectLead(Person)ToOneProject */
	public XProject getXProject() {
		return xProject;
	}

	public void setXProject(XProject xProject) {
		this.xProject = xProject;
	}

	/* Bi-Directional ManyPersonsToManyProjects */
	public Collection<XProject> getXProjects() {
		return xProjects;
	}

	public void setXProjects(Collection<XProject> xProjects) {
		this.xProjects = xProjects;
	}

	/* Uni-Directional Single-Valued OnePersonToManyReviews */
	public Collection<XAnnualReview> getXAnnualReviews() {
		return xAnnualReviews;
	}

	public void setXAnnualReviews(Collection<XAnnualReview> xAnnualReviews) {
		this.xAnnualReviews = xAnnualReviews;
	}

	/* Uni-Directional Multi-Valued Relationship ManyInsuranceToManyPersons */
	public Collection<XInsurance> getXInsurance() {
		return xCarriers;
	}

	public void setXInsurance(Collection<XInsurance> xCarriers) {
		this.xCarriers = xCarriers;
	}

}
