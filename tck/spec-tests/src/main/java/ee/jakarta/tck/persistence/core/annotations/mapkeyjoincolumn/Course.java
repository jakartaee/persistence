/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkeyjoincolumn;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Course implements java.io.Serializable {

	@Id
	int id;

	String courseName;

	public Course() {
	}

	public Course(int id, String name) {
		this.id = id;
		this.courseName = name;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int hashCode() {

		return this.id;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof Course)) {
			Course course = (Course) obj;
			result = (course.id == this.id);
		}
		return result;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
