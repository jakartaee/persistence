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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * CreditCard
 */

@Entity
@Table(name = "CREDITCARD_TABLE")
public class CreditCard implements java.io.Serializable {

	// Instance variables
	private String id;

	private String number;

	private String type;

	private String expires;

	private boolean approved;

	private double balance;

	private Order order;

	private Customer customer;

	public CreditCard() {
	}

	public CreditCard(String v1, String v2, String v3, String v4, boolean v5, double v6, Order v7, Customer v8) {
		id = v1;
		number = v2;
		type = v3;
		expires = v4;
		approved = v5;
		balance = v6;
		order = v7;
		customer = v8;
	}

	public CreditCard(String v1, String v2, String v3, String v4, boolean v5, double v6) {
		id = v1;
		number = v2;
		type = v3;
		expires = v4;
		approved = v5;
		balance = v6;
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

	@Column(name = "CREDITCARD_NUMBER")
	public String getNumber() {
		return number;
	}

	public void setNumber(String v) {
		number = v;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String v) {
		type = v;
	}

	@Column(name = "EXPIRES")
	public String getExpires() {
		return expires;
	}

	public void setExpires(String v) {
		expires = v;
	}

	@Column(name = "APPROVED")
	public boolean getApproved() {
		return approved;
	}

	public void setApproved(boolean v) {
		approved = v;
	}

	@Column(name = "BALANCE")
	public double getBalance() {
		return balance;
	}

	public void setBalance(double v) {
		balance = v;
	}

	// ===========================================================
	// getters and setters for association fields

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_FOR_ORDER_TABLE")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order v) {
		order = v;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK3_FOR_CUSTOMER_TABLE")
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
		result.append(", type: " + getType());
		result.append(", expires: " + getExpires());
		result.append(", approved: " + getApproved());
		result.append("]");
		return result.toString();
	}
}
