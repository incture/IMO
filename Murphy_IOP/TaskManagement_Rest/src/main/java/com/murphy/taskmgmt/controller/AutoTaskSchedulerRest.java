/**
 * 
 */
package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dao.ATSTaskAssignmentDao;
import com.murphy.taskmgmt.dao.BlackLineDao;
import com.murphy.taskmgmt.dao.ProductionLocationDao;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.AutoTaskSchedulingFacade;
import com.murphy.taskmgmt.util.MurphyConstant;

/**
 * @author Rashmendra.Sai
 *
 */

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/AutoTaskSchedulerRest", produces = "application/json")
public class AutoTaskSchedulerRest {

	@Autowired
	AutoTaskSchedulingFacade atsf;

	@Autowired
	ATSTaskAssignmentDao atsD;
	
	@Autowired
	ProductionLocationDao productionLocationDao;
	
	@Autowired
	BlackLineDao blackLineDao;

	// generate data for ATS table
	@RequestMapping(value = "/generateDataForATSTable", method = RequestMethod.GET)
	public String getATS() {
		String response = atsf.serviceHitCanary();
		return response;
	}

	// Updating in Other tables from ATS Table
	@RequestMapping(value = "/updateInOtherTable", method = RequestMethod.GET)
	public String getATSTesting() {
		String response = MurphyConstant.FAILURE;
		String res = atsD.fetchATSTaskandUpdateTable();
		if (res.equalsIgnoreCase(MurphyConstant.SUCCESS))
			response = atsD.updateATSTaskAssignmentTableStatus();
		return response;
	}

	// Deletion of data from ATS Table
	@RequestMapping(value = "/deleteATSTable", method = RequestMethod.GET)
	public String getATSDelete() {
		return atsD.deletePrevDayTask();
	}
	
	@RequestMapping(value = "/test/cftext", method = RequestMethod.GET)
	public ResponseEntity<?> testCftext(@RequestParam String locationCode) {

		try {
			return ResponseEntity.ok().body(productionLocationDao.getCFLocationCode(locationCode));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/shift/opetators", method = RequestMethod.GET)
	public ResponseEntity<?> getCurentShiptOperators() {

		try {
			return ResponseEntity.ok().body(blackLineDao.getEmpByShift());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	} 
}
