package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.NonDispatchResponseDto;
import com.murphy.taskmgmt.dto.NonDispatchTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.UpdateRequestDto;
import com.murphy.taskmgmt.entity.NonDispatchTaskDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("NonDispatchTaskDao")
@Transactional
public class NonDispatchTaskDao extends BaseDao<NonDispatchTaskDo, NonDispatchTaskDto> {

	private static final Logger logger = LoggerFactory.getLogger(NonDispatchTaskDao.class);
	public NonDispatchTaskDao() {
	}


	//	@Autowired 
	//	NDTaskMappingDao mappingDao;

	@Override
	protected NonDispatchTaskDo importDto(NonDispatchTaskDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {

		NonDispatchTaskDo entity = new NonDispatchTaskDo();
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			entity.setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getLocation()))
			entity.setLocation(fromDto.getLocation());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedBy()))
			entity.setCreatedBy(fromDto.getCreatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getLocType()))
			entity.setLocType(fromDto.getLocType());
		if (!ServicesUtil.isEmpty(fromDto.getGroup()))
			entity.setGroup(fromDto.getGroup());

		return entity;
	}

	@Override
	protected NonDispatchTaskDto exportDto(NonDispatchTaskDo entity) {

		NonDispatchTaskDto dto = new NonDispatchTaskDto();
		if (!ServicesUtil.isEmpty(entity.getTaskId()))
			dto.setTaskId(entity.getTaskId());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			dto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getLocation()))
			dto.setLocation(entity.getLocation());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			dto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getCreatedBy()))
			dto.setCreatedBy(entity.getCreatedBy());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAtDisplay(ServicesUtil.convertFromZoneToZoneString(entity.getCreatedAt(),null, "" ,MurphyConstant.UTC_ZONE ,"",MurphyConstant.DATE_DISPLAY_FORMAT));
		if (!ServicesUtil.isEmpty(entity.getLocType()))
			dto.setLocType(entity.getLocType());
		if (!ServicesUtil.isEmpty(entity.getGroup()))
			dto.setGroup(entity.getGroup());


		return dto;
	}


	public ResponseMessage createInstance(NonDispatchTaskDto dto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.ND_TASK+ MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			dto.setStatus(MurphyConstant.NON_DISPATCH);
			dto.setCreatedAt(new Date());
			create(dto);
			responseMessage.setMessage(MurphyConstant.ND_TASK+ MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NonDispatchTaskDao][createInstance][error]"+e.getMessage());
		}

		return responseMessage;

	}

	public ResponseMessage deleteInstance(UpdateRequestDto reqDto){

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.ND_TASK+ MurphyConstant.DELETE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			//	NonDispatchTaskDto dto = new NonDispatchTaskDto();
			//	dto.setTaskId(reqDto.getTaskId());
			//	delete(dto);
			reqDto.setStatus(MurphyConstant.DELETED);
			updateStatusOfInstance(reqDto);
			responseMessage.setMessage(MurphyConstant.ND_TASK+ MurphyConstant.DELETE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NonDispatchTaskDao][deleteInstance][error]"+e.getMessage());
		}

		return responseMessage;

	}

	public ResponseMessage updateInstance(NonDispatchTaskDto dto){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.ND_TASK+ MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			//			NonDispatchTaskDo entity =null;
			//			if(MurphyConstant.COMPLETE.equals(dto.getStatus())){
			//				entity = (NonDispatchTaskDo) getSession().load(NonDispatchTaskDo.class,dto.getTaskId());
			//				String user = dto.getCompletedBy();
			//				entity.setCompletedAt(new Date());
			//				entity.setCompletedBy(user);
			//				entity.setStatus(dto.getStatus());
			//				//				update(exportDto(entity));
			//				merge(entity);
			//			}else{
			update(dto);
			//			}
			responseMessage.setMessage(MurphyConstant.ND_TASK+ MurphyConstant.UPDATE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NonDispatchTaskDao][updateInstance][error]"+e.getMessage());
		}

		return responseMessage;

	}

	@SuppressWarnings({ "unchecked" })
	public NonDispatchResponseDto readAllInstance(String group,String location,String locType){
		NonDispatchResponseDto responseDto = new NonDispatchResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		List<NonDispatchTaskDto> responseList  = null;
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);

		try{

			if(group.contains(",")){
				group =	ServicesUtil.getStringFromList(group.split(","));
			}else{
				group = "'"+group+"'";
			}
			String queryString = "Select ndt from NonDispatchTaskDo ndt where ndt.group in ("+group+")";
			if(!ServicesUtil.isEmpty(location)){
				queryString = 	queryString+ " and ndt.location ='"+location+"'";
			}

			if(!ServicesUtil.isEmpty(locType)){
				queryString = 	queryString+ " and ndt.locType ='"+locType+"'";
			}


			queryString = 	queryString+ " and ndt.status = '"+MurphyConstant.NON_DISPATCH+"'  order by ndt.createdAt desc";

			Query q =  this.getSession().createQuery(queryString.trim());
			List<NonDispatchTaskDo> resultList = (List<NonDispatchTaskDo>) q.list();

			if(!ServicesUtil.isEmpty(resultList)){
				responseList = new ArrayList<NonDispatchTaskDto>();
				for(NonDispatchTaskDo entity : resultList){
					responseList.add(exportDto(entity));
				}
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			}
			else{
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NonDispatchTaskDao][readAllInstance][error]"+e.getMessage());
		}
		responseDto.setTaskList(responseList);
		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	public ResponseMessage updateStatusOfInstance(UpdateRequestDto dto){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{

			NonDispatchTaskDo entity = (NonDispatchTaskDo) getSession().load(NonDispatchTaskDo.class,dto.getTaskId() );
			if(!ServicesUtil.isEmpty(entity)){
				if(MurphyConstant.RESOLVE.equals(dto.getStatus())){
					entity.setStatus(dto.getStatus());
					entity.setCompletedAt(new Date());
					entity.setCompletedBy(dto.getUserId());
				}else{
					entity.setStatus(dto.getStatus());
				}
				merge(entity);
				responseMessage.setMessage(MurphyConstant.UPDATE+MurphyConstant.SUCCESSFULLY);
			}else{
				responseMessage.setMessage(MurphyConstant.NO_RECORD);
			}
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e){
			logger.error("[Murphy][TaskManagement][NonDispatchTaskDao][updateStatusOfInstance][error]"+e.getMessage());
		}
		return responseMessage;

	}


	@SuppressWarnings("unused")
	public String updateBulkStatus(List<String> ndTaskList,String status){
		String response = MurphyConstant.FAILURE;
		if(!ServicesUtil.isEmpty(ndTaskList)){
			String listString = ServicesUtil.getStringFromList(ndTaskList);
			try{
				String queryString = "update NonDispatchTaskDo ndt set ndt.status = '"+status+"'  where ndt.taskId IN ("+listString+")";
				Query q =  this.getSession().createQuery(queryString.trim());
				int result = q.executeUpdate();
				response = MurphyConstant.SUCCESS;
			}
			catch(Exception e){
				logger.error("[Murphy][TaskManagement][NonDispatchTaskDao][updateStatusOfInstance][error]"+e.getMessage());
			}
		}

		return response;
	}


	//	Criteria criteria = this.getSession().createCriteria(
	//					NonDispatchTaskDo.class);
	//			 criteria.add(Restrictions
	//					 .eq("createdBy", user));
	//			 if(!ServicesUtil.isEmpty(location)){
	//			 criteria.add(Restrictions
	//					 .eq("location", location));
	//			 
	//			 }
	//			  criteria.add(Restrictions.not(Restrictions
	//					  .eq("status", "COMPLETED")));
	//			 List<NonDispatchTaskDo> resultList = criteria.list();


}

