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

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.paho.mqttsn.gateway.utils.GWParameters;

public class TopicMappingTable {

	private Hashtable<Integer, String> topicIdTable;

	public TopicMappingTable(){
		topicIdTable = new Hashtable<Integer, String>();
	}


	public void initialize() {
		Iterator<?> iter = GWParameters.getPredefTopicIdTable().keySet().iterator();
		Iterator<?> iterVal = GWParameters.getPredefTopicIdTable().values().iterator();

		Integer topicId;
		String topicName;
		while (iter.hasNext()) {
			topicId = (Integer)iter.next();	
			topicName = (String)iterVal.next();
			topicIdTable.put(topicId, topicName);
		}
	}

	public void clear() {
    topicIdTable.clear();
  }

	/**
	 * @param topicId
	 * @param topicName
	 */
	public void assignTopicId(int topicId, String topicName) {
		topicIdTable.put(Integer.valueOf(topicId), topicName);
	}

	public String getTopicName(int topicId) {
		return (String)topicIdTable.get(Integer.valueOf(topicId));
	}

	/**
	 * @param topicName
	 * @return
	 */
	public int getTopicId(String topicName) {
		Iterator<Integer> iter = topicIdTable.keySet().iterator();
		Iterator<String> iterVal = topicIdTable.values().iterator();
		Integer ret = Integer.valueOf(0);
		while (iter.hasNext()) {
			Integer topicId = (Integer)iter.next();			
			String tname = (String)(iterVal.next());
			if(tname.equals(topicName)) {
				ret = topicId;
				break;
			}
		}
		return ret.intValue();
	}

	/**
	 * @param topicId
	 */
	public void removeTopicId(int topicId) {
		topicIdTable.remove(Integer.valueOf(topicId));
	}


	/**
	 * @param topicName
	 */
	public void removeTopicId(String topicName) {
		Iterator<Integer> iter = topicIdTable.keySet().iterator();
		Iterator<String> iterVal = topicIdTable.values().iterator();
		while (iter.hasNext()) {
			Integer topicId = (Integer)iter.next();			
			String tname = (String)(iterVal.next());

			//don't remove predefined topic ids
			if(tname.equals(topicName) && topicId.intValue() > GWParameters.getPredfTopicIdSize()) {
				topicIdTable.remove(topicId);
				break;
			}
		}
	}


	/**
	 * 
	 * Utility method. Prints the content of this mapping table
	 */
	public void printContent(){
		Iterator<Integer> iter = topicIdTable.keySet().iterator();
		Iterator<String> iterVal = topicIdTable.values().iterator();
		while (iter.hasNext()) {
			Integer topicId = (Integer)iter.next();			
			String tname = (String)(iterVal.next());
			System.out.println(topicId+" = "+ tname);
		}		
	}
}
