package com.murphy.taskmgmt.scheduler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.dao.ProductionVarianceDao;

@Component
public class ProductionVarianceSchedular {
	
	@Autowired
	ProductionVarianceDao productionVarianceDao;
	
//	@Scheduled(cron = "0 9 * * * ?")
//	@Scheduled(fixedDelay = 100000)
	public  void stagingProveData() {
		
		String s1 = productionVarianceDao.createRecord();
		System.err.println("Message " + s1);
		System.err.println("[Murphy][ProductionVarianceSchedular][stagingProveData][end][Message] : "+ s1);
		
	}


}
