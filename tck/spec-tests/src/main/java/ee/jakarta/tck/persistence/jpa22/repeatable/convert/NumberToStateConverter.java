/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.repeatable.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NumberToStateConverter implements AttributeConverter<Integer, String> {

	public String convertToDatabaseColumn(Integer attribute) {
		System.out.println("*** Entering NumberToStateConverter:convertToDatabaseColumn[" + attribute + "] ***");
		String value = "NumberToStateConverter:convertToDatabaseColumn";

		if (attribute.equals(1)) {
			value = "MA";
		} else if (attribute.equals(2)) {
			value = "CA";
		} else if (attribute.equals(-1)) {
			throw new RuntimeException("Exception was thrown from convertToDatabaseColumn");
		} else {
			value = attribute.toString();
		}
		System.out.println("*** Leaving NumberToStateConverter:convertToDatabaseColumn[" + value + "] ***");

		return value;
	}

	public Integer convertToEntityAttribute(String dbData) {
		System.out.println("*** Entering NumberToStateConverter:convertToEntityAttribute[" + dbData + "] ***");
		int value = 0;
		if (dbData.equals("MA")) {
			value = 1;
		} else if (dbData.equals("CA")) {
			value = 2;
		} else if (dbData.equals("-2")) {
			throw new RuntimeException("Exception was thrown from convertToEntityAttribute");
		} else {
			value = Integer.valueOf(dbData);
		}

		System.out.println("*** Entering NumberToStateConverter:convertToEntityAttribute[" + value + "] ***");
		return value;
	}

}
