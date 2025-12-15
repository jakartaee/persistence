/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates and others. All rights reserved.
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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares an {@linkplain AttributeNode attribute node} as a member
 * element of a {@link NamedEntityGraph}.
 *
 * <p>A {@code NamedAttributeNode} is reified at runtime as an
 * instance of {@link AttributeNode}.
 *
 * @apiNote Alternatively, use {@link NamedEntityGraphAttributeNode}
 * to declare an attribute node by annotating a field of the graphed
 * entity class.
 *
 * @see NamedEntityGraph
 * @see NamedSubgraph
 *
 * @since 2.1
 */
@Target({})
@Retention(RUNTIME)
public @interface NamedAttributeNode {

    /**
     * (Required) The name of the attribute that must be included in
     * the graph.
     */
    String value();

    /**
     * (Optional) If the attribute references a managed type that has
     * its own {@link AttributeNode}s, this element is used to refer
     * to that {@link NamedSubgraph} definition.
     *
     * <p> If the target type has inheritance, multiple subgraphs can
     * be specified. These additional subgraphs are intended to add
     * subclass-specific attributes. Superclass subgraph entries are
     * merged into subclass subgraphs.
     *
     * <p> The value of this element is the name of the subgraph as
     * specified by the {@link NamedSubgraph#name name} element of the
     * a {@link NamedSubgraph} annotation. That is, <em>it is not the
     * name of an {@linkplain EntityGraph entity graph}</em>, but the
     * name of a {@linkplain NamedSubgraph named subgraph} declared
     * within the containing {@link NamedEntityGraph} annotation. If
     * multiple subgraphs are specified due to inheritance, they are
     * referenced by this name.
     */
    String subgraph() default "";

   /**
    * (Optional) If the attribute references a Map type, this element
    * can be used to specify a subgraph for the Key in the case of an
    * entity key type. A {@code keySubgraph} can not be specified
    * without the {@code Map} attribute also being specified.
    *
    * <p> If the target type has inheritance, multiple subgraphs can
    * be specified. These additional subgraphs are intended to add
    * subclass-specific attributes. Superclass subgraph entries are
    * merged into subclass subgraphs.
    * 
    * <p> The value of this element is the name of the key subgraph as
    * specified by the {@code name} element of the corresponding
    * {@link NamedSubgraph} element. If multiple key subgraphs are
    * specified due to inheritance, they are referenced by this name.
    */
    String keySubgraph() default "";
}


