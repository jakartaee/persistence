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
 * XAddress
 */

public class XAddress implements java.io.Serializable {

	// Instance Variables
	private String xId;

	private String xStreet;

	private String xCity;

	private String xState;

	private String xZip;

	public XAddress() {
	}

	public XAddress(String xId, String xStreet, String xCity, String xState, String xZip) {
		this.xId = xId;
		this.xStreet = xStreet;
		this.xCity = xCity;
		this.xState = xState;
		this.xZip = xZip;
	}

	// ===========================================================
	// getters and setters for the state fields

	public String getXId() {
		return xId;
	}

	public void setXId(String xId) {
		this.xId = xId;
	}

	public String getXStreet() {
		return xStreet;
	}

	public void setXStreet(String xStreet) {
		this.xStreet = xStreet;
	}

	public String getXCity() {
		return xCity;
	}

	public void setXCity(String xCity) {
		this.xCity = xCity;
	}

	public String getXState() {
		return xState;
	}

	public void setXState(String xState) {
		this.xState = xState;
	}

	public String getXZip() {
		return xZip;
	}

	public void setXZip(String xZip) {
		this.xZip = xZip;
	}
}
