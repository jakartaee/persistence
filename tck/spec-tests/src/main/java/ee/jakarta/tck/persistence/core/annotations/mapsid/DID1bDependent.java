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

package ee.jakarta.tck.persistence.core.annotations.mapsid;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class DID1bDependent implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	DID1bDependentId id;

	@MapsId("empPK")
	@ManyToOne
	DID1bEmployee emp;

	public DID1bDependent() {
	}

	public DID1bDependent(DID1bDependentId id, DID1bEmployee emp) {
		this.id = id;
		this.emp = emp;
	}

	public DID1bEmployee getEmp() {
		return emp;
	}

	public void setEmp(DID1bEmployee emp) {
		this.emp = emp;
	}

	@Override
	public String toString() {
		return "ex1b.DID1bDependent[id=" + id + "]";
	}
}
