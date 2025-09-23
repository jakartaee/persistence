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

package ee.jakarta.tck.persistence.core.derivedid.ex6b;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class DID6bMedicalHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	DID6bPersonId id;

	@MapsId
	@JoinColumns({ @JoinColumn(name = "FIRSTNAME", referencedColumnName = "FIRSTNAME"),
			@JoinColumn(name = "LASTNAME", referencedColumnName = "LASTNAME") })
	@OneToOne
	DID6bPerson patient;

	private String doctorName;

	public DID6bMedicalHistory() {
	}

	public DID6bMedicalHistory(DID6bPerson patient, String doctorName) {
		this.patient = patient;
		this.doctorName = doctorName;
	}

	public DID6bMedicalHistory(DID6bPersonId id, DID6bPerson patient, String doctorName) {
		this.id = id;
		this.patient = patient;
		this.doctorName = doctorName;
	}

	public DID6bPerson getPatient() {
		return patient;
	}

	public void setPatient(DID6bPerson patient) {
		this.patient = patient;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public DID6bPersonId getId() {
		return id;
	}

	public void setId(DID6bPersonId id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DID6bMedicalHistory other = (DID6bMedicalHistory) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		if (this.patient != other.patient && (this.patient == null || !this.patient.equals(other.patient))) {
			return false;
		}
		if ((this.doctorName == null) ? (other.doctorName != null) : !this.doctorName.equals(other.doctorName)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 47 * hash + (this.patient != null ? this.patient.hashCode() : 0);
		hash = 47 * hash + (this.doctorName != null ? this.doctorName.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "derivedpatient5.DID5MedicalHistory[patient=" + patient + "]";
	}
}
