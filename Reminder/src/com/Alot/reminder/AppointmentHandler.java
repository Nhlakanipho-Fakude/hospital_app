package com.Alot.reminder;

public class AppointmentHandler {
	private String date, time, hospitalName;

	public AppointmentHandler(String date, String time, String hospitalName) {
		this.date = date;
		this.time = time;
		this.hospitalName = hospitalName;

	}

	public String getDate() {

		return this.date;
	}

	public String getTime() {
		return this.time;
	}

	public String getHName() {
		return this.hospitalName;
	}

}
