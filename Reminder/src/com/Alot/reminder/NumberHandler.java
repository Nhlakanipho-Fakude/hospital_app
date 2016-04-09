package com.Alot.reminder;

public class NumberHandler {
	private String id;
	public String fullname;

	public NumberHandler(String i, String f) {
		this.id = i;
		this.fullname = f;
	}

	public String getID() {
		return this.id;

	}

	public String getFullName() {
		return this.fullname;
	}
}
