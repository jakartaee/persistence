/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany;

import java.lang.System.Logger;
import java.util.Collection;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "BEJB_MXM_BI_BTOB")
public class B implements java.io.Serializable {

	private static final Logger logger = (Logger) System.getLogger(A.class.getName());

	// ===========================================================
	// instance variables
	@Id
	protected String id;

	@Basic
	protected String name;

	@Basic
	protected int value;

	// ===========================================================
	// relationship fields

	@ManyToMany(targetEntity = ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.A.class, mappedBy = "bCol", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Collection aCol = new java.util.ArrayList();

	// ===========================================================
	// constructors

	public B() {
		logger.log(Logger.Level.TRACE, "Entity B no arg constructor");
	}

	public B(String id, String name, int value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public B(String id, String name, int value, Collection aCol) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.aCol = aCol;
	}

	// ==========================================================
	// Business Methods for Test Cases

	public Collection getACol() {
		logger.log(Logger.Level.TRACE, "getACol");
		return aCol;
	}

	public void setACol(Collection aCol) {
		logger.log(Logger.Level.TRACE, "setACol");
		this.aCol = aCol;
	}

	public String getBId() {
		return id;
	}

	public String getBName() {
		return name;
	}

	public int getBValue() {
		return value;
	}
}
