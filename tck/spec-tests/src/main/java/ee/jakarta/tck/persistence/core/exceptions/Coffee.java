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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.exceptions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COFFEE")
public class Coffee implements java.io.Serializable {

	private Integer id;

	private String brandName;

	private float price;

	public Coffee() {
	}

	public Coffee(Integer id, String brandName, float price) {
		this.id = id;
		this.brandName = brandName;
		this.price = price;
	}

	@Id
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "BRANDNAME")
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String bName) {
		this.brandName = bName;
	}

	@Column(name = "PRICE")
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String toString() {
		return "Coffee id=" + getId() + ", brandName=" + getBrandName() + ", price=" + getPrice();
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof Coffee))
			return false;

		Coffee c = (Coffee) o;

		boolean result = false;

		if (this.getId() == c.getId() && this.getBrandName().equals(c.getBrandName())
				&& this.getPrice() == c.getPrice()) {
			result = true;
		}

		return result;

	}

	@Override
	public int hashCode() {
		return this.getId() + this.getBrandName().hashCode() + new Float(this.getPrice()).hashCode();
	}

}
