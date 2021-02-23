package com.murphy.taskmgmt.reports;

public interface ReportFacadeLocal {

	DownloadReportResponseDto generateReport(ReportPayload payload);

}
