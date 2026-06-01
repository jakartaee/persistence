/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.spi;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.security.ProtectionDomain;
import java.util.Map;

/**
 * Performs standard and provider-specific enhancement of managed
 * classes. An instance of {@link ClassTransformer} is encouraged
 * to:
 * <ul>
 * <li>to each entity class that lacks a constructor with no parameters,
 *     add a {@code protected} constructor with no parameters that
 *     initializes every field to its standard default value 
 * <li>remove {@code final} modifiers from entity classes, fields,
 *     and methods, and
 * <li>perform additional provider-specific enhancements as necessary
 *     to support lazy loading, proxying, and so on.
 * </ul>
 * <p>
 * A {@link ClassTransformer} is specific to a persistence provider,
 * and a class transformed by a given provider might not be usable
 * with a different provider.
 * <p>
 * A persistence provider supplies an instance of this interface to
 * the {@link PersistenceUnitInfo#addTransformer} method and returns
 * an instance from {@link PersistenceProvider#getClassTransformer}.
 * The supplied transformer instance is called to transform entity
 * class files when they are loaded or redefined. The transformation
 * occurs before the class is defined by the Java Virtual Machine.
 *
 * @apiNote This is an SPI interface forming part of the Jakarta EE
 * container / persistence provider contract. It is not intended for
 * direct use by application programs.
 *
 * @since 1.0
 */
public interface ClassTransformer {

    /**
     * Invoked when a class is being loaded or redefined.
     * The implementation of this method may transform the 
     * supplied class file and return a new replacement class 
     * file.
     *
     * @param loader  the defining loader of the class to be 
     *        transformed, may be null if the bootstrap loader
     * @param className  the name of the class in the internal form 
     *        of fully qualified class and interface names
     * @param classBeingRedefined  if this is a redefine, the 
     *        class being redefined, otherwise null
     * @param protectionDomain  the protection domain of the 
     *        class being defined or redefined
     * @param classfileBuffer  the input byte buffer in class 
     *        file format - must not be modified
     * @return a well-formed class file buffer (the result of 
     *         the transform), or null if no transform is performed
     * @throws TransformerException  if the input does
     *         not represent a well-formed class file
     */
    @Nullable
    byte[] transform(@Nullable ClassLoader loader,
                     @Nonnull String className,
                     @Nullable Class<?> classBeingRedefined,
                     @Nonnull ProtectionDomain protectionDomain,
                     @Nonnull byte[] classfileBuffer)
        throws TransformerException;
}
