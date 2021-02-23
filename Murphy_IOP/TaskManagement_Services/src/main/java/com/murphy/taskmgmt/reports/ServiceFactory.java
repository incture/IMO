package com.murphy.taskmgmt.reports;

public abstract class ServiceFactory {
	
	
	public abstract Report getReport(String reportName);

	public abstract File getFile(String fileFormate);
}
