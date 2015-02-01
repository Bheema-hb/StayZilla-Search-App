package com.techjini.communication.listener;

/**
 * 
 */

import com.techjini.communication.messages.ResponseMessage;

/**
 * @author divya
 * 
 */
public interface ServiceListener {

	public void onSuccessDataReceive(String serviceId,
			ResponseMessage responseModel);

	public void onFailureReceive(String serviceId, ResponseMessage responseModel);

}
