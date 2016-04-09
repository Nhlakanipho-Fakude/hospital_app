package com.Alot.reminder;

public class SearchHandler {
	private String firstname, lastname;
	private String location;

	public SearchHandler(String firstname, String lastname, String location) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.location = location;
	}

	public String getFullName() {
		return this.firstname + " " + this.lastname;
	}

	public String getLocation() {

		return this.location;
	}
}
