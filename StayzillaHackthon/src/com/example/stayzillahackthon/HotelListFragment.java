package com.example.stayzillahackthon;

import com.example.stayzillahackthon.model.HotelSearchResponse;
import com.example.stayzillahackthon.views.CustomExpandableListview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HotelListFragment extends Fragment {
	CustomExpandableListview listview;
	HotelSearchResponse response;

	public HotelListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.hotel_list_view, container,
				false);
		listview = (CustomExpandableListview) rootView
				.findViewById(R.id.hotelList);
		listview.setData(response.hotels);

		return rootView;
	}

	public void setHotelData(HotelSearchResponse responseModel,
			HotelListActivity hotelListActivity) {

		response = responseModel;

	}
}
