/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.nestedembedding;

import jakarta.persistence.Embeddable;

@Embeddable
public class ZipCode implements java.io.Serializable {

	protected String zip;

	protected String plusFour;

	public ZipCode() {
		// logger.log(Logger.Level.TRACE,"ZipCode no arg constructor");
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

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		if (getZip() != null) {
			result.append("zip: " + getZip());
		} else {
			result.append("zip: null");
		}
		if (getPlusFour() != null) {
			result.append(", plusFour: " + getPlusFour());
		} else {
			result.append(", plusFour: null");
		}
		result.append("]");
		return result.toString();
	}
}
