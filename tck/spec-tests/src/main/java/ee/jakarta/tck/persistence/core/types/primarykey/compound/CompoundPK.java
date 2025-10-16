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

package ee.jakarta.tck.persistence.core.types.primarykey.compound;

import jakarta.persistence.Embeddable;

/*
 * Class used to define a compound primary key for Entity beans.
 */

@Embeddable
public class CompoundPK implements java.io.Serializable {

	/* Fields */
	private Integer pmIDInteger;

	private String pmIDString;

	private Float pmIDFloat;

	public Integer getPmIDInteger() {
		return pmIDInteger;
	}

	public void setPmIDInteger(Integer pmIDInteger) {
		this.pmIDInteger = pmIDInteger;
	}

	public String getPmIDString() {
		return pmIDString;
	}

	public void setPmIDString(String pmIDString) {
		this.pmIDString = pmIDString;
	}

	public Float getPmIDFloat() {
		return pmIDFloat;
	}

	public void setPmIDFloat(Float pmIDFloat) {
		this.pmIDFloat = pmIDFloat;
	}

	/** No-arg Constructor */
	public CompoundPK() {
	}

	/** Standard Constructor */
	public CompoundPK(int intID, String strID, float floatID) {
		this.pmIDInteger = intID;
		this.pmIDString = strID;
		this.pmIDFloat = floatID;
	}

	/** Override java.lang.Object method */
	public int hashCode() {
		int myHash;

		myHash = getPmIDInteger().hashCode() + getPmIDString().hashCode() + getPmIDFloat().hashCode();

		return myHash;
	}

	/** Override java.lang.Object method */
	public boolean equals(Object o) {
		CompoundPK other;
		boolean same = true;

		if (!(o instanceof CompoundPK)) {
			return false;
		}
		other = (CompoundPK) o;

		same &= getPmIDInteger().equals(other.getPmIDInteger());
		same &= getPmIDString().equals(other.getPmIDString());
		same &= getPmIDFloat().equals(other.getPmIDFloat());

		return same;
	}

	/** Override java.lang.Object method */
	public String toString() {
		return "CompoundPK [ " + getPmIDInteger() + ", " + getPmIDString() + ", " + getPmIDFloat() + " ]";
	}

}
