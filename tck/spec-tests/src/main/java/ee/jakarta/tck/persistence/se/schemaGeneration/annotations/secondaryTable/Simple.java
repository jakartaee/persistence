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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.secondaryTable;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEMAGENSIMPLE")
@SecondaryTable(name = "SCHEMAGENSIMPLE_SECOND", pkJoinColumns = @PrimaryKeyJoinColumn(name = "SECONDARY_ID"), foreignKey = @ForeignKey(name = "MYCONSTRAINT", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (SECONDARY_ID) REFERENCES SCHEMAGENSIMPLE (SIMPLEID)"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PRODUCT_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Product")
public class Simple implements java.io.Serializable {

	// ===========================================================
	// instance variables
	int simple_id;

	// ===========================================================
	// constructors
	public Simple() {
	}

	public Simple(int id) {
		this.simple_id = id;
	}

	@Id
	public int getSimpleId() {
		return simple_id;
	}

	public void setSimpleId(int id) {
		this.simple_id = id;
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

		if (this.getSimpleId() == o1.getSimpleId()) {
			result = true;
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getSimpleId());
		result.append("]");
		return result.toString();
	}

}
