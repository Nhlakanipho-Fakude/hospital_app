package com.Alot.reminder;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Clinic extends FragmentActivity {
	private ImageView back, exit;
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clinic);
		back = (ImageView) findViewById(R.id.back);
		exit = (ImageView) findViewById(R.id.exit_button);
		Integer resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(Clinic.this);
		if (resultCode == ConnectionResult.SUCCESS) {
			// Do what you want
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					back.setImageResource(R.drawable.clicked_back);
					startActivity(new Intent(Clinic.this, HomeScreen.class));
					finish();
				}
			});
			exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					++index;
					exit.setBackgroundResource(R.drawable.clicked_exit);
					if (index == 1) {
						Toast.makeText(getApplicationContext(),
								"click the exit button again to exit.",
								Toast.LENGTH_LONG).show();
					} else if (index > 1) {
						finish();
					} else {
						Toast.makeText(getApplicationContext(),
								"Logging out...", Toast.LENGTH_LONG).show();
					}
				}
			});
		} else {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					Clinic.this, 0);
			if (dialog != null) {
				// This dialog will help the user update to the latest
				// GooglePlayServices
				dialog.show();
			}
		}

	}

}
