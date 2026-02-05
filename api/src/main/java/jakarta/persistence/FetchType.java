/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0


package jakarta.persistence;

/**
 * Defines policies for fetching data from the database.
 * <ul>
 * <li>The {@link #EAGER} policy is a requirement on the
 *     persistence provider runtime that data must be eagerly
 *     fetched.
 * <li>The {@link #LAZY} policy is a hint to the persistence
 *     provider runtime that data should be fetched lazily when
 *     it is first accessed, in the case of a managed entity
 *     associated with a persistence context, or on a call to
 *     {@link EntityAgent#fetch}, in the case of a detached
 *     entity. The implementation is permitted to eagerly fetch
 *     data for which the {@code LAZY} policy hint has been
 *     specified.
 * <li>A {@link #DEFAULT} value specifies that the fetching
 *     policy depends on the <em>default fetch type of the
 *     persistence unit for one-to-one and many-to-one
 *     associations</em>. This default fetch type is controlled
 *     via the {@code default-to-one-fetch-type} element in
 *     the {@code persistence.xml} file or by setting the
 *     {@link PersistenceConfiguration#defaultToOneFetchType()
 *     defaultToOneFetchType} of the {@link PersistenceConfiguration},
 *     and defaults to {@link FetchType#EAGER} for backward
 *     compatibility.
 * </ul>
 *
 * <p>This example specifies that a {@link ManyToOne} association
 * should, by default, be fetched lazily on first access:
 * {@snippet :
 * @ManyToOne(fetch = LAZY)
 * private Publisher publisher;
 * }
 * <p>This example specifies that an {@link ElementCollection} is
 * fetched eagerly:
 * {@snippet :
 * @ElementCollection(fetch = EAGER)
 * Set<String> topics;
 * }
 * <p>The use of {@code fetch = EAGER} often leads to fetching of
 * unwanted data and is therefore discouraged. Eager fetching
 * may be explicitly requested precisely where needed using an
 * {@link EntityGraph} or a query with {@code left join fetch}.
 *
 * @apiNote It is very strongly recommended that new applications
 * set the {@linkplain #DEFAULT default fetch type} for one-to-one
 * and many-to-one associations to {@link FetchType#LAZY} to avoid
 * accidental fetching of unnecessary data.
 *
 * @see Basic
 * @see ElementCollection
 * @see ManyToMany
 * @see OneToMany
 * @see ManyToOne
 * @see OneToOne
 * @see AttributeNode#addOption(FetchOption)
 *
 * @since 1.0
 */
public enum FetchType implements FetchOption {
    /**
     * Specifies that data can be lazily fetched.
     * <p>
     * This fetching policy is a hint to the provider.
     * A persistence provider is always permitted to
     * eagerly fetch more data than what is explicitly
     * requested by the application.
     */
    LAZY,

    /**
     * Specifies that data must be eagerly fetched.
     * <p>
     * This fetching policy is a requirement on the
     * persistence provider.
     */
    EAGER,

    /**
     * Specifies that the fetching policy depends on
     * the default fetch type of the persistence unit
     * for one-to-one and many-to-one associations.
     * @apiNote It is very strongly recommended that
     * new applications set the default fetch type to
     * {@link FetchType#LAZY} to avoid accidental
     * fetching of unnecessary data.
     * @since 4.0
     */
    DEFAULT
}
