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

package ee.jakarta.tck.persistence.core.annotations.access.property;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "DATATYPES2")
@Access(AccessType.PROPERTY)
public class DataTypes2 implements java.io.Serializable {

	private java.util.Date id;

	private java.sql.Time timeData;

	private java.sql.Timestamp tsData;

	public DataTypes2() {
	}

	public DataTypes2(java.util.Date id) {
		this.id = id;
	}

	public DataTypes2(java.util.Date id, java.sql.Time timeData, java.sql.Timestamp tsData) {
		this.id = id;

		this.timeData = timeData;
		this.tsData = tsData;
	}

	@Id
	@Column(name = "DATATYPES2_ID")
	@Temporal(TemporalType.DATE)
	public java.util.Date getId() {
		return id;
	}

	public void setId(java.util.Date id) {
		this.id = id;
	}

	@Column(name = "TIMEDATA")
	public java.sql.Time getTimeData() {
		return timeData;
	}

	public void setTimeData(java.sql.Time timeData) {
		this.timeData = timeData;
	}

	@Column(name = "TSDATA")
	public java.sql.Timestamp getTsData() {
		return tsData;
	}

	public void setTsData(java.sql.Timestamp tsData) {
		this.tsData = tsData;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getTimeData() != null) {
			result.append(", TimeData: " + getTimeData());
		} else {
			result.append(", TimeData: null");
		}
		if (getTsData() != null) {
			result.append(", Timestamp: " + getTsData());
		} else {
			result.append(", Timestamp: null");
		}
		result.append("]");
		return result.toString();
	}

}
