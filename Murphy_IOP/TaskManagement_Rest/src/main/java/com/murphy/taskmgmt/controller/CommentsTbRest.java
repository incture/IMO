package com.murphy.taskmgmt.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.QueryParam;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.CommentsTbDto;
import com.murphy.integration.dto.DailyReportCommentsDto;
import com.murphy.integration.dto.ResponseMessage;
import com.murphy.integration.dto.UIResponseDto;
import com.murphy.integration.interfaces.CommentsTbLocal;
import com.murphy.integration.service.CommentsTbService;
import com.murphy.integration.service.DailyReportForeManCmtsService;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/commentsTb", produces = "application/json")
public class CommentsTbRest {
	

	CommentsTbLocal commentsTblocal;
	DailyReportForeManCmtsService dailyReportCommentsServiceLocal;
	
	@RequestMapping(value = "/updateComments", method = RequestMethod.POST)
	public ResponseMessage updateComments(@RequestBody CommentsTbDto commentsTbDto) {
		commentsTblocal = new CommentsTbService();
		return commentsTblocal.insertMerrickIdIntoDB(commentsTbDto);
	}
	
	@RequestMapping(value = "/getComments", method = RequestMethod.GET)
	public UIResponseDto fetchComments(@QueryParam("uwiId") String uwiId, @QueryParam("originalDateEntered") String originalDateEntered) {
		commentsTblocal = new CommentsTbService();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date originalDate = null;
		UIResponseDto responseDto = null;
		try {
			originalDate = df.parse(originalDateEntered);
			responseDto = commentsTblocal.fetchDataFromCommentsDB(uwiId, originalDate);
		} catch (ParseException e) {
			System.err.println("Exception : "+e.getMessage());
		}
		return responseDto;
	}
	
//	@RequestMapping(value = "/createComments", method = RequestMethod.POST)
//	public ResponseMessage createComments(@RequestBody DailyReportCommentsDto prodComments) {
//		dailyReportCommentsServiceLocal=new DailyReportForeManCmtsService();
//		return dailyReportCommentsServiceLocal.saveComments(prodComments);
//	}
	
	
}
