package com.murphy.taskmgmt.util;

public interface BlackLineConstants {

//	String BLACKLINE_METHOD_GET = "Get";
//	String TYPE_NAME_DEVICE_SEARCH = "Device";
//	String TYPE_NAME_DEVICESTATUSINFO = "DeviceStatusInfo";
//	String BLACKLINE_METHOD_AUTHENTICATE = "Authenticate";
	Double EARTH_RADIUS_IN_MILES = 3959.00;
	Double WELLS_INSIDE_RADIUS_IN_MILES = 0.0170455; //  30yards =0.0170455 miles
	
	
	String BLACKILINE_AUTHORIZE_URL ="https://connect-live.blacklinesafety.com/1/"
			+ "authorize?response_type=code&client_id=blackline_cleint_id&scope=read+write"; 
	
	String BLACKLINE_ACCESS_TOKEN_URL ="https://connect-live.blacklinesafety.com/1/"
			+ "token?grant_type=authorization_code&code=AuthorizeCode&client_id=blackline_cleint_id"
			+ "&client_secret=blackline_cleint_secret";
	
	String BLACKLINE_BASE_URL = "https://connect-live.blacklinesafety.com/1/device?access_token=AccessToken";
}
