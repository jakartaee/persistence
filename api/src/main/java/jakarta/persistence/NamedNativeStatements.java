package jakarta.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares multiple named native SQL {@linkplain Statement statements}.
 * Query names are scoped to the persistence unit.
 * The {@code NamedNativeStatements} annotation can be applied to an entity or mapped superclass.
 *
 * @since 4.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface NamedNativeStatements {
	/**
	 * (Required) An array of {@link NamedNativeStatement} annotations.
	 */
	NamedNativeStatement[] value();
}
