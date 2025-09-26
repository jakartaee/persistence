/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.generators.tablegenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.TableGenerators;

@Entity
@Table(name = "DATATYPES")
@TableGenerators({
		@TableGenerator(name = "myTableGenerator", table = "GENERATOR_TABLE", pkColumnName = "PK_COL", valueColumnName = "VAL_COL", pkColumnValue = "DT1_ID", allocationSize = 1, initialValue = 1) })
public class DataTypes implements java.io.Serializable {
	private static final long serialVersionUID = 22L;

	private int id;

	private String stringData;

	public DataTypes() {
	}

	public DataTypes(String stringData) {
		this.stringData = stringData;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "myTableGenerator")
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "CHARARRAYDATA")
	public String getStringData() {
		return stringData;
	}

	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", stringData: " + getStringData());
		result.append("]");
		return result.toString();
	}

}
