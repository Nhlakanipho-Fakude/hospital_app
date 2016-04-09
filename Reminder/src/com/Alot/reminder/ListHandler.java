package com.Alot.reminder;

public class ListHandler {
	private String title, high;
	private int iconId;

	public ListHandler(String ti, String hi, int ic) {
		this.title = ti;
		this.high = hi;
		this.iconId = ic;
	}

	public int getIconId() {
		return this.iconId;
	}

	public String getTitle() {
		return this.title;
	}

	public String getHigh() {
		return this.high;
	}
}
