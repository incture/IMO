package com.murphy.integration.interfaces;

import com.murphy.integration.dto.UIResponseDto;

public interface InvestigationHistoryFromWellViewServiceLocal {
	public UIResponseDto fetchInvestigationDataFromWellView(String uwiId);
}
