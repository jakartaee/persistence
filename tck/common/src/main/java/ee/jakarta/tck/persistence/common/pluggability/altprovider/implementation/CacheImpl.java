/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.persistence.Cache;

public class CacheImpl implements Cache {

	protected TSLogger logger;

	public CacheImpl() {
		logger = TSLogger.getInstance();
	}

	@Override
	public boolean contains(java.lang.Class cls, java.lang.Object primaryKey) {
		return false;
	}

	@Override
	public void evict(java.lang.Class cls) {
	}

	@Override
	public void evict(java.lang.Class cls, java.lang.Object primaryKey) {
	}

	@Override
	public void evictAll() {

	}

	@Override
	public Class unwrap(java.lang.Class cls) {
		return cls;
	}

}
