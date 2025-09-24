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

package ee.jakarta.tck.persistence.core.annotations.mapkeyclass;

import java.util.Map;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyClass;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.MapKeyJoinColumns;

@Entity
public class Student implements java.io.Serializable {

	@Id
	int id;

	String studentName;

	public Student() {
	}

	public Student(int id) {
		this.id = id;
	}

	public Student(int id, String name) {
		this.id = id;
		this.studentName = name;
	}

	/*
	 * students and semesters are many-many enrollment is really a
	 * Map<Course,Semester>, but generics aren't used due to testing of @MapKeyClass
	 * enrollment contains past and present student enrollments
	 */
	@ManyToMany(targetEntity = ee.jakarta.tck.persistence.core.annotations.mapkeyclass.Semester.class)
	@MapKeyClass(ee.jakarta.tck.persistence.core.annotations.mapkeyclass.Course.class)
	@MapKeyJoinColumns({ @MapKeyJoinColumn(name = "ENROLLMENT_KEY") })
	@JoinTable(name = "ENROLLMENTS", joinColumns = @JoinColumn(name = "STUDENT"), inverseJoinColumns = @JoinColumn(name = "SEMESTER"))
	Map enrollment;

	public Set<Course> getCourses() {
		return enrollment.keySet();
	}

	public Map getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Map enrollment) {
		this.enrollment = enrollment;
	}

	public int getId() {
		return id;
	}

	public void setId(int studentId) {
		this.id = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int hashCode() {

		return this.id;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof Student)) {
			Student student = (Student) obj;
			result = (student.id == this.id);
		}
		return result;
	}
}
