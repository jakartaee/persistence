/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Gavin King      - 4.0

package jakarta.persistence.sql;

import jakarta.persistence.TupleElement;

/**
 * Supertype of objects which map a column or columns of a
 * JDBC {@link java.sql.ResultSet} to a Java type returned
 * by the query. In particular, a {@link CompoundMapping}
 * or {@link TupleMapping} packages a list of
 * {@code MappingElement}s.
 *
 * @param <T> The type returned
 *
 * @since 4.0
 */
public sealed interface MappingElement<T> extends TupleElement<T>
        permits EntityMapping, ColumnMapping, ConstructorMapping  {
}
