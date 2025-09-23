/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.joincolumn;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;

@Entity
public class Customer1 implements Serializable {

	@Id
	private Long id;

	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "CUST_RETAIL", joinColumns = @JoinColumn(name = "CUSTOMERID"), inverseJoinColumns = @JoinColumn(name = "RETAILID"))
	private Set<RetailOrder1> orders = new HashSet();

	public Customer1() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int hashCode() {
		int hash = 0;
		hash += (this.getId() != null ? this.getId().hashCode() : 0);
		return hash;
	}

	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof Customer1)) {
			return false;
		}
		Customer1 other = (Customer1) object;
		if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "ee.jakarta.tck.persistence.core.override.joincolumn." + "Customer1[id=" + getId() + "]";
	}

	public Set<RetailOrder1> getOrders() {
		return orders;
	}

	public void setOrders(Set<RetailOrder1> orders) {
		this.orders = orders;
	}

	public void addOrder(RetailOrder1 order) {
		this.getOrders().add(order);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
