/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - Java Persistence 2.1
//     Linda DeMichiel - Java Persistence 2.0


package javax.persistence;

/**
 * Thrown by the persistence provider when a transaction is required but is not
 * active.
 * 
 * @since Java Persistence 1.0
 */
public class TransactionRequiredException extends PersistenceException {

	/**
	 * Constructs a new <code>TransactionRequiredException</code> exception with
	 * <code>null</code> as its detail message.
	 */
	public TransactionRequiredException() {
		super();
	}

	/**
	 * Constructs a new <code>TransactionRequiredException</code> exception with
	 * the specified detail message.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public TransactionRequiredException(String message) {
		super(message);
	}
}
