package ee.jakarta.tck.persistence.core.annotations.access.property;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client2 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"ee.jakarta.tck.persistence.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_property2.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup2() throws Exception {
		logger.log(Logger.Level.TRACE, "setup2");
		try {

			super.setup();
			createDeployment();

			removeTestData();
			createTestData2();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: transientTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1327.9
	 * 
	 * @test_Strategy: when transient is specified, verify data isn't persisted
	 */
	@Test
	public void transientTest() throws Exception {

		boolean pass = false;
		final String newString = "NEW_VALUE";
		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);
			logger.log(Logger.Level.INFO, "d1.toString():" + d1.toString());

			if (null != d1) {
				if (d1.getShouldNotPersist() == null) {
					logger.log(Logger.Level.TRACE, "Int value after find=" + d1.getShouldNotPersist());
					d1.setShouldNotPersist(newString);
					logger.log(Logger.Level.TRACE, "Int value after set=" + d1.getShouldNotPersist());
					getEntityManager().merge(d1);
					getEntityManager().flush();
					clearCache();
					d1 = null;
					d1 = getEntityManager().find(DataTypes.class, 1);

					if (d1.getShouldNotPersist() == null) {
						pass = true;
						logger.log(Logger.Level.TRACE, "Received expected null value");
					} else {
						logger.log(Logger.Level.ERROR, "Expected:null, actual:" + d1.getShouldNotPersist());
					}
					getEntityTransaction().commit();

				} else {
					logger.log(Logger.Level.ERROR, "getShouldNotPersist() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "find returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("transientTest failed");
	}

	public void createTestData2() {
		logger.log(Logger.Level.TRACE, "createTestData2");

		try {
			getEntityTransaction().begin();

			d1 = new DataTypes(1, "INITIAL_VALUE");

			getEntityManager().persist(d1);

			getEntityManager().flush();

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

	}

}
