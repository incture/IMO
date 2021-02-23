package com.murphy.taskmgmt.service.interfaces;


import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;

public interface GroupsFacadeLocal {

	TaskOwnersResponeDto getUsersOfGroup(String groupId, String userType,String taskType);

}
