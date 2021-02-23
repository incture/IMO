package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.NDTaskMappingDto;
import com.murphy.taskmgmt.dto.NonDispatchTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.entity.NDTaskMappingDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("NDTaskMappingDao")
@Transactional
public class NDTaskMappingDao extends BaseDao<NDTaskMappingDo, NDTaskMappingDto> {

	private static final Logger logger = LoggerFactory.getLogger(NDTaskMappingDao.class);

	public NDTaskMappingDao() {
	}

	@Autowired 
	NonDispatchTaskDao ndTaskDao;


	@Override
	protected NDTaskMappingDo importDto(NDTaskMappingDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {

		NDTaskMappingDo entity = new NDTaskMappingDo();
		if (!ServicesUtil.isEmpty(fromDto.getMappingId()))
			entity.setMappingId(fromDto.getMappingId());
		if (!ServicesUtil.isEmpty(fromDto.getNdTaskId()))
			entity.setNdTaskId(fromDto.getNdTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		return entity;
	}

	@Override
	protected NDTaskMappingDto exportDto(NDTaskMappingDo entity) {

		NDTaskMappingDto dto = new NDTaskMappingDto();
		if (!ServicesUtil.isEmpty(entity.getMappingId()))
			dto.setMappingId(entity.getMappingId());
		if (!ServicesUtil.isEmpty(entity.getNdTaskId()))
			dto.setNdTaskId(entity.getNdTaskId());
		if (!ServicesUtil.isEmpty(entity.getTaskId()))
			dto.setTaskId(entity.getTaskId());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			dto.setStatus(entity.getStatus());
		return dto;
	}


	public ResponseMessage createInstance(NDTaskMappingDto dto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.ND_TASK+MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			create(dto);
			responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][createInstance][error]"+e.getMessage());
		}

		return responseMessage;

	}

