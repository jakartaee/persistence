/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.repeatable.namednativequery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

@NamedNativeQuery(name = "findAllSQLCoffees2", query = "select * from COFFEE", resultClass = Coffee.class)
@NamedNativeQuery(name = "findAllSQLCoffees", query = "select * from COFFEE", resultSetMapping = "CoffeeResult")
@SqlResultSetMapping(name = "CoffeeResult", entities = @EntityResult(entityClass = Coffee.class))
@NamedQuery(name = "findAllCoffees", query = "Select Distinct c from Coffee c")
@NamedQuery(name = "findAllNewCoffees", query = "Select NEW ee.jakarta.tck.persistence.jpa22.repeatable.namednativequery.Coffee(c.id, c.brandName, c.price) from Coffee c where c.price <> 0")
@Entity
@Table(name = "COFFEE")
public class Coffee implements java.io.Serializable {
	private static final long serialVersionUID = 22L;

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
}
