/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.entitytest.apitests;

public class Bar implements java.io.Serializable {

	private Integer id;

	private String brandName;

	private Float price;

	public Bar() {
	}

	public Bar(Integer id, String brandName, float price) {
		this.id = id;
		this.brandName = brandName;
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getBrandName().hashCode() + this.getPrice().intValue();
	}

	public boolean equals(Object o) {
		Bar other;
		boolean same = true;

		if (!(o instanceof Bar)) {
			return false;
		}
		other = (Bar) o;

		if (getId() != other.getId() || !getBrandName().equals(other.getBrandName())
				|| !getPrice().equals(other.getPrice())) {
			return false;

		}

		return same;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(",  brandName: " + getBrandName());
		if (getBrandName() != null) {
			result.append(", brandName: " + getBrandName());
		} else {
			result.append(", brandName: null");
		}
		if (getPrice() != null) {
			result.append(", price: " + getPrice());
		} else {
			result.append(", price: null");
		}
		result.append("]");
		return result.toString();
	}
}
