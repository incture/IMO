package com.murphy.taskmgmt.reports;

import javax.jws.WebParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.AuditDao;
import com.murphy.taskmgmt.dao.DropDownEditorDao;
import com.murphy.taskmgmt.dao.OBXSchedulerDao;
import com.murphy.taskmgmt.dao.ProductionVarianceDao;
import com.murphy.taskmgmt.dao.ShiftAuditLogDao;
import com.murphy.taskmgmt.dao.TaskEventsDao;
import com.murphy.taskmgmt.service.DGPFacade;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("ReportFacade")
public class ReportFacade implements ReportFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ReportFacade.class);

	public ReportFacade() {
	}

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private OBXSchedulerDao obxDao;

	@Autowired
	DGPFacade dgpFacade;

	@Autowired
	private DropDownEditorDao dropDownDao;

	@Autowired
	private TaskEventsDao taskEventDao;
	
	@Autowired
	private ShiftAuditLogDao shiftAuditLogDao;

	@Override
	public DownloadReportResponseDto generateReport(@WebParam(name = "payload") ReportPayload payload) {
		DownloadReportResponseDto response = null;

		logger.error(
				"[Murphy][generateReport][payload][]" + payload.getFileFormate() + " : " + payload.getReportName());

		if (!ServicesUtil.isEmpty(payload) && !ServicesUtil.isEmpty(payload.getFileFormate())
				&& !ServicesUtil.isEmpty(payload.getReportName())) {

			ServiceFactory reportFactory = DownloadReportFactoryGenerator.getReportFactory();

			Report report = reportFactory.getReport(payload.getReportName());
			PMCReportBaseDto pmcReportBaseDto = null;
			// pmcReportBaseDto = report.getData(payload, auditDao);

			if (payload.getReportName().contains(MurphyConstant.SHIFT_AUDIT_REPORT)) {
				
				pmcReportBaseDto = report.getData(payload, shiftAuditLogDao);
				
			} else if (payload.getReportName().contains(MurphyConstant.REPORT_NAME_REQUEST_PROD_VAR)) {
				pmcReportBaseDto = report.getData(payload, dgpFacade);

			} else if (payload.getReportName().contains(MurphyConstant.REPORT_NAME_REQUEST_AUDIT)) {
				pmcReportBaseDto = report.getData(payload, auditDao);

			} else if (payload.getReportName().contains(MurphyConstant.OBX_REPORT)) {
				logger.error(MurphyConstant.OBX_REPORT);
				pmcReportBaseDto = report.getData(payload, obxDao);

			} else if (payload.getReportName().contains(MurphyConstant.DROPDOWN_REPORT)) {
				pmcReportBaseDto = report.getData(payload, dropDownDao);

			} else if (payload.getReportName().contains(MurphyConstant.DROPDOWN_CLASSIFICATION_REPORT)) {
				pmcReportBaseDto = report.getData(payload, dropDownDao);
			} else if (payload.getReportName().contains(MurphyConstant.REPORT_IOP_ADMIN)) {
				logger.error(MurphyConstant.REPORT_IOP_ADMIN);
				pmcReportBaseDto = report.getData(payload, taskEventDao);

			}

			ServiceFactory downloadFactory = DownloadReportFactoryGenerator.getDownloadFactory();
			File file = downloadFactory.getFile(payload.getFileFormate());
			String reportName = payload.getReportName();

			if (!ServicesUtil.isEmpty(payload.getUiRequestDto())) {

				if (MurphyConstant.DGP_REPORT_ID.equalsIgnoreCase(payload.getUiRequestDto().getReportId())) {

					reportName = MurphyConstant.REPORT_NAME_REQUEST_PROD_DGP_VAR;
				}
			} else {
				reportName = payload.getReportName();
			}
			ReportFormattedDto formattedDto = file.setSheetName(reportName);
			response = file.pushData(file.exportToFormattedDto(pmcReportBaseDto, formattedDto));
		}
		return response;
	}
}
