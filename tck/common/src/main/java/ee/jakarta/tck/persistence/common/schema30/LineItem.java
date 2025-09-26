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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * LineItem
 */

@Entity
@Table(name = "LINEITEM_TABLE")
public class LineItem implements java.io.Serializable {

	// Instance variables
	private String id;

	private int quantity;

	private Order order;

	private Product product;

	public LineItem() {
	}

	public LineItem(String v1, int v2, Order v3, Product v4) {
		id = v1;
		quantity = v2;
		order = v3;
		product = v4;
	}

	public LineItem(String v1, int v2) {
		id = v1;
		quantity = v2;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		id = v;
	}

	@Column(name = "QUANTITY")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int v) {
		quantity = v;
	}

	@ManyToOne
	@JoinColumn(name = "FK1_FOR_ORDER_TABLE")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order v) {
		order = v;
	}

	@ManyToOne
	@JoinColumn(name = "FK_FOR_PRODUCT_TABLE")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product v) {
		product = v;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", quantity: " + getQuantity());
		if (getOrder() != null) {
			result.append(", fk_order.id: " + getOrder().getId());
		} else {
			result.append(", fk_order.id: null");
		}
		if (getProduct() != null) {
			result.append(", fk_product.id: " + getProduct().getId());
		} else {
			result.append(", fk_product.id: null");
		}
		result.append("]");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode() + this.getQuantity() + this.getOrder().getId().hashCode()
				+ this.getProduct().getId().hashCode();
	}

	public boolean equals(Object o) {
		LineItem other;
		boolean result = false;

		if (!(o instanceof LineItem)) {
			return result;
		}
		other = (LineItem) o;

		if (this.getId().equals(other.getId()) && this.getQuantity() == other.getQuantity()
				&& this.getOrder().getId().equals(other.getOrder().getId())
				&& this.getProduct().getId().equals(other.getProduct().getId())) {
			result = true;
		}

		return result;
	}
}
