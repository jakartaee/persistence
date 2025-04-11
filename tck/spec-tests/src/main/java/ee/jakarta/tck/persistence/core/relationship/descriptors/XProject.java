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
import java.math.BigDecimal;
import java.util.Collection;

/*
 * XProject
 */

public class XProject implements java.io.Serializable {

	private static final Logger logger = (Logger) System.getLogger(XProject.class.getName());

	// Instance Variables
	private long xProjId;

	private String xName;

	private BigDecimal xBudget;

	private XPerson xProjectLead;

	private Collection<XPerson> xPersons = new java.util.ArrayList<XPerson>();

	public XProject() {
		logger.log(Logger.Level.TRACE, "XProject no-arg constructor");
	}

	public XProject(long xProjId, String xName, BigDecimal xBudget) {
		this.xProjId = xProjId;
		this.xName = xName;
		this.xBudget = xBudget;
	}

	// ===========================================================
	// getters and setters for the state fields

	public long getXProjId() {
		return xProjId;
	}

	public void setXProjId(long xProjId) {
		this.xProjId = xProjId;
	}

	public String getXName() {
		return xName;
	}

	public void setXName(String xName) {
		this.xName = xName;
	}

	public BigDecimal getXBudget() {
		return xBudget;
	}

	public void setXBudget(BigDecimal xBudget) {
		this.xBudget = xBudget;
	}

	// ===========================================================
	// getters and setters for the association fields

	/* Bi-Directional OneProjectLeadToOnePerson */
	public XPerson getXProjectLead() {
		return xProjectLead;
	}

	public void setXProjectLead(XPerson xProjectLead) {
		this.xProjectLead = xProjectLead;
	}

	/* Bi-Directional ManyPersonsToManyProjects */
	public Collection<XPerson> getXPersons() {
		return xPersons;
	}

	public void setXPersons(Collection<XPerson> xPersons) {
		this.xPersons = xPersons;
	}

}
