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

import java.util.function.Consumer;

/**
 * Represents the registration of an entity lifecycle event
 * listener with the {@link EntityManagerFactory}. This object
 * may be used to {@linkplain #cancel} the registration.
 *
 * @see EntityManagerFactory#addListener(Class, Class, Consumer)
 *
 * @since 4.0
 */
public interface EntityListenerRegistration {
    /**
     * Remove the listener.
     */
    void cancel();
}
