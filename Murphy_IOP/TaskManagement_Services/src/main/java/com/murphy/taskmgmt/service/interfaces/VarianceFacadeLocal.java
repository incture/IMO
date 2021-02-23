package com.murphy.taskmgmt.service.interfaces;


import com.murphy.integration.dto.UIRequestDto;
import com.murphy.taskmgmt.dto.DOPVarianceResponseDto;

public interface VarianceFacadeLocal {

	DOPVarianceResponseDto fetchVarianceData(UIRequestDto uiRequestDto);

	String createRecord();

}
