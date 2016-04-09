package com.Alot.reminder;

import java.util.ArrayList;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends Activity {
	private ImageView hospital, about, profile, pills, appointments, exit,
			alarm;
	private ProgressDialog pDialog;
	private String ans = "NO", count = "0", value = "0";
	private TextView number;

	private int index = 0;
	private ArrayAdapter<AppointmentHandler> ADAPTER;
	private String[] DATE, TIME, HNAME;
	List<AppointmentHandler> myAppointments = new ArrayList<AppointmentHandler>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);
		hospital = (ImageView) findViewById(R.id.hospital);
		about = (ImageView) findViewById(R.id.about);
		profile = (ImageView) findViewById(R.id.profile);
		pills = (ImageView) findViewById(R.id.pills);
		appointments = (ImageView) findViewById(R.id.appointment);
		exit = (ImageView) findViewById(R.id.exit);
		alarm = (ImageView) findViewById(R.id.alarm);
		number = (TextView) findViewById(R.id.number);

		new aboutPills().execute(new ApiConnector(
				"http://alot.2fh.co/remminder/pills.php"));
		SharedPreferences prefs = getSharedPreferences("patient", MODE_PRIVATE);
		number.setText(prefs.getString("days", ""));

		hospital.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hospital.setBackgroundResource(R.drawable.clicked_location);
				startActivity(new Intent(HomeScreen.this, Clinic.class));
				finish();

			}
		});
		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				about.setBackgroundResource(R.drawable.clicked_about);
				startActivity(new Intent(HomeScreen.this, About.class));
				finish();

			}
		});
		profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				profile.setBackgroundResource(R.drawable.clicked_profile);
				startActivity(new Intent(HomeScreen.this, Profile.class));
				finish();
			}
		});

		pills.setOnClickListener(new OnClickListener() {

			@SuppressLint({ "InflateParams", "NewApi" })
			@Override
			public void onClick(View v) {
				pills.setBackgroundResource(R.drawable.clicked_pills);
				LayoutInflater factory = LayoutInflater.from(HomeScreen.this);

				final View textEntryView = factory.inflate(
						R.layout.pills_layout, null);

				final RadioGroup answer = (RadioGroup) textEntryView
						.findViewById(R.id.answer);
				final TextView number = (TextView) textEntryView
						.findViewById(R.id.HIGH);
				final ImageView back = (ImageView) textEntryView
						.findViewById(R.id.back);
				final ImageView exit = (ImageView) textEntryView
						.findViewById(R.id.exit_button);
				back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						back.setBackgroundResource(R.drawable.clicked_back);
						startActivity(new Intent(HomeScreen.this,
								HomeScreen.class));
						finish();
					}
				});
				exit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						exit.setBackgroundResource(R.drawable.clicked_exit);
						finish();
					}
				});

				if (TextUtils.isDigitsOnly(count)) {
					if (count.equals("0")) {
						number.setText("");
					} else {
						number.setText(count);
					}
				} else {
					number.setText("");
				}
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						HomeScreen.this, AlertDialog.THEME_HOLO_LIGHT);

				alert.setView(textEntryView)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										if (answer.getCheckedRadioButtonId() == R.id.yes)
											ans = "YES";
										else

											ans = "NO";
										value = "1";
										new aboutPills()
												.execute(new ApiConnector(
														"http://alot.2fh.co/remminder/pills.php"));
									}
								})

						.show();
			}
		});
		appointments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				appointments.setBackgroundResource(R.drawable.clicked_appoint);
				new appointMents().execute(new ApiConnector(
						"http://alot.2fh.co/remminder/appointments.php"));
			}
		});
		alarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alarm.setBackgroundResource(R.drawable.clicked_alarm);
				startActivity(new Intent(HomeScreen.this, Alarm.class));
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
					Toast.makeText(getApplicationContext(), "Logging out...",
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	private class aboutPills extends AsyncTask<ApiConnector, Long, JSONArray> {
		public aboutPills() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(HomeScreen.this,
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

				params[0].addString("idnumber",
						prefs.getString("idnumber", null));
				params[0].addString("answer", ans);
				params[0].addString("value", value);
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
					new AlertDialog.Builder(HomeScreen.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("Pills")
							.setMessage(
									"You do not have any pills left please order your ARVs.")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				} else if (result.getString(0).equals("222")) {
					new AlertDialog.Builder(HomeScreen.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("Warning")
							.setMessage(
									"You must take your medication everytime you are remminded to.")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				} else if (result.getString(0).equals("555")) {
					// nothing is shown
				} else {
					count = result.getString(0);
					SharedPreferences prefs = getSharedPreferences("patient",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("count", count);
					editor.commit();
				}
			} catch (JSONException e) {

				e.printStackTrace();
				new AlertDialog.Builder(HomeScreen.this,
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

	public void populateListView(ListView list) {

		ArrayAdapter<AppointmentHandler> adapter = new MyListAdapter();
		ADAPTER = adapter;
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				aboutAppoint(position);
			}
		});

	}

	private class MyListAdapter extends ArrayAdapter<AppointmentHandler> {
		public MyListAdapter() {
			super(HomeScreen.this, R.layout.appoint_layout_list, myAppointments);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.appoint_layout_list, parent, false);
			}

			AppointmentHandler handler = myAppointments.get(position);
			TextView days = (TextView) itemView.findViewById(R.id.TITLE);
			days.setText(handler.getDate());
			TextView hospitalName = (TextView) itemView
					.findViewById(R.id.wapsite);
			hospitalName.setText(handler.getHName());
			TextView time = (TextView) itemView.findViewById(R.id.time);
			time.setText(handler.getTime());
			return itemView;

		}
	}

	private class appointMents extends AsyncTask<ApiConnector, Long, JSONArray> {
		public appointMents() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(HomeScreen.this,
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

				params[0].addString("idnumber",
						prefs.getString("idnumber", null));
				return params[0].executePost();

			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;

		}

		@SuppressLint({ "NewApi", "InlinedApi", "InflateParams" })
		@Override
		protected void onPostExecute(JSONArray result) {
			super.onPostExecute(result);
			pDialog.cancel();
			boolean isk = false;
			String date, time, Hname;
			try {
				if (result.getString(0).equals("111")) {

				} else {
					isk = true;

					DATE = new String[result.length()];
					TIME = new String[result.length()];
					HNAME = new String[result.length()];

					for (int i = 0; i < result.length(); i++) {
						date = result.getJSONArray(i).getString(0);
						time = result.getJSONArray(i).getString(2);
						Hname = result.getJSONArray(i).getString(1);
						myAppointments.add(new AppointmentHandler(date, time,
								Hname));
						DATE[i] = date;
						TIME[i] = time;
						HNAME[i] = Hname;
					}
				}
				if (isk) {
					LayoutInflater factory = LayoutInflater
							.from(HomeScreen.this);

					final View textEntryView = factory.inflate(
							R.layout.appointments_layout, null);
					final ListView Listv = (ListView) textEntryView
							.findViewById(R.id.allPointments);
					populateListView(Listv);

					final AlertDialog.Builder alert = new AlertDialog.Builder(
							HomeScreen.this, AlertDialog.THEME_HOLO_LIGHT);
					alert.setView(textEntryView).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									ADAPTER.clear();
									ADAPTER.notifyDataSetChanged();
								}
							});
					alert.show();
				} else {
					LayoutInflater factory = LayoutInflater
							.from(HomeScreen.this);
					final View textEntryView = factory.inflate(
							R.layout.appointments_layout, null);

					final TextView no = (TextView) textEntryView
							.findViewById(R.id.wapsite);
					no.setText("No appointment");

					final AlertDialog.Builder alert = new AlertDialog.Builder(
							HomeScreen.this, AlertDialog.THEME_HOLO_LIGHT);

					alert.setView(textEntryView).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							});
					alert.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				new AlertDialog.Builder(HomeScreen.this,
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

	@SuppressLint({ "NewApi", "InflateParams", "InlinedApi" })
	public void aboutAppoint(int position) {
		LayoutInflater factory = LayoutInflater.from(HomeScreen.this);

		final View textEntryView = factory
				.inflate(R.layout.about_appoint, null);
		final EditText medical_centre = (EditText) textEntryView
				.findViewById(R.id.medical_centre);
		final EditText location = (EditText) textEntryView
				.findViewById(R.id.text_location);
		final EditText nurse = (EditText) textEntryView
				.findViewById(R.id.nurse);
		final EditText doctor = (EditText) textEntryView
				.findViewById(R.id.doctor);
		final EditText arrival = (EditText) textEntryView
				.findViewById(R.id.arrival);
		final EditText text_date = (EditText) textEntryView
				.findViewById(R.id.text_date);
		final EditText daysLeft = (EditText) textEntryView
				.findViewById(R.id.daysLeft);
		final TextView header = (TextView) textEntryView
				.findViewById(R.id.wapsite);
		header.setText(HNAME[position]);
		medical_centre.setText(HNAME[position]);
		text_date.setText(DATE[position]);
		arrival.setText(TIME[position]);
		location.setText("Empangeni");
		nurse.setText("Miss Khoza");
		doctor.setText("Dr Smith");
		SharedPreferences prefs = getSharedPreferences("patient", MODE_PRIVATE);
		daysLeft.setText(prefs.getString("days", ""));
		final AlertDialog.Builder alert = new AlertDialog.Builder(
				HomeScreen.this, AlertDialog.THEME_HOLO_LIGHT);

		alert.setView(textEntryView).show();
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(),
				" Us the exit button to log out", Toast.LENGTH_SHORT).show();
	}
}