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

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/*
 * Insurance
 */

@Entity
public class Insurance implements java.io.Serializable {

	// Instance variables
	private int insid;

	private String carrier;

	public Insurance() {
	}

	public Insurance(int insid, String carrier) {
		this.insid = insid;
		this.carrier = carrier;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	public int getInsId() {
		return insid;
	}

	public void setInsId(int insid) {
		this.insid = insid;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

}
