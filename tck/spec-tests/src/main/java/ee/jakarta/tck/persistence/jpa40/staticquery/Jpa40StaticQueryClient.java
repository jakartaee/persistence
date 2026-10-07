/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.jpa40.staticquery;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Reference;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Jpa40StaticQueryClient extends PMClientBase {
    public JavaArchive createDeployment() throws Exception {
        String packageName = Jpa40StaticQueryClient.class.getPackageName();
        String[] classes = {packageName + ".StaticQueryBook", packageName + ".StaticQueryBook_"};
        return createDeploymentJar("jpa_jpa40_staticquery.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests that a static query reference preserves a null argument in the
     * first position and binds it to its named query parameter. The explicit
     * IS NULL predicate makes the title filter optional.
     */
    @Test
    public void nullFirstQueryArgumentTest() {
        assertSearch(null, "fiction", List.of(1, 2));
    }

    /**
     * Tests that a static query reference preserves a null argument in the
     * second position without losing the preceding non-null argument.
     */
    @Test
    public void nullSecondQueryArgumentTest() {
        assertSearch("Alpha", null, List.of(1));
    }

    /**
     * Tests that a static query reference may contain only null arguments.
     * Both explicit IS NULL predicates evaluate to true.
     */
    @Test
    public void allNullQueryArgumentsTest() {
        assertSearch(null, null, List.of(1, 2, 3));
    }

    /**
     * Tests the same static query reference with non-null arguments, so that
     * both predicates restrict the result set.
     */
    @Test
    public void nonNullQueryArgumentsTest() {
        assertSearch("Beta", "fiction", List.of(2));
    }

    /**
     * Tests that a single array-valued argument is preserved as one list
     * element, regardless of the number of elements in the array.
     */
    @Test
    public void singleArrayQueryArgumentTest() {
        for (String[] titles : new String[][] {{"Alpha", "Beta"}, {"Alpha"}, {}}) {
            var reference = StaticQueryBook_.searchByTitles(titles);
            assertEquals(List.of(String[].class), reference.getParameterTypes());
            assertEquals(List.of("titles"), reference.getParameterNames());
            assertEquals(1, reference.getArguments().size());
            assertSame(titles, reference.getArguments().get(0));
            assertUnmodifiableArguments(reference);
        }
    }

    /**
     * Tests that a null array-valued argument produces one null list element.
     */
    @Test
    public void nullArrayQueryArgumentTest() {
        var reference = StaticQueryBook_.searchByTitles(null);
        assertEquals(1, reference.getArguments().size());
        assertNull(reference.getArguments().get(0));
        assertUnmodifiableArguments(reference);
    }

    /**
     * Tests that the argument helper copies the argument container while
     * preserving null elements and the identity of array-valued arguments.
     */
    @Test
    public void argumentArrayCopyTest() {
        String[] titles = {"Alpha", "Beta"};
        Object[] arguments = {titles, null};
        var list = Reference.arguments(arguments);
        arguments[0] = "changed";
        arguments[1] = "changed";

        assertEquals(2, list.size());
        assertSame(titles, list.get(0));
        assertNull(list.get(1));
        assertThrows(UnsupportedOperationException.class, () -> list.set(0, "changed"));
        assertThrows(UnsupportedOperationException.class, () -> list.add("extra"));
    }

    /**
     * Tests that an empty argument array produces an unmodifiable empty list.
     */
    @Test
    public void emptyArgumentArrayTest() {
        var list = Reference.arguments(new Object[0]);
        assertEquals(List.of(), list);
        assertThrows(UnsupportedOperationException.class, () -> list.add("extra"));
    }

    /**
     * Tests that a null argument container is rejected, unlike null elements.
     */
    @Test
    public void nullArgumentArrayTest() {
        assertThrows(NullPointerException.class, () -> Reference.arguments(null));
    }

    /**
     * Tests that a static statement reference preserves a null argument and
     * binds it to its positional parameter when executing an update.
     */
    @Test
    public void nullStatementArgumentTest() {
        var reference = StaticQueryBook_.updateTitle(1, null);
        assertEquals(List.of(Integer.class, String.class), reference.getParameterTypes());
        assertEquals(List.of("id", "title"), reference.getParameterNames());
        assertEquals(Arrays.asList(1, null), reference.getArguments());
        assertUnmodifiableArguments(reference);

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int updated = getEntityManager().createStatement(reference).execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, updated);
        assertNull(getEntityManager().find(StaticQueryBook.class, 1).getTitle());
        assertEquals("Beta", getEntityManager().find(StaticQueryBook.class, 2).getTitle());
    }

    private void assertSearch(String title, String category, List<Integer> expectedIds) {
        var reference = StaticQueryBook_.search(title, category);
        assertEquals(List.of(String.class, String.class), reference.getParameterTypes());
        assertEquals(List.of("title", "category"), reference.getParameterNames());
        assertEquals(Arrays.asList(title, category), reference.getArguments());
        assertUnmodifiableArguments(reference);

        List<Integer> ids = getEntityManager().createQuery(reference).getResultList()
                .stream().map(StaticQueryBook::getId).toList();
        assertEquals(expectedIds, ids);
    }

    private void assertUnmodifiableArguments(Reference reference) {
        assertThrows(UnsupportedOperationException.class, () -> reference.getArguments().set(0, "changed"));
        assertThrows(UnsupportedOperationException.class, () -> reference.getArguments().add("extra"));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new StaticQueryBook(1, "Alpha", "fiction"));
        getEntityManager().persist(new StaticQueryBook(2, "Beta", "fiction"));
        getEntityManager().persist(new StaticQueryBook(3, "Gamma", "tech"));
        transaction.commit();
        getEntityManager().clear();
    }
}
