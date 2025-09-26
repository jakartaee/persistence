package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilTrimData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilTrimData.class.getName());

	@BeforeEach
	public void setupTrimData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupTrimData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTrimData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupTrimData failed:", e);
		}
	}

}
