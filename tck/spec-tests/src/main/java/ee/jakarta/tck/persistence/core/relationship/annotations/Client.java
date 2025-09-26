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

package ee.jakarta.tck.persistence.core.relationship.annotations;

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

	private static final Address aRef[] = new Address[5];

	private static final AnnualReview rRef[] = new AnnualReview[10];

	private static final Company cRef[] = new Company[5];

	private static final Insurance insRef[] = new Insurance[5];

	private static final Person pRef[] = new Person[20];

	private static final Project projRef[] = new Project[10];

	private static final Team tRef[] = new Team[10];

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "AnnualReview", pkgName + "Company", pkgName + "Insurance",
				pkgName + "Person", pkgName + "Project", pkgName + "Team" };
		return createDeploymentJar("jpa_core_relationship_annotations.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: annotationMappingTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
	 * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
	 * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
	 * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
	 * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations BiDirectional OneToOne
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
	public void annotationMappingTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin annotationMappingTest1");
		boolean pass = false;

		try {
			createPeople();
			createProjects();
			getEntityTransaction().begin();

			pRef[0].setProject(projRef[2]);
			projRef[2].setProjectLead(pRef[0]);

			getEntityManager().merge(pRef[0]);
			getEntityManager().merge(projRef[2]);

			Person newPerson = getEntityManager().find(Person.class, 1);

			if (newPerson.getProject().getName().equals("Asp")) {
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
			throw new Exception("annotationMappingTest1 failed");
	}

	/*
	 * @testName: annotationMappingPersistTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
	 * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
	 * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
	 * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
	 * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations BiDirectional OneToOne
	 * Relationship
	 *
	 * OnePerson to OneProjectLead
	 *
	 * Entity Person mapped to table named PERSON references a single instance of
	 * Project. Entity Project mapped to table named PROJECT a single instance of
	 * Person. Entity Person is the owner of the relationship.
	 *
	 * Table PERSON contains a foreign key to PROJECT. The foreign key is named
	 * PROJECT_PROJID. set both sides of the association inside the same
	 * transaction, and persist the owing side, which should be cascaded to the
	 * inverse side.
	 */
	@Test
	public void annotationMappingPersistTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin annotationMappingPersistTest1");
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			createPeople(false);
			createProjects(false);
			pRef[0].setProject(projRef[2]);
			projRef[2].setProjectLead(pRef[0]);

			getEntityManager().persist(pRef[0]);
			getEntityTransaction().commit();
			Person personFound = getEntityManager().find(Person.class, pRef[0].getPersonId());
			Project projectFound = getEntityManager().find(Project.class, projRef[2].getProjId());
			if (personFound != null && projectFound != null) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Failed to find persisted Person or Project: " + " personFound: "
						+ personFound + ", projectFound: " + projectFound);
			}
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
			throw new Exception("annotationMappingPersistTest1 failed");
	}

	/*
	 * @testName: annotationMappingTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1090; PERSISTENCE:SPEC:1091;
	 * PERSISTENCE:JAVADOC:9; PERSISTENCE:JAVADOC:110; PERSISTENCE:SPEC:561;
	 * PERSISTENCE:SPEC:564; PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570;
	 * PERSISTENCE:SPEC:572
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations BiDirectional ManyToOne
	 * Relationship
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
	public void annotationMappingTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin annotationMappingTest2");
		boolean pass1 = true;
		boolean pass2 = false;
		Vector<Team> v1;
		Vector<Team> v2;
		int foundTeam1 = 0;
		int foundTeam2 = 0;
		final String[] expectedTeam1 = new String[] { "Engineering", "Marketing", "Sales" };
		final String[] expectedTeam2 = new String[] { "Accounting", "Training" };

		try {
			createTeams();
			createCompany();

			getEntityTransaction().begin();

			tRef[0].setCompany(cRef[0]);
			tRef[1].setCompany(cRef[0]);
			tRef[2].setCompany(cRef[0]);

			v1 = new Vector<Team>();
			v1.add(tRef[0]);
			v1.add(tRef[1]);
			v1.add(tRef[2]);
			cRef[0].setTeams(v1);

			tRef[3].setCompany(cRef[1]);
			tRef[4].setCompany(cRef[1]);

			v2 = new Vector<Team>();
			v2.add(tRef[3]);
			v2.add(tRef[4]);
			cRef[1].setTeams(v2);

			getEntityManager().merge(tRef[0]);
			getEntityManager().merge(tRef[1]);
			getEntityManager().merge(tRef[2]);
			getEntityManager().merge(tRef[3]);
			getEntityManager().merge(tRef[4]);

			getEntityManager().merge(cRef[0]);
			getEntityManager().merge(cRef[1]);
			getEntityManager().flush();

			Company c1 = getEntityManager().find(Company.class, 25501L);
			Collection<Team> t1 = c1.getTeams();

			Company c2 = getEntityManager().find(Company.class, 37560L);
			Collection<Team> t2 = c2.getTeams();

			if ((t1.size() != 3) || (t2.size() != 2)) {
				logger.log(Logger.Level.ERROR,
						"annotationMappingTest2: Did not get expected results."
								+ "Team1 Collection Expected 3 references, got: " + t1.size()
								+ ", Team2 Collection Expected 2 references, got: " + t2.size());
				pass1 = false;
			} else if (pass1) {

				Iterator i1 = t1.iterator();
				while (i1.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Team 1 Collection for expected Teams");
					Team o1 = (Team) i1.next();

					for (int l = 0; l < 3; l++) {
						if (expectedTeam1[l].equals((String) o1.getName())) {
							logger.log(Logger.Level.TRACE, "Found Team 1:" + (String) o1.getName());
							foundTeam1++;
							break;
						}
					}
				}

				Iterator i2 = t2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Team 2 Collection for expected Teams");
					Team o2 = (Team) i2.next();

					for (int l = 0; l < 2; l++) {
						if (expectedTeam2[l].equals((String) o2.getName())) {
							logger.log(Logger.Level.TRACE, "Found Team 2:" + (String) o2.getName());
							foundTeam2++;
							break;
						}
					}
				}
			}

			if ((foundTeam1 != 3) || (foundTeam2 != 2)) {
				logger.log(Logger.Level.ERROR, "annotationMappingTest2: Did not get expected results");
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
			throw new Exception("annotationMappingTest2 failed");
	}

	/*
	 * @testName: annotationMappingTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1095; PERSISTENCE:JAVADOC:131;
	 * PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:563; PERSISTENCE:SPEC:567;
	 * PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations BiDirectional OneToMany
	 * Relationship
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
	public void annotationMappingTest3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin annotationMappingTest3");
		boolean pass = false;
		Vector v1;
		Vector v2;

		try {
			createTeams();
			createCompany();

			getEntityTransaction().begin();

			tRef[0].setCompany(cRef[0]);
			tRef[1].setCompany(cRef[0]);

			v1 = new Vector();
			v1.add(tRef[0]);
			v1.add(tRef[1]);
			cRef[0].setTeams(v1);

			tRef[2].setCompany(cRef[1]);
			tRef[3].setCompany(cRef[1]);
			tRef[4].setCompany(cRef[1]);

			v2 = new Vector();
			v2.add(tRef[2]);
			v2.add(tRef[3]);
			v2.add(tRef[4]);

			cRef[1].setTeams(v2);

			getEntityManager().merge(tRef[0]);
			getEntityManager().merge(tRef[1]);
			getEntityManager().merge(tRef[2]);
			getEntityManager().merge(tRef[3]);
			getEntityManager().merge(tRef[4]);

			getEntityManager().merge(cRef[0]);
			getEntityManager().merge(cRef[1]);

			getEntityManager().flush();

			Team t1 = getEntityManager().find(Team.class, 1);
			Team t2 = getEntityManager().find(Team.class, 2);
			Team t3 = getEntityManager().find(Team.class, 3);
			Team t4 = getEntityManager().find(Team.class, 4);
			Team t5 = getEntityManager().find(Team.class, 5);

			if ((t1.getCompany().getCompanyId() == 25501L) && (t2.getCompany().getCompanyId() == 25501L)
					&& (t3.getCompany().getCompanyId() == 37560L) && (t4.getCompany().getCompanyId() == 37560L)
					&& (t5.getCompany().getCompanyId() == 37560L)) {

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
			throw new Exception("annotationMappingTest3 failed");
	}

	/*
	 * @testName: annotationMappingTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:SPEC:561;
	 * PERSISTENCE:SPEC:562; PERSISTENCE:SPEC:568
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations UniDirectional OneToOne
	 * Relationship
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
	public void annotationMappingTest4() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin annotationMappingTest4");
		boolean pass = false;

		try {

			createAddress();
			createCompany();

			getEntityTransaction().begin();

			cRef[0].setAddress(aRef[0]);
			cRef[1].setAddress(aRef[1]);

			getEntityManager().merge(cRef[0]);
			getEntityManager().merge(cRef[1]);

			getEntityManager().flush();

			Company c1 = getEntityManager().find(Company.class, 25501L);
			Company c2 = getEntityManager().find(Company.class, 37560L);

			if (c1.getAddress().getCity().equals("Burlington") && c2.getAddress().getCity().equals("Santa Clara")) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE,
						"annotationMappingTest4: Did not get expected results"
								+ "Expected: Burlington and Santa Clara, got: " + c1.getAddress().getCity()
								+ c2.getAddress().getCity());
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
			throw new Exception("annotationMappingTest4 failed");
	}

	/*
	 * @testName: annotationMappingTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1091; PERSISTENCE:SPEC:561;
	 * PERSISTENCE:SPEC:564; PERSISTENCE:SPEC:568
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations UniDirectional ManyToOne
	 * Relationship
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
	public void annotationMappingTest5() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin annotationMappingTest5");
		boolean pass = false;

		try {
			createTeams();
			createPeople();

			getEntityTransaction().begin();

			pRef[1].setTeam(tRef[0]);
			pRef[3].setTeam(tRef[1]);
			pRef[5].setTeam(tRef[2]);
			pRef[7].setTeam(tRef[3]);
			pRef[9].setTeam(tRef[4]);

			pRef[2].setTeam(tRef[4]);
			pRef[4].setTeam(tRef[3]);
			pRef[6].setTeam(tRef[2]);
			pRef[8].setTeam(tRef[1]);
			pRef[10].setTeam(tRef[0]);

			for (int i = 1; i < 11; i++) {
				getEntityManager().merge(pRef[i]);
			}

			getEntityManager().flush();

			if ((pRef[1].getTeam() == tRef[0]) && (pRef[10].getTeam() == tRef[0]) && (pRef[3].getTeam() == tRef[1])
					&& (pRef[8].getTeam() == tRef[1]) && (pRef[5].getTeam() == tRef[2])
					&& (pRef[6].getTeam() == tRef[2]) && (pRef[7].getTeam() == tRef[3])
					&& (pRef[4].getTeam() == tRef[3]) && (pRef[9].getTeam() == tRef[4])
					&& (pRef[2].getTeam() == tRef[4])) {

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
			throw new Exception("annotationMappingTest5 failed");
	}

	/*
	 * @testName: annotationMappingTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1097; PERSISTENCE:SPEC:1098;
	 * PERSISTENCE:JAVADOC:109; PERSISTENCE:JAVADOC:106; PERSISTENCE:JAVADOC:99;
	 * PERSISTENCE:JAVADOC:100; PERSISTENCE:JAVADOC:101; PERSISTENCE:SPEC:561;
	 * PERSISTENCE:SPEC:565; PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:574;
	 * PERSISTENCE:SPEC:1099
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations BiDirectional ManyToMany
	 * Relationship
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
	public void annotationMappingTest6() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin annotationMappingTest6");
		boolean pass1 = true;
		boolean pass2 = false;
		Vector<Team> v1;
		Vector<Team> v2;
		int foundProject1 = 0;
		int foundProject2 = 0;
		final Long[] expectedResults1 = new Long[] { 123456789L, 345678901L, 567890123L };
		final Long[] expectedResults2 = new Long[] { 234567890L, 345678901L, 456789012L };

		try {
			createPeople();
			createProjects();

			getEntityTransaction().begin();

			pRef[5].getProjects().add(projRef[0]);
			pRef[5].getProjects().add(projRef[2]);
			pRef[5].getProjects().add(projRef[4]);
			getEntityManager().merge(pRef[5]);

			pRef[8].getProjects().add(projRef[1]);
			pRef[8].getProjects().add(projRef[2]);
			pRef[8].getProjects().add(projRef[3]);
			getEntityManager().merge(pRef[8]);

			projRef[0].getPersons().add(pRef[5]);
			getEntityManager().merge(projRef[0]);

			projRef[1].getPersons().add(pRef[8]);
			getEntityManager().merge(projRef[1]);

			projRef[2].getPersons().add(pRef[5]);
			getEntityManager().merge(projRef[2]);

			projRef[2].getPersons().add(pRef[8]);
			getEntityManager().merge(projRef[2]);

			projRef[3].getPersons().add(pRef[8]);
			getEntityManager().merge(projRef[3]);

			projRef[4].getPersons().add(pRef[5]);
			getEntityManager().merge(projRef[4]);

			getEntityManager().flush();

			Person p1 = getEntityManager().find(Person.class, 6);
			Person p2 = getEntityManager().find(Person.class, 9);

			Collection<Project> projCol1 = p1.getProjects();
			Collection<Project> projCol2 = p2.getProjects();

			if ((projCol1.size() != 3) || (projCol2.size() != 3)) {
				logger.log(Logger.Level.ERROR,
						"annotationMappingTest6: Did not get expected results."
								+ "Expected 3 Projects for Karen Tegan (PK 6) , got: " + projCol1.size()
								+ ", Expected 2 Projects for William Keaton (PK 9), got: " + projCol2.size());
				pass1 = false;
			} else if (pass1) {

				Iterator i1 = projCol1.iterator();
				while (i1.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Karen Tegan Projects");
					Project o1 = (Project) i1.next();

					for (int l = 0; l < 3; l++) {
						if (expectedResults1[l].equals((Long) o1.getProjId())) {
							logger.log(Logger.Level.TRACE, "Found Project for Karen Tegan: " + (String) o1.getName());
							foundProject1++;
							break;
						}
					}
				}

				Iterator i2 = projCol2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for William Keaton Projects");
					Project o2 = (Project) i2.next();

					for (int l = 0; l < 3; l++) {
						if (expectedResults2[l].equals((Long) o2.getProjId())) {
							logger.log(Logger.Level.TRACE,
									"Found Project for William Keaton: " + (String) o2.getName());
							foundProject2++;
							break;
						}
					}
				}

			}

			if ((foundProject1 != 3) || (foundProject2 != 3)) {
				logger.log(Logger.Level.ERROR, "annotationMappingTest6: Did not get expected results");
				pass2 = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			Collection<Person> nullPersonCol = new Vector<Person>();
			projRef[0].setPersons(nullPersonCol);
			getEntityManager().merge(projRef[0]);

			projRef[1].setPersons(nullPersonCol);
			getEntityManager().merge(projRef[1]);

			projRef[2].setPersons(nullPersonCol);
			getEntityManager().merge(projRef[2]);

			projRef[3].setPersons(nullPersonCol);
			getEntityManager().merge(projRef[3]);

			projRef[4].setPersons(nullPersonCol);
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
			throw new Exception("annotationMappingTest6 failed");
	}

	/*
	 * @testName: annotationMappingTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1095; PERSISTENCE:SPEC:1097;
	 * PERSISTENCE:JAVADOC:99; PERSISTENCE:JAVADOC:100; PERSISTENCE:JAVADOC:101;
	 * PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:563; PERSISTENCE:SPEC:568
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations UniDirectional OneToMany
	 * Relationship
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
	public void annotationMappingTest7() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin annotationMappingTest7");
		boolean pass1 = true;
		boolean pass2 = false;

		Vector<AnnualReview> v1 = null;
		Vector<AnnualReview> v2 = null;
		Vector<AnnualReview> v3 = null;
		Vector<AnnualReview> v4 = null;
		int foundCol1 = 0;
		int foundCol2 = 0;
		int foundCol3 = 0;
		int foundCol4 = 0;
		int foundCol5 = 0;
		final Integer[] expectedCol1 = new Integer[] { 1, 2, 3, 4 };
		final Integer[] expectedCol2 = new Integer[] { 5, 6 };
		final Integer[] expectedCol3 = new Integer[] { 3, 8 };
		final Integer[] expectedCol4 = new Integer[] { 4, 7 };
		final Integer[] expectedCol5 = new Integer[] { 1, 2, 3, 4 };

		try {
			createReviews();
			createPeople();

			getEntityTransaction().begin();

			v1 = new Vector<AnnualReview>();
			v1.add(rRef[0]);
			v1.add(rRef[1]);
			v1.add(rRef[2]);
			v1.add(rRef[3]);

			v2 = new Vector<AnnualReview>();
			v2.add(rRef[4]);
			v2.add(rRef[5]);

			v3 = new Vector<AnnualReview>();
			v3.add(rRef[2]);
			v3.add(rRef[7]);

			v4 = new Vector<AnnualReview>();
			v4.add(rRef[3]);
			v4.add(rRef[6]);

			pRef[11].setAnnualReviews(v1);
			pRef[13].setAnnualReviews(v2);
			pRef[15].setAnnualReviews(v3);
			pRef[17].setAnnualReviews(v4);
			pRef[19].setAnnualReviews(v1);

			getEntityManager().merge(pRef[11]);
			getEntityManager().merge(pRef[13]);
			getEntityManager().merge(pRef[15]);
			getEntityManager().merge(pRef[17]);
			getEntityManager().merge(pRef[19]);

			getEntityManager().flush();

			Person p1 = getEntityManager().find(Person.class, 12);
			Person p2 = getEntityManager().find(Person.class, 14);
			Person p3 = getEntityManager().find(Person.class, 16);
			Person p4 = getEntityManager().find(Person.class, 18);
			Person p5 = getEntityManager().find(Person.class, 20);

			Collection<AnnualReview> col1 = p1.getAnnualReviews();
			Collection<AnnualReview> col2 = p2.getAnnualReviews();
			Collection<AnnualReview> col3 = p3.getAnnualReviews();
			Collection<AnnualReview> col4 = p4.getAnnualReviews();
			Collection<AnnualReview> col5 = p5.getAnnualReviews();

			if ((col1.size() != 4) || (col2.size() != 2 || col3.size() != 2 || col4.size() != 2 || col5.size() != 4)) {
				logger.log(Logger.Level.ERROR,
						"annotationMappingTest7: Did not get expected results."
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
					AnnualReview o1 = (AnnualReview) i1.next();

					for (int l = 0; l < 5; l++) {
						if (expectedCol1[l].equals((Integer) o1.getService())) {
							logger.log(Logger.Level.TRACE,
									"Found Mary Macy Annual Review for Service Year: " + (Integer) o1.getService());
							foundCol1++;
							break;
						}
					}
				}

				Iterator i2 = col2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Julie OClaire Reviews");
					AnnualReview o2 = (AnnualReview) i2.next();

					for (int l = 0; l < 2; l++) {
						if (expectedCol2[l].equals((Integer) o2.getService())) {
							logger.log(Logger.Level.TRACE,
									"Found Julie OClaire Annual Review for Service Year: " + (Integer) o2.getService());
							foundCol2++;
							break;
						}
					}
				}

				Iterator i3 = col3.iterator();
				while (i3.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Kellie Lee Reviews");
					AnnualReview o3 = (AnnualReview) i3.next();

					for (int l = 0; l < 2; l++) {
						if (expectedCol3[l].equals((Integer) o3.getService())) {
							logger.log(Logger.Level.TRACE,
									"Found Kellie Lee Annual Review for Service Year: " + (Integer) o3.getService());
							foundCol3++;
							break;
						}
					}
				}

				Iterator i4 = col4.iterator();
				while (i4.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Mark Francis Reviews");
					AnnualReview o4 = (AnnualReview) i4.next();

					for (int l = 0; l < 2; l++) {
						if (expectedCol4[l].equals((Integer) o4.getService())) {
							logger.log(Logger.Level.TRACE,
									"Found Mark Francis Annual Review for Service Year: " + (Integer) o4.getService());
							foundCol4++;
							break;
						}
					}
				}

				Iterator i5 = col5.iterator();
				while (i5.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Collection for Katy Hughes Reviews");
					AnnualReview o5 = (AnnualReview) i5.next();

					for (int l = 0; l < 5; l++) {
						if (expectedCol5[l].equals((Integer) o5.getService())) {
							logger.log(Logger.Level.TRACE,
									"Found Katy Hughes Annual Review for Service Year: " + (Integer) o5.getService());
							foundCol5++;
							break;
						}
					}
				}

			}

			if ((foundCol1 != 4) || (foundCol2 != 2) || (foundCol3 != 2) || (foundCol4 != 2) || (foundCol5 != 4)) {

				logger.log(Logger.Level.ERROR, "annotationMappingTest7: Did not get expected results");
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
			throw new Exception("annotationMappingTest7 failed");
	}

	/*
	 * @testName: annotationMappingTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1097; PERSISTENCE:SPEC:1098;
	 * PERSISTENCE:JAVADOC:109; PERSISTENCE:JAVADOC:99; PERSISTENCE:JAVADOC:100;
	 * PERSISTENCE:JAVADOC:101; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:565;
	 * PERSISTENCE:SPEC:568
	 * 
	 * @test_Strategy: RelationShip Mapping Annotations UniDirectional ManyToMany
	 * Relationship
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
	public void annotationMappingTest8() throws Exception {
		boolean pass1 = true;
		boolean pass2 = false;
		int foundInsurance1 = 0;
		int foundInsurance2 = 0;
		int foundInsurance3 = 0;
		final Integer[] expectedResults1 = new Integer[] { 1, 3 };
		final Integer[] expectedResults2 = new Integer[] { 2, 3 };
		final Integer[] expectedResults3 = new Integer[] { 1, 2, 3 };

		try {
			createPeople();
			createInsurance();

			getEntityTransaction().begin();

			pRef[2].getInsurance().add(insRef[0]);
			pRef[2].getInsurance().add(insRef[2]);
			getEntityManager().merge(pRef[2]);

			pRef[12].getInsurance().add(insRef[1]);
			pRef[12].getInsurance().add(insRef[2]);
			getEntityManager().merge(pRef[12]);

			pRef[16].getInsurance().add(insRef[0]);
			pRef[16].getInsurance().add(insRef[1]);
			pRef[16].getInsurance().add(insRef[2]);
			getEntityManager().merge(pRef[16]);

			getEntityManager().flush();

			Person p1 = getEntityManager().find(Person.class, 3);
			Person p2 = getEntityManager().find(Person.class, 13);
			Person p3 = getEntityManager().find(Person.class, 17);

			Collection<Insurance> insCol1 = p1.getInsurance();
			Collection<Insurance> insCol2 = p2.getInsurance();
			Collection<Insurance> insCol3 = p3.getInsurance();

			if ((insCol1.size() != 2) || (insCol2.size() != 2) || (insCol3.size() != 3)) {
				logger.log(Logger.Level.ERROR,
						"annotationMappingTest8: Did not get expected results."
								+ "Expected 2 Insurance Carriers for Shelly McGowan (PK 3) , got: " + insCol1.size()
								+ ", Expected 2 Insurance Carriers for Cheng Fang (PK 13) , got: " + insCol2.size()
								+ ", Expected 3 Insurance Carriers for Nicole Martin (PK 17), got: " + insCol3.size());
				pass1 = false;
			} else if (pass1) {

				Iterator i1 = insCol1.iterator();
				while (i1.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Insurance Carriers for Shelly McGowan");
					Insurance o1 = (Insurance) i1.next();

					for (int l = 0; l < 2; l++) {
						if (expectedResults1[l].equals((Integer) o1.getInsId())) {
							logger.log(Logger.Level.TRACE,
									"Found Insurance Carrier for Shelly McGowan: " + (String) o1.getCarrier());
							foundInsurance1++;
							break;
						}
					}
				}

				Iterator i2 = insCol2.iterator();
				while (i2.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Insurance Carriers for Cheng Fang");
					Insurance o2 = (Insurance) i2.next();

					for (int l = 0; l < 2; l++) {
						if (expectedResults2[l].equals((Integer) o2.getInsId())) {
							logger.log(Logger.Level.TRACE,
									"Found Insurance Carrier for Cheng Fang: " + (String) o2.getCarrier());
							foundInsurance2++;
							break;
						}
					}
				}

				Iterator i3 = insCol3.iterator();
				while (i3.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check Insurance Carriers for Nicole Martin");
					Insurance o3 = (Insurance) i3.next();

					for (int l = 0; l < 3; l++) {
						if (expectedResults3[l].equals((Integer) o3.getInsId())) {
							logger.log(Logger.Level.TRACE,
									"Found Insurance Carrier for Nicole Martin: " + (String) o3.getCarrier());
							foundInsurance3++;
							break;
						}
					}
				}

			}

			if ((foundInsurance1 != 2) || (foundInsurance2 != 2) || (foundInsurance3 != 3)) {
				logger.log(Logger.Level.ERROR, "annotationMappingTest8: Did not get expected results");
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
			throw new Exception("annotationMappingTest8 failed");
	}

	/*
	 * Clean up
	 */

	/*
	 * Business Methods to set up data for Test Cases
	 */
	private void createPeople() throws Exception {
		createPeople(true);
	}

	private void createPeople(boolean persist) throws Exception {
		logger.log(Logger.Level.TRACE, "CreatePeople(persist=" + persist + ")");

		logger.log(Logger.Level.TRACE, "Create 20 People");
		pRef[0] = new Person(1, "Alan", "Frechette");
		pRef[1] = new Person(2, "Arthur", "Frechette");
		pRef[2] = new Person(3, "Shelly", "McGowan");
		pRef[3] = new Person(4, "Robert", "Bissett");
		pRef[4] = new Person(5, "Stephen", "DMilla");
		pRef[5] = new Person(6, "Karen", "Tegan");
		pRef[6] = new Person(7, "Stephen", "Cruise");
		pRef[7] = new Person(8, "Irene", "Caruso");
		pRef[8] = new Person(9, "William", "Keaton");
		pRef[9] = new Person(10, "Kate", "Hudson");
		pRef[10] = new Person(11, "Jonathan", "Smith");
		pRef[11] = new Person(12, "Mary", "Macy");
		pRef[12] = new Person(13, "Cheng", "Fang");
		pRef[13] = new Person(14, "Julie", "OClaire");
		pRef[14] = new Person(15, "Steven", "Rich");
		pRef[15] = new Person(16, "Kellie", "Lee");
		pRef[16] = new Person(17, "Nicole", "Martin");
		pRef[17] = new Person(18, "Mark", "Francis");
		pRef[18] = new Person(19, "Will", "Forrest");
		pRef[19] = new Person(20, "Katy", "Hughes");

		if (persist) {
			logger.log(Logger.Level.TRACE, "Start to persist people ");
			getEntityTransaction().begin();
			for (Person p : pRef) {
				if (p != null) {
					getEntityManager().persist(p);
					logger.log(Logger.Level.TRACE, "Persisting person " + p);
				}
			}
			getEntityTransaction().commit();
		}
	}

	private void createTeams() throws Exception {

		logger.log(Logger.Level.TRACE, "Create 5 Teams");
		tRef[0] = new Team(1, "Engineering");
		tRef[1] = new Team(2, "Marketing");
		tRef[2] = new Team(3, "Sales");
		tRef[3] = new Team(4, "Accounting");
		tRef[4] = new Team(5, "Training");

		logger.log(Logger.Level.TRACE, "Start to persist teams ");
		getEntityTransaction().begin();

		for (Team t : tRef) {
			if (t != null) {
				getEntityManager().persist(t);
				logger.log(Logger.Level.TRACE, "persisted team " + t);
			}
		}
		getEntityTransaction().commit();

	}

	private void createInsurance() throws Exception {
		logger.log(Logger.Level.TRACE, "Create 3 Insurance Carriers");
		insRef[0] = new Insurance(1, "Prudential");
		insRef[1] = new Insurance(2, "Cigna");
		insRef[2] = new Insurance(3, "Sentry");

		logger.log(Logger.Level.TRACE, "Start to persist insurance ");
		getEntityTransaction().begin();
		for (Insurance i : insRef) {
			if (i != null) {

				getEntityManager().persist(i);
				logger.log(Logger.Level.TRACE, "persisted insurance " + i);
			}
		}
		getEntityTransaction().commit();

	}

	private void createProjects() throws Exception {
		createProjects(true);
	}

	private void createProjects(boolean persist) throws Exception {
		logger.log(Logger.Level.TRACE, "Create 5 Projects (persist=" + persist + ")");
		projRef[0] = new Project(123456789L, "Sidewinder", new BigDecimal("20500.0"));
		projRef[1] = new Project(234567890L, "Boa", new BigDecimal("75000.0"));
		projRef[2] = new Project(345678901L, "Asp", new BigDecimal("500000.0"));
		projRef[3] = new Project(456789012L, "King Cobra", new BigDecimal("250000.0"));
		projRef[4] = new Project(567890123L, "Python", new BigDecimal("1000.0"));

		if (persist) {
			logger.log(Logger.Level.TRACE, "Start to persist projects ");
			getEntityTransaction().begin();
			for (Project p : projRef) {
				if (p != null) {

					getEntityManager().persist(p);
					logger.log(Logger.Level.TRACE, "persisted project " + p);
				}
			}
			getEntityTransaction().commit();
		}
	}

	private void createCompany() throws Exception {

		logger.log(Logger.Level.TRACE, "Create 2 Companies");
		cRef[0] = new Company(25501L, "American Gifts");
		cRef[1] = new Company(37560L, "Planet Earth");

		logger.log(Logger.Level.TRACE, "Start to persist companies ");
		getEntityTransaction().begin();
		for (Company c : cRef) {
			if (c != null) {

				getEntityManager().persist(c);
				logger.log(Logger.Level.TRACE, "persisted company " + c);
			}
		}
		getEntityTransaction().commit();
	}

	private void createAddress() throws Exception {
		logger.log(Logger.Level.TRACE, "Create 2 Addresses");
		aRef[0] = new Address("100", "1 Network Drive", "Burlington", "MA", "01803");
		aRef[1] = new Address("200", "4150 Network Drive", "Santa Clara", "CA", "95054");

		logger.log(Logger.Level.TRACE, "Start to persist addresses ");
		getEntityTransaction().begin();
		for (Address a : aRef) {
			if (a != null) {

				getEntityManager().persist(a);
				logger.log(Logger.Level.TRACE, "persisted address " + a);
			}
		}
		getEntityTransaction().commit();

	}

	private void createReviews() throws Exception {
		logger.log(Logger.Level.TRACE, "Create 5 Addresses");
		rRef[0] = new AnnualReview(1, 1);
		rRef[1] = new AnnualReview(2, 2);
		rRef[2] = new AnnualReview(3, 3);
		rRef[3] = new AnnualReview(4, 4);
		rRef[4] = new AnnualReview(5, 5);
		rRef[5] = new AnnualReview(6, 6);
		rRef[6] = new AnnualReview(7, 7);
		rRef[7] = new AnnualReview(8, 8);

		logger.log(Logger.Level.TRACE, "Start to persist annual reviews ");
		getEntityTransaction().begin();
		for (AnnualReview a : rRef) {
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
			getEntityManager().createNativeQuery("DELETE FROM PROJECT_PERSON").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PERSON_INSURANCE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PERSON_ANNUALREVIEW").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PERSON").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM INSURANCE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ANNUALREVIEW").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PROJECT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM TEAM").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM COMPANY").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM ADDRESS").executeUpdate();
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
