package com.murphy.taskmgmt.service.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.murphy.geotab.UserLocationResponse;
import com.murphy.taskmgmt.dto.NearByTaskListDto;
import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.dto.ObxTaskUpdateDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.TaskOwnersResponeDto;
import com.murphy.taskmgmt.dto.WellVisitMatrixDto;

public interface ObxSchedulerFacadeLocalDummy {

	void generateWellVisitMatrix();

	String updateWellVisitMatrix(WellVisitMatrixDto fieldMatrix, String field);

	HashMap<String, List<String>> CreateUserJobs();
	
	ResponseMessage createOBXTask(String userId, String location);

	UserLocationResponse getUserCurrentLocation(String userId);
	
	public ResponseMessage updateOBXTask(ObxTaskUpdateDto dto);
	
	//Remove
//	Double getDistance(String userId,String locationCode);

	NearByTaskListDto getNearbyAssignedTask(double latitude, double longitude, String userId);

	TaskOwnersResponeDto getOBXUsers(String roles);

	HashMap<String, List<String>> createClusters();

	void divideClusters(List<ObxTaskDto> tasks, List<String> usersList) throws Exception;

	HashMap<String, ArrayList<String>> prepareRoleAndFieldMap();

	ResponseMessage updateTaskSequence(String obxRole);
}
