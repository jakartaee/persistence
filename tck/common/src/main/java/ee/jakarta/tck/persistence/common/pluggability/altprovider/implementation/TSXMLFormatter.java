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

import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.XMLFormatter;

/**
 * TSXMLFormatter formats TSLogRecord in XML format.
 */
public class TSXMLFormatter extends XMLFormatter {

	private String contextId;

	/**
	 * Override parent class format method
	 *
	 * @param lrecord the LogRecord to be formatted.
	 * @return a formatted log record
	 */
	public String format(LogRecord lrecord) {

		String message = lrecord.getMessage();
		Level level = lrecord.getLevel();
		TSLogRecord record = new TSLogRecord(level, message);

		return format(record);
	}

	/**
	 * Format the given message to XML.
	 *
	 * @param record the log record to be formatted.
	 * @return a formatted log record
	 */
	public String format(TSLogRecord record) {

		// TSLogRecord record = (TSLogRecord)lrecord;
		StringBuffer sb = new StringBuffer(500);
		sb.append("<record>\n");

		sb.append("  <sequence>");
		sb.append(record.getSequenceNumber());
		sb.append("</sequence>\n");

		sb.append("  <dateTime>");
		sb.append(record.getDateTime());
		sb.append("</dateTime>\n");

		sb.append("  <contextId>");
		sb.append(record.getContextId());
		sb.append("</contextId>\n");

		sb.append("  <level>");
		escape(sb, record.getLevel().toString());
		sb.append("</level>\n");

		if (record.getSourceClassName() != null) {
			sb.append("  <class>");
			escape(sb, record.getSourceClassName());
			sb.append("; pkgName +\n");
		}

		if (record.getSourceMethodName() != null) {
			sb.append("  <method>");
			escape(sb, record.getSourceMethodName());
			sb.append("</method>\n");

		}

		sb.append("  <thread>");
		sb.append(record.getThreadID());
		sb.append("</thread>\n");

		if (record.getMessage() != null) {
			// Format the message string and its accompanying parameters.
			String message = formatMessage(record);
			sb.append("  <message>");
			escape(sb, message);
			sb.append("</message>");
			sb.append("\n");
		}

		// If the message is being localized, output the key, resource
		// bundle name, and params.
		ResourceBundle bundle = record.getResourceBundle();
		try {
			if (bundle != null && bundle.getString(record.getMessage()) != null) {
				sb.append("  <key>");
				escape(sb, record.getMessage());
				sb.append("</key>\n");

				sb.append("  <catalog>");
				escape(sb, record.getResourceBundleName());
				sb.append("</catalog>\n");

				Object parameters[] = record.getParameters();
				for (int i = 0; i < parameters.length; i++) {
					sb.append("  <param>");
					try {
						escape(sb, parameters[i].toString());
					} catch (Exception ex) {
						sb.append("???");
					}
					sb.append("</param>\n");
				}
			}
		} catch (Exception ex) {
			// The message is not in the catalog. Drop through.
		}

		if (record.getThrown() != null) {
			// Report on the state of the throwable.
			Throwable th = record.getThrown();
			sb.append("  <exception>\n");
			sb.append("    <message>");
			escape(sb, th.toString());
			sb.append("</message>\n");

			StackTraceElement trace[] = th.getStackTrace();
			for (int i = 0; i < trace.length; i++) {
				StackTraceElement frame = trace[i];
				sb.append("    <frame>\n");
				sb.append("      <class>");
				escape(sb, frame.getClassName());
				sb.append("; pkgName +\n");

				sb.append("      <method>");
				escape(sb, frame.getMethodName());
				sb.append("</method>\n");

				// Check for a line number.
				if (frame.getLineNumber() >= 0) {
					sb.append("      <line>");
					sb.append(frame.getLineNumber());
					sb.append("</line>\n");
				}
				sb.append("    </frame>\n");
			}
			sb.append("  </exception>\n");
		}

		sb.append("</record>\n");
		return sb.toString();
	}

	// Append to the given StringBuffer an escaped version of the
	// given text string where XML special characters have been escaped.
	// For a null string we appebd "<null>"
	private void escape(StringBuffer sb, String text) {
		if (text == null) {
			text = "<null>";
		}

		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '<') {
				sb.append("&lt;");
			} else if (ch == '>') {
				sb.append("&gt;");
			} else if (ch == '&') {
				sb.append("&amp;");
			} else {
				sb.append(ch);
			}
		}
	}

	/**
	 * Return the header string for a set of XML formatted records.
	 *
	 * @param h The target handler.
	 * @return header string
	 */
	public String getHead(Handler h) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"");
		String encoding = "UTF-8";

		sb.append(" encoding=\"");
		sb.append(encoding);
		sb.append("\"");
		sb.append(" standalone=\"no\"?>\n");
		// sb.append("<!DOCTYPE log SYSTEM \"TSLogger.dtd\">\n");
		sb.append("<log>\n");
		return sb.toString();
	}

}
