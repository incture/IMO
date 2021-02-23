package com.murphy.taskmgmt.scheduler;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.murphy.taskmgmt.service.interfaces.NotificationFacadeLocal;
import com.murphy.taskmgmt.websocket.TokenServiceEndPoint;

@Component
public class NotificationScheduler {

	private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
	
	@Autowired
	NotificationFacadeLocal notifictaionFacadeLocal;
	
//	@Scheduled(cron="0 */2 * ? * *")//Scheduled Every 2 Mins
//	@Scheduled(cron="*/30 * * ? * *")//Scheduled Every 30 secs
	public void sendAlertMessage(){
		logger.error("[Murphy][NotificationScheduler][sendAlertMessage][Scheduler] Scheduler before Running"+new Date());
		notifictaionFacadeLocal.getNotificationList();
		logger.error("[Murphy][NotificationScheduler][sendAlertMessage][Scheduler] Scheduler Run Completed" );
	}
	
//	@Scheduled(cron="0 */50 * ? * *") //Scheduled Every 50 Mins
	public void sendToken(){
		logger.error("[Murphy][NotificationScheduler][sendToken][Scheduler] Scheduler before Running"+new Date());
		TokenServiceEndPoint tokenServiceEndPoint = new TokenServiceEndPoint();
		tokenServiceEndPoint.callRest();
		logger.error("[Murphy][NotificationScheduler][sendToken][Scheduler] Scheduler Run Completed" );
	}

}
