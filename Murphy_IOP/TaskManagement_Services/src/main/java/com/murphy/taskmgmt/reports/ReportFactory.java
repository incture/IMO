package com.murphy.taskmgmt.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

public class ReportFactory extends ServiceFactory {

	private static final Logger logger = LoggerFactory.getLogger(ReportFactory.class);

	@Override
	public Report getReport(String reportName) {
		
		if (!ServicesUtil.isEmpty(reportName)) {
			if (MurphyConstant.REPORT_NAME_REQUEST_AUDIT.equalsIgnoreCase(reportName.trim())) {
				logger.error(MurphyConstant.REPORT_NAME_REQUEST_AUDIT);
				return new TaskManagerReport();
			}
			if (MurphyConstant.REPORT_NAME_REQUEST_PROD_VAR.equalsIgnoreCase(reportName.trim())) {
				logger.error(MurphyConstant.REPORT_NAME_REQUEST_PROD_VAR);
				return new TaskManagerReport();
			}
			if (MurphyConstant.SHIFT_AUDIT_REPORT.equalsIgnoreCase(reportName.trim())) {
				logger.error(MurphyConstant.SHIFT_AUDIT_REPORT);
				return new TaskManagerReport();
			}
			if (MurphyConstant.REPORT_NAME_REQUEST_OBX.equalsIgnoreCase(reportName.trim())) {
				logger.error(MurphyConstant.REPORT_NAME_REQUEST_OBX);
				return new TaskManagerReport();
			}
			if (MurphyConstant.REPORT_NAME_REQUEST_DROPDOWN.equalsIgnoreCase(reportName.trim())) {
				logger.error(MurphyConstant.REPORT_NAME_REQUEST_DROPDOWN);
				return new TaskManagerReport();
			}
			if (MurphyConstant.REPORT_NAME_REQUEST_CLASSIFICATION.equalsIgnoreCase(reportName.trim())) {
				logger.error(MurphyConstant.REPORT_NAME_REQUEST_CLASSIFICATION);
				return new TaskManagerReport();
			}

			if (MurphyConstant.REPORT_IOP_ADMIN_REQUEST.equalsIgnoreCase(reportName.trim())) {
				logger.error(MurphyConstant.REPORT_IOP_ADMIN_REQUEST);
				return new TaskManagerReport();
			}
		}
		return null;
	}

	@Override
	public File getFile(String fileFormate) {
		logger.error("NO IMPLEMENTATION -- Get Implementation in FileFactory class");
		return null;
	}
}
