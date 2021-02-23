package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.UpliftFactorDto;

public interface UpliftFactorFacadeLocal {

	UpliftFactorDto getUpliftFactor(String locationCode, Long startDate);

}
