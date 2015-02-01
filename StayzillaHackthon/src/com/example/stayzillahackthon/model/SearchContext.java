package com.example.stayzillahackthon.model;

public class SearchContext {

	public String locationId;
	public String city;
	public String checkout;
	public boolean latLongSearch;
	public String lat;
	public String lng;
	@Override
	public String toString() {
		return "SearchContext [locationId=" + locationId + ", city=" + city
				+ ", checkout=" + checkout + ", latLongSearch=" + latLongSearch
				+ ", lat=" + lat + ", lng=" + lng + "]";
	}
	
	
	
}
