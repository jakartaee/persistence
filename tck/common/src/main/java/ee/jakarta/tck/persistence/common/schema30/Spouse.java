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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * Spouse
 */

@Entity
@Table(name = "SPOUSE_TABLE")
public class Spouse implements java.io.Serializable {

	// Instance variables
	private String id;

	private String first;

	private String maiden;

	private String last;

	private String sNumber;

	private Info info;

	private Customer customer;

	public Spouse() {
	}

	public Spouse(String v1, String v2, String v3, String v4, String v5, Info v6) {
		id = v1;
		first = v2;
		maiden = v3;
		last = v4;
		sNumber = v5;
		info = v6;
	}

	public Spouse(String v1, String v2, String v3, String v4, String v5, Info v6, Customer v7) {
		id = v1;
		first = v2;
		maiden = v3;
		last = v4;
		sNumber = v5;
		info = v6;
		customer = v7;
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

	@Column(name = "FIRSTNAME")
	public String getFirstName() {
		return first;
	}

	public void setFirstName(String v) {
		first = v;
	}

	@Column(name = "MAIDENNAME")
	public String getMaidenName() {
		return maiden;
	}

	public void setMaidenName(String v) {
		maiden = v;
	}

	@Column(name = "LASTNAME")
	public String getLastName() {
		return last;
	}

	public void setLastName(String v) {
		last = v;
	}

	@Column(name = "SOCSECNUM")
	public String getSocialSecurityNumber() {
		return sNumber;
	}

	public void setSocialSecurityNumber(String v) {
		sNumber = v;
	}

	// 1X1
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_FOR_INFO_TABLE")
	public Info getInfo() {
		return info;
	}

	public void setInfo(Info v) {
		info = v;
	}

	// 1X1
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK7_FOR_CUSTOMER_TABLE")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer v) {
		customer = v;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", first: " + getFirstName());
		result.append(", maiden: " + getMaidenName());
		result.append(", last: " + getLastName());
		result.append(", sNumber: " + getSocialSecurityNumber());
		result.append("]");

		return result.toString();
	}
}
