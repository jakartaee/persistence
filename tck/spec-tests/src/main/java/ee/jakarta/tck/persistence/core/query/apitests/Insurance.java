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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.query.apitests;

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
 * Insurance
 */

@Entity
@Table(name = "INSURANCE")
public class Insurance implements java.io.Serializable {

	// Instance variables
	private int id;

	private String carrier;

	private Collection<Employee> employees = new java.util.ArrayList<Employee>();

	public Insurance() {
	}

	public Insurance(int id, String carrier) {
		this.id = id;
		this.carrier = carrier;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	@Column(name = "INSID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "CARRIER")
	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	// ===========================================================
	// getters and setters for the association fields

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "insurance")
	public Collection<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof Insurance))
			return false;

		Insurance o1 = (Insurance) o;

		boolean result = false;

		if (this.getId() == o1.getId() && this.getCarrier().equals(o1.getCarrier())) {
			result = true;
		}

		return result;

	}

	@Override
	public int hashCode() {
		return this.getId() + this.getCarrier().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", carrier: " + getCarrier());
		result.append("]");
		return result.toString();
	}
}
