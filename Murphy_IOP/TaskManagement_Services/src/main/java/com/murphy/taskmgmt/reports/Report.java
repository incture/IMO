package com.murphy.taskmgmt.reports;

import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.DropDownEditorDao;
import com.murphy.taskmgmt.dao.OBXSchedulerDao;
import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dao.ShiftAuditLogDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.service.DGPFacade;


public interface Report {
	
	public PMCReportBaseDto getData(ReportPayload payload,AuditDao auditDao);
	
	public PMCReportBaseDto getData(ReportPayload payload,ShiftAuditLogDao shiftAuditLogDao); 
	
	public PMCReportBaseDto getData(ReportPayload payload,DGPFacade dgpFacade);

	public PMCReportBaseDto getData(ReportPayload payload, OBXSchedulerDao obxDao);
	
	public PMCReportBaseDto getData(ReportPayload payload, DropDownEditorDao dropDownDao);
	
	public PMCReportBaseDto getData(ReportPayload payload, TaskEventsDao taskEventsDao);
}
