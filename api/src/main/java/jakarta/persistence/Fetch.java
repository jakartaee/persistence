/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
//    Gavin King  - 4.0

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies options influencing how an attribute is fetched.
 * <ul>
 * <li>By default, the options apply when not overridden by
 *     an {@linkplain EntityGraph entity graph}. For a load
 *     graph, options specified via the {@code @Fetch}
 *     annotation are overridden when the graph has an
 *     attribute node representing the annotated attribute.
 *     For a fetch graph, options specified by {@code @Fetch}
 *     are always considered overridden.
 * <li>When a {@linkplain #graph graph name} is specified,
 *     the options apply when the attribute is fetched using
 *     the {@linkplain NamedEntityGraph named entity graph}
 *     with the specified name and the named graph contains
 *     an {@linkplain AttributeNode attribute node}
 *     representing the annotated attribute.
 * </ul>
 * <p>
 * In this example, the {@code @Fetch} annotation is equivalent
 * to having specified {@code @ManyToMany(fetch=EAGER)}:
 * {@snippet :
 * @Fetch
 * @ManyToMany
 * List<Author> authors;
 * }
 * <p>
 * In this example, additional options influencing the mechanics
 * of fetching the associated entity are specified:
 * {@snippet :
 * @Fetch(cacheStoreMode=BYPASS, batchSize = 10)
 * @ManyToMany
 * List<Author> authors;
 * }
 * <p>
 * In this example, the options only apply when the named entity
 * graph {@code BooksWithAuthors} is used:
 * {@snippet :
 * @Fetch(cacheStoreMode=BYPASS, batchSize = 10,
 *        graph = "BooksWithAuthors")
 * @ManyToMany
 * List<Author> authors;
 * }
 * <p>
 * Multiple {@code @Fetch} annotations may be specified for a
 * single attribute:
 * {@snippet :
 * @Fetch // eager by default
 * @Fetch(cacheStoreMode=BYPASS, batchSize = 10,
 *        graph = "BooksWithAuthors")
 * @ManyToMany
 * List<Author> authors;
 * }
 * At most one such {@code @Fetch} annotation may have a missing
 * {@link #graph} name.
 *
 * @see NamedEntityGraph
 *
 * @since 4.0
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Repeatable(Fetch.Fetches.class)
public @interface Fetch {
    /**
     * The name of an entity graph.
     * <ul>
     * <li>If no entity graph name is specified, the specified
     *     fetching options apply when the attribute is fetched
     *     without the use of an entity graph.
     * <li>Otherwise, the specified fetching options apply when
     *     the attribute is fetched using the entity graph with
     *     the specified name. The named graph contains an
     *     attribute node representing the annotated attribute.
     * </ul>
     * <p>
     * At most one {@code @Fetch} annotation of a given field or
     * property may have a missing {@code graph} name.
     */
    String graph() default "";

    /**
     * The names of one or more entity graphs to be attached as
     * subgraphs of the {@linkplain #graph named} entity graph
     * to the attribute node representing the annotated
     * association.
     * <p>
     * The annotated attribute must be an association.
     */
    String[] subgraph() default {};

    /**
     * The fetching policy. Overrides the value specified by the
     * {@code fetch} member of the {@link Basic}, {@link OneToOne},
     *  {@link ManyToOne}, {@link OneToMany}, {@link ManyToMany}, or
     * {@link ElementCollection} annotation.
     */
    FetchType type() default FetchType.EAGER;

    /**
     * A batch size, that is, how many entities should be fetched
     * in each request to the database. This option is always a
     * hint, and might be ignored by the persistence provider.
     */
    int batchSize() default -1;

    /**
     * Specifies whether the persistence provider should cache
     * the associated entity after fetching it from the database.
     */
    CacheStoreMode cacheStoreMode() default CacheStoreMode.USE;

    /**
     * Specifies whether the persistence provider is permitted
     * to read the associated entity data from the cache.
     */
    CacheRetrieveMode cacheRetrieveMode() default CacheRetrieveMode.USE;

    /**
     * Vendor-specific fetching hints.
     * <p>
     * Any hint not recognized by a provider is ignored.
     */
    QueryHint[] hints() default {};

    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    @interface Fetches {
        Fetch[] value();
    }
}