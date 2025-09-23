package ee.jakarta.tck.persistence.core.annotations.access.field;

import java.lang.System.Logger;
import java.util.GregorianCalendar;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.TypedQuery;

public class Client4 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client4.class.getPackageName();
		String pkgName = Client4.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"ee.jakarta.tck.persistence.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_field4.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setup4() throws Exception {
		logger.log(Logger.Level.TRACE, "setup3");
		try {

			super.setup();
			createDeployment();

			removeTestData();
			createTestData4();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: testExtractDateYear
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.1
	 *
	 * @test_Strategy: SELECT EXTRACT(YEAR FROM d.id) FROM DataTypes2 d WHERE d.id =
	 * :id
	 */

	@Test
	public void testExtractDateYear() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager()
					.createQuery("SELECT EXTRACT(YEAR FROM d.id) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long y = q1.getSingleResult().longValue();
			System.out.println("DATETIME -- YEAR: " + y);
			if (y != TD4_YEAR) {
				throw new Exception(
						"EXTRACT(YEAR FROM date) returned wrong value: expeted " + TD4_YEAR + " but got " + y);
			}
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateQuarter
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.2
	 *
	 * @test_Strategy: SELECT EXTRACT(QUARTER FROM d.id) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateQuarter() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager()
					.createQuery("SELECT EXTRACT(QUARTER FROM d.id) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long q = q1.getSingleResult().longValue();
			if (q != TD4_QUARTER) {
				throw new Exception(
						"EXTRACT(QUARTER FROM date) returned wrong value: expeted " + TD4_QUARTER + " but got " + q);
			}
			System.out.println("DATETIME -- QUARTER: " + q);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateMonth
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.3
	 *
	 * @test_Strategy: SELECT EXTRACT(MONTH FROM d.id) FROM DataTypes2 d WHERE d.id
	 * = :id
	 */
	@Test
	public void testExtractDateMonth() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager()
					.createQuery("SELECT EXTRACT(MONTH FROM d.id) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long m = q1.getSingleResult().longValue();
			if (m != TD4_MONTH) {
				throw new Exception(
						"EXTRACT(MONTH FROM date) returned wrong value: expeted " + TD4_MONTH + " but got " + m);
			}
			System.out.println("DATETIME -- MONTH: " + m);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateDay
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.5
	 *
	 * @test_Strategy: SELECT EXTRACT(DAY FROM d.id) FROM DataTypes2 d WHERE d.id =
	 * :id
	 */
	@Test
	public void testExtractDateDay() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager()
					.createQuery("SELECT EXTRACT(DAY FROM d.id) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long d = q1.getSingleResult().longValue();
			if (d != TD4_DAY) {
				throw new Exception(
						"EXTRACT(DAY FROM date) returned wrong value: expeted " + TD4_DAY + " but got " + d);
			}
			System.out.println("DATETIME -- DAY: " + d);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractTimeHour
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.6
	 *
	 * @test_Strategy: SELECT EXTRACT(HOUR FROM d.timeData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractTimeHour() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager().createQuery(
					"SELECT EXTRACT(HOUR FROM d.timeData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long h = q1.getSingleResult().longValue();
			if (h != TD4_HOUR) {
				throw new Exception(
						"EXTRACT(HOUR FROM time) returned wrong value: expeted " + TD4_HOUR + " but got " + h);
			}
			System.out.println("DATETIME -- HOUR: " + h);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractTimeMinute
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.7
	 *
	 * @test_Strategy: SELECT EXTRACT(MINUTE FROM d.timeData) FROM DataTypes2 d
	 * WHERE d.id = :id
	 */
	@Test
	public void testExtractTimeMinute() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager().createQuery(
					"SELECT EXTRACT(MINUTE FROM d.timeData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long m = q1.getSingleResult().longValue();
			if (m != TD4_MINUTE) {
				throw new Exception(
						"EXTRACT(MINUTE FROM time) returned wrong value: expeted " + TD4_MINUTE + " but got " + m);
			}
			System.out.println("DATETIME -- MINUTE: " + m);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractTimeSecond
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.8
	 *
	 * @test_Strategy: SELECT EXTRACT(SECOND FROM d.timeData) FROM DataTypes2 d
	 * WHERE d.id = :id
	 */
	@Test
	public void testExtractTimeSecond() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager().createQuery(
					"SELECT EXTRACT(SECOND FROM d.timeData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long s = q1.getSingleResult().longValue();
			if (s != TD4_SECOND) {
				throw new Exception(
						"EXTRACT(SECOND FROM time) returned wrong value: expeted " + TD4_SECOND + " but got " + s);
			}
			System.out.println("DATETIME -- SECOND: " + s);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateTimeYear
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.1
	 *
	 * @test_Strategy: SELECT EXTRACT(YEAR FROM d.tsData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateTimeYear() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager()
					.createQuery("SELECT EXTRACT(YEAR FROM d.tsData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long y = q1.getSingleResult().longValue();
			System.out.println("DATETIME -- YEAR: " + y);
			if (y != TD4_YEAR) {
				throw new Exception(
						"EXTRACT(YEAR FROM timestamp) returned wrong value: expeted " + TD4_YEAR + " but got " + y);
			}
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateTimeQuarter
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.2
	 *
	 * @test_Strategy: SELECT EXTRACT(QUARTER FROM d.tsData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateTimeQuarter() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager().createQuery(
					"SELECT EXTRACT(QUARTER FROM d.tsData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long q = q1.getSingleResult().longValue();
			if (q != TD4_QUARTER) {
				throw new Exception("EXTRACT(QUARTER FROM timestamp) returned wrong value: expeted " + TD4_QUARTER
						+ " but got " + q);
			}
			System.out.println("DATETIME -- QUARTER: " + q);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateTimeMonth
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.3
	 *
	 * @test_Strategy: SELECT EXTRACT(MONTH FROM d.tsData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateTimeMonth() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager().createQuery(
					"SELECT EXTRACT(MONTH FROM d.tsData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long m = q1.getSingleResult().longValue();
			if (m != TD4_MONTH) {
				throw new Exception(
						"EXTRACT(MONTH FROM timestamp) returned wrong value: expeted " + TD4_MONTH + " but got " + m);
			}
			System.out.println("DATETIME -- MONTH: " + m);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateTimeDay
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.5
	 *
	 * @test_Strategy: SELECT EXTRACT(DAY FROM d.tsData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateTimeDay() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager()
					.createQuery("SELECT EXTRACT(DAY FROM d.tsData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long d = q1.getSingleResult().longValue();
			if (d != TD4_DAY) {
				throw new Exception(
						"EXTRACT(DAY FROM timestamp) returned wrong value: expeted " + TD4_DAY + " but got " + d);
			}
			System.out.println("DATETIME -- DAY: " + d);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateTimeHour
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.6
	 *
	 * @test_Strategy: SELECT EXTRACT(HOUR FROM d.tsData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateTimeHour() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager()
					.createQuery("SELECT EXTRACT(HOUR FROM d.tsData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long h = q1.getSingleResult().longValue();
			if (h != TD4_HOUR) {
				throw new Exception(
						"EXTRACT(HOUR FROM timestamp) returned wrong value: expeted " + TD4_HOUR + " but got " + h);
			}
			System.out.println("DATETIME -- HOUR: " + h);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateTimeMinute
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.7
	 *
	 * @test_Strategy: SELECT EXTRACT(MINUTE FROM d.tsData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateTimeMinute() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager().createQuery(
					"SELECT EXTRACT(MINUTE FROM d.tsData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long m = q1.getSingleResult().longValue();
			if (m != TD4_MINUTE) {
				throw new Exception(
						"EXTRACT(MINUTE FROM timestamp) returned wrong value: expeted " + TD4_MINUTE + " but got " + m);
			}
			System.out.println("DATETIME -- MINUTE: " + m);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	/*
	 * @testName: testExtractDateTimeSecond
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2519.8
	 *
	 * @test_Strategy: SELECT EXTRACT(SECOND FROM d.tsData) FROM DataTypes2 d WHERE
	 * d.id = :id
	 */
	@Test
	public void testExtractDateTimeSecond() throws Exception {
		try {
			TypedQuery<Number> q1 = getEntityManager().createQuery(
					"SELECT EXTRACT(SECOND FROM d.tsData) FROM DataTypes2 d WHERE d.id = :id", Number.class);
			q1.setParameter("id", t4Date());
			long s = q1.getSingleResult().longValue();
			if (s != TD4_SECOND) {
				throw new Exception(
						"EXTRACT(SECOND FROM timestamp) returned wrong value: expeted " + TD4_SECOND + " but got " + s);
			}
			System.out.println("DATETIME -- SECOND: " + s);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	private final int TD4_YEAR = 1989;
	private final int TD4_QUARTER = 4;
	private final int TD4_MONTH = 11;
	private final int TD4_DAY = 17;
	private final int TD4_HOUR = 11;
	private final int TD4_MINUTE = 23;
	private final int TD4_SECOND = 36;

	private java.sql.Date t4Date() {
		final GregorianCalendar cal = new GregorianCalendar(TD4_YEAR, TD4_MONTH - 1, TD4_DAY, TD4_HOUR, TD4_MINUTE,
				TD4_SECOND);
		final java.sql.Timestamp ts = new java.sql.Timestamp(cal.getTimeInMillis());
		return java.sql.Date.valueOf(ts.toLocalDateTime().toLocalDate());
	}

	public void createTestData4() {
		logger.log(Logger.Level.TRACE, "createTestData4");
		final GregorianCalendar cal = new GregorianCalendar(TD4_YEAR, TD4_MONTH - 1, TD4_DAY, TD4_HOUR, TD4_MINUTE,
				TD4_SECOND);
		final java.sql.Timestamp ts = new java.sql.Timestamp(cal.getTimeInMillis());
		final java.sql.Date dt = java.sql.Date.valueOf(ts.toLocalDateTime().toLocalDate());
		final java.sql.Time tm = java.sql.Time.valueOf(ts.toLocalDateTime().toLocalTime());

		try {
			getEntityTransaction().begin();
			DataTypes2 dataTypes2 = new DataTypes2(dt, tm, ts);
			getEntityManager().persist(dataTypes2);
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

	}

}
