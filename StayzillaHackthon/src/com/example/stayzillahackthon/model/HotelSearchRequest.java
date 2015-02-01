package com.example.stayzillahackthon.model;

import java.io.Serializable;

import com.techjini.communication.messages.RequestMessage;

public class HotelSearchRequest extends RequestMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3237296119092075101L;
	public String location;
	public String checkin;
	public String checkout;
	public String property_type;

}
