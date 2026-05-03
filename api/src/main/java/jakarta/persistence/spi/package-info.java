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

/**
 * Defines an SPI for integrating with the persistence provider
 * and with the Jakarta EE platform.
 * <ul>
 * <li>{@link jakarta.persistence.spi.PersistenceProvider} must
 *     be implemented by every persistence provider.
 * <li>{@link jakarta.persistence.spi.ClassTransformer} allows
 *     the persistence provider to peform its own custom bytecode
 *     processing in a Jakarta EE environment.
 * <li>{@link jakarta.persistence.spi.PersistenceUnitInfo}
 *     carries information about a persistence unit under the
 *     management of the Jakarta EE container.
 * <li>The {@link jakarta.persistence.spi.Discoverable}
 *     meta-annotation identifies annotation types which trigger
 *     automatic discovery of classes by the Jakarta EE container.
 * </ul>
 */
package jakarta.persistence.spi;
