package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.AuditReportResponseDto;
import com.murphy.taskmgmt.reports.DownloadReportResponseDto;
import com.murphy.taskmgmt.reports.ReportFacadeLocal;
import com.murphy.taskmgmt.reports.ReportPayload;
import com.murphy.taskmgmt.service.interfaces.AuditFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/report", produces = "application/json")
public class AuditReportRest {

	@Autowired
	ReportFacadeLocal facadeLocal;

	@Autowired
	AuditFacadeLocal auditLocal;

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public DownloadReportResponseDto getReport(@RequestBody ReportPayload dto) {
		return facadeLocal.generateReport(dto);
	}
	
	 @RequestMapping(value = "/getAudit", method = RequestMethod.GET)
	 public AuditReportResponseDto getAudit() {
	 return auditLocal.getReport();
	 }
}
