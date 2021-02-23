package com.murphy.taskmgmt.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.integration.interfaces.DowntimeCaptureLocal;
import com.murphy.integration.service.DowntimeCapture;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.DowntimeWellChildCodeDto;
import com.murphy.taskmgmt.dto.DowntimeWellChildCodeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeResponseDto;
import com.murphy.taskmgmt.dto.FracZoneDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.DowntimeCapturedFacadeCALocal;
import com.murphy.taskmgmt.service.interfaces.DowntimeCapturedFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/downtimeCapture", produces = "application/json")
public class DowntimeCaptureRest {

	// private static final Logger logger =
	// LoggerFactory.getLogger(DowntimeCaptureRest.class);

	@Autowired
	DowntimeCapturedFacadeLocal downtimeFacadeLocal;

	@Autowired
	DowntimeCapturedFacadeCALocal canadaDowntimeFacadeLocal;

//	@Autowired
//	DowntimeCaptureLocal downtimeCaptureLocal;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseMessage createDowntimeCapture(@RequestBody DowntimeUpdateDto dto) {
		if (dto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
			return canadaDowntimeFacadeLocal.createDowntimeCapturedForCanada(dto);
		else
			return downtimeFacadeLocal.createDowntimeCaptured(dto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseMessage updateDowntimeCapture(@RequestBody DowntimeUpdateDto dto) {
		if (dto.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
			return canadaDowntimeFacadeLocal.updateDowntimeCaptured(dto);
		else
			return downtimeFacadeLocal.updateDowntimeCaptured(dto);
	}

	@RequestMapping(value = "/getDowntime", method = RequestMethod.POST)
	public DowntimeResponseDto getAllDowntimeCapture(@RequestBody DowntimeRequestDto dtoGet) {
		if (dtoGet.getCountryCode().equalsIgnoreCase(MurphyConstant.CA_CODE))
			return canadaDowntimeFacadeLocal.getDowntimeHierarchy(dtoGet);
		else
			return downtimeFacadeLocal.getDowntimeHierarchy(dtoGet);
	}

	/*
	 * @RequestMapping(value = "/getData", method = RequestMethod.GET) public
	 * DowntimeCapturedDto updateDowntimeCapture(@RequestBody String dId){
	 * return facadeLocal.getDowntimeCaptured(dId); }
	 * 
	 * @RequestMapping(value = "/getAllData", method = RequestMethod.GET) public
	 * List<DowntimeCapturedDto> updateDowntimeCapture(){ return
	 * facadeLocal.getAllDowntimeCaptured(); }
	 */

	/*
	 * @Autowired DowntimeCaptureLocal downtimeCaptureLocal;
	 * 
	 * @RequestMapping(value = "/updateDowntimeCapture", method =
	 * RequestMethod.POST) ResponseMessage createTaskFromTemplate(@RequestBody
	 * DowntimeCaptureDto downtimeCaptureDto) {
	 * logger.error("1. downtimeCaptureDto " + downtimeCaptureDto); return
	 * downtimeCaptureLocal.insertOrUpdateCounts(downtimeCaptureDto); }
	 *//*
		 * @RequestMapping(value = "/getDowntimeCapture", method =
		 * RequestMethod.GET) DowntimeCaptureFetchResponseDto
		 * fetchDowntimeCapture(@RequestParam(value =
		 * "originalDateEntered") @DateTimeFormat(pattern=
		 * "yyyy-MM-dd'T'HH:mm:ss") Date
		 * originalDateEntered, @RequestParam(value ="uwiId") String uwiId) { //
		 * LOGGER.info("Inside delivery data creation");
		 * logger.error("1. originalDateEntered " + originalDateEntered +
		 * "  uwiId" + uwiId); return
		 * downtimeCaptureLocal.fetchRecordForProvidedUwiIdAndDate(
		 * originalDateEntered, uwiId); }
		 */

	// Methods for Procount
//	@RequestMapping(value = "/updateDowntimeCapture", method = RequestMethod.POST)
//	public com.murphy.integration.dto.ResponseMessage createTaskFromTemplate(DowntimeCaptureDto downtimeCaptureDto) {
//		downtimeCaptureLocal = new DowntimeCapture();
//		return downtimeCaptureLocal.insertOrUpdateCounts(downtimeCaptureDto);
//	}
//
//	@RequestMapping(value = "/getDowntimeCapture", method = RequestMethod.GET)
//	public DowntimeCaptureFetchResponseDto fetchDowntimeCapture(
//			@QueryParam("originalDateEntered") String originalDateEntered, @QueryParam("uwiId") String uwiId) {
//		downtimeCaptureLocal = new DowntimeCapture();
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//		Date originalDate = null;
//		DowntimeCaptureFetchResponseDto responseDto = null;
//		try {
//			originalDate = df.parse(originalDateEntered);
//			responseDto = downtimeCaptureLocal.fetchRecordForProvidedUwiIdAndDate(originalDate, uwiId);
//		} catch (ParseException e) {
//			System.err.println("Exception : " + e.getMessage());
//		}
//		// LOGGER.info("Inside delivery data creation");
//		return responseDto;
//	}

	// added as a part of Data Maintenance - sprint 5
	// SOC
	@RequestMapping(value = "/getActiveParenCodeForWellDowntime", method = RequestMethod.GET)
	public DowntimeWellParentCodeResponseDto getActiveParenCodeForWellDowntime(
			@RequestParam(value = "country", required = false) String country) {
		return downtimeFacadeLocal.getActiveParenCodeForWellDowntime(country);
	}

	@RequestMapping(value = "/getActiveChildCodeForWellDowntime", method = RequestMethod.GET)
	public DowntimeWellChildCodeResponseDto getActiveChildCodeForWellDowntime() {
		return downtimeFacadeLocal.getActiveChildCodeForWellDowntime();
	}

	@RequestMapping(value = "/getActiveChildCodeForWellDowntimeByParentCode", method = RequestMethod.GET)
	public DowntimeWellChildCodeResponseDto getActiveChildCodeForWellDowntimeByParentCode(
			@QueryParam("parentCode") String parentCode) {
		return downtimeFacadeLocal.getActiveChildCodeForWellDowntimeByParentCode(parentCode);
	}

	// EOC
	public static void main(String[] args) throws ParseException {
		String d = "2018-04-20T00:00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = df.parse(d);
		System.out.println(date);
	}
	

}
