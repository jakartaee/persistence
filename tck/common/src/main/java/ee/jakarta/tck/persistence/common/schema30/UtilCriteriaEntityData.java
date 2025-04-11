package ee.jakarta.tck.persistence.common.schema30;

import org.junit.jupiter.api.BeforeEach;

import java.lang.System.Logger;

public abstract class UtilCriteriaEntityData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilCriteriaEntityData.class.getName());

	@BeforeEach
	public void setupCriteriaEntityData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupAliasData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCriteriaEntityData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupCriteriaEntityData failed:", e);
		}
	}

}
