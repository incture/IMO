package com.murphy.taskmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.QRCodeDetailsDto;
import com.murphy.taskmgmt.dto.QRCodeDto;
import com.murphy.taskmgmt.entity.QRCodeDo;
import com.murphy.taskmgmt.service.interfaces.QRCodeFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/qrcode", produces = "application/json")
public class QRCodeRest {
	
	@Autowired
	QRCodeFacadeLocal qrCodeFacade;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody QRCodeDo qrCodeDo){
		qrCodeFacade.addQRCodeDetail(qrCodeDo);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(@RequestBody String qrCode){
		qrCodeFacade.deleteQRCodeDetail(qrCode);
	}
	
	@RequestMapping(value = "/getAllQRCodeDetail", method = RequestMethod.GET)
	public List<QRCodeDo> getAllQRCodeDetail(){
		return qrCodeFacade.QRCodeDetailList();
	}
	
	@RequestMapping(value = "/generateQRCode", method = RequestMethod.GET)
	public QRCodeDto getAllQRCodeDetail(@RequestParam("qrcode") String qrCode,@RequestParam("width") String width,@RequestParam("height") String height){
		int wid = Integer.parseInt(width);
		int hig = Integer.parseInt(height);
		return qrCodeFacade.GenerateQRCode(qrCode, wid, hig);
	}
	
	@RequestMapping(value = "/getDetails", method = RequestMethod.GET)
	public QRCodeDetailsDto getDetails(@RequestParam(value = "qRNumber") String qRNumber) {

		return qrCodeFacade.getDetails(qRNumber);
	}



}
