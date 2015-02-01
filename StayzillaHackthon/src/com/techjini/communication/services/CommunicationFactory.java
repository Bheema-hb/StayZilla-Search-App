/**
 * 
 */
package com.techjini.communication.services;

import com.techjini.communication.handler.CommunicationHandler;
import com.techjini.communication.handler.RestCommunicationHandler;

/**
 * @author arun
 * 
 */
public class CommunicationFactory {

	public CommunicationHandler getHandler(String protocol) {
		protocol.trim();
		if (protocol.compareToIgnoreCase("REST") == 0) {
			return new RestCommunicationHandler();
		} else {
			System.out.println("Not Yet Implemented");
			return null;
		}

	}

}
