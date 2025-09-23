/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.parameter;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class Client1 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Employee" };
		return createDeploymentJar("jpa_core_criteriaapi_parameter1.jar", pkgNameWithoutSuffix, classes);
	}
	
	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			getEntityManager();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}



	/*
	 * @testName: parameterTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383; PERSISTENCE:JAVADOC:1093; PERSISTENCE:JAVADOC:1092;
	 * 
	 * @test_Strategy: Create a query with 2 named parameters and retrieve
	 * information about the parameters.
	 */
	@Test
	public void parameterTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Starting parameterTest1");
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		boolean pass6 = false;
		boolean pass7 = false;
		boolean pass8 = false;
		boolean pass9 = false;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);

			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();
			ParameterExpression<String> pe = qbuilder.parameter(String.class, "first");
			Class<?> c = pe.getParameterType();
			if (c.isAssignableFrom(java.lang.String.class)) {
				logger.log(Logger.Level.TRACE, "Received expected type from getParameterType()");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected type String from getParameterType(), instead got:" + c);
			}
			String name = pe.getName();
			if (name != null) {
				if (!name.equals("first")) {
					logger.log(Logger.Level.ERROR, "getName() returned wrong name, expected: first, actual:" + name);
				} else {
					pass2 = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "getName() returned null");
			}
			Integer position = pe.getPosition();
			if (position != null) {
				logger.log(Logger.Level.ERROR, "getPosition() returned:" + position + ", instead of null");
			} else {
				pass3 = true;
			}

			ParameterExpression<String> pe2 = qbuilder.parameter(String.class, "last");
			criteria.add(qbuilder.equal(employee.get("firstName"), pe));
			criteria.add(qbuilder.equal(employee.get("lastName"), pe2));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);

			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logger.log(Logger.Level.TRACE, "parameter name = " + p.getName());
				logger.log(Logger.Level.TRACE, "parameter position = " + p.getPosition());
				logger.log(Logger.Level.TRACE, "parameter type =" + p.getParameterType());
			}

			String sExpected = "first";
			Parameter p1 = q.getParameter(sExpected);
			String sActual = p1.getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR, "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass4 = true;
			}

			sExpected = null;
			Integer iActual = p1.getPosition();
			if (iActual != null) {
				logger.log(Logger.Level.ERROR, "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass5 = true;
			}
			sExpected = "java.lang.String";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR,
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass6 = true;
			}

			sExpected = "last";
			Parameter p2 = q.getParameter(sExpected);
			sActual = p2.getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR, "p2.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass7 = true;
			}
			sExpected = null;
			iActual = p2.getPosition();
			if (iActual != null) {
				logger.log(Logger.Level.ERROR, "p2.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass8 = true;
			}

			sExpected = "java.lang.String";
			sActual = p2.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR,
						"p2.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass9 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7 || !pass8 || !pass9) {
			throw new Exception("parameterTest1 test failed");

		}

	}

	/*
	 * @testName: parameterTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a named parameter that is a float and
	 * retrieve information about the parameter.
	 */
	@Test
	public void parameterTest2() throws Exception {
		logger.log(Logger.Level.TRACE, "Starting parameterTest2");
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);
			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();
			ParameterExpression<Float> pe = qbuilder.parameter(Float.class, "salary");
			criteria.add(qbuilder.equal(employee.get("salary"), pe));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);

			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logger.log(Logger.Level.TRACE, "parameter name = " + p.getName());
				logger.log(Logger.Level.TRACE, "parameter position = " + p.getPosition());
				logger.log(Logger.Level.TRACE, "parameter type =" + p.getParameterType());
			}

			String sExpected = "salary";
			Parameter p1 = q.getParameter(sExpected);
			String sActual = p1.getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR, "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass1 = true;
			}

			sExpected = null;
			Integer iActual = p1.getPosition();
			if (iActual != null) {
				logger.log(Logger.Level.ERROR, "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass2 = true;
			}
			sExpected = "java.lang.Float";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR,
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass3 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("parameterTest2 test failed");

		}
	}

	/*
	 * @testName: parameterTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a named parameter that is a date and and
	 * retrieve information about the parameter.
	 */
	@Test
	public void parameterTest3() throws Exception {
		logger.log(Logger.Level.TRACE, "Starting parameterTest3");
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);
			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();
			ParameterExpression<java.sql.Date> pe = qbuilder.parameter(java.sql.Date.class, "hdate");
			criteria.add(qbuilder.equal(employee.get("hireDate"), pe));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);

			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logger.log(Logger.Level.TRACE, "parameter name = " + p.getName());
				logger.log(Logger.Level.TRACE, "parameter position = " + p.getPosition());
				logger.log(Logger.Level.TRACE, "parameter type =" + p.getParameterType());
			}

			String sExpected = "hdate";
			Parameter p1 = q.getParameter(sExpected);
			String sActual = p1.getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR, "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass1 = true;
			}

			sExpected = null;
			Integer iActual = p1.getPosition();
			if (iActual != null) {
				logger.log(Logger.Level.ERROR, "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass2 = true;
			}
			sExpected = "java.sql.Date";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logger.log(Logger.Level.ERROR,
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass3 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("parameterTest3 test failed");

		}
	}

	/*
	 * @testName: parameterTest4
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:382; PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a parameter where the name is not
	 * specified and retrieve information about the parameter.
	 */
	@Test
	public void parameterTest4() throws Exception {
		logger.log(Logger.Level.TRACE, "Starting parameterTest4");
		boolean pass1 = false;
		boolean pass2 = true;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);
			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();

			// No name is being assigned to the parameter
			ParameterExpression<String> pe = qbuilder.parameter(String.class);

			criteria.add(qbuilder.equal(employee.get("firstName"), pe));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);
			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logger.log(Logger.Level.TRACE, "parameter position = " + p.getPosition());
				logger.log(Logger.Level.TRACE, "parameter type =" + p.getParameterType());
			}

			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				pass1 = true;
				// the value returned by getName() in this instance where no name has
				// been assigned to the parameter is undefined in the spec therefore
				// we will not test for it.

				String sExpected = null;
				Integer iActual = p.getPosition();
				if (iActual != null) {
					logger.log(Logger.Level.ERROR, "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
				}
				sExpected = "java.lang.String";
				String sActual = p.getParameterType().getName();
				if (!sActual.equals(sExpected)) {
					logger.log(Logger.Level.ERROR,
							"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2) {
			throw new Exception("parameterTest4 test failed");

		}

	}

}
