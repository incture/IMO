package com.murphy.downtime.util;

public interface DowntimeConstant {

	String MS_DB_USERNAME = "iop_user";
	String MS_DB_PASSWORD = "$i0pu$er$";
	String MS_DB_CONNECTION_URL = "jdbc:sqlserver://10.100.0.123:1433;databaseName=procount_Test";
	//For QA
//		String CLOUD_SUBACCOUNT_NAME = "d7e367960";
	//For MCD
	String CLOUD_SUBACCOUNT_NAME = "d998e5467";
	String CLOUD_LOCATION = "houston";
	String ORIGINAL_TIME_ENTERED = "00:00:00";
	int OBJECT_TYPE = 1;
	int UPDOWN_FLAG = 1;
	int DATEENTRY_FLAG = 0;
	float REPAIR_COSTS = 0;
	float LOST_PRODUCTION = 0;
	int CALC_DOWNTIME_FLAG = 0;
	String START_PRODUCTION_DATE = "1900-01-01";
	String END_PRODUCTION_DATE = "1900-01-01";
	String COMMENTS = "Well brought back test 2";
	String REASON = "";
	int MESSAGE_SEND_FLAG = 0;
	int DESTINATION_PERSON = 0;
	String RIO_DOWNTIME_ID = "";
	float LAST_DAY_HOURS_DOWN = 0;
	int DELETE_FLAG = 2;
	int LAST_TRANSMISSION = 0;
	String LAST_LOAD_DATE = "1900-01-01";
	String LAST_LOAD_TIME = "00:00:00";
	int TRANSMIT_FLAG = 0;
	String DATETIMESTAMP = "2017-03-27";
	String BLOGICDATESTAMP = "1900-01-01";
	String BLOGICTIMESTAMP = "00:00:00";
	String USERDATESTAMP = "2017-03-27";
	String USERTIMESTAMP = "08:20:00";
	String USERID = "10015";

	/*  Changes based on Production / Dev */
	String SOCKS_PORT ="20004";
	String SOCKS_PORT_NAME = "socksProxyPort";
	String SOCKS_HOST = "localhost";
	String SOCK_HOST_NAME = "socksProxyHost";
}
