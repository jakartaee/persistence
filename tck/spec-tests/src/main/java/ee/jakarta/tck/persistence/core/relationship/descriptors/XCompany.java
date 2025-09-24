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

package ee.jakarta.tck.persistence.core.relationship.descriptors;

import java.lang.System.Logger;
import java.util.Collection;

/*
 * XCompany
 */

public class XCompany implements java.io.Serializable {

	private static final Logger logger = (Logger) System.getLogger(XCompany.class.getName());

	private long xCompanyId;

	private String xName;

	private XAddress xAddress;

	private Collection<XTeam> xTeams = new java.util.ArrayList<XTeam>();

	public XCompany() {
		logger.log(Logger.Level.TRACE, "Company no arg constructor");
	}

	public XCompany(long xCompanyId, String xName) {
		this.xCompanyId = xCompanyId;
		this.xName = xName;
	}

	public XCompany(long xCompanyId, String xName, XAddress xAddress) {
		this.xCompanyId = xCompanyId;
		this.xName = xName;
		this.xAddress = xAddress;
	}

	// ===========================================================
	// getters and setters for the state fields

	public long getXCompanyId() {
		return xCompanyId;
	}

	public void setXCompanyId(long xCompanyId) {
		this.xCompanyId = xCompanyId;
	}

	public String getXName() {
		return xName;
	}

	public void setXName(String xName) {
		this.xName = xName;
	}

	// ===========================================================
	// getters and setters for the association fields

	/* Uni-directional Single-Valued One(Company)ToOne(Address) - Company Owner */
	public XAddress getXAddress() {
		return xAddress;
	}

	public void setXAddress(XAddress xAddress) {
		this.xAddress = xAddress;
	}

	/* Bi-directional One(Company)ToMany(Teams) - Owner Teams */
	public Collection<XTeam> getXTeams() {
		return xTeams;
	}

	public void setXTeams(Collection<XTeam> xTeams) {
		this.xTeams = xTeams;
	}

}
