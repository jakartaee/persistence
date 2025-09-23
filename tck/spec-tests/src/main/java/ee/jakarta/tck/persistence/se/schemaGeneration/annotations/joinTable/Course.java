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
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEMAGENCOURSE")
public class Course implements java.io.Serializable {
	private static final long serialVersionUID = 22L;

	int courseId;

	String courseName;

	List<Student> students;

	public Course() {
	}

	public Course(int id, String name) {
		this.courseId = id;
		this.courseName = name;
	}

	@Id
	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int id) {
		this.courseId = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@JoinTable(name = "SCHEMAGEN_COURSE_STUDENT", joinColumns = @JoinColumn(name = "COURSE_ID"), inverseJoinColumns = @JoinColumn(name = "STUDENT_ID"), foreignKey = @ForeignKey(name = "COURSEIDCONSTRAINT", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (COURSE_ID) REFERENCES SCHEMAGENCOURSE (COURSEID)"), inverseForeignKey = @ForeignKey(name = "STUDENTIDCONSTRAINT", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (STUDENT_ID) REFERENCES SCHEMAGENSTUDENT (STUDENTID)"))
	@ManyToMany(cascade = CascadeType.ALL)
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public int hashCode() {

		return this.courseId;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof Course)) {
			Course course = (Course) obj;
			result = (course.getCourseId() == this.getCourseId());
		}
		return result;
	}

}
