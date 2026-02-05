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
//     Gavin King      - 4.0

package jakarta.persistence.sql;

import static java.util.Objects.requireNonNull;

/**
 * Maps columns of a JDBC {@link java.sql.ResultSet} to parameters
 * of the constructor of a Java class.
 *
 * @param targetClass The Java class which declares the constructor
 * @param arguments Mappings assigned to the parameters of the constructor
 * @param <T> The type of the Java class
 *
 * @see jakarta.persistence.ConstructorResult
 *
 * @since 4.0
 */

public record ConstructorMapping<T>(Class<T> targetClass, MappingElement<?>[] arguments)
        implements MappingElement<T>, ResultSetMapping<T> {

    public ConstructorMapping(Class<T> targetClass, MappingElement<?>[] arguments) {
        requireNonNull(targetClass, "targetClass is required");
        requireNonNull(arguments, "arguments are required");
        if (arguments.length == 0) {
            throw new IllegalArgumentException("at least one argument is required");
        }
        for (var element : arguments) {
            requireNonNull(element, "argument is required");
        }
        this.targetClass = targetClass;
        this.arguments = arguments.clone();
    }

    @Override
    public MappingElement<?>[] arguments() {
        return arguments.clone();
    }

    /**
     * The Java class which declares the constructor.
     */
    @Override
    public Class<T> getJavaType() {
        return targetClass;
    }

    /**
     * Always returns {@code null}.
     */
    @Override
    public String getAlias() {
        return null;
    }

    /**
     * Construct a new instance.
     * @param targetClass The Java class which declares the constructor
     * @param arguments Mappings assigned to the parameters of the constructor
     * @param <T> The type of the Java class
     */
    public static <T> ConstructorMapping<T> of(Class<T> targetClass, MappingElement<?>... arguments) {
        return new ConstructorMapping<>(targetClass, arguments);
    }

    /**
     * The Java class which declares the constructor.
     */
    @Override
    public Class<T> type() {
        return targetClass;
    }
}
