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

package ee.jakarta.tck.persistence.se.schemaGeneration.xml.mapKeyJoinColumn;

import java.util.HashMap;
import java.util.Map;

public class Student implements java.io.Serializable {

	private static final long serialVersionUID = 22L;

	private int id;

	private Map<Course, Semester> enrollments = new HashMap<>();

	public Student() {
	}

	public Student(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Course, Semester> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(Map<Course, Semester> enrollments) {
		this.enrollments = enrollments;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return id == ((Student) o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id: " + id + "]";
	}
}
