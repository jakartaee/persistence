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

package ee.jakarta.tck.persistence.core.derivedid.ex3b;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

/**
 *
 * @author Raja Perumal
 */
@Entity
public class DID3bDependent implements Serializable {
	@AttributeOverrides({
			@AttributeOverride(name = "empPK.firstName", column = @Column(name = "FIRSTNAME", updatable = false)),
			@AttributeOverride(name = "empPK.lastName", column = @Column(name = "LASTNAME", updatable = false)),
			@AttributeOverride(name = "name", column = @Column(name = "NAME", updatable = false)) })
	@EmbeddedId
	DID3bDependentId id;

	@MapsId("empPK")
	@JoinColumns({ @JoinColumn(name = "FIRSTNAME", referencedColumnName = "FIRSTNAME"),
			@JoinColumn(name = "LASTNAME", referencedColumnName = "LASTNAME") })
	@ManyToOne
	DID3bEmployee emp;

	public DID3bDependent() {
	}

	public DID3bDependent(DID3bDependentId id, DID3bEmployee emp) {
		this.id = id;
		this.emp = emp;
	}

	public DID3bDependentId getId() {
		return id;
	}

	public void setId(DID3bDependentId id) {
		this.id = id;
	}

	public DID3bEmployee getEmp() {
		return emp;
	}

	public void setEmp(DID3bEmployee emp) {
		this.emp = emp;
	}
}
