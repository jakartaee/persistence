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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Entity containing new JPA date and time types.
 */
@Entity
@Table(name = "DATATYPES_DATE_TIME")
// Query on exact value match.
@NamedQuery(name = "DateTimeEntity.findByLocalDate", query = "SELECT e FROM DateTimeEntity e WHERE e.localDate = :date")
@NamedQuery(name = "DateTimeEntity.findByLocalTime", query = "SELECT e FROM DateTimeEntity e WHERE e.localTimeAttr = :time")
@NamedQuery(name = "DateTimeEntity.findByLocalDateTime", query = "SELECT e FROM DateTimeEntity e WHERE e.localDateTime = :dateTime")
@NamedQuery(name = "DateTimeEntity.findByOffsetTime", query = "SELECT e FROM DateTimeEntity e WHERE e.offsetTime = :time")
@NamedQuery(name = "DateTimeEntity.findByOffsetDateTime", query = "SELECT e FROM DateTimeEntity e WHERE e.offsetDateTime = :dateTime")
@NamedQuery(name = "DateTimeEntity.findByInstant", query = "SELECT e FROM DateTimeEntity e WHERE e.instantAttr = :instant")
@NamedQuery(name = "DateTimeEntity.findByYear", query = "SELECT e FROM DateTimeEntity e WHERE e.yearAttr = :year")
// Query on values range match.
@NamedQuery(name = "DateTimeEntity.findLocalDateRange", query = "SELECT e FROM DateTimeEntity e WHERE e.localDate > :min AND e.localDate < :max")
@NamedQuery(name = "DateTimeEntity.findLocalTimeRange", query = "SELECT e FROM DateTimeEntity e WHERE e.localTimeAttr > :min AND e.localTimeAttr < :max")
@NamedQuery(name = "DateTimeEntity.findLocalDateTimeRange", query = "SELECT e FROM DateTimeEntity e WHERE e.localDateTime > :min AND e.localDateTime < :max")
@NamedQuery(name = "DateTimeEntity.findOffsetTimeRange", query = "SELECT e FROM DateTimeEntity e WHERE e.offsetTime > :min AND e.offsetTime < :max")
@NamedQuery(name = "DateTimeEntity.findOffsetDateTimeRange", query = "SELECT e FROM DateTimeEntity e WHERE e.offsetDateTime > :min AND e.offsetDateTime < :max")
@NamedQuery(name = "DateTimeEntity.findByInstantRange", query = "SELECT e FROM DateTimeEntity e WHERE e.instantAttr > :min AND e.instantAttr < :max")
@NamedQuery(name = "DateTimeEntity.findByYearRange", query = "SELECT e FROM DateTimeEntity e WHERE e.yearAttr > :min AND e.yearAttr < :max")
public class DateTimeEntity implements java.io.Serializable {
    private static final long serialVersionUID = 22L;

    /** Entity primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /** Java 8 Instant attribute.
     * From Jakarta Persistence 3.2
     **/
    private Instant instantAttr;

    /** Java 8 LocalDate attribute. */
    private LocalDate localDate;

    // LOCALTIME is MySQL keyword so using another name for this attribute.
    /** Java 8 LocalTime attribute. */
    private LocalTime localTimeAttr;

    /** Java 8 LocalDateTime attribute. */
    private LocalDateTime localDateTime;

    /** Java 8 OffsetTime attribute. */
    private OffsetTime offsetTime;

    /** Java 8 OffsetDateTime attribute. */
    private OffsetDateTime offsetDateTime;

    /** Java 8 Instant attribute.
     * From Jakarta Persistence 3.2
     **/
    private Year yearAttr;

    /**
     * Creates an instance of Java 9 date and time entity. Entity attributes are not
     * initialized.
     */
    public DateTimeEntity() {
    }

