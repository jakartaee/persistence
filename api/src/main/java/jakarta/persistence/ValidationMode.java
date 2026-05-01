/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

/**
 * The validation mode to be used by the provider for the persistence
 * unit.
 * <p>
 * A {@code ValidationMode} may be specified by:
 * <ul>
 * <li>the {@code <validation-mode>} element of {@code persistence.xml},
 * <li>the property
 *     {@value Persistence.ValidationProperties#VALIDATION_MODE}, or
 * <li>{@link PersistenceConfiguration#validationMode(ValidationMode)}.
 * </ul>
 *
 * @see jakarta.persistence.spi.PersistenceUnitInfo#getValidationMode()
 * @see PersistenceConfiguration#validationMode()
 * @see Persistence.ValidationProperties#VALIDATION_MODE
 *
 * @since 2.0
 */
public enum ValidationMode {
   
    /**
     * If a Bean Validation provider is present in the environment,
     * the persistence provider must perform the automatic validation
     * of entities. If no Bean Validation provider is present in the
     * environment, no lifecycle event validation takes place.
     * This is the default behavior.
     */
    AUTO,

    /**
     * The persistence provider must perform the lifecycle event
     * validation. It is an error if there is no Bean Validation
     * provider present in the environment.
     */
    CALLBACK,

    /**
     * The persistence provider must not perform lifecycle event
     * validation.
     */
    NONE
}
