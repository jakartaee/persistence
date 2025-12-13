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
//     Gavin King       - 4.0

package jakarta.persistence.query;

import jakarta.persistence.FindOption;
import jakarta.persistence.TypedQueryReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A reference to a query declared using a {@link StaticQuery}
 * or {@link StaticNativeQuery} annotation. An instance of this
 * class is usually instantiated by a generated method of the
 * {@linkplain jakarta.persistence.metamodel.StaticMetamodel
 * static metamodel} for the annotated program element.
 *
 * @param <R> The result type of the query
 *
 * @since 4.0
 */
public class StaticQueryReference<R>
        implements TypedQueryReference<R> {
    private final Class<?> annotatedClass;
    private final String annotatedMemberName;
    private final Class<R> resultType;
    private final String name;
    private final List<Class<?>> parameterTypes;
    private final List<String> parameterNames;
    private final List<Object> arguments;
    private final List<FindOption> options;
    private final Map<String, Object> hints;

    public StaticQueryReference(
            String queryName,
            Class<?> annotatedClass,
            String annotatedMemberName,
            Class<R> resultType,
            List<Class<?>> parameterTypes,
            List<String> parameterNames,
            List<Object> arguments,
            Map<String, Object> hints,
            FindOption... options) {
        this.name = queryName;
        this.annotatedClass = annotatedClass;
        this.annotatedMemberName = annotatedMemberName;
        this.resultType = resultType;
        this.parameterTypes = parameterTypes;
        this.parameterNames = parameterNames;
        this.arguments = arguments;
        this.options = List.of(options);
        this.hints = hints;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<? extends R> getResultType() {
        return resultType;
    }

    @Override
    public Map<String, Object> getHints() {
        return hints;
    }

    @Override
    public List<FindOption> getOptions() {
        return options;
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public List<String> getParameterNames() {
        return parameterNames;
    }

    @Override
    public List<Object> getArguments() {
        return arguments;
    }

    public Class<?> getAnnotatedClass() {
        return annotatedClass;
    }

    public String getAnnotatedMemberName() {
        return annotatedMemberName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if ((!(obj instanceof StaticQueryReference<?> that))) {
            return false;
        }
        else {
            return Objects.equals(this.resultType, that.resultType)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.parameterTypes, that.parameterTypes)
                && Objects.equals(this.parameterNames, that.parameterNames)
                && Objects.equals(this.arguments, that.arguments)
                && Objects.equals(this.options, that.options);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultType, name, parameterTypes, parameterNames, arguments, options);
    }

    @Override
    public String toString() {
        return "StaticQueryReference["
                + "resultType=" + resultType + ", "
                + "name=" + name + ", "
                + "parameterNames=" + parameterNames + ", "
                + "arguments=" + arguments + ", "
                + "options=" + options + ']';
    }
}
