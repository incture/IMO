package com.murphy.taskmgmt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.geotab.NearestUserDto;
import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dao.GeoTabDao;
import com.murphy.taskmgmt.dao.ShiftRegisterDao;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.GeoTabFacadeLocal;
import com.murphy.taskmgmt.util.GeoTabUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("geoTabFacade")
public class GeoTabFacade implements GeoTabFacadeLocal {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GeoTabFacade.class);
	
	public GeoTabFacade() {
		
	}
	
	@Autowired
	private GeoTabDao geoTabDao;

	@Autowired
	private ShiftRegisterDao shiftRegisterDao;
	
	@Override
	public NearestUserDtoResponse getUsers(Double latitude, Double longitude, String locationCode, String groupId,
			String userType, String taskType) {
		NearestUserDtoResponse nearestUserDtoResponseBeforeShiftFilter = new NearestUserDtoResponse();
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			nearestUserDtoResponseBeforeShiftFilter = GeoTabUtil.getUsers(latitude, longitude, locationCode, groupId,
					userType);
			// Shift register changes
			/*
			 NearestUserDtoResponse nearestUserDtoResponseAfterShiftFilter = new NearestUserDtoResponse();
			 List<String> empShift = new ArrayList<String>();
			 List<NearestUserDto> nearestUsersList = new ArrayList<NearestUserDto>();
			 empShift = shiftRegisterDao.getEmpByShift();
			logger.error("[MURPHY][GeoTabFacade][getUsers][empEmailListHavingShift]" + empShift);
				for (NearestUserDto NearestUserDto : nearestUserDtoResponseBeforeShiftFilter.getNearestUsers()) {

					for (String email : empShift) {

						if (NearestUserDto.getEmailId().equalsIgnoreCase(email))
							nearestUsersList.add(NearestUserDto);

					}
				}
				nearestUserDtoResponseAfterShiftFilter.setNearestUsers(nearestUsersList);
			
			logger.error("[MURPHY][GeoTabFacade][getUsers][NearestUserAfterShiftFilter]"
					+ nearestUserDtoResponseAfterShiftFilter); */
			
			if (!ServicesUtil.isEmpty(nearestUserDtoResponseBeforeShiftFilter.getNearestUsers())) {
				responseMessage.setMessage("Nearest users fetched successfully");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				nearestUserDtoResponseBeforeShiftFilter.setResponseMessage(responseMessage);
			}else{
				responseMessage.setMessage("No Operators available");
				responseMessage.setStatus(MurphyConstant.FAILURE);
				responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
				nearestUserDtoResponseBeforeShiftFilter.setResponseMessage(responseMessage);
			}
			

			return nearestUserDtoResponseBeforeShiftFilter;
		} catch (Exception e) {
			logger.error("[MURPHY][GeoTabFacade][getUsers][Exception]" + e.getMessage());
			return nearestUserDtoResponseBeforeShiftFilter;
		}

	}
	
	@Override
	public NearestUserDtoResponse getUsers(String location, String type) {
		return GeoTabUtil.getUsers(location, type);
	}
	
//	@Override
//	public NearestUserDtoResponse getUsers(String muwi) {
//		return GeoTabUtil.getUsers(muwi);
//	}
	
	@Override
	public ArcGISResponseDto getArcGISDetails(String locationCodeOne, String locationCodeTwo, String userId) {
		return geoTabDao.getRoadDistance(locationCodeOne, locationCodeTwo, userId);
	}
	
	
	@Override
	public LocationHierarchyResponseDto getWellUsingEmailId(String emailId)
	{
		return geoTabDao.getWellUsingEmail(emailId);
	}

	@Override
	public NearestUserDtoResponse getRoadDistanceOfUser(String userId , String locationCode) {
		// TODO Auto-generated method stub
		return GeoTabUtil.getUsersRoadDistance(userId, locationCode);
	}
	
}
