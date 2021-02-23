package com.murphy.downtime.service.interfaces;

import java.util.Date;

import com.murphy.downtime.dto.DowntimeCaptureDto;
import com.murphy.downtime.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.downtime.dto.ResponseMessage;

public interface DowntimeCaptureLocal {
	ResponseMessage insertOrUpdateCounts(DowntimeCaptureDto downtimeCaptureDto);

	DowntimeCaptureFetchResponseDto fetchRecordForProvidedUwiIdAndDate(Date originalDateEntered, String uwiId);
}
