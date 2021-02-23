package com.murphy.datamaintenance.util;

import com.murphy.datamaintenance.dto.ExcelFormatDto;

public class FileUtilService {
	public void setSheetName(String subModule,ExcelFormatDto formatDto) {
		formatDto.setSheetName( DataMaintenanceConstants.valueOf(subModule).getFileName());
	}

}
