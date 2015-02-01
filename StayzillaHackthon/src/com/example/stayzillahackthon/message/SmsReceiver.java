package com.example.stayzillahackthon.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		new MessageParser().handleMessage(intent,context);
	}

}
