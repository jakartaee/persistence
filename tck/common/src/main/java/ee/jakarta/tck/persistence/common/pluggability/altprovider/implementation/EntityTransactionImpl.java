/*
 * Copyright (c) 2011, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation;

public class EntityTransactionImpl implements jakarta.persistence.EntityTransaction {
	protected boolean isActive;

	protected TSLogger logger;

	public EntityTransactionImpl() {
		logger = TSLogger.getInstance();
		isActive = false;
	}

	@Override
	public void begin() {
		isActive = true;
	}

	@Override
	public void commit() {
		isActive = false;
	}

	@Override
	public boolean getRollbackOnly() {
		return false;
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public void setTimeout(Integer timeout) {

	}

	@Override
	public Integer getTimeout() {
		return null;
	}

	@Override
	public void rollback() {

	}

	@Override
	public void setRollbackOnly() {

	}

}
