/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@NamedNativeQueries({
		@NamedNativeQuery(name = "findDecafSQLCoffees", query = "Select c.ID, c.BRANDNAME, c.PRICE from COFFEE c where c.BRANDNAME like '%decaf%' ", resultClass = ee.jakarta.tck.persistence.core.entitytest.apitests.Coffee.class) })
@NamedQueries({
		@NamedQuery(name = "findDecafCoffees", query = "Select c from Coffee c where c.brandName like '%decaf%'") })

@MappedSuperclass()
public abstract class CoffeeMappedSC implements java.io.Serializable {

	private String brandName;

	protected CoffeeMappedSC() {
	}

	protected CoffeeMappedSC(String brandName) {
		this.brandName = brandName;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Column(name = "BRANDNAME")
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String bName) {
		this.brandName = bName;
	}

}
