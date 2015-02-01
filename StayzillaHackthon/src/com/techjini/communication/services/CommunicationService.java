package com.techjini.communication.services;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.provider.SyncStateContract.Constants;

import com.techjini.communication.handler.CommunicationHandler;
import com.techjini.communication.listener.ServiceListener;
import com.techjini.communication.messages.HeaderMessage;
import com.techjini.communication.messages.RequestMessage;
import com.techjini.communication.messages.ResponseMessage;
import com.techjini.communication.utility.Utility;

/**
 * @author arun,Srikanth
 * 
 */

public class CommunicationService {
	public static final int POST_BODY_TYPE_NOT_APPLICABLE = 0;
	public static final int POST_BODY_TYPE_JSON = 1;
	public static final int POST_BODY_TYPE_STRING = 2;
	public static final int POST_BODY_TYPE_OLA = 3;

	public void communicate(String serviceId, Context context,
			ServiceListener listener, RequestMessage requestModel,
			final ResponseMessage responseModel, HeaderMessage headerModel,
			int postBodyType) {

		Properties prop = new Properties();

		try {
				prop = new Utility().loadPropties(context,
						"Config.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String protocol = prop.getProperty(serviceId + ".protocol");

		CommunicationHandler commsHandler = new CommunicationFactory()
				.getHandler(protocol);
		if (commsHandler == null) {
			listener.onFailureReceive(serviceId, new ResponseMessage());
			return;
		}
		commsHandler.setServiceId(serviceId);
		commsHandler.setConfiguration(prop);
		commsHandler.setListener(listener);

		commsHandler.communicate(requestModel, responseModel, headerModel,
				postBodyType);
	}

}
