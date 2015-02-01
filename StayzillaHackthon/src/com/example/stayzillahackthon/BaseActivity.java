package com.example.stayzillahackthon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 
 * @author Bheema
 *
 */
public class BaseActivity extends Activity {

	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void showToast(final String string) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, string,
						Toast.LENGTH_LONG).show();

			}
		});

	}

	public void showProgress(final String message) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (!isFinishing()) {
					if (mDialog == null) {
						mDialog = new ProgressDialog(BaseActivity.this);

						mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						mDialog.setCancelable(false);
						mDialog.setCanceledOnTouchOutside(false);
					}
					mDialog.setMessage(message);
					if (!mDialog.isShowing()) {
						mDialog.show();
					}
				}
			}
		});

	}

	public void stopProgress() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
}
