package com.techjini.communication.listener;

/**
 * 
 */

import com.techjini.communication.messages.ResponseMessage;

public interface NetworkListener {

	public void onComplete(boolean success, String serviceId,
			ResponseMessage responseModel);
}
