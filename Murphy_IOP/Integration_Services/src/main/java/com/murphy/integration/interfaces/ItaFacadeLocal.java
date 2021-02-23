package com.murphy.integration.interfaces;

import com.murphy.integration.dto.ResponseMessage;

public interface ItaFacadeLocal {

	ResponseMessage getLocCodeListForGasBlowBy(String oilMeterMerrickId, String gasMeterMerrickId, String meterName, Double dailyOilValue);
	
	

}
