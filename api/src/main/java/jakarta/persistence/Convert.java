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
//     Gavin King       - 4.0
//     Petros Splinakis - 2.2
//     Linda DeMichiel  - 2.1

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
 * a dot ({@code .}) notation form must be used in the {@link #attributeName}
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
 * {@snippet :
 * @Converter
 * public class BooleanToIntegerConverter
 *         implements AttributeConverter<Boolean, Integer> {  ... }
 *
 * @Entity
 * public class Employee {
 *     @Id
 *     long id;
 *
 *     @Convert(converter = BooleanToIntegerConverter.class)
 *     boolean fullTime;
 *     ...
 * }
 * }
 *
 * <p>Example 2: Auto-apply conversion of a basic attribute
 * {@snippet :
 * @Converter(autoApply = true)
 * public class EmployeeDateConverter
 *         implements AttributeConverter<com.acme.EmployeeDate, java.sql.Date> {  ... }
 *
 * @Entity
 * public class Employee {
 *     @Id
 *     long id;
 *     ...
 *     // EmployeeDateConverter is applied automatically
 *     EmployeeDate startDate;
 * }
 * }
 *
 * <p>Example 3: Disable conversion in the presence of an autoapply converter
 * {@snippet :
 * @Convert(disableConversion = true)
 * EmployeeDate lastReview;
 * }
 *
 * <p>Example 4: Apply a converter to an element collection of basic type
 * {@snippet :
 * @ElementCollection
 * // applies to each element in the collection
 * @Convert(converter = NameConverter.class)
 * List<String> names;
 * }
 *
 * <p>Example 5: Apply a converter to an element collection that is a map
 *               of basic values. The converter is applied to the map value.
 * {@snippet :
 * @ElementCollection
 * @Convert(converter = EmployeeNameConverter.class)
 * Map<String, String> responsibilities;
 * }
 *
 * <p>Example 6: Apply a converter to a map key of basic type
 * {@snippet :
 * @OneToMany
 * @Convert(converter = ResponsibilityCodeConverter.class,
 *          attributeName = "key")
 * Map<String, Employee> responsibilities;
 * }
 *
 * <p>Example 7: Apply a converter to an embeddable attribute
 * {@snippet :
 * @Embedded
 * @Convert(converter = CountryConverter.class,
 *          attributeName = "country")
 * Address address;
 * }
 *
 * <p>Example 8:  Apply a converter to a nested embeddable attribute
 * {@snippet :
 * @Embedded
 * @Convert(converter = CityConverter.class,
 *          attributeName = "region.city")
 * Address address;
 * }
 *
 * <p>Example 9: Apply a converter to a nested attribute of an embeddable
 *               that is a map key of an element collection
 * {@snippet :
 * @Entity public class PropertyRecord {
 *     ...
 *     @Convert(attributeName = "key.region.city",
 *              converter = CityConverter.class)
 *     @ElementCollection
 *     Map<Address, PropertyInfo> parcels;
 * }
 * }
 *
 * <p>Example 10: Apply a converter to an embeddable that is a map key for
 *                a relationship
 * {@snippet :
 * @OneToMany
 * @Convert(attributeName = "key.jobType",
 *          converter = ResponsibilityTypeConverter.class)
 * Map<Responsibility, Employee> responsibilities;
 * }
 *
 * <p>Example 11: Override conversion mappings for attributes inherited from
 *                a mapped superclass
 * {@snippet :
 * @Entity
 * @Converts({
 *      @Convert(attributeName = "startDate",
 *               converter = DateConverter.class),
 *      @Convert(attributeName = "endDate",
 *               converter = DateConverter.class)})
 * public class FullTimeEmployee extends GenericEmployee { ... }
 * }
 *
 * <p>Example 12: Use of a built-in conversion between basic types:
 * {@snippet :
 * @Convert(to = BigDecimal.class)
 * @Column(precision = 10, scale = 4)
 * // maps to column of type DECIMAL(10,4)
 * double magnitude;
 *
 * @Convert(to = long.class)
 * // maps to column of type BIGINT for extra headroom
 * int count;
 *
 * @Convert(to = byte.class)
 * // maps to column of type TINYINT in legacy database
 * boolean enabled;
 * }
 *
 * @see Converter
 * @see Converts
 * @see Basic
 *
 * @since 2.1
 */
@Repeatable(Converts.class)
@Target({METHOD, FIELD, TYPE})
@Retention(RUNTIME)
public @interface Convert {

  /**
   * Specifies the {@linkplain Converter converter} to be
   * applied. This element must be explicitly specified if
   * multiple converters would otherwise apply.
   */
  Class<? extends AttributeConverter> converter() default AttributeConverter.class;

  /**
   * Specifies a basic type for the conversion. This element may be
   * specified if the persistence provider features built-in support
   * for converting between the specified type and:
   * <ul>
   * <li>the declared type of the annotated program element, in the
   *     case that no {@linkplain #converter converter} is specified,
   *     or
   * <li>the basic type output by the
   *     {@link AttributeConverter#convertToDatabaseColumn}
   *     method of the specified {@linkplain #converter converter}.
   * </ul>
   * <p>A value is converted <em>to</em> the specified basic type
   * which writing to the database, and <em>from</em> the specified
   * basic type when reading from the database.
   * <p>Both {@link #converter} and {@code to} may be specified for
   * a given program element. In this case, when writing a value to
   * the database:
   * <ul>
   * <li>the value of the target attribute of the entity is passed
   *     first to the converter, and then
   * <li>the built-in conversion is applied to the output of the
   *     converter.
   * </ul>
   * <p>When reading a value from the database:
   * <ul>
   * <li>the built-in conversion is first applied to the value read
   *     from the database, and then
   * <li>the resulting converted value is passed to the converter.
   * </ul>
   *
   * <p>The Persistence provider is required to support the following
   * built-in conversions:
   * <ul>
   * <li>all numeric conversions defined by methods of the class
   *     {@link Number} between primitive or wrapped numeric types,
   * <li>all conversions defined by {@code toString} and {@code parse}
   *     methods (for example, {@link Float#toString(float)} and
   *     {@link Float#parseFloat(String)}) between primitive or wrapped
   *     numeric types and {@link String},
   * <li>conversions between {@link java.math.BigDecimal} and
   *     {@link Double}, {@link Long}, {@code double}, or {@code long},
   *     and between {@link java.math.BigInteger} and {@link Long}
   *     or {@code long}, and
   * <li>conversion between {@link Boolean} or {@code boolean} and any
   *     integral numeric primitive or wrapper type, where {@code false}
   *     or {@code FALSE} is converted to {@code 0} and {@code true} or
   *     {@code TRUE} is converted to {@code 1}.
   * </ul>
   * <p>A provider is encouraged to support additional vendor-specific
   * built-in conversions, but such conversions are not portable between
   * providers and should not be relied upon by portable application
   * programs.
   *
   * @since 4.0
   */
  Class<?> to() default void.class;

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
   *     {@code startDate} of the referenced
   *     {@linkplain Embeddable embeddable} class, or
   * <li>if an {@linkplain ElementCollection map collection}
   *     whose key type is an embeddable type is annotated
   *     {@code @Convert(attributeName="key.jobType")}, the
   *     converter is applied to the field or property named
   *     {@code jobType} of the map key class.
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
