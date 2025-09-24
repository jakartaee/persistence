/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.math.BigDecimal;

public class Order2 implements java.io.Serializable {

	// Instance variables
	private int id;

	private double totalPrice;

	private String itemName;

	public Order2() {
	}

	public Order2(int id) {
		this.id = id;
	}

	public Order2(int id, double totalPrice) {
		this.id = id;
		this.totalPrice = totalPrice;
	}

	public Order2(int id, double totalPrice, String itemName) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.itemName = itemName;
	}

	public Order2(BigDecimal id, double totalPrice, String itemName) {
		if (id != null) {
			this.id = id.intValue();
		} else {
			throw new IllegalArgumentException("Received a null for the ID, this should not occur");
		}
		this.totalPrice = totalPrice;
		this.itemName = itemName;
	}

	public Order2(BigDecimal id, BigDecimal totalPrice, String itemName) {
		if (id != null) {
			this.id = id.intValue();
		} else {
			throw new IllegalArgumentException("Received a null for the ID, this should not occur");
		}
		if (totalPrice != null) {
			this.totalPrice = totalPrice.longValue();
		} else {
			throw new IllegalArgumentException("Received a null for the TOTALPRICE, this should not occur");
		}
		this.itemName = itemName;
	}

	// ====================================================================
	// getters and setters for State fields

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double price) {
		this.totalPrice = price;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String v) {
		itemName = v;
	}
}
