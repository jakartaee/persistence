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

package ee.jakarta.tck.persistence.core.annotations.convert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Department
 */

@Entity
@Table(name = "DEPARTMENT")
public class Department implements java.io.Serializable {

	// Instance variables
	private int id;

	private String name;

	public Department() {
	}

	public Department(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof Department))
			return false;

		Department o1 = (Department) o;

		boolean result = false;

		if (this.getId() == o1.getId() && this.getName().equals(o1.getName())) {
			result = true;
		}

		return result;

	}

	@Override
	public int hashCode() {
		return this.getId() + this.getName().hashCode();
	}

	@Override
	public String toString() {
		return "Department: id=" + getId() + ", name=" + getName();
	}
}
