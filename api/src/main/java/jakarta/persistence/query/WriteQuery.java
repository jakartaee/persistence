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
//     Gavin King       - 4.0

package jakarta.persistence.query;

import jakarta.persistence.QueryHint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a query expressed in the Jakarta Persistence Query Language,
 * usually an {@code UPDATE} or {@code DELETE} statement, which is
 * executed by calling {@link jakarta.persistence.Query#executeUpdate()
 * executeUpdate()}.
 *
 * <p>This annotation may be applied to any abstract method of any class
 * or interface belonging to the persistence unit, or to a method of a
 * Jakarta Data repository.
 * {@snippet :
 * interface Orders {
 *     @WriteQuery(query="update Order set cancelled = true where datePaid is null and date < :cutoffDate")
 *     void cancelUnpaidOrders(LocalDate cutoffDate);
 * }
 *}
 *
 * <p> When this annotation is applied to a method of a class or interface
 * belonging to the persistence unit, a reference to a query declared using
 * this annotation may be obtained from the static metamodel class of the
 * annotated class or interface. In addition, the query is treated as a
 * named query, where the query name is the name of the annotated method.
 * {@snippet :
 * int updated =
 *         em.createQuery(Library_._cancelUnpaidOrders_)
 *           .setParameter("cutoffDate", cutoffDate)
 *           .executeUpdate();
 * }
 *
 * <p> An implementation of Jakarta Data backed by Jakarta Persistence
 * must treat this annotation as a Jakarta Data query annotation.
 *
 * @since 4.0
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface WriteQuery {

    /**
     * The query string in the Jakarta Persistence Query Language.
     */
    String query();

    /**
     * A query timeout in milliseconds.
     * By default, there is no timeout.
     * @see jakarta.persistence.Query#setTimeout
     */
    int timeout() default -1;

    /**
     * Query properties and hints.
     * May include vendor-specific query hints.
     * @see jakarta.persistence.Query#setHint(String, Object)
     */
    QueryHint[] hints() default {};
}