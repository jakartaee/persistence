/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies how the values of a field or property are converted to
 * a {@linkplain Basic basic type}, enabling a converter defined
 * {@link Converter#autoApply() autoApply=false}, overriding the use
 * of a converter defined {@code autoApply=true}, or overriding the
 * use of a converter specified by a field or property of an embedded
 * type or inherited mapped superclass.
 *
 * <p>It is not necessary to use the {@code Basic} annotation (or
 * corresponding XML element) to specify the converted basic type.
 * Nor is it usually necessary to {@linkplain Convert#converter
 * explicitly specify} the converter class, except to disambiguate
 * cases where multiple converters would otherwise apply.
 *
 * <p>The {@code Convert} annotation should not be used to specify
 * conversion of id attributes, of version attributes, of relationship
 * attributes, or of attributes explicitly declared as
 * {@link Enumerated} or {@link Temporal}. Applications that depend
 * on such conversions are not portable.
 *
 * <p>The {@code Convert} annotation may be applied to:
 * <ul>
 * <li>a basic attribute, or
 * <li>a {@linkplain ElementCollection collection attribute} of any type
 *     other than {@link java.util.Map}, in which case the converter is
 *     applied to the elements of the collection.
 * </ul>
 * In these cases, the {@link #attributeName} must not be specified.
 *
 * <p>Alternatively, the {@code Convert} annotation may be applied to:
 * <ul>
 * <li>an {@linkplain Embedded embedded attribute},
 * <li>a {@linkplain ElementCollection collection attribute} whose
 *     element type is an embeddable type, in which case the converter
 *     is applied to the specified attribute of the embeddable instances
 *     contained in the collection
 * <li>a map collection attribute, that is, a collection attribute of
 *     type {@link java.util.Map}, in which case the converter is applied
 *     to the keys or values of the map, or to the specified attribute of
 *     the embeddable instances contained in the map, or
 * <li>an entity class which extends a {@linkplain MappedSuperclass mapped
 *     superclass}, to enable or override conversion of an inherited basic
 *     or embedded attribute.
 * </ul>
 * In these cases, the {@link #attributeName} must be specified.
 *
 * <p>To override conversion mappings at multiple levels of embedding,
 * a dot {@code .} notation form must be used in the {@link #attributeName}
 * element to indicate an attribute within an embedded attribute. The
 * value of each identifier used with the dot notation is the name of
 * the respective embedded field or property.
 *
 * <p>The dot notation may also be used with map entries:
 * <ul>
 * <li>When this annotation is applied to a map to specify conversion of
 *     a map key or value, {@code "key"} or {@code "value"}, respectively,
 *     must be used as the value of the {@link #attributeName} element to
 *     specify that it is the map key or map value that is converted.
 * <li>When this annotation is applied to a map whose key or value type
 *     is an embeddable type, the {@link #attributeName} element must be
 *     specified, and {@code "key."} or {@code "value."} (respectively)
 *     must be used to prefix the name of the attribute of the key or value
 *     type that is converted.
 * </ul>
 * 
 * <p>Example 1:  Convert a basic attribute
 * <pre>
 *    &#064;Converter
 *    public class BooleanToIntegerConverter 
 *       implements AttributeConverter&#060;Boolean, Integer&#062; {  ... }
 *
 *    &#064;Entity
 *    public class Employee {
 *        &#064;Id long id;
 *
 *        &#064;Convert(converter=BooleanToIntegerConverter.class)
 *         boolean fullTime;
 *         ...
 *    }
 * </pre>
 *
 * <p>Example 2: Auto-apply conversion of a basic attribute
 * <pre>
 *    &#064;Converter(autoApply=true)
 *    public class EmployeeDateConverter 
 *       implements AttributeConverter&#060;com.acme.EmployeeDate, java.sql.Date&#062; {  ... }
 *
 *    &#064;Entity
 *    public class Employee {
 *        &#064;Id long id;
 *        ...
 *        // EmployeeDateConverter is applied automatically
 *        EmployeeDate startDate;
 *    }
 * </pre>
 *
 * <p>Example 3: Disable conversion in the presence of an autoapply converter
 * <pre>
 *    &#064;Convert(disableConversion=true)
 *    EmployeeDate lastReview;
 * </pre>
 *
 * <p>Example 4: Apply a converter to an element collection of basic type
 * <pre>
 *    &#064;ElementCollection
 *    // applies to each element in the collection
 *    &#064;Convert(converter=NameConverter.class) 
 *    List&#060;String&#062; names;
 * </pre>
 *
 * <p>Example 5: Apply a converter to an element collection that is a map
 *               of basic values. The converter is applied to the map value.
 * <pre>
 *    &#064;ElementCollection
 *    &#064;Convert(converter=EmployeeNameConverter.class)
 *    Map&#060;String, String&#062; responsibilities;
 * </pre>
 *
 * <p>Example 6: Apply a converter to a map key of basic type
 * <pre>
 *    &#064;OneToMany
 *    &#064;Convert(converter=ResponsibilityCodeConverter.class, 
 *             attributeName="key")
 *    Map&#060;String, Employee&#062; responsibilities;
 * </pre>
 *
 * <p>Example 7: Apply a converter to an embeddable attribute
 * <pre>
 *    &#064;Embedded
 *    &#064;Convert(converter=CountryConverter.class, 
 *             attributeName="country")
 *    Address address;
 * </pre>
 *
 * <p>Example 8:  Apply a converter to a nested embeddable attribute
 * <pre>
 *    &#064;Embedded
 *    &#064;Convert(converter=CityConverter.class, 
 *             attributeName="region.city")
 *    Address address;
 * </pre>
 *
 * <p>Example 9: Apply a converter to a nested attribute of an embeddable
 *               that is a map key of an element collection
 * <pre>
 *    &#064;Entity public class PropertyRecord {
 *         ...
 *        &#064;Convert(attributeName="key.region.city", 
 *                 converter=CityConverter.class)
 *        &#064;ElementCollection
 *        Map&#060;Address, PropertyInfo&#062; parcels;
 *    }
 * </pre>
 *
 * <p>Example 10: Apply a converter to an embeddable that is a map key for
 *                a relationship
 * <pre>
 *    &#064;OneToMany
 *    &#064;Convert(attributeName="key.jobType", 
 *             converter=ResponsibilityTypeConverter.class)
 *    Map&#060;Responsibility, Employee&#062; responsibilities;
 * </pre>
 *
 * <p>Example 11: Override conversion mappings for attributes inherited from
 *                a mapped superclass
 * <pre>
 *    &#064;Entity
 *        &#064;Converts({
 *           &#064;Convert(attributeName="startDate", 
 *                    converter=DateConverter.class),
 *           &#064;Convert(attributeName="endDate", 
 *                    converter=DateConverter.class)})
 *    public class FullTimeEmployee extends GenericEmployee { ... }
 * </pre>
 *
 * @see Converter
 * @see Converts
 * @see Basic
 *
 * @since 2.1
 */
@Repeatable(Converts.class)
@Target({METHOD, FIELD, TYPE}) @Retention(RUNTIME)
public @interface Convert {

  /**
   * Specifies the {@linkplain Converter converter} to be
   * applied. This element must be explicitly specified if
   * multiple converters would otherwise apply.
   */
  Class<? extends AttributeConverter> converter() default AttributeConverter.class;

  /**
   * A name or period-separated path identifying the converted
   * attribute relative to the annotated program element.
   *
   * <p>For example:
   * <ul>
   * <li>if an {@linkplain Entity entity class} is annotated
   *     {@code @Convert(attributeName = "startDate")}, then the
   *     converter is applied to the field or property named
   *     {@code startDate} of the annotated entity class,
   * <li>if an {@linkplain Embedded embedded field} is annotated
   *     {@code @Convert(attributeName = "startDate")}, then the
   *     converter is applied to the field or property named
   *     {@code startDate} of the referenced {@linkplain
   *     Embeddable embeddable} class, or
   * <li>if an {@linkplain ElementCollection map collection}
   *     whose key type is an embeddable type is annotated
   *     {@code @Convert(attributeName="key.jobType")}, the
   *     converter is applied to the field or property named
   *     {@code jobType} if the map key class.
   * </ul>
   *
   * <p>When {@code Convert} directly annotates the converted
   * attribute, this member must not be specified. (In this case
   * the path relative to the annotated element is simply the
   * empty path.)
   */
  String attributeName() default "";

  /**
   * Disables an {@linkplain Converter#autoApply auto-apply} or
   * inherited converter. If {@code disableConversion = true},
   * the {@link #converter} element should not be specified.
   */
  boolean disableConversion() default false;
}
