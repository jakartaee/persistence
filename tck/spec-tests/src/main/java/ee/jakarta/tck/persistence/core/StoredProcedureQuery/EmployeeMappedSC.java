/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.StoredProcedureQuery;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.QueryHint;
import jakarta.persistence.StoredProcedureParameter;

/*
 * Employee
 */

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "getempfirstnamefromout", procedureName = "GetEmpFirstNameFromOut", hints = {
				@QueryHint(name = "fooname", value = "barvalue"),
				@QueryHint(name = "fooname2", value = "barvalue2") }, parameters = {
						@StoredProcedureParameter(type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(type = String.class, mode = ParameterMode.OUT) }) })

@MappedSuperclass()
public abstract class EmployeeMappedSC implements java.io.Serializable {

	private String firstName;

	public EmployeeMappedSC() {
	}

	public EmployeeMappedSC(String firstName) {
		this.firstName = firstName;

	}

	// ===========================================================
	// getters and setters for the state fields

	@Column(name = "FIRSTNAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
