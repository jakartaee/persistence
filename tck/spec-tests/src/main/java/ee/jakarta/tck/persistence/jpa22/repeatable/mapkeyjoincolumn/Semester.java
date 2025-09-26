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

package ee.jakarta.tck.persistence.jpa22.repeatable.mapkeyjoincolumn;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Semester implements java.io.Serializable {
	private static final long serialVersionUID = 22L;

	@Id
	int id;

	public Semester() {
	}

	public Semester(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int semester) {
		this.id = semester;
	}

	public int hashCode() {

		return id;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof Semester)) {
			Semester semester = (Semester) obj;
			result = (semester.getId() == (this.id));
		}
		return result;
	}
}
