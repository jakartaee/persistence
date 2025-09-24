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

package ee.jakarta.tck.persistence.core.annotations.temporal;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "DATE_TABLE")
public class A2_Property implements java.io.Serializable {

	// ===========================================================
	// instance variables

	protected Calendar id;

	protected String stringVersion = null;

	// ===========================================================
	// constructors
	public A2_Property() {
	}

	public A2_Property(Calendar id) {
		this.id = id;
	}

	public A2_Property(Calendar id, String stringVersion) {
		this.id = id;
		this.stringVersion = stringVersion;
	}

	@Id
	@Temporal(TemporalType.DATE)
	public Calendar getId() {
		return id;
	}

	public void setId(Calendar id) {
		this.id = id;
	}

	public String getStringVersion() {
		return this.stringVersion;
	}

	public void setStringVersion(String stringVersion) {
		this.stringVersion = stringVersion;
	}

	public boolean equals(Object o) {
		A2_Property other;
		boolean result = false;

		if (!(o instanceof A2_Property)) {
			return result;
		}
		other = (A2_Property) o;

		if (this.getId().equals(other.getId()) && this.getStringVersion().equals(other.getStringVersion())) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getId().hashCode() + this.getStringVersion().hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", Calendar: " + getStringVersion());
		result.append("]");
		return result.toString();
	}
}
