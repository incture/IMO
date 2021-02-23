package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.CustomLocationHistoryDto;

public interface CustomLocationHistoryFacadeLocal {

	CustomLocationHistoryDto getLocTaskHistory(String locationCode, String monthTime, int time, int page_size);
}
