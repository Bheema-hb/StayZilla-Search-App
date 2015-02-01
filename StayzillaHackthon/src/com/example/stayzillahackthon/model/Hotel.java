package com.example.stayzillahackthon.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Hotel {

	public String name;
	public String displayName;
	public String stayType;
	public String address;
	public String city;
	public String id;
	public String price;
	public String rawPrice;
	public String imageURL;
	public String starRating;
	public String custpickOrder;
	public String distanceFromLatLong;
	public boolean dayRoomAvailable;
	public boolean isICH;
	public String bookings;
	public ArrayList<Room> rooms = new ArrayList<Room>();
	public String[] gallery;
	public GeoCoOrdinates geoCoordinates;
	public DistanceFrom distanceFrom;
	public Info info;
	public Amenities amenities;

	@Override
	public String toString() {
		return "Hotel [name=" + name + ", displayName=" + displayName
				+ ", stayType=" + stayType + ", address=" + address + ", city="
				+ city + ", id=" + id + ", price=" + price + ", rawPrice="
				+ rawPrice + ", imageURL=" + imageURL + ", starRating="
				+ starRating + ", custpickOrder=" + custpickOrder
				+ ", distanceFromLatLong=" + distanceFromLatLong
				+ ", dayRoomAvailable=" + dayRoomAvailable + ", isICH=" + isICH
				+ ", bookings=" + bookings + ", rooms=" + rooms + ", gallery="
				+ Arrays.toString(gallery) + ", geoCoordinates="
				+ geoCoordinates + ", distanceFrom=" + distanceFrom + ", info="
				+ info + ", amenities=" + amenities + "]";
	}

}
