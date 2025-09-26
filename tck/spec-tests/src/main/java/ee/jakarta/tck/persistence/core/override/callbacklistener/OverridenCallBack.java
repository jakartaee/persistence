/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.callbacklistener;

import ee.jakarta.tck.persistence.core.override.util.CallBackCounts;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NOENTITYLISTENER_TABLE")
public class OverridenCallBack implements java.io.Serializable {

	private Long id;

	public OverridenCallBack() {
	}

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void prePersistFromXML() {
		updateCallBackCount("prePersistFromXML");
	}

	public void postRemoveFromXML() {
		updateCallBackCount("postRemoveFromXML");
	}

	public void postUpdateFromXML() {
		updateCallBackCount("postUpdateFromXML");
	}

	public void postLoadFromXML() {
		updateCallBackCount("postLoadFromXML");
	}

	public void postPersistFromXML() {
		updateCallBackCount("postPersistFromXML");
	}

	public void preRemoveFromXML() {
		updateCallBackCount("preRemoveFromXML");
	}

	public void preUpdateFromXML() {
		updateCallBackCount("preUpdateFromXML");
	}

	protected void updateCallBackCount(String callBackKeyName) {
		CallBackCounts.updateCount(callBackKeyName);
	}

	public int hashCode() {
		int hash = 0;
		hash += (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	public boolean equals(Object object) {
		// TODO:Warning-this method won't work in the case the id fields are not set
		if (!(object instanceof OverridenCallBack)) {
			return false;
		}
		OverridenCallBack other = (OverridenCallBack) object;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "override.callbacklistener.OverridenCallBack id=" + id;
	}
}
