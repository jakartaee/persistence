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

package ee.jakarta.tck.persistence.core.callback.inheritance;

import ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF;
import ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * All callbacks are defined in entity superclass. The super class of this
 * entity is not an entity.
 */

@Entity
@Table(name = "PRICED_PRODUCT_TABLE")
public class PricedPartProduct_2 extends PricedPartProductCallback implements java.io.Serializable, CallbackStatusIF {
	private String id;

	private String name;

	private double price;

	private int quantity;

	private long partNumber;

	private CallbackStatusImpl callbackStatus = new CallbackStatusImpl();

	public PricedPartProduct_2() {
		super();
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "QUANTITY")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int v) {
		this.quantity = v;
	}

	@Column(name = "PNUM")
	public long getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(long v) {
		this.partNumber = v;
	}

	@Transient
	public CallbackStatusImpl getCallbackStatus() {
		return callbackStatus;
	}

	public void setCallbackStatus(CallbackStatusImpl callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	/////////////////////////////////////////////////////////////////////////
	public void setPreUpdateCalled(boolean b) {
		getCallbackStatus().setPreUpdateCalled(b);
	}

	public void setPreRemoveCalled(boolean b) {
		getCallbackStatus().setPreRemoveCalled(b);
	}

	public void setPrePersistCalled(boolean b) {
		getCallbackStatus().setPrePersistCalled(b);
	}

	public void setPostLoadCalled(boolean b) {
		getCallbackStatus().setPostLoadCalled(b);
	}

	public void setPostPersistCalled(boolean b) {
		getCallbackStatus().setPostPersistCalled(b);
	}

	public void setPostRemoveCalled(boolean b) {
		getCallbackStatus().setPostRemoveCalled(b);
	}

	public void setPostUpdateCalled(boolean b) {
		getCallbackStatus().setPostUpdateCalled(b);
	}

	public void setTestName(String s) {
		getCallbackStatus().setTestName(s);
	}

	@Transient
	public String getEntityName() {
		return "PricedPartProduct_2";
	}

	@Transient
	public String getTestName() {
		return getCallbackStatus().getTestName();
	}

	@Transient
	public boolean isPostLoadCalled() {
		return getCallbackStatus().isPostLoadCalled();
	}

	@Transient
	public boolean isPostPersistCalled() {
		return getCallbackStatus().isPostPersistCalled();
	}

	@Transient
	public boolean isPostRemoveCalled() {
		return getCallbackStatus().isPostRemoveCalled();
	}

	@Transient
	public boolean isPostUpdateCalled() {
		return getCallbackStatus().isPostUpdateCalled();
	}

	@Transient
	public boolean isPrePersistCalled() {
		return getCallbackStatus().isPrePersistCalled();
	}

	@Transient
	public boolean isPreRemoveCalled() {
		return getCallbackStatus().isPreRemoveCalled();
	}

	@Transient
	public boolean isPreUpdateCalled() {
		return getCallbackStatus().isPreUpdateCalled();
	}
}
