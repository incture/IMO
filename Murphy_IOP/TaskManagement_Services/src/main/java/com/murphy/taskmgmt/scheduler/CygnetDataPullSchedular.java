package com.murphy.taskmgmt.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class CygnetDataPullSchedular {
	
//	@Autowired
//	private CygnetAlarmFeedDao cygnetAlarmFeedDao;
//	
//	private static final Logger logger = LoggerFactory.getLogger(CygnetDataPullSchedular.class);
	
//	@SuppressWarnings("unchecked")
   /* @Scheduled(fixedRate = 60000)
	@SuppressWarnings("unchecked")
	public void getAlarmFeed() {
		
		logger.info("[Murphy][CygnetDataPullSchedular][getAlarmFeed][init]"+System.currentTimeMillis());
		
		List<Object[]> resultList = cygnetAlarmFeedDao.saveAndUpdateAlarmFeed();
		if(!ServicesUtil.isEmpty(resultList)) {
			if(resultList.size() > 0){
				if(!ServicesUtil.isEmpty(resultList)){	
					CygnetAlarmFeedDo alarmFeedDo = null;
					@SuppressWarnings("unused")
					int i =0;				
					for(Object[] obj : resultList){
						alarmFeedDo = new CygnetAlarmFeedDo();	
						cygnetAlarmFeedDao.saveAlarmFeedInstance(obj,alarmFeedDo );
						i++;
						//logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][saveAndUpdateAlarmFeed][info]"+i); 
						//set value to downtime counter table
					}
				}
				logger.info("[Murphy][CygnetDataPullSchedular][getAlarmFeed][end]"+System.currentTimeMillis());
			}
		}
		
	}*/
	
	
	/* Fetch data from Canary Hana table*/
	/*@SuppressWarnings("unchecked")
	public void getData(){		
		try{

			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query q =  session.createSQLQuery(getQueryString().trim());
			List<Object[]> resultList = q.list();

			if(!ServicesUtil.isEmpty(resultList)){	
				CygnetAlarmFeedDo alarmFeedDo = null;
				int i=0;				
				for(Object[] obj : resultList){
					alarmFeedDo = new CygnetAlarmFeedDo();
					i++;
					SetObjectToDo(obj, alarmFeedDo);                             //setting value to alarm object

					session.saveOrUpdate(alarmFeedDo);
					//load alarm for the point id and if its less then update the desig
					setDowntimeCounter(alarmFeedDo);                                        //set value to downtime counter table
					if ( i % 20 == 0 ) {                                     //20, same as the JDBC batch size					                                                              //flush a batch of inserts and release memory:
						session.flush();
						session.clear();
					}					
				}

				tx.commit();
				session.close();
			}					

		}catch(Exception e){
			logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][getData][error]"+e.getMessage());
		}
	}*/
	
	/*Insert data to CYGNET_DOWNTIME_COUNTER table*/
	/*public void setDowntimeCounter( CygnetAlarmFeedDo alarmFeedDo){
		
		 CygnetDowntimeCounterDao cygnetDowntimeCounterDao = SpringContextBridge.services().getCygnetDowntimeCounterDao();
		
		//logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][setDowntimeCounter][info][started]");
		try {			
			boolean isrecordExist =  cygnetDowntimeCounterDao.load(alarmFeedDo.getPointId(),alarmFeedDo.getAlarmSeverity(),alarmFeedDo.getTimeStamp());
		//	logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][setDowntimeCounter][isrecordExist]"+isrecordExist);
			if(!isrecordExist){	
			//	logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][setDowntimeCounter][insert]");
				CygnetDowntimeCounterDo cygnetDowntimeCounterDo = new CygnetDowntimeCounterDo();
				cygnetDowntimeCounterDo.setId(UUID.randomUUID().toString());
				cygnetDowntimeCounterDo.setPointId(alarmFeedDo.getPointId());
				cygnetDowntimeCounterDo.setAlarmSeverity(alarmFeedDo.getAlarmSeverity());
				cygnetDowntimeCounterDo.setTimeStamp(alarmFeedDo.getTimeStamp());
				cygnetDowntimeCounterDao.createDowntimeCounter(cygnetDowntimeCounterDo);	
				SetRecommendedDowntime(cygnetDowntimeCounterDo);
			//	logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][setDowntimeCounter][insert][end]");
			} 

		} catch(Exception e){
			logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][setDowntimeCounter][error]"+e.getMessage());
		}
		
	}*/
    
    /*Fetch Data from  CYGNET_DOWNTIME_COUNTER table and call SetRecommendedDowntime for Each Record*/
	/*@Scheduled(fixedRate = 60000)
    public  void loadData(){
		List<Object[]> alarmFeeds=cygnetAlarmFeedDao.getAllAlarms();
		CygnetDowntimeCounterDo downTimeCounter=new CygnetDowntimeCounterDo();
		for(Object[] obj:alarmFeeds){
			downTimeCounter.setPointId(ServicesUtil.isEmpty(obj[0])?null:obj[0].toString());
			downTimeCounter.setAlarmSeverity(ServicesUtil.isEmpty(obj[1])?null:obj[1].toString());
			downTimeCounter.setTimeStamp(ServicesUtil.isEmpty(obj[2]) ? null
					: ServicesUtil.convertFromZoneToZone(null, obj[2], MurphyConstant.CST_ZONE,
							MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE,
							MurphyConstant.DATE_DB_FORMATE));
			logger.error(downTimeCounter.toString());
			SetRecommendedDowntime(downTimeCounter);
		}
    }*/
	
	/*Insert data to CYGNET_RECOM_DOWNTIME table*/
	/*public void SetRecommendedDowntime(CygnetDowntimeCounterDo cygnetDowntimeCounterDo){
		
	//	logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][SetRecommendedDowntime][info][started]");

		try{
			
			 CygnetRecomDowntimeDao cygnetRecomDowntimeDao = SpringContextBridge.services().getCygnetRecomDowntimeDao();

			if(cygnetDowntimeCounterDo.getAlarmSeverity().equals("1-Alarm") ){
				
				boolean isrecordExist =  cygnetRecomDowntimeDao.load(cygnetDowntimeCounterDo.getPointId(), cygnetDowntimeCounterDo.getTimeStamp());
				//logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][SetRecommendedDowntime][info][1-Alarm]"+isrecordExist);
				if(!isrecordExist){
				//	logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][SetRecommendedDowntime][info][1-Alarm][insert]");
					CygnetRecomDowntimeDo cygnetRecomDowntimeDo = new CygnetRecomDowntimeDo();
					cygnetRecomDowntimeDo.setId(UUID.randomUUID().toString());
					cygnetRecomDowntimeDo.setPointId(cygnetDowntimeCounterDo.getPointId());
					cygnetRecomDowntimeDo.setStartTime(cygnetDowntimeCounterDo.getTimeStamp());
					cygnetRecomDowntimeDao.createRecomDowntime(cygnetRecomDowntimeDo);
				//	logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][SetRecommendedDowntime][info][1-Alarm][insert][end]");
				} 
				
			} else if(cygnetDowntimeCounterDo.getAlarmSeverity().equals("4-Normal")){
				//logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][SetRecommendedDowntime][info][4-Normal]");
				cygnetRecomDowntimeDao.updateEndTime(cygnetDowntimeCounterDo.getPointId(), cygnetDowntimeCounterDo.getTimeStamp());
			}

		} catch(Exception e){
			logger.error("[Murphy][TaskManagement][CygnetDataPullSchedular][SetRecommendedDowntime][error]"+e.getMessage());
		}
		
	}*/
	
	public static String resultDateAsString(Date date){
		SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
		return sf.format(date);
	}
}