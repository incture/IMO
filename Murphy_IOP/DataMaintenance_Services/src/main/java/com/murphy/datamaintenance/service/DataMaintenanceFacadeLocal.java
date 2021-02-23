package com.murphy.datamaintenance.service;

import com.murphy.datamaintenance.dto.DataMaintenanceResponseDto;
import com.murphy.datamaintenance.dto.DataMaintenanceUploadResponseDto;
import com.murphy.datamaintenance.dto.RequestPayloadDto;
import com.murphy.datamaintenance.dto.UploadRequestPayloadDto;

public interface DataMaintenanceFacadeLocal {
	public DataMaintenanceResponseDto downloadTableData(RequestPayloadDto requestPayloadDto);
	public DataMaintenanceUploadResponseDto uploadExcelToDB(UploadRequestPayloadDto requestPayloadDto);
}
