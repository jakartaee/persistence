package ee.jakarta.tck.persistence.common.schema30;

import java.lang.System.Logger;

import org.junit.jupiter.api.BeforeEach;

public abstract class UtilDepartmentEmployeeData extends Util {

	private static final Logger logger = (Logger) System.getLogger(UtilDepartmentEmployeeData.class.getName());

	@BeforeEach
	public void setupDepartmentEmployeeData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupDepartmentEmployeeData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createDepartmentEmployeeData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("setupDepartmentEmployeeData failed:", e);
		}
	}

}
