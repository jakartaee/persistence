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
public class IntegerConverter implements AttributeConverter<Integer, Integer> {

	public Integer convertToDatabaseColumn(Integer attribute) {
		System.out.println("*** Entering IntegerConverter:convertToDatabaseColumn[" + attribute + "] ***");
		Integer i = attribute;
		if (attribute >= 1000) {
			i = attribute + 100;
		}
		System.out.println("*** leaving IntegerConverter:convertToDatabaseColumn[" + i + "] ***");

		return i;
	}

	public Integer convertToEntityAttribute(Integer dbData) {
		System.out.println("*** Entering IntegerConverter:convertToEntityAttribute[" + dbData + "] ***");
		Integer i = dbData;
		if (dbData >= 1000) {
			i = dbData + 10;
		}
		System.out.println("*** Leaving IntegerConverter:convertToEntityAttribute[" + i + "] ***");
		return i;

	}

}
