package com.murphy.taskmgmt.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.util.GeoTabUtil;

@Component
public class AutoSignInScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoSignInScheduler.class);
	
//	@Scheduled(fixedRate = 60000)
	public void signInUsers () {
		logger.error("[AutoSignInScheduler][init] : "+System.currentTimeMillis());
		ResponseMessage signInResponseMessage = GeoTabUtil.signInUsers();
//		logger.error("INFO : [AutoSignInScheduler][Response][signInResponseMessage] : "+signInResponseMessage);
		ResponseMessage signOutResponseMessage = GeoTabUtil.signOutUsers();
//		logger.error("INFO : [AutoSignInScheduler][Response][signOutResponseMessage] : "+signOutResponseMessage);
		ResponseMessage deleteResponseMessage = GeoTabUtil.deleteRecords();
//		logger.error("INFO : [AutoSignInScheduler][Response][deleteResponseMessage] : "+deleteResponseMessage);
		logger.info("Response Info : "+signInResponseMessage + signOutResponseMessage + deleteResponseMessage);
		logger.error("[AutoSignInScheduler][end] : "+System.currentTimeMillis() + signInResponseMessage + signOutResponseMessage + deleteResponseMessage);
	}

}
