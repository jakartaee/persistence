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

package ee.jakarta.tck.persistence.core.annotations.version;

import java.sql.Timestamp;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "A_BASIC")
@Access(AccessType.FIELD)
public class Timestamp_Field implements java.io.Serializable {

	@Id
	protected String id;

	@Version
	protected Timestamp basicTimestamp;

	@Basic
	protected String name;

	public Timestamp_Field() {
	}

	public Timestamp_Field(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getVersion() {
		return this.basicTimestamp;
	}

	public void setVersion(Timestamp basicTimestamp) {
		this.basicTimestamp = basicTimestamp;
	}

	public boolean equals(Object o) {
		Timestamp_Field other;
		boolean result = false;

		if (!(o instanceof Timestamp_Field)) {
			return result;
		}
		other = (Timestamp_Field) o;

		if (this.getId().equals(other.getId()) && this.basicTimestamp.equals(other.basicTimestamp)
				&& this.name.equals(other.getName())) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getId().hashCode() + this.basicTimestamp.hashCode() + this.name.hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", version: " + basicTimestamp);
		result.append(", name: " + name);
		result.append("]");
		return result.toString();
	}
}
