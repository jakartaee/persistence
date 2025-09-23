/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Trim
 */

@Entity
@Table(name = "TRIM_TABLE")
public class Trim implements java.io.Serializable {

	// Instance variables
	private String id;

	private String name;

	public Trim() {
	}

	public Trim(String id, String name) {
		this.id = id;
		this.name = name;
	}

	// ===========================================================
	// getters and setters for CMP fields

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		this.id = v;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name = s;
	}

	public boolean equals(Object o) {
		Trim other;
		boolean same = true;

		if (!(o instanceof Trim)) {
			return false;
		}
		other = (Trim) o;

		same &= this.id.equals(other.id);

		return same;
	}

	public int hashCode() {
		int myHash;

		myHash = this.id.hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(",  name: |" + getName() + "|");
		result.append("]");
		return result.toString();
	}
}
