package com.Alot.reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Administrator extends Activity {
	private EditText searchBox;
	private ImageView set_app, delete, contact, collect;
	private Button exit, search;
	List<SearchHandler> results = new ArrayList<SearchHandler>();
	private String VALUE, Message, Arrival, Center, Date, Doctor, Location;
	private ProgressDialog pDialog;
	private int this_search = 0;
	private boolean isok = false, k;
	private int this_value = 0;
	private TextView numberOfUsers;
	private String Total, Left, ID;
	private String[] idNumbers;
	List<NumberHandler> needs = new ArrayList<NumberHandler>();
	private ArrayAdapter<NumberHandler> ADAPTER;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrator);
		set_app = (ImageView) findViewById(R.id.set_ap);
		search = (Button) findViewById(R.id.Searching);
		delete = (ImageView) findViewById(R.id.delete);
		contact = (ImageView) findViewById(R.id.contact);
		collect = (ImageView) findViewById(R.id.c_pills);
		exit = (Button) findViewById(R.id.exit_admin);
		searchBox = (EditText) findViewById(R.id.searchBox);
		numberOfUsers = (TextView) findViewById(R.id.needPills);
		new needPills().execute(new ApiConnector(
				"http://alot.2fh.co/remminder/needPills.php"));
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search.setBackgroundResource(R.drawable.clicked_search);
				if (TextUtils.isEmpty(searchBox.getText().toString().trim())) {
					searchBox.setError("Enter anything to search !");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									search.setBackgroundResource(R.drawable.search);
								}
							}, 1000);
						}
					});
				} else {
					// go on and search all around the application.
					VALUE = searchBox.getText().toString().trim();
					new search().execute(new ApiConnector(
							"http://alot.2fh.co/remminder/search.php"));

				}

			}
		});
		contact.setOnClickListener(new OnClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater
						.from(Administrator.this);
				final View textEntryView = factory
						.inflate(R.layout.check, null);
				final EditText value = (EditText) textEntryView
						.findViewById(R.id.value);

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						Administrator.this, AlertDialog.THEME_HOLO_LIGHT);
				alert.setView(textEntryView).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								VALUE = value.getText().toString();
								new getPatientInfo()
										.execute(new ApiConnector(
												"http://alot.2fh.co/remminder/login.php"));
							}
						});
				alert.show();

			}
		});
		collect.setOnClickListener(new OnClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				if (k == true) {
					LayoutInflater factory = LayoutInflater
							.from(Administrator.this);

					final View textEntryView = factory.inflate(
							R.layout.needs_list, null);
					final ListView Listv = (ListView) textEntryView
							.findViewById(R.id.needs_list);
					populateListView(Listv);

					final AlertDialog.Builder alert = new AlertDialog.Builder(
							Administrator.this, AlertDialog.THEME_HOLO_LIGHT);
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
					new AlertDialog.Builder(Administrator.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("message")
							.setMessage("no one needs any pills ")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				}
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exit.setBackgroundResource(R.drawable.clicked_exit_admin);
				Toast.makeText(getApplicationContext(), "GoodBye",
						Toast.LENGTH_LONG).show();
				finish();

			}
		});
		set_app.setOnClickListener(new OnClickListener() {

			@SuppressLint({ "InflateParams", "InlinedApi", "NewApi" })
			@Override
			public void onClick(View v) {

				LayoutInflater factory = LayoutInflater
						.from(Administrator.this);
				final View textEntryView = factory
						.inflate(R.layout.check, null);
				final EditText value = (EditText) textEntryView
						.findViewById(R.id.value);

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						Administrator.this, AlertDialog.THEME_HOLO_LIGHT);
				alert.setView(textEntryView).setPositiveButton("CHECK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								VALUE = value.getText().toString();
								if (checkPatient(VALUE) == true) {
									new setAppointment()
											.execute(new ApiConnector(
													"http://alot.2fh.co/remminder/setAppointment.php"));
								} else {
									new AlertDialog.Builder(Administrator.this,
											AlertDialog.THEME_HOLO_LIGHT)
											.setTitle(
													"failed to set an Appointment")
											.setMessage(
													"no patient was found to be registered with "
															+ VALUE)
											.setPositiveButton(
													"OK",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {

														}
													})
											.setIcon(
													android.R.drawable.ic_dialog_alert)
											.show();
								}
							}
						});
				alert.show();

			}
		});
		delete.setOnClickListener(new OnClickListener() {

			@SuppressLint({ "InflateParams", "InlinedApi" })
			@Override
			public void onClick(View v) {
				this_value = 1;
				LayoutInflater factory = LayoutInflater
						.from(Administrator.this);
				final View textEntryView = factory
						.inflate(R.layout.check, null);
				final EditText value = (EditText) textEntryView
						.findViewById(R.id.value);

				final AlertDialog.Builder alert = new AlertDialog.Builder(
						Administrator.this, AlertDialog.THEME_HOLO_LIGHT);
				alert.setView(textEntryView).setPositiveButton("check",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								VALUE = value.getText().toString();
								if (checkPatient(VALUE) == true) {
									new setAppointment()
											.execute(new ApiConnector(
													"http://alot.2fh.co/remminder/setAppointment.php"));
								} else {
									new AlertDialog.Builder(Administrator.this,
											AlertDialog.THEME_HOLO_LIGHT)
											.setTitle(
													"failed to set an Appointment")
											.setMessage(
													"no patient was found to be registered with "
															+ VALUE)
											.setPositiveButton(
													"OK",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {

														}
													})
											.setIcon(
													android.R.drawable.ic_dialog_alert)
											.show();
								}
							}
						});
				alert.show();

			}
		});

	}

	private class search extends AsyncTask<ApiConnector, Long, JSONArray> {
		public search() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Administrator.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("searching...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				params[0].addString("value", VALUE);
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

			String firstname, lastname, location;
			try {
				if (result.getString(0).equals("111")) {

				} else {
					isok = true;
					search.setBackgroundResource(R.drawable.search);

					for (int i = 0; i < result.length(); i++) {
						firstname = result.getJSONArray(i).getString(0);
						location = result.getJSONArray(i).getString(2);
						lastname = result.getJSONArray(i).getString(1);
						results.add(new SearchHandler(firstname, lastname,
								location));
					}
				}
				if (this_search == 0) {
					if (isok) {
						LayoutInflater factory = LayoutInflater
								.from(Administrator.this);

						final View textEntryView = factory.inflate(
								R.layout.search_results, null);
						final ListView Listv = (ListView) textEntryView
								.findViewById(R.id.results);
						if (isok) {
							populateSearchList(Listv);
						} else {
							Listv.setBackgroundResource(R.drawable.no_appoint);
						}

						final AlertDialog.Builder alert = new AlertDialog.Builder(
								Administrator.this,
								AlertDialog.THEME_HOLO_LIGHT);
						alert.setView(textEntryView).setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {

									}
								});
						alert.show();
					} else {
						LayoutInflater factory = LayoutInflater
								.from(Administrator.this);

						final View textEntryView = factory.inflate(
								R.layout.search_results, null);
						final TextView Listv = (TextView) textEntryView
								.findViewById(R.id.wapsite);
						Listv.setText("No results found !");
						final AlertDialog.Builder alert = new AlertDialog.Builder(
								Administrator.this,
								AlertDialog.THEME_HOLO_LIGHT);
						alert.setView(textEntryView).setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								});
						alert.show();
					}
				}
			} catch (Exception e) {
				search.setBackgroundResource(R.drawable.search);
				e.printStackTrace();
				new AlertDialog.Builder(Administrator.this,
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

	public void populateSearchList(ListView list) {

		ArrayAdapter<SearchHandler> this_adapter = new MySearchAdapter();

		list.setAdapter(this_adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

	}

	private class MySearchAdapter extends ArrayAdapter<SearchHandler> {
		public MySearchAdapter() {
			super(Administrator.this, R.layout.search_layout, results);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.search_layout,
						parent, false);
			}

			SearchHandler handler = results.get(position);
			TextView fullname = (TextView) itemView.findViewById(R.id.fullname);
			fullname.setText(handler.getFullName());
			TextView location = (TextView) itemView.findViewById(R.id.local);
			location.setText(handler.getLocation());

			return itemView;

		}
	}

	@SuppressLint({ "NewApi", "InflateParams", "InlinedApi", "SimpleDateFormat" })
	public void aboutAppoint(int position) {
		LayoutInflater factory = LayoutInflater.from(Administrator.this);

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

		Message = nurse.getText().toString();
		Arrival = arrival.getText().toString();
		Center = medical_centre.getText().toString();
		Date = text_date.getText().toString();
		Doctor = doctor.getText().toString();
		Location = location.getText().toString();
		header.setText(Center);
		String date = new SimpleDateFormat("dd").format(new Date());
		daysLeft.setText((Integer.parseInt(date) - Integer.parseInt(Date
				.substring(0, 1).toString())) + "");

		final AlertDialog.Builder alert = new AlertDialog.Builder(
				Administrator.this, AlertDialog.THEME_HOLO_LIGHT);

		alert.setView(textEntryView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if ((TextUtils.isEmpty(Location) || TextUtils
								.isEmpty(Message))
								|| ((TextUtils.isEmpty(Arrival) || TextUtils
										.isEmpty(Center)) || TextUtils
										.isEmpty(Date))) {
							new AlertDialog.Builder(Administrator.this,
									AlertDialog.THEME_HOLO_LIGHT)
									.setTitle("failed to set appoint")
									.setMessage(
											"unable to set an appoint due to invalid information,please try again.")
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {

												}
											})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();
						} else {

						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
		alert.show();
	}

	private class setAppointment extends
			AsyncTask<ApiConnector, Long, JSONArray> {
		public setAppointment() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Administrator.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("setting appointment...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				params[0].addString("idnumber", VALUE);
				params[0].addString("doctor", Doctor);
				params[0].addString("hosptital", Center);
				params[0].addString("date", Date);
				params[0].addString("message", Message);
				params[0].addString("time", Arrival);

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
			try {
				new AlertDialog.Builder(Administrator.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("Results")
						.setMessage(result.getString(0).toString())
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			} catch (Exception e) {
				search.setBackgroundResource(R.drawable.search);
				e.printStackTrace();
				new AlertDialog.Builder(Administrator.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("failed")
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

	public boolean checkPatient(String value) {
		VALUE = value;
		this_search = 1;
		if (TextUtils.isEmpty(VALUE)) {
			Toast.makeText(Administrator.this, "Invalid patient information",
					Toast.LENGTH_LONG).show();
		} else {
			if (this_value == 0) {
				new search().execute(new ApiConnector(
						"http://alot.2fh.co/remminder/search.php"));
				return isok;
			} else {
				new deletePatient().execute(new ApiConnector(
						"http://alot.2fh.co/remminder/deletePatient.php"));
				return isok;
			}
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(),
				" Us the exit button to log out", Toast.LENGTH_SHORT).show();
	}

	private class deletePatient extends
			AsyncTask<ApiConnector, Long, JSONArray> {
		public deletePatient() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Administrator.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("deleting...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				params[0].addString("idnumber", VALUE);
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
			try {
				new AlertDialog.Builder(Administrator.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("Results")
						.setMessage(result.getString(0).toString())
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			} catch (Exception e) {

				e.printStackTrace();
				new AlertDialog.Builder(Administrator.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("failed")
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

	private class needPills extends AsyncTask<ApiConnector, Long, JSONArray> {
		public needPills() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Administrator.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				params[0].addString("idnumber", "0");
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
			k = false;
			try {
				if (Integer.parseInt(result.getJSONArray(0).getString(0)) > 0) {
					k = true;
					idNumbers = new String[result.length()];
					for (int i = 0; i < result.length(); i++) {
						idNumbers[i] = result.getJSONArray(i).getString(1)
								.toString();
						needs.add(new NumberHandler(result.getJSONArray(i)
								.getString(1).toString(), result
								.getJSONArray(i).getString(2).toString()));
						idNumbers[i] = result.getJSONArray(i).getString(1)
								.toString();
					}
					numberOfUsers.setText(result.getJSONArray(0).getString(0));
				} else {
					numberOfUsers.setText("");
				}

			} catch (Exception e) {

				e.printStackTrace();
				new AlertDialog.Builder(Administrator.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("failed")
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

		ArrayAdapter<NumberHandler> adapter = new MyListAdapter();
		list.setAdapter(adapter);
		ADAPTER = adapter;
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				addingPills(position);
			}
		});

	}

	private class MyListAdapter extends ArrayAdapter<NumberHandler> {
		public MyListAdapter() {
			super(Administrator.this, R.layout.need_layout, needs);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.need_layout,
						parent, false);
			}

			NumberHandler handler = needs.get(position);
			TextView idNumber = (TextView) itemView.findViewById(R.id.idNumber);
			idNumber.setText(handler.getID());
			TextView fullName = (TextView) itemView.findViewById(R.id.fullName);
			fullName.setText(handler.getFullName());

			return itemView;

		}
	}

	@SuppressLint("InflateParams")
	public void addingPills(final int position) {
		LayoutInflater factory = LayoutInflater.from(Administrator.this);

		final View textEntryView = factory
				.inflate(R.layout.collect_pills, null);
		final EditText total = (EditText) textEntryView
				.findViewById(R.id.TOTAL);
		final EditText left = (EditText) textEntryView.findViewById(R.id.LEFT);

		final AlertDialog.Builder alert = new AlertDialog.Builder(
				Administrator.this, AlertDialog.THEME_HOLO_LIGHT);

		alert.setView(textEntryView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if ((!TextUtils.isEmpty(left.getText().toString()) && Integer
								.parseInt(left.getText().toString()) <= 30)
								&& (!TextUtils.isEmpty(total.getText()
										.toString()) && Integer.parseInt(left
										.getText().toString()) <= 30)) {
							Total = total.getText().toString();
							Left = left.getText().toString();
							ID = idNumbers[position];
							new addThemPills()
									.execute(new ApiConnector(
											"http://alot.2fh.co/remminder/addPills.php"));
						}
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)

				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
		alert.show();
	}

	private class addThemPills extends AsyncTask<ApiConnector, Long, JSONArray> {
		public addThemPills() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Administrator.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {
				params[0].addString("pillsLeft", Left);
				params[0].addString("totalPills", Left);
				params[0].addString("idnumber", ID);
				params[0].addString("days", Total);
				params[0].addString("pillsTaken", Total);
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
			try {

				new AlertDialog.Builder(Administrator.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("Results")
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
				new AlertDialog.Builder(Administrator.this,
						AlertDialog.THEME_HOLO_LIGHT)
						.setTitle("failed")
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

	private class getPatientInfo extends
			AsyncTask<ApiConnector, Long, JSONArray> {
		public getPatientInfo() {
			super();
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(Administrator.this,
					ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("Please wait while loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {

			try {

				params[0].addString("value", "11");
				params[0].addString("idnumber", VALUE);
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

					new AlertDialog.Builder(Administrator.this,
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

					new AlertDialog.Builder(Administrator.this,
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
					startActivity(new Intent(Administrator.this, Profile.class));
					finish();
				}
			} catch (Exception e) {

				e.printStackTrace();
				new AlertDialog.Builder(Administrator.this,
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

}
