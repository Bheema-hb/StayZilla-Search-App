package com.techjini.communication.handler;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.techjini.communication.messages.HeaderMessage;
import com.techjini.communication.messages.RequestMessage;
import com.techjini.communication.messages.ResponseMessage;
import com.techjini.communication.parsers.JSONParser;

/**
 * @author Techjini
 */
public class RestCommunicationHandler extends CommunicationHandler {

	// private final String HEADER_KEY = "x-wsse";
	// private final String HEADER_PREFIX_VALUE = "UsernameToken ";

	public void communicate(RequestMessage requestModel,
			final ResponseMessage responseModel, HeaderMessage headerModel,
			int postBodyType) {

		final HttpUriRequest requestUri = getRequestUri(requestModel,
				postBodyType);

		requestUri.addHeader("Content-Type",
				"application/x-www-form-urlencoded");
	
		new Thread(new Runnable() {

			public void run() {
				RestCommunicator communicator = new RestCommunicator();
				communicator.communicate(serviceId, requestUri,
						RestCommunicationHandler.this, responseModel);

			}
		}).start();
	}

	private HttpUriRequest getRequestUri(RequestMessage requestModel,
			int postBodyType) {
		String baseURL = getConfiguration().getProperty(serviceId + ".baseURL");
		// String baseURL = Constants.BASEURL
		// + this.getConfiguration().getProperty(
		// this.serviceId + ".baseURL");
		String method = getConfiguration().getProperty(serviceId + ".method");

		String url = null;
		if (method.compareToIgnoreCase("GET") == 0) {
			url = new JSONParser().createGetRequest(baseURL, requestModel);
			if (serviceId.equalsIgnoreCase("accountSummary")) {
				Log.d("", url);
			}
			HttpGet httpGet = new HttpGet(url);

			return httpGet;
		} else if (method.compareToIgnoreCase("POST") == 0) {
			url = baseURL;

			// DLogger.d(this, "post request");
			HttpPost httpPost = new JSONParser().createPostRequest(baseURL,
					requestModel, postBodyType);
			return httpPost;
		} else if (method.compareToIgnoreCase("PUT") == 0) {
			url = baseURL;

			// System.out.println(url);
			HttpPut httpPut = new JSONParser().createPutRequest(baseURL,
					requestModel);

			return httpPut;
		} else if (method.compareToIgnoreCase("DELETE") == 0) {
			url = baseURL;

			// System.out.println(url);
			HttpDelete httpdelete = new JSONParser().createDeleteRequest(
					baseURL, requestModel);

			return httpdelete;
		} else {
			System.out.println("not implemented");

		}

		return null;

	}

}
