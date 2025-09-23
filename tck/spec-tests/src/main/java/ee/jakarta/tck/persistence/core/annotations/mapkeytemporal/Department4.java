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

package ee.jakarta.tck.persistence.core.annotations.mapkeytemporal;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyTemporal;
import jakarta.persistence.Table;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "DEPARTMENT2")
public class Department4 implements java.io.Serializable {
	private static final long serialVersionUID = 22L;

	// Instance variables
	@Id
	private int id;

	private String name;

	@Transient
	private Map<Date, EmbeddedEmployee> lastNameEmployees;

	public Department4() {
	}

	public Department4(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// ===========================================================
	// getters and setters for the state fields

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

	// ===========================================================
	// getters and setters for the association fields

	@ElementCollection(targetClass = EmbeddedEmployee.class)
	@CollectionTable(name = "EMP_MAPKEYCOL2", joinColumns = @JoinColumn(name = "FK_DEPT5"))
	@AttributeOverrides({ @AttributeOverride(name = "employeeId", column = @Column(name = "ID")),
			@AttributeOverride(name = "employeeName", column = @Column(name = "LASTNAME")) })
	@MapKeyColumn(name = "THEDATE")
	@MapKeyTemporal(TemporalType.DATE)
	public Map<Date, EmbeddedEmployee> getLastNameEmployees() {
		return lastNameEmployees;
	}

	public void setLastNameEmployees(Map<Date, EmbeddedEmployee> lastNameEmployees) {
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
