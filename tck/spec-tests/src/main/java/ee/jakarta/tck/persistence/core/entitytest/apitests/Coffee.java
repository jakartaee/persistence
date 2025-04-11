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

package ee.jakarta.tck.persistence.core.entitytest.apitests;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

@NamedNativeQueries({
		@NamedNativeQuery(name = "findAllSQLCoffees2", query = "select ID, BRANDNAME, PRICE from COFFEE c", resultClass = ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee.class),
		@NamedNativeQuery(name = "findAllSQLCoffees", query = "select ID, BRANDNAME, PRICE from COFFEE c", resultSetMapping = "CoffeeResult"),
		@NamedNativeQuery(name = "xmlOverridesNamedNativeQuery", query = "select ID, BRANDNAME, PRICE from COFFEE c", resultClass = ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee.class) })
@SqlResultSetMapping(name = "CoffeeResult", entities = @EntityResult(entityClass = ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee.class))
@NamedQueries({
		@NamedQuery(name = "findAllCoffees", query = "Select c from Coffee c", lockMode = LockModeType.PESSIMISTIC_READ),
		@NamedQuery(name = "findAllNewCoffees", query = "Select NEW ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee(c.id, c.brandName, c.price) from Coffee c where c.price <> 0"),
		@NamedQuery(name = "xmlOverridesNamedQuery", query = "Select c from Coffee c ") })
@Entity
@Table(name = "COFFEE")
public class Coffee extends CoffeeMappedSC {

	private Integer id;

	private Float price;

	public Coffee() {
		super();
	}

	public Coffee(Integer id, String brandName, float price) {
		super(brandName);
		this.id = id;
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

	@Column(name = "PRICE")
	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode() + this.getBrandName().hashCode();
	}

	public boolean equals(Object o) {
		Coffee other;
		boolean same = true;

		if (!(o instanceof Coffee)) {
			return false;
		}
		other = (Coffee) o;

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
