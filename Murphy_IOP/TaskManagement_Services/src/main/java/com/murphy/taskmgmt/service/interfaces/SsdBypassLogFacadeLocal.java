/**
 * 
 */
package com.murphy.taskmgmt.service.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RiskListResponseDto;
import com.murphy.taskmgmt.dto.SsdBypassActivityLogDto;
import com.murphy.taskmgmt.dto.SsdBypassListDto;
import com.murphy.taskmgmt.dto.SsdBypassLogListResponseDto;
import com.murphy.taskmgmt.dto.SsdBypassLogResponseDto;

/**
 * @author Kamlesh.Choubey
 *
 */
public interface SsdBypassLogFacadeLocal {

	public String test();

	ResponseMessage createBypassLog(SsdBypassLogResponseDto dto);

	SsdBypassLogResponseDto getBypassLogById(String bypassId);

	ResponseMessage updateBypassLog(SsdBypassLogResponseDto dto);

	// SsdBypassLogListResponseDto getBypassLogList(String location);

	SsdBypassLogListResponseDto getBypassLogList(String location, int timePeriod, int pageNo, int pageSize,
			String bypassLogStatus , String locationType,boolean isActive);

	//public List<String> bypassLogListForEscalation(int hours);

	// void pushBypassLogNotification(List<String> operatorIds);
	void pushBypassLogNotification(List<String> operatorIds, String alert, String dataJson);

	ResponseMessage sendNotificationForEscalation(int hours, String notificationTime,String zone);

	ResponseMessage sendNotificationForShiftChange(String zone);

	public ResponseMessage createBypassActivityLog(SsdBypassActivityLogDto dto);

	ResponseMessage updateOperatorResponse(String activityLogId, boolean responseValue, String operatorType,
			String operatorId);

	public List<SsdBypassListDto> getBypassLogListByUserGroup(String technicalRole, String businessRole);
	
	public HashMap<String, String> getUserLoginByPid(String pid);
	
	public RiskListResponseDto getRiskLevelList();


}
