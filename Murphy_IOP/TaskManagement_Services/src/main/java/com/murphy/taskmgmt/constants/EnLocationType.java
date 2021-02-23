package com.murphy.taskmgmt.constants;

public enum EnLocationType {

	WELL("Well"),

	WELL_PAD("Well Pad"),

	FACILITY("Facility"),

	FIELD("Field"),

	CENTRAL_FACILITY("Central Facility"),

	BASE("Base");

	String value;

	EnLocationType(String value) {
		this.value = value;
	}

	public String getvalue() {
		return value;
	}
	
	
	public static EnLocationType get(String value) {
		for (EnLocationType en : values()) {
			if (en.getvalue().equals(value)) {
				return en;
			}
		}
		return null;
	}

}
