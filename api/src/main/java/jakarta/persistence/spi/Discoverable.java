/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package jakarta.persistence.spi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Specifies that the meta-annotated annotation type identifies
 * classes which are automatically discovered by the Jakarta EE
 * container and passed on to the persistence provider via
 * {@link PersistenceUnitInfo#getAllClassNames()}. Such classes
 * might include compiled Java types, module descriptors, and
 * package descriptors.
 * <p>
 * A persistence provider may define its own custom discoverable
 * annotation types. Classes annotated with custom discoverable
 * annotations must be included in the list of class names passed
 * by the container to the provider.
 *
 * @since 4.0
 */
@Retention(CLASS)
@Target(ANNOTATION_TYPE)
public @interface Discoverable {
}
