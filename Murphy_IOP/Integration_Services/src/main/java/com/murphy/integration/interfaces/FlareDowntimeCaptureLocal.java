package com.murphy.integration.interfaces;

import java.util.List;

import com.murphy.integration.dto.FlareCaptureFetchResponseDto;
import com.murphy.integration.dto.FlareDowntimeCaptureDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.entity.FlareDowntimeCaptureDo;

public interface FlareDowntimeCaptureLocal {

	ResponseMessage insertOrUpdateFlareDowntime(FlareDowntimeCaptureDto flareDowntimeCaptureDto);
	
	// For flare downtime location history
	FlareCaptureFetchResponseDto fetchRecordForMerrickIds(List<String> merrickIds, String fromDate, String toDate, int page, int page_size);
	
	FlareDowntimeCaptureDto convertDowntimeCaptureDoToDTo(FlareDowntimeCaptureDo flareDowntimeCaptureDo);
}
