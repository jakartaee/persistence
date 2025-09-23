/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.versioning;

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/*
 * Member
 */

@Entity
@Table(name = "MEMBER")
public class Member implements java.io.Serializable {

	private int memberId;

	private Integer version;

	private String memberName;

	private boolean duesPaid;

	private BigInteger donation;

	public Member() {
	}

	public Member(int memberId, String memberName, boolean duesPaid) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.duesPaid = duesPaid;
	}

	public Member(int memberId, String memberName, boolean duesPaid, BigInteger donation) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.duesPaid = duesPaid;
		this.donation = donation;
	}

	// ===========================================================
	// getters and setters for the state fields

	@Id
	@Column(name = "MEMBER_ID")
	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	@Version
	@Column(name = "VERSION")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer i) {
		version = i;
	}

	@Column(name = "MEMBER_NAME", length = 255)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Column(name = "DUES")
	public boolean isDuesPaid() {
		return duesPaid;
	}

	public void setDuesPaid(boolean duesPaid) {
		this.duesPaid = duesPaid;
	}

	@Column(name = "DONATION", precision = 18, scale = 0, table = "MEMBER")
	public BigInteger getDonation() {
		return donation;
	}

	public void setDonation(BigInteger donation) {
		this.donation = donation;
	}

}
