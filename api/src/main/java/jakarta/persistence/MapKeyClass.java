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
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the type of the map key for associations of type
 * {@link java.util.Map}.  The map key can be a basic type, an
 * embeddable class, or an entity. If the map is specified using Java
 * generics, the {@code MapKeyClass} annotation and associated
 * type need not be specified; otherwise they must be specified.
 * 
 * <p> The {@code MapKeyClass} annotation is used in conjunction
 * with {@link ElementCollection} or one of the collection-valued
 * relationship annotations ({@link OneToMany} or {@link ManyToMany}).
 * The {@link MapKey} annotation is not used when {@code MapKeyClass}
 * is specified and vice versa.
 *
 * <p>Example 1:
 * <pre>
 *    &#064;Entity
 *    public class Item {
 *       &#064;Id int id;
 *       ...
 *       &#064;ElementCollection(targetClass=String.class)
 *       &#064;MapKeyClass(String.class)
 *       Map images;  // map from image name to image filename
 *       ...
 *    }
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *    // MapKeyClass and target type of relationship can be defaulted
 *
 *    &#064;Entity
 *    public class Item {
 *       &#064;Id int id;
 *       ...
 *       &#064;ElementCollection
 *       Map&#060;String, String&#062; images; 
 *        ...
 *     }
 * </pre>
 *
 * <p>Example 3:
 * <pre>
 *     &#064;Entity
 *     public class Company {
 *        &#064;Id int id;
 *        ...
 *        &#064;OneToMany(targetEntity=com.example.VicePresident.class)
 *        &#064;MapKeyClass(com.example.Division.class)
 *        Map organization;
 *     }
 * </pre>
 *
 * <p>Example 4:
 * <pre>
 *     // MapKeyClass and target type of relationship are defaulted
 *
 *     &#064;Entity
 *     public class Company {
 *        &#064;Id int id;
 *        ...
 *        &#064;OneToMany
 *        Map&#060;Division, VicePresident&#062; organization;
 *     }
 * </pre>
 *
 * @see ElementCollection 
 * @see OneToMany
 * @see ManyToMany
 * @since 2.0
 */

@Target( { METHOD, FIELD })
@Retention(RUNTIME)
public @interface MapKeyClass {
	/**
	 * (Required) The type of the map key.
	 */
	Class<?> value();
}