	public ResponseMessage updateStatusOfInstance(UpdateRequestDto dto){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.ND_TASK +MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			String queryString = "update NDTaskMappingDo ndt set ndt.status = '"+dto.getStatus()+"'  where ndt.taskId ='"+dto.getTaskId()+"' and ndt.ndTaskId ='"+dto.getNdTaskId()+"'";

			Query q =  this.getSession().createQuery(queryString.trim());
			int result = q.executeUpdate();
			if(result >0){
				responseMessage.setMessage(MurphyConstant.UPDATE+MurphyConstant.SUCCESSFULLY);
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
			if(MurphyConstant.RESOLVE.equals(dto.getStatus())){
				dto.setNdTaskId(dto.getTaskId());
				if(ndTaskDao.updateStatusOfInstance(dto).getStatusCode().equals(MurphyConstant.CODE_FAILURE)){
					return responseMessage;
				}
			}
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][updateStatusOfInstance][error]"+e.getMessage());
		}
		return responseMessage;

	}

	@SuppressWarnings("unchecked")
	public List<NonDispatchTaskDto> getNDTasksByTaskId(String taskId){

		List<NonDispatchTaskDto> responseDto = new ArrayList<NonDispatchTaskDto>();
		try{
			String queryString ="select nd.TASK_ID,nd.DESCRIPTION,nd.ND_LOC,mp.STATUS from TM_ND_MAPPING mp Inner JOIN TM_NON_DISPTCH nd on mp.ND_TASK_ID = nd.TASK_ID where mp.TASK_ID = '"+taskId+"'";
			Query q =  this.getSession().createSQLQuery(queryString.trim());
			List<Object[]> resultList = q.list();
			if(!ServicesUtil.isEmpty(resultList)){
				NonDispatchTaskDto dto = null;
				for(Object[] obj : resultList){
					dto = new NonDispatchTaskDto();
					dto.setTaskId(ServicesUtil.isEmpty(obj[0])?null:(String) obj[0]);
					dto.setDescription(ServicesUtil.isEmpty(obj[1])?null:((String) obj[1]));
					dto.setLocation(ServicesUtil.isEmpty(obj[2])?null:(String) obj[2]);
					dto.setStatus(ServicesUtil.isEmpty(obj[3])?null:(String) obj[3]);
					responseDto.add(dto);
				}
				return responseDto;
			}
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][getNDTasksByTaskId][error]"+e.getMessage());
		}
		return null;
	}	



	@SuppressWarnings("unused")
	public ResponseMessage changeStatusWhenResolved(String taskId){
//		logger.error("[Murphy][TaskManagement][NDTaskMappingDao][updateStatusOfInstance][entry]");

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{

			String queryString = "update TM_NON_DISPTCH set STATUS='"+MurphyConstant.NON_DISPATCH+"' where TASK_ID IN (Select ndt.ND_TASK_ID from TM_ND_MAPPING ndt where ndt.TASK_ID ='"+taskId+"'  and ndt.STATUS <> 'RESOLVED')";
			String queryString3 = "update TM_ND_MAPPING ndt set ndt.STATUS='"+MurphyConstant.UNRESOLVE+"' where  ndt.STATUS <> 'RESOLVED' and  ndt.TASK_ID ='"+taskId+"'";

			Query q =  this.getSession().createSQLQuery(queryString.trim());
			Query q3 =  this.getSession().createSQLQuery(queryString3.trim());
			int result3 = q3.executeUpdate();
			int result = q.executeUpdate();

//			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][changeStatusWhenResolved][result]"+result+"[result3]"+result3);


			responseMessage.setMessage(MurphyConstant.UPDATE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][changeStatusWhenResolved][error]"+e.getMessage());
		}
		return responseMessage;

	}

	@SuppressWarnings("unused")
	public ResponseMessage changeStatusWhenComplete(String taskId,String status){
//		logger.error("[Murphy][TaskManagement][NDTaskMappingDao][updateStatusOfInstance][entry]");

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{

			String queryString2 = "update TM_NON_DISPTCH set STATUS='"+MurphyConstant.COMPLETE+"' where TASK_ID IN (Select ndt.ND_TASK_ID from TM_ND_MAPPING ndt where ndt.TASK_ID ='"+taskId+"'  and ndt.STATUS = 'RESOLVED')";
			Query q2 =  this.getSession().createSQLQuery(queryString2.trim());
			int result2 = q2.executeUpdate();
			
			if(!MurphyConstant.RESOLVE.equals(status)){
				String queryString = "update TM_NON_DISPTCH set STATUS='"+MurphyConstant.NON_DISPATCH+"' where TASK_ID IN (Select ndt.ND_TASK_ID from TM_ND_MAPPING ndt where ndt.TASK_ID ='"+taskId+"'  and ndt.STATUS <> 'RESOLVED')";
				String queryString3 = "update TM_ND_MAPPING ndt set ndt.STATUS='"+MurphyConstant.UNRESOLVE+"' where  ndt.STATUS <> 'RESOLVED' and  ndt.TASK_ID ='"+taskId+"'";
			
				Query q =  this.getSession().createSQLQuery(queryString.trim());
				Query q3 =  this.getSession().createSQLQuery(queryString3.trim());
				int result3 = q3.executeUpdate();
				int result = q.executeUpdate();
				logger.error("[Murphy][TaskManagement][NDTaskMappingDao][changeStatusWhenResolved][result]"+result+"[result3]"+result3);

			}

//			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][changeStatusWhenComplete][result2]"+result2);


			responseMessage.setMessage(MurphyConstant.UPDATE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][changeStatusWhenComplete][error]"+e.getMessage());
		}
		return responseMessage;

	}





	/*
	 * 
	public ResponseMessage deleteInstance(UpdateRequestDto reqDto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.DELETE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			NDTaskMappingDto dto = new NDTaskMappingDto();
			dto.setMappingId(reqDto.getTaskId());
			delete(dto);
			responseMessage.setMessage(MurphyConstant.DELETE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][deleteInstance][error]"+e.getMessage());
		}

		return responseMessage;

	}
	 *
	 */

	@SuppressWarnings("unchecked")
	public String checkIfInstanceExists(String taskId,String ndTaskId){
		String response = MurphyConstant.FAILURE;
		try{
			String queryString = "Select ndt from NDTaskMappingDo ndt  where ndt.taskId ='"+taskId+"' and ndt.ndTaskId ='"+ndTaskId+"'";

			Query q =  this.getSession().createQuery(queryString.trim());
			List<NDTaskMappingDo> result = q.list();
			if(result.size() >0){
				response = MurphyConstant.EXISTS;
			}else{
				response = MurphyConstant.NOTEXIST;
			}
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][checkIfInstanceExists][error]"+e.getMessage());
		}
