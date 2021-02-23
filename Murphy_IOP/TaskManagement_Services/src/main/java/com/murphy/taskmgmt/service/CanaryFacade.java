package com.murphy.taskmgmt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.service.interfaces.CanaryFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;



@Service("canaryFacade")
public class CanaryFacade implements CanaryFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(CanaryFacade.class);

	public CanaryFacade() {
	}

	@Override
	public String setCanaryData(String startTime,String endTime) {
		String response = MurphyConstant.FAILURE;
		try{
			//			CanaryStagingScheduler sch = new CanaryStagingScheduler();
			//			canaryStagingScheduler.setStagingData(startTime, endTime);
			response = MurphyConstant.SUCCESS;
		}
		catch(Exception e){
			logger.error("[Murphy][CanaryStagingScheduler][setCanaryData][error]"+e.getMessage());
		}

		return response;
	}	

}



