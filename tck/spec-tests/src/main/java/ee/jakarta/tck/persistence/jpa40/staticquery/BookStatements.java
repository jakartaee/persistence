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

import jakarta.persistence.query.JakartaStatement;
import jakarta.persistence.query.NativeStatement;

public interface BookStatements {

    @JakartaStatement("UPDATE Jpa40StaticQueryBook b SET b.title = :title WHERE b.id = :id")
    int renameBook(String title, Integer id);

    @NativeStatement("DELETE FROM JPA40_STATIC_QUERY_BOOK WHERE ID = ?")
    int deleteBook(Integer id);
}
