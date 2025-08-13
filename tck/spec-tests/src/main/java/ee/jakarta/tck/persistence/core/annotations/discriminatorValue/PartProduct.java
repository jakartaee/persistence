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

package ee.jakarta.tck.persistence.core.annotations.discriminatorValue;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PartProduct")
public class PartProduct extends Product implements java.io.Serializable {
	private long partNumber;

	public PartProduct() {
		super();
	}

	@Column(name = "PNUM")
	public long getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(long v) {
		this.partNumber = v;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getName() != null) {
			result.append(", name: " + getName());
		} else {
			result.append(", name: null");
		}
		result.append(", quantity: " + getQuantity());
		result.append(", partNumber: " + getPartNumber());
		result.append("]");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	public boolean equals(Object o) {
		PartProduct other;
		boolean result = false;

		if (!(o instanceof PartProduct)) {
			return result;
		}
		other = (PartProduct) o;

		if (this.getId().equals(other.getId()) && this.getName().equals(other.getName())
				&& this.getQuantity() == other.getQuantity() && this.getPartNumber() == other.getQuantity()) {
			result = true;
		}

		return result;
	}
}
