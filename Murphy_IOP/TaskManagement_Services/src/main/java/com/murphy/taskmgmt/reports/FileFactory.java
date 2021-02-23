package com.murphy.taskmgmt.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

public class FileFactory extends ServiceFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(FileFactory.class);

	@Override
	public File getFile(String fileFormate) {
		if (!ServicesUtil.isEmpty(fileFormate)) {
			if (MurphyConstant.REPORT_EXCEL.equalsIgnoreCase(fileFormate.trim())) {
				return new Excel();
			} else if (MurphyConstant.REPORT_PDF.equalsIgnoreCase(fileFormate.trim())) {
				return new PDF();
			}
		}
		return null;
	}
	
	
	@Override
	public Report getReport(String reportName) {
		logger.error("NO IMPLEMENTATION -- Get Implementation in ReportFactory class");
		return null;
	}


}
