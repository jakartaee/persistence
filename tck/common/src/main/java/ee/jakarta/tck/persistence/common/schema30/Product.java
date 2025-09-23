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

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.SecondaryTables;
import jakarta.persistence.Table;

/*
 * 
 * Product
 *	  
 */

@Entity
@Table(name = "PRODUCT_TABLE")
@SecondaryTables({ @SecondaryTable(name = "PRODUCT_DETAILS", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID")) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PRODUCT_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Product")
public class Product implements java.io.Serializable, Comparable<Product> {

	// Instance variables
	private String id;

	private String name;

	private double price;

	private int quantity;

	private long partNumber;

	private String wareHouse;

	private ShelfLife shelfLife;

	public Product() {
	}

	public Product(String id, String name, double price, int quantity, long partNumber) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.partNumber = partNumber;
	}

	// ===========================================================
	// getters and setters for State fields

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "QUANTITY")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int v) {
		this.quantity = v;
	}

	@Column(name = "PNUM")
	public long getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(long v) {
		this.partNumber = v;
	}

	@Column(name = "WHOUSE", nullable = true, table = "PRODUCT_DETAILS")
	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String v) {
		this.wareHouse = v;
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "inceptionDate", column = @Column(name = "INCEPTION", nullable = true)),
			@AttributeOverride(name = "soldDate", column = @Column(name = "SOLD", nullable = true)) })
	public ShelfLife getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(ShelfLife v) {
		this.shelfLife = v;
	}

	public boolean equals(Object o) {
		Product other;
		boolean same = true;

		if (!(o instanceof Product)) {
			return false;
		}
		other = (Product) o;

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
		result.append(", name: " + getName());
		result.append(", price: " + getPrice());
		result.append(", quantity: " + getQuantity());
		result.append(", partNumber: " + getPartNumber());
		result.append(", wareHouse: " + getWareHouse());

		result.append("]");
		return result.toString();
	}

	public int compareTo(Product p) {
		int lastCmp = Integer.valueOf(getId()).compareTo(Integer.valueOf(p.getId()));
		return (lastCmp != 0 ? lastCmp : Integer.valueOf(getId()).compareTo(Integer.valueOf(p.getId())));
	}
}
