package com.murphy.integration.interfaces;


import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.ResponseMessage;

public interface CompressorDowntimeCaptureLocal {
	
	//DowntimeCaptureFetchResponseDto fetchRecordForProvidedUwiIdAndDate(Date originalDateEntered, String uwiId);

	ResponseMessage insertOrUpdateCounts(DowntimeCaptureDto downtimeCaptureDto);
}
