package com.murphy.taskmgmt.reports;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.taskmgmt.dto.AuditReportDto;
import com.murphy.taskmgmt.dto.AuditReportResponseDto;
import com.murphy.taskmgmt.dto.DOPVarianceDto;
import com.murphy.taskmgmt.dto.DOPVarianceReportResponseDto;
import com.murphy.taskmgmt.dto.DropDownDataDto;
import com.murphy.taskmgmt.dto.DropDownReportResponseDto;
import com.murphy.taskmgmt.dto.IOPAdminReportResponseDto;
import com.murphy.taskmgmt.dto.OBXReportResponseDto;
import com.murphy.taskmgmt.dto.ObxTaskAllocationDto;
import com.murphy.taskmgmt.dto.ShiftAuditLogDto;
import com.murphy.taskmgmt.dto.ShiftAuditReportResponseDto;
import com.murphy.taskmgmt.dto.TaskListDto;
import com.murphy.taskmgmt.dto.TaskListResponseDto;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

public abstract class File {

	private static final Logger logger = LoggerFactory.getLogger(File.class);

	public abstract DownloadReportResponseDto pushData(ReportFormattedDto reportFormattedDto);

	public ReportFormattedDto setSheetName(String sheetName) {
		ReportFormattedDto formattedDto = new ReportFormattedDto();
		if (MurphyConstant.REPORT_NAME_REQUEST_AUDIT.contains(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_NAME_RESPONSE_AUDIT + "." + MurphyConstant.EXCEL_EXT);

		if (MurphyConstant.REPORT_NAME_REQUEST_PROD_VAR.equalsIgnoreCase(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_NAME_RESPONSE_PROD_VAR + "_" + ServicesUtil.getCstTime()
					+ "." + MurphyConstant.EXCEL_EXT);
		
		if (MurphyConstant.REPORT_NAME_REQUEST_PROD_DGP_VAR.equalsIgnoreCase(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_NAME_RESPONSE_PROD_DGP_VAR + "_" + ServicesUtil.getCstTime()
					+ "." + MurphyConstant.EXCEL_EXT);
		
		if (MurphyConstant.SHIFT_AUDIT_REPORT.contains(sheetName))
			formattedDto.setSheetName(MurphyConstant.SHIFT_AUDIT_REPORT + "." + MurphyConstant.EXCEL_EXT);

		
		if (MurphyConstant.REPORT_NAME_REQUEST_OBX.contains(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_NAME_RESPONSE_OBX + "." + MurphyConstant.EXCEL_EXT);

		if (MurphyConstant.REPORT_IOP_ADMIN_REQUEST.contains(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_IOP_ADMIN_RESPONSE + "." + MurphyConstant.EXCEL_EXT);

		if (MurphyConstant.REPORT_NAME_REQUEST_DROPDOWN.contains(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_NAME_RESPONSE_DROPDOWN  + "." + MurphyConstant.EXCEL_EXT);
		
		if(MurphyConstant.REPORT_NAME_REQUEST_CLASSIFICATION.contains(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_NAME_RESPONSE_CLASSIFICATION + "." + MurphyConstant.EXCEL_EXT);
		
		if (MurphyConstant.REPORT_IOP_ADMIN_REQUEST.contains(sheetName))
			formattedDto.setSheetName(MurphyConstant.REPORT_IOP_ADMIN_RESPONSE + "." + MurphyConstant.EXCEL_EXT);	
		
		
		return formattedDto;
	}

	public ReportFormattedDto exportToFormattedDto(PMCReportBaseDto pmcReportBaseDto, ReportFormattedDto formattedDto) {
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			if (pmcReportBaseDto.getClass().getSimpleName().equals(AuditReportResponseDto.class.getSimpleName())) {
				return this.exportfromProcessDetailsResponse(pmcReportBaseDto, formattedDto);
			}
			
			if(pmcReportBaseDto.getClass().getSimpleName().equals(ShiftAuditReportResponseDto.class.getSimpleName())) {
				return this.exportfromShiftAuditResponse(pmcReportBaseDto, formattedDto);
			}
			
			if (pmcReportBaseDto.getClass().getSimpleName()
					.equals(DOPVarianceReportResponseDto.class.getSimpleName())) {
				return this.exportfromProductionVarianceResponse(pmcReportBaseDto, formattedDto);
			}
			if (pmcReportBaseDto.getClass().getSimpleName().equals(OBXReportResponseDto.class.getSimpleName())) {
				return this.exportfromOBXreportResponse(pmcReportBaseDto, formattedDto);
			}

			if (pmcReportBaseDto.getClass().getSimpleName().equals(IOPAdminReportResponseDto.class.getSimpleName())) {
				return this.exportfromIOPAdminResponse(pmcReportBaseDto, formattedDto);
			}
			if (pmcReportBaseDto.getClass().getSimpleName().equals(DropDownReportResponseDto.class.getSimpleName())) {
				if (formattedDto.getSheetName().equalsIgnoreCase(
						MurphyConstant.REPORT_NAME_RESPONSE_DROPDOWN + "." + MurphyConstant.EXCEL_EXT))
					return this.exportfromDropDownReportResponse(pmcReportBaseDto, formattedDto);
				if (formattedDto.getSheetName().equalsIgnoreCase(
						MurphyConstant.REPORT_NAME_RESPONSE_CLASSIFICATION + "." + MurphyConstant.EXCEL_EXT))
					return this.exportfromDropDownClassificationReportResponse(pmcReportBaseDto, formattedDto);
			}
			
			
			
			if(pmcReportBaseDto.getClass().getSimpleName().equals(IOPAdminReportResponseDto.class.getSimpleName())) {
				return this.exportfromIOPAdminResponse(pmcReportBaseDto, formattedDto);
			}
		}
		return formattedDto;
	}

	private ReportFormattedDto exportfromShiftAuditResponse(PMCReportBaseDto pmcReportBaseDto,
			ReportFormattedDto formattedDto) {
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<ShiftAuditLogDto> dtos = ((ShiftAuditReportResponseDto) pmcReportBaseDto).getTasks();
			logger.error("List<ShiftAuditLogDto> from getAuditDetails() - " + dtos);
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headerRow = new ArrayList<String>();
				headerRow.add("Modified User");
				headerRow.add("Modified Date");
				headerRow.add("Shift Day");
				headerRow.add("Operators");
				headerRow.add("Shift");
				headerRow.add("Previous Shift");
				headerRow.add("Location");
				headerRow.add("Previous Location");
				formattedDto.setHeaders(headerRow);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				for (ShiftAuditLogDto dto : dtos) {
					Object[] row = new Object[headerRow.size()];
					row[0] = ServicesUtil.isEmpty(dto.getModifiedBy()) ? null : dto.getModifiedBy();
					row[1] = ServicesUtil.isEmpty(dto.getModifiedAt()) ? null : dto.getModifiedAt();
					row[2] = ServicesUtil.isEmpty(dto.getShiftDay()) ? null : dto.getShiftDay();
					row[3] = ServicesUtil.isEmpty(dto.getResource()) ? null : dto.getResource();
					row[4] = ServicesUtil.isEmpty(dto.getCurrentShift()) ? null : dto.getCurrentShift();
					row[5] = ServicesUtil.isEmpty(dto.getPreviousShift()) ? null : dto.getPreviousShift();
					row[6] = ServicesUtil.isEmpty(dto.getCurrentBaseLoc()) ? null : dto.getCurrentBaseLoc();
					row[7] = ServicesUtil.isEmpty(dto.getPrevBaseLoc()) ? null : dto.getPrevBaseLoc();
					dataRows.add(row);
				}
				System.err.println("[dataRows]" + dataRows);
				formattedDto.setDataRows(dataRows);
			}
		}
		return formattedDto;
	}

	private ReportFormattedDto exportfromDropDownReportResponse(PMCReportBaseDto pmcReportBaseDto,
			ReportFormattedDto formattedDto) {
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<DropDownDataDto> dtos = ((DropDownReportResponseDto) pmcReportBaseDto).getTasks();
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headers = new ArrayList<String>();
				headers.add("Classification");
		        headers.add("SubClassification");
		        headers.add("ValueId");
		       
				formattedDto.setHeaders(headers);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				for (DropDownDataDto dto : dtos) {
					Object[] row = new Object[headers.size()];
					row[0] = ServicesUtil.isEmpty(dto.getClassification()) ? null : dto.getClassification();
					row[1] = ServicesUtil.isEmpty(dto.getSubclassification()) ? null : dto.getSubclassification();
					row[2]=ServicesUtil.isEmpty(dto.getValueId()) ? null : dto.getValueId();
					dataRows.add(row);
				}
				System.err.println("[dataRows]" + dataRows);
				formattedDto.setDataRows(dataRows);
			}
		}
		return formattedDto;
	}
	
	private ReportFormattedDto exportfromDropDownClassificationReportResponse(PMCReportBaseDto pmcReportBaseDto,
			ReportFormattedDto formattedDto) {
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<DropDownDataDto> dtos = ((DropDownReportResponseDto) pmcReportBaseDto).getTasks();
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headers = new ArrayList<String>();
				headers.add("Classification");
		        headers.add("ValueId");
		       
				formattedDto.setHeaders(headers);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				for (DropDownDataDto dto : dtos) {
					Object[] row = new Object[headers.size()];
					row[0] = ServicesUtil.isEmpty(dto.getClassification()) ? null : dto.getClassification();
					row[1]=ServicesUtil.isEmpty(dto.getValueId()) ? null : dto.getValueId();
					dataRows.add(row);
				}
				System.err.println("[dataRows]" + dataRows);
				formattedDto.setDataRows(dataRows);
			}
		}
		return formattedDto;
	}
	
	
	private ReportFormattedDto exportfromOBXreportResponse(PMCReportBaseDto pmcReportBaseDto,
			ReportFormattedDto formattedDto) {
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<ObxTaskAllocationDto> dtos = ((OBXReportResponseDto) pmcReportBaseDto).getTasks();
			logger.error("List<ObxTaskAllocationDto> from getObxTaskReport() - " + dtos);
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headers = new ArrayList<String>();
				headers.add("Field");
				headers.add("Cluster");
				headers.add("Day");
				headers.add("Well Location");
				headers.add("Location Code");
				headers.add("Tier");
				headers.add("Drive Time");
				headers.add("Task Owner");
				headers.add("Status");
				headers.add("Sequence");

				formattedDto.setHeaders(headers);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				for (ObxTaskAllocationDto dto : dtos) {
					Object[] row = new Object[headers.size()];
					row[0] = ServicesUtil.isEmpty(dto.getField()) ? null : dto.getField();
					row[1] = ServicesUtil.isEmpty(dto.getClusterdId()) ? null : dto.getClusterdId();
					row[2] = ServicesUtil.isEmpty(dto.getDay()) ? null : dto.getDay();
					row[3] = ServicesUtil.isEmpty(dto.getWell()) ? null : dto.getWell();
					row[4] = ServicesUtil.isEmpty(dto.getLocationCode()) ? null : dto.getLocationCode();
					row[5] = ServicesUtil.isEmpty(dto.getTier()) ? null : dto.getTier();
					row[6] = ServicesUtil.isEmpty(dto.getDriveTime()) ? null : dto.getDriveTime();
					row[7] = ServicesUtil.isEmpty(dto.getObxOperator()) ? null : dto.getObxOperator();
					row[8] = ServicesUtil.isEmpty(dto.getStatus()) ? null : dto.getStatus();
					row[9] = ServicesUtil.isEmpty(dto.getSequenceNum()) ? null : dto.getSequenceNum();
					dataRows.add(row);
				}
				System.err.println("[dataRows]" + dataRows);
				formattedDto.setDataRows(dataRows);
			}
		}
		return formattedDto;
	}

	private ReportFormattedDto exportfromProcessDetailsResponse(PMCReportBaseDto pmcReportBaseDto,
			ReportFormattedDto formattedDto) {
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<AuditReportDto> dtos = ((AuditReportResponseDto) pmcReportBaseDto).getTasks();
			logger.error("List<ProcessEventsDto> from getProcessEventsList() - " + dtos);
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headerRow = new ArrayList<String>();
				headerRow.add("Task ID");
				headerRow.add("Item Type");
				headerRow.add("Task Title");
				headerRow.add("Location");
				headerRow.add("Classification");
				headerRow.add("Sub-Classification");
				headerRow.add("Created By");
				headerRow.add("Assigned To");
				headerRow.add("Created Date-Time");
				headerRow.add("Acknowledge Date-Time");
				headerRow.add("Resolved Date-Time");
				headerRow.add("Completed Date-Time");
				headerRow.add("Status");
				formattedDto.setHeaders(headerRow);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				for (AuditReportDto dto : dtos) {
					Object[] row = new Object[headerRow.size()];
					row[0] = ServicesUtil.isEmpty(dto.getRequestId()) ? null : dto.getRequestId();
					row[1] = ServicesUtil.isEmpty(dto.getNdTaskId()) ? null : dto.getNdTaskId();
					row[2] = ServicesUtil.isEmpty(dto.getDescription()) ? null : dto.getDescription();
					row[3] = ServicesUtil.isEmpty(dto.getLocation()) ? null : dto.getLocation();
					row[4] = ServicesUtil.isEmpty(dto.getClassification()) ? null : dto.getClassification();
					row[5] = ServicesUtil.isEmpty(dto.getSubClassification()) ? null : dto.getSubClassification();
					row[6] = ServicesUtil.isEmpty(dto.getCreatedBy()) ? null : dto.getCreatedBy();
					row[7] = ServicesUtil.isEmpty(dto.getAssignedTo()) ? null : dto.getAssignedTo();
					row[8] = ServicesUtil.isEmpty(dto.getCreatedAtDisplay()) ? null : dto.getCreatedAt();
					row[9] = ServicesUtil.isEmpty(dto.getAcknowledgedAtDisplay()) ? null : dto.getAcknowledgedAt();
					row[10] = ServicesUtil.isEmpty(dto.getResolvedAtDisplay()) ? null : dto.getResolvedAt();
					row[11] = ServicesUtil.isEmpty(dto.getCompletedAtDisplay()) ? null : dto.getCompletedAt();
					row[12] = ServicesUtil.isEmpty(dto.getStatus()) ? null : dto.getStatus();
					dataRows.add(row);
				}
				System.err.println("[dataRows]" + dataRows);
				formattedDto.setDataRows(dataRows);
			}
		}
		return formattedDto;
	}

	private ReportFormattedDto exportfromProductionVarianceResponse(PMCReportBaseDto pmcReportBaseDto,
			ReportFormattedDto formattedDto) {
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			List<DOPVarianceDto> dtos = ((DOPVarianceReportResponseDto) pmcReportBaseDto).getTasks();
			logger.error("List<DOPVariance> from getProdVarList() - " + dtos);
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headerRow = new ArrayList<String>();
				headerRow.add("Well Name");
				headerRow.add("Tier");
				headerRow.add("Actual");
				headerRow.add("Projected");
				headerRow.add("Forecast");
				headerRow.add("Variance");
				headerRow.add("Variance %");
				headerRow.add("Comments");
				formattedDto.setHeaders(headerRow);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				for (DOPVarianceDto dto : dtos) {
					Object[] row = new Object[headerRow.size()];
					row[0] = ServicesUtil.isEmpty(dto.getLocation()) ? null : dto.getLocation();
					row[1] = ServicesUtil.isEmpty(dto.getTier()) ? null : dto.getTier();
					row[2] = ServicesUtil.isEmpty(dto.getActualBoed()) ? null : dto.getActualBoed();
					row[3] = ServicesUtil.isEmpty(dto.getProjectedBoed()) ? null : dto.getProjectedBoed();
					row[4] = ServicesUtil.isEmpty(dto.getForecastBoed()) ? null : dto.getForecastBoed();
					row[5] = ServicesUtil.isEmpty(dto.getVariance()) ? null : dto.getVariance();
					row[6] = ServicesUtil.isEmpty(dto.getVariancePercent()) ? null : dto.getVariancePercent();
					dataRows.add(row);
				}
				System.err.println("[dataRows]" + dataRows);
				formattedDto.setDataRows(dataRows);
			}
		}
		return formattedDto;
	}

	private ReportFormattedDto exportfromIOPAdminResponse(PMCReportBaseDto pmcReportBaseDto,
			ReportFormattedDto formattedDto) {
		String origin = "";
		String parentOrigin = "";
		String taskType = "Dispatch";
		if (!ServicesUtil.isEmpty(pmcReportBaseDto)) {
			TaskListResponseDto dtos = ((IOPAdminReportResponseDto) pmcReportBaseDto).getTaskListResponseDto();
			logger.error("List<TaskListDto> from getTaskListReasponseDto() - " + dtos);
			if (!ServicesUtil.isEmpty(dtos)) {
				List<String> headers = new ArrayList<String>();
				headers.add("Location");
				headers.add("Date Time");
				headers.add("Classification");
				headers.add("Task Type");
				headers.add("Assigned To");
				headers.add("Root Cause");
				headers.add("Task Time Difference");
				headers.add("Status");
				formattedDto.setHeaders(headers);
				List<Object[]> dataRows = new ArrayList<Object[]>();
				for (TaskListDto dto : dtos.getTaskList()) {
					origin = dto.getOrigin();
					parentOrigin = dto.getParentOrigin();

					Object[] row = new Object[headers.size()];
					row[0] = ServicesUtil.isEmpty(dto.getLocation()) ? null : dto.getLocation();
					row[1] = ServicesUtil.isEmpty(dto.getCreatedAtInString()) ? null : dto.getCreatedAtInString();
					row[2] = ServicesUtil.isEmpty(dto.getClassification()) ? null : dto.getClassification();
					if(origin.equalsIgnoreCase("Dispatch")	&& (parentOrigin.contains("ITA")))
							{
						row[3] = taskType + "-ITA";
							}
					else if (origin.equalsIgnoreCase("Dispatch")	&& (parentOrigin.equalsIgnoreCase("Custom") || parentOrigin.equalsIgnoreCase("Inquiry")
									|| parentOrigin.equalsIgnoreCase("Investigation"))) {
						row[3] = taskType;
					} else if (origin.equalsIgnoreCase("Dispatch")) {
						row[3] = taskType + "-" + parentOrigin;
					} else {
						row[3] = ServicesUtil.isEmpty(dto.getOrigin()) ? null : dto.getOrigin();
					}
					row[4] = ServicesUtil.isEmpty(dto.getTaskOwner()) ? null : dto.getTaskOwner();
					row[5] = ServicesUtil.isEmpty(dto.getRootCause()) ? null : dto.getRootCause();
					row[6] = ServicesUtil.isEmpty(dto.getTaskTimeDifference()) ? null : dto.getTaskTimeDifference();
					row[7] = ServicesUtil.isEmpty(dto.getStatus()) ? null : dto.getStatus();
					dataRows.add(row);
				}
				logger.error("[dataRows]" + dataRows);
				formattedDto.setDataRows(dataRows);
			}
		}
		return formattedDto;
	}

}

