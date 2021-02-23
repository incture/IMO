package com.murphy.taskmgmt.controller;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.PlotlyRequestDto;
import com.murphy.taskmgmt.dto.Response;
import com.murphy.taskmgmt.service.interfaces.PlotlyFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/plotly", produces = "application/json")
public class PlotlyRest {
	

	
	@Autowired
	PlotlyFacadeLocal plotlyFacadeLocal;
	
	@RequestMapping(value = "/fetchData", method = RequestMethod.POST)
	public ResponseEntity<Response<JSONObject>> fetchDataForTimeline(@RequestBody PlotlyRequestDto testDto) {
		return plotlyFacadeLocal.generatePlotlyChart(testDto);
	}

//	Test Connection
//    @RequestMapping(value = "/testConnection", method = RequestMethod.GET)
//    ResponseEntity<?> testOnprimeDbConnection(@RequestHeader(value="url") String url,@RequestHeader(value="userName") String userName,
//			@RequestHeader(value = "password") String password) {
//		try {
//
//			TestConnectionService testConnectionService = new TestConnectionService();
//			return ResponseEntity.ok().body(testConnectionService.testConnection(url, userName, password));
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
	
}
