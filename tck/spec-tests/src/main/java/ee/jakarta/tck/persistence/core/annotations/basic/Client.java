/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.basic;

import java.lang.System.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public void createTestData() {
		try {
			getEntityTransaction().begin();
			final Integer integer = 1234;
			final short basicShort = 12;
			final Short basicBigShort = basicShort;
			final float basicFloat = 12.3f;
			final Float basicBigFloat = basicFloat;
			final long basicLong = 1234l;
			final Long basicBigLong = basicLong;
			final double basicDouble = 1234.5;
			final Double basicBigDouble = basicDouble;
			final char[] charArray = { 'a', 'b', 'c' };
			final Character[] bigCharacterArray = { 'a', 'b', 'c' };
			final byte[] byteArray = "abc".getBytes();
			final Byte[] bigByteArray = { (byte) 111, (byte) 101, (byte) 100 };
			final BigInteger bigInteger = new BigInteger("12345");
			final BigDecimal bigDecimal = new BigDecimal(bigInteger);
			final Date date = new Date();
			final long timeInSeconds = date.getTime();
			final Time time = new Time(timeInSeconds);
			final Timestamp timeStamp = new Timestamp(timeInSeconds);
			final Calendar calendar = Calendar.getInstance();

			A aRef = new A("9", null, 9, integer, basicShort, basicBigShort, basicFloat, basicBigFloat, basicLong,
					basicBigLong, basicDouble, basicBigDouble, 'a', charArray, bigCharacterArray, byteArray,
					bigByteArray, bigInteger, bigDecimal, date, time, timeStamp, calendar);

			getEntityManager().persist(aRef);
			getEntityManager().flush();
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

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}

	}

	protected void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
