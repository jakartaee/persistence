/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa40.criteria.specialized;

import jakarta.persistence.metamodel.BooleanAttribute;
import jakarta.persistence.metamodel.ComparableAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.NumericAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import jakarta.persistence.metamodel.TemporalAttribute;
import jakarta.persistence.metamodel.TextAttribute;

import java.time.LocalDate;

@StaticMetamodel(SpecializedBook.class)
public abstract class SpecializedBook_ {
    public static volatile TextAttribute<SpecializedBook> title;
    public static volatile NumericAttribute<SpecializedBook, Integer> quantity;
    public static volatile NumericAttribute<SpecializedBook, Double> price;
    public static volatile TemporalAttribute<SpecializedBook, LocalDate> publishedOn;
    public static volatile BooleanAttribute<SpecializedBook> available;
    public static volatile ComparableAttribute<SpecializedBook, String> category;
    public static volatile SetAttribute<SpecializedBook, String> tags;
    public static volatile MapAttribute<SpecializedBook, String, Integer> tagScores;
}
