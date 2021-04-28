package com.incture.ptw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.incture.ptw.services.JsaUpdateService;
import com.incture.ptw.util.ResponseDto;

@RestController
@RequestMapping("/updatejsa")
public class JsaUpdateController {
	@Autowired
	private JsaUpdateService jsaUpdateService;
	
	@PostMapping()
	public ResponseDto updateJsa() {
		return jsaUpdateService.updateJsaService();
	}

}
