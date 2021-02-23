package com.murphy.integration.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.murphy.integration.dto.UIRequestDto;
import com.murphy.integration.dto.UIResponseDto;

public interface EnersightProveMonthlyLocal {
	
	public UIResponseDto fetchProveData(UIRequestDto uiRequestDto);

	Map<String, String> getMuwiByLocationTypeAndCode(String locationType, List<String> locationCodeList)
			throws Exception;

	List<String> fetchLocationCodeInvestigation(Set<String> locationCodeSet) throws Exception;

	public UIResponseDto fetchProveDailyData();

	List<String> fetchLocationCodeInquire(Set<String> locationCodeSet,String userType) throws Exception;

	List<String> getMuwiWherVarLessThanThres(List<String> muwiList, double duration, double thresholdPercent, String version);
	
	public Map<String, String> fetchFracData(Set<String> uwiIdList);
	public Map<String, String> fetchPiggingValue(Set<String> uwiIdSet);
	
}
