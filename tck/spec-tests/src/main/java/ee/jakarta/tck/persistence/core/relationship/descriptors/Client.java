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
package ee.jakarta.tck.persistence.core.relationship.descriptors;

import java.lang.System.Logger;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final XAddress aRef[] = new XAddress[5];

	private static final XAnnualReview rRef[] = new XAnnualReview[10];

	private static final XCompany cRef[] = new XCompany[5];

	private static final XInsurance insRef[] = new XInsurance[5];

	private static final XPerson pRef[] = new XPerson[20];

	private static final XProject projRef[] = new XProject[10];

	private static final XTeam tRef[] = new XTeam[10];

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { ORM_XML };
		String[] classes = { pkgName + "XAddress", pkgName + "XAnnualReview", pkgName + "XCompany",
				pkgName + "XInsurance", pkgName + "XPerson", pkgName + "XProject", pkgName + "XTeam" };
		return createDeploymentJar("jpa_core_relationship_descriptors.jar", pkgNameWithoutSuffix, classes, xmlFiles);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: descriptorMappingTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * PERSISTENCE:SPEC:949; PERSISTENCE:SPEC:950
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors BiDirectional OneToOne
	 * Relationship
	 *
	 * OnePerson to OneProjectLead
	 *
	 * Entity Person mapped to table named PERSON references a single instance of
	 * Project. Entity Project mapped to table named PROJECT a single instance of
	 * Person. Entity Person is the owner of the relationship.
	 *
	 * Table PERSON contains a foreign key to PROJECT. The foreign key is named
	 * PROJECT_PROJID.
	 * 
	 */
	@Test
	public void descriptorMappingTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin descriptorMappingTest1");
		boolean pass = false;

		try {
			createPeople();
			createProjects();
			getEntityTransaction().begin();

			pRef[0].setXProject(projRef[2]);
			projRef[2].setXProjectLead(pRef[0]);

			getEntityManager().merge(pRef[0]);
			getEntityManager().merge(projRef[2]);

			XPerson newPerson = getEntityManager().find(XPerson.class, 1);

			if (newPerson.getXProject().getXName().equals("Asp")) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("descriptorMappingTest1 failed");
	}

	/*
	 * @testName: descriptorMappingTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors BiDirectional
	 * ManyToOne Relationship
	 *
	 * ManyTeams to OneCompany
	 *
	 * Entity Team is mapped to a table named TEAM references a single instance of
	 * Company. Entity Company mapped to table named COMPANY references a collection
	 * of Entity Team. Entity Team is the owner of the relationship.
	 *
	 * Table TEAM contains a foreign key to COMPANY. The foreign key is named
	 * COMPANY_COMPANYID.
	 * 
	 */
	@Test
	public void descriptorMappingTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin descriptorMappingTest2");
		boolean pass1 = true;
		boolean pass2 = false;
		Vector<XTeam> v1;
		Vector<XTeam> v2;
		int foundTeam1 = 0;
		int foundTeam2 = 0;
		final String[] expectedTeam1 = new String[] { "Engineering", "Marketing", "Sales" };
		final String[] expectedTeam2 = new String[] { "Accounting", "Training" };

		try {
			createTeams();
			createCompany();

			getEntityTransaction().begin();

			tRef[0].setXCompany(cRef[0]);
			tRef[1].setXCompany(cRef[0]);
			tRef[2].setXCompany(cRef[0]);

			v1 = new Vector<XTeam>();
			v1.add(tRef[0]);
			v1.add(tRef[1]);
			v1.add(tRef[2]);
			cRef[0].setXTeams(v1);

			tRef[3].setXCompany(cRef[1]);
			tRef[4].setXCompany(cRef[1]);

			v2 = new Vector<XTeam>();
			v2.add(tRef[3]);
			v2.add(tRef[4]);
			cRef[1].setXTeams(v2);

			getEntityManager().merge(tRef[0]);
			getEntityManager().merge(tRef[1]);
			getEntityManager().merge(tRef[2]);
			getEntityManager().merge(tRef[3]);
			getEntityManager().merge(tRef[4]);

			getEntityManager().merge(cRef[0]);
			getEntityManager().merge(cRef[1]);
			getEntityManager().flush();

			XCompany c1 = getEntityManager().find(XCompany.class, 25501L);
			Collection<XTeam> t1 = c1.getXTeams();

			XCompany c2 = getEntityManager().find(XCompany.class, 37560L);
			Collection<XTeam> t2 = c2.getXTeams();

			if ((t1.size() != 3) || (t2.size() != 2)) {
				logger.log(Logger.Level.ERROR,
						"descriptorMappingTest2: Did not get expected results."
								+ "Team1 Collection Expected 3 references, got: " + t1.size()
								+ ", Team2 Collection Expected 2 references, got: " + t2.size());
				pass1 = false;
			} else if (pass1) {

				Iterator i1 = t1.iterator();
				while (i1.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Team 1 Collection for expected Teams");
					XTeam o1 = (XTeam) i1.next();

					for (int l = 0; l < 3; l++) {
						if (expectedTeam1[l].equals(o1.getXName())) {
							logger.log(Logger.Level.TRACE, "Found Team 1:" + o1.getXName());
							foundTeam1++;
							break;
						}
					}
				}

				Iterator i2 = t2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Team 2 Collection for expected Teams");
					XTeam o2 = (XTeam) i2.next();

					for (int l = 0; l < 2; l++) {
						if (expectedTeam2[l].equals(o2.getXName())) {
							logger.log(Logger.Level.TRACE, "Found Team 2:" + o2.getXName());
							foundTeam2++;
							break;
						}
					}
				}
			}

			if ((foundTeam1 != 3) || (foundTeam2 != 2)) {
				logger.log(Logger.Level.ERROR, "descriptorMappingTest2: Did not get expected results");
				pass2 = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}

		}

		if (!pass2)
			throw new Exception("descriptorMappingTest2 failed");
	}

	/*
	 * @testName: descriptorMappingTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors BiDirectional
	 * OneToMany Relationship
	 *
	 * OneCompany To ManyTeams
	 *
	 * Entity Company mapped to table named COMPANY references a collection of
	 * Entity Team. Entity Team is mapped to a table named TEAM references a single
	 * instance of Entity Company. Entity Team is the owner of the relationship.
	 *
	 * Table TEAM contains a foreign key to COMPANY. The foreign key is named
	 * COMPANY_COMPANYID.
	 *
	 */
	@Test
	public void descriptorMappingTest3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin descriptorMappingTest3");
		boolean pass = false;
		Vector v1;
		Vector v2;

		try {
			createTeams();
			createCompany();

			getEntityTransaction().begin();

			tRef[0].setXCompany(cRef[0]);
			tRef[1].setXCompany(cRef[0]);

			v1 = new Vector();
			v1.add(tRef[0]);
			v1.add(tRef[1]);
			cRef[0].setXTeams(v1);

			tRef[2].setXCompany(cRef[1]);
			tRef[3].setXCompany(cRef[1]);
			tRef[4].setXCompany(cRef[1]);

			v2 = new Vector();
			v2.add(tRef[2]);
			v2.add(tRef[3]);
			v2.add(tRef[4]);

			cRef[1].setXTeams(v2);

			getEntityManager().merge(tRef[0]);
			getEntityManager().merge(tRef[1]);
			getEntityManager().merge(tRef[2]);
			getEntityManager().merge(tRef[3]);
			getEntityManager().merge(tRef[4]);

			getEntityManager().merge(cRef[0]);
			getEntityManager().merge(cRef[1]);

			getEntityManager().flush();

			XTeam t1 = getEntityManager().find(XTeam.class, 1);
			XTeam t2 = getEntityManager().find(XTeam.class, 2);
			XTeam t3 = getEntityManager().find(XTeam.class, 3);
			XTeam t4 = getEntityManager().find(XTeam.class, 4);
			XTeam t5 = getEntityManager().find(XTeam.class, 5);

			if ((t1.getXCompany().getXCompanyId() == 25501L) && (t2.getXCompany().getXCompanyId() == 25501L)
					&& (t3.getXCompany().getXCompanyId() == 37560L) && (t4.getXCompany().getXCompanyId() == 37560L)
					&& (t5.getXCompany().getXCompanyId() == 37560L)) {

				pass = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("descriptorMappingTest3 failed");
	}

	/*
	 * @testName: descriptorMappingTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors UniDirectional
	 * OneToOne Relationship
	 *
	 * OneCompany To OneAddress
	 *
	 * Entity Company mapped to table named COMPANY references a single instance of
	 * Entity Address. Entity Address is mapped to a table named ADDRESS which does
	 * not reference Entity Company. Entity Company is the owner of the
	 * relationship.
	 *
	 * Table COMPANY contains a foreign key to ADDRESS. The foreign key is named
	 * ADDRESS_ID.
	 *
	 */
	@Test
	public void descriptorMappingTest4() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin descriptorMappingTest4");
		boolean pass = false;

		try {

			createAddress();
			createCompany();

			getEntityTransaction().begin();

			cRef[0].setXAddress(aRef[0]);
			cRef[1].setXAddress(aRef[1]);

			getEntityManager().merge(cRef[0]);
			getEntityManager().merge(cRef[1]);

			getEntityManager().flush();

			XCompany c1 = getEntityManager().find(XCompany.class, 25501L);
			XCompany c2 = getEntityManager().find(XCompany.class, 37560L);

			if (c1.getXAddress().getXCity().equals("Burlington") && c2.getXAddress().getXCity().equals("Santa Clara")) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results" + "Expected: Burlington and Santa Clara, got: "
								+ c1.getXAddress().getXCity() + c2.getXAddress().getXCity());
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}

		}

		if (!pass)
			throw new Exception("descriptorMappingTest4 failed");
	}

	/*
	 * @testName: descriptorMappingTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors UniDirectional
	 * ManyToOne Relationship
	 *
	 * ManyPersons To OneTeam
	 *
	 * Entity Person mapped to table named PERSON references a single instance of
	 * Entity Team. Entity Team is mapped to a table named TEAM and does not
	 * reference Entity Person. Entity Person is the owner of the relationship.
	 *
	 * Table PERSON contains a foreign key to TEAM. The foreign key is named
	 * TEAM_TEAMID.
	 *
	 */
	@Test
	public void descriptorMappingTest5() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin descriptorMappingTest5");
		boolean pass = false;

		try {
			createTeams();
			createPeople();

			getEntityTransaction().begin();

			pRef[1].setXTeam(tRef[0]);
			pRef[3].setXTeam(tRef[1]);
			pRef[5].setXTeam(tRef[2]);
			pRef[7].setXTeam(tRef[3]);
			pRef[9].setXTeam(tRef[4]);

			pRef[2].setXTeam(tRef[4]);
			pRef[4].setXTeam(tRef[3]);
			pRef[6].setXTeam(tRef[2]);
			pRef[8].setXTeam(tRef[1]);
			pRef[10].setXTeam(tRef[0]);

			for (int i = 1; i < 11; i++) {
				getEntityManager().merge(pRef[i]);
			}

			getEntityManager().flush();

			if ((pRef[1].getXTeam() == tRef[0]) && (pRef[10].getXTeam() == tRef[0]) && (pRef[3].getXTeam() == tRef[1])
					&& (pRef[8].getXTeam() == tRef[1]) && (pRef[5].getXTeam() == tRef[2])
					&& (pRef[6].getXTeam() == tRef[2]) && (pRef[7].getXTeam() == tRef[3])
					&& (pRef[4].getXTeam() == tRef[3]) && (pRef[9].getXTeam() == tRef[4])
					&& (pRef[2].getXTeam() == tRef[4])) {

				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				pass = false;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}

		}

		if (!pass)
			throw new Exception("descriptorMappingTest5 failed");
	}

	/*
	 * @testName: descriptorMappingTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors BiDirectional
	 * ManyToMany Relationship
	 *
	 * ManyProjects To ManyPersons
	 *
	 * Entity Project mapped to table named PROJECT references a collection of
	 * Entity Project. Entity Person is mapped to a table named PERSON and
	 * references a collection of Entity Project. Entity Project is the owner of the
	 * relationship.
	 *
	 * There is a join table named PROJECT_PERSON (owner named first. One foreign
	 * key column refers to table PROJECT. The name of the foreign key column is
	 * PROJECTS_PROJID. The other foreign key column is refers to the PERSON table.
	 * The name of this foreign key is PERSONS_PERSONID.
	 *
	 */
	@Test
	public void descriptorMappingTest6() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin descriptorMappingTest6");
		boolean pass1 = true;
		boolean pass2 = false;
		int foundProject1 = 0;
		int foundProject2 = 0;
		final Long[] expectedResults1 = new Long[] { 123456789L, 345678901L, 567890123L };
		final Long[] expectedResults2 = new Long[] { 234567890L, 345678901L, 456789012L };

		try {
			createPeople();
			createProjects();

			getEntityTransaction().begin();

			pRef[5].getXProjects().add(projRef[0]);
			pRef[5].getXProjects().add(projRef[2]);
			pRef[5].getXProjects().add(projRef[4]);
			getEntityManager().merge(pRef[5]);

			pRef[8].getXProjects().add(projRef[1]);
			pRef[8].getXProjects().add(projRef[2]);
			pRef[8].getXProjects().add(projRef[3]);
			getEntityManager().merge(pRef[8]);

			projRef[0].getXPersons().add(pRef[5]);
			getEntityManager().merge(projRef[0]);

			projRef[1].getXPersons().add(pRef[8]);
			getEntityManager().merge(projRef[1]);

			projRef[2].getXPersons().add(pRef[5]);
			getEntityManager().merge(projRef[2]);

			projRef[2].getXPersons().add(pRef[8]);
			getEntityManager().merge(projRef[2]);

			projRef[3].getXPersons().add(pRef[8]);
			getEntityManager().merge(projRef[3]);

			projRef[4].getXPersons().add(pRef[5]);
			getEntityManager().merge(projRef[4]);

			getEntityManager().flush();

			XPerson p1 = getEntityManager().find(XPerson.class, 6);
			XPerson p2 = getEntityManager().find(XPerson.class, 9);

			Collection<XProject> projCol1 = p1.getXProjects();
			Collection<XProject> projCol2 = p2.getXProjects();

			if ((projCol1.size() != 3) || (projCol2.size() != 3)) {
				logger.log(Logger.Level.ERROR,
						"descriptorMappingTest6: Did not get expected results."
								+ "Expected 3 Projects for Karen Tegan (PK 6) , got: " + projCol1.size()
								+ ", Expected 2 Projects for William Keaton (PK 9), got: " + projCol2.size());
				pass1 = false;
			} else if (pass1) {

				Iterator i1 = projCol1.iterator();
				while (i1.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Karen Tegan Projects");
					XProject o1 = (XProject) i1.next();

					for (int l = 0; l < 3; l++) {
						if (expectedResults1[l].equals(o1.getXProjId())) {
							logger.log(Logger.Level.TRACE, "Found Project for Karen Tegan: " + o1.getXName());
							foundProject1++;
							break;
						}
					}
				}

				Iterator i2 = projCol2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for William Keaton Projects");
					XProject o2 = (XProject) i2.next();

					for (int l = 0; l < 3; l++) {
						if (expectedResults2[l].equals(o2.getXProjId())) {
							logger.log(Logger.Level.TRACE, "Found Project for William Keaton: " + o2.getXName());
							foundProject2++;
							break;
						}
					}
				}

			}

			if ((foundProject1 != 3) || (foundProject2 != 3)) {
				logger.log(Logger.Level.ERROR, "descriptorMappingTest6: Did not get expected results");
				pass2 = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			Collection<XPerson> nullPersonCol = new Vector<XPerson>();
			projRef[0].setXPersons(nullPersonCol);
			getEntityManager().merge(projRef[0]);

			projRef[1].setXPersons(nullPersonCol);
			getEntityManager().merge(projRef[1]);

			projRef[2].setXPersons(nullPersonCol);
			getEntityManager().merge(projRef[2]);

			projRef[3].setXPersons(nullPersonCol);
			getEntityManager().merge(projRef[3]);

			projRef[4].setXPersons(nullPersonCol);
			getEntityManager().merge(projRef[4]);

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("descriptorMappingTest6 failed");
	}

	/*
	 * @testName: descriptorMappingTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors UniDirectional
	 * OneToMany Relationship
	 *
	 * OnePerson To ManyReviews
	 *
	 * Entity Person mapped to table named PERSON references a collection of Entity
	 * AnnualReview. Entity AnnualReview is mapped to a table named ANNUALREVIEW
	 * which does not reference Entity Person. Entity Person is the owner of the
	 * relationship.
	 *
	 * There is a join table named PERSON_ANNUALREVIEW (owner named first. One
	 * foreign key column refers to table PERSON. The name of the foreign key column
	 * is PERSON_PERSONID. The other foreign key column is refers to the
	 * ANNUALREVIEW table. The name of this foreign key is ANNUALREVIEWS_AID.
	 *
	 */
	@Test
	public void descriptorMappingTest7() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin descriptorMappingTest7");
		boolean pass1 = true;
		boolean pass2 = false;

		Vector<XAnnualReview> v1 = null;
		Vector<XAnnualReview> v2 = null;
		Vector<XAnnualReview> v3 = null;
		Vector<XAnnualReview> v4 = null;
		int foundCol1 = 0;
		int foundCol2 = 0;
		int foundCol3 = 0;
		int foundCol4 = 0;
		int foundCol5 = 0;
		Integer[] expectedCol1 = new Integer[] { 1, 2, 3, 4 };
		Integer[] expectedCol2 = new Integer[] { 5, 6 };
		Integer[] expectedCol3 = new Integer[] { 3, 8 };
		Integer[] expectedCol4 = new Integer[] { 4, 7 };
		Integer[] expectedCol5 = new Integer[] { 1, 2, 3, 4 };

		try {
			createReviews();
			createPeople();

			getEntityTransaction().begin();

			v1 = new Vector<XAnnualReview>();
			v1.add(rRef[0]);
			v1.add(rRef[1]);
			v1.add(rRef[2]);
			v1.add(rRef[3]);

			v2 = new Vector<XAnnualReview>();
			v2.add(rRef[4]);
			v2.add(rRef[5]);

			v3 = new Vector<XAnnualReview>();
			v3.add(rRef[2]);
			v3.add(rRef[7]);

			v4 = new Vector<XAnnualReview>();
			v4.add(rRef[3]);
			v4.add(rRef[6]);

			pRef[11].setXAnnualReviews(v1);
			pRef[13].setXAnnualReviews(v2);
			pRef[15].setXAnnualReviews(v3);
			pRef[17].setXAnnualReviews(v4);
			pRef[19].setXAnnualReviews(v1);

			getEntityManager().merge(pRef[11]);
			getEntityManager().merge(pRef[13]);
			getEntityManager().merge(pRef[15]);
			getEntityManager().merge(pRef[17]);
			getEntityManager().merge(pRef[19]);

			getEntityManager().flush();

			XPerson p1 = getEntityManager().find(XPerson.class, 12);
			XPerson p2 = getEntityManager().find(XPerson.class, 14);
			XPerson p3 = getEntityManager().find(XPerson.class, 16);
			XPerson p4 = getEntityManager().find(XPerson.class, 18);
			XPerson p5 = getEntityManager().find(XPerson.class, 20);

			Collection<XAnnualReview> col1 = p1.getXAnnualReviews();
			Collection<XAnnualReview> col2 = p2.getXAnnualReviews();
			Collection<XAnnualReview> col3 = p3.getXAnnualReviews();
			Collection<XAnnualReview> col4 = p4.getXAnnualReviews();
			Collection<XAnnualReview> col5 = p5.getXAnnualReviews();

			if ((col1.size() != 4) || (col2.size() != 2 || col3.size() != 2 || col4.size() != 2 || col5.size() != 4)) {
				logger.log(Logger.Level.ERROR,
						"descriptorMappingTest7: Did not get expected results."
								+ "Expected 4 reviews for Mary Macy (PK 12) , got: " + col1.size()
								+ ", Expected 2 reviews for Julie OClaire (PK 14), got: " + col2.size()
								+ ", Expected 2 reviews for Kellie Lee (PK 16), got: " + col3.size()
								+ ", Expected 2 reviews for Mark Francis (PK 18), got: " + col4.size()
								+ ", Expected 4 reviews for Katy Hughes (PK 20), got: " + col5.size());
				pass1 = false;
			} else if (pass1) {

				Iterator i1 = col1.iterator();
				while (i1.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Mary Macy Reviews");
					XAnnualReview o1 = (XAnnualReview) i1.next();

					for (int l = 0; l < 5; l++) {
						if (expectedCol1[l].equals(o1.getXService())) {
							logger.log(Logger.Level.TRACE,
									"Found Mary Macy Annual Review for Service Year: " + o1.getXService());
							foundCol1++;
							break;
						}
					}
				}

				Iterator i2 = col2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Julie OClaire Reviews");
					XAnnualReview o2 = (XAnnualReview) i2.next();

					for (int l = 0; l < 2; l++) {
						if (expectedCol2[l].equals(o2.getXService())) {
							logger.log(Logger.Level.TRACE,
									"Found Julie OClaire Annual Review for Service Year: " + o2.getXService());
							foundCol2++;
							break;
						}
					}
				}

				Iterator i3 = col3.iterator();
				while (i3.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Kellie Lee Reviews");
					XAnnualReview o3 = (XAnnualReview) i3.next();

					for (int l = 0; l < 2; l++) {
						if (expectedCol3[l].equals(o3.getXService())) {
							logger.log(Logger.Level.TRACE,
									"Found Kellie Lee Annual Review for Service Year: " + o3.getXService());
							foundCol3++;
							break;
						}
					}
				}

				Iterator i4 = col4.iterator();
				while (i4.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Mark Francis Reviews");
					XAnnualReview o4 = (XAnnualReview) i4.next();

					for (int l = 0; l < 2; l++) {
						if (expectedCol4[l].equals(o4.getXService())) {
							logger.log(Logger.Level.TRACE,
									"Found Mark Francis Annual Review for Service Year: " + o4.getXService());
							foundCol4++;
							break;
						}
					}
				}

				Iterator i5 = col5.iterator();
				while (i5.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Katy Hughes Reviews");
					XAnnualReview o5 = (XAnnualReview) i5.next();

					for (int l = 0; l < 5; l++) {
						if (expectedCol5[l].equals(o5.getXService())) {
							logger.log(Logger.Level.TRACE,
									"Found Katy Hughes Annual Review for Service Year: " + o5.getXService());
							foundCol5++;
							break;
						}
					}
				}

			}

			if ((foundCol1 != 4) || (foundCol2 != 2) || (foundCol3 != 2) || (foundCol4 != 2) || (foundCol5 != 4)) {

				logger.log(Logger.Level.ERROR, "descriptorMappingTest7: Did not get expected results");
				pass2 = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("descriptorMappingTest7 failed");
	}

	/*
	 * @testName: descriptorMappingTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1144; PERSISTENCE:SPEC:919;
	 * 
	 * @test_Strategy: RelationShip Mapping Using Descriptors UniDirectional
	 * ManyToMany Relationship
	 *
	 * ManyPersons To ManyInsurances
	 *
	 * Entity Person mapped to table named PERSON references a collection of Entity
	 * Insurance. Entity Insurance is mapped to a table named INSURANCE and which
	 * does not reference Entity Person. Entity Person is the owner of the
	 * relationship.
	 *
	 * There is a join table named PERSON_INSURANCE (owner named first. One foreign
	 * key column refers to table PERSON. The name of the foreign key column is
	 * PERSON_PERSONID. The other foreign key column is refers to the INSURANCE
	 * table. The name of this foreign key is INSURANCES_INSID.
	 *
	 */
	@Test
	public void descriptorMappingTest8() throws Exception {
		boolean pass1 = true;
		boolean pass2 = false;
		int foundInsurance1 = 0;
		int foundInsurance2 = 0;
		int foundInsurance3 = 0;
		Integer[] expectedResults1 = new Integer[] { 1, 3 };
		Integer[] expectedResults2 = new Integer[] { 2, 3 };
		Integer[] expectedResults3 = new Integer[] { 1, 2, 3 };

		try {
			createPeople();
			createInsurance();

			getEntityTransaction().begin();

			pRef[2].getXInsurance().add(insRef[0]);
			pRef[2].getXInsurance().add(insRef[2]);
			getEntityManager().merge(pRef[2]);

			pRef[12].getXInsurance().add(insRef[1]);
			pRef[12].getXInsurance().add(insRef[2]);
			getEntityManager().merge(pRef[12]);

			pRef[16].getXInsurance().add(insRef[0]);
			pRef[16].getXInsurance().add(insRef[1]);
			pRef[16].getXInsurance().add(insRef[2]);
			getEntityManager().merge(pRef[16]);

			getEntityManager().flush();

			XPerson p1 = getEntityManager().find(XPerson.class, 3);
			XPerson p2 = getEntityManager().find(XPerson.class, 13);
			XPerson p3 = getEntityManager().find(XPerson.class, 17);

			Collection<XInsurance> insCol1 = p1.getXInsurance();
			Collection<XInsurance> insCol2 = p2.getXInsurance();
			Collection<XInsurance> insCol3 = p3.getXInsurance();

			if ((insCol1.size() != 2) || (insCol2.size() != 2) || (insCol3.size() != 3)) {
				logger.log(Logger.Level.ERROR,
						"descriptorMappingTest8: Did not get expected results."
								+ "Expected 2 Insurance Carriers for Shelly McGowan (PK 3) , got: " + insCol1.size()
								+ ", Expected 2 Insurance Carriers for Cheng Fang (PK 13) , got: " + insCol2.size()
								+ ", Expected 3 Insurance Carriers for Nicole Martin (PK 17), got: " + insCol3.size());
				pass1 = false;
			} else if (pass1) {

				Iterator i1 = insCol1.iterator();
				while (i1.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Insurance Carriers for Shelly McGowan");
					XInsurance o1 = (XInsurance) i1.next();

					for (int l = 0; l < 2; l++) {
						if (expectedResults1[l].equals(o1.getXInsId())) {
							logger.log(Logger.Level.TRACE,
									"Found Insurance Carrier for Shelly McGowan: " + o1.getXCarrier());
							foundInsurance1++;
							break;
						}
					}
				}

				Iterator i2 = insCol2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Insurance Carriers for Cheng Fang");
					XInsurance o2 = (XInsurance) i2.next();

					for (int l = 0; l < 2; l++) {
						if (expectedResults2[l].equals(o2.getXInsId())) {
							logger.log(Logger.Level.TRACE,
									"Found Insurance Carrier for Cheng Fang: " + o2.getXCarrier());
							foundInsurance2++;
							break;
						}
					}
				}

				Iterator i3 = insCol3.iterator();
				while (i3.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Insurance Carriers for Nicole Martin");
					XInsurance o3 = (XInsurance) i3.next();

					for (int l = 0; l < 3; l++) {
						if (expectedResults3[l].equals(o3.getXInsId())) {
							logger.log(Logger.Level.TRACE,
									"Found Insurance Carrier for Nicole Martin: " + o3.getXCarrier());
							foundInsurance3++;
							break;
						}
					}
				}

			}

			if ((foundInsurance1 != 2) || (foundInsurance2 != 2) || (foundInsurance3 != 3)) {
				logger.log(Logger.Level.ERROR, "descriptorMappingTest8: Did not get expected results");
				pass2 = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("descriptorMappingTest8 failed");
	}

	/*
	 * Business Methods to set up data for Test Cases
	 */

	private void createPeople() throws Exception {
		logger.log(Logger.Level.TRACE, "CreatePeople");

		logger.log(Logger.Level.TRACE, "Create 20 People");
		pRef[0] = new XPerson(1, "Alan", "Frechette");
		pRef[1] = new XPerson(2, "Arthur", "Frechette");
		pRef[2] = new XPerson(3, "Shelly", "McGowan");
		pRef[3] = new XPerson(4, "Robert", "Bissett");
		pRef[4] = new XPerson(5, "Stephen", "DMilla");
		pRef[5] = new XPerson(6, "Karen", "Tegan");
		pRef[6] = new XPerson(7, "Stephen", "Cruise");
		pRef[7] = new XPerson(8, "Irene", "Caruso");
		pRef[8] = new XPerson(9, "William", "Keaton");
		pRef[9] = new XPerson(10, "Kate", "Hudson");
		pRef[10] = new XPerson(11, "Jonathan", "Smith");
		pRef[11] = new XPerson(12, "Mary", "Macy");
		pRef[12] = new XPerson(13, "Cheng", "Fang");
		pRef[13] = new XPerson(14, "Julie", "OClaire");
		pRef[14] = new XPerson(15, "Steven", "Rich");
		pRef[15] = new XPerson(16, "Kellie", "Lee");
		pRef[16] = new XPerson(17, "Nicole", "Martin");
		pRef[17] = new XPerson(18, "Mark", "Francis");
		pRef[18] = new XPerson(19, "Will", "Forrest");
		pRef[19] = new XPerson(20, "Katy", "Hughes");

		logger.log(Logger.Level.TRACE, "Start to persist people ");
		getEntityTransaction().begin();
		for (XPerson p : pRef) {
			getEntityManager().persist(p);
			logger.log(Logger.Level.TRACE, "persisted person " + p);
		}
		getEntityTransaction().commit();
	}

	private void createTeams() throws Exception {

		logger.log(Logger.Level.TRACE, "Create 5 Teams");
		tRef[0] = new XTeam(1, "Engineering");
		tRef[1] = new XTeam(2, "Marketing");
		tRef[2] = new XTeam(3, "Sales");
		tRef[3] = new XTeam(4, "Accounting");
		tRef[4] = new XTeam(5, "Training");

		logger.log(Logger.Level.TRACE, "Start to persist teams ");
		getEntityTransaction().begin();
		for (XTeam t : tRef) {
			if (t != null) {
				getEntityManager().persist(t);
				logger.log(Logger.Level.TRACE, "persisted team " + t);
			}
		}
		getEntityTransaction().commit();

	}

	private void createInsurance() throws Exception {
		logger.log(Logger.Level.TRACE, "Create 3 Insurance Carriers");
		insRef[0] = new XInsurance(1, "Prudential");
		insRef[1] = new XInsurance(2, "Cigna");
		insRef[2] = new XInsurance(3, "Sentry");

		logger.log(Logger.Level.TRACE, "Start to persist insurance ");
		getEntityTransaction().begin();
		for (XInsurance i : insRef) {
			if (i != null) {

				getEntityManager().persist(i);
				logger.log(Logger.Level.TRACE, "persisted insurance " + i);
			}
		}
		getEntityTransaction().commit();

	}

	private void createProjects() throws Exception {
		logger.log(Logger.Level.TRACE, "Create 5 Projects");
		projRef[0] = new XProject(123456789L, "Sidewinder", new BigDecimal("20500.0"));
		projRef[1] = new XProject(234567890L, "Boa", new BigDecimal("75000.0"));
		projRef[2] = new XProject(345678901L, "Asp", new BigDecimal("500000.0"));
		projRef[3] = new XProject(456789012L, "King Cobra", new BigDecimal("250000.0"));
		projRef[4] = new XProject(567890123L, "Python", new BigDecimal("1000.0"));

		logger.log(Logger.Level.TRACE, "Start to persist projects ");
		getEntityTransaction().begin();
		for (XProject p : projRef) {
			if (p != null) {

				getEntityManager().persist(p);
				logger.log(Logger.Level.TRACE, "persisted project " + p);
			}
		}
		getEntityTransaction().commit();

	}

	private void createCompany() throws Exception {

		logger.log(Logger.Level.TRACE, "Create 2 Companies");
		cRef[0] = new XCompany(25501L, "American Gifts");
		cRef[1] = new XCompany(37560L, "Planet Earth");

		logger.log(Logger.Level.TRACE, "Start to persist companies ");
		getEntityTransaction().begin();

		for (XCompany c : cRef) {
			if (c != null) {

				getEntityManager().persist(c);
				logger.log(Logger.Level.TRACE, "persisted company " + c);
			}
		}
		getEntityTransaction().commit();

	}

	private void createAddress() throws Exception {
		logger.log(Logger.Level.TRACE, "Create 2 Addresses");
		aRef[0] = new XAddress("100", "1 Network Drive", "Burlington", "MA", "01803");
		aRef[1] = new XAddress("200", "4150 Network Drive", "Santa Clara", "CA", "95054");

		logger.log(Logger.Level.TRACE, "Start to persist addresses ");
		getEntityTransaction().begin();
		for (XAddress a : aRef) {
			if (a != null) {
				getEntityManager().persist(a);
				logger.log(Logger.Level.TRACE, "persisted address " + a);
			}
		}
		getEntityTransaction().commit();

	}

	private void createReviews() throws Exception {
		logger.log(Logger.Level.TRACE, "Create 5 Addresses");
		rRef[0] = new XAnnualReview(1, 1);
		rRef[1] = new XAnnualReview(2, 2);
		rRef[2] = new XAnnualReview(3, 3);
		rRef[3] = new XAnnualReview(4, 4);
		rRef[4] = new XAnnualReview(5, 5);
		rRef[5] = new XAnnualReview(6, 6);
		rRef[6] = new XAnnualReview(7, 7);
		rRef[7] = new XAnnualReview(8, 8);

		logger.log(Logger.Level.TRACE, "Start to persist annual reviews ");
		getEntityTransaction().begin();
		for (XAnnualReview a : rRef) {
			if (a != null) {
				getEntityManager().persist(a);
				logger.log(Logger.Level.TRACE, "persisted annual reviews " + a);
			}
		}
		getEntityTransaction().commit();

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

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM PERSON_INSURANCE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PERSON_ANNUALREVIEW").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM INSURANCE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ANNUALREVIEW").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PERSON").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM TEAM").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM COMPANY").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ADDRESS").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PROJECT_PERSON").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PROJECT").executeUpdate();
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
