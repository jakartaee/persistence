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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * Info
 */

@Entity
@Table(name = "INFO_TABLE")
public class Info implements java.io.Serializable {

	// Instance variables
	private String id;

	private String street;

	private String city;

	private String state;

	private String zip;

	private Spouse spouse;

	public Info() {
	}

	public Info(String v1, String v2, String v3, String v4, String v5) {
		id = v1;
		street = v2;
		city = v3;
		state = v4;
		zip = v5;
	}

	public Info(String v1, String v2, String v3, String v4, String v5, Spouse v6) {
		id = v1;
		street = v2;
		city = v3;
		state = v4;
		zip = v5;
		spouse = v6;
	}

	// ===========================================================
	// getters and setters for state fields

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		id = v;
	}

	@Column(name = "INFOSTREET")
	public String getStreet() {
		return street;
	}

	public void setStreet(String v) {
		street = v;
	}

	@Column(name = "INFOSTATE")
	public String getState() {
		return state;
	}

	public void setState(String v) {
		state = v;
	}

	@Column(name = "INFOCITY")
	public String getCity() {
		return city;
	}

	public void setCity(String v) {
		city = v;
	}

	@Column(name = "INFOZIP")
	public String getZip() {
		return zip;
	}

	public void setZip(String v) {
		zip = v;
	}

	// ===========================================================
	// getters and setters for association fields

	// ONEXONE
	@OneToOne(mappedBy = "info")
	public Spouse getSpouse() {
		return spouse;
	}

	public void setSpouse(Spouse v) {
		this.spouse = v;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", street: " + getStreet());
		result.append(", city: " + getCity());
		result.append(", state: " + getState());
		result.append(", zip: " + getZip());
		result.append("]");

		return result.toString();
	}
}
