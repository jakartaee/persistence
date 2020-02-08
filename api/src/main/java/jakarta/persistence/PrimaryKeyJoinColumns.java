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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Groups {@link PrimaryKeyJoinColumn} annotations.
 * It is used to map composite foreign keys.
 *
 * <pre>
 *    Example: ValuedCustomer subclass
 *
 *    &#064;Entity
 *    &#064;Table(name="VCUST")
 *    &#064;DiscriminatorValue("VCUST")
 *    &#064;PrimaryKeyJoinColumns({
 *        &#064;PrimaryKeyJoinColumn(name="CUST_ID", 
 *            referencedColumnName="ID"),
 *        &#064;PrimaryKeyJoinColumn(name="CUST_TYPE",
 *            referencedColumnName="TYPE")
 *    })
 *    public class ValuedCustomer extends Customer { ... }
 * </pre>
 *
 * @see ForeignKey
 *
 * @since 1.0
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)

public @interface PrimaryKeyJoinColumns {

    /** One or more <code>PrimaryKeyJoinColumn</code> annotations. */
    PrimaryKeyJoinColumn[] value();

    /**
     *  (Optional) Used to specify or control the generation of a
     *  foreign key constraint when table generation is in effect. 
     *  If both this element and the <code>foreignKey</code> element 
     *  of any of the <code>PrimaryKeyJoinColumn</code> elements are specified, 
     *  the behavior is undefined.  If no foreign key annotation element
     *  is specified in either location, the persistence provider's
     *  default foreign key strategy will apply.
     *
     *  @since 2.1
     */
    ForeignKey foreignKey() default @ForeignKey(ConstraintMode.PROVIDER_DEFAULT);

}
