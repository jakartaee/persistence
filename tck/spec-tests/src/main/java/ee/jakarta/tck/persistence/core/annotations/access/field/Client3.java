package ee.jakarta.tck.persistence.core.annotations.access.field;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client3 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = Client3.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"ee.jakarta.tck.persistence.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_field3.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setup3() throws Exception {
		logger.log(Logger.Level.TRACE, "setup3");
		try {

			super.setup();
			createDeployment();
			removeTestData();
			createTestData3();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	public void createTestData3() {
		logger.log(Logger.Level.TRACE, "createTestData3");

		try {
			getEntityTransaction().begin();
			d1 = new DataTypes(1, (byte) 5);

			getEntityManager().persist(d1);
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

	/*
	 * @testName: transientTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1327.2
	 * 
	 * @test_Strategy: when transient is specified, verify data isn't persisted
	 */
	@Test
	public void transientTest() throws Exception {

		boolean pass = false;
		byte newByte = (byte) 111;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if ((d1.getTransient() == (byte) 0)) {
					logger.log(Logger.Level.TRACE, "First find returned expected result:" + d1.getTransient());
					d1.setTransient(newByte);

					getEntityManager().merge(d1);
					getEntityManager().flush();
					clearCache();
					d1 = null;
					d1 = getEntityManager().find(DataTypes.class, 1);

					if (d1.getTransient() == (byte) 0) {
						pass = true;
						logger.log(Logger.Level.TRACE, "Second find returned expected value:" + d1.getTransient());
					} else {
						logger.log(Logger.Level.ERROR, "Second find expected:0, actual:" + d1.getTransient());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected first find to return:0, actual:" + d1.getTransient());
				}
				getEntityTransaction().commit();
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

}
