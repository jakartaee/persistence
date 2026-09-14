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

package ee.jakarta.tck.persistence.jpa40.entityagent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.LockModeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.NamedStatement;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity(name = "Jpa40AgentBook")
@Table(name = "JPA40_AGENT_BOOK")
@NamedQuery(name = AgentBook.LOCKED_QUERY,
        query = "SELECT b FROM Jpa40AgentBook b ORDER BY b.id",
        lockMode = LockModeType.PESSIMISTIC_WRITE)
@NamedStatement(
        name = AgentBook.UPDATE_TITLE,
        statement = "UPDATE Jpa40AgentBook b SET b.title = :title WHERE b.id = :id")
public class AgentBook {

    public static final String UPDATE_TITLE = "Jpa40AgentBook.updateTitle";
    public static final String LOCKED_QUERY = "Jpa40AgentBook.locked";

    @Id
    private Integer id;

    @Column(unique = true)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUBLISHER_ID")
    private AgentPublisher publisher;

    @Version
    private int version;

    public AgentBook() {
    }

    public AgentBook(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public AgentBook(Integer id, String title, AgentPublisher publisher) {
        this(id, title);
        this.publisher = publisher;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVersion() {
        return version;
    }

    public AgentPublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(AgentPublisher publisher) {
        this.publisher = publisher;
    }
}
