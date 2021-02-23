package com.murphy.taskmgmt.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.interfaces.CompressorDowntimeCaptureLocal;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.CompressorDowntimeFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/compressorDowntime", produces = "application/json")
public class CompressorDowntimeRest {
	
//	private static final Logger logger = LoggerFactory.getLogger(DowntimeCaptureRest.class);

	@Autowired
	CompressorDowntimeFacadeLocal compressorDowntimeFacadeLocal;
	
	CompressorDowntimeCaptureLocal compressorDowntimeCaptureLocal;
	
	@RequestMapping(value = "/getDowntimeCodes", method = RequestMethod.GET)
	public DowntimeResponseDto getDowntimeCodes() {
		return compressorDowntimeFacadeLocal.getCompressorDowntimeCodes();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseMessage createDowntimeCapture(@RequestBody DowntimeUpdateDto  dto){
		return compressorDowntimeFacadeLocal.createCompressorDowntime(dto);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseMessage updateDowntimeCapture(@RequestBody DowntimeUpdateDto  dto){
		return compressorDowntimeFacadeLocal.updateCompressorDowntime(dto);
	}

	@RequestMapping(value = "/getDowntime", method = RequestMethod.POST)
	public DowntimeResponseDto getAllDowntimeCapture(@RequestBody DowntimeRequestDto dtoGet){
		return compressorDowntimeFacadeLocal.getDowntimeHierarchy(dtoGet);
	}
	
	
	//Methods for Procount
	/*@RequestMapping(value = "/updateDowntimeCapture", method = RequestMethod.POST)
	public com.murphy.integration.dto.ResponseMessage createTaskFromTemplate(DowntimeCaptureDto downtimeCaptureDto) {
		downtimeCaptureLocal = new DowntimeCapture();
		return downtimeCaptureLocal.insertOrUpdateCounts(downtimeCaptureDto);
	}

	@RequestMapping(value = "/getDowntimeCapture", method = RequestMethod.GET)
	public DowntimeCaptureFetchResponseDto fetchDowntimeCapture(@QueryParam("originalDateEntered") String originalDateEntered, @QueryParam("uwiId") String uwiId) {
		downtimeCaptureLocal = new DowntimeCapture();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date originalDate = null;
		DowntimeCaptureFetchResponseDto responseDto = null;
		try {
			originalDate = df.parse(originalDateEntered);
			responseDto = downtimeCaptureLocal.fetchRecordForProvidedUwiIdAndDate(originalDate, uwiId);
		} catch (ParseException e) {
			System.err.println("Exception : "+e.getMessage());
		}
		// LOGGER.info("Inside delivery data creation");
		return responseDto;
	}
	*/
	public static void main(String[] args) throws ParseException {
		String d = "2018-04-20T00:00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = df.parse(d);
		System.out.println(date);
	}
	

}
