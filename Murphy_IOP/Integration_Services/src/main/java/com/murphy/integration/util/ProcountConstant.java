package com.murphy.integration.util;

public interface ProcountConstant {

	String MS_DB_USERNAME = "iop_user";
	String MS_DB_PASSWORD = "iop$1234!";
	String MS_DB_CONNECTION_URL = "jdbc:sqlserver://10.100.0.123:1433;databaseName=procount_Test";
	//String CLOUD_SUBACCOUNT_NAME = "d7e367960";		//MCQ
	String CLOUD_SUBACCOUNT_NAME = "d998e5467";		//MCD
	String CLOUD_HOUSTON_LOCATION = "houston";
	String CLOUD_HOUST_LOTCATION = "HOUST";
	String ORIGINAL_TIME_ENTERED = "00:00:00";
	int OBJECT_TYPE = 1;
	int COMPRESSOR_OBJECT_TYPE = 4;	// added for compressor downtime
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
	
	//Constant for CommentsTb
	
	int COM_REFERENCE_MERRICK_TYPE = 1;
	int COM_COMMENT_TYPE = 3;
	String COM_COMMENT_PURPOSE = "";
	int COM_RIO_COMMENTCODE = 0;
	String COM_RIO_PROD_COMMENTCODE = "";
	int COM_PRIORITY_TYPE = 0;
	int COM_MESSAGE_SENDFLAG = 0;
	int COM_DESTINATION_PERSON = 0;
	int COM_TEMP_INTEGER = 0;
	int COM_LAST_TRANSMISSION = 57692;
	String COM_LAST_LOADDATE = "1900-01-01 00:00:00.000";
	String COM_LAST_LOADTIME = "00:00:00";
	int COM_TRANSMIT_FLAG = 0;
    String COM_DATETIMESTAMP = "2018-06-28 00:00:00.000";
    String COM_USERDATESTAMP = "2018-06-28 00:00:00.000";
    String COM_USER_TIMESTAMP = "12:50:52";
    int COM_USERID = 10087;
    String COM_ROWUID = "51222AAE-FFA8-4721-984C-474197CA3039";
    int COM_COMMENT_SERVICEID = 0;
    String COMMENT_IDENTIFIER="#$@$#";
    String COM_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    String DISPLAY_DATETIME_FORMAT="MMM dd, yyyy hh:mm:ss a";
	String EXTRACT_DATE_TIME_FORMAT="MM/dd/yyyy hh:mm:ss a";
	String DISPLAY_DATE_FORMAT="MMM dd, yyyy";
	String EXTRACT_DATE_FORMAT="MM/dd/yyyy";
    String Extract="Extract";
    String APP_DISPLAY="Application";
    
    //Prod View Comments
    
    String SVC_IOP="svc_iop";
    String DATEFORMAT_T = "yyyy-MM-dd HH:mm:ss";
    String DATEFORMAT_UTIL="yyyy-MM-dd'T'HH:mm";
	String DATE_DB_FORMATE = "yyyy-MM-dd HH:mm:ss.SSS";
	String UTC_ZONE="UTC";
	String MST_ZONE="MST";

	
	
	/*  Changes based on Production / Dev */
	String SOCKS_PORT ="20004";
	String SOCKS_PORT_NAME = "socksProxyPort";
	String SOCKS_HOST = "localhost";
	String SOCK_HOST_NAME = "socksProxyHost";
	
	
	String CAL_PV30_DB="CAL_PV30";
	String DAILY_REPORTS="DAILY_REPORTS";
	String EFS_CODE="EFS";
	String CA_CODE="CA";
}
