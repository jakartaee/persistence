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
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@Generated("EclipseLink JPA 2.0 Canonical Model Generation")
@StaticMetamodel(Customer.class)
public class Customer_ {

	public static volatile SingularAttribute<Customer, String> id;

	public static volatile SingularAttribute<Customer, Address> work;

	public static volatile SingularAttribute<Customer, Address> home;

	public static volatile CollectionAttribute<Customer, Alias> aliasesNoop;

	public static volatile SingularAttribute<Customer, Spouse> spouse;

	public static volatile SingularAttribute<Customer, String> name;

	public static volatile CollectionAttribute<Customer, Order> orders;

	public static volatile CollectionAttribute<Customer, Alias> aliases;

	public static volatile CollectionAttribute<Customer, CreditCard> creditCards;

	public static volatile SingularAttribute<Customer, Country> country;

	public static volatile SetAttribute<Customer, Order> orders2;

	public static volatile ListAttribute<Customer, Order> orders3;

}
