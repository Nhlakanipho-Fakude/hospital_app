package com.javapapers.androidalarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmActivity extends Activity {

	AlarmManager alarmManager;
	private PendingIntent pendingIntent;
	private TimePicker alarmTimePicker;
	private static AlarmActivity inst;
	private TextView alarmTextView;

	public static AlarmActivity instance() {
		return inst;
	}

	@Override
	public void onStart() {
		super.onStart();
		inst = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
		alarmTextView = (TextView) findViewById(R.id.alarmText);
		ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				onToggleClicked(buttonView);
				
			}
		});
	}

	public void onToggleClicked(View view) {
		if (((ToggleButton) view).isChecked()) {
			Log.d("MyActivity", "Alarm On");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
			calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
			Intent myIntent = new Intent(AlarmActivity.this,
					AlarmReceiver.class);
			pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0,
					myIntent, 0);
			alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
					pendingIntent);
			Toast.makeText(Alarm.this, "Alarm set !", Toast.LENGTH_LONG).show();
		} else {
			alarmManager.cancel(pendingIntent);
			setAlarmText("");
			Log.d("MyActivity", "Alarm Off");
			Toast.makeText(Alarm.this, "Alarm Off", Toast.LENGTH_LONG).show();
		}
	}

	public void setAlarmText(String alarmText) {
		alarmTextView.setText(alarmText);
	}
}
