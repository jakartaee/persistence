/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/**
 *
 * @author Raja Perumal
 */

package ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * TSLogger is the custom Logger which extends java.util.Logger
 */
public class TSLogger extends Logger {
	public static final String MESSAGE_PREFIX = "JPA_ALTERNATE_PROVIDER : ";

	public static final String LOG_NAME = "JPALog.xml";

	public static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS");

	/**
	 * @serial The logging context Id
	 */
	private String contextId;

	private int levelValue = Level.INFO.intValue();

	private int offValue = Level.OFF.intValue();

	private Filter filter;

	private String name;

	// Note : The logger instance should not be
	// stored in this instance variable,
	// it should be kept at the log Manager using
	//
	// LogManager.addLogger(TSlogger);
	//
	// and it can be retrieved using
	//
	// LogManager.getLogger(name);
	//
	// Since Logger and TSLogger are of different types
	// we cannot use the above logic and hence we have
	// no choice except to store it here.
	//
	private static TSLogger tsLogger = null;

	protected TSLogger(String name) {
		super(name, null);
		this.name = name;
		levelValue = Level.INFO.intValue();
	}

	/**
	 * Find or create a logger for a named subsystem. If a logger has already been
	 * created with the given name it is returned. Otherwise a new logger is
	 * created.
	 * <p/>
	 * If a new logger is created its log level will be configured based on the
	 * LogManager configuration and it will configured to also send logging output
	 * to its parent's handlers. It will be registered in the LogManager global
	 * namespace.
	 *
	 * @param name A name for the logger. This should be a dot-separated name and
	 *             should normally be based on the package name or class name of the
	 *             subsystem, such as java.net or javax.swing
	 * @return a suitable Logger
	 */
	public static synchronized TSLogger getTSLogger(String name) {
		TSLogger result = null;

		LogManager manager = LogManager.getLogManager();

		// TSLogger result = manager.getLogger(name);
		// if (result == null){
		// result = new TSLogger(name);
		// manager.addLogger(result);
		// result = manager.getLogger(name);
		// }

		if (tsLogger != null) {
			if (tsLogger.getName().equals(name))
				result = tsLogger;
		} else {
			result = new TSLogger(name);
			manager.addLogger(result);
		}

		return result;
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects at the
	 * Level.INFO level
	 * <p/>
	 *
	 * @param msg The string message (or a key in the message catalog)
	 */
	public void log(String msg) {
		// assign default context (JPA) to all messages ???
		log(Level.INFO, msg);
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects.
	 * <p/>
	 *
	 * @param level One of the message level identifiers, e.g. SEVERE
	 * @param msg   The string message (or a key in the message catalog)
	 */
	public void log(Level level, String msg) {
		// assign default context (JPA) to all messages ???

		log(level, createDateTime(), MESSAGE_PREFIX + msg, "JPA");
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects.
	 * <p/>
	 *
	 * @param level    One of the message level identifiers, e.g. SEVERE
	 * @param dateTime The dateTime stamp of the message
	 * @param msg      The string message (or a key in the message catalog)
	 */
	public void log(Level level, String dateTime, String msg) {
		// assign default context (JPA) to all messages ???

		log(level, dateTime, MESSAGE_PREFIX + msg, "JPA");
	}

	/**
	 * Log a message, with no arguments.
	 * <p/>
	 * If the logger is currently enabled for the given message level then the given
	 * message is forwarded to all the registered output Handler objects.
	 * <p/>
	 *
	 * @param level     One of the message level identifiers, e.g. SEVERE
	 * @param msg       The string message (or a key in the message catalog)
	 * @param contextId the logging context Id
	 */
	public void log(Level level, String dateTime, String msg, String contextId) {
		if (level.intValue() < levelValue || levelValue == offValue) {
			return;
		}
		TSLogRecord lr = new TSLogRecord(level, dateTime, msg, contextId);
		String rbn = null;

		Logger target = this;
		while (target != null) {
			rbn = target.getResourceBundleName();
			if (rbn != null) {
				break;
			}
			target = target.getParent();
		}

		if (rbn != null) {
			lr.setResourceBundleName(rbn);
			// lr.setResourceBundle("null");
		}

		log(lr);

	}

	/**
	 * Log a TSLogRecord.
	 *
	 * @param record the TSLogRecord to be published
	 */
	public void log(TSLogRecord record) {
		if (record.getLevel().intValue() < levelValue || levelValue == offValue) {
			return;
		}
		synchronized (this) {
			if (filter != null && !filter.isLoggable(record)) {
				return;
			}
		}

		// Post the LogRecord to all our Handlers, and then to
		// our parents' handlers, all the way up the tree.

		TSLogger logger = this;
		while (logger != null) {
			Handler targets[] = logger.getHandlers();

			if (targets != null) {
				for (int i = 0; i < targets.length; i++) {
					// targets[i].publish(record);

					// Publish record only if the
					// handler is of type FileHandler
					// Do not publish to all parent handler
					// Parent handler may not be able to
					// Format the TSLogRecord, because
					// TSLogRecord is the custom record.
					if (targets[i] instanceof FileHandler)
						targets[i].publish(record);
				}
			}

			if (!logger.getUseParentHandlers()) {
				break;
			}

			// logger = (TSLogger)logger.getParent();
			logger = null;
		}
	}

	// pulled from TSJavaLog:
	private static TSLogger logger = getInstance();
	// private static TSJavaLog instance = getInstance();

	public static TSLogger getInstance() {
		/*
		 * if (instance == null) { instance = new TSJavaLog();
		 */

		if (logger == null) {
			try {
				String logFileLocation = System.getProperty("log.file.location");
				if (logFileLocation != null) {
					System.out.println("JPA_ALTERNATE_PROVIDER log.file.location:" + logFileLocation);

					File dir = new File(logFileLocation);
					String[] chld = dir.list();
					if (chld == null) {
						System.out.println("Appserver log directory does not exist or is not a directory.");
					} else {
						System.out.println("Searching for previous log files to delete");
						for (int i = 0; i < chld.length; i++) {
							String fileName = chld[i];
							if (fileName.indexOf(LOG_NAME) >= 0) {
								// System.out.println("Found File:"+fileName);
								File file = new File(logFileLocation + "/" + fileName);
								if (file.exists()) {
									System.out.println("Deleting JPA logfile:" + file.getName());
									file.delete();
								}
							} else {
								// System.out.println("File will not be deleted:"+fileName);
							}
						}
					}

					System.out.println("JPA_ALTERNATE_PROVIDER log:" + logFileLocation + "/" + LOG_NAME);

					logger = TSLogger.getTSLogger("JPA");
					boolean appendMode = false;

					// create a new file
					FileHandler fileHandler = new FileHandler(logFileLocation + "/" + LOG_NAME, appendMode);
					// new FileHandler(logFileLocation + "/" + LOG_NAME, 0, 1,
					// appendMode);
					fileHandler.setFormatter(new TSXMLFormatter());
					logger.addHandler(fileHandler);

				} else {
					// use default logging mechanism
					System.out.println("JPA_ALTERNATE_PROVIDER: log.file.location not set, using default logger");
					logger = TSLogger.getTSLogger("JPA");
					logger.log(Level.SEVERE, "log.file.location not set: Using default logger");
				}
			} catch (Exception e) {
				throw new RuntimeException("TSLogger Initialization failed", e);
			}
		}
		return logger;
	}

	public String createDateTime() {
		return df.format(new Date());
	}
}
