/*
 * Copyright (c) 2020, 2025 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

/**
 * The Jakarta Persistence API, the standard for management
 * of persistence and object/relational mapping in Java(R)
 * environments.
 */
module jakarta.persistence {

    requires java.logging;
    requires transitive java.sql;

    exports jakarta.persistence;
    exports jakarta.persistence.criteria;
    exports jakarta.persistence.metamodel;
    exports jakarta.persistence.sql;
    exports jakarta.persistence.query;
    exports jakarta.persistence.spi;

    uses jakarta.persistence.spi.PersistenceProvider;
}
