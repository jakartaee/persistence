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

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a many-valued association with one-to-many multiplicity.
 * 
 * <p> If the collection is defined using generics to specify the 
 * element type, the associated target entity type need not be 
 * specified; otherwise the target entity class must be specified.
 * If the relationship is bidirectional, the
 * <code> mappedBy</code> element must be used to specify the relationship field or
 * property of the entity that is the owner of the relationship.
 *
 * <p> The <code>OneToMany</code> annotation may be used within an embeddable class
 * contained within an entity class to specify a relationship to a
 * collection of entities. If the relationship is bidirectional, the
 * <code> mappedBy</code> element must be used to specify the relationship field or
 * property of the entity that is the owner of the relationship.
 *
 * When the collection is a <code>java.util.Map</code>, the <code>cascade</code> 
 * element and the <code>orphanRemoval</code> element apply to the map value.
 *
 * <pre>
 *
 *    Example 1: One-to-Many association using generics
 *
 *    // In Customer class:
 *
 *    &#064;OneToMany(cascade=ALL, mappedBy="customer")
 *    public Set&#060;Order&#062; getOrders() { return orders; }
 *
 *    In Order class:
 *
 *    &#064;ManyToOne
 *    &#064;JoinColumn(name="CUST_ID", nullable=false)
 *    public Customer getCustomer() { return customer; }
 *
 *
 *    Example 2: One-to-Many association without using generics
 *
 *    // In Customer class:
 *
 *    &#064;OneToMany(targetEntity=com.acme.Order.class, cascade=ALL,
 *                mappedBy="customer")
 *    public Set getOrders() { return orders; }
 *
 *    // In Order class:
 *
 *    &#064;ManyToOne
 *    &#064;JoinColumn(name="CUST_ID", nullable=false)
 *    public Customer getCustomer() { return customer; }
 *
 *
 *    Example 3: Unidirectional One-to-Many association using a foreign key mapping
 *
 *    // In Customer class:
 *
 *    &#064;OneToMany(orphanRemoval=true)
 *    &#064;JoinColumn(name="CUST_ID") // join column is in table for Order
 *    public Set&#060;Order&#062; getOrders() {return orders;}
 *    
 * </pre>
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)

public @interface OneToMany {

    /**
     * (Optional) The entity class that is the target
     * of the association. Optional only if the collection
     * property is defined using Java generics.
     * Must be specified otherwise.
     *
     * <p> Defaults to the parameterized type of
     * the collection when defined using generics.
     */
    Class<?> targetEntity() default void.class;

    /** 
     * (Optional) The operations that must be cascaded to 
     * the target of the association.
     * <p> Defaults to no operations being cascaded.
     *
     * <p> When the target collection is a {@link java.util.Map
     * java.util.Map}, the <code>cascade</code> element applies to the
     * map value.
     */
    CascadeType[] cascade() default {};

    /** (Optional) Whether the association should be lazily loaded or
     * must be eagerly fetched. The EAGER strategy is a requirement on
     * the persistence provider runtime that the associated entities
     * must be eagerly fetched.  The LAZY strategy is a hint to the
     * persistence provider runtime.
     */
    FetchType fetch() default FetchType.LAZY;

    /** 
     * The field that owns the relationship. Required unless 
     * the relationship is unidirectional.
     */
    String mappedBy() default "";

    /**
     * (Optional) Whether to apply the remove operation to entities that have
     * been removed from the relationship and to cascade the remove operation to
     * those entities.
     * @since 2.0
     */
    boolean orphanRemoval() default false;
}
