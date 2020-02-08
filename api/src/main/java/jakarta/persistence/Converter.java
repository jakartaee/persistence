/*
 * Copyright (c) 2011, 2020 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that the annotated class is a converter and defines its
 * scope.  A converter class must be annotated with the <code>Converter</code>
 * annotation or defined in the object/relational mapping descriptor as
 * a converter.
 *
 * <p>If the <code>autoApply</code> element is specified as
 * <code>true</code>, the persistence provider must automatically
 * apply the converter to all mapped attributes of the specified
 * target type for all entities in the persistence unit except for
 * attributes for which conversion is overridden by means of the
 * <code>Convert</code> annotation (or XML equivalent).
 *
 * <p>In determining whether a converter is applicable to an attribute,
 * the provider must treat primitive types and wrapper types as
 * equivalent.
 *
 * <p>Note that Id attributes, version attributes, relationship
 * attributes, and attributes explicitly annotated as
 * <code>Enumerated</code> or <code>Temporal</code> (or designated as
 * such via XML) will not be converted.
 *
 * <p>Note that if <code>autoApply</code> is <code>true</code>, the
 * <code>Convert</code> annotation may be used to override or disable
 * auto-apply conversion on a per-attribute basis.
 *
 * <p>If <code>autoApply</code> is <code>false</code>, only those
 * attributes of the target type for which the <code>Convert</code>
 * annotation (or corresponding XML element) has been specified will
 * be converted.
 *
 * <p>If there is more than one converter defined for the same target
 * type, the <code>Convert</code> annotation should be used to
 * explicitly specify which converter to use.
 *
 * @see AttributeConverter
 * @see Convert
 *
 * @since 2.1
 */
@Target({TYPE}) @Retention(RUNTIME)
public @interface Converter {
     boolean autoApply() default false;
}
