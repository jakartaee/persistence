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

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "COLTAB")
@AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "NAME")) })
public class A implements java.io.Serializable {

	private static final long serialVersionUID = 20L;
	protected String id;
	protected String name;

	protected List<Address> lAddress = new ArrayList<Address>();

	public A() {
	}

	public A(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public A(String id, String name, List<Address> addr) {
		this.id = id;
		this.name = name;
		this.lAddress = addr;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "COLTAB_ADDRESS", joinColumns = @JoinColumn(name = "A_ID"))
	@OrderBy("zipCode.zip DESC")
	public List<Address> getAddressList() {
		return lAddress;
	}

	public void setAddressList(List<Address> addr) {
		this.lAddress = addr;
	}

}
