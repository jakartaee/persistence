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

package ee.jakarta.tck.persistence.jpa32.metamodel;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import jakarta.persistence.sql.ResultSetMapping;

import javax.annotation.processing.Generated;

@Generated("manual")
@StaticMetamodel(MetaBook.class)
public class MetaBook_ {

    public static volatile EntityType<MetaBook> class_;

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    public static final String PUBLISHER = "publisher";

    public static final String QUERY_JPA32_META_BOOK_FIND_BY_CATEGORY = MetaBook.QUERY_BY_CATEGORY;
    public static final String GRAPH_JPA32_META_BOOK_WITH_PUBLISHER = MetaBook.GRAPH_WITH_PUBLISHER;
    public static final String MAPPING_JPA32_META_BOOK_TITLE_MAPPING = MetaBook.MAPPING_TITLE;

    public static volatile SingularAttribute<MetaBook, Integer> id;
    public static volatile SingularAttribute<MetaBook, String> title;
    public static volatile SingularAttribute<MetaBook, String> category;
    public static volatile SingularAttribute<MetaBook, MetaPublisher> publisher;

    public static volatile TypedQueryReference<MetaBook> _Jpa32MetaBook_findByCategory_;
    public static volatile EntityGraph<MetaBook> _Jpa32MetaBook_withPublisher;
    public static volatile ResultSetMapping<String> _Jpa32MetaBookTitleMapping;
}
