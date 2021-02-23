package com.murphy.taskmgmt.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.DropDownEditorDao;
import com.murphy.taskmgmt.dao.OBXSchedulerDao;
import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dao.ShiftAuditLogDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.dto.AuditReportResponseDto;
import com.murphy.taskmgmt.dto.DOPVarianceReportResponseDto;
import com.murphy.taskmgmt.dto.DropDownReportResponseDto;
import com.murphy.taskmgmt.dto.IOPAdminReportResponseDto;
import com.murphy.taskmgmt.dto.OBXReportResponseDto;
import com.murphy.taskmgmt.dto.ShiftAuditReportResponseDto;
import com.murphy.taskmgmt.service.DGPFacade;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;


public class TaskManagerReport implements Report {

	@Override
	public PMCReportBaseDto getData(ReportPayload payload,AuditDao auditDao) {
		AuditReportResponseDto response = new AuditReportResponseDto();
		if (!ServicesUtil.isEmpty(payload)) {
			response.setTasks(auditDao.getReport());
		}
		return response;
	}
	
	@Override
	public PMCReportBaseDto getData(ReportPayload payload, ShiftAuditLogDao shiftAuditLogDao) {
		ShiftAuditReportResponseDto response = new ShiftAuditReportResponseDto();
		if (!ServicesUtil.isEmpty(payload)) {
			response.setTasks(shiftAuditLogDao.getReport(payload.getDurationInMonths()));
		}
		return response;
	}

	@Override
	public PMCReportBaseDto getData(ReportPayload payload, DGPFacade dgpFacade) {
		DOPVarianceReportResponseDto response = new DOPVarianceReportResponseDto();
		if (!ServicesUtil.isEmpty(payload)) {
			response.setTasks(dgpFacade.getReport(payload.getUiRequestDto()));
		}
		return response;
	}

	@Override
	public PMCReportBaseDto getData(ReportPayload payload, OBXSchedulerDao obxDao) {
		OBXReportResponseDto response = new OBXReportResponseDto();
		if (!ServicesUtil.isEmpty(payload)) {
			response.setTasks(obxDao.getObxTaskReport(payload.getDate()));
		}
		return response;
	}
	
	@Override
	public PMCReportBaseDto getData(ReportPayload payload, DropDownEditorDao dropDownDao) {
		DropDownReportResponseDto response = new DropDownReportResponseDto();
		if (!ServicesUtil.isEmpty(payload)&& payload.getReportName().equalsIgnoreCase(MurphyConstant.DROPDOWN_REPORT)) {
			response.setTasks(dropDownDao.getClassificationAndSubClassification());
		}
		if(!ServicesUtil.isEmpty(payload)&& payload.getReportName().equalsIgnoreCase(MurphyConstant.DROPDOWN_CLASSIFICATION_REPORT))
		{
			response.setTasks(dropDownDao.getClassification());
		}
		return response;
	}

@Override
	public PMCReportBaseDto getData(ReportPayload payload, TaskEventsDao taskEventsDao) {
		
		String status= payload.getStatus();
		String taskType = payload.getTaskType();
		String parentOrigin = payload.getParentOrigin();
		IOPAdminReportResponseDto response = new IOPAdminReportResponseDto();		
		if (!ServicesUtil.isEmpty(payload)) {
			response.setTaskListResponseDto(taskEventsDao.getAllTasksForAdmin(0, 0, taskType ,status ,parentOrigin));
			
		}
		return response;
	}
}	
