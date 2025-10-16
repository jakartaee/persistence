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

@Converter(autoApply = true)
public class CharConverter implements AttributeConverter<char[], String> {

	public String convertToDatabaseColumn(char[] attribute) {
		StringBuffer sb = new StringBuffer();
		for (char c : attribute) {
			sb.append(c);
		}
		String s = sb.toString();
		System.out.println("*** Entering CharConverter:convertToDatabaseColumn[" + s + "] ***");

		if ((attribute.length == 3) && (attribute[0] == 'D') && (attribute[1] == 'o') && (attribute[2] == 'e')) {
			s = "Smith";
		}
		System.out.println("*** Leaving CharConverter:convertToDatabaseColumn[" + s + "] ***");

		return s;
	}

	public char[] convertToEntityAttribute(String dbData) {
		System.out.println("*** Entering CharConverter:convertToEntityAttribute[" + dbData.toString() + "] ***");
		char[] c = dbData.toCharArray();
		if (dbData.equals("Smith")) {
			c = new char[] { 'J', 'a', 'm', 'e', 's' };
		}
		StringBuffer sb = new StringBuffer();
		for (char c1 : c) {
			sb.append(c1);
		}
		System.out.println("*** Leaving CharConverter:convertToEntityAttribute[" + sb.toString() + "] ***");
		return c;
	}
}
