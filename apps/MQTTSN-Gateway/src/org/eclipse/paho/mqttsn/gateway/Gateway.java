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

package org.eclipse.paho.mqttsn.gateway;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.mqttsn.gateway.core.Dispatcher;
import org.eclipse.paho.mqttsn.gateway.core.GatewayMsgHandler;
import org.eclipse.paho.mqttsn.gateway.exceptions.MqttsException;
import org.eclipse.paho.mqttsn.gateway.messages.Message;
import org.eclipse.paho.mqttsn.gateway.messages.control.ControlMessage;
import org.eclipse.paho.mqttsn.gateway.timer.TimerService;
import org.eclipse.paho.mqttsn.gateway.utils.ConfigurationParser;
import org.eclipse.paho.mqttsn.gateway.utils.GWParameters;
import org.eclipse.paho.mqttsn.gateway.utils.GatewayAddress;
import org.eclipse.paho.mqttsn.gateway.utils.GatewayLogger;

/**
 * This is the entry point of the MQTT-SN Gateway.
 * 
 */
public class Gateway {
	private static Dispatcher dispatcher;
	private static ShutDownHook shutdHook;



	public void start(String fileName){
		DateFormat dFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");

		System.out.println();
		System.out.println(dFormat.format(new Date())+ 
				"  INFO:  -------- MQTT-SN Gateway starting --------");

		//load the gateway parameters from a file		
		System.out.println(dFormat.format(new Date())+ 
				"  INFO:  Loading MQTT-SN Gateway parameters from " + fileName + " ... ");
		try {
			ConfigurationParser.parseFile(fileName);
		} catch (MqttsException e) {
			e.printStackTrace();
			GatewayLogger.error("Failed to load Gateway parameters. Gateway cannot start.");
			System.exit(1);
		}
		GatewayLogger.info("Gateway paremeters loaded.");

		//instantiate the timer service
		TimerService.getInstance();

		//instantiate the dispatcher
		dispatcher = Dispatcher.getInstance();

		//initialize the dispatcher
		dispatcher.initialize();		

		//create the address of the gateway itself(see org.eclipse.paho.mqttsn.gateway.utils.GatewayAdress)
		int len = 1;
		byte[] addr = new byte[len];
		addr[0] = (byte)GWParameters.getGwId();

		InetAddress ip = null;

		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {			
			e.printStackTrace();
			GatewayLogger.error("Failed to create the address of the Gateway.Gateway cannot start.");
			System.exit(1);
		}

		int port = GWParameters.getUdpPort();

		GatewayAddress gatewayAddress = new GatewayAddress(addr,ip,port);
		GWParameters.setGatewayAddress(gatewayAddress);		


		//create a new GatewayMsgHandler (for the connection of the gateway itself)		
		GatewayMsgHandler gatewayHandler = new GatewayMsgHandler(GWParameters.getGatewayAddress());

		//insert this handler to the Dispatcher's mapping table
		dispatcher.putHandler(GWParameters.getGatewayAddress(), gatewayHandler);

		//initialize the GatewayMsgHandler
		gatewayHandler.initialize();

		//connect to the broker
		gatewayHandler.connect();

		//add a "listener" for catching shutdown events (Ctrl+C,etc.)
		shutdHook = new ShutDownHook(); 
		Runtime.getRuntime().addShutdownHook(shutdHook);		
	}

	/**
	 * 
	 */
	public static void shutDown(){
		//generate a control message 
		ControlMessage controlMsg = new ControlMessage();
		controlMsg.setMsgType(ControlMessage.SHUT_DOWN);

		//construct an "internal" message and put it to dispatcher's queue
		//@see org.eclipse.paho.mqttsn.gateway.core.Message
		Message msg = new Message(null);
		msg.setType(Message.CONTROL_MSG);
		msg.setControlMessage(controlMsg);
		dispatcher.putMessage(msg);
	}


	/**
	 *
	 */
	private class ShutDownHook extends Thread{
		public void run(){
			shutDown();	
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "gateway.properties";
		if (args.length > 0) fileName = args[0];
		Gateway gateway = new Gateway();
		gateway.start(fileName);		
	}

	public static void removeShutDownHook() {
		Runtime.getRuntime().removeShutdownHook(shutdHook);		
	}	
}
