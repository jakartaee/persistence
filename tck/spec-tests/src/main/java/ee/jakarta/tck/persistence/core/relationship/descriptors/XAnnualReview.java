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
 * XAnnualReview
 */

public class XAnnualReview implements java.io.Serializable {

	// Instance variables
	private Integer xAid;

	private Integer xService;

	public XAnnualReview() {
	}

	public XAnnualReview(Integer xAid, Integer xService) {
		this.xAid = xAid;
		this.xService = xService;
	}

	// ===========================================================
	// getters and setters for the state fields

	public Integer getXAid() {
		return xAid;
	}

	public void setXAid(Integer xAid) {
		this.xAid = xAid;
	}

	public Integer getXService() {
		return xService;
	}

	public void setXService(Integer xService) {
		this.xService = xService;
	}

}
