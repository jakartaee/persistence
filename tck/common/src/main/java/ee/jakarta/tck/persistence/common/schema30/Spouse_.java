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

package ee.jakarta.tck.persistence.common.schema30;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@Generated("EclipseLink JPA 2.0 Canonical Model Generation")
@StaticMetamodel(Spouse.class)
public class Spouse_ {

	public static volatile SingularAttribute<Spouse, String> id;

	public static volatile SingularAttribute<Spouse, String> lastName;

	public static volatile SingularAttribute<Spouse, String> socialSecurityNumber;

	public static volatile SingularAttribute<Spouse, String> firstName;

	public static volatile SingularAttribute<Spouse, Customer> customer;

	public static volatile SingularAttribute<Spouse, String> maidenName;

	public static volatile SingularAttribute<Spouse, Info> info;

}
