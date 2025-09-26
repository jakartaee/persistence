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

package ee.jakarta.tck.persistence.core.annotations.convert;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Country implements java.io.Serializable {

	// Instance variables
	@Basic
	@Convert(converter = SpaceConverter.class)
	private String country;

	@Basic
	private String code;

	public Country() {
	}

	public Country(String v1, String v2) {
		country = v1;
		code = v2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String v) {
		country = v;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String v) {
		code = v;
	}

	public boolean equals(Object o) {
		Country other;
		boolean same = false;

		if (!(o instanceof Country)) {
			return false;
		}
		other = (Country) o;

		same = this.country.equals(other.country) && (this.code.equals(other.code));

		return same;
	}

	public int hashCode() {
		int myHash;

		myHash = this.country.hashCode() + this.code.hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("country: " + getCountry());
		result.append(", code: " + getCode());
		result.append("]");
		return result.toString();
	}
}
