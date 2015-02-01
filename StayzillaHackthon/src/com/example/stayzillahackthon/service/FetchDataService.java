package com.example.stayzillahackthon.service;

import android.content.Context;

import com.example.stayzillahackthon.model.HotelSearchResponse;
import com.techjini.communication.listener.NetworkListener;
import com.techjini.communication.listener.ServiceListener;
import com.techjini.communication.messages.RequestMessage;
import com.techjini.communication.messages.ResponseMessage;
import com.techjini.communication.services.CommunicationService;

public class FetchDataService implements ServiceListener {

	private NetworkListener mNetworkListener;

	public FetchDataService(Context context, RequestMessage request,
			NetworkListener listener) {
		mNetworkListener = listener;
		new CommunicationService().communicate("searchRequest", context, this,
				request, new HotelSearchResponse(), null,
				CommunicationService.POST_BODY_TYPE_STRING);
	}

	@Override
	public void onSuccessDataReceive(String serviceId,
			ResponseMessage responseModel) {
		mNetworkListener.onComplete(true, serviceId, responseModel);
	}

	@Override
	public void onFailureReceive(String serviceId, ResponseMessage responseModel) {
		mNetworkListener.onComplete(false, serviceId, responseModel);
	}
}
