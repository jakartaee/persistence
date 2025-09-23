package ee.jakarta.tck.persistence.core.annotations.access.property;

import java.lang.System.Logger;

import org.junit.jupiter.api.AfterEach;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	protected DataTypes d1;

	protected DataTypes2 d2;

	final protected java.util.Date dateId = getPKDate(2006, 04, 15);

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

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
			getEntityManager().createNativeQuery("Delete from DATATYPES").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DATATYPES2").executeUpdate();
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
