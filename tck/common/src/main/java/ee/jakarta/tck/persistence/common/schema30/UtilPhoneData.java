package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilPhoneData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilPhoneData.class.getName());

	@BeforeEach
	public void setupPhoneData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupPhoneData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createPhoneData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
