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

package ee.jakarta.tck.persistence.core.types.primarykey.compound;

/*
 * Class used to define a compound primary key for Entity beans.
 */

public class CompoundPK3 implements java.io.Serializable {

	/* Fields */
	private Integer pmIDInteger;

	private String pmIDString;

	private Float pmIDFloat;

	/** No-arg Constructor */
	public CompoundPK3() {
	}

	/** Standard Constructor */
	public CompoundPK3(int intID, String strID, float floatID) {
		this.pmIDInteger = intID;
		this.pmIDString = strID;
		this.pmIDFloat = floatID;
	}

	/** Override java.lang.Object method */
	public int hashCode() {
		int myHash;

		myHash = this.pmIDInteger.hashCode() + this.pmIDString.hashCode() + this.pmIDFloat.hashCode();

		return myHash;
	}

	/** Override java.lang.Object method */
	public boolean equals(Object o) {
		CompoundPK3 other;
		boolean same = true;

		if (!(o instanceof CompoundPK3)) {
			return false;
		}
		other = (CompoundPK3) o;

		same &= this.pmIDInteger.equals(other.pmIDInteger);
		same &= this.pmIDString.equals(other.pmIDString);
		same &= this.pmIDFloat.equals(other.pmIDFloat);

		return same;
	}

	/** Override java.lang.Object method */
	public String toString() {
		return "CompoundPK3 [ " + pmIDInteger + ", " + pmIDString + ", " + pmIDFloat + " ]";
	}

}
