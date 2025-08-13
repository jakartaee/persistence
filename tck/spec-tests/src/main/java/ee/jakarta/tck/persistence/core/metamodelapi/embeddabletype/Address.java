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

package ee.jakarta.tck.persistence.core.metamodelapi.embeddabletype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address implements java.io.Serializable {

	protected String street;

	protected String city;

	protected String state;

	@ElementCollection
	protected Collection<ZipCode> cZipcode = new ArrayList<ZipCode>();

	@ElementCollection
	protected List<ZipCode> lZipcode = new ArrayList<ZipCode>();

	@ElementCollection
	protected Map<ZipCode, String> mZipcode = new HashMap<ZipCode, String>();

	@ElementCollection
	protected Set<ZipCode> sZipcode = new HashSet<ZipCode>();

	public Address() {
	}

	public Address(String street, String city, String state) {
		this.street = street;
		this.city = city;
		this.state = state;
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

	public Set<ZipCode> getZipcodeSet() {
		return sZipcode;
	}

	public void setZipcode(Set<ZipCode> zip) {
		this.sZipcode = zip;
	}

	public Collection<ZipCode> getZipcodeCollection() {
		return cZipcode;
	}

	public void setZipcode(Collection<ZipCode> zip) {
		this.cZipcode = zip;
	}

	public List<ZipCode> getZipcodeList() {
		return lZipcode;
	}

	public void setZipcode(List<ZipCode> zip) {
		this.lZipcode = zip;
	}

	public Map<ZipCode, String> getZipcodeMap() {
		return mZipcode;
	}

	public void setZipcode(Map<ZipCode, String> zip) {
		this.mZipcode = zip;
	}

}
