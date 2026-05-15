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

package ee.jakarta.tck.persistence.jpa40.lockscope;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.LockModeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.Table;

@Entity(name = "Jpa40LockScopeBook")
@Table(name = "JPA40_LOCK_SCOPE_BOOK")
@NamedQuery(
        name = LockScopeBook.FETCHED_LOCK_QUERY,
        query = "SELECT b FROM Jpa40LockScopeBook b JOIN FETCH b.publisher WHERE b.id = :id",
        resultClass = LockScopeBook.class,
        lockMode = LockModeType.PESSIMISTIC_WRITE,
        lockScope = PessimisticLockScope.FETCHED)
public class LockScopeBook {
    public static final String FETCHED_LOCK_QUERY = "Jpa40LockScopeBook.fetchedLock";

    @Id
    private Integer id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PUBLISHER_ID")
    private LockScopePublisher publisher;

    public LockScopeBook() {
    }

    public LockScopeBook(Integer id, String title, LockScopePublisher publisher) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LockScopePublisher getPublisher() {
        return publisher;
    }
}
