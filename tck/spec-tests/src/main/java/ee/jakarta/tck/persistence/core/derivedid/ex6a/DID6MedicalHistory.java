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

package ee.jakarta.tck.persistence.core.derivedid.ex6a;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;

/**
 *
 * @author Raja Perumal
 */
@Entity
@IdClass(DID6PersonId.class)
public class DID6MedicalHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumns({ @JoinColumn(name = "FIRSTNAME", referencedColumnName = "FIRSTNAME"),
			@JoinColumn(name = "LASTNAME", referencedColumnName = "LASTNAME") })
	private DID6Person patient;

	private String doctorName;

	public DID6MedicalHistory() {
	}

	public DID6MedicalHistory(DID6Person patient, String doctorName) {
		this.patient = patient;
		this.doctorName = doctorName;
	}

	public DID6Person getPatient() {
		return patient;
	}

	public void setPatient(DID6Person patient) {
		this.patient = patient;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (patient != null ? patient.hashCode() : 0);
		return hash;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof DID6MedicalHistory)) {
			return false;
		}
		DID6MedicalHistory other = (DID6MedicalHistory) object;
		if ((this.patient == null && other.patient != null)
				|| (this.patient != null && !this.patient.equals(other.patient))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "derivedpatient5.DID5MedicalHistory[patient=" + patient + "]";
	}
}
