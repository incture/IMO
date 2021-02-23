package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.UserIDPMappingDao;
import com.murphy.taskmgmt.dto.GroupsUserDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.service.interfaces.GroupsFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;


@Service("groupsFacade")
public class GroupsFacade implements GroupsFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(GroupsFacade.class);

	public GroupsFacade() {
	}

	
	@Autowired
	private UserIDPMappingDao userMappingDao;



	@Override
	public TaskOwnersResponeDto getUsersOfGroup(String groupId ,String userType,String taskType){
		ResponseMessage responseMessage = new ResponseMessage();
		TaskOwnersResponeDto responseDto = new TaskOwnersResponeDto();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			//Start-CHG0037344-Inquiry to a field seat.
			if (taskType.equals(MurphyConstant.INQUIRY)) {
				List<GroupsUserDto> userList = userMappingDao.getUsersBasedOnGroupRole(userType);
				if (ServicesUtil.isEmpty(userList)) {
					responseMessage.setMessage(MurphyConstant.NO_RESULT);
				} else {
					responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
					responseDto.setUserList(userList);
				}
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} 
			//End-CHG0037344-Inquiry to a field seat.
			else {
				List<GroupsUserDto> userList = userMappingDao.getUsersBasedOnPOTRole(groupId,userType);
				if (ServicesUtil.isEmpty(userList)) {
					responseMessage.setMessage(MurphyConstant.NO_RESULT);
				}else{
					responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
					responseDto.setUserList(userList);
				}
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("[Murphy][GroupsFacade][getUsersOfGroup][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;
	}

}
