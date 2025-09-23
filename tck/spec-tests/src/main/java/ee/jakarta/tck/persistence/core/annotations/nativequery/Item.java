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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * Item
 */

@Entity
@Table(name = "ITEM")
public class Item implements java.io.Serializable {

	// Instance variables
	private int id;

	private String itemName;

	private Order1 order1;

	public Item() {
	}

	public Item(int id, String itemName) {
		this.id = id;
		this.itemName = itemName;
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

	@Column(name = "ITEMNAME")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@OneToOne(mappedBy = "item")
	public Order1 getOrder1() {
		return order1;
	}

	public void setOrder1(Order1 v) {
		order1 = v;
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getItemName().hashCode();
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof Item)) {
			Item i = (Item) obj;
			result = (i.id == getId() && i.itemName.equals(getItemName()));
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", name: " + getItemName());
		result.append("]");
		return result.toString();
	}
}
