/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
//    Steve Ebersole  - 4.0

package jakarta.persistence;

import jakarta.persistence.spi.Discoverable;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares multiple named Jakarta Persistence query language {@linkplain Statement statements}.
 * Query names are scoped to the persistence unit.
 * The {@code NamedStatements} annotation can be applied to an entity or mapped superclass.
 *
 * @since 4.0
 */
@Target({TYPE, PACKAGE, MODULE})
@Retention(RUNTIME)
@Discoverable
public @interface NamedStatements {
	/**
	 * (Required) An array of {@link NamedStatement} annotations.
	 */
	NamedStatement[] value();
}
