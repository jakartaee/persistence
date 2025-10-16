/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.cache.xml.all;

public class Order implements java.io.Serializable {

	private int id;

	private int total;

	public Order() {
	}

	public Order(int total) {
		this.total = total;
	}

	public Order(int id, int total) {
		this.id = id;
		this.total = total;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Order id=" + getId() + ", total=" + getTotal();
	}

	@Override
	public int hashCode() {
		int myHash;

		myHash = this.getId() + this.getTotal();

		return myHash;
	}

	@Override
	public boolean equals(Object o) {
		Order other;
		boolean result = false;

		if (!(o instanceof Order)) {
			return result;
		}
		other = (Order) o;

		if (this.getId() == other.getId() && this.getTotal() == (other.getTotal())) {
			result = true;
		}

		return result;
	}
}
