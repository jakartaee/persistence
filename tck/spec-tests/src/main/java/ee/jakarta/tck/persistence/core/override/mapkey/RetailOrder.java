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

package ee.jakarta.tck.persistence.core.override.mapkey;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class RetailOrder implements Serializable {

	@Id
	private Long id;

	private double cost;

	@ManyToMany()
	@JoinTable(name = "RETAILORDER_CONSUMER", joinColumns = @JoinColumn(name = "ORDERS_ID"), inverseJoinColumns = @JoinColumn(name = "CONSUMERS_ID"))
	private Set<Consumer> consumers = new HashSet();

	public RetailOrder() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int hashCode() {
		int hash = 17;
		hash += 37 * hash + Double.doubleToLongBits(cost);
		return hash;
	}

	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof RetailOrder)) {
			return false;
		}
		RetailOrder other = (RetailOrder) object;
		if (Double.doubleToLongBits(this.cost) == Double.doubleToLongBits(other.cost)) {
			return true;
		}

		return false;
	}

	public String toString() {
		return "ee.jakarta.tck.persistence.core.override.mapkey." + "RetailOrder[id=" + getId() + "]";
	}

	public Set<Consumer> getConsumers() {
		return consumers;
	}

	public void setConsumers(Set<Consumer> consumers) {
		this.consumers = consumers;
	}

	public void addConsumer(Consumer consumer) {
		this.getConsumers().add(consumer);
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
}
