package com.murphy.integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dao.DailyReportForeManCmtsDao;
import com.murphy.integration.dto.DailyReportCommentsDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.interfaces.DailyReportForeManCmtsLocal;
import com.murphy.integration.util.ServicesUtil;

public class DailyReportForeManCmtsService implements DailyReportForeManCmtsLocal {

	private static final Logger logger = LoggerFactory.getLogger(DailyReportForeManCmtsService.class);

	@Override
	public ResponseMessage saveComments(DailyReportCommentsDto prodComments) {
		ResponseMessage responseMessage = new ResponseMessage();
		DailyReportCommentsDto dailyReportDto = new DailyReportCommentsDto();
		DailyReportForeManCmtsDao dailyRprtForeManCmtsDao = new DailyReportForeManCmtsDao();
		try {
			if (!ServicesUtil.isEmpty(prodComments) && !ServicesUtil.isEmpty(prodComments.getMuwi())) {

				// Fetch all hierarchy details by Muwi
				dailyReportDto = dailyRprtForeManCmtsDao.getHierarchyDetails(prodComments);

				// Fetch Area by Facility
				if (!ServicesUtil.isEmpty(dailyReportDto) && !ServicesUtil.isEmpty(dailyReportDto.getPad())) {
					String fieldArea = dailyRprtForeManCmtsDao.getAreaByFacility(dailyReportDto.getArea());
					if (!ServicesUtil.isEmpty(fieldArea)) {
						dailyReportDto.setAsset(fieldArea);
						responseMessage = dailyRprtForeManCmtsDao.saveCommentsToDoraReport(dailyReportDto);
					} else {
						responseMessage.setMessage("Failed to Fetch Field Area by WellPad");
						responseMessage.setStatus("FAILURE");
						responseMessage.setStatusCode("1");
					}
				} else {
					responseMessage.setMessage("Failed to fetch Hierarchy Details for Muwi " + prodComments.getMuwi());
					responseMessage.setStatus("FAILURE");
					responseMessage.setStatusCode("1");
				}

			} else {
				responseMessage.setMessage("Muwi is Missing");
				responseMessage.setStatus("FAILURE");
				responseMessage.setStatusCode("1");
			}
		} catch (Exception e) {
			logger.error("[Murphy][DailyReports][saveComments][Exception]" + e.getMessage());
		}

		return responseMessage;
	}

}
