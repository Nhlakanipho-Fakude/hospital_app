package com.Alot.reminder;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LogInScreen extends Activity {
	private EditText username, password;
	private Button login, register;
	private TextView forgot;
	private ProgressDialog pDialog;
	private String pass, user;
	private String id, f_P, f_U;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in_screen);
		username = (EditText) findViewById(R.id.searchBox);
		password = (EditText) findViewById(R.id.surname);
		login = (Button) findViewById(R.id.Searching);
		register = (Button) findViewById(R.id.pills);
		forgot = (TextView) findViewById(R.id.forgot);
		login.setOnClickListener(new OnClickListener() {
			@SuppressLint({ "NewApi", "InlinedApi" })
			@Override
			public void onClick(View v) {
				login.setBackgroundResource(R.drawable.clicked_button);
				if (CheckNetwork.isInternetAvailable(LogInScreen.this)) {
					logIn();
				} else {
					login.setBackgroundResource(R.drawable.button);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							LogInScreen.this, AlertDialog.THEME_HOLO_LIGHT);
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

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				register.setBackgroundResource(R.drawable.clicked_button);
				startActivity(new Intent(LogInScreen.this, Register.class));
				finish();
			}
		});
		forgot.setOnClickListener(new OnClickListener() {

			@SuppressLint({ "InflateParams", "NewApi", "InlinedApi" })
			@Override
			public void onClick(View v) {
				forgot.setTextColor(Color.parseColor("#006666"));
				LayoutInflater factory = LayoutInflater.from(LogInScreen.this);

				final View textEntryView = factory.inflate(R.layout.forgot,
						null);

				final EditText id = (EditText) textEntryView
						.findViewById(R.id.f_id);
				final EditText user = (EditText) textEntryView
						.findViewById(R.id.f_username);
				final EditText newP = (EditText) textEntryView
						.findViewById(R.id.f_newP);
				final EditText pass = (EditText) textEntryView
						.findViewById(R.id.f_confirm);
				final ImageView back = (ImageView) textEntryView
						.findViewById(R.id.back);

				back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						back.setBackgroundResource(R.drawable.clicked_back);
						startActivity(new Intent(LogInScreen.this,
								LogInScreen.class));
						finish();
					}
				});

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						LogInScreen.this, AlertDialog.THEME_HOLO_LIGHT);

				alert.setView(textEntryView)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										if ((TextUtils.isDigitsOnly(id
												.getText().toString()) && !TextUtils
												.isEmpty(id.getText()
														.toString()))
												&& ((id.length() == 13 && newP
														.getText()
														.toString()
														.equals(pass.getText()
																.toString())) && !TextUtils
														.isEmpty(user.getText()
																.toString()))) {
											setValues(id.getText().toString(),
													pass.getText().toString(),
													user.getText().toString());
											new forgotPassword()
													.execute(new ApiConnector(
															"http://alot.2fh.co/remminder/forgot.php"));
										} else {
											Toast.makeText(
													getApplicationContext(),
													"Incorrect details",
													Toast.LENGTH_LONG).show();
											forgot.setTextColor(Color
													.parseColor("#ff0000"));
										}
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										forgot.setTextColor(Color
												.parseColor("#ff0000"));
									}
								});

				alert.show();
			}
		});
	}

	private class getPatientInfo extends
			AsyncTask<ApiConnector, Long, JSONArray> {
		public getPatientInfo() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(LogInScreen.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("Please wait while loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {

				params[0].addString("password", pass);
				params[0].addString("username", user);
				params[0].addString("value", "0");
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
					login.setBackgroundResource(R.drawable.button);
					new AlertDialog.Builder(LogInScreen.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("failed to Log in")
							.setMessage("Invalid log in details")
							.setPositiveButton("Try again",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				} else if (result.getString(0).equals("222")) {
					login.setBackgroundResource(R.drawable.button);
					new AlertDialog.Builder(LogInScreen.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("failed to Log in")
							.setMessage(
									"unable to connect to the database please check your internet")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();

				} else {

					SharedPreferences prefs = getSharedPreferences("patient",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("firstname", result.getJSONArray(0)
							.getString(0).toString());
					editor.putString("lastname", result.getJSONArray(0)
							.getString(1).toString());
					editor.putString("gender", result.getJSONArray(0)
							.getString(2).toString());
					editor.putString("idnumber", result.getJSONArray(0)
							.getString(3).toString());
					editor.putString("phonenumber", result.getJSONArray(0)
							.getString(4).toString());

					editor.putString("location", result.getJSONArray(0)
							.getString(5).toString());

					editor.putString("days", result.getJSONArray(0)
							.getJSONArray(6).getString(4));
					editor.putString("pillsTaken", result.getJSONArray(0)
							.getJSONArray(6).getString(1));
					editor.putString("diagnosed", result.getJSONArray(0)
							.getJSONArray(6).getString(0));
					editor.putString("pillsLeft", result.getJSONArray(0)
							.getJSONArray(6).getString(2));
					editor.putString("totalPills", result.getJSONArray(0)
							.getJSONArray(6).getString(3));

					editor.commit();
					startActivity(new Intent(LogInScreen.this, HomeScreen.class));
					finish();
				}
			} catch (Exception e) {
				login.setBackgroundResource(R.drawable.button);
				// TODO Auto-generated catch block
				e.printStackTrace();
				new AlertDialog.Builder(LogInScreen.this,
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

	public void logIn() {
		if (!(TextUtils.isEmpty(username.getText().toString().trim()) && TextUtils
				.isEmpty(password.getText().toString().trim()))) {
			pass = password.getText().toString().trim();
			user = username.getText().toString().trim();
			if (user.equals("administrator") && pass.equals("d3d4")) {
				startActivity(new Intent(LogInScreen.this, Administrator.class));
				finish();
			} else {
				new getPatientInfo().execute(new ApiConnector(
						"http://alot.2fh.co/remminder/login.php"));
			}

		} else {
			if (TextUtils.isEmpty(username.getText().toString()))
				username.setError("Please enter the username !");
			if (TextUtils.isEmpty(password.getText().toString()))
				password.setError("Please enter the password");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							login.setBackgroundResource(R.drawable.button);
						}
					}, 500);
				}
			});

		}
	}

	@Override
	public void onBackPressed() {

		finish();
	}

	private class forgotPassword extends
			AsyncTask<ApiConnector, Long, JSONArray> {
		public forgotPassword() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(LogInScreen.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				params[0].addString("idnumber", id);
				params[0].addString("username", f_U);
				params[0].addString("password", f_P);

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
				new AlertDialog.Builder(LogInScreen.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("Forgot password")
						.setMessage(result.getString(0))
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void setValues(String id, String pass, String user) {
		this.f_P = pass;
		this.f_U = user;
		this.id = id;
	}
}
