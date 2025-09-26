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

package ee.jakarta.tck.persistence.core.annotations.orderby;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class Address implements java.io.Serializable {

	protected String street;

	protected String city;

	protected String state;

	protected ZipCode zipcode;

	public Address() {
	}

	public Address(String street, String city, String state) {
		this.street = street;
		this.city = city;
		this.state = state;
	}

	public Address(String street, String city, String state, ZipCode zip) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipcode = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Embedded
	public ZipCode getZipCode() {
		return zipcode;
	}

	public void setZipCode(ZipCode zipcode) {
		this.zipcode = zipcode;
	}

	public boolean equals(Object o) {
		Address other;
		boolean result = false;

		if (!(o instanceof Address)) {
			return result;
		}
		other = (Address) o;

		if (this.getStreet().equals(other.getStreet()) && this.getCity().equals(other.getCity())
				&& this.getState().equals(other.getState())
				&& this.getZipCode().getZip().equals(other.getZipCode().getZip())) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getStreet().hashCode() + this.getCity().hashCode() + this.getState().hashCode()
				+ this.getZipCode().getZip().hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("street: " + getStreet());
		result.append(", city: " + getCity());
		result.append(", state: " + getState());
		result.append(", zip: " + getZipCode().getZip());
		result.append("]");
		return result.toString();
	}

}
