package com.murphy.integration.interfaces;

import java.util.Date;
import java.util.List;

import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.integration.dto.DowntimeCapturedCADto;
import com.murphy.integration.dto.ResponseMessage;

public interface DowntimeCaptureLocal {
	ResponseMessage insertOrUpdateCounts(DowntimeCaptureDto downtimeCaptureDto);

	DowntimeCaptureFetchResponseDto fetchRecordForProvidedUwiIdAndDate(Date originalDateEntered, String uwiId);

	// For well downtime location history
	DowntimeCaptureFetchResponseDto fetchRecordForProvidedUwiIds(List<String> uwiIds, String fromDate, String toDate, int page, int page_size, boolean isCompressor);

	ResponseMessage insertOrUpdateDataToProdView(DowntimeCaptureDto convertDto,String location);
}
