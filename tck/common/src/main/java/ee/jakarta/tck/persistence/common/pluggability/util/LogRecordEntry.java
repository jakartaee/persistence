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

package ee.jakarta.tck.persistence.common.pluggability.util;

import java.io.Serializable;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LogRecordEntry implements Serializable {

	private long milliSeconds;

	private long sequenceNumber;

	private String level;

	private String className;

	private String methodName;

	private String message;

	private String thrown;

	public LogRecordEntry(Node recordNode) throws Exception {
		if (!recordNode.getNodeName().equals("record")) {
			throw new Exception("Unexpected tag :" + recordNode.getNodeName());
		}
		NodeList nodes = recordNode.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String childNode = node.getNodeName();

			if (childNode.equals("millis")) {
				milliSeconds = Long.valueOf(getText(node));

			} else if (childNode.equals("sequence")) {
				sequenceNumber = Long.valueOf(getText(node));

			} else if (childNode.equals("level")) {
				level = getText(node);

			} else if (childNode.equals("class")) {
				className = getText(node);

			} else if (childNode.equals("method")) {
				methodName = getText(node);

			} else if (childNode.equals("message")) {
				message = getText(node);

			} else if (childNode.equals("exception")) {
				thrown = getText(node);

			}

		}
	}

	public long getMilliSeconds() {
		return this.milliSeconds;
	}

	public void setMilliSeconds(long milliSec) {
		milliSeconds = milliSec;
	}

	public long getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(long seqNum) {
		sequenceNumber = seqNum;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String msg) {
		message = msg;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String lvl) {
		level = lvl;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String cName) {
		className = cName;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String mName) {
		methodName = mName;
	}

	public String getThrown() {
		return this.thrown;
	}

	public void setThrown(String thrwn) {
		thrown = thrwn;
	}

	public String getText(Node textNode) {
		String result = "";
		NodeList nodes = textNode.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeType() == Node.TEXT_NODE) {
				result = node.getNodeValue();
				break;
			}
		}
		return result;
	}
}
