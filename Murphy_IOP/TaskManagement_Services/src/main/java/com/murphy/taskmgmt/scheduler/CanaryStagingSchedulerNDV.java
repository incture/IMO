package com.murphy.taskmgmt.scheduler;

import org.springframework.stereotype.Component;

@Component("CanaryStagingSchedulerNDV")
public class CanaryStagingSchedulerNDV {
//
//	@Autowired
//	private CanaryStagingDao canaryStagingDao;
//
//	@Autowired
//	private CanaryStagingNDVDao stagingDao;
//	
//	@Autowired
//	HierarchyDao locDao;
//
//
////	@Scheduled(fixedRate = 3600000)
//	public  void setStagingData() {
//		
//		System.err.println("[Murphy][CanaryStagingSchedulerNDV][setStagingData][init] : "+new Date());
//		String endTime = ServicesUtil.convertFromZoneToZoneString1(null, null , "", MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T, 60);
//		String startTime =   ServicesUtil.getPreviousDateAtZoneInString(MurphyConstant.DATEFORMAT_T,"CST",60,MurphyConstant.MINUTES, 60);
//		String timeToDelete = ServicesUtil.getPreviousDateAtZoneInString(MurphyConstant.DATEFORMAT_T,"CST",30,MurphyConstant.DAYS, 60).replace('T', ' ');
//		String s2 =	setMissingData(timeToDelete,endTime.replace("T", " "));
//		String s = setCanaryData(startTime,endTime);
////		String s1 = canaryStagingDao.deleteAllDataBeforeDate(timeToDelete,"TM_CANARY_STAGING_NDV");
////		System.err.println("[Murphy][CanaryStagingScheduler][setCanaryData][setCanaryData]" +s +"[startTime]" +startTime +"[endTime]"+endTime +"s1"+s1+"s2"+s2);
//		System.err.println("[Murphy][CanaryStagingSchedulerNDV][setStagingData][end] : "+new Date());
//	}
//
//
//
//
//	public String setCanaryData(String startTime,String endTime) {
//		String response = MurphyConstant.FAILURE;
//		try{
//			setStagingData(startTime, endTime);
//			response = MurphyConstant.SUCCESS;
//		}
//		catch(Exception e){
//			System.err.println("[Murphy][CanaryStagingSchedulerNDV][setCanaryData][error]"+e.getMessage());
//		}
//
//		return response;
//	}	
//
//
//
//	private String getUserToken(){
//		String response = null;
//		try {
//			String userTokenPayload = "{\"username\":\""+MurphyConstant.CANARY_USERNAME+"\",\"password\":\""+MurphyConstant.CANARY_PASSWORD+"\","
//					+ "\"timeZone\":\""+MurphyConstant.CANARY_TIMEZONE+"\",\"application\":\""+MurphyConstant.CANARY_APP+"\"}";
//			String url = "api/v1/getUserToken";
//
//			String jsonString = DestinationUtil.executeWithDest(MurphyConstant.DEST_CANARY, url,
//					MurphyConstant.HTTP_METHOD_POST, MurphyConstant.APPLICATION_JSON, "",userTokenPayload,"",false);
//
//			JSONParser parser = new JSONParser();
//			JSONObject json = (JSONObject) parser.parse(jsonString);
//			response =  (String) json.get("userToken");
//		} catch (Exception e) {
//			System.err.println("[Murphy][CanaryStagingSchedulerNDV][getUserToken][error]"+e.getMessage());
//		}
//		return response ;
//	}
//
//
//	private String getPayloadInString(List<String> payloadList ){
//		String payload = "";
//
//		for(String st : payloadList){
//			payload = payload +"\""+st+"\",";
//		}
//		payload = payload.substring(0,payload.length()-1);
//		payload = "[" +payload +"]"; 
//		return payload;
//
//	}
//
//	private JSONObject getCanaryData(String userToken,String payload ,String startTime,String endTime){
//		try {
//			String canaryUrl =	"api/v1/getTagData"; 
//			String canaryPayload =	"{"
//					+"\"userToken\": \""+userToken+"\","
//					+ "\"startTime\": \""+startTime+":00.0000000-05:00\","
//					+ "\"endTime\": \""+endTime+":00.0000000-05:00\","
//					+ "\"aggregateName\": \"Maximum\","
//					+ "\"aggregateInterval\": \"0:01:00:00\","
//					+ "\"includeQuality\": false,"
//					+ " \"MaxSize\": 4000000,"
//					+ "\"continuation\": null,"
//					+"\"tags\": "+payload+""
//					//					+"\"tags\": [\"MUWI.9264225500000000WHF0004H1.PRCASXIN\",  \"MUWI.9264225500000000WHF0004H1.PRTUBXIN\","
//					//					+" \"MUWI.9264225500000000WHF0004H1.PRSTAXIN\", \"MUWI.9264225500000000WHF0004H1.QTGASD\","
//					//					+" \"MUWI.9264225500000000WHF0004H1.QTOILD\", \"MUWI.9264225500000000WHF0004H1.QTH2OD\","
//					//					+"\"MUWI.9264201300000000SCD0043H1.PRCASXIN\",  \"MUWI.9264201300000000SCD0043H1.PRTUBXIN\",  \"MUWI.9264201300000000SCD0043H1.PRSTAXIN\","
//					//					+" \"MUWI.9264201300000000SCD0043H1.QTGASD\",  \"MUWI.9264201300000000SCD0043H1.QTOILD\",  \"MUWI.9264201300000000SCD0043H1.QTH2OD\"," 
//					//					+"\"MUWI.9264231100000000RLP0007H1.PRCASXIN\",  \"MUWI.9264231100000000RLP0007H1.PRTUBXIN\",  \"MUWI.9264231100000000RLP0007H1.PRSTAXIN\","
//					//					+" \"MUWI.9264231100000000RLP0007H1.QTGASD\",  \"MUWI.9264231100000000RLP0007H1.QTOILD\",  \"MUWI.9264231100000000RLP0007H1.QTH2OD\"" 
//					//
//					//+"]"
//					+ "}";
//
////			System.err.println("[Murphy][CanaryStagingSchedulerNDV][getStagingData][canaryPayload]"+ canaryPayload);
//
//			String canaryResponse = DestinationUtil.executeWithDest(MurphyConstant.DEST_CANARY, canaryUrl,
//					MurphyConstant.HTTP_METHOD_POST, MurphyConstant.APPLICATION_JSON, "",canaryPayload,"",false);
//
//			JSONParser parser = new JSONParser();
//			JSONObject canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
//			return (JSONObject) canaryJson.get("data");
//		} catch (Exception e) {
//			System.err.println("[Murphy][CanaryStagingSchedulerNDV][getCanaryData][error]"+e.getMessage());
//		}
//		return null;
//	}
//
//	//https://murphy.canarylabs.online:55236/api/v1/getUserToken
//
//	public void setStagingData(String startTime,String endTime) {
//		System.err.println("[Murphy][CanaryStagingSchedulerNDV][getStagingData][init]");
//
//		try{
//			String userToken = getUserToken();
//			if(!ServicesUtil.isEmpty(userToken)){
//				List<String> payloadList = new ArrayList<String>();
////						payloadDao.getPayload();
//				String[] payloadParams = MurphyConstant.CANARY_PARAM_NDV;
//				List<String> wells = locDao.getAllWells();
//				for(String well : wells){
//					for(String param : payloadParams){
//						payloadList.add("MUWI."+well+"."+param);
//					}
//				}
//				
//				
//				if(!ServicesUtil.isEmpty(payloadList)){
//					String payload = getPayloadInString(payloadList);
//					JSONObject canaryData =   getCanaryData( userToken, payload , startTime, endTime);
//					if(!ServicesUtil.isEmpty(canaryData)){
//						//						String[]	payloadList1 = { "MUWI.9264225500000000WHF0004H1.PRCASXIN", "MUWI.9264225500000000WHF0004H1.PRTUBXIN", "MUWI.9264225500000000WHF0004H1.PRSTAXIN", "MUWI.9264225500000000WHF0004H1.QTGASD",
//						//						               "MUWI.9264225500000000WHF0004H1.QTOILD", "MUWI.9264225500000000WHF0004H1.QTH2OD",
//						//						               "MUWI.9264201300000000SCD0043H1.PRCASXIN",  "MUWI.9264201300000000SCD0043H1.PRTUBXIN",  "MUWI.9264201300000000SCD0043H1.PRSTAXIN",
//						//						               "MUWI.9264201300000000SCD0043H1.QTGASD",  "MUWI.9264201300000000SCD0043H1.QTOILD",  "MUWI.9264201300000000SCD0043H1.QTH2OD",
//						//						               "MUWI.9264231100000000RLP0007H1.PRCASXIN",  "MUWI.9264231100000000RLP0007H1.PRTUBXIN",  "MUWI.9264231100000000RLP0007H1.PRSTAXIN",
//						//						               "MUWI.9264231100000000RLP0007H1.QTGASD",  "MUWI.9264231100000000RLP0007H1.QTOILD",  "MUWI.9264231100000000RLP0007H1.QTH2OD" };
//						//						
//						createTasks(canaryData,payloadList);
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			System.err.println("[Murphy][CanaryStagingSchedulerNDV][getStagingData][error]"+e.getMessage());
//			e.printStackTrace();
//		}
//
//	}
//
//
//	public void createTasks(JSONObject data,List<String> payloadList){
//		System.err.println("[Murphy][CanaryStagingSchedulerNDV][createTasks][init]");
//		int k = 0;
//		for(String well: payloadList){
//			JSONArray wellData = (JSONArray) data.get(well);
//			for(int i = 0;i<wellData.size();i++){
//				k++;
//				JSONArray wellList = (JSONArray) wellData.get(i);
//				CanaryStagingDto canaryDto = new CanaryStagingDto();
//				String[] paramList = well.split("\\.");
//				canaryDto.setMuwiId(paramList[1]);
//				canaryDto.setParameterType(paramList[2]);
//				canaryDto.setCreatedAt(ServicesUtil.convertFromZoneToZone(null, wellList.get(0),"","",MurphyConstant.DATEFORMAT_T_FULL, MurphyConstant.DATE_DB_FORMATE_SD));
//
//				//				System.err.println("[Murphy][CanaryStagingSchedulerNDV][createTasks][][getCreatedAt]" + canaryDto.getCreatedAt());
//
//				if(wellList.get(1)!= null && wellList.get(1) !="null"){
//					if(wellList.get(1).getClass().getName().equals("java.lang.Double")){
//						canaryDto.setDataValue((Double) wellList.get(1));	
//					}else if(wellList.get(1).getClass().getName().equals("java.lang.Long")){
//						canaryDto.setDataValue(((Long) wellList.get(1)).doubleValue());	
//					}else if(wellList.get(1).getClass().getName().equals("java.lang.Integer")){
//						canaryDto.setDataValue(((Integer) wellList.get(1)).doubleValue());	
//					}
//				}
//				stagingDao.createStaging(canaryDto, k);
//			}
//
//		}	
//		System.err.println("[Murphy][CanaryStagingSchedulerNDV][createTasks][end][totalCount]" + k);
//	}
//
//	public String setMissingData(String timeToDelete,String endTime){
//		String response = MurphyConstant.FAILURE;
//		try{
//			List<Object> dateList = null;
////					canaryStagingDao.getAllDatesInDesc(timeToDelete,endTime,"TM_CANARY_STAGING_NDV");
//			if(ServicesUtil.isEmpty(dateList)){
//				dateList =  new ArrayList<Object>();
//			}
//			if(dateList.size() == 0){
//				dateList.add(timeToDelete +":00.000");
//				dateList.add(endTime +":00.000");
//			}
//			
////			System.err.println("[Murphy][CanaryStagingScheduler][setMissingData][dateList] : "+dateList);
//			List<Date> calendarList  = new LinkedList<>();
//			for(Object date : dateList){
//				Date calDate =(ServicesUtil.convertFromZoneToZone(null, date,MurphyConstant.CST_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DB_FORMATE));
//				calendarList.add(calDate);
//			}
//
////			System.err.println("[Murphy][CanaryStagingScheduler][setMissingData][calendarList] : "+calendarList);
//			for(int i=0 ;i< calendarList.size()-1;i++){
//				if ((calendarList.get(i+1).getTime()) - calendarList.get(i).getTime()  > 60*60*1000) {
//					setCanaryData(ServicesUtil.convertFromZoneToZoneString(ServicesUtil.getDateWithInterval(calendarList.get(i),60,MurphyConstant.MINUTES), null , "", MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T),ServicesUtil.convertFromZoneToZoneString1(calendarList.get(i+1), null , "", MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T, 60));
//				}
//			}	o
//			response = MurphyConstant.SUCCESS;
//		} catch (Exception e) {
//			System.err.println("[Murphy][CanaryStagingSchedulerNDV][setMissingData][error]"+e.getMessage());
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//
}