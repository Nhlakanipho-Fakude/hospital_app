package com.Alot.reminder;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class Profile extends Activity {
	private EditText firstname, lastname, gender, location, idnumber,
			phonenumber, diagnosed, pillsTaken, pillsLeft, totalPills;
	private ImageView exit, back, edit;
	private int count = 0;
	private String Fi, La, Lo, PL, Pho, PT;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		exit = (ImageView) findViewById(R.id.exit_button);
		back = (ImageView) findViewById(R.id.back);
		firstname = (EditText) findViewById(R.id.firstname);
		lastname = (EditText) findViewById(R.id.lastname);
		gender = (EditText) findViewById(R.id.gender);
		location = (EditText) findViewById(R.id.location);
		idnumber = (EditText) findViewById(R.id.idnumber);
		phonenumber = (EditText) findViewById(R.id.phoneNumber);
		diagnosed = (EditText) findViewById(R.id.diagnosed);
		pillsTaken = (EditText) findViewById(R.id.pillsTaken);
		pillsLeft = (EditText) findViewById(R.id.pillsLeft);
		totalPills = (EditText) findViewById(R.id.TotalPills);
		edit = (ImageView) findViewById(R.id.edit);
		setProfileFields();
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				++count;
				if (count == 0)
					edit.setImageResource(R.drawable.clicked_edit);
				setButtonImage();

			}

		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exit.setImageResource(R.drawable.clicked_exit);
				finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back.setImageResource(R.drawable.clicked_back);
				startActivity(new Intent(Profile.this, HomeScreen.class));

				finish();
			}
		});
	}

	public void setProfileFields() {
		SharedPreferences prefs = getSharedPreferences("patient", MODE_PRIVATE);
		firstname.setText(prefs.getString("firstname", ""));
		lastname.setText(prefs.getString("lastname", ""));
		gender.setText(prefs.getString("gender", ""));
		location.setText(prefs.getString("location", ""));
		idnumber.setText(prefs.getString("idnumber", ""));
		phonenumber.setText(prefs.getString("phonenumber", ""));
		diagnosed.setText(prefs.getString("diagnosed", ""));
		pillsLeft.setText(prefs.getString("pillsLeft", ""));
		pillsTaken.setText(prefs.getString("pillsTaken", ""));
		totalPills.setText(prefs.getString("totalPills", ""));
	}

	private void setButtonImage() {
		if (count == 1) {
			firstname.setEnabled(true);
			lastname.setEnabled(true);
			location.setEnabled(true);
			pillsTaken.setEnabled(true);
			pillsLeft.setEnabled(true);
			phonenumber.setEnabled(true);
			edit.setImageResource(R.drawable.save);

		} else if (count == 2) {

			firstname.setEnabled(false);
			lastname.setEnabled(false);
			location.setEnabled(false);
			pillsTaken.setEnabled(false);
			pillsLeft.setEnabled(false);
			phonenumber.setEnabled(false);
			edit.setImageResource(R.drawable.edited);
			++count;
			Fi = firstname.getText().toString();
			La = lastname.getText().toString();
			Lo = location.getText().toString();
			PL = pillsLeft.getText().toString();
			Pho = phonenumber.getText().toString();
			PT = pillsTaken.getText().toString();

			new update().execute(new ApiConnector(
					"http://alot.2fh.co/remminder/update.php"));
		} else {
			count = 0;
		}
	}

	private class update extends AsyncTask<ApiConnector, Long, JSONArray> {
		public update() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Profile.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("Please wait while loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				SharedPreferences prefs = getSharedPreferences("patient",
						MODE_PRIVATE);
				if (!Fi.equals(prefs.getString("firstname", "firstname").trim())) {
					params[0].addString("firstname", Fi);
				} else {
					params[0].addString("firstname", "firstname");
				}
				if (!La.equals(prefs.getString("lastname", "lastname").trim())) {
					params[0].addString("lastname", La);
				} else {
					params[0].addString("lastname", "lastname");
				}
				if (!Lo.equals(prefs.getString("location", "location").trim()))

				{
					params[0].addString("location", Lo);
				} else {
					params[0].addString("location", "location");
				}
				if (!PT.equals(prefs.getString("pillsTaken", "pillstaken")
						.trim())) {

					params[0].addString("pillstaken", PT);
				} else {
					params[0].addString("pillstaken", "pillstaken");
				}
				if (!PL.equals(prefs.getString("pillsLeft", "pillsleft").trim())) {
					params[0].addString("pillsleft", PL);
				} else {
					params[0].addString("pillsleft", "pillsleft");
				}
				if (!Pho.equals(prefs.getString("phonenumber", "phonenumber")
						.trim())) {
					params[0].addString("phonenumber", Pho);
				} else {
					params[0].addString("phonenumber", "phonenumber");
				}

				params[0]
						.addString("idnumber", prefs.getString("idnumber", ""));
				return params[0].executePost();

			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;

		}

		@SuppressLint({ "NewApi", "InlinedApi" })
		@Override
		protected void onPostExecute(JSONArray result) {
			super.onPostExecute(result);
			pDialog.cancel();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							edit.setImageResource(R.drawable.edit);
						}
					}, 1000);

				}
			});
			try {
				if (result.getString(0).equals("0")) {

					new AlertDialog.Builder(Profile.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("Update")
							.setMessage("Nothing was updated.")
							.setPositiveButton("Try again",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				} else if (Integer.parseInt(result.getString(0).toString()) >= 1) {

					new AlertDialog.Builder(Profile.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("Updated successfully.")
							.setMessage(
									result.getString(0)
											+ " fields were updated")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();

				} else {

				}
			} catch (JSONException e) {

				e.printStackTrace();
				new AlertDialog.Builder(Profile.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("failed to Log in")
						.setMessage(
								"unable to connect to the database please check your internet")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			}

		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(Profile.this, HomeScreen.class));
		finish();
	}
}
