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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * Phone
 */

@Entity
@Table(name = "PHONE_TABLE")
public class Phone implements java.io.Serializable {

	// Instance variables
	private String id;

	private String area;

	private String number;

	private Address address;

	public Phone() {
	}

	public Phone(String v1, String v2, String v3) {
		id = v1;
		area = v2;
		number = v3;
	}

	public Phone(String v1, String v2, String v3, Address v4) {
		id = v1;
		area = v2;
		number = v3;
		address = v4;
	}

	// getters and setters for state fields
	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		id = v;
	}

	@Column(name = "AREA")
	public String getArea() {
		return area;
	}

	public void setArea(String v) {
		area = v;
	}

	@Column(name = "PHONE_NUMBER")
	public String getNumber() {
		return number;
	}

	public void setNumber(String v) {
		number = v;
	}
	// ===========================================================
	// getters and setters for Association fields

	// Manyx1

	@ManyToOne
	@JoinColumn(name = "FK_FOR_ADDRESS")
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address a) {
		address = a;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", area: " + getArea());
		result.append(", number: " + getNumber());
		result.append("]");

		return result.toString();
	}
}
