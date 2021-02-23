package com.murphy.taskmgmt.reports;

public class DownloadReportFactoryGenerator {

	public static ServiceFactory getReportFactory() {
		return new ReportFactory();
	}

	public static ServiceFactory getDownloadFactory() {
		return new FileFactory();
	}
}
