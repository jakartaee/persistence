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

package ee.jakarta.tck.persistence.common.schema30;

import java.sql.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;

/*
 * ShelfLife 
 */

@Embeddable
public class ShelfLife implements java.io.Serializable {

	// Instance variables
	private Date inceptionDate;

	private Date soldDate;

	public ShelfLife() {
	}

	public ShelfLife(Date d1, Date d2) {
		inceptionDate = d1;
		soldDate = d2;
	}

	@Basic
	public Date getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(Date d1) {
		inceptionDate = d1;
	}

	@Basic
	public Date getSoldDate() {
		return soldDate;
	}

	public void setSoldDate(Date d2) {
		soldDate = d2;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("inceptionDate: " + getInceptionDate());
		result.append(", soldDate: " + getSoldDate());
		result.append("]");

		return result.toString();
	}
}
