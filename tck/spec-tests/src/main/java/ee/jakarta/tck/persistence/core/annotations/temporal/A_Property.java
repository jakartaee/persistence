/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.temporal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "A_BASIC")
public class A_Property implements java.io.Serializable {

	protected String id;

	protected Date basicDate;

	protected Calendar basicCalendar;

	private List<Calendar> dates = new ArrayList<Calendar>();

	public A_Property() {
	}

	public A_Property(String id, Date basicDate, Calendar basicCalendar) {

		this.id = id;
		this.basicDate = basicDate;
		this.basicCalendar = basicCalendar;

	}

	public A_Property(String id, Date basicDate, Calendar basicCalendar, List<Calendar> dates) {
		this.id = id;
		this.basicDate = basicDate;
		this.basicCalendar = basicCalendar;
		this.dates = dates;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Basic
	@Temporal(TemporalType.DATE)
	public Calendar getBasicCalendar() {
		return basicCalendar;
	}

	public void setBasicCalendar(Calendar basicCalendar) {
		this.basicCalendar = basicCalendar;
	}

	@Basic
	@Temporal(TemporalType.DATE)
	public Date getBasicDate() {
		return basicDate;
	}

	public void setBasicDate(Date basicDate) {
		this.basicDate = basicDate;
	}

	@ElementCollection
	@CollectionTable(name = "DATES_TABLE", joinColumns = @JoinColumn(name = "ID"))
	@Temporal(TemporalType.DATE)
	@Column(name = "DATES")
	public List<Calendar> getDates() {
		return dates;
	}

	public void setDates(List<Calendar> dates) {
		this.dates = dates;
	}

	public boolean equals(Object o) {
		A_Property other;
		boolean result = false;

		if (!(o instanceof A_Property)) {
			return result;
		}
		other = (A_Property) o;

		if (this.getId().equals(other.getId()) && this.getBasicCalendar().equals(other.getBasicCalendar())
				&& this.getBasicDate().equals(other.getBasicDate())) {
			result = true;
		}

		return result;
	}

	public int hashCode() {
		int myHash;

		myHash = this.getId().hashCode() + this.getBasicCalendar().hashCode() + this.getBasicDate().hashCode();

		return myHash;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName() + "[");
		result.append("id: " + getId());
		result.append(", date: " + getBasicDate());
		result.append(", calendar: " + getBasicCalendar());
		result.append("]");
		return result.toString();
	}
}
