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
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * Order
 */

@Entity
@Table(name = "ORDER_TABLE")
public class Order implements java.io.Serializable, Comparable<Order> {

	// Instance variables
	private String id;

	private double totalPrice;

	private Customer customer;

	private CreditCard creditCard;

	private LineItem sampleLineItem;

	private Collection<LineItem> lineItemsCollection = new ArrayList<LineItem>();

	private List<LineItem> lineItemsList = new ArrayList<LineItem>();

	private Set<LineItem> lineItemsSet = new HashSet();

	public Order() {
	}

	public Order(String id, double totalPrice) {
		this.id = id;
		this.totalPrice = totalPrice;
	}

	public Order(String id, Customer customer) {
		this.id = id;
		this.customer = customer;
	}

	public Order(String id) {
		this.id = id;
	}

	// ====================================================================
	// getters and setters for State fields

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TOTALPRICE")
	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double price) {
		this.totalPrice = price;
	}

	// ====================================================================
	// getters and setters for Association fields

	// MANYx1
	@ManyToOne
	@JoinColumn(name = "FK4_FOR_CUSTOMER_TABLE")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	// 1x1
	@OneToOne(mappedBy = "order")
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard cc) {
		this.creditCard = cc;
	}

	// 1x1
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "FK0_FOR_LINEITEM_TABLE")
	public LineItem getSampleLineItem() {
		return sampleLineItem;
	}

	public void setSampleLineItem(LineItem l) {
		this.sampleLineItem = l;
	}

	// 1xMANY
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	public Collection<LineItem> getLineItemsCollection() {
		return lineItemsCollection;
	}

	public void setLineItemsCollection(Collection<LineItem> c) {
		this.lineItemsCollection = c;
	}

	// 1xMANY
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	public List<LineItem> getLineItemsList() {
		return lineItemsList;
	}

	public void setLineItemsList(List<LineItem> l) {
		this.lineItemsList = l;
	}

	// 1xMANY
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	public Set<LineItem> getLineItemsSet() {
		return lineItemsSet;
	}

	public void setLineItemsSet(Set<LineItem> s) {
		this.lineItemsSet = s;
	}

	// ====================================================================
	// Miscellaneous Business Methods

	public void addLineItem(LineItem p) throws LineItemException {
		getLineItemsCollection().add(p);
	}

	public void addSampleLineItem(LineItem p) throws LineItemException {
		setSampleLineItem(p);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", totalPrice: " + getTotalPrice());
		result.append(", lineitem.id[");

		boolean found = false;
		StringBuilder tmp = new StringBuilder();
		for (LineItem li : getLineItemsCollection()) {
			found = true;
			tmp.append(li.getId() + ",");
		}
		if (found) {
			result.append(tmp.toString().substring(0, tmp.length() - 1));
		}
		result.append("]");
		if (getCustomer() != null) {
			result.append(", custId: " + getCustomer().getId());
			result.append(", custName: " + getCustomer().getName());
		} else {
			result.append(", custId: null");
			result.append(", custName: null");
		}
		if (getCreditCard() != null) {
			result.append(", cc: " + getCreditCard().getId());
			result.append(", type: " + getCreditCard().getType());
		} else {
			result.append(", cc: null");
			result.append(", type: null");
		}
		result.append("]");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	public boolean equals(Object o) {
		Order other;
		boolean result = false;

		if (!(o instanceof Order)) {
			return result;
		}
		other = (Order) o;

		if (this.getId().equals(other.getId()) && this.getCreditCard().getId().equals(other.getCreditCard().getId())
				&& this.getCustomer().getId().equals(other.getCustomer().getId())) {
			result = true;
		}

		return result;
	}

	public int compareTo(Order order) {
		int lastCmp = Integer.valueOf(getId()).compareTo(Integer.valueOf(order.getId()));
		return (lastCmp != 0 ? lastCmp : Integer.valueOf(getId()).compareTo(Integer.valueOf(order.getId())));
	}

}
