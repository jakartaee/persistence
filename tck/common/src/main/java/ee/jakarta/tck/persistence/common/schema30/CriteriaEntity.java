/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.common.schema30;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

// Criteria query tests
@Entity
@Table(name="CRITERIA_TEST_TABLE")
public class CriteriaEntity {

    @Id
    private long id;

    @Column(name = "STR_VAL_1")
    private String strVal1;
    @Column(name = "STR_VAL_2")
    private String strVal2;

    @Column(name = "INT_VAL")
    private Integer intVal;

    @Column(name = "TIME_VAL")
    private LocalTime timeVal;

    @Column(name = "DATE_VAL")
    private LocalDate dateVal;

    @ElementCollection
    @CollectionTable(name = "CRITERIA_TEST_COLTABLE", joinColumns = @JoinColumn(name = "ent_id"))
    private Collection<String> colVal;

    public CriteriaEntity() {
    }

    public CriteriaEntity(Long id,
                        String strVal1,
                        String strVal2,
                        Integer intVal,
                        LocalTime timeVal,
                        LocalDate dateVal,
                        Collection<String> colVal) {
        this.id = id;
        this.strVal1 = strVal1;
        this.strVal2 = strVal2;
        this.intVal = intVal;
        this.timeVal = timeVal;
        this.dateVal = dateVal;
        this.colVal = colVal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStrVal1() {
        return strVal1;
    }

    public void setStrVal1(String strVal1) {
        this.strVal1 = strVal1;
    }

    public String getStrVal2() {
        return strVal2;
    }

    public void setStrVal2(String strVal2) {
        this.strVal2 = strVal2;
    }

    public Integer getIntVal() {
        return intVal;
    }

    public void setIntVal(Integer intVal) {
        this.intVal = intVal;
    }

    public LocalTime getTimeVal() {
        return timeVal;
    }

    public void setTimeVal(LocalTime timeVal) {
        this.timeVal = timeVal;
    }

    public LocalDate getDateVal() {
        return dateVal;
    }

    public void setDateVal(LocalDate dateVal) {
        this.dateVal = dateVal;
    }

}
