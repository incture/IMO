package com.murphy.integration.interfaces;

import com.murphy.integration.dto.UIResponseDto;

public interface InvestigationHistoryLocal {
	
	public UIResponseDto fetchInvestigationData(String uwiId);
	
}
