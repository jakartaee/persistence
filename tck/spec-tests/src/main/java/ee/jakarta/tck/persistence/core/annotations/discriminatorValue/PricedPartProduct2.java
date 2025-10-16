/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.discriminatorValue;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class PricedPartProduct2 extends PartProduct2 implements java.io.Serializable {

	private double price;

	public PricedPartProduct2() {
		super();
	}

	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getName() != null) {
			result.append(", name: " + getName());
		} else {
			result.append(", name: null");
		}
		result.append(", quantity: " + getQuantity());
		result.append(", partNumber: " + getPartNumber());
		result.append(", price: " + getPrice());
		result.append("]");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	public boolean equals(Object o) {
		PricedPartProduct2 other;
		boolean result = false;

		if (!(o instanceof PricedPartProduct2)) {
			return result;
		}
		other = (PricedPartProduct2) o;

		if (this.getId().equals(other.getId()) && this.getName().equals(other.getName())
				&& this.getQuantity() == other.getQuantity() && this.getPartNumber() == other.getQuantity()
				&& this.getPrice() == other.getPrice()) {
			result = true;
		}

		return result;
	}
}
