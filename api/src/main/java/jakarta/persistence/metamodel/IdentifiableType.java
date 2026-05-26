/*
 * Copyright (c) 2008, 2026 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence.metamodel;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Set;

/**
 * An instance of the type {@code IdentifiableType} represents an
 * entity or mapped superclass type.
 *
 * @param <X> The represented entity or mapped superclass type.
 *
 * @since 2.0
 *
 */
public interface IdentifiableType<X> extends ManagedType<X> {
    
    /**
     * The identifier attribute of the entity or mapped superclass,
     * which must have the given type.
     * @param type  the type of the id attribute
     * @param <Y> The type of the id attribute
     * @return id attribute
     * @throws IllegalArgumentException if no id attribute of the
     *         given type is present in the identifiable type, or
     *         if the identifiable type has an id class
     */
    @Nonnull
    <Y> SingularAttribute<? super X, Y> getId(Class<Y> type);

    /**
     * The identifier attribute declared by the entity or mapped
     * superclass, with the given type.
     * @param type  the type of the declared id attribute
     * @param <Y> The type of the declared id attribute
     * @return declared id attribute
     * @throws IllegalArgumentException if no id attribute of the
     *         given type is present in the identifiable type, or
     *         if the identifiable type has an id class
     */
    @Nonnull
    <Y> SingularAttribute<X, Y> getDeclaredId(Class<Y> type);

    /**
     * The version attribute of the entity or mapped superclass,
     * which must have the given type.
     * @param type  the type of the version attribute
     * @param <Y> The type of the version attribute
     * @return version attribute
     * @throws IllegalArgumentException if no version attribute
     *         of the given type is present in the identifiable
     *         type
     */
    @Nonnull
    <Y> SingularAttribute<? super X, Y> getVersion(Class<Y> type);

    /**
     * The version attribute declared by the entity or mapped
     * superclass, with the given type.
     * @param type  the type of the declared version attribute
     * @param <Y> The type of the declared version attribute
     * @return declared version attribute
     * @throws IllegalArgumentException if no version attribute
     *         of the given type is declared by the identifiable
     *         type
     */
    @Nonnull
    <Y> SingularAttribute<X, Y> getDeclaredVersion(Class<Y> type);
    
    /**
     * Return the identifiable type representing the most
     * specific mapped superclass or entity extended by the
     * entity or mapped superclass.
     * @return an identifiable supertype of the identifiable
     *         type, or null if the identifiable type has no
     *         identifiable supertype
     */
    @Nullable
    IdentifiableType<? super X> getSupertype();

    /**
     * Whether the identifiable type has an identifier. Every
     * entity class has a well-defined identifier. A mapped
     * superclass may or may not have an identifier.
     * @return true if the type has an identifier
     * @since 4.0
     */
    boolean hasId();

    /**
     * Whether the identifiable type has a single id attribute.
     * Returns true for a {@linkplain jakarta.persistence.Id simple id}
     * or {@linkplain jakarta.persistence.EmbeddedId embedded id}, or
     * false for an {@linkplain jakarta.persistence.IdClass id class}.
     * @return true if the identifiable type has a single id attribute
     */
    boolean hasSingleIdAttribute();

    /**
     * Whether the identifiable type has a
     * {@linkplain jakarta.persistence.Version version attribute}.
     * @return true if the identifiable type has a version
     *         attribute
     */
    boolean hasVersionAttribute();

    /**
     * The persistent attributes corresponding to the fields of
     * the {@linkplain jakarta.persistence.IdClass id class} of
     * the identifiable type.
     * @return the identifier attributes
     * @throws IllegalArgumentException if the identifiable type
     *         does not have an id class
     */
    @Nonnull
    Set<SingularAttribute<? super X, ?>> getIdClassAttributes();

    /**
     * The type of the identifier of the identifiable type,
     * or null if the identifiable type is a mapped superclass
     * with no identifier attribute.
     * @return the identifier type, or null
     */
    @Nullable
    Type<?> getIdType();
}
