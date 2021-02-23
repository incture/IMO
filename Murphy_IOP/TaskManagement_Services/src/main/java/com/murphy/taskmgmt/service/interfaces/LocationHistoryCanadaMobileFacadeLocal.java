package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.LocationHistoryMobileDto;

public interface LocationHistoryCanadaMobileFacadeLocal {
	LocationHistoryMobileDto getMobDowntimeHistoryForCanada(DowntimeRequestDto dtoGet);
}
