/*
 * Copyright (c) 2008, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Gavin King      - 3.2


package jakarta.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A function which makes use of a {@linkplain Connection JDBC connection}
 * to compute a result.
 *
 * @see ConnectionConsumer
 * @see EntityManager#callWithConnection(ConnectionFunction)
 *
 * @since 3.2
 */
@FunctionalInterface
public interface ConnectionFunction<T> {
	/**
	 * Compute a result using the given connection.
	 *
	 * @param connection the connection to use
	 *
	 * @return the result
	 * 
	 * @throws SQLException if a problem occurs calling JDBC
	 */
	T apply(Connection connection) throws SQLException;
}
