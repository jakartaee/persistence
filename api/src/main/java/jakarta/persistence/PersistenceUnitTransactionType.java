/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 3.2
package jakarta.persistence;

/**
 * Enumerates the possible approaches to transaction management in Jakarta
 * Persistence. The approach to transaction management is determined by the
 * {@linkplain PersistenceConfiguration#transactionType() configuration of
 * a persistence unit}, and is common across all entity managers and entity
 * agents for the given persistence unit. An {@link EntityHandler} may be
 * either:
 * <ul>
 * <li>a {@linkplain #JTA} entity manager or entity agent, where transaction
 *     management is done via JTA (Jakarta Transactions), or
 * <li>a {@linkplain #RESOURCE_LOCAL resource-local} entity manager or entity
 *     agent, where transaction management is a responsibility of the
 *     persistence provider and the transaction lifecycle is controlled via
 *     the {@link EntityTransaction} interface.
 * </ul>
 *
 * @since 3.2
 */
public enum PersistenceUnitTransactionType {
    /**
     * Transaction management via JTA (Jakarta Transactions).
     */
    JTA,
    /**
     * Resource-local transaction management.
     */
    RESOURCE_LOCAL
}
