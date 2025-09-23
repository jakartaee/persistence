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

package ee.jakarta.tck.persistence.se.cache.inherit;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
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
@Cacheable(false)
@Table(name = "PRODUCT_TABLE")
@SecondaryTables({ @SecondaryTable(name = "PRODUCT_DETAILS", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID")) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PRODUCT_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Product2")
public class Product2 implements java.io.Serializable {
	private static final long serialVersionUID = 22L;

	// Instance variables
	private String id;

	private int quantity;

	public Product2() {
	}

	public Product2(String id, int quantity) {
		this.id = id;
		this.quantity = quantity;
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

	@Column(name = "QUANTITY")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int v) {
		this.quantity = v;
	}

	public boolean equals(Object o) {
		Product2 other;
		boolean same = true;

		if (!(o instanceof Product2)) {
			return false;
		}
		other = (Product2) o;

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
		result.append(", quantity: " + getQuantity());
		result.append("]");
		return result.toString();
	}

}
