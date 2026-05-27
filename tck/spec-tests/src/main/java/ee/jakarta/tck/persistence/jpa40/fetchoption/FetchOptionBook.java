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

package ee.jakarta.tck.persistence.jpa40.fetchoption;

import jakarta.persistence.Entity;
import jakarta.persistence.Fetch;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "Jpa40FetchOptionBook")
@Table(name = "JPA40_FETCH_OPTION_BOOK")
public class FetchOptionBook {
    @Id
    private Integer id;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    private FetchOptionPublisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch
    private FetchOptionPublisher defaultFetchPublisher;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(type = FetchType.LAZY)
    private FetchOptionPublisher lazyFetchPublisher;

    public FetchOptionBook() {
    }

    public FetchOptionBook(Integer id, String title, FetchOptionPublisher publisher) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
    }

    public FetchOptionBook(Integer id, String title, FetchOptionPublisher publisher,
            FetchOptionPublisher defaultFetchPublisher, FetchOptionPublisher lazyFetchPublisher) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.defaultFetchPublisher = defaultFetchPublisher;
        this.lazyFetchPublisher = lazyFetchPublisher;
    }

    public FetchOptionPublisher getPublisher() {
        return publisher;
    }

    public FetchOptionPublisher getDefaultFetchPublisher() {
        return defaultFetchPublisher;
    }

    public FetchOptionPublisher getLazyFetchPublisher() {
        return lazyFetchPublisher;
    }
}
