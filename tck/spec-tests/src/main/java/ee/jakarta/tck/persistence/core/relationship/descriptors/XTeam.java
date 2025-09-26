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

/*
 * XTeam
 */

public class XTeam implements java.io.Serializable {

	// Instance variables
	private int xteamid;

	private String xname;

	private XCompany xcompany;

	public XTeam() {
	}

	public XTeam(int xteamid, String xname) {
		this.xteamid = xteamid;
		this.xname = xname;
	}

	// ===========================================================
	// getters and setters for the state fields

	public int getXTeamId() {
		return xteamid;
	}

	public void setXTeamId(int xteamid) {
		this.xteamid = xteamid;
	}

	public String getXName() {
		return xname;
	}

	public void setXName(String xname) {
		this.xname = xname;
	}

	// ===========================================================
	// getters and setters for the association fields

	/* Bi-Directional Many(Teams)ToOne(Company) - Owner Team */
	public XCompany getXCompany() {
		return xcompany;
	}

	public void setXCompany(XCompany xcompany) {
		this.xcompany = xcompany;
	}

}
