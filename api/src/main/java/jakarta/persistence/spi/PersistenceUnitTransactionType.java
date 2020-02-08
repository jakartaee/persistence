/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.spi;

/**
 * Specifies whether entity managers created by the {@link
 * jakarta.persistence.EntityManagerFactory} will be JTA or
 * resource-local entity managers.
 *
 * @since 1.0
 */
public enum PersistenceUnitTransactionType {

    /** JTA entity managers will be created. */
    JTA,
	
    /** Resource-local entity managers will be created. */
    RESOURCE_LOCAL
}
