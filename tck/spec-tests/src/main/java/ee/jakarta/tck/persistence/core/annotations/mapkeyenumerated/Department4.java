/*
 * Copyright (c) 2014, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkeyenumerated;

import java.util.Map;

import jakarta.persistence.*;

@Entity
@Table(name = "DEPARTMENT2")
public class Department4 implements java.io.Serializable {

	private static final long serialVersionUID = 22L;

	// Instance variables
	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	@ElementCollection(targetClass = EmbeddedEmployee.class)
	@CollectionTable(name = "EMP_MAPKEYCOL2", joinColumns = @JoinColumn(name = "FK_DEPT5"))
	@AttributeOverrides({
			@AttributeOverride(name = "value.employeeId", column = @Column(name = "ID")),
			@AttributeOverride(name = "value.employeeName", column = @Column(name = "LASTNAME"))
	})
	@MapKeyEnumerated(EnumType.STRING)
	@MapKeyColumn(name = "LASTNAMEEMPLOYEES_KEY")
	private Map<Numbers, EmbeddedEmployee> lastNameEmployees;

	public Department4() {
	}

	public Department4(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// ===========================================================
	// getters and setters for the state fields

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ===========================================================
	// getters and setters for the association fields

	public Map<Numbers, EmbeddedEmployee> getLastNameEmployees() {
		return lastNameEmployees;
	}

	public void setLastNameEmployees(Map<Numbers, EmbeddedEmployee> lastNameEmployees) {
		this.lastNameEmployees = lastNameEmployees;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getName() != null) {
			result.append(", name: " + getName());
		} else {
			result.append(", name: null");
		}
		result.append("]");
		return result.toString();
	}
}
