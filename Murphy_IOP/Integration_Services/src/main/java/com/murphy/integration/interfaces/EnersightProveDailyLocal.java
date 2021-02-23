package com.murphy.integration.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.murphy.integration.dto.PROVEUIResponseDto;
import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.dto.UIResponseDto;

public interface EnersightProveDailyLocal {

	PROVEUIResponseDto fetchProveData(UIRequestDto uiRequestDto);

	UIResponseDto fetchProveDailyData();

	List<String> getMuwiWherVarLessThanThres(List<String> muwiList, double duration, double thresholdPercent,
			String version, String country);

	Map<String, String> fetchFracData(Set<String> uwiIdSet);

	Map<String, String> fetchPiggingValue(Set<String> uwiIdSet);

	//added for pw hooper canada
	 Map<String,String> getMuwiWherVarLessThanThresForCanada(List<String> muwiList , double duration , double thresholdPercent , 
			String version, String country) ;
}