    /**
     * Creates an instance of Java 9 date and time entity. Entity attributes are
     * initialized using provided values.
     *
     * @param id             date and time entity primary key
     * @param instant      	 Java 8 Instant attribute
     * @param localDate      Java 8 LocalDate attribute
     * @param localTime      Java 8 LocalTime attribute
     * @param localDateTime  Java 8 LocalDateTime attribute
     * @param offsetTime     Java 8 OffsetTime attribute
     * @param offsetDateTime Java 8 OffsetDateTime attribute
     * @param year      	 Java 8 Year attribute
     */
    public DateTimeEntity(Long id, Instant instant, LocalDate localDate, LocalTime localTime, LocalDateTime localDateTime,
                          OffsetTime offsetTime, OffsetDateTime offsetDateTime, Year year) {
        // bug 27376147:as a result of GenerationType.Auto,
        // Client is picking incorrect ID values
        // this.id = id;

        this.instantAttr = instant;
        this.localDate = localDate;
        this.localTimeAttr = localTime;
        this.localDateTime = localDateTime;
        this.offsetTime = offsetTime;
        this.offsetDateTime = offsetDateTime;
        this.yearAttr = year;
    }

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

    /**
     * Get Java 8 Instant attribute.
     *
     * @return Instant attribute
     */
    java.time.Instant getInstantAttr() {
        return instantAttr;
    }

    /**
     * Set Java 8 Instant attribute.
     *
     * @param instantAttr Instant attribute to set
     */
    void setInstantAttr(java.time.Instant instantAttr) {
        this.instantAttr = instantAttr;
    }

    /**
     * Get Java 8 LocalDate attribute.
     *
     * @return LocalDate attribute
     */
    java.time.LocalDate getLocalDate() {
        return localDate;
    }

    /**
     * Set Java 8 LocalDate attribute.
     *
     * @param localDate LocalDate attribute to set
     */
    void setLocalDate(java.time.LocalDate localDate) {
        this.localDate = localDate;
    }

    /**
     * Get Java 8 LocalTime attribute.
     *
     * @return LocalTime attribute
     */
    java.time.LocalTime getLocalTime() {
        return localTimeAttr;
    }

    /**
     * Set Java 8 LocalTime attribute.
     *
     * @param localTime LocalTime attribute to set
     */
    void setLocalTime(java.time.LocalTime localTime) {
        this.localTimeAttr = localTime;
    }

    /**
     * Get Java 8 LocalDateTime attribute.
     *
     * @return LocalDateTime attribute
     */
    java.time.LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Get Java 8 LocalDateTime attribute.
     *
     * @param localDateTime LocalDateTime attribute to set
     */
    void setLocalDateTime(java.time.LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    /**
     * Get Java 8 OffsetTime attribute.
     *
     * @return OffsetTime attribute
     */
    java.time.OffsetTime getOffsetTime() {
        return offsetTime;
    }

    /**
     * Set Java 8 OffsetTime attribute.
     *
     * @param offsetTime OffsetTime attribute to set
     */
    void setOffsetTime(java.time.OffsetTime offsetTime) {
        this.offsetTime = offsetTime;
    }

    /**
     * Get Java 8 OffsetDateTime attribute.
     *
     * @return OffsetDateTime attribute
     */
    java.time.OffsetDateTime getOffsetDateTime() {
        return offsetDateTime;
    }

    /**
     * Set Java 8 OffsetDateTime attribute.
     *
     * @param offsetDateTime OffsetDateTime attribute to set
     */
    void setOffsetDateTime(java.time.OffsetDateTime offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }

    /**
     * Get Java 8 Year attribute.
     *
     * @return Year attribute
     */
    java.time.Year getYearAttr() {
        return yearAttr;
    }

    /**
     * Set Java 8 Year attribute.
     *
     * @param yearAttr Year attribute to set
     */
    void setYearAttr(java.time.Year yearAttr) {
        this.yearAttr = yearAttr;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DateTimeEntity) {
            final DateTimeEntity other = (DateTimeEntity) object;
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
        result.append(',');
        result.append("instant=").append(instantAttr != null ? instantAttr.toString() : "null");
        result.append(',');
        result.append("localDate=").append(localDate != null ? localDate.toString() : "null");
        result.append(',');
        result.append("localTime=").append(localTimeAttr != null ? localTimeAttr.toString() : "null");
        result.append(',');
        result.append("localDateTime=").append(localDateTime != null ? localDateTime.toString() : "null");
        result.append(',');
        result.append("offsetTime=").append(offsetTime != null ? offsetTime.toString() : "null");
        result.append(',');
        result.append("offsetDateTime=").append(offsetDateTime != null ? offsetDateTime.toString() : "null");
        result.append(',');
        result.append("year=").append(yearAttr != null ? yearAttr.toString() : "null");
        result.append(']');
        return result.toString();
    }

}
