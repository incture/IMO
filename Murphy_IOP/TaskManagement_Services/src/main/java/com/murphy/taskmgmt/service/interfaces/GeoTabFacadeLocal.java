package com.murphy.taskmgmt.service.interfaces;

import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;

public interface GeoTabFacadeLocal {

	NearestUserDtoResponse getUsers(String facility, String type);

//	NearestUserDtoResponse getUsers(String muwi);

	ArcGISResponseDto getArcGISDetails(String locationCodeOne, String locationCodeTwo, String userId);

	NearestUserDtoResponse getUsers(Double latitude, Double longitude, String locationCode, String groupId, String userType, String taskType);

	LocationHierarchyResponseDto getWellUsingEmailId(String emailId);

	NearestUserDtoResponse getRoadDistanceOfUser(String userId, String locationCode);
}
