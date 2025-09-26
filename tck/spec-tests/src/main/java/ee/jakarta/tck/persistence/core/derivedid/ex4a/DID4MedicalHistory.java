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

package ee.jakarta.tck.persistence.core.derivedid.ex4a;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class DID4MedicalHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumn(name = "PATIENT_SSN")
	private DID4Person patient;

	private String doctorName;

	public DID4MedicalHistory() {
	}

	public DID4MedicalHistory(DID4Person patient, String doctorName) {
		this.patient = patient;
		this.doctorName = doctorName;
	}

	public DID4Person getPatient() {
		return patient;
	}

	public void setPatient(DID4Person patient) {
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
		if (!(object instanceof DID4MedicalHistory)) {
			return false;
		}
		DID4MedicalHistory other = (DID4MedicalHistory) object;
		if ((this.patient == null && other.patient != null)
				|| (this.patient != null && !this.patient.equals(other.patient))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "derivedpatient4.DID4MedicalHistory[patient=" + patient + "]";
	}
}
