package com.comp3111.achievement;

public class Achievement {
	public int id;
	public String name = "";
	public String type = "";
	public int threshold = 0;
	public String record = "";

	Achievement( String name, String type, int threshold) {

		this.name = name;
		this.type = type;
		this.threshold = threshold;
	}

	String ach_name() {
		return name;
	}

	public boolean issucceed() {
		if (record.equals(""))
			return false;
		else
			return true;
	}

}
