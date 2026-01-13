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

package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilProductData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilProductData.class.getName());

	@BeforeEach
	public void setupProductData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupProductData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createProductData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupProductData failed:", e);
		}
	}

}
