package com.example.stayzillahackthon.model;

import java.util.ArrayList;

import com.techjini.communication.messages.ResponseMessage;

public class HotelSearchResponse extends ResponseMessage {
	public SearchContext context;
	public ArrayList<Hotel> hotels = new ArrayList<Hotel>();
	public MetaData metadata;
	@Override
	public String toString() {
		return "HotelSearchResponse [context=" + context + ", hotels=" + hotels
				+ ", metadata=" + metadata + "]";
	}

	
}
