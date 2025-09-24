package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilOrderData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilOrderData.class.getName());

	@BeforeEach
	public void setupOrderData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupOrderData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createProductData();
			createOrderData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
