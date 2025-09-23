package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilAliasOnlyData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilAliasOnlyData.class.getName());

	@BeforeEach
	public void setupAliasOnlyData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupAliasOnlyData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createAliasOnlyData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
