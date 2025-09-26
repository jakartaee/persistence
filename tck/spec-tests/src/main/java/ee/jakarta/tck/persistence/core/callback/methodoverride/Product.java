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

package ee.jakarta.tck.persistence.core.callback.methodoverride;

import ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF;
import ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl;
import ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "PRODUCT_TABLE")
public class Product extends CallbackStatusImpl implements java.io.Serializable, CallbackStatusIF {
	private String id;

	private String name;

	private double price;

	private int quantity;

	private long partNumber;

	private GenerictListenerImpl callbackImpl = new GenerictListenerImpl();

	public Product() {
		super();
	}

	@Transient
	public GenerictListenerImpl getCallbackImpl() {
		return callbackImpl;
	}

	public void setCallbackImpl(GenerictListenerImpl callbackImpl) {
		this.callbackImpl = callbackImpl;
	}

	/////////////////////////////////////////////////////////////////////////
	// @PrePersist
	private void prePersist() {
		getCallbackImpl().prePersist(this);
	}

	// @PostPersist
	private void postPersist() {
		getCallbackImpl().postPersist(this);
	}

	// @PreRemove
	private void preRemove() {
		getCallbackImpl().preRemove(this);
	}

	// @PostRemove
	private void postRemove() {
		getCallbackImpl().postRemove(this);
	}

	// @PreUpdate
	private void preUpdate() {
		getCallbackImpl().preUpdate(this);
	}

	// @PostUpdate
	private void postUpdate() {
		getCallbackImpl().postUpdate(this);
	}

	// @PostLoad
	private void postLoad() {
		getCallbackImpl().postLoad(this);
	}

	/////////////////////////////////////////////////////////////////////////

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
}
