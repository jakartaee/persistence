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

package ee.jakarta.tck.persistence.core.relationship.defaults;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/*
 * AnnualReview
 */

@Entity
public class AnnualReview implements java.io.Serializable {

	// Instance variables
	private Integer id;

	private Integer service;

	public AnnualReview() {
	}

	public AnnualReview(Integer id, Integer service) {
		this.id = id;
		this.service = service;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	public Integer getAid() {
		return id;
	}

	public void setAid(Integer id) {
		this.id = id;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

}
