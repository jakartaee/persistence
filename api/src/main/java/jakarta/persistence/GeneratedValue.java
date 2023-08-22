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
//     Gavin King      - 3.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static jakarta.persistence.GenerationType.AUTO;

/**
 * Provides for the specification of generation strategies for the
 * values of primary keys. 
 *
 * <p> The <code>GeneratedValue</code> annotation
 * may be applied to a primary key property or field of an entity or
 * mapped superclass in conjunction with the {@link Id} annotation.
 * The use of the <code>GeneratedValue</code> annotation is only
 * required to be supported for simple primary keys. Use of the
 * <code>GeneratedValue</code> annotation is not supported for derived
 * primary keys.
 *
 * <pre>
 *
 *     Example 1:
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy=SEQUENCE, generator="CUST_SEQ")
 *     &#064;Column(name="CUST_ID")
 *     public Long getId() { return id; }
 *
 *     Example 2:
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy=TABLE, generator="CUST_GEN")
 *     &#064;Column(name="CUST_ID")
 *     Long id;
 * </pre>
 *
 * @see Id
 * @see TableGenerator
 * @see SequenceGenerator
 *
 * @since 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface GeneratedValue {

    /**
     * (Optional) The primary key generation strategy
     * that the persistence provider must use to
     * generate the annotated entity primary key.
     */
    GenerationType strategy() default AUTO;

    /**
     * (Optional) The name of the primary key generator to
     * use, as specified by the {@link SequenceGenerator}
     * or {@link TableGenerator} annotation which declares
     * the generator.
     * <p> The name defaults to the entity name of the
     * entity in which the annotation occurs.
     * <p> If there is no generator with the defaulted
     * name, then the persistence provider supplies a
     * default id generator, of a type compatible with
     * the value of the strategy member.
     */
    String generator() default "";
}
