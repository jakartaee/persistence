/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.lob;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES")
public class DataTypes implements java.io.Serializable {

	@Id
	protected int id;

	@Lob
	@Basic
	@Column(name = "BYTEARRAYDATA")
	protected Byte[] byteArrayData;

	public DataTypes() {
	}

	public DataTypes(int id) {
		this.id = id;
	}

	public DataTypes(int id, Byte[] byteArrayData) {
		this.id = id;
		this.byteArrayData = byteArrayData;

	}

	public Integer getIdData() {
		return id;
	}

	public Byte[] getByteArrayData() {
		return byteArrayData;
	}

	public void setByteArrayData(Byte[] byteArrayData) {
		this.byteArrayData = byteArrayData;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getIdData());

		if (getByteArrayData() != null && getByteArrayData().length > 0) {
			StringBuilder sb = new StringBuilder(", ByteData: [");
			for (int i = 0; i < byteArrayData.length; i++) {
				sb.append(byteArrayData[i]);
				if (i < byteArrayData.length - 1) {
					sb.append(", ");
				}
			}
			sb.append("],");
			result.append(sb.toString());
		} else {
			result.append(", ByteData: null");
		}
		result.append("]");
		return result.toString();
	}
}
