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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * Customer
 */

@Entity
@Table(name = "CUSTOMER_TABLE")
public class Customer implements java.io.Serializable {

	// Instance variables
	private String id;

	private String name;

	private Address home;

	private Address work;

	private Country country;

	private Spouse spouse;

	private Collection<CreditCard> creditCards = new java.util.ArrayList<CreditCard>();

	private Collection<Order> orders = new java.util.ArrayList<Order>();

	private Collection<Alias> aliases = new java.util.ArrayList<Alias>();

	private Collection<Alias> aliasesNoop = new java.util.ArrayList<Alias>();

	private Set<Order> orders2 = new HashSet();

	private List<Order> orders3 = new ArrayList<Order>();

	public Customer() {
	}

	public Customer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Customer(String id, String name, Country country) {
		this.id = id;
		this.name = name;
		this.country = country;
	}

	public Customer(String id, String name, Country country, Address work) {
		this.id = id;
		this.name = name;
		this.country = country;
		this.work = work;
	}

	public Customer(String id, String name, Address home, Address work, Country country) {
		this.id = id;
		this.name = name;
		this.home = home;
		this.work = work;
		this.country = country;
	}

	// ===========================================================
	// getters and setters for CMP fields

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		this.id = v;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String v) {
		this.name = v;
	}

	@Embedded
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country v) {
		this.country = v;
	}

	// ===========================================================
	// getters and setters for State fields

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK6_FOR_CUSTOMER_TABLE")
	public Address getHome() {
		return home;
	}

	public void setHome(Address v) {
		this.home = v;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK5_FOR_CUSTOMER_TABLE")
	public Address getWork() {
		return work;
	}

	public void setWork(Address v) {
		this.work = v;
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
	public Spouse getSpouse() {
		return spouse;
	}

	public void setSpouse(Spouse v) {
		this.spouse = v;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	public Collection<CreditCard> getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(Collection<CreditCard> v) {
		this.creditCards = v;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> v) {
		this.orders = v;
	}

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "customers")
	public Collection<Alias> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<Alias> v) {
		this.aliases = v;
	}

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "customersNoop")
	public Collection<Alias> getAliasesNoop() {
		return aliasesNoop;
	}

	public void setAliasesNoop(Collection<Alias> v) {
		this.aliasesNoop = v;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	public Set<Order> getOrders2() {
		return orders2;
	}

	public void setOrders2(Set<Order> v) {
		this.orders2 = v;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	public List<Order> getOrders3() {
		return orders3;
	}

	public void setOrders3(List<Order> v) {
		this.orders3 = v;
	}

	public boolean equals(Object o) {
		Customer other;
		boolean same = true;

		if (!(o instanceof Customer)) {
			return false;
		}
		other = (Customer) o;

		same &= this.id.equals(other.id);

		return same;
	}

	public int hashCode() {
		int myHash;

		myHash = this.id.hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(",  name: " + getName());
		if (getHome() != null) {
			result.append(", home: " + getHome().getId());
		} else {
			result.append(", home: null");
		}
		if (getWork() != null) {
			result.append(", work: " + getWork().getId());
		} else {
			result.append(", work: null");
		}
		if (getSpouse() != null) {
			result.append(", spouse: " + getSpouse());
		} else {
			result.append(", spouse: null");
		}
		if (getCountry() != null) {
			result.append(", country: " + getCountry());
		} else {
			result.append(", country: null");
		}
		result.append("]");
		return result.toString();
	}
}
