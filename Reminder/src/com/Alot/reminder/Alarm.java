package com.Alot.reminder;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Alarm extends Activity {

	private ImageView back;
	private PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		back = (ImageView) findViewById(R.id.back);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back.setImageResource(R.drawable.clicked_back);
				startActivity(new Intent(Alarm.this, HomeScreen.class));
				finish();
			}
		});
		/* Retrieve a PendingIntent that will perform a broadcast */
		Intent alarmIntent = new Intent(Alarm.this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(Alarm.this, 0, alarmIntent,
				0);

		findViewById(R.id.startAlarm).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						start();
					}
				});

		findViewById(R.id.stopAlarm).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						cancel();
					}
				});

		findViewById(R.id.stopAlarmAt10).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startAt10();
					}
				});
	}

	@SuppressLint({ "NewApi", "InflateParams" })
	public void start() {
		LayoutInflater factory = LayoutInflater.from(Alarm.this);
		final View textEntryView = factory.inflate(R.layout.check, null);
		final EditText hour = (EditText) textEntryView.findViewById(R.id.hour);
		final EditText minute = (EditText) textEntryView
				.findViewById(R.id.minute);
		final EditText second = (EditText) textEntryView
				.findViewById(R.id.second);
		final Button back = (Button) textEntryView.findViewById(R.id.back);

		final AlertDialog.Builder alert = new AlertDialog.Builder(Alarm.this,
				AlertDialog.THEME_HOLO_LIGHT);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back.setBackgroundResource(R.drawable.clicked_back);
				startActivity(new Intent(Alarm.this, Alarm.class));
				finish();
			}
		});
		alert.setView(textEntryView)
				.setPositiveButton("Set Alarm",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
								/* Set the alarm to start at 10:30 AM */
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(System
										.currentTimeMillis());
								calendar.set(Calendar.HOUR_OF_DAY, Integer
										.parseInt(hour.getText().toString()));
								calendar.set(Calendar.MINUTE, Integer
										.parseInt(minute.getText().toString()));
								calendar.set(Calendar.SECOND, Integer
										.parseInt(second.getText().toString()));

								/* Repeating on every 20 minutes interval */
								manager.setRepeating(
										AlarmManager.RTC_WAKEUP,
										calendar.getTimeInMillis(),
										Integer.parseInt(hour.getText()
												.toString())
												+ Integer.parseInt(minute
														.getText().toString())
												+ Integer.parseInt(second
														.getText().toString()),
										pendingIntent);
								Toast.makeText(
										Alarm.this,
										"Alarm Set for "
												+ hour.getTag().toString()
												+ ":"
												+ minute.getTag().toString()
												+ ":"
												+ second.getText().toString(),
										Toast.LENGTH_SHORT).show();
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

	public void cancel() {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pendingIntent);
		Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
	}

	@SuppressLint({ "NewApi", "InflateParams" })
	public void startAt10() {

		LayoutInflater factory = LayoutInflater.from(Alarm.this);
		final View textEntryView = factory.inflate(R.layout.check, null);
		final EditText hour = (EditText) textEntryView.findViewById(R.id.hour);
		final EditText minute = (EditText) textEntryView
				.findViewById(R.id.minute);
		final EditText second = (EditText) textEntryView
				.findViewById(R.id.second);
		final Button back = (Button) textEntryView.findViewById(R.id.back);

		final AlertDialog.Builder alert = new AlertDialog.Builder(Alarm.this,
				AlertDialog.THEME_HOLO_LIGHT);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back.setBackgroundResource(R.drawable.clicked_back);
				startActivity(new Intent(Alarm.this, Alarm.class));
				finish();
			}
		});
		alert.setView(textEntryView)
				.setPositiveButton("Set Alarm",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
								/* Set the alarm to start at 10:30 AM */
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(System
										.currentTimeMillis());
								calendar.set(Calendar.HOUR_OF_DAY, Integer
										.parseInt(hour.getText().toString()));
								calendar.set(Calendar.MINUTE, Integer
										.parseInt(minute.getText().toString()));
								calendar.set(Calendar.SECOND, Integer
										.parseInt(second.getText().toString()));

								/* Repeating on every 20 minutes interval */
								manager.setRepeating(
										AlarmManager.RTC_WAKEUP,
										calendar.getTimeInMillis(),
										Integer.parseInt(hour.getText()
												.toString())
												+ Integer.parseInt(minute
														.getText().toString())
												+ Integer.parseInt(second
														.getText().toString()),
										pendingIntent);
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
}