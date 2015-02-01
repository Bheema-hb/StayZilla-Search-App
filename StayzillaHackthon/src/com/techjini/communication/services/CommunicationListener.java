/**
 * 
 */
package com.techjini.communication.services;

import java.io.InputStream;

import com.techjini.communication.messages.ResponseMessage;

/**
 * @author arun
 * 
 */

public interface CommunicationListener {

	public void onSuccess(String serviceId, String url, InputStream jsonResponse,
			ResponseMessage model);

	public void onError(String serviceId, int errorCode, String errorMsg,
			ResponseMessage model);

}
