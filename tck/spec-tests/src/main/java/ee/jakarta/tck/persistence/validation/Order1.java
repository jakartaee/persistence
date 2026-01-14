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

package ee.jakarta.tck.persistence.validation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity
@Table(name = "PURCHASE_ORDER")
public class Order1 implements java.io.Serializable {

	private int id;

    @Positive
	private int total;

	public Order1() {
	}

	public Order1(int total) {
		this.total = total;
	}

	public Order1(int id, int total) {
		this.id = id;
		this.total = total;
	}

	@Id
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "TOTAL")
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String toString() {
		return "Order id=" + getId() + ", total=" + getTotal();
	}

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Order1 order1)) return false;
        return id == order1.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