//		logger.error("[Murphy][TaskManagement][NDTaskMappingDao][checkIfInstanceExists][response]"+response);
		return response;

	}

	@SuppressWarnings("unchecked")
	public List<String> getAllMappingForTask(String taskId){
		List<String> result = null;
		try{

			String queryString = "Select ndt.ND_TASK_ID from TM_ND_MAPPING ndt  where ndt.TASK_ID ='"+taskId+"' ";

			Query q =  this.getSession().createSQLQuery(queryString.trim());
			result = (List<String>) q.list();
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NDTaskMappingDao][getAllMappingForTask][error]"+e.getMessage());
		}
//		logger.error("[Murphy][TaskManagement][NDTaskMappingDao][getAllMappingForTask][result]"+result);
		return result;

	}

	public String deleteAllMappingForTask(List<String> nonDispatchList,String taskId){
		String result = MurphyConstant.FAILURE;
		if(!ServicesUtil.isEmpty(nonDispatchList)){
			String listString = ServicesUtil.getStringFromList(nonDispatchList);
			try{
				String queryString = "delete from TM_ND_MAPPING ndt  where ndt.TASK_ID ='"+taskId+"' and ndt.ND_TASK_ID IN ("+listString+")";
				Query q =  this.getSession().createSQLQuery(queryString.trim());
				q.executeUpdate();
				result = MurphyConstant.SUCCESS;

			}
			catch(Exception e){
				logger.error("[Murphy][TaskManagement][NDTaskMappingDao][deleteAllMappingForTask][error]"+e.getMessage());
			}
		}else{
			result = MurphyConstant.SUCCESS;
		}
		return result;

	}


	public String updateTaskMappings(List<NonDispatchTaskDto> nonDispatchTasks,String taskId) {
		String response = MurphyConstant.FAILURE;
		List<String> currentTasks = getAllMappingForTask(taskId);

//		logger.error("[currentTasks]"+currentTasks+"[nonDispatchTasks]"+nonDispatchTasks);
		try {

			if(!ServicesUtil.isEmpty(nonDispatchTasks) && !ServicesUtil.isEmpty(currentTasks)){
				response = MurphyConstant.SUCCESS;
			}
			if(!ServicesUtil.isEmpty(nonDispatchTasks)){
//				logger.error("[nonDispatchTasks]"+nonDispatchTasks);
				for(NonDispatchTaskDto dto : nonDispatchTasks){
					/*	MurphyConstant.NOTEXIST.equals(checkIfInstanceExists(taskId,dto.getTaskId()))*/
					if(ServicesUtil.isEmpty(currentTasks) || (!ServicesUtil.isEmpty(currentTasks) && !currentTasks.contains(dto.getTaskId()))){
						UpdateRequestDto updateDto = new UpdateRequestDto();
						updateDto.setStatus(MurphyConstant.DISPATCH);
						updateDto.setTaskId(dto.getTaskId());
						ndTaskDao.updateStatusOfInstance(updateDto);
						NDTaskMappingDto mappingdto= new NDTaskMappingDto();
						mappingdto.setNdTaskId(dto.getTaskId());
						mappingdto.setTaskId(taskId);
						mappingdto.setStatus(MurphyConstant.INPROGRESS);
						create(mappingdto);
					}else if(!ServicesUtil.isEmpty(currentTasks)){
						currentTasks.remove(dto.getTaskId());
					}
				}
			}
			if(!ServicesUtil.isEmpty(currentTasks)){
				response = deleteAllMappingForTask(currentTasks, taskId);
				if(MurphyConstant.SUCCESS.equals(response)){
					response = ndTaskDao.updateBulkStatus(currentTasks,MurphyConstant.NON_DISPATCH);
				}
			}
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][NonDispatchTaskDao][updateTaskMappings][error]"+e.getMessage());
			response = MurphyConstant.FAILURE;
		} 
		return response;
	}


}
