package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilCustomerData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilCustomerData.class.getName());

	@BeforeEach
	public void setupCustomerData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupCustomerData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
