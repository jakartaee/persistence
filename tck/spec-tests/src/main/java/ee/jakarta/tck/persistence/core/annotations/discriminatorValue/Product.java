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
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCT_TABLE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PRODUCT_TYPE", discriminatorType = DiscriminatorType.STRING)
/*
 * If the DiscriminatorValue annotation is not specified, a provider-specific
 * function to generate a value representing the entity type is used for the
 * value of the discriminator column. If the DiscriminatorType is STRING, the
 * discriminator value default is the entity name.
 * //@DiscriminatorValue("Product")
 */
public class Product implements java.io.Serializable {
	private String id;

	private String name;

	private int quantity;

	public Product() {
		super();
	}

	public Product(String id, String name, int quantity) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
	}

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

	@Column(name = "QUANTITY")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int v) {
		this.quantity = v;
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
		result.append("]");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	public boolean equals(Object o) {
		Product other;
		boolean result = false;

		if (!(o instanceof Product)) {
			return result;
		}
		other = (Product) o;

		if (this.getId().equals(other.getId()) && this.getName().equals(other.getName())
				&& this.getQuantity() == other.getQuantity()) {
			result = true;
		}

		return result;
	}
}
