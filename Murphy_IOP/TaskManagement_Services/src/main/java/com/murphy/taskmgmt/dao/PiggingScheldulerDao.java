package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.integration.util.ApplicationConstant;
import com.murphy.taskmgmt.dto.PigUpdateServiceDto;
import com.murphy.taskmgmt.dto.PiggingSchedulerDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.PiggingSchedulerDo;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;




@Repository("PiggingSchedulerDao")
public class PiggingScheldulerDao extends BaseDao<PiggingSchedulerDo, PiggingSchedulerDto> {

	private static final Logger logger = LoggerFactory.getLogger(PiggingScheldulerDao.class);

	@Override
	protected PiggingSchedulerDo importDto(PiggingSchedulerDto fromDto) {
		PiggingSchedulerDo entity=new PiggingSchedulerDo();
		entity.setEquipmentId(fromDto.getEquipmentId());
		entity.setWorkOrderNo(fromDto.getWorkOrderNo());
		entity.setFunctionalLoc(fromDto.getFunctionalLoc());
		entity.setFlag(fromDto.getFlag());
		entity.setTime(fromDto.getTime());
		entity.setPigLaunchStatus(fromDto.getPigLaunchStatus());
		entity.setPigRetrievalStatus(fromDto.getPigRetrievalStatus());
		entity.setWorkOrderStatus(fromDto.getWorkOrderStatus());
		return entity;
	}

	@Override
	protected PiggingSchedulerDto exportDto(PiggingSchedulerDo entity) {
		PiggingSchedulerDto dto=new PiggingSchedulerDto();
		dto.setEquipmentId(entity.getEquipmentId());
		dto.setWorkOrderNo(entity.getWorkOrderNo());
		dto.setFunctionalLoc(entity.getFunctionalLoc());
		dto.setFlag(entity.getFlag());
		dto.setTime(entity.getTime());
		dto.setPigLaunchStatus(entity.getPigLaunchStatus());
		dto.setPigRetrievalStatus(entity.getPigRetrievalStatus());
		dto.setWorkOrderStatus(entity.getWorkOrderStatus());

		return dto;
	}

