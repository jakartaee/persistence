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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "LINEITEM_TABLE")
public class LineItem extends CallbackStatusImpl implements java.io.Serializable, CallbackStatusIF {
	private String id;

	private int quantity;

	private Order order;

	private Product product;

	private GenerictListenerImpl callbackImpl = new GenerictListenerImpl();

	public LineItem() {
		super();
	}

	public LineItem(String v1, int v2, Order v3, Product v4) {
		id = v1;
		quantity = v2;
		order = v3;
		product = v4;
	}

	public LineItem(String v1, int v2) {
		id = v1;
		quantity = v2;
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

	public void setId(String v) {
		id = v;
	}

	@Column(name = "QUANTITY")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int v) {
		quantity = v;
	}

	@ManyToOne
	@JoinColumn(name = "FK1_FOR_ORDER_TABLE")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order v) {
		order = v;
	}

	@ManyToOne
	@JoinColumn(name = "FK_FOR_PRODUCT_TABLE")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product v) {
		product = v;
	}
}
