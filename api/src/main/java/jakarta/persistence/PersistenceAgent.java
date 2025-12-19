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
//     Gavin King - 4.0

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Expresses a dependency on an {@link EntityAgent} and its
 * associated persistence unit. Every container-manager entity
 * agent is a JTA entity agent.
 *
 * @since 4.0
 */
@Repeatable(PersistenceAgents.class)
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface PersistenceAgent {

    /**
     * (Optional) The name by which the entity agent is to be
     * accessed in the environment referencing context;
     * not needed when dependency injection is used.
     */
    String name() default "";

    /**
     * (Optional) The name of the persistence unit as defined
     * in the {@code persistence.xml} file. If specified, the
     * persistence unit for the entity manager factory that is
     * accessible in JNDI must have the same name.
     */
    String unitName() default "";

    /**
     * (Optional) Properties for the container or persistence
     * provider. Vendor-specific properties may be included in
     * this set of properties. Properties that are not recognized
     * by a vendor are ignored.
     * @see EntityManagerFactory#createEntityAgent(Map)
     */
    PersistenceProperty[] properties() default {};
}
