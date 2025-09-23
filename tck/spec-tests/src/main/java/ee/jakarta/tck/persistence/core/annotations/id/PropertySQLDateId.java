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

package ee.jakarta.tck.persistence.core.annotations.id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES2")
public class PropertySQLDateId implements java.io.Serializable {

	protected java.sql.Date id;

	private java.sql.Date date;

	public PropertySQLDateId() {
	}

	public PropertySQLDateId(java.sql.Date id, java.sql.Date date) {
		this.id = id;
		this.date = date;
	}

	@Id
	@Column(name = "DATATYPES2_ID")
	public java.sql.Date getId() {
		return id;
	}

	public void setId(java.sql.Date id) {
		this.id = id;
	}

	@Column(name = "DATEDATA")
	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		if (getDate() != null) {
			result.append(", date: " + getDate());
		} else {
			result.append(", date: null");
		}
		result.append("]");
		return result.toString();
	}
}
