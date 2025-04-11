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

package ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass()
public abstract class B implements java.io.Serializable {

	private static final Logger logger = (Logger) System.getLogger(B.class.getName());

	protected String name;

	@ElementCollection
	@CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
	protected List<Address> lAddress_inherited = new ArrayList<Address>();

	@ElementCollection
	@CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
	protected Map<Address, String> mAddress_inherited = new HashMap<Address, String>();

	@ElementCollection
	@CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
	Collection<Address> cAddress_inherited = new ArrayList<Address>();

	@ElementCollection
	@CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
	Set<Address> sAddress_inherited = new HashSet<Address>();

	public B() {
	}

	public B(String name) {
		this.name = name;
	}

	public Set<Address> getAddressSet() {
		logger.log(Logger.Level.TRACE, "getAddressSet");
		return sAddress_inherited;
	}

	public void setAddressSet(Set<Address> addr) {
		logger.log(Logger.Level.TRACE, "setAddressSet");
		this.sAddress_inherited = addr;
	}

	public Collection<Address> getAddressCollection() {
		logger.log(Logger.Level.TRACE, "getAddressCollection");
		return cAddress_inherited;
	}

	public void setAddressCollection(Collection<Address> addr) {
		logger.log(Logger.Level.TRACE, "setAddressCollection");
		this.cAddress_inherited = addr;
	}

	public List<Address> getAddressList() {
		logger.log(Logger.Level.TRACE, "getAddressList");
		return lAddress_inherited;
	}

	public void setAddressList(List<Address> addr) {
		logger.log(Logger.Level.TRACE, "setAddressList");
		this.lAddress_inherited = addr;
	}

	public Map<Address, String> getAddressMap() {
		logger.log(Logger.Level.TRACE, "getAddressMap");
		return mAddress_inherited;
	}

	public void setAddressMap(Map<Address, String> addr) {
		logger.log(Logger.Level.TRACE, "setAddressMap");
		this.mAddress_inherited = addr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
