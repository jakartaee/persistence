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

package ee.jakarta.tck.persistence.core.types.primarykey.compound;

import java.lang.System.Logger;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(ee.jakarta.tck.persistence.core.types.primarykey.compound.CompoundPK3.class)
@Table(name = "PKEY")
public class TestBean3 implements java.io.Serializable {

	private static final Logger logger = (Logger) System.getLogger(TestBean3.class.getName());

	@Id
	private Integer pmIDInteger;

	@Id
	private String pmIDString;

	@Id
	private Float pmIDFloat;

	private String brandName;

	private float price;

	public TestBean3() {
	}

	public TestBean3(Integer pmIDInteger, String pmIDString, Float pmIDFloat, String brandName, float price) {
		this.pmIDInteger = pmIDInteger;
		this.pmIDString = pmIDString;
		this.pmIDFloat = pmIDFloat;
		this.brandName = brandName;
		this.price = price;
	}

	public void ping() {
		logger.log(Logger.Level.TRACE, "[TestBean] ping()");
	}

}
