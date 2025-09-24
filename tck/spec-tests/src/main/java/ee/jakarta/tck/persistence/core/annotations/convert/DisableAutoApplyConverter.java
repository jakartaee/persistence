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

package ee.jakarta.tck.persistence.core.annotations.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Having this set to false means that these methods should not be executed
@Converter(autoApply = false)
public class DisableAutoApplyConverter implements AttributeConverter<String, String> {

	public String convertToDatabaseColumn(String attribute) {
		System.out.println("*** Entering DisableAutoApplyConverter:convertToDatabaseColumn[" + attribute + "] ***");
		attribute = "DisableAutoApplyConverter:convertToDatabaseColumn - THIS SHOULD NOT HAVE BEEN EXECUTED";
		System.out.println("*** Leaving DisableAutoApplyConverter:convertToDatabaseColumn[" + attribute + "] ***");
		return attribute;
	}

	public String convertToEntityAttribute(String dbData) {
		System.out.println("*** Entering DisableAutoApplyConverter:convertToEntityAttribute[" + dbData + "] ***");
		dbData = "AutoApplyConverter2:convertToEntityAttribute - THIS SHOULD NOT HAVE BEEN EXECUTED";
		System.out.println("*** Leaving DisableAutoApplyConverter:convertToEntityAttribute[" + dbData + "] ***");

		return dbData;
	}

}
