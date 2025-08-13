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

package ee.jakarta.tck.persistence.core.override.embeddable;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final Integer BOOKSTORE_ID = 12345;

	private static final String PUBLISHER_NAME = "Sun";

	private static final String PUBLISHER_LOCATION = "Santa Clara";

	private static final int COMPLAINT_NUMBER = 24680;

	private static final String APPLICANT_NAME = "Sunny";

	private static final String APPLICANT_ADDRESS = "Menlo Park";

	private static final Integer COMPLAINT_ID = 9;

	private static final Integer MOVIETICKET_ID = 6;

	private static final String FILM_CODE = "007";

	private static final String FILM_NAME = "Network is Computer";

	private static final Integer BOOK_ID = 5;

	private static final String PUBLISHER1_NAME = "Penguin";

	private static final String PUBLISHER1_STATE = "California";

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { ORM_XML };
		String[] classes = { pkgName + "Applicant", pkgName + "Book", pkgName + "BookStore", pkgName + "Complaint",
				pkgName + "Film", pkgName + "MovieTicket", pkgName + "Publisher", pkgName + "Publisher1" };
		return createDeploymentJar("jpa_core_override_embeddable.jar", pkgNameWithoutSuffix, classes, xmlFiles);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception:test failed ", e);
		}
	}

	/*
	 * @testName: testOverrideTransient
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
	 * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
	 * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
	 * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
	 * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
	 * PERSISTENCE:SPEC:1127;PERSISTENCE:SPEC:1130;
	 * 
	 * @test_Strategy: A field in an entity which is declared as Basic is overriden
	 * in orm.xml as Transient.
	 */
	@Test
	public void testOverrideTransient() throws Exception {

		getEntityTransaction().begin();
		Publisher publisher = new Publisher();
		publisher.setName(PUBLISHER_NAME);
		publisher.setLocation(PUBLISHER_LOCATION);
		BookStore bookstore = new BookStore();
		bookstore.setId(BOOKSTORE_ID);
		bookstore.setPublisher(publisher);
		getEntityManager().persist(bookstore);
		getEntityManager().flush();
		try {
			BookStore retrieveBook = getEntityManager().find(BookStore.class, BOOKSTORE_ID);
			retrieveBook.setPublisher(new Publisher());
			getEntityManager().refresh(retrieveBook);
			Publisher retrievePublisher = retrieveBook.getPublisher();
			if (retrievePublisher.getName().equals(PUBLISHER_NAME)) {
				if (retrievePublisher.getLocation() == null) {
					logger.log(Logger.Level.TRACE, "Test Passed");
				} else {
					throw new Exception("The Location fields was expected to be empty, "
							+ "expected Length - null, actual - " + "" + retrievePublisher.getLocation());
				}
			} else {
				throw new Exception("Incorrect BookStore object obtained from the" + " database");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOverrideTransient" + e);
		} finally {
			getEntityManager().remove(bookstore);
			getEntityTransaction().commit();

		}
	}

	/*
	 * @testName: testOverrideEmbeddable
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
	 * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
	 * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
	 * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
	 * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
	 * PERSISTENCE:SPEC:1127; PERSISTENCE:SPEC:1130;
	 * 
	 * @test_Strategy: Applicant class is declared as "Embeddable" in orm.xml
	 * without using annotation and an entity named Complaint uses Applicant. The
	 * following test test applies that by reading from the orm.xml.
	 */
	@Test
	public void testOverrideEmbeddable() throws Exception {

		getEntityTransaction().begin();
		Applicant applicant = new Applicant();
		applicant.setName(APPLICANT_NAME);
		applicant.setAddress(APPLICANT_ADDRESS);
		Complaint complaint = new Complaint();
		complaint.setId(COMPLAINT_ID);
		complaint.setComplaintNumber(COMPLAINT_NUMBER);
		complaint.setApplicant(applicant);
		getEntityManager().persist(complaint);
		getEntityManager().flush();
		try {
			Complaint retrieveComplaint = getEntityManager().find(Complaint.class, COMPLAINT_ID);
			Applicant retrieveApplicant = retrieveComplaint.getApplicant();
			if (retrieveComplaint.getComplaintNumber() == COMPLAINT_NUMBER
					&& retrieveApplicant.getName().equals(APPLICANT_NAME)
					&& retrieveApplicant.getAddress().equals(APPLICANT_ADDRESS)) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("Expected Complaint Number COMPLAINT_NUMBER to be"
						+ " retrieved; complaint in DB - " + retrieveComplaint.getComplaintNumber());
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOverrideEmbeddable" + e);
		} finally {
			getEntityManager().remove(complaint);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testOverrideEmbedded
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
	 * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
	 * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
	 * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
	 * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
	 * PERSISTENCE:SPEC:1127; PERSISTENCE:SPEC:1130;
	 * 
	 * @test_Strategy: Film is an Embeddable class. MovieTicket is an entity has
	 * Film embedded in it by defining it as "embedded" in orm.xml instead of using
	 * annotation. The following test checks for the above.
	 * 
	 */
	@Test
	public void testOverrideEmbedded() throws Exception {

		getEntityTransaction().begin();
		Film film = new Film();
		film.setFilmCode(FILM_CODE);
		film.setFilmName(FILM_NAME);
		MovieTicket ticket = new MovieTicket();
		ticket.setId(MOVIETICKET_ID);
		ticket.setFilm(film);
		getEntityManager().persist(ticket);
		getEntityManager().flush();
		try {
			MovieTicket retrieveTicket = getEntityManager().find(MovieTicket.class, MOVIETICKET_ID);
			Film retrieveFilm = retrieveTicket.getFilm();
			if (retrieveFilm.getFilmName().equals(FILM_NAME) && retrieveFilm.getFilmCode().equals(FILM_CODE)) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("Expected MovieTicket(FILM_NAME)" + " to be retrieved; film in DB - "
						+ retrieveFilm.getFilmName());
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOverrideEmbedded" + e);
		} finally {
			getEntityManager().remove(ticket);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testMetadataCompleteness
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
	 * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
	 * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
	 * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
	 * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
	 * PERSISTENCE:SPEC:1127; PERSISTENCE:SPEC:1130;
	 * 
	 * @test_Strategy: Book is an entity and has an embedded field publisher1 which
	 * is an object of an embeddable class Publisher1. Publisher1 has two fields
	 * name that is declared Transient and state that is declared of length 2. The
	 * orm.xml has metadata-complete=true. The following test checks for the
	 * metadata completeness.
	 * 
	 */
	@Test
	public void testMetadataCompleteness() throws Exception {

		getEntityTransaction().begin();
		Book book = new Book();
		Publisher1 publisher1 = new Publisher1();
		book.setId(BOOK_ID);
		publisher1.setName(PUBLISHER1_NAME);
		publisher1.setState(PUBLISHER1_STATE);
		book.setPublisher1(publisher1);
		getEntityManager().persist(book);
		getEntityManager().flush();
		try {
			Book retrieveBook = getEntityManager().find(Book.class, BOOK_ID);
			/*
			 * setting Publisher in order to refresh the entity after it has been overriden
			 */
			retrieveBook.setPublisher1(new Publisher1());
			getEntityManager().refresh(retrieveBook);
			Publisher1 retrievePublisher1 = retrieveBook.getPublisher1();
			if (retrievePublisher1.getName().equals(PUBLISHER1_NAME)
					&& retrievePublisher1.getState().equals(PUBLISHER1_STATE)) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("Publisher1's name and state were not persisted "
						+ "as expected -- metadata-complete=true is not" + " read from orm.xml");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testMetadataCompleteness" + e);
		} finally {
			getEntityManager().remove(book);
			getEntityTransaction().commit();
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM BOOK").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM COMPLAINT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM MOVIETICKET").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BOOKSTORE").executeUpdate();

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
