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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.joinTable;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEMAGENSTUDENT")
public class Student implements java.io.Serializable {
	int studentId;

	String studentName;

	List<Course> courses;

	public Student() {
	}

	public Student(int id) {
		this.studentId = id;
	}

	public Student(int id, String name) {
		this.studentId = id;
		this.studentName = name;
	}

	@Id
	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int id) {
		this.studentId = id;
	}

	@ManyToMany(mappedBy = "students", cascade = CascadeType.ALL)
	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int hashCode() {

		return this.getStudentId() + this.getStudentName().hashCode();
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof Student)) {
			Student student = (Student) obj;
			result = (student.getStudentId() == this.getStudentId()
					&& student.getStudentName().equals(this.getStudentName()));
		}
		return result;
	}

}
