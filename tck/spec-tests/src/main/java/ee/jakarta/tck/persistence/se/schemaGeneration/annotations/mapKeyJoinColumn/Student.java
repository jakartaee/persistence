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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.mapKeyJoinColumn;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.MapKeyJoinColumns;
import jakarta.persistence.Table;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "SCHEMAGENMKJCSTUDENT")
public class Student implements java.io.Serializable {

	private static final long serialVersionUID = 22L;

	@Id
	@Column(name = "STUDENTID")
	private int id;

	@ManyToMany
	@JoinTable(name = "SCHEMAGENMKJC_ENROLLMENTS",
			joinColumns = @JoinColumn(name = "STUDENT_ID"),
			inverseJoinColumns = @JoinColumn(name = "SEMESTER_ID"))
	@MapKeyJoinColumn(name = "COURSE_KEY",
			foreignKey = @ForeignKey(name = "COURSEKEYCONSTRAINT", value = ConstraintMode.CONSTRAINT,
					foreignKeyDefinition = "FOREIGN KEY (COURSE_KEY) REFERENCES SCHEMAGENMKJCCOURSE (COURSEID)"))
	private Map<Course, Semester> enrollments = new HashMap<>();

	@ManyToMany
	@JoinTable(name = "SCHEMAGENMKJC_RETAKES",
			joinColumns = @JoinColumn(name = "STUDENT_ID"),
			inverseJoinColumns = @JoinColumn(name = "SEMESTER_ID"))
	@MapKeyJoinColumns(value = @MapKeyJoinColumn(name = "COURSE_KEY"),
			foreignKey = @ForeignKey(name = "COURSEKEYSCONSTRAINT", value = ConstraintMode.CONSTRAINT,
					foreignKeyDefinition = "FOREIGN KEY (COURSE_KEY) REFERENCES SCHEMAGENMKJCCOURSE (COURSEID)"))
	private Map<Course, Semester> retakes = new HashMap<>();

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

	public Map<Course, Semester> getRetakes() {
		return retakes;
	}

	public void setRetakes(Map<Course, Semester> retakes) {
		this.retakes = retakes;
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
