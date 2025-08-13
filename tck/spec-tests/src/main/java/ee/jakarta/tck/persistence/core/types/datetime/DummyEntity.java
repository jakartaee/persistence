/*
 * Copyright (c) 2017, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.types.datetime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Dummy JPA entity used to verify schema generation process.
 */
@Entity
@Table(name = "DT_DUMMY_ENTITY")
public class DummyEntity implements java.io.Serializable {
    private static final long serialVersionUID = 22L;

    /**
     * Creates an instance of dummy entity. Entity attributes are not initialized.
     */
    public DummyEntity() {
    }

    /**
     * Creates an instance of dummy entity. Entity attributes are initialized using
     * provided values.
     *
     * @param id dummy entity primary key
     */
    public DummyEntity(Long id) {
        this.id = id;
    }

    /** Entity primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /**
     * Get entity primary key.
     *
     * @return primary key
     */
    public Long getId() {
        return id;
    }

    /**
     * Set entity primary key.
     *
     * @param id primary key to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DummyEntity) {
            final DummyEntity other = (DummyEntity) object;
            return (this.id == null && other.id == null) || this.id != null && this.id.equals(other.id);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName()).append('[');
        result.append("id=").append(id != null ? id.toString() : "null");
        result.append(']');
        return result.toString();
    }

}
