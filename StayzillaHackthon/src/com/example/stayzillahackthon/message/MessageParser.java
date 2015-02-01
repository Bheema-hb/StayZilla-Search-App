package com.example.stayzillahackthon.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.stayzillahackthon.HotelListActivity;
import com.example.stayzillahackthon.R;
import com.example.stayzillahackthon.model.HotelSearchRequest;

public class MessageParser {

	String source,destination;
	String bookingDate;
	String matchedPattern;

	public void handleMessage(Intent intent, Context context) {

		String message = getMessageFromIntent(intent);

		boolean isbookingSms = isBookingSms(message);
		if(isbookingSms){
			showNotification(context);
		}

	}

	private void showNotification(Context context) {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.stayzillalogo)
		        .setContentTitle("Hey! Are you travelling from "+matchedPattern)
		        .setContentText("Why don't you book a Hotel?");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, HotelListActivity.class);
		HotelSearchRequest request  = new HotelSearchRequest();
		request.location=destination.trim();
		request.property_type="Hotels";
		resultIntent.putExtra("REQUEST", request);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(HotelListActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());
	}

	private boolean isBookingSms(String message) {
		source=null;
		destination=null;
		matchedPattern=null;

		if (isMakeMyTripBooking(message)) {
			Pattern pattern = Pattern.compile("([A-Za-z]+-[A-Za-z]+)");
			getDestination(message,pattern);
			destination = matchedPattern.substring(matchedPattern.indexOf("-")+1);
			
			return true;

		} else if (isIRCTCBooking(message)) {
			Pattern pattern = Pattern.compile("([A-Z]+-[A-Z]+)");
			getDestination(message,pattern);
			destination = matchedPattern.substring(matchedPattern.indexOf("-")+1);
			if(destination.equalsIgnoreCase("SBC")) destination="Bangalore";
			return true;

		} else if (isKSRTCBooking(message)) {
			Pattern pattern = Pattern.compile("([A-Za-z]+ to [A-Za-z]+)");
			getDestination(message,pattern);
			
			destination = matchedPattern.substring(matchedPattern.indexOf("to")+"to".length());
			
			
			return true;
			
		}

		return false;
	}

	private void getDestination(String message, Pattern pattern) {

		
		Matcher m = pattern.matcher(message);

		if (m.find()) {
			Log.i("", "Matched count " + m.groupCount()+" "+m.group());
			matchedPattern = m.group(1);
		}
	}

	private boolean isMakeMyTripBooking(String message) {
		return message.contains("MakeMyTrip");
	}

	private boolean isIRCTCBooking(String message) {
		return (message.contains("PNR") && message.contains("TRAIN"));

	}

	private boolean isKSRTCBooking(String message) {
		return (message.contains("KSRTC") && message.contains("PNRno"));

	}

	public static String getMessageFromIntent(Intent intent) {
		String message = null;
		final Bundle bundle = intent.getExtras();

		if (bundle != null) {

			final Object[] pdusObj = (Object[]) bundle.get("pdus");

			for (int i = 0; i < pdusObj.length; i++) {

				SmsMessage currentMessage = SmsMessage
						.createFromPdu((byte[]) pdusObj[i]);
				String phoneNumber = currentMessage
						.getDisplayOriginatingAddress();

				String senderNum = phoneNumber;
				if (message == null) {
					message = currentMessage.getDisplayMessageBody();
				} else {
					message += currentMessage.getDisplayMessageBody();
				}
				Log.i("SMS received", "senderNum: " + senderNum + "; message: "
						+ message);

			}
		}

		return message;
	}
}
