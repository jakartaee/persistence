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

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * Alias
 */

@Entity
@Table(name = "ALIAS_TABLE")
public class Alias implements java.io.Serializable {

	// Instance variables
	private String id;

	private String alias;

	private Customer customerNoop;

	private Collection<Customer> customersNoop = new java.util.ArrayList<Customer>();

	private Collection<Customer> customers = new java.util.ArrayList<Customer>();

	public Alias() {
	}

	public Alias(String id, String alias) {
		this.id = id;
		this.alias = alias;
	}

	// ===========================================================
	// getters and setters for persistent fields

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ALIAS")
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	// ===========================================================
	// getters and setters for relationship fields

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK1_FOR_CUSTOMER_TABLE", insertable = false, updatable = false)
	public Customer getCustomerNoop() {
		return customerNoop;
	}

	public void setCustomerNoop(Customer customerNoop) {
		this.customerNoop = customerNoop;
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "FKS_ANOOP_CNOOP", joinColumns = @JoinColumn(name = "FK2_FOR_ALIAS_TABLE", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "FK8_FOR_CUSTOMER_TABLE", referencedColumnName = "ID"))
	public Collection<Customer> getCustomersNoop() {
		return customersNoop;
	}

	public void setCustomersNoop(Collection<Customer> customersNoop) {
		this.customersNoop = customersNoop;

	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "FKS_ALIAS_CUSTOMER", joinColumns = @JoinColumn(name = "FK_FOR_ALIAS_TABLE", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "FK_FOR_CUSTOMER_TABLE", referencedColumnName = "ID"))
	public Collection<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Collection<Customer> customers) {
		this.customers = customers;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", alias: " + getAlias());
		result.append("]");
		return result.toString();
	}
}
