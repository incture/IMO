package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.HseDocumentDto;
import com.murphy.taskmgmt.dto.HseDocumentResponse;
import com.murphy.taskmgmt.dto.HseResponseBodyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.HseDocumentFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/tasks", produces = "application/json")
public class HSEDocumentRest {
	
	@Autowired
	HseDocumentFacadeLocal facadelocal;

	// GET DOCUMENT DETAILS
	@RequestMapping(value = "/getHseDocument", method = RequestMethod.GET)
	public HseResponseBodyDto getHSEDocument() {
		return facadelocal.getHseDocument();

	}

	// UPLOAD HSE DOCUMNET
	@RequestMapping(value = "/uploadHseDocument", method = RequestMethod.POST)
	public ResponseMessage uploadHSEDocument(@RequestBody HseDocumentDto dto) {
		return facadelocal.createHSEDocument(dto);

	}

	// GET THE FREQUENTLY USED TOP 5 STRINGS
	@RequestMapping(value = "/getStringList", method = RequestMethod.GET)
	public HseResponseBodyDto getStringDetails() {
		return facadelocal.getStringList();

	}

	// GET STRING DOCUMNET
	@RequestMapping(value = "/getStringDetails", method = RequestMethod.GET)
	public HseDocumentResponse getStringDetails(@RequestParam(value = "string") String text) {

		return facadelocal.getStringDocument(text);

	}

	// GET PARAGRAPH
	@RequestMapping(value = "/getParagraph", method = RequestMethod.GET)
	public HseDocumentResponse getParagraph(@RequestParam(value = "page") int page,
			@RequestParam(value = "line") String line, @RequestParam(value = "url") String url) {

		return facadelocal.getParagraph(page, line, url);

	}

}
