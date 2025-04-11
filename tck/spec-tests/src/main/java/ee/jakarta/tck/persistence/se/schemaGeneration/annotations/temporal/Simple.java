/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.temporal;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "SCHEMAGENSIMPLE")
public class Simple implements java.io.Serializable {

	int id;

	Date utilDate;

	public Simple() {
	}

	public Simple(int id) {
		this.id = id;
	}

	public Simple(int id, Date utilDate) {
		this.id = id;
		this.utilDate = utilDate;
	}

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	public Date getUtilDate() {
		return utilDate;
	}

	public void setUtilDate(Date utilDate) {
		this.utilDate = utilDate;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getId() + this.getUtilDate().hashCode();

		return myHash;
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof Simple))
			return false;

		Simple o1 = (Simple) o;

		boolean result = false;

		if (this.getId() == o1.getId() && (this.getUtilDate() != null && o1.getUtilDate() != null
				&& this.getUtilDate().equals(o1.getUtilDate()))) {
			result = true;
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", date " + getUtilDate());
		result.append("]");
		return result.toString();
	}

}
