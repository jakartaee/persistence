package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilCustAliasProductData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilCustAliasProductData.class.getName());

	@BeforeEach
	public void setupCustAliasProductData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupCustAliasProductData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createProductData();
			createAliasData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupCustAliasProductData failed:", e);
		}
	}

}
