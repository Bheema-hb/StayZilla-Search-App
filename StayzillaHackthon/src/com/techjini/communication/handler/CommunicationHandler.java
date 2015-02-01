/**
 * 
 */
package com.techjini.communication.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.techjini.communication.listener.ServiceListener;
import com.techjini.communication.messages.HeaderMessage;
import com.techjini.communication.messages.RequestMessage;
import com.techjini.communication.messages.ResponseMessage;
import com.techjini.communication.services.CommunicationListener;

/**
 * @author arun
 * 
 */
public class CommunicationHandler implements CommunicationListener {

	public static final int ERROR_CLIENT_PROTOCOL = 1;
	public static final int ERROR_IO = 0;

	protected ServiceListener listener = null;

	protected Properties properties = null;

	protected String serviceId;

	public void communicate(RequestMessage requestModel,
			ResponseMessage responseModel, HeaderMessage headerModel,
			int postBodyType) {

	}

	public void onSuccess(String serviceId, String url,
			InputStream jsonResponse, ResponseMessage model) {

		Gson gson = new Gson();
		// String
		// string=CommunicationHandler.getStringFromInputStream(jsonResponse);
		// DLogger.e(this, "nitin "+string);
		JsonReader reader = new JsonReader(new InputStreamReader(jsonResponse));
		try {

			// model = gson.fromJson(string, model.getClass());
			model = gson.fromJson(reader, model.getClass());
			if (model == null) {
				model = new ResponseMessage();
				model.message = "parse exception " + serviceId;
				// DLogger.e(this, "parse exception " + serviceId);
				listener.onFailureReceive(serviceId, model);
				return;
			}
			model.http_code = 200;
			model.message = "success";
		} catch (JsonIOException e) {
			e.printStackTrace();
			model = new ResponseMessage();
			model.message = "JsonIOException " + serviceId;
			Log.e("sd", serviceId + " JsonIOException");
			listener.onFailureReceive(serviceId, model);
			return;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			model = new ResponseMessage();
			model.message = "JsonSyntaxException " + serviceId;
			Log.e("sds", serviceId + " JsonSyntaxException");
			listener.onFailureReceive(serviceId, model);
			return;
		} finally {

			try {
				reader.close();
				jsonResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		listener.onSuccessDataReceive(serviceId, model);
		return;

	}


	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public void onError(String serviceId, int errorCode, String errorMsg,
			ResponseMessage model) {
		// DLogger.e(this, "Response:: " + serviceId + "= " + errorCode);

		if (model == null) {
			model = new ResponseMessage();
		}
		model.http_code = errorCode;
		model.message = errorMsg;
		listener.onFailureReceive(serviceId, model);
		return;
	}

	/**
	 * @return the listener
	 */
	public ServiceListener getListener() {
		return listener;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(ServiceListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the properties
	 */
	public Properties getConfiguration() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setConfiguration(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId
	 *            the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
