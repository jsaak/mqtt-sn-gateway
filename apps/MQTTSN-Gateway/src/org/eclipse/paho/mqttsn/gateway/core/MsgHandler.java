/*******************************************************************************
 * Copyright (c) 2008, 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *    Ian Craggs - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.paho.mqttsn.gateway.core;

import org.eclipse.paho.mqttsn.gateway.messages.control.ControlMessage;
import org.eclipse.paho.mqttsn.gateway.messages.mqtt.MqttMessage;
import org.eclipse.paho.mqttsn.gateway.messages.mqtts.MqttsMessage;

public abstract class MsgHandler {
		
	/**
	 * 
	 */
	public abstract void initialize();
	
	
	/**
	 * @param msg
	 */
	public abstract void handleMqttsMessage(MqttsMessage msg);
	
	
	/**
	 * @param msg
	 */
	public abstract void handleMqttMessage(MqttMessage msg);
	
	
	/**
	 * @param msg
	 */
	public abstract void handleControlMessage(ControlMessage msg);
}
