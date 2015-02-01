package com.example.stayzillahackthon;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stayzillahackthon.model.HotelSearchRequest;

public class MainActivity extends BaseActivity implements OnClickListener {

	Button mFindButton;
	EditText checkinDate, checkoutDate, cityText;
	String checkInSelectedDate;
	String checkOutSelectedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mFindButton = (Button) findViewById(R.id.button_search);
		checkinDate = (EditText) findViewById(R.id.editText_CheckIN);
		checkoutDate = (EditText) findViewById(R.id.editText_checkOut);
		cityText = (EditText) findViewById(R.id.editText_location);
		mFindButton.setOnClickListener(this);
		checkinDate.setOnClickListener(this);
		checkoutDate.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_search:
			if (isAllFieldEntered()) {
				fetchData();
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

	private void fetchData() {

		HotelSearchRequest request = new HotelSearchRequest();
		request.checkin = checkinDate.getText().toString().trim();
		request.checkout = checkoutDate.getText().toString().trim();
		request.location = cityText.getText().toString().trim();
		request.property_type = "Hotels";

		Intent hotelListIntent = new Intent(this, HotelListActivity.class);
		hotelListIntent.putExtra("REQUEST", request);

		startActivity(hotelListIntent);

	}

	protected boolean isAllFieldEntered() {
		String city = cityText.getText().toString();
		if (TextUtils.isEmpty(city)) {
			showToast("Please Enter City Name");
			return false;
		}

		if (TextUtils.isEmpty(checkinDate.getText().toString().trim())) {
			showToast("Please Enter CheckIn Time");
			return false;
		}
		if (TextUtils.isEmpty(checkoutDate.getText().toString().trim())) {
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
		DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						checkInSelectedDate = dayOfMonth + "/"
								+ (monthOfYear + 1) + "/" + year;
						checkinDate.setText(checkInSelectedDate);

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
		DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						checkOutSelectedDate = dayOfMonth + "/"
								+ (monthOfYear + 1) + "/" + year;
						checkoutDate.setText(checkOutSelectedDate);

					}
				}, mYear, mMonth, mDay);
		dpd.show();
	}

}
