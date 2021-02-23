package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.datamaintenance.dto.DataMaintenanceResponseDto;
import com.murphy.datamaintenance.dto.DataMaintenanceUploadResponseDto;
import com.murphy.datamaintenance.dto.RequestPayloadDto;
import com.murphy.datamaintenance.dto.UploadRequestPayloadDto;
import com.murphy.datamaintenance.service.DataMaintenanceFacadeLocal;
import com.murphy.datamaintenancev2.dto.DataMaintenanceServicesv2;
import com.murphy.taskmgmt.util.MurphyConstant;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/dataMaintenance", produces = "application/json")
public class DataMaintenanceRest {
	
	@Autowired
	DataMaintenanceFacadeLocal facadeLocal;
	
	@Autowired
	DataMaintenanceServicesv2 dataMaintenanceServicesv2;
	
	
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public DataMaintenanceResponseDto downloadExcel(@RequestBody RequestPayloadDto requestPayloadDto) {
		//System.err.println("inside rest class");
		return facadeLocal.downloadTableData(requestPayloadDto);
	}
	
	@RequestMapping(value="/print",method=RequestMethod.GET)
	public String getMessage(){
		return "Success";
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public DataMaintenanceUploadResponseDto uploadExcelToDB(@RequestBody UploadRequestPayloadDto requestPayloadDto) {
		// System.err.println("inside rest class");

		if (!requestPayloadDto.getRequestPayloadDto().getSubModule()
				.equalsIgnoreCase(MurphyConstant.LOCATION_COORDINATE)
				&& !requestPayloadDto.getRequestPayloadDto().getSubModule()
						.equalsIgnoreCase(MurphyConstant.WELL_TIER)) {

			return facadeLocal.uploadExcelToDB(requestPayloadDto);
		} else {
			return dataMaintenanceServicesv2.uploadExcelToDB(requestPayloadDto);
		}

	}
}
