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

package jakarta.persistence;

import java.lang.annotation.Annotation;

/**
 * Specifies the validation groups to be validated by the
 * persistence provider before operations executed via a
 * given {@link EntityManager} or {@link EntityAgent}.
 * {@snippet :
 * var agent =
 *     factory.createEntityAgent(
 *             new ValidationGroups(PrePersist.class, PersistValidation.class),
 *             new ValidationGroups(PreDelete.class, DeleteValidation.class));
 *}
 *
 * @param event the lifecycle event when validation occurs;
 *              must be the class of a lifecycle callback
 *              annotation
 * @param groups the validation groups to be validated
 *
 * @see EntityManagerFactory#createEntityManager(EntityManager.CreationOption...)
 * @see EntityManagerFactory#createEntityAgent(EntityAgent.CreationOption...)
 *
 * @since 4.0
 */
public record ValidationGroups(Class<? extends Annotation> event, Class<?>... groups)
        implements EntityManager.CreationOption, EntityAgent.CreationOption {
}
