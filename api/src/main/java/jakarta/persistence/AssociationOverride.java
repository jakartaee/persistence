/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Petros Splinakis - 2.2
//     Linda DeMichiel - 2.0 - Version 2.0 (October 1 - 2013)
//     Specification available from https://projects.eclipse.org/projects/ee4j.jpa

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static jakarta.persistence.ConstraintMode.PROVIDER_DEFAULT;

/**
 * Used to override a mapping for an entity relationship.
 *
 * <p> May be applied to an entity that extends a mapped superclass to
 * override a relationship mapping defined by the mapped
 * superclass. If not specified, the association is mapped the same as
 * in the original mapping. When used to override a mapping defined by
 * a mapped superclass, <code>AssociationOverride</code> is applied to
 * the entity class.
 *
 * <p> May be used to override a relationship mapping from an
 * embeddable within an entity to another entity when the embeddable
 * is on the owning side of the relationship. When used to override a
 * relationship mapping defined by an embeddable class (including an
 * embeddable class embedded within another embeddable class),
 * <code>AssociationOverride</code> is applied to the field or
 * property containing the embeddable.
 *
 * <p> When <code>AssociationOverride</code> is used to override a
 * relationship mapping from an embeddable class, the
 * <code>name</code> element specifies the referencing relationship
 * field or property within the embeddable class. To override mappings
 * at multiple levels of embedding, a dot (".") notation syntax must
 * be used in the <code>name</code> element to indicate an attribute
 * within an embedded attribute.  The value of each identifier used
 * with the dot notation is the name of the respective embedded field
 * or property.
 * 
 * <p> When <code>AssociationOverride</code> is applied to override
 * the mappings of an embeddable class used as a map value,
 * "<code>value.</code>" must be used to prefix the name of the
 * attribute within the embeddable class that is being overridden in
 * order to specify it as part of the map value.
 *
 * <p> If the relationship mapping is a foreign key mapping, the
 * <code>joinColumns</code> element is used.  If the relationship
 * mapping uses a join table, the <code>joinTable</code> element must
 * be specified to override the mapping of the join table and/or its
 * join columns.
 *
 * <pre>
 *    Example 1: Overriding the mapping of a relationship defined by a mapped superclass
 *
 *    &#064;MappedSuperclass
 *    public class Employee {
 *        ...
 *        &#064;ManyToOne
 *        protected Address address;
 *        ...
 *    }
 *    
 *    &#064;Entity 
 *        &#064;AssociationOverride(name="address", 
 *                             joinColumns=&#064;JoinColumn(name="ADDR_ID"))
 *        // address field mapping overridden to ADDR_ID foreign key
 *    public class PartTimeEmployee extends Employee {
 *        ...
 *    }
 * </pre>
 *
 * <pre>
 *    Example 2: Overriding the mapping for phoneNumbers defined in the ContactInfo class
 *
 *    &#064;Entity
 *    public class Employee {
 *        &#064;Id int id;
 *        &#064;AssociationOverride(
 *          name="phoneNumbers",
 *          joinTable=&#064;JoinTable(
 *             name="EMPPHONES",
 *             joinColumns=&#064;JoinColumn(name="EMP"),
 *             inverseJoinColumns=&#064;JoinColumn(name="PHONE")
 *          )
 *        )
 *        &#064;Embedded ContactInfo contactInfo;
 *       ...
 *    }
 * 
 *    &#064;Embeddable
 *    public class ContactInfo {
 *        &#064;ManyToOne Address address; // Unidirectional
 *        &#064;ManyToMany(targetEntity=PhoneNumber.class) List phoneNumbers;
 *    }
 * 
 *    &#064;Entity
 *    public class PhoneNumber {
 *        &#064;Id int number;
 *        &#064;ManyToMany(mappedBy="contactInfo.phoneNumbers")
 *        Collection&#060;Employee&#062; employees;
 *     }
 *    </pre>
 *
 * @see Embedded
 * @see Embeddable
 * @see MappedSuperclass
 * @see AttributeOverride
 *
 * @since 1.0 
 */
@Repeatable(AssociationOverrides.class)
@Target({TYPE, METHOD, FIELD}) 
@Retention(RUNTIME)

public @interface AssociationOverride {

    /**
     * (Required) The name of the relationship property whose mapping is
     * being overridden if property-based access is being used,
     * or the name of the relationship field if field-based access is used.
     */
    String name();

    /**
     * The join column(s) being mapped to the persistent attribute(s).
     * The <code>joinColumns</code> elements must be specified if a
     * foreign key mapping is used in the overriding of the mapping of
     * the relationship.  The <code>joinColumns</code> element must
     * not be specified if a join table is used in the overriding of
     * the mapping of the relationship.
     */
    JoinColumn[] joinColumns() default {};

    /**
     *  (Optional) Used to specify or control the generation of a
     *   foreign key constraint for the columns corresponding to the
     *   <code>joinColumns</code> element when table generation is in
     *   effect.  If both this element and the <code>foreignKey</code>
     *   element of any of the <code>joinColumns</code> elements are
     *   specified, the behavior is undefined.  If no foreign key
     *   annotation element is specified in either location, the
     *   persistence provider's default foreign key strategy will
     *   apply.
     *
     *  @since 2.1
     */
    ForeignKey foreignKey() default @ForeignKey(PROVIDER_DEFAULT);

    /**
     * The join table that maps the relationship.
     * The <code>joinTable</code> element must be specified if a join table 
     * is used in the overriding of the mapping of the
     * relationship.  The <code>joinTable</code> element must not be specified
     * if a foreign key mapping is used in the overriding of
     * the relationship.
     *
     * @since 2.0
     */
    JoinTable joinTable() default @JoinTable;
}
