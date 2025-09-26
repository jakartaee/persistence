/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaUpdate;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collection;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.SoftwareProduct;
import ee.jakarta.tck.persistence.common.schema30.UtilProductData;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends UtilProductData {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {

	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaUpdate.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: fromClassGetRootSetStringObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1696; PERSISTENCE:JAVADOC:1698;
	 * PERSISTENCE:JAVADOC:1703; PERSISTENCE:JAVADOC:631; PERSISTENCE:SPEC:1777;
	 * PERSISTENCE:SPEC:1778; PERSISTENCE:SPEC:1779; PERSISTENCE:SPEC:1793;
	 *
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 *
	 */
	@Test
	public void fromClassGetRootSetStringObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");
			if (root.equals(cd.getRoot())) {
				logger.log(Logger.Level.TRACE, "Obtained expected root");
				cd.set("quantity", 0);
				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received expected number deleted:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get expected root");
				logger.log(Logger.Level.ERROR, "Expected:" + cd.getRoot() + ", actual:" + root);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("fromClassGetRootSetStringObjectTest failed");
		}
	}

	/*
	 * @testName: fromEntityTypeSetStringObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1697; PERSISTENCE:JAVADOC:1703;
	 * 
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 */
	@Test
	public void fromEntityTypeSetStringObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);

		Metamodel mm = getEntityManager().getMetamodel();
		EntityType<Product> Product_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Product.class);

		Root<Product> root = cd.from(Product_);
		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");
			cd.set("quantity", 0);
			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
				pass1 = true;
				logger.log(Logger.Level.TRACE, "Received expected number deleted:" + actual);
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logger.log(Logger.Level.ERROR,
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass2 = false;
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logger.log(Logger.Level.ERROR,
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass3 = false;
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logger.log(Logger.Level.ERROR,
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass4 = false;
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + productRef.length + softwareRef.length + hardwareRef.length
						+ ", actual:" + actual);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("fromEntityTypeSetStringObjectTest failed");
		}
	}

	/*
	 * @testName: whereExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1704; PERSISTENCE:JAVADOC:3354;
	 *
	 * @test_Strategy: UPDATE Product p SET p.quantity = 0 WHERE p.id in (1,2,3)
	 */
	@Test
	public void whereExpressionTest() throws Exception {
		boolean pass2 = false;
		boolean pass3 = true;
		boolean pass4 = true;
		boolean pass5 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");
			EntityType<Product> product_ = root.getModel();
			cd.set(product_.getSingularAttribute("quantity", Integer.class), 0);

			Expression exp = root.get("id");
			Collection<String> col = new ArrayList<String>();
			col.add("1");
			col.add("2");
			col.add("3");

			cd.where(exp.in(col));

			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == col.size()) {
				logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
				pass2 = true;
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (p.getId().equals("1") || p.getId().equals("2") || p.getId().equals("3")) {
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						} else {
							logger.log(Logger.Level.TRACE, "Product:" + p.getId() + " update was successfully");
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logger.log(Logger.Level.ERROR, "Product:" + p.getId()
									+ " quantity does not equal original value:" + p.getQuantity());
							pass3 = false;
						} else {
							logger.log(Logger.Level.TRACE,
									"Product:" + pp.getId() + " quantity matches original value" + p.getQuantity());
						}
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logger.log(Logger.Level.ERROR, "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());
						pass4 = false;
					} else {
						logger.log(Logger.Level.TRACE, "Received expected software quantity:" + pp.getQuantity());
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logger.log(Logger.Level.ERROR, "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());
						pass5 = false;
					} else {
						logger.log(Logger.Level.TRACE, "Received expected hardware quantity:" + pp.getQuantity());
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + col.size() + ", actual:" + actual);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass2 || !pass3 || !pass4 || !pass5) {
			throw new Exception("whereExpressionTest failed");
		}

	}

	/*
	 * @testName: wherePredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1705; PERSISTENCE:JAVADOC:949;
	 *
	 * @test_Strategy: UPDATE Product p SET p.quantity = 0 WHERE p.id in (2)
	 */
	@Test
	public void wherePredicateArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);

		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");

			EntityType<Product> product_ = root.getModel();
			cd.set(product_.getSingularAttribute("quantity", Integer.class), 0);

			Predicate[] predArray = { cbuilder.equal(root.get("id"), "2") };

			cd.where(predArray);

			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == predArray.length) {
				logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
				pass1 = true;
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (p.getId().equals("2")) {
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						} else {
							logger.log(Logger.Level.TRACE, "Product:" + p.getId() + " update was successfully");
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logger.log(Logger.Level.ERROR, "Product:" + p.getId()
									+ " quantity does not equal original value:" + p.getQuantity());
							pass2 = false;
						} else {
							logger.log(Logger.Level.TRACE,
									"Product:" + pp.getId() + " quantity matches original value" + p.getQuantity());
						}
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logger.log(Logger.Level.ERROR, "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());

						pass3 = false;
					} else {
						logger.log(Logger.Level.TRACE, "Received expected software quantity:" + pp.getQuantity());
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != p.getQuantity()) {
						logger.log(Logger.Level.ERROR, "Expected product:" + p.getId() + " to have quantity of "
								+ p.getQuantity() + ", actual:" + pp.getQuantity());
						pass4 = false;
					} else {
						logger.log(Logger.Level.TRACE, "Received expected hardware quantity:" + pp.getQuantity());
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected updates:" + predArray.length + ", actual:" + actual);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("wherePredicateArrayTest failed");
		}
	}

	/*
	 * @testName: setSingularAttributeObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1699; PERSISTENCE:JAVADOC:950;
	 * 
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 *
	 */
	@Test
	public void setSingularAttributeObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");
			if (root.getModel().getName().equals(cd.getRoot().getModel().getName())) {
				logger.log(Logger.Level.TRACE, "Obtained expected root");

				EntityType<Product> product_ = root.getModel();
				cd.set(product_.getSingularAttribute("quantity", Integer.class), 0);

				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get expected root");
				logger.log(Logger.Level.ERROR,
						"Expected:" + cd.getRoot().getModel().getName() + ", actual:" + root.getModel().getName());
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setSingularAttributeObjectTest failed");
		}
	}

	/*
	 * @testName: setSingularAttributeExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1700;
	 * 
	 * @test_Strategy: UPDATE Product p SET p.quantity = prod(p.quantity,0)
	 *
	 */
	@Test
	public void setSingularAttributeExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");
			if (root.getModel().getName().equals(cd.getRoot().getModel().getName())) {
				logger.log(Logger.Level.TRACE, "Obtained expected root");

				EntityType<Product> product_ = root.getModel();
				Expression exp = cbuilder.prod(root.get(product_.getSingularAttribute("quantity", Integer.class)), 0);
				cd.set(product_.getSingularAttribute("quantity", Integer.class), exp);
				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get expected root");
				logger.log(Logger.Level.ERROR,
						"Expected:" + cd.getRoot().getModel().getName() + ", actual:" + root.getModel().getName());
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setSingularAttributeExpressionTest failed");
		}
	}

	/*
	 * @testName: setPathObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1701;
	 * 
	 * @test_Strategy: UPDATE Product SET QUANTITY = 0
	 *
	 */
	@Test
	public void setPathObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");
			if (root.getModel().getName().equals(cd.getRoot().getModel().getName())) {
				logger.log(Logger.Level.TRACE, "Obtained expected root");
				cd.set(root.get("quantity"), 0);
				int actual = getEntityManager().createQuery(cd).executeUpdate();
				if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
					clearCache();
					for (Product p : productRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass2 = false;
						}
					}
					for (Product p : softwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass3 = false;
						}
					}
					for (Product p : hardwareRef) {
						Product pp = getEntityManager().find(Product.class, p.getId());
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR, "Expected product:" + p.getId()
									+ " to have quantity of 0, actual:" + pp.getQuantity());
							pass4 = false;
						}
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected:" + productRef.length + softwareRef.length
							+ hardwareRef.length + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get expected root");
				logger.log(Logger.Level.ERROR,
						"Expected:" + cd.getRoot().getModel().getName() + ", actual:" + root.getModel().getName());
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setPathObjectTest failed");
		}
	}

	/*
	 * @testName: setPathExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1702;
	 * 
	 * @test_Strategy: UPDATE Product p SET p.quantity = prod(p.quantity,0)
	 *
	 */
	@Test
	public void setPathExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = true;
		boolean pass4 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
		Root<Product> root = cd.from(Product.class);
		if (root != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null root");

			EntityType<Product> product_ = root.getModel();
			Expression<Integer> exp = cbuilder.prod(root.get(product_.getSingularAttribute("quantity", Integer.class)),
					0);

			cd.set(root.<Integer>get("quantity"), exp);
			int actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == productRef.length + softwareRef.length + hardwareRef.length) {
				pass1 = true;
				logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
				clearCache();
				for (Product p : productRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logger.log(Logger.Level.ERROR,
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass2 = false;
					}
				}
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logger.log(Logger.Level.ERROR,
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass3 = false;
					}
				}
				for (Product p : hardwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					if (pp.getQuantity() != 0) {
						logger.log(Logger.Level.ERROR,
								"Expected product:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						pass4 = false;
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + productRef.length + softwareRef.length + hardwareRef.length
						+ ", actual:" + actual);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}
		getEntityTransaction().commit();
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setPathExpressionTest failed");
		}
	}

	/*
	 * @testName: subquery
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3355
	 *
	 * @test_Strategy:
	 *
	 * UPDATE Product p SET p.quantity = prod(p.quantity,0) WHERE EXISTS (Select
	 * hardProd From PRODUCT hardprod where hardprod.id = '1').
	 *
	 */
	@Test
	public void subquery() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Product> cu = cbuilder.createCriteriaUpdate(Product.class);
		if (cu != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cu.from(Product.class);
			EntityType<Product> product_ = product.getModel();

			Expression<Integer> exp = cbuilder
					.prod(product.get(product_.getSingularAttribute("quantity", Integer.class)), 0);

			cu.set(product.<Integer>get("quantity"), exp);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();

			// create Subquery instance, with root Customer
			Subquery<Product> sq = cu.subquery(Product.class);
			Root<Product> hardProd = sq.from(Product.class);

			// the subquery references the root of the containing query
			sq.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"))
					.select(hardProd);

			// an exists condition is applied to the subquery result
			cu.where(cbuilder.exists(sq));

			int actual = getEntityManager().createQuery(cu).executeUpdate();
			if (actual == 1) {
				logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
				clearCache();
				Product pp = getEntityManager().find(Product.class, "1");
				if (pp.getQuantity() != 0) {
					logger.log(Logger.Level.ERROR,
							"Expected product:1 to have quantity of 0, actual:" + pp.getQuantity());
				} else {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected:1, actual:" + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("exists test failed");
		}
	}

	/*
	 * @testName: modifiedQueryTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1788; PERSISTENCE:SPEC:1796;
	 * 
	 * @test_Strategy: UPDATE SoftwareProduct p SET p.quantity = 0 WHERE p.quantity
	 * < 35) UPDATE SoftwareProduct p SET p.quantity = 0 WHERE p.quantity > 100)
	 *
	 */
	@Test
	public void modifiedQueryTest() throws Exception {
		int passModifiedCount1 = 0;
		int passUnModifiedCount1 = 0;
		int passModifiedCount2 = 0;
		int passUnModifiedCount2 = 0;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		logger.log(Logger.Level.INFO, "Testing initial query");
		CriteriaUpdate<SoftwareProduct> cd = cbuilder.createCriteriaUpdate(SoftwareProduct.class);
		Root<SoftwareProduct> softwareproduct = cd.from(SoftwareProduct.class);
		if (softwareproduct != null) {
			EntityType<SoftwareProduct> softwareproduct_ = softwareproduct.getModel();
			cd.set(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)), 0);
			cd.where(cbuilder.lt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					35));
			Query q = getEntityManager().createQuery(cd);
			logger.log(Logger.Level.INFO, "Modify CriteriaUpdate object");
			cd.where(cbuilder.lt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					500));
			int actual = q.executeUpdate();
			if (actual == 4) {
				logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
				clearCache();
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(SoftwareProduct.class, p.getId());
					if (p.getId().equals("30") || p.getId().equals("31") || p.getId().equals("36")
							|| p.getId().equals("37")) {
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR,
									"id:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						} else {
							logger.log(Logger.Level.TRACE, "id:" + p.getId() + " update was successfully");
							passModifiedCount1++;
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logger.log(Logger.Level.ERROR,
									"id:" + p.getId() + " quantity does not equal original value:" + p.getQuantity());
						} else {
							logger.log(Logger.Level.TRACE,
									"id:" + pp.getId() + " quantity matches original value:" + p.getQuantity());
							passUnModifiedCount1++;
						}
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 4 updates, actual:" + actual);
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					logger.log(Logger.Level.ERROR, "id:" + p.getId() + ", quantity:" + pp.getQuantity());
				}
			}
			logger.log(Logger.Level.INFO, "Testing modified CriteriaQuery");
			cd.where(cbuilder.gt(softwareproduct.get(softwareproduct_.getSingularAttribute("quantity", Integer.class)),
					100));

			actual = getEntityManager().createQuery(cd).executeUpdate();
			if (actual == 3) {
				logger.log(Logger.Level.TRACE, "Received expected number of updates:" + actual);
				clearCache();
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(SoftwareProduct.class, p.getId());
					if (p.getId().equals("29") || p.getId().equals("34") || p.getId().equals("38")
							|| p.getId().equals("30") || p.getId().equals("31") || p.getId().equals("36")
							|| p.getId().equals("37")) {
						if (pp.getQuantity() != 0) {
							logger.log(Logger.Level.ERROR,
									"id:" + p.getId() + " to have quantity of 0, actual:" + pp.getQuantity());
						} else {
							logger.log(Logger.Level.TRACE, "id:" + p.getId() + " update was successfully");
							passModifiedCount2++;
						}
					} else {
						if (pp.getQuantity() != p.getQuantity()) {
							logger.log(Logger.Level.ERROR,
									"id:" + p.getId() + " quantity does not equal original value:" + p.getQuantity());
						} else {
							logger.log(Logger.Level.TRACE,
									"id:" + pp.getId() + " quantity matches original value:" + p.getQuantity());
							passUnModifiedCount2++;
						}
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 3, actual:" + actual);
				for (Product p : softwareRef) {
					Product pp = getEntityManager().find(Product.class, p.getId());
					logger.log(Logger.Level.ERROR, "id:" + p.getId() + ", quantity:" + pp.getQuantity());
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();
		if (passModifiedCount1 != 4 || passUnModifiedCount1 != 6 || passModifiedCount2 != 7
				|| passUnModifiedCount2 != 3) {
			throw new Exception("modifiedQueryTest failed");
		}

	}

	public void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_DETAILS").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
