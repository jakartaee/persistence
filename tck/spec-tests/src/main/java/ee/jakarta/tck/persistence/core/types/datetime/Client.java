/*
 * Copyright (c) 2017, 2025 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.core.types.datetime;

import java.lang.System.Logger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class Client extends PMClientBase {

    private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

    private static final long serialVersionUID = 22L;

    public Client() {
    }

    public JavaArchive createDeployment() throws Exception {

        String pkgNameWithoutSuffix = Client.class.getPackageName();
        String pkgName = pkgNameWithoutSuffix + ".";
        String[] classes = { pkgName + "DateTimeEntity", pkgName + "DummyEntity" };
        return createDeploymentJar("jpa_datetime.jar", pkgNameWithoutSuffix, (String[]) classes);

    }

    @Override
    @BeforeEach
    public void setup() throws Exception {
        logger.log(Logger.Level.INFO, "Setup: Jakarta Persistence Java 8 date and time types test");
        try {
            super.setup();
            createDeployment();
            Properties props = getPersistenceUnitProperties();
            props.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
            props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
            displayProperties(props);
            logger.log(Logger.Level.INFO, " - executing persistence schema generation");
            Persistence.generateSchema(getPersistenceUnitName(), props);
            clearEMAndEMF();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "caught Exception: ", e);
            throw new Exception(" ! Jakarta Persistence Java 8 date and time types test setup failed", e);
        }
        verifySchema();
    }

    @Override
    @AfterEach
    public void cleanup() throws Exception {
        try {
            logger.log(Logger.Level.INFO, "Cleanup: Jakarta Persistence 2.2 Java 8 date and time types test");
            Properties props = getPersistenceUnitProperties();
            props.put("jakarta.persistence.schema-generation.database.action", "drop");
            displayProperties(props);
            logger.log(Logger.Level.INFO, " - executing persistence schema cleanup");
            Persistence.generateSchema(getPersistenceUnitName(), props);
            closeEMAndEMF();
            super.cleanup();
        } finally {
            removeTestJarFromCP();
        }
    }

    /** Default Instant constant. */
    private static final Instant INSTANT_DEF = Instant.ofEpochSecond(0);

    /** Instant constant. */
    private static final Instant INSTANT = Instant.now();

    /** Default LocalDate constant. */
    private static final LocalDate LOCAL_DATE_DEF = LocalDate.of(1970, 1, 1);

    /** LocalDate constant. */
    private static final LocalDate LOCAL_DATE = LocalDate.now();

    /** Default LocalTime constant. */
    private static final LocalTime LOCAL_TIME_DEF = LocalTime.of(0, 0, 0);

    /** LocalTime constant. */
    private static final LocalTime LOCAL_TIME = LocalTime.now().withNano(0);

    /** Default LocalDateTime constant. */
    private static final LocalDateTime LOCAL_DATE_TIME_DEF = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    /** LocalDateTime constant. */
    private static final LocalDateTime LOCAL_DATE_TIME = initLocalDateTime();

    /** Default OffsetTime constant. */
    private static final OffsetTime OFFSET_TIME_DEF = OffsetTime.of(LOCAL_TIME_DEF, ZoneOffset.ofHours(1));

    /** OffsetTime constant. */
    private static final OffsetTime OFFSET_TIME = OffsetTime.of(LOCAL_TIME, ZoneOffset.ofHours(0));

    /** Default OffsetDateTime constant. */
    private static final OffsetDateTime OFFSET_DATE_TIME_DEF = OffsetDateTime.of(LOCAL_DATE_TIME_DEF,
            ZoneOffset.ofHours(1));

    /** OffsetDateTime constant. */
    private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(LOCAL_DATE_TIME, ZoneOffset.ofHours(0));

    /** Default Year constant. */
    private static final Year YEAR_DEF = Year.of(0);

    /** Year constant. */
    private static final Year YEAR = Year.now();

    private static final DateTimeEntity[] entities = {
            new DateTimeEntity(1L, INSTANT_DEF, LOCAL_DATE, LOCAL_TIME_DEF, LOCAL_DATE_TIME_DEF, OFFSET_TIME_DEF,
                    OFFSET_DATE_TIME_DEF, YEAR_DEF),
            new DateTimeEntity(2L, INSTANT_DEF, LOCAL_DATE_DEF, LOCAL_TIME, LOCAL_DATE_TIME_DEF, OFFSET_TIME_DEF,
                    OFFSET_DATE_TIME_DEF, YEAR_DEF),
            new DateTimeEntity(3L, INSTANT_DEF, LOCAL_DATE_DEF, LOCAL_TIME_DEF, LOCAL_DATE_TIME, OFFSET_TIME_DEF,
                    OFFSET_DATE_TIME_DEF, YEAR_DEF),
            new DateTimeEntity(4L, INSTANT_DEF, LOCAL_DATE_DEF, LOCAL_TIME_DEF, LOCAL_DATE_TIME_DEF, OFFSET_TIME,
                    OFFSET_DATE_TIME_DEF, YEAR_DEF),
            new DateTimeEntity(5L, INSTANT_DEF, LOCAL_DATE_DEF, LOCAL_TIME_DEF, LOCAL_DATE_TIME_DEF, OFFSET_TIME_DEF,
                    OFFSET_DATE_TIME, YEAR_DEF),
            new DateTimeEntity(6L, INSTANT, LOCAL_DATE_DEF, LOCAL_TIME_DEF, LOCAL_DATE_TIME_DEF, OFFSET_TIME_DEF,
                    OFFSET_DATE_TIME_DEF, YEAR_DEF),
            new DateTimeEntity(7L, INSTANT_DEF, LOCAL_DATE_DEF, LOCAL_TIME_DEF, LOCAL_DATE_TIME_DEF, OFFSET_TIME_DEF,
                    OFFSET_DATE_TIME_DEF, YEAR)};

    // Databases precision is usually not nanoseconds. Truncate to miliseconds.
    private static LocalDateTime initLocalDateTime() {
        final LocalDateTime value = LocalDateTime.now();
        return value.withNano((value.getNano() / 1000000) * 1000000);
    }

    /*
     * @testName: dateTimeTest
     *
     * @assertion_ids: PERSISTENCE:JAVADOC:320; PERSISTENCE:SPEC:2118.19;
     * PERSISTENCE:SPEC:2118.5;
     *
     * @test_Strategy: Test Jakarta Persistence date and time types: java.time.Instant,
     * java.time.LocalDate, java.time.LocalTime, java.time.LocalDateTime, java.time.OffsetTime,
     * java.time.OffsetDateTime and java.time.Year
     *
     * @throws com.sun.ts.lib.harness.EETest.Exception when test failed
     */
    @Test
    public void dateTimeTest() throws Exception {
        logger.log(Logger.Level.INFO, "Test: Jakarta Persistence Java 8 date and time types");
        verifySchema();
        boolean createResult = createEntities();
        boolean allFindResult = true;
        boolean[] findResults = new boolean[entities.length];
        for (int i = 0; i < entities.length; i++) {
            findResults[i] = findEntityById(entities[i]);
        }
        boolean localDateResult = queryEntities("DateTimeEntity.findByLocalDate", "date", LOCAL_DATE, entities[0]);
        boolean localTimeResult = queryEntities("DateTimeEntity.findByLocalTime", "time", LOCAL_TIME, entities[1]);
        boolean localDateTimeResult = queryEntities("DateTimeEntity.findByLocalDateTime", "dateTime", LOCAL_DATE_TIME,
                entities[2]);
        boolean offsetTimeResult = queryEntities("DateTimeEntity.findByOffsetTime", "time", OFFSET_TIME, entities[3]);
        boolean offsetDateTimeResult = queryEntities("DateTimeEntity.findByOffsetDateTime", "dateTime",
                OFFSET_DATE_TIME, entities[4]);
        boolean instantResult = queryEntities("DateTimeEntity.findByInstant", "instant", INSTANT, entities[5]);
        boolean yearResult = queryEntities("DateTimeEntity.findByYear", "year", YEAR, entities[6]);
        boolean localDateRangeResult = queryEntitiesRange("DateTimeEntity.findLocalDateRange", "min",
                LOCAL_DATE.minus(10, ChronoUnit.DAYS), "max", LOCAL_DATE.plus(10, ChronoUnit.DAYS), entities[0]);
        boolean localTimeRangeResult = queryEntitiesRange("DateTimeEntity.findLocalTimeRange", "min",
                LOCAL_TIME.minus(10, ChronoUnit.MINUTES), "max", LOCAL_TIME.plus(10, ChronoUnit.MINUTES), entities[1]);
        boolean localDateTimeRangeResult = queryEntitiesRange("DateTimeEntity.findLocalDateTimeRange", "min",
                LOCAL_DATE_TIME.minus(10, ChronoUnit.DAYS), "max", LOCAL_DATE_TIME.plus(10, ChronoUnit.DAYS),
                entities[2]);
        boolean offsetTimeRangeResult = queryEntitiesRange("DateTimeEntity.findOffsetTimeRange", "min",
                OFFSET_TIME.minus(10, ChronoUnit.MINUTES), "max", OFFSET_TIME.plus(10, ChronoUnit.MINUTES),
                entities[3]);
        boolean offsetDateTimeRangeResult = queryEntitiesRange("DateTimeEntity.findOffsetDateTimeRange", "min",
                OFFSET_DATE_TIME.minus(10, ChronoUnit.DAYS).minus(10, ChronoUnit.MINUTES), "max",
                OFFSET_DATE_TIME.plus(10, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES), entities[4]);
        boolean instantRangeResult = queryEntitiesRange("DateTimeEntity.findByInstantRange", "min",
                INSTANT.minus(10, ChronoUnit.DAYS).minus(10, ChronoUnit.MINUTES), "max",
                INSTANT.plus(10, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES), entities[5]);
        boolean yearRangeResult = queryEntitiesRange("DateTimeEntity.findByYearRange", "min",
                YEAR.minus(10, ChronoUnit.YEARS), "max",
                YEAR.plus(10, ChronoUnit.YEARS), entities[6]);
        logger.log(Logger.Level.INFO,
                "--------------------------------------------------------------------------------");
        logger.log(Logger.Level.INFO, " - Jakarta Persistence Java 8 date and time types test results:");
        logTestResult("Entities creation", createResult);
        for (int i = 0; i < entities.length; i++) {
            logTestResult("Find by ID=" + entities[i].getId().toString(), findResults[i]);
            allFindResult = allFindResult && findResults[i];
        }
        logTestResult("Query DateTimeEntity.findByLocalDate", localDateResult);
        logTestResult("Query DateTimeEntity.findByLocalTime", localTimeResult);
        logTestResult("Query DateTimeEntity.findByLocalDateTime", localDateTimeResult);
        logTestResult("Query DateTimeEntity.findByOffsetTime", offsetTimeResult);
        logTestResult("Query DateTimeEntity.findByOffsetDateTime", offsetDateTimeResult);
        logTestResult("Query DateTimeEntity.findByInstant", instantResult);
        logTestResult("Query DateTimeEntity.findByYear", yearResult);
        logTestResult("Query DateTimeEntity.findLocalDateRange", localDateRangeResult);
        logTestResult("Query DateTimeEntity.findLocalTimeRange", localTimeRangeResult);
        logTestResult("Query DateTimeEntity.findLocalDateTimeRange", localDateTimeRangeResult);
        logTestResult("Query DateTimeEntity.findOffsetTimeRange", offsetTimeRangeResult);
        logTestResult("Query DateTimeEntity.findOffsetDateTimeRange", offsetDateTimeRangeResult);
        logTestResult("Query DateTimeEntity.findByInstantRange", instantRangeResult);
        logTestResult("Query DateTimeEntity.findByYearRange", yearRangeResult);
        logger.log(Logger.Level.INFO,
                "--------------------------------------------------------------------------------");
        if (!(createResult && allFindResult && localDateResult && localTimeResult && localDateTimeResult
                && offsetTimeResult && offsetDateTimeResult && instantResult && yearResult
                && localDateRangeResult && localTimeRangeResult && localDateTimeRangeResult
                && offsetTimeRangeResult && offsetDateTimeRangeResult && instantRangeResult
                && yearRangeResult)) {
            throw new Exception("dateTimeTest (Jakarta Persistence Java 8 date and time types test) failed");
        }
    }

    /**
     * Create entities with Java 8 date and time types.
     *
     * @return value of {@code true} when entities were created successfully or
     *         {@code false} otherwise
     */
    private boolean createEntities() {
        logger.log(Logger.Level.INFO, " - creating test entities");
        try {
            getEntityTransaction().begin();
            for (DateTimeEntity e : entities) {
                getEntityManager().persist(e);
            }
            getEntityTransaction().commit();
            return true;
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "caught Exception: ", e);
            return false;
        } finally {
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
        }
    }

    /**
     * Find entity by primary key.
     *
     * @param expected entity to find and compare to (must contain proper ID)
     * @return value of {@code true} when entity was found and matches
     *         {@code expected} argument or {@code false} otherwise
     */
    private boolean findEntityById(DateTimeEntity expected) {
        logger.log(Logger.Level.INFO, " - executing find by ID=" + expected.getId().toString());
        try {
            DateTimeEntity result = getEntityManager().find(DateTimeEntity.class, expected.getId());
            if (result == null) {
                logger.log(Logger.Level.ERROR, "no result returned for " + expected.toString());
                return false;
            }
            if (!expected.equals(result)) {
                logger.log(Logger.Level.ERROR, "returned entity does not match expected");
                logger.log(Logger.Level.ERROR, " - expected: " + expected.toString());
                logger.log(Logger.Level.ERROR, " - returned: " + result.toString());
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "caught Exception: ", e);
            return false;
        }
    }

    /**
     * Find entity by Java 8 date and time type value.
     *
     * @param queryName  named query identifier (named queries are defined in
     *                   DateTimeEntity class)
     * @param paramName  name of query parameter to set
     * @param paramValue query parameter value to set
     * @param expected   expected returned entity
     * @return value of {@code true} when exactly one entity was found and matches
     *         {@code expected} argument or {@code false} otherwise
     */
    private boolean queryEntities(String queryName, String paramName, Object paramValue, DateTimeEntity expected) {
        logger.log(Logger.Level.INFO,
                " - executing query " + queryName + ": " + paramName + "=" + paramValue.toString());
        try {
            TypedQuery<DateTimeEntity> query = getEntityManager().createNamedQuery(queryName, DateTimeEntity.class);
            query.setParameter(paramName, paramValue);
            List<DateTimeEntity> result = query.getResultList();
            if (result == null || result.isEmpty()) {
                logger.log(Logger.Level.ERROR, "no result returned for query " + queryName + " with " + paramName + "="
                        + paramValue.toString());
                return false;
            }
            if (result.size() > 1) {
                logger.log(Logger.Level.ERROR, "too many results (" + Integer.toString(result.size())
                        + ") returned for query " + queryName + " with " + paramName + "=" + paramValue.toString());
                return false;
            }
            DateTimeEntity returned = result.get(0);
            if (!expected.equals(returned)) {
                logger.log(Logger.Level.ERROR, "returned entity does not match expected");
                logger.log(Logger.Level.ERROR, " - expected: " + expected.toString());
                logger.log(Logger.Level.ERROR, " - returned: " + (returned != null ? returned.toString() : "null"));
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "caught Exception: ", e);
            return false;
        }
    }

    /**
     * Find entity by Java 8 date and time type value.
     *
     * @param queryName  named query identifier (named queries are defined in
     *                   DateTimeEntity class)
     * @param minName	 name of query parameter (for minimal value) to set
     * @param minValue	 query parameter for a minimal value to set
     * @param maxName	 name of query parameter (for maximal value) to set
     * @param maxValue	 query parameter for a maximal value to set
     * @param expected   expected returned entity
     * @return value of {@code true} when exactly one entity was found and matches
     *         {@code expected} argument or {@code false} otherwise
     */
    private boolean queryEntitiesRange(String queryName, String minName, Object minValue, String maxName,
                                       Object maxValue, DateTimeEntity expected) {
        logger.log(Logger.Level.INFO, " - executing query " + queryName + ": " + minName + "=" + minValue.toString()
                + ", " + maxName + "=" + maxValue.toString());
        try {
            TypedQuery<DateTimeEntity> query = getEntityManager().createNamedQuery(queryName, DateTimeEntity.class);
            query.setParameter(minName, minValue);
            query.setParameter(maxName, maxValue);
            List<DateTimeEntity> result = query.getResultList();
            if (result == null || result.isEmpty()) {
                logger.log(Logger.Level.ERROR, "no result returned for query " + queryName + " with " + minName + "="
                        + minValue.toString() + ", " + maxName + "=" + maxValue.toString());
                return false;
            }
            if (result.size() > 1) {
                logger.log(Logger.Level.ERROR,
                        "too many results (" + Integer.toString(result.size()) + ") returned for query " + queryName
                                + " with " + minName + "=" + minValue.toString() + ", " + maxName + "="
                                + maxValue.toString());
                return false;
            }
            DateTimeEntity returned = result.get(0);
            if (!expected.equals(returned)) {
                logger.log(Logger.Level.ERROR, "returned entity does not match expected");
                logger.log(Logger.Level.ERROR, " - expected: " + expected.toString());
                logger.log(Logger.Level.ERROR, " - returned: " + (returned != null ? returned.toString() : "null"));
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "caught Exception: ", e);
            return false;
        }
    }

    /**
     * Verify generated schema. Java 8 date and time support is not finished so
     * using dummy entity containing only ID to verify that schema exists.
     *
     * @return verification result
     */
    private void verifySchema() throws Exception {
        logger.log(Logger.Level.INFO, " - executing persistence schema check");
        try {
            getEntityTransaction().begin();
            DummyEntity e = new DummyEntity();
            getEntityManager().persist(e);
            getEntityTransaction().commit();
            logger.log(Logger.Level.TRACE, "   - stored " + e.toString());
            DummyEntity result = getEntityManager().find(DummyEntity.class, e.getId());
            if (result == null) {
                logger.log(Logger.Level.ERROR, "   ! no entity was found");
                throw new Exception("dateTimeTest: Schema verification failed");
            }
            getEntityTransaction().begin();
            result = getEntityManager().merge(result);
            getEntityManager().remove(result);
            getEntityTransaction().commit();
        } catch (Exception e) {
            logger.log(Logger.Level.ERROR, "caught Exception: ", e);
            if (getEntityTransaction().isActive()) {
                getEntityTransaction().rollback();
            }
            throw new Exception("dateTimeTest: Schema verification failed");
        }
    }

    @Override
    public EntityTransaction getEntityTransaction() {
        if (isStandAloneMode() && !super.getEntityTransaction().isActive()) {
            EntityTransaction et = getEntityManager().getTransaction();
            setEntityTransaction(et);
        }
        return super.getEntityTransaction();
    }

    /**
     * Log test result message.
     *
     * @param name   test name
     * @param result test result
     */
    private void logTestResult(String name, boolean result) {
        StringBuilder sb = new StringBuilder();
        sb.append("   ").append(result ? '+' : '-').append(' ');
        sb.append(name).append(": ");
        sb.append(result ? "PASSED" : "FAILED");
        logger.log(Logger.Level.INFO, sb.toString());
    }

}
