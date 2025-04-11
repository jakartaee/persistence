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

package ee.jakarta.tck.persistence.core.relationship.bidironexone;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class BiDir1X1Project implements java.io.Serializable {

	private long projId;

	private String name;

	private float budget;

	private BiDir1X1Person biDir1X1Person;

	public BiDir1X1Project() {
	}

	public BiDir1X1Project(long projId, String name, float budget) {
		this.projId = projId;
		this.name = name;
		this.budget = budget;
	}

	@Id
	public long getProjId() {
		return projId;

	}

	public void setProjId(long projId) {
		this.projId = projId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getBudget() {
		return budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	// Bi-directional OnePerson OneProject
	@OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
	public BiDir1X1Person getBiDir1X1Person() {
		return biDir1X1Person;
	}

	public void setBiDir1X1Person(BiDir1X1Person biDir1X1Person) {
		this.biDir1X1Person = biDir1X1Person;
	}

}
