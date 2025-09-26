package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilProductData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilProductData.class.getName());

	@BeforeEach
	public void setupProductData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupProductData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createProductData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupProductData failed:", e);
		}
	}

}
