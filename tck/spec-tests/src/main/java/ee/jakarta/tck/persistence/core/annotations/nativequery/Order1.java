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

package ee.jakarta.tck.persistence.core.annotations.nativequery;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.Table;

/*
 * Order1
 */

@SqlResultSetMappings({
		@SqlResultSetMapping(name = "Order1ItemResults", entities = {
				@EntityResult(entityClass = ee.jakarta.tck.persistence.core.annotations.nativequery.Order1.class),
				@EntityResult(entityClass = ee.jakarta.tck.persistence.core.annotations.nativequery.Item.class) }),
		@SqlResultSetMapping(name = "Order2ItemResults", entities = {
				@EntityResult(entityClass = ee.jakarta.tck.persistence.core.annotations.nativequery.Order1.class, fields = {
						@FieldResult(name = "id", column = "OID"), @FieldResult(name = "totalPrice", column = "OPRICE"),
						@FieldResult(name = "item", column = "OITEM") }) }, columns = {
								@ColumnResult(name = "INAME") }),
		@SqlResultSetMapping(name = "Order3ItemResults", entities = {
				@EntityResult(entityClass = ee.jakarta.tck.persistence.core.annotations.nativequery.Order1.class, fields = {
						@FieldResult(name = "id", column = "THISID"),
						@FieldResult(name = "totalPrice", column = "THISPRICE"),
						@FieldResult(name = "item", column = "THISITEM") }),
				@EntityResult(entityClass = ee.jakarta.tck.persistence.core.annotations.nativequery.Item.class) }),
		@SqlResultSetMapping(name = "Order4ItemResults", entities = {
				@EntityResult(entityClass = ee.jakarta.tck.persistence.core.annotations.nativequery.Order1.class, fields = {
						@FieldResult(name = "id", column = "OID"), @FieldResult(name = "totalPrice", column = "OPRICE"),
						@FieldResult(name = "item", column = "OITEM") }) }, columns = {
								@ColumnResult(name = "INAME", type = String.class) }),
		@SqlResultSetMapping(name = "Order5ItemResults", classes = {
				@ConstructorResult(targetClass = ee.jakarta.tck.persistence.core.annotations.nativequery.Order2.class, columns = {
						@ColumnResult(name = "OID"), @ColumnResult(name = "OPRICE"),
						@ColumnResult(name = "OITEMNAME") }) }),
		@SqlResultSetMapping(name = "PurchaseOrder1Results", classes = {
				@ConstructorResult(targetClass = ee.jakarta.tck.persistence.core.annotations.nativequery.PurchaseOrder.class, columns = {
						@ColumnResult(name = "OID"), @ColumnResult(name = "PTOTAL") }) }),
		@SqlResultSetMapping(name = "PurchaseOrder2Results", classes = {
				@ConstructorResult(targetClass = ee.jakarta.tck.persistence.core.annotations.nativequery.PurchaseOrder.class, columns = {
						@ColumnResult(name = "PTOTAL") }) }) })
@Entity
@Table(name = "ORDER1")
public class Order1 implements java.io.Serializable {

	// Instance variables
	private int id;

	private double totalPrice;

	private Item item;

	public Order1() {
	}

	public Order1(int id, double totalPrice) {
		this.id = id;
		this.totalPrice = totalPrice;
	}

	public Order1(int id) {
		this.id = id;
	}

	// ====================================================================
	// getters and setters for State fields

	@Id
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "TOTALPRICE")
	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double price) {
		this.totalPrice = price;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK1_FOR_ITEM")
	public Item getItem() {
		return item;
	}

	public void setItem(Item v) {
		item = v;
	}

	@Override
	public int hashCode() {
		return this.getItem().getId();
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof Order1)) {
			Order1 o = (Order1) obj;
			result = (o.id == this.id && o.getTotalPrice() == this.totalPrice && o.item.equals(this.item));
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", total price: " + getTotalPrice());
		result.append("]");
		return result.toString();
	}
}
