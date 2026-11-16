/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.primaryKeyJoinColumn;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEMAGENPKJCCHILD2")
@PrimaryKeyJoinColumns(value = @PrimaryKeyJoinColumn(name = "CHILD2ID", referencedColumnName = "PARENTID"),
		foreignKey = @ForeignKey(name = "PKJCSCONSTRAINT", value = ConstraintMode.CONSTRAINT,
				foreignKeyDefinition = "FOREIGN KEY (CHILD2ID) REFERENCES SCHEMAGENPKJCPARENT (PARENTID)"))
public class Child2 extends Parent {

	private static final long serialVersionUID = 22L;

	@Column(name = "CHILD2NAME")
	private String child2Name;

	public Child2() {
	}

	public Child2(int id, String child2Name) {
		super(id);
		this.child2Name = child2Name;
	}

	public String getChild2Name() {
		return child2Name;
	}

	public void setChild2Name(String child2Name) {
		this.child2Name = child2Name;
	}
}