	public ResponseMessage insertWoData(PiggingSchedulerDto dto) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failed to Insert Data");
		responseMessage.setStatus("FAILURE");
		responseMessage.setStatusCode("1");
		try{
			if(!ServicesUtil.isEmpty(dto)) {
				String duplicateCheck=checkForDuplicates(dto);
				if(duplicateCheck.equalsIgnoreCase("1")){
					responseMessage.setMessage("Duplicate WorkOrders Found While Inserting Data");
					responseMessage.setStatus("Success");
					responseMessage.setStatusCode("0");
				}
				else{
					String equipId=dto.getEquipmentId();
					equipId=equipId.replaceFirst("^0+(?!$)","");
					dto.setEquipmentId(equipId);
					this.getSession().persist(importDto(dto));
					responseMessage.setMessage("Sucessfully Inserted File");
					responseMessage.setStatus(MurphyConstant.SUCCESS);
					responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				}
			}
			else{
				responseMessage.setMessage("Invalid Input");
				responseMessage.setStatus(MurphyConstant.FAILURE);
				responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
			}
		}
		catch(Exception e){
			logger.error("Exception while inserting to DB"+ e.getMessage());
		}
		return responseMessage;
	}


	@SuppressWarnings("unchecked")
	private String checkForDuplicates(PiggingSchedulerDto dto){
		BigInteger count = null;
		String woOrder=null;
		try{
			String query="Select Count(*) FROM PIGGING_SCHEDULER WHERE WORKORDER_NO='"+dto.getWorkOrderNo()+"'";
			Query q = this.getSession().createSQLQuery(query);
			List<BigInteger> response = (List<BigInteger>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				count = response.get(0);
			}
			woOrder = "" + count;
		}
		catch(Exception e){
			logger.error("Exception while Checking for Duplicates"+e.getMessage());
		}
		return woOrder;

	}
	public ResponseMessage updateHistory(String workOrderNumber,String flag, Date time){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failed to Insert Data");
		responseMessage.setStatus("FAILURE");
		responseMessage.setStatusCode("1");

		try{
			String hql="update PiggingSchedulerDo d set d.flag=:flag,d.time=:time where d.workOrderNo=:workOrderNo";
			Query query= this.getSession().createQuery(hql);
			query.setParameter("flag",flag);
			query.setParameter("time",time);
			query.setParameter("workOrderNo",workOrderNumber);
			query.executeUpdate();
			responseMessage.setMessage("Sucessfully Inserted File");
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("0");
		}
		catch(Exception e){
			logger.error("[Murphy][TaskEventsDao][updateHistory][updateQuery]" + e.getMessage());
		}

		return responseMessage;
	}
	@SuppressWarnings("unchecked")
	public List<PiggingSchedulerDto> getByFlag(String flag){
		List<PiggingSchedulerDto> listDto= new ArrayList<>();
		List<PiggingSchedulerDo> listDo=new ArrayList<>();
		try{
			String hql="Select d from PiggingSchedulerDo d where d.flag = :flag";
			Query q = this.getSession().createQuery(hql);
			q.setParameter("flag", flag);
			listDo = q.list();

			/* Criteria criteria= this.getSession().createCriteria(PiggingSchedulerDo.class);
			        criteria.add(Restrictions.eq("flag", flag));
			        listDo=criteria.list();*/

			for(PiggingSchedulerDo dos:listDo){
				listDto.add(exportDto(dos));
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return listDto;
	}


	@SuppressWarnings("unused")
	public String updateWorkOrder(PiggingSchedulerDto dto,String pigtaskType) {
		PigUpdateServiceDto pigJsonObj=new PigUpdateServiceDto();
		ArrayList<PigUpdateServiceDto> pigdtoList=new ArrayList<PigUpdateServiceDto>();	
		int tecoCount=0;
		String status=null;
		String responseStatus=null;
		String pigTaskId=null;
		String pigRetrievalTime=null;
		Date updatedAt=null;
		String actualResolveTime=null;


		try{
			if(!ServicesUtil.isEmpty(dto.getTaskID())) {
				ResponseMessage message = new ResponseMessage();
				String workOrderNo=dto.getWorkOrderNo();
				workOrderNo=("000000000000" + workOrderNo).substring(workOrderNo.length());
				String taskId=dto.getTaskID();
				String selectQuery="Select DOCUMENT_URL,FILE_NAME from "+ApplicationConstant.DOCUMENTSERVICE_SCHEMA+".DOCUMENTS_DETAIL WHERE FILE_NAME LIKE('%"+taskId+"%')";

				Query q = this.getSession().createSQLQuery(selectQuery);
				List<Object[]> response = (List<Object[]>) q.list();
				logger.error("Size of List"+response.size());

				if(MurphyConstant.PIGGING_RECEIVE.equalsIgnoreCase(pigtaskType)){

					Date pigRetrievalStartTime=getCreatedAtForTaskId(taskId);
					Date pigCompletionTime=getResolvedTimeForTaskId(taskId);
					logger.error("Pig Retrieval Time"+pigRetrievalStartTime);

					actualResolveTime=getActualWorkingHours(pigRetrievalStartTime,pigCompletionTime);
					if (!ServicesUtil.isEmpty(response)) {
						for(Object[] obj:response){
							tecoCount++;
							String docUrl=(String) obj[0];
							String fileName=(String)obj[1];
							if(response.size()==tecoCount){
								status=MurphyConstant.TECO_STATUS;
							}
							else{
								status="none";
							}
							pigJsonObj=createHCIJsonObject(docUrl,fileName,status,workOrderNo,actualResolveTime);
							pigdtoList.add(pigJsonObj);
						}
						responseStatus=createJsonArrayObject(pigdtoList,MurphyConstant.PIGGING_RECEIVE);

					}
					else{
						pigJsonObj=createHCIJsonObject("none","none",MurphyConstant.TECO_STATUS,workOrderNo,actualResolveTime);
						pigdtoList.add(pigJsonObj);
						responseStatus=createJsonArrayObject(pigdtoList,MurphyConstant.PIGGING_RECEIVE);
					}
				}
				else{
					if (!ServicesUtil.isEmpty(response)) {
						for(Object[] obj:response){
							tecoCount++;
							String docUrl=(String) obj[0];
							String fileName=(String)obj[1];
							pigJsonObj=createHCIJsonObject(docUrl,fileName,"none",workOrderNo,actualResolveTime);
							pigdtoList.add(pigJsonObj);
						}
						responseStatus=createJsonArrayObject(pigdtoList,MurphyConstant.PIGGING_LAUNCH);

					}
					else{
//						pigJsonObj=createHCIJsonObject("","","none",workOrderNo,actualResolveTime);
//						pigdtoList.add(pigJsonObj);
//						responseStatus=createJsonArrayObject(pigdtoList,MurphyConstant.PIGGING_LAUNCH);
						updateWorkOrderStatus("",workOrderNo,"",MurphyConstant.PIGGING_LAUNCH);
					}
				}


			}
			else{
				logger.error("TaskID cannot be empty");
			}
		}
		catch(Exception e){
			logger.error("[Murphy][PiggingSchedulerDao][updateWorkOrder]"+e.getMessage());
		}
		return responseStatus;


	}


	private PigUpdateServiceDto createHCIJsonObject(String docUrl,String fileName,String status,String workOrderNo,String actualResolveTime){
		PigUpdateServiceDto updateService=new PigUpdateServiceDto();
		try{
			updateService.setDocumentUrl(docUrl);
			updateService.setFileName(fileName);
			updateService.setStatus(status);
			updateService.setWorkOrderNo(workOrderNo);
			updateService.setActualResolveTime(actualResolveTime);
		}
		catch(Exception e){
			logger.error("Exception While creating HCI Object"+e.getMessage());
		}
		return updateService;

	}

	public String createJsonArrayObject(ArrayList<PigUpdateServiceDto> dto,String pigType){
		String updateResponse=null;
		JSONArray aProjects=new JSONArray();
		try{
			for(int i=0;i<dto.size();i++){
				JSONObject jsonObj=new JSONObject();
				jsonObj.put("workOrderNo", dto.get(i).getWorkOrderNo());
				jsonObj.put("fileName", dto.get(i).getFileName());
				jsonObj.put("fileURL", dto.get(i).getDocumentUrl());
				jsonObj.put("status",dto.get(i).getStatus());
				jsonObj.put("actualResolveTime",dto.get(i).getActualResolveTime());
				logger.error("JSONArray Object"+dto.get(i).getWorkOrderNo()+"FileName"+dto.get(i).getFileName());
				aProjects.put(jsonObj);
			}
			updateResponse=createInputJsonForUpdateService(aProjects,pigType);
		}
		catch(Exception e){
			logger.error("Exception While Creating HCI Response Object"+e.getMessage());
		}
		return updateResponse;

	}

	@SuppressWarnings("static-access")
	private String createInputJsonForUpdateService(JSONArray jsonArray,String pigType){
		RestUtil restClass=new RestUtil();
		String urlStatus=null;
		String wrkOrderNo=null;
		String workOrderStatus=null;
		String response=null;
		String entity="{" +"\"record\": " + jsonArray+ "}";
		System.err.println("InputEntity"+entity);
		try{
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			JSONObject jsonObject=restClass.callRest(ApplicationConstant.CPI_SERVICE_URL,entity,MurphyConstant.HTTP_METHOD_POST,ApplicationConstant.CPI_USERNAME,ApplicationConstant.CPI_PASSWORD);
			logger.error("ResponseBody of HCI Interface"+jsonObject);
			JSONObject object=jsonObject.getJSONObject("root");
			Object obj=object.get("piggingSchedulerDtoList");
			if(obj instanceof JSONArray){
				JSONArray jsonAr = (JSONArray)obj;
				for(int i =0;i<jsonAr.length();i++){
					JSONObject jsonObj=jsonAr.getJSONObject(i);
					urlStatus=jsonObj.getString("URL_status");
					wrkOrderNo=jsonObj.getString("workOrderNo");
					workOrderStatus=jsonObj.getString("Teco");
					response=updateWorkOrderStatus(urlStatus,wrkOrderNo,workOrderStatus,pigType);
				}
			}
			else{
				JSONObject responseObj=(JSONObject)obj;
				urlStatus=responseObj.getString("URL_status");
				wrkOrderNo=responseObj.getString("workOrderNo");
				workOrderStatus=responseObj.getString("Teco");
				response=updateWorkOrderStatus(urlStatus,wrkOrderNo,workOrderStatus,pigType);	
			}

		}
		catch(Exception e){
			logger.error("Exception while Calling HCI interface"+e.getMessage());
		}

		return response;
	}

	private String updateWorkOrderStatus(String urlStatus,String wrkOrderNo,String workOrderStatus,String pigType){
		Integer result = null;
		try{
			if(!ServicesUtil.isEmpty(wrkOrderNo) && !ServicesUtil.isEmpty(pigType)){

				if(MurphyConstant.PIGGING_RECEIVE.equalsIgnoreCase(pigType)){

					String updateQuery="Update PIGGING_SCHEDULER SET PIG_RETRIEVAL_STATUS='"+urlStatus+"',WORKORDER_STATUS='"+workOrderStatus+"' WHERE WORKORDER_NO='"+wrkOrderNo+"'";
					Query q = this.getSession().createSQLQuery(updateQuery);
					result = q.executeUpdate();
					logger.error("Update Pig Retrieval Status Query"+updateQuery);
					logger.error("[Murphy][TaskEventsDao][updateTaskStatusResolveToComplete][updateQuery]" + result);
				}
				else{

					String updateQuery="Update PIGGING_SCHEDULER SET PIG_LAUNCH_STATUS='"+urlStatus+"' WHERE WORKORDER_NO='"+wrkOrderNo+"'";
					Query q = this.getSession().createSQLQuery(updateQuery);
					result = q.executeUpdate();
					logger.error("Update Pig Launch Status Query"+updateQuery);
					logger.error("[Murphy][TaskEventsDao][updateTaskStatusResolveToComplete][updateQuery]" + result);

				}
			}
			else{
				logger.error("Empty WorkOrder Found,Cannot Update to DB");
			}

		}
		catch(Exception e){
			logger.error("Exception While Updating WorkOrder status to DB"+e.getMessage());
		}
		if (result > 0)
			return MurphyConstant.SUCCESS;
		else
			return MurphyConstant.NO_RECORD;


	}

	@SuppressWarnings("unused")
	public String getTaskIdForWorkOrder(String workOrderNo){
		String taskId=null;
		logger.error("Fetching TaskId for WOrkOrderNumber");
		try{
			if(!ServicesUtil.isEmpty(workOrderNo)){
				String selectQuery="SELECT TASK_ID FROM TM_TASK_EVNTS WHERE PREV_TASK='"+workOrderNo+"'";
				logger.error("Select Query For Fetching TaskId"+selectQuery);
				Query q = this.getSession().createSQLQuery(selectQuery);
				List<String> taskIdList = (List<String>) q.list();
				if (!ServicesUtil.isEmpty(taskIdList)) {
					for(String str:taskIdList){
						taskId=str;
					}
				}

			}
			else{
				logger.error("Empty Workorder Found,Cannot Fetch TaskId");
			}
		}
		catch(Exception e){
			logger.error("Exception While Fetching TaskId From DB"+e.getMessage());

		}
		return taskId;
	}



	/*@SuppressWarnings("unused")
	public String getPigTimeForTaskId(String taskId){
		String estimatedPigTime=null;
		logger.error("Fetching TaskId for WOrkOrderNumber");
		try{
			if(!ServicesUtil.isEmpty(taskId)){
				String selectQuery="SELECT INS_VALUE FROM TM_ATTR_INSTS WHERE TASK_ID='"+taskId+"' AND ATTR_TEMP_ID='PIG001'";
				logger.error("Select Query For Fetching TaskId"+selectQuery);
				Query q = this.getSession().createSQLQuery(selectQuery);
				List<String> insValue = (List<String>) q.list();
				if (!ServicesUtil.isEmpty(insValue)) {
					for(String str:insValue){
						estimatedPigTime=str;
					}
				}

			}
			else{
				logger.error("Empty Workorder Found,Cannot Fetch TaskId");
			}
		}
		catch(Exception e){
			logger.error("Exception While Fetching TaskId From DB"+e.getMessage());

		}
		return estimatedPigTime;
	}*/

	@SuppressWarnings("unused")
	public String getActualWorkingHours(Date pigStartTime,Date pigResolveTime){
		Date piggingStartTime=null;
		Date PiggingResolveTime=null;
		String actualResolveTime=null;
		String differenceHours=null;
		int totalMinutes=0;
		int hourInMinutes=0;
		int hourInDecimal=0;
		try{
			logger.error("Pig Start Time"+pigStartTime+"Pig Resolve Time"+pigResolveTime);
			piggingStartTime=getFormattedDate(pigStartTime);
			PiggingResolveTime=getFormattedDate(pigResolveTime);
			logger.error("Formatted PigResolveTime"+PiggingResolveTime+"Formatted PigStartTime"+piggingStartTime);
			long difference = PiggingResolveTime.getTime() - piggingStartTime.getTime();
			logger.error("Resolve Time"+difference);
			long minute = Math.abs(difference);   
			int Hours = (int)minute/60;     
			int Minutes = (int)minute%60;  
			int diffMinutes = (int)difference / (60 * 1000) % 60;
			int diffHours = (int)difference / (60 * 60 * 1000) % 24;
			logger.error("DiffMinutes"+diffMinutes+"DiffHours"+diffHours);
			logger.error("TIME "+diffHours+":"+diffMinutes);
			hourInMinutes=diffHours*60;
			totalMinutes=hourInMinutes+diffMinutes;
			actualResolveTime=totalMinutes+"";
			logger.error("ActualResolveTime Pigging:"+actualResolveTime);

		}
		catch(Exception e){
			logger.error("Exception While Calculating Actual Time"+e.getMessage());
		}
		return actualResolveTime;

	}

	public Date getFormattedDate(Date date){
		Date parsedDate=null;
		try{
			SimpleDateFormat formatterDate=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String formatDate=formatterDate.format(date);
			logger.error("FormatDate"+formatDate);
			parsedDate=formatterDate.parse(formatDate);
			logger.error("Parsed Date"+parsedDate);
		}
		catch(Exception e){
			logger.error("Error While Formatting Date"+e.getMessage());
		}
		return parsedDate;
	}


	@SuppressWarnings("unchecked")
	public Date getCreatedAtForTaskId(String taskId){
		Date createdAtTime=null;
		logger.error("[PiggingScheldulerDao][getCreatedAtForTaskId][TaskId]"+taskId);
		try{
			String selectQuery="SELECT CREATED_AT FROM TM_TASK_EVNTS WHERE TASK_ID='"+taskId+"'";
			logger.error("Select Query For Fetching CreatedAt"+selectQuery);
			Query q = this.getSession().createSQLQuery(selectQuery);
			List<Date> createdAt = (List<Date>) q.list();
			logger.error("CreatedAT"+createdAt.size());
			if (!ServicesUtil.isEmpty(createdAt)) {
				for(Date date:createdAt){
					createdAtTime=date;
					logger.error("CreatedAt"+createdAtTime);
				}
			}
		}
		catch(Exception e){
			logger.error("Exception While Fetching CreatedAt"+e.getMessage());
		}


		return createdAtTime;

	}


	@SuppressWarnings("unchecked")
	public Date getResolvedTimeForTaskId(String taskId){
		Date createdAtTime=null;
		logger.error("[PiggingScheldulerDao][getResolvedTimeForTaskId][TaskId]"+taskId);
		try{
			String selectQuery="SELECT CREATED_AT FROM TM_AUDIT_TRAIL WHERE TASK_ID='"+taskId+"' AND ACTION ='RESOLVED'";
			logger.error("Select Query For Fetching ResolvedTime"+selectQuery);
			Query q = this.getSession().createSQLQuery(selectQuery);
			List<Date> createdAt = (List<Date>) q.list();
			logger.error("ResolvedAt"+createdAt.size());
			if (!ServicesUtil.isEmpty(createdAt)) {
				for(Date date:createdAt){
					createdAtTime=date;
					logger.error("ResolvedAt"+createdAtTime);
				}
			}
		}
		catch(Exception e){
			logger.error("Exception While Fetching ResolveTime"+e.getMessage());
		}


		return createdAtTime;

	}
	
	public String checkPigLaunchStatus(String workOrderNo){
		String workOrderNum =null;
		String pigLaunchStatus=null;
		logger.error("checkPigLaunchStatus"+workOrderNo);
		workOrderNum=("000000000000" + workOrderNo).substring(workOrderNo.length());
		try{
			String selectQuery="SELECT PIG_LAUNCH_STATUS FROM PIGGING_SCHEDULER WHERE WORKORDER_NO='"+workOrderNum+"'";
			logger.error("Select Query For Fetching CreatedAt"+selectQuery);
			Query q = this.getSession().createSQLQuery(selectQuery);
			List<String> pigStatusList = (List<String>) q.list();
			logger.error("ResolvedAt"+pigStatusList.size());
			if (!ServicesUtil.isEmpty(pigStatusList)) {
				for(String pigStatus:pigStatusList){
					pigLaunchStatus=pigStatus;
					logger.error("PigLaunchStatus"+pigLaunchStatus);
				}
			}
		}
		catch(Exception e){
			logger.error("Exception While Fetching ResolvedAt"+e.getMessage());
		}


		return pigLaunchStatus;

	}




}
