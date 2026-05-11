/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

@NamedQuery(
        name = DescriptorBook.QUERY,
        query = "SELECT b FROM Jpa40DescriptorBook b WHERE b.title = :title",
        resultClass = DescriptorBook.class)
@NamedNativeQuery(
        name = DescriptorBook.NATIVE_QUERY,
        query = "SELECT ID, TITLE FROM JPA40_DESCRIPTOR_BOOK WHERE TITLE = ?",
        resultClass = DescriptorBook.class)
@NamedStatement(
        name = DescriptorBook.STATEMENT,
        statement = "UPDATE Jpa40DescriptorBook b SET b.title = :title WHERE b.id = :id")
package ee.jakarta.tck.persistence.jpa40.nameddescriptor;

import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.NamedStatement;
