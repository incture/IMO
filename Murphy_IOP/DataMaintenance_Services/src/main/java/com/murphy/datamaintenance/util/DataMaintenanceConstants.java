package com.murphy.datamaintenance.util;

public enum DataMaintenanceConstants {
	Scenario("FRAC_SCENARIO_LOOK_UP", "FRAC - Scenario"), 
	Orientation("FRAC_ORIENTATION", "FRAC - Orientation"), 
	Zone("FRAC_ZONE","FRAC - Zone"), 
	WellStatus("FRAC_WELL_STATUS", "FRAC - Well Status"),
	ParentCode("DT_WELL_PARENT_CODE","Well Downtime - Parent Code"),
	ChildCode("DT_WELL_CHILD_CODE","Well Downtime - Child Code"),
	DowntimeCode("COMPRESSOR_DOWNTIME","Compressor - Downtime code"),
	FlareCode("FLARECODE","Flare - Downtime code"),
	ReasonForEnergyIsolation("EI_REASON","Reason For Energy Isolation"),
	ReasonForByPass("REASON_FOR_BYPASS","Reason For ByPass"),
	BypassDeviceList("DEVICE","ByPass Device List"),
	RootcauseList("TM_ROOTCAUSE_MASTER","Rootcause List"),
	LocationCoordinate("LOCATION_COORDINATE","Location - Coordinate"),
	WellTier("WELL_TIER","Well - Tier");
	
	// WellStatus ( "DM_TEST","Well Status");

	private String tableName;
	private String fileName;

	DataMaintenanceConstants(String tableName, String fileName) {
		this.setTableName(tableName);
		this.setFileName(fileName);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
