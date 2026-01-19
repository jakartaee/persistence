package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares a named {@linkplain Statement statement} written in native SQL.
 * Query names are scoped to the persistence unit.
 *
 * <p> The {@code NamedNativeStatement} annotation can be applied to an entity
 * class or mapped superclass.
 * {@snippet :
 * @NamedNativeStatement(name = "deleteNamedCustomers",
 *             query = "delete from Customer c where c.name like :custName")
 * @Entity
 * class Customer { ... }
 * }
 * <p> A named statement may be executed by calling
 * {@link EntityManager#createNamedStatement(String)}.
 * {@snippet :
 * em.createNamedStatement("deleteNamedCustomers")
 *             .setParameter("custName", "Smith")
 *             .execute();
 * }
 *
 * @see EntityManager#createNamedStatement(String)
 * @see EntityManager#createStatement(StatementReference)
 * @see EntityManagerFactory#addNamedStatement(String, Statement)
 * @see NamedStatement
 *
 * @since 4.0
 */
@Repeatable(NamedNativeStatements.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface NamedNativeStatement {
	/**
	 * (Required) The name used to identify the query in calls to
	 * {@link EntityManager#createNamedStatement(String)}.
	 */
	String name();

	/**
	 * (Required) The query string written in native SQL.
	 */
	String query();

	/**
	 * (Optional) Query properties and hints. May include
	 * vendor-specific query hints.
	 * @see Query#setHint
	 */
	QueryHint[] hints() default {};
}
