package jakarta.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares multiple named Jakarta Persistence query language {@linkplain Statement statements}.
 * Query names are scoped to the persistence unit.
 * The {@code NamedStatements} annotation can be applied to an entity or mapped superclass.
 *
 * @since 4.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface NamedStatements {
	/**
	 * (Required) An array of {@link NamedStatement} annotations.
	 */
	NamedStatement[] value();
}
