/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.persistence.jpa40.staticquery;

import jakarta.persistence.Reference;
import jakarta.persistence.StatementReference;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import jakarta.persistence.query.StaticStatementReference;
import jakarta.persistence.query.StaticTypedQueryReference;

import javax.annotation.processing.Generated;
import java.util.List;

@Generated("manual")
@StaticMetamodel(StaticQueryBook.class)
public class StaticQueryBook_ {
    public static volatile EntityType<StaticQueryBook> class_;
    public static volatile SingularAttribute<StaticQueryBook, Integer> id;
    public static volatile SingularAttribute<StaticQueryBook, String> title;
    public static volatile SingularAttribute<StaticQueryBook, String> category;

    public static TypedQueryReference<StaticQueryBook> search(String title, String category) {
        return new StaticTypedQueryReference<>(
                StaticQueryBook.class.getName() + "#search(java.lang.String,java.lang.String)",
                StaticQueryBook.class,
                "search",
                StaticQueryBook.class,
                List.of(String.class, String.class),
                List.of("title", "category"),
                Reference.arguments(new Object[] { title, category }));
    }

    public static TypedQueryReference<StaticQueryBook> searchByTitles(String[] titles) {
        return new StaticTypedQueryReference<>(
                StaticQueryBook.class.getName() + "#searchByTitles(java.lang.String[])",
                StaticQueryBook.class,
                "searchByTitles",
                StaticQueryBook.class,
                List.of(String[].class),
                List.of("titles"),
                Reference.arguments(new Object[] { titles }));
    }

    public static StatementReference updateTitle(Integer id, String title) {
        return new StaticStatementReference(
                StaticQueryBook.class.getName() + "#updateTitle(java.lang.Integer,java.lang.String)",
                StaticQueryBook.class,
                "updateTitle",
                List.of(Integer.class, String.class),
                List.of("id", "title"),
                Reference.arguments(new Object[] { id, title }));
    }
}
