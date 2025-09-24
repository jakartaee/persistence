/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.convert;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/*
 * Employee as mapped superclass, which can be concrete or abstract.
 * Mapping may be overriden by subclass entities with annotation or descriptor.
 */

@MappedSuperclass()
public abstract class Employee2 {

	private int id;

	private String firstName;

	private char[] lastName;

	protected Employee2() {
	}

	protected Employee2(int id, String firstName, char[] lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	@Column(name = "IDxx")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "FIRSTNAMExx")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LASTNAMExx")
	public char[] getLastName() {
		return lastName;
	}

	public void setLastName(char[] lastName) {
		this.lastName = lastName;
	}

}
