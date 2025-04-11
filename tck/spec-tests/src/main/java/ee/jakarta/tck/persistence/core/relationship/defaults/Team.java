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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/*
 * Team
 */

@Entity
public class Team implements java.io.Serializable {

	// Instance variables
	private int teamid;

	private String name;

	private Company company;

	public Team() {
	}

	public Team(int teamid, String name) {
		this.teamid = teamid;
		this.name = name;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	public int getTeamId() {
		return teamid;
	}

	public void setTeamId(int teamid) {
		this.teamid = teamid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ===========================================================
	// getters and setters for the association fields

	/* Bi-Directional Many(Teams)ToOne(Company) - Owner Team */
	@ManyToOne(cascade = CascadeType.REMOVE)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
