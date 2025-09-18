/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

import java.util.Map;

/**
 * A reference to a named query declared via the {@link NamedQuery}
 * or {@link NamedNativeQuery} annotations.
 *
 * @see EntityManager#createQuery(QueryReference)
 *
 * @since 4.0
 */
public interface QueryReference {
    /**
     * The name of the query.
     */
    String getName();

    /**
     * A map keyed by hint name of all hints specified via
     * {@link NamedQuery#hints} or {@link NamedNativeQuery#hints}.
     */
    Map<String,Object> getHints();

}
