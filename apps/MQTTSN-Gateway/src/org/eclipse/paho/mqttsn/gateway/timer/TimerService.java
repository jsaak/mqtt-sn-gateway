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

package org.eclipse.paho.mqttsn.gateway.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.eclipse.paho.mqttsn.gateway.core.Dispatcher;
import org.eclipse.paho.mqttsn.gateway.messages.Message;
import org.eclipse.paho.mqttsn.gateway.messages.control.ControlMessage;
//import org.eclipse.paho.mqttsn.gateway.timer.TimerService.TimeoutTimerTask;
import org.eclipse.paho.mqttsn.gateway.utils.Address;


public class TimerService {
	
	private static TimerService instance = null;
	
	private Timer timer;
	private Dispatcher dispatcher;
	private Vector<TimeoutTimerTask> timeoutTasks;


	/**
	 * Constructor.
	 */
	public TimerService() {
		timer = new Timer();
		dispatcher = Dispatcher.getInstance();	
		timeoutTasks = new Vector<TimeoutTimerTask>();
	}
	
	/**
	 * This method returns the instance of this object.If there no such an instance
	 * a new object is created.
	 * 
	 * @return The instance of this object.
	 */
	public static synchronized TimerService getInstance() {
		if (instance == null) {
			instance = new TimerService();
		}
		return instance;
	}

	/**
	 * This method schedules a TimeoutTimerTask to the timer for future executions and
	 * stores it to a list. 
	 * 
	 * @see TimeoutTimerTask
	 * 
	 * @param clientAddress The address of the client.
	 * @param type The type of task/timeout (WAITING_WILLTOPIC, WAITING_WILLMESSAGE, KEEP_ALIVE,etc.).
	 * @param timeout Expresses the delay and the period (in seconds) of executing the TimeoutTimerTask.
	 */
	public void register(Address address, int type, int timeout) {
		long delay = timeout * 1000;
		long period = timeout * 1000;
		
		TimeoutTimerTask timeoutTimerTask = new TimeoutTimerTask(address,type);

		//put this timeoutTimerTask in a list
		timeoutTasks.add(timeoutTimerTask);
		
		//schedule for future executions
		timer.scheduleAtFixedRate(timeoutTimerTask, delay, period);		
	}

	/**
	 * This method removes a TimeoutTimerTask from the list and cancels it. 
	 * 
	 * @see TimeoutTimerTask
	 * 
	 * @param clientAddress The address of the client.
	 * @param type The type of task/timeout (WAITING_WILLTOPIC, WAITING_WILLMESSAGE, etc.).
	 */
	public void unregister(Address address, int type) {
		for(int i = 0 ; i<timeoutTasks.size(); i++) {
			TimeoutTimerTask timeout = (TimeoutTimerTask)timeoutTasks.get(i);
			if(timeout.getAddress().equal(address)) 
				if (timeout.getType() == type){
					timeoutTasks.remove(i);
					timeout.cancel();
					break;
				}
		}
	}
	
	/**
	 * This method removes a TimeoutTimerTask from the list and cancels it. 
	 * 
	 * @see TimeoutTimerTask
	 * 
	 * @param clientAddress The address of the client.
	 */
	public void unregister(Address address){
		for(int i = timeoutTasks.size()-1; i >= 0; i--) {
			TimeoutTimerTask timeout = (TimeoutTimerTask)timeoutTasks.get(i);
			if(timeout.getAddress().equal(address)){
					timeoutTasks.remove(i);
					timeout.cancel();
			}
		}		
	}
	

	/**
	 * This object represents a TimeoutTimerTask.It is uniquely identified
	 * by the clientAddress and the type of task/timeout (WAITING_WILLTOPIC, etc.)
	 *
	 */
	 public class TimeoutTimerTask extends TimerTask {
		Address address;
		int type;
		
		/**
		* Constructor.
		* 
		* @param clientAddress The address of the client.
		* @param type The type of task/timeout (WAITING_WILLTOPIC, WAITING_WILLMESSAGE,etc.
		*/
		public TimeoutTimerTask(Address addr, int type) {
			this.address = addr;
			this.type = type;
		}
		
		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		public void run(){
			//create new control message
			ControlMessage controlMsg = new ControlMessage();
			controlMsg.setMsgType(type);
			
			//create an "internal" message
			Message msg = new Message(this.address);
			msg.setType(Message.CONTROL_MSG);
			msg.setControlMessage(controlMsg);
			
			//put this message to the Dispatcher's queue
			dispatcher.putMessage(msg);
        }

		public Address getAddress() {
			return address;
		}

		public int getType() {
			return type;
		}
	}
}