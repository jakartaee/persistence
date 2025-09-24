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

import java.util.Date;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "DATE_TABLE")
@Access(AccessType.FIELD)
public class A2_Field implements java.io.Serializable {

	// ===========================================================
	// instance variables

	@Id
	@Temporal(TemporalType.DATE)
	protected Date id;

	protected String stringVersion = null;

	// ===========================================================
	// constructors
	public A2_Field() {
	}

	public A2_Field(Date id) {
		this.id = id;
	}

	public A2_Field(Date id, String stringVersion) {
		this.id = id;
		this.stringVersion = stringVersion;
	}

	public Date getId() {
		return id;
	}

	public void setId(Date id) {
		this.id = id;
	}

	public String getStringVersion() {
		return this.stringVersion;
	}

	public void setStringVersion(String stringVersion) {
		this.stringVersion = stringVersion;
	}

	public boolean equals(Object o) {
		A2_Field other;
		boolean result = false;

		if (!(o instanceof A2_Field)) {
			return result;
		}
		other = (A2_Field) o;

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
		result.append(", date: " + getStringVersion());
		result.append("]");
		return result.toString();
	}
}
