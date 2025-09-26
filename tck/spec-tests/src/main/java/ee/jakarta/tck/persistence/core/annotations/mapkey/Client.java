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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.annotations.mapkey;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	public Client() {
	}

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	protected Employee empRef[] = new Employee[10];

	protected static Department deptRef[] = new Department[5];

	/*
	 * 
	 * Business Methods to set up data for Test Cases
	 */

	public void createTestDataCommon() throws Exception {
		try {

			logger.log(Logger.Level.TRACE, "createTestDataCommon");

			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Create 2 Departments");
			deptRef[0] = new Department(1, "Marketing");
			deptRef[1] = new Department(2, "Administration");

			logger.log(Logger.Level.TRACE, "Start to persist departments ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department " + dept.getName());
				}
			}

			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception creating test data:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}
	}

	public void createTestData() throws Exception {
		try {

			logger.log(Logger.Level.TRACE, "createTestData");
			createTestDataCommon();
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create 5 employees");
			empRef[0] = new Employee(1, "Alan", "Frechette");
			empRef[0].setDepartment(deptRef[0]);

			empRef[1] = new Employee(2, "Arthur", "Frechette");
			empRef[1].setDepartment(deptRef[1]);

			empRef[2] = new Employee(3, "Shelly", "McGowan");
			empRef[2].setDepartment(deptRef[0]);

			empRef[3] = new Employee(4, "Robert", "Bissett");
			empRef[3].setDepartment(deptRef[1]);

			empRef[4] = new Employee(5, "Stephen", "DMilla");
			empRef[4].setDepartment(deptRef[0]);

			Map<String, Employee> link = new HashMap<String, Employee>();
			link.put(empRef[0].getLastName(), empRef[0]);
			link.put(empRef[2].getLastName(), empRef[2]);
			link.put(empRef[4].getLastName(), empRef[4]);
			deptRef[0].setLastNameEmployees(link);

			Map<String, Employee> link1 = new HashMap<String, Employee>();
			link1.put(empRef[1].getLastName(), empRef[1]);
			link1.put(empRef[3].getLastName(), empRef[3]);
			deptRef[1].setLastNameEmployees(link1);

			logger.log(Logger.Level.TRACE, "Start to persist employees ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted employee " + emp.getId());
				}
			}

			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception creating test data:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	protected void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in RemoveSchemaData:", re);
			}
		}
	}
}
