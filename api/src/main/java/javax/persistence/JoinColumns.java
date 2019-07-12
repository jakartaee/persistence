/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - Java Persistence 2.1
//     Linda DeMichiel - Java Persistence 2.0

package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.persistence.ConstraintMode.PROVIDER_DEFAULT;

/**
 * Specifies the mapping for composite foreign keys. This annotation 
 * groups <code>JoinColumn</code> annotations for the same relationship.
 *
 * <p> When the <code>JoinColumns</code> annotation is used, 
 * both the <code>name</code> and the <code>referencedColumnName</code> elements 
 * must be specified in each such <code>JoinColumn</code> annotation.
 *
 * <pre>
 *
 *    Example:
 *    &#064;ManyToOne
 *    &#064;JoinColumns({
 *        &#064;JoinColumn(name="ADDR_ID", referencedColumnName="ID"),
 *        &#064;JoinColumn(name="ADDR_ZIP", referencedColumnName="ZIP")
 *    })
 *    public Address getAddress() { return address; }
 * </pre>
 *
 * @see JoinColumn
 * @see ForeignKey
 *
 * @since Java Persistence 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface JoinColumns {

    /**
     * The join columns that map the relationship.
     */
    JoinColumn[] value();

    /**
     *  (Optional) Used to specify or control the generation of a
     *  foreign key constraint when table generation is in effect. 
     *  If both this element and the <code>foreignKey</code> element 
     *  of any of the <code>JoinColumn</code> elements are specified, 
     *  the behavior is undefined.  If no foreign key annotation element
     *  is specified in either location, the persistence provider's
     *  default foreign key strategy will apply.
     *
     *  @since Java Persistence 2.1
     */
    ForeignKey foreignKey() default @ForeignKey(PROVIDER_DEFAULT);
}
