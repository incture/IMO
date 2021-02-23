package com.murphy.integration.interfaces;

import com.murphy.integration.dto.DailyReportCommentsDto;
import com.murphy.integration.dto.ResponseMessage;

public interface DailyReportForeManCmtsLocal {


	ResponseMessage saveComments(DailyReportCommentsDto prodComments);


}
