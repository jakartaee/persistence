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
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*
 * HardwareProduct
 */

@Entity
@DiscriminatorValue("HW")
public class HardwareProduct extends Product implements java.io.Serializable {

	// Instance variables
	private int modelNumber;

	public HardwareProduct() {
		super();
	}

	// ===========================================================
	// getters and setters for the state fields

	@Column(name = "MODEL", nullable = true)
	public int getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(int modelNumber) {
		this.modelNumber = modelNumber;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("modelNumber: " + getModelNumber());
		result.append(", name: " + getName());
		result.append(", price: " + getPrice());
		result.append(", quantity: " + getQuantity());
		result.append(", partNumber: " + getPartNumber());
		result.append(", wareHouse: " + getWareHouse());
		result.append("]");
		return result.toString();
	}
}
