package com.example.stayzillahackthon;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stayzillahackthon.model.HotelSearchRequest;
import com.example.stayzillahackthon.model.HotelSearchResponse;
import com.example.stayzillahackthon.service.FetchDataService;
import com.techjini.communication.listener.NetworkListener;
import com.techjini.communication.messages.ResponseMessage;

public class HotelListActivity extends BaseActivity implements NetworkListener,
		OnClickListener {
	DrawerLayout dLayout;
	LinearLayout leftDrawer;

	Button searchButton;
	HotelSearchRequest searchRequest;
	TextView modifySearch;
	EditText location, checkIn, checkOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotel_drawer);
		modifySearch = (TextView) findViewById(R.id.modify_search_text);
		location = (EditText) findViewById(R.id.editText_location);
		checkIn = (EditText) findViewById(R.id.editText_CheckIN);
		checkIn.setOnClickListener(this);
		checkOut = (EditText) findViewById(R.id.editText_checkOut);
		checkOut.setOnClickListener(this);
		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

		searchButton = (Button) findViewById(R.id.button_search);
		searchButton.setOnClickListener(this);

		searchRequest = (HotelSearchRequest) getIntent().getSerializableExtra(
				"REQUEST");
		initializeSearchData();
		fetchData();

	}

	private void initializeSearchData() {

		if (!TextUtils.isEmpty(searchRequest.location)) {
			location.setText(searchRequest.location);
		}

		if (!TextUtils.isEmpty(searchRequest.checkin)) {
			checkIn.setText(searchRequest.checkin);
		}
		if (!TextUtils.isEmpty(searchRequest.checkout)) {
			checkOut.setText(searchRequest.checkout);
		}

	}

	protected boolean isAllFieldEntered() {
		String city = location.getText().toString();
		if (TextUtils.isEmpty(city)) {
			showToast("Please Enter City Name");
			return false;
		}
		searchRequest.location = city;

		if (TextUtils.isEmpty(searchRequest.checkin)) {
			showToast("Please Enter CheckIn Time");
			return false;
		}
		if (TextUtils.isEmpty(searchRequest.checkout)) {
			showToast("Please Enter Checkout Time");
			return false;
		}

		return true;
	}

	// Check in
	public void checkInClick(View v) {
		Toast.makeText(getApplicationContext(), "date check in",
				Toast.LENGTH_SHORT).show();
		// Process to get Current Date
		final Calendar c = Calendar.getInstance();
		int mYear, mMonth, mDay;
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(HotelListActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						String checkInSelectedDate = dayOfMonth + "/"
								+ (monthOfYear + 1) + "/" + year;
						searchRequest.checkin = checkInSelectedDate;
						checkIn.setText(checkInSelectedDate);

					}
				}, mYear, mMonth, mDay);
		dpd.show();
	}

	// Check in
	public void checkOutClick(View v) {
		Toast.makeText(getApplicationContext(), "date check Out",
				Toast.LENGTH_SHORT).show();
		// Process to get Current Date
		int mYear, mMonth, mDay;
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(HotelListActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						String checkOutSelectedDate = dayOfMonth + "/"
								+ (monthOfYear + 1) + "/" + year;
						searchRequest.checkout = checkOutSelectedDate;
						checkOut.setText(checkOutSelectedDate);

					}
				}, mYear, mMonth, mDay);
		dpd.show();
	}

	private void fetchData() {

		if (searchRequest != null && isAllFieldEntered()) {
			showProgress("Fetching Data ...");

			new FetchDataService(this, searchRequest, this);
		} else {
			dLayout.openDrawer(leftDrawer);
		}
	}

	@Override
	public void onComplete(boolean success, String serviceId,
			final ResponseMessage responseModel) {
		stopProgress();

		if (success && serviceId.equalsIgnoreCase("searchRequest")
				&& responseModel instanceof HotelSearchResponse) {

			HotelSearchResponse response = (HotelSearchResponse) responseModel;
			if (response.hotels.size() > 0) {
				Log.i("", "Got the data " + responseModel.toString());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						populateData((HotelSearchResponse) responseModel);
					}
				});
			} else {
				showToast("No Hotels found!. Modify Your Search");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dLayout.openDrawer(leftDrawer);
					}
				});
			}

		} else {
			if (responseModel != null && responseModel.message != null) {
				showToast(responseModel.message);
			} else {
				showToast("Unable to fetch data! Try Again.");
			}
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					dLayout.openDrawer(leftDrawer);
				}
			});

		}

	}

	protected void populateData(HotelSearchResponse responseModel) {
		if (responseModel != null && responseModel.hotels != null
				&& responseModel.hotels.size() == 0) {
			showToast("No Hotels Found. Modify Your Search");
			dLayout.openDrawer(leftDrawer);
		}
		// else {
		// dLayout.closeDrawer(leftDrawer);
		// }

		HotelListFragment hotelListFragment = new HotelListFragment();
		hotelListFragment.setHotelData(responseModel, this);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, hotelListFragment).commit();
		stopProgress();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_search:
			if (isAllFieldEntered()) {
				fetchData();
				dLayout.closeDrawer(leftDrawer);
			}
			break;
		case R.id.editText_CheckIN:
			checkInClick(v);
			break;
		case R.id.editText_checkOut:
			checkOutClick(v);
			break;

		default:
			break;
		}

	}
}
