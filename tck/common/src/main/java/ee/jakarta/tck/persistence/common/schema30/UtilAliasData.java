package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilAliasData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilAliasData.class.getName());

	@BeforeEach
	public void setupAliasData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupAliasData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createAliasData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupAliasData failed:", e);
		}
	}

}
