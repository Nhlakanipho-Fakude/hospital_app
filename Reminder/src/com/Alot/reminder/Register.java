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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	private EditText firstname, lastname, phonenumber, idnumber, vct, town,
			cPassword, password, username;
	private Button submit;
	private String gender = "";
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		firstname = (EditText) findViewById(R.id.FIRSTNAME);
		lastname = (EditText) findViewById(R.id.LASTNAME);
		phonenumber = (EditText) findViewById(R.id.PHONENUMBER);
		idnumber = (EditText) findViewById(R.id.IDNUMBER);
		vct = (EditText) findViewById(R.id.VCTCLINIC);
		town = (EditText) findViewById(R.id.TOWN);
		cPassword = (EditText) findViewById(R.id.CONFIRMPASSWORD);
		password = (EditText) findViewById(R.id.PASSWORD);
		username = (EditText) findViewById(R.id.USERNAME);
		submit = (Button) findViewById(R.id.SUBMIT);
		submit.setOnClickListener(new OnClickListener() {

			@SuppressLint({ "InlinedApi", "NewApi" })
			@Override
			public void onClick(View v) {
				submit.setBackgroundResource(R.drawable.button_pressed);
				if (CheckNetwork.isInternetAvailable(Register.this)) {
					if (registerUser()) {

						new register().execute(new ApiConnector(
								"http://alot.2fh.co/remminder/register.php"));
					} else {
						submit.setBackgroundResource(R.drawable.button);
					}
				} else {
					submit.setBackgroundResource(R.drawable.button);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Register.this, AlertDialog.THEME_HOLO_LIGHT);
					builder.setTitle("No Internet Connection");
					builder.setMessage("Enable your Mobile Data,Wifi Connection !");
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
								}

							});
					builder.show();
				}
			}
		});

	}

	private boolean registerUser() {
		SharedPreferences prefs = getSharedPreferences("profile", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		try {
			if (!TextUtils.isEmpty(cPassword.getText().toString().trim())
					&& !TextUtils.isEmpty(password.getText().toString().trim())
					&& !TextUtils
							.isEmpty(firstname.getText().toString().trim())
					&& !TextUtils.isEmpty(lastname.getText().toString().trim())
					&& !TextUtils.isEmpty(phonenumber.getText().toString()
							.trim())

					&& !TextUtils.isEmpty(town.getText().toString().trim())

					&& !TextUtils.isEmpty(vct.getText().toString().trim())

					&& !TextUtils.isEmpty(idnumber.getText().toString().trim())
					&& !TextUtils.isEmpty(username.getText().toString().trim())) {

				if (idnumber.getText().toString().length() == 13) {
					String value = idnumber.getText().toString()
							.substring(6, 7);
					if (Integer.parseInt(value) <= 4) {
						gender = "Female";
					} else {
						gender = "Male";
					}
					if (phonenumber.getText().toString().length() == 12
							|| phonenumber.getText().toString().length() < 12) {

						if (cPassword.getText().toString()
								.equals(password.getText().toString())) {
							editor.putString("password", password.getText()
									.toString());

							editor.putString("phonenumber", phonenumber
									.getText().toString());
							editor.putString("username", username.getText()
									.toString());
							editor.putString("idnumber", idnumber.getText()
									.toString());
							editor.putString("firstname", firstname.getText()
									.toString());
							editor.putString("lastname", lastname.getText()
									.toString());
							editor.putString("vct", vct.getText().toString());
							editor.putString("town", town.getText().toString());

							editor.commit();
							return true;

						} else {
							Toast.makeText(this, "Passwords do not match",
									Toast.LENGTH_LONG).show();
							return false;
						}

					} else {
						Toast.makeText(this, "Invalid phone number",
								Toast.LENGTH_LONG).show();
						return false;

					}

				} else {

					Toast.makeText(this, "Invalid Id number.",
							Toast.LENGTH_LONG).show();
					return false;
				}

			} else {
				if (TextUtils.isEmpty(cPassword.getText().toString().trim())) {
					cPassword.setError("Password cannot be empty");
				}
				if (TextUtils.isEmpty(firstname.getText().toString().trim())) {
					firstname.setError("firstname cannot be empty");
				}
				if (TextUtils.isEmpty(lastname.getText().toString().trim())) {
					lastname.setError("lastname cannot be empty");
				}
				if (TextUtils.isEmpty(vct.getText().toString().trim())) {
					vct.setError("VCT CLINIC cannot be empty");
				}
				if (TextUtils.isEmpty(town.getText().toString().trim())) {
					town.setError("town address cannot be empty");
				}
				if (TextUtils.isEmpty(phonenumber.getText().toString().trim())) {
					phonenumber.setError("phone number cannot be empty");
				}

				if (TextUtils.isEmpty(idnumber.getText().toString().trim())) {
					idnumber.setError("Id number cannot be empty");

				}

				if (TextUtils.isEmpty(password.getText().toString().trim())) {
					password.setError("Password cannot be empty");
				}
				if (TextUtils.isEmpty(username.getText().toString().trim())) {
					password.setError("username cannot be empty");
				}

				return false;

			}

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, e.toString() + "\n Invalid information.",
					Toast.LENGTH_LONG).show();

		}
		return false;
	}

	private class register extends AsyncTask<ApiConnector, Long, JSONArray> {
		public register() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Register.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("Please wait while loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				params[0]
						.addString("firstname", firstname.getText().toString());
				params[0].addString("password", password.getText().toString());
				params[0].addString("phonenumber", phonenumber.getText()
						.toString());
				params[0].addString("idnumber", idnumber.getText().toString());
				params[0].addString("lastname", lastname.getText().toString());
				params[0].addString("vct", vct.getText().toString());
				params[0].addString("town", town.getText().toString());
				params[0].addString("gender", gender);
				params[0].addString("username", username.getText().toString());
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
			try {
				if (result.getString(0).equals("111")) {
					new AlertDialog.Builder(Register.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("Registration")
							.setMessage("You have been registered successully")
							.setPositiveButton("Log in",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													Register.this,
													LogInScreen.class));
											finish();
										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				} else {
					new AlertDialog.Builder(Register.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("Registration failed")
							.setMessage(
									"unable to register you due to invalid information.")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													Register.this,
													HomeScreen.class));
										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new AlertDialog.Builder(Register.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("Registration failed")
						.setMessage(
								"unable to connect to server ,Please try again a bit later.")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										startActivity(new Intent(Register.this,
												HomeScreen.class));
									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			}

		}
	}

	@Override
	public void onBackPressed() {

		startActivity(new Intent(Register.this, LogInScreen.class));
		finish();
	}
}
