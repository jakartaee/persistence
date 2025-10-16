/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.index;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEMAGENSIMPLE", indexes = { @Index(name = "SCHEMAGENSIMPLE_SVALUE_ASC", columnList = "SVALUE"),
		@Index(name = "SCHEMAGENSIMPLE_SVALUE2_DESC", columnList = "SVALUE2 DESC"),
		@Index(name = "SCHEMAGENSIMPLE_SVALUE3", columnList = "SVALUE3", unique = true) })
public class Simple implements java.io.Serializable {

	// ===========================================================
	// instance variables
	int id;

	String sValue = null;

	String sValue2 = null;

	String sValue3 = null;

	// ===========================================================
	// constructors
	public Simple() {
	}

	public Simple(int id, String sValue) {
		this.id = id;
		this.sValue = sValue;
	}

	public Simple(int id, String sValue, String sValue2, String sValue3) {
		this.id = id;
		this.sValue = sValue;
		this.sValue2 = sValue2;
		this.sValue3 = sValue3;
	}

	@Id
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "SVALUE")
	public String getSValue() {
		return sValue;
	}

	public void setSValue(String sValue) {
		this.sValue = sValue;
	}

	@Column(name = "SVALUE2")
	public String getSValue2() {
		return sValue2;
	}

	public void setSValue2(String sValue) {
		this.sValue2 = sValue;
	}

	@Column(name = "SVALUE3")
	public String getSValue3() {
		return sValue3;
	}

	public void setSValue3(String sValue) {
		this.sValue3 = sValue;
	}

	@Override
	public boolean equals(Object o) {
		// check for self-comparison
		if (this == o)
			return true;
		if (!(o instanceof Simple))
			return false;

		Simple o1 = (Simple) o;

		boolean result = false;

		if (this.getId() == o1.getId() &&

				((this.getSValue() == null && o1.getSValue() == null) || this.getSValue().equals(o1.getSValue()))
				&& ((this.getSValue2() == null && o1.getSValue2() == null) || this.getSValue2().equals(o1.getSValue2()))
				&& ((this.getSValue3() == null && o1.getSValue3() == null)
						|| this.getSValue3().equals(o1.getSValue3()))) {
			result = true;
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", sValue: " + getSValue());
		result.append(", sValue2: " + getSValue2());
		result.append("]");
		return result.toString();
	}

}
