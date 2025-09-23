/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.common;

import java.lang.System.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DriverManagerConnection {

	private static final Logger logger = (Logger) System.getLogger(DriverManagerConnection.class.getName());

	public Connection getConnection(Properties p) throws Exception {
		String dbUrl, dbUser, dbPassword, dbDriver;
		dbUrl = dbUser = dbPassword = dbDriver = null;

		dbUrl = p.getProperty("jakarta.persistence.jdbc.url", "");
		dbUser = p.getProperty("jakarta.persistence.jdbc.user", "");
		dbPassword = p.getProperty("jakarta.persistence.jdbc.password", "");
		dbDriver = p.getProperty("jakarta.persistence.jdbc.driver", "");

		logger.log(Logger.Level.TRACE, "Url : " + dbUrl);
		logger.log(Logger.Level.TRACE, "Username  : " + dbUser);
		logger.log(Logger.Level.TRACE, "Password  : " + dbPassword);
		logger.log(Logger.Level.TRACE, "Driver    : " + dbDriver);

		logger.log(Logger.Level.TRACE, "About to load the driver class");
		Class.forName(dbDriver);
		logger.log(Logger.Level.INFO, "Successfully loaded the driver class");

		logger.log(Logger.Level.TRACE, "About to make the DB connection");
		Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		logger.log(Logger.Level.INFO, "Made the JDBC connection to the DB");

		return con;
	}

}
