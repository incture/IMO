package com.murphy.taskmgmt.service.interfaces;


import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.murphy.taskmgmt.dto.PlotlyRequestDto;
import com.murphy.taskmgmt.dto.Response;

public interface PlotlyFacadeLocal {

	ResponseEntity<Response<JSONObject>> generatePlotlyChart(PlotlyRequestDto testDto);

}
