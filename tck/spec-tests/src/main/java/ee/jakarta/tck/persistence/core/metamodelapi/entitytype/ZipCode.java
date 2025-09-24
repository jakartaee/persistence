/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.metamodelapi.entitytype;

import jakarta.persistence.Embeddable;

@Embeddable
public class ZipCode implements java.io.Serializable {

	protected String zip;

	protected String plusFour;

	public ZipCode() {
	}

	public ZipCode(String zip, String plusFour) {
		this.zip = zip;
		this.plusFour = plusFour;
	}

	public String getPlusFour() {
		return plusFour;
	}

	public void setPlusFour(String plusFour) {
		this.plusFour = plusFour;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
