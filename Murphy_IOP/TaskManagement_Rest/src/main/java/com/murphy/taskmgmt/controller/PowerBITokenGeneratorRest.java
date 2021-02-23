//package com.murphy.taskmgmt.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.murphy.taskmgmt.dto.PBITokenResponse;
//import com.murphy.taskmgmt.service.interfaces.PBITokenGenFacadeLocal;
//import com.murphy.taskmgmt.util.ServicesUtil;
//
//@RestController
//@CrossOrigin
//@ComponentScan("com.murphy")
//@RequestMapping(value = "/powerBI", produces = "application/json")
//public class PowerBITokenGeneratorRest {
//	
//	@Autowired
//	PBITokenGenFacadeLocal pbiTokenFacadeLocal;
//	
//	@RequestMapping(value = "/generateToken", method = RequestMethod.GET)
//	public PBITokenResponse generateToken(@RequestParam(value = "type") String type){
//		 if(!ServicesUtil.isEmpty(pbiTokenFacadeLocal))
//			 return pbiTokenFacadeLocal.generateToken(type);
//		 return null;
//	}
//
//}
