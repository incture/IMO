package com.murphy.taskmgmt.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dto.UserDetailsDto;
import com.murphy.taskmgmt.service.interfaces.UsermanagementFacadeLocal;
import com.murphy.taskmgmt.util.DestinationUtil;
import com.murphy.taskmgmt.util.MurphyConstant;

@Service("userManagementFacade")
public class UserManagementFacade implements UsermanagementFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(UserManagementFacade.class);
	
	@Override

	public UserDetailsDto getUserDetails(String userName) {

		// password ="Murphy$8090";
		// user = "T000000";
		//https://murphyidp.accounts.ondemand.com

		UserDetailsDto user = new UserDetailsDto();
		user.setIsExists(false);

		try {
			userName = userName.replaceAll("\\s+","");
			userName = userName.replace("'","\\'");
			String url = "/service/scim/Users?filter=userName%20eq%20%27"
					+ userName + "%27";

			String jsonString = DestinationUtil.executeWithDest(MurphyConstant.DEST_IDP, url,
					MurphyConstant.HTTP_METHOD_GET, MurphyConstant.CONTENT_TYPE_SCIM, "","","",true);
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(jsonString);
			long i = (Long) json.get("totalResults");
			
			if (i > 0) {
				user.setIsExists(true);
				JSONArray resources = (JSONArray) json.get("Resources");
				JSONArray emails = (JSONArray) ((JSONObject) resources.get(0)).get("emails");
				user.setEmailId(((JSONObject) emails.get(0)).get("value").toString());
			}

		} catch (Exception e) {
			logger.error("[Murphy][UserManagementFacade][getUserDetails][error]" + e.getMessage());
		}

		return user;
	}
	
	@Override
	public String getTrackDirection(){
		
		String jsonString = "";
		try {
			 String url = "/arcgis/rest/services/Networks/Road_Network_TX/NAServer/Route/solve?stops=-99.0236,28.4644;-98.6424,28.4758;-99.0407,28.2402&returnDirections=true&returnRoutes=true& findBestSequence=true&preserveFirstStop=true&f=json";
			 String mainUrl = url.replaceAll(" ", "%20");
			//String mainUrl = URLEncoder.encode(url, "UTF-8");

			 jsonString = DestinationUtil.executeWithDest(MurphyConstant.DEST_TRACK_DIR, mainUrl,
					 			MurphyConstant.HTTP_METHOD_GET, MurphyConstant.APPLICATION_JSON, MurphyConstant.TENNANT_ID, "", MurphyConstant.ON_PREMISE_PROXY,false);
			
		} catch (Exception e) {
			logger.error("[Murphy][UserManagementFacade][getTrackDirection][error]"+e.getMessage());
		}
		return jsonString ;
	}
	
}
