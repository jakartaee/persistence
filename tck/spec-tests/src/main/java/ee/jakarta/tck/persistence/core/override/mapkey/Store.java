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
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Store implements Serializable {

	@Id
	private Integer id;

	private Map<String, Customers> customers = new HashMap<String, Customers>();

	private String name;

	public Store() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
		if (!(object instanceof Store)) {
			return false;
		}
		Store other = (Store) object;
		if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "ee.jakarta.tck.persistence.core.override." + "mapkey.Store[id=" + getId() + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Customers> getCustomers() {
		return customers;
	}

	public void setCustomers(Map<String, Customers> customers) {
		this.customers = customers;
	}
}
