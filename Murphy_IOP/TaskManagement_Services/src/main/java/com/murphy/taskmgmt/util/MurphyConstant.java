package com.murphy.taskmgmt.util;

public interface MurphyConstant {

	String IOP_PROPERTY_FILE_LOCATION = "application.properties";
	String HYPHEN = "-";
	int PAGE_SIZE = 100;
	String NOT_OWNER = "Not a Owner";
	String EXISTS = "EXISTS";
	String NOTEXIST = "NOT_EXIST";

	
	/* Parent origin of tasks */
	String P_ALARM = "Alarm";
	String P_CUSTOM = "Custom";
	String P_FRAC = "Frac";
	String P_INQUIRY = "Inquiry";
	String P_INVESTIGATION = "Investigation";
	String P_OBX = "OBX";
	String P_PIGGING = "Pigging";
	String P_VARIANCE = "Variance";
	String P_ITA = "ITA_TASK";
	String P_ITA_DOP = "ITA_DOP";
	long TOTAL_TIME = 20L;

	/* Order by */

	String ORDER_BY_ASC = "ASC";
	String ORDER_BY_DESC = "DESC";

	/* Date Formats */

	String Murphy_DATE_FORMATE = "dd MMM yyyy";
	String ALS_COMPLETED_DATE_FORMAT = "dd-MMM-yy";
	String DATE_DB_FORMATE = "yyyy-MM-dd HH:mm:ss.SSS";
	String DATE_DB_FORMATE_SD = "yyyy-MM-dd HH:mm:ss";
	String Murphy_DOWNTIME_DATE_ = "YYYY-MM-DD HH:MI:SS";
	String Murphy_DOWNTIME_DATE_PROCOUNT = "yyyy-MM-dd'T'HH:mm:ss";
	String DETAIL_DATE_FORMATE = "dd MMM YYYY hh:mm:ss";
	String DATEFORMAT_T = "yyyy-MM-dd'T'HH:mm";
	String DATEFORMAT_T_FULL = "yyyy-MM-dd'T'HH:mm:ss'.0000000-05:00'";
	String DATEFORMAT_T_FULL6 ="yyyy-MM-dd'T'HH:mm:ss'.0000000-06:00'";
	String DATEFORMAT_FOR_CANARY = "yyyy-MM-dd'T'HH:mm:ss";
	String DATEFORMAT_T_CANARY = "yyyy-MM-dd'T'HH:mm:ss";
	String DATE_DISPLAY_FORMAT = "dd-MMM-yy hh:mm:ss a";
	String DATE_IOS_FORMAT = "yyyy-MM-dd HH:mm:ss";
	String HRSMINSEC_FORMAT = "HH:mm:ss";
	String DATE_STANDARD = "yyyy-MM-dd";
	String DATE_REVERSE_FULL = "dd-MMM-yy";
	String DISPLAY_DATETIME_FORMAT = "MMM dd, yyyy hh:mm:ss a";
	String EXTRACT_DATE_TIME_FORMAT = "MM/dd/yyyy hh:mm:ss a";
	String DISPLAY_DATE_FORMAT = "MMM dd, yyyy";
	String EXTRACT_DATE_FORMAT = "MM/dd/yyyy";
	// Estimated Pig Retrieval Time Format
	String PIG_DATE_DISPLAY_FORMAT = "hh:mm:ss a";

	/* DATE TYPE */
	String EXTRACT = "Extract";
	String APP_DISPLAY = "Application";

	/* Download Reports */

	String REPORT_EXCEL = "Excel";
	String REPORT_PDF = "PDF";

	String EXCEL_EXT = "xlsx";

	String AUDIT_REPORT = "Audit Report";
	String SHIFT_AUDIT_REPORT="ShiftAuditReport";
	String REPORT_NAME_REQUEST_PROD_VAR = "ProductionVarianceReport";
	String REPORT_NAME_REQUEST_PROD_DGP_VAR = "DGPProductionVarianceReport";
	String REPORT_NAME_REQUEST_AUDIT = "AuditReport";
	String REPORT_NAME_RESPONSE_PROD_VAR = "DOP";
	String REPORT_NAME_RESPONSE_PROD_DGP_VAR = "DGP";
	String REPORT_NAME_RESPONSE_AUDIT = "TaskList";
	String OBX_REPORT = "OBXReport";
	String REPORT_NAME_REQUEST_OBX = "OBXReport";
	String REPORT_NAME_RESPONSE_OBX = "OBXTaskListReport";
	String REPORT_IOP_ADMIN = "IOPAdminReport";
	String REPORT_IOP_ADMIN_REQUEST = "IOPAdminReport";
	String REPORT_IOP_ADMIN_RESPONSE = "IOPAdminListReport";
	String DROPDOWN_REPORT = "DropDownReport";
	String DROPDOWN_CLASSIFICATION_REPORT = "DropDownClassificationReport";
	String REPORT_NAME_REQUEST_CLASSIFICATION = "DropDownClassificationReport";
	String REPORT_NAME_RESPONSE_CLASSIFICATION = "ClassificationReport";
	String REPORT_NAME_REQUEST_DROPDOWN = "DropDownReport";
	String REPORT_NAME_RESPONSE_DROPDOWN = "DropDownDataReport";
	String DROPDOWN_ROOTCAUSE_REPORT = "rootCauseDropDownReport";

	/* Date Time */

	String DAYS = "days";
	String HOURS = "hours";
	String MINUTES = "minute";
	String SECONDS = "seconds";
	String MILLISEC = "ms";

	/* Time Zones */

	String CST_ZONE = "CST";
	String UTC_ZONE = "UTC";
	String IST_ZONE = "IST";
	String MST_ZONE = "MST";//Canada/Mountain
	String PST_ZONE = "PST";

	/* Headers */

	String HTTP_METHOD_PUT = "PUT";
	String HTTP_METHOD_POST = "POST";
	String HTTP_METHOD_GET = "GET";
	String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	String HTTP_HEADER_ACCEPT = "Accept";
	String APPLICATION_JSON = "application/json";

	/* Status */

	// UPADTED BY- ADMIN
	String IOP_ADMIN = "IOP_ADMIN";
	String NEW_TASK = "NEW";
	String REVOKED = "REVOKED";
	String DELETED = "DELETED";
	String CREATED = "CREATED";
	String COMPLETE = "COMPLETED";
	String RETURN = "RETURNED";
	String ASSIGN = "ASSIGNED";
	String REASSIGN = "RE ASSIGNED";
	String FORWARD = "FORWARDED";
	String RESOLVE = "RESOLVED";
	String UNRESOLVE = "UN RESOLVED";
	String NON_DISPATCH = "NON DISPATCH";
	String DISPATCH = "DISPATCHED";
	String INPROGRESS = "IN PROGRESS";
	String SUBMIT = "SUBMITTED";
	String ACTIVE = "ACTIVE";
	String DRAFT = "DRAFT";
	String CANCELLED = "CANCELLED";

	// FOR DISPATCH TASK
	String DISPATCH_TASK = "Dispatch";

	String DOWNTIME_SUBMITTED = "SUBMITTED";
	String DOWNTIME_REVIEW = "REVIEW";
	String DOWNTIME_DESIGNATED = "DESIGNATED";
	String DOWNTIME_PENDING = "PENDING";
	String DOWNTIME_COMPLETE = "COMPLETED";

	/* Response Messages */

	String NO_RECORD = "No Record Exists with that Id";
	String MAND_MISS = "Mandatory Fields are missing";
	String NO_RESULT = "No Data Found";
	String INTERNAL_ERROR = "Internal Error";

	String ND_TASK = "Non Dispatch task ";
	String TASK = "Task ";

	String UPDATED = "Updated";
	String CLOSED = "Closed";

	String SUCCESSFULLY = "Successfully";

	String CODE_FAILURE = "1";
	String CODE_SUCCESS = "0";

	String SUCCESS = "SUCCESS";
	String FAILURE = "FAILURE";

	String UPDATE = "Update";
	String CREATE = "Create";
	String DELETE = "Delete";
	String READ = "Read";

	String CREATED_SUCCESS = "created successfully";
	String UPDATE_SUCCESS = "updated successfully";
	String FORWARD_SUCCESS = "forwarded successfully";
	String DELETE_SUCCESS = "deleted successfully";
	String READ_SUCCESS = "Data fetched successfully";
	String SAVE_SUCCESS = "Data saved successfully";
	String ACKNOWLEDGED_SUCCESS = "Acknowledged successfully";

	String CREATE_FAILURE = "creation failed";
	String UPDATE_FAILURE = "updation failed";
	String DELETE_FAILURE = "deletion failed";
	String READ_FAILURE = "Data fetch failed";
	String SAVE_FAILURE = "Data save failed";
	String ACKNOWLEDGED_FAILURE = "Acknowledge failed";

	/* ROC Roles */

	String ROLE_CATARINA = "IOP_TM_ROC_Catarina";
	String ROLE_WTILDEN = "IOP_TM_ROC_WestTilden";
	String ROLE_CTILDEN = "IOP_TM_ROC_CentralTilden";
	String ROLE_KARNES = "IOP_TM_ROC_Karnes";
	String KAYBOB_USER= "IOP_TM_ROC_Kaybob";
	String MONTNEY_USER= "IOP_TM_ROC_Montney";

	/* IDP */

	String CONTENT_TYPE_SCIM = "scim+json";
	String DEST_IDP = "MurphyCloudIdPDest";

	/* Competency */

	String PRO_A = "PRO Operator A";
	String PRO_B = "PRO Operator B";
	String OBX_B = "OBX Operator B";
	String OBX_C = "OBX Operator C";
	String SSE = "SSE";

	/* Socks */

	String SOCKS_PORT = "20004";
	String SOCKS_PORT_NAME = "socksProxyPort";
	String SOCKS_HOST = "localhost";
	String SOCK_HOST_NAME = "socksProxyHost";

	/* Destinations */

	String DEST_TRACK_DIR = "TrackDirection";
	String ON_PREMISE_PROXY = "OnPremise";
	String LOCATION_ID = "HOUST";

	/* Canary */

	// String CANARY_API_USERID_REF = "17";
	// String CANARY_API_PASSWORD_REF = "18";

	//String CANARY_USERNAME = "Murphy_API_02";
	//String CANARY_PASSWORD = "erONgD5Ad1L2";
	String CANARY_TIMEZONE = "Central Standard Time";
	String CANARY_APP = "Murphy IOP - QA";
	
	String CANARY_USERNAME = "Murphy_api_iop_dev";
	String CANARY_PASSWORD = "I9bSDzuuaDc0";
	
	// String CANARY_USERNAME = "mexcel01";
	// String CANARY_PASSWORD = "yiNI6SdvGDaa";
	// String CANARY_APP = "API Test";

	// Change for PROD
    //String CANARY_USERNAME = "Murphy_api_iop_prod";
    //String CANARY_PASSWORD = "sTWqJOcOh7ch";
	//String CANARY_APP = "Murphy IOP Prod";
	//String CANARY_API_HOST = "https://murphyapi.canarylabs.online:55236/"; //"https://murphy.canarylabs.online:55236/";

	String CANARY_API_HOST = "https://murphyapi.canarylabs.online:55236/";
	String CANARY_API_USERID_REF = "CANARY_API_USERID";
	String CANARY_API_PASSWORD_REF = "CANARY_API_PASSWORD";

	/* CRT Canary Credentials */
	String CRT_CANARY_API_USERID_REF = "CRT_CANARY_API_USERID";
	String CRT_CANARY_API_PASSWORD_REF = "CRT_CANARY_API_PASSWORD";
	String CRT_CANARY_API_HOST = "CRT_CANARY_API_HOST";
	String CRT_CANARY_APP = "CRT_CANARY_APP";

	String DEST_CANARY = "CanaryAWSTest";
	String TENNANT_ID = "d998e5467";

	String[] CANARY_PARAM = { "PRCASXIN", "PRTUBXIN", "PRSTAXIN", "QTGASD", "QTOILD", "QTH2OD" };
	String[] CANARY_PARAM_NDV = { "QTOILD", "QTH2OD" };
	String[] CANARY_PARAM_REMAINING_NDV = { "PRCASXIN", "PRTUBXIN", "PRSTAXIN", "QTGASD" };
	String[] CANARY_PARAM_PV = { "QTOILD" };
	String[] CANARY_CANADA_PARAM = {"SEP_CNDTOTTDY","SEP_PRDTOTTDY", "AFLOW", "PG_FLOWD"};
	String[] CANARY_PARAM_DOP_EFS_CANDADA= {"QTOILD", "SEP_CNDTOTTDY","SEP_PRDTOTTDY", "AFLOW", "PG_FLOWD" };

	String AGGR_NAME_TOTAL = "Total";
	String AGGR_NAME_AVG = "Average";
	String AGGR_NAME_MAX = "Maximum";
	String AGGR_NAME_MIN = "Minimum";
	String CANARY_TEST_APP = "API Test";
	String DOWNTIME_CLASSIFICATION = "Downtime";

	int BATCH_PERSIST_SIZE = 50;

	/* Cygnet */

	String CYGNET_SCHEMA_PROPERTY_NAME = "cygnet.schema";

	/* Alarms */

	String ALARM_NON_DOWNTIME = "Non Downtime";
	String ALARM_DESIGNATE = "DESIGNATE";
	String ALARM_DISPATCH = "DISPATCH";
	String ALARM_ACKNOWLEDGE = "ACKNOWLEDGE";
	String ALARM_SEVERITY_1_ALARM = "1-Alarm";

	/* Task Types */

	String HUMAN = "Human";
	String SYSTEM = "SYSTEM";

	/* Investigation Task */

	String TEMP_ID_VAR_OBS = "VAR_OBS";
	String TEMP_ID_VARIANCE = "VARIANCE";
	String TEMP_ID_OBSERVATION = "TASK_OBS','PROCESS_OBS";
	String TEMP_ID_TASK_OBS = "TASK_OBS";
	String TEMP_ID_PROCESS_OBS = "PROCESS_OBS";

	/* Inquiry Task */

	String TEMP_ID_INQUIRY = "INQ";
	String TEMP_ID_TASK_INQUIRY = "TASK_INQ";
	String TEMP_ID_PROC_INQUIRY = "PROC_INQ";
	String TEMP_ID_INQUIRY_OBS = "TASK_INQ','PROC_INQ";

	/* User Types */

	String USER_TYPE_POT = "POT";
	String USER_TYPE_ALS = "ALS";
	String USER_TYPE_ENG = "ENG";
	String USER_TYPE_ROC = "ROC";
	String USER_TYPE_RE = "RE";
	String USER_TYPE_WW = "WW";
	String USER_TYPE_FIELD = "Field_Task_User";
	String USER_TYPE_ENG_POT = "ENG_POT";

	/* Custom Attr Labels */

	String ASSIGNEDTO = "Assign to person(s)";
	String LOCATION = "Location";
	String CLASSIFICATION = "Task Classification";
	String SUB_CLASSIFICATION = "Sub Classification";
	String ISSUE_CLASSIFICATION = "Issue Classification";
	String ISSUE_CLASSIFICATION_ID = "INQ05";
	String STATUS = "Status";
	String ND_ASSIGNEDTO_GRP = "Assign To group";
	String PRIMARY_CLASSIFICATION = "Issue Classification";
	String SECONDARY_CLASSIFICATION = "Sub Classification";
	String TIER = "Tier";
	String START_DATE = "Last Prod Date";

	/* Power BI */

	// String POWER_BI_CLIENTID_REF = "1";
	// String POWER_BI_GROUPS_REF = "2";
	// String POWER_BI_REPORTS_REF = "3";
	// String POWER_BI_USERNAME_REF = "4";
	// String POWER_BI_PASSWORD_REF = "5";
	// String POWER_BI_GRANTTYPE_REF = "6";
	// String POWER_BI_RESOURCE_REF = "7";
	// String POWER_BI_INVERSTIGATION_REPORTS_REF = "13";
	// String POWERBI_VARIANCE_REPORTID = "16";
	// String POWER_BI_FRAC_REPORTS_REF = "40";

	String POWER_BI_CLIENTID_REF = "POWERBI_CLIENTID";
	String POWER_BI_GROUPS_REF = "POWERBI_GROUPID";
	String POWER_BI_USERNAME_REF = "POWERBI_USERNAME";
	String POWER_BI_PASSWORD_REF = "POWERBI_PASSWORD";
	String POWER_BI_GRANTTYPE_REF = "POWERBI_GRANTTYPE";
	String POWER_BI_RESOURCE_REF = "POWERBI_RESOURCE";

	String POWERBI_INVESTIGATION_REPORTID_7 = "INVESTIGATION_REPORTID_7";
	String POWERBI_INVESTIGATION_REPORTID_30 = "INVESTIGATION_REPORTID_30";
	String POWERBI_ALARM_REPORTID = "ALARM_REPORTID";
	String POWERBI_VARIANCE_REPORTID = "VARIANCE_REPORTID";
	String POWERBI_FRAC_REPORTID = "FRAC_REPORTID";

	String POWER_BI_BASE_URL = "https://login.microsoftonline.com/murphyoilcorp.com/oauth2/token";
	String POWER_BI_TOKEN_BASE_URL = "https://api.powerbi.com/v1.0/myorg/";
	String POWER_BI_URL_GROUPS = "groups/";
	String POWER_BI_URL_REPORTS = "/reports/";
	String POWER_BI_URL_GEN_TOKEN = "/GenerateToken";
	String POWER_BI_TOKEN_GEN_BODY = "{\"accessLevel\": \"View\"}";

	String POWER_BI_VARIENCE = "VARIENCE";
	String POWER_BI_ALARM = "ALARM";
	String POWER_BI_INVESTIGATION_7 = "INVESTIGATION7";
	String POWER_BI_INVESTIGATION_30 = "INVESTIGATION30";
	String POWER_BI_FRAC = "FRAC";

	/* Task Origin Types */

	String INVESTIGATON = "Investigation";
	String VARIANCE = "Variance";
	String ALARM = "Alarm";
	String CUSTOM = "Custom";
	String INQUIRY = "Inquiry";
	String DISPATCH_ORIGIN = "Dispatch";

	/* Hierarchy */

	String FIELD = "Field";
	String FACILITY = "Facility";
	String FACILITY_DB = "Central Facility";
	String WELLPAD = "Well Pad";
	String WELL = "Well";
	String CHILD = "CHILD";
	String PARENT = "PARENT";
	String BASE = "BASE";
	String COMPRESSOR = "Compressor";
	String FLARE = "Meter";
	String WELLPAD_COMPRESSOR = "WELLPAD_COMPRESSOR";
	String SEARCH = "SEARCH";

	/* ArcGIS Constants */

	String ARCGIS_SERVICE_URL = "/arcgis/rest/services/NETWORKS/Road_Network_TX/NAServer/Route/solve?stops=";
	//String ARCGIS_SERVICE_URL = "/arcgis/rest/services/Networks/Road_Network_TX/NAServer/Route/solve?stops=";
	String ARCGIS_SERVICE_URL_PAYLOAD = "&returnDirections=true&returnRoutes=false&findBestSequence=true&preserveFirstStop=true&f=json";
	String ARCGIS_SERVICE_BEST_ROUTE_PAYLOAD_WITH_FIRSTSTOP = "&impedanceAttributeName=Time&restrictUTurns=esriNFSBAllowBacktrack&useHierarchy=false&returnDirections=false&returnRoutes=true&returnStops=true&returnBarriers=false&returnPolylineBarriers=false&returnPolygonBarriers=false&outputLines=esriNAOutputLineStraight&findBestSequence=true&preserveFirstStop=true&preserveLastStop=false&useTimeWindows=false&startTime=0&startTimeIsUTC=false&outputGeometryPrecisionUnits=esriDecimalDegrees&directionsOutputType=esriDOTComplete&directionsTimeAttributeName=Time&directionsLengthUnits=esriNAUMiles&f=json";
	String ARCGIS_SERVICE_BEST_ROUTE_PAYLOAD = "&impedanceAttributeName=Time&restrictUTurns=esriNFSBAllowBacktrack&useHierarchy=false&returnDirections=false&returnRoutes=true&returnStops=true&returnBarriers=false&returnPolylineBarriers=false&returnPolygonBarriers=false&outputLines=esriNAOutputLineStraight&findBestSequence=true&preserveFirstStop=false&preserveLastStop=false&useTimeWindows=false&startTime=0&startTimeIsUTC=false&outputGeometryPrecisionUnits=esriDecimalDegrees&directionsOutputType=esriDOTComplete&directionsTimeAttributeName=Time&directionsLengthUnits=esriNAUMiles&f=json";
	String ARCGIS_DEST_NAME = "TrackDirection";
	String ARCGIS_API_HOST = "http://arcgis01.murphyoilcorp.org:6080/";
	//String ARCGIS_API_HOST = "http://houqgisprod:6080/";

	/* GeoTab */

	// String GEOTAB_USERNAME_REF = "8";
	// String GEOTAB_PASSWORD_REF = "9";
	// String GEOTAB_DATABASE_REF = "10";
	// String GEOTAB_SESSION_ID_REF = "11";
	// String GEOTAB_SESSION_ID_REFRESH_DATE_REF = "12";
	//
	String GEOTAB_USERNAME_REF = "GEOTAB_USERNAME";
	String GEOTAB_PASSWORD_REF = "GEOTAB_PASSWORD";
	String GEOTAB_DATABASE_REF = "GEOTAB_DATABASE";
	String GEOTAB_SESSION_ID_REF = "GEOTAB_SESSIONID";
	String GEOTAB_SESSION_ID_REFRESH_DATE_REF = "GEOTAB_SESSIONID_REFRESH_DATE";
	String GEOTAB_SESSION_ID_PROPERTY_NAME = "geotab.sessionId";

	String CURRENT = "Current";
	String BEFORE = "Before";
	String AFTER = "After";

	/* BlackLine */
//EFS
	String BLACKLINE_CLIENT_ID = "83d1a9e0922d1f2ebbbf";
	String BLACKLINE_CLIENT_SECRET = "0c0b73713613be15bc7cb39db5985efbee18c3ee";
	
	//Montney
	String MONTNEY_BLACKLINE_CLIENT_ID = "181a8ac2e1e31d24003d";
	String MONTNEY_BLACKLINE_CLIENT_SECRET = "6ac7c1bf1515d69a5c25aaa0c769df352d04de16";
	
	//Kaybob
	String KAYBOB_BLACKLINE_CLIENT_ID = "93d06bdd732f20d0176c";
	String KAYBOB_BLACKLINE_CLIENT_SECRET = "722dadd75aaff4faa7094bb16d231cd5da465dbb";

	// String BLACKLINE_USERNAME_REF = "BLACKLINE_USERNAME";
	// String BLACKLINE_PASSWORD_REF = "BLACKLINE_PASSWORD";
	// String BLACKLINE_DATABASE_REF = "BLACKLINE_DATABASE";
	// String BLACKLINE_SESSION_ID_REF = "BLACKLINE_SESSIONID";
	// String BLACKLINE_SESSION_ID_REFRESH_DATE_REF =
	// "BLACKLINE_SESSIONID_REFRESH_DATE";

	/* ALS Staging */

	String PWR = "PWR Date";
	String SHUT_IN = "Shut In Date";
	String RETURN_TO_PROD = "RTP Date";
	String JOB_COMP = "Job Comp Date";
	String SOURCE_ALS = "ALS";
	String SOURCE_IOP = "IOP";
	String INVESTIGATION_STARTED = "Investigation Started";

	/* Image processing */

	String FILE_TYPE_PNG = "image/png";
	String FILE_TYPE_JPG = "image/jpg";
	String FILE_TYPE_JPEG = "image/jpeg";
	String FILE_TYPE_COMPRESSED = "Compressed";
	String FILE_TYPE_FILE_DOC = "Complete";
	String IMAGE_FORMAT_NOT_SUPPORTED = "Image format not Supported";

	/* Cache constants */

	// String TENANT_ID_REF = "14";
	// String LOCATION_ID_HOUST_REF = "15";

	String TENANT_ID_REF = "TENANT_ID";
	String LOCATION_ID_HOUST_REF = "LOCATION_ID";
	String APPLICATION_TOKEN_REF_CONSTANT = "applicationToken";
	String EMBEDDED_TOKEN_REF_CONSTANT = "embeddedToken";
	int AUTOSIGNIN_TIME_PARKED_CONSTANT = 300;

	/* DOP */

	String DOP_TABLE = "TM_PRODUCTION_VARIANCE_STAGING";
	String DOP_CANARY = "Actual (Canary)";
	String DOP_PROJECTED = "Projected (HANA)";
	String DOP_FORECAST = "Forecast (Enersight)";
	String DAILY = "Daily";
	String WEEKLY = "Weekly";
	String ZONE_OFFSET_CANARY_EFS_OIL="EFS_QTOILD_ZONE_CANARY";
	String ZONE_OFFSET_CANARY_EFS_GAS="EFS_QTGASD_ZONE_CANARY";
	String ZONE_OFFSET_CANARY_CA_OIL="CA_SEP_CNDTOTTDY_ZONE_CANARY";
	String ZONE_OFFSET_CANARY_CA_KAYBOB_GAS="CA_SEP_PRDTOTTDY_ZONE_CANARY";
	String ZONE_OFFSET_CANARY_CA_MONTNEY_GAS_ONE="CA_AFLOW_ZONE_CANARY";
	String ZONE_OFFSET_CANARY_CA_MONTNEY_GAS_TWO="CA_PG_FLOWD_ZONE_CANARY";
	String RESET_HOUR_EFS_OIL="EFS_QTOILD_REST_TIME_CANARY";
	String RESET_HOUR_EFS_GAS="EFS_QTGASD_REST_TIME_CANARY";
	String RESET_HOUR_CA_OIL="CA_SEP_CNDTOTTDY_REST_TIME_CANARY";
	String RESET_HOUR_CA_KAYBOB_GAS="CA_SEP_PRDTOTTDY_REST_TIME_CANARY";
	String RESET_HOUR_CA_MONTNEY_GAS_ONE="CA_AFLOW_REST_TIME_CANARY";
	String RESET_HOUR_CA_MONTNEY_GAS_TWO="CA_PG_FLOWD_REST_TIME_CANARY";
	

	/* PW Hopper */

	String PW_HOPPER = "PW Hopper";
	String TRUE = "true";
	String PWH_PERCENT_VARIANCE = "PWH_Percent_Variance";
	String PWH_DURATION_IN_DAYS = "PWH_Duration_In_Days";
	String PWH_VERSION = "PWH_Version";
	String DATATYPE_CB = "CheckBox";
	String DATATYPE_INPUT = "Input";
	String CHECKLIST = "CheckList";

	String RED = "#fd5959";
	String GREY = "#808080";
	String GREEN = "#00b500";
	String ORANGE = "#ffa500";
	String[] HOPPER_FIELDS = { "MUR-US-EFS-TN00", "MUR-US-EFS-TC00", "MUR-US-EFS-TE00", "MUR-US-EFS-TW00",
			"MUR-US-EFS-KN00", "MUR-US-EFS-KS00", "MUR-US-EFS-CT00" };
	String[] CA_HOPPER_FIELD ={ "MUR-CA-KAY" , "MUR-CA-MNT" };

	/* Task Scheduling */

	String COMPLETED_TASK_DAYS_COUNT = "Completed_Task_Days_Count";

	/* Well Tiers */
	String TIER_A = "Tier A";
	String TIER_B = "Tier B";
	String TIER_C = "Tier C";

	/* OBX */

	int NUMBER_OF_DAYS = 5;
	String OBX_BUSINESS_ROLE = "IOP_SYSTEM";
	String OBX_SUB_CLASSIFICATION = "OBX Well Stop";
	String OBX = "OBX";
	// int OBX_RESOLVE_TIME=20;
	String OBX_CREATOR = "System User";
	String OBX_DAY_CAPACITY = "OBX_DAY_CAPACITY";
	String[] TILDEN_FIELDS = { "MUR-US-EFS-TN00", "MUR-US-EFS-TC00", "MUR-US-EFS-TE00", "MUR-US-EFS-TW00" };
	String[] KARNES_FIELDS = { "MUR-US-EFS-KN00", "MUR-US-EFS-KS00" };
	String[] CATARINA_FIELDS = { "MUR-US-EFS-CT00" };

	/* OBX Schedule */
	String DURATION_STOP_BY_WELLS = "DURATION_STOP_BY_WELLS";
	String NUM_OBX_OPERATOR_EFS = "NUM_OBX_OPERATOR_EFS";
	String SHIFT_DURATION = "SHIFT_DURATION";
	String UPLOAD_WORKFACTOR_PERCENT = "UPLOAD_WORKFACTOR_PERCENT";
	String LAST_UPDATED_DATE_TIME = "LAST_UPDATED_DATE_TIME";
	String OBX_ENGINE_UPDATED_BY = "OBX_ENGINE_UPDATED_BY";
	String OBX_ENGINE_RUNNING_FLAG = "OBX_ENGINE_RUNNING_FLAG";
	String TIME_EXCEEDED = "Operators cannot be reallocated after 5.30 AM";
	String ALL = "All";
	String[] DAY = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

	/* Pigging */
	double LATERAL_FACTOR = 1.5;
	int DEFAULT_FREQUENCY = 7;
	String PIGGING = "Pigging";
	String PIGGING_LAUNCH = "Pigging Launch";
	String PIGGING_RECEIVE = "Pigging Retrieval";
	String DATE_TIME = "Date Time";
	String EQUIPMENT = "Equipment";
	String EQUIPMENT_DESCRIPTION = "Equipment Description";
	String TASK_CREATED = "Task Created";
	String TASK_NOT_CREATED = "Task Not Created";
	String NEW = "New";
	String TECO_STATUS = "TECO";

	/* Frac Well */
	String FRAC_CRITICAL = "Critical";
	String FRAC_NORMAL = "Normal";

	/* Email Notification */
	String SMTP_AUTH = "mail.smtp.auth";
	String SMTP_TTLS = "mail.smtp.starttls.enable";
	String MAIL_HOST = "mail.smtp.host";
	String SMTP_PORT = "mail.smtp.port";
	String TRANSPORT_PROTOCOL = "mail.transport.protocol";
	String BOUNCER_ID = "swathisathiyanarayanan@gmail.com";
	String BOUNCER_PORT = "mail.smtp.from";
	String SENDER_MAILID = "noreply_sapnotification@murphyoilcorp.com";
	String SENDER_PASSWORD = "Cor11972";
	// String SENDER_MAILID = "swathi.vs@incture.com";
	// String SENDER_PASSWORD = "sai@1ganesh";
	String MAIL_SUBJECT = "Initial Incident report";
	String MAIL_UTF8_CONTENT_TYPE = "text/html; charset=utf-8";
	String EMAIL_ALERT_ON = "true";
	String EMAIL_ALERT_OFF = "false";
	String EMAIL_RSL_CONTENT_INQUIRY = "Inquiry raised by you have been resolved. Please";
	String EMAIL_CLSD_CONTENT_INQUIRY = "Inquiry raised by you have been Closed.";
	String EMAIL_USERS = "'Engineer_East','Engineer_West','Engineer_West,Engineer_East','Engineer_East,Engineer_West'";

	/* Bubble Notification */
	String NO_ACK = "false";
	String BYPASS_CREATION = "Created";
	String SHIFT_CHANGE = "ShiftChange";
	String BYPASS_REJECT = "Bypass Denied";
	String SAFETY_REQ_ACCEPTED = "Accepted";
	String BP_CREATION_MSG = "Bypass Created";
	String BP_SHIFTCHNG = "Operator Shift Change in progress. New Responsible Operator needs to be assigned for Bypass to continue.";
	String EI_SHIFTCHNG = "Operator Shift Change in progress. New Responsible Operator needs to be assigned for EnergyIsolation to continue.";
	String BP_REJECT_MSG = "Bypass Rejected";
	String BY_PASS = "BypassLog";
	String ENRGY_ISOLATION = "EnergyIsolation";
	String BP_CRITICAL = "Critical";
	String BP_REGULAR = "Regular";
	String BP_HIGH = "High";
	String BP_LOW = "Low";
	String BP_MEDIUM = "Medium";

	/* Bypass log */
	// String BYPASS_ATTACHMENT_FOLDER_NAME = "test";
	String BYPASS_ATTACHMENT_FOLDER_NAME = "SCPApps/Iop/Bypasslog";
	Integer BYPASS_LOG_DATA_FOR_DAYS = 7;
	// String HSE_DOCUMENT_FOLDER_NAME = "test";
	String HSE_DOCUMENT_FOLDER_NAME = "SCPApps/Iop/HSEFieldGuide";

	/* Push notification */
	// MCQ
	 String PUSH_NOTIFICATION_URL = "https://mobile-d7e367960.us2.hana.ondemand.com/restnotification/application/com.incture.taskmanagement/user";
	 String PUSH_NOTIFICATION_USERNAME = "P000989";
	 String PUSH_NOTIFICATION_PASSWORD = "Murphy@123";

//	// MCP
//	String PUSH_NOTIFICATION_URL = "https://mobile-dee8964f1.us2.hana.ondemand.com/restnotification/application/com.incture.taskmanagement/user";
//	String PUSH_NOTIFICATION_USERNAME = "P000659";
//	String PUSH_NOTIFICATION_PASSWORD = "Murphy$8090";
	 
	 /*Touchless Token api parameters*/
	// MCD
//	String client_id = "762738760636-57u1gahtdaftt06bh37u2sqkio6nsqma.apps.googleusercontent.com";
//    String client_secret="ScvCK9qR6ACA0VWsapTBeLaP";
//	String refresh_token="1//0gdt14yve8kBtCgYIARAAGBASNwF-L9IrPHXMwyWtDjDymTzUo5Yfy963bQsUDIrTeux79Jw0XL7c7XyFdzAW5FY13aNYQdgD2gU";
	// MCQ
	String client_id = "867744963854-3jf9uuh4f5ku6l4bvs8a1r8rorqq6l80.apps.googleusercontent.com";
	String client_secret="IVuyaP_oeyCoXIqJTMsDux1_";
	String refresh_token="1//0gKEywaG306e9CgYIARAAGBASNwF-L9Irjd-b5n_Krjo5Bv3VgZprDjYm0ESvW6MozQqSxaRvGZGarOTkPuVhR9ExSZmY35w7l9U";
	// MCP
//	String client_id = "";
//	String client_secret="";
//	String refresh_token="";

	/* Energy Isolation */
	String EI_FORM = "Energy Isolation";
	String ACTIVITY_LOG = "Activity Log";
	// String EI_ATTACHMENT_FOLDER_NAME = "EI Attachments";
	String EI_ATTACHMENT_FOLDER_NAME = "SCPApps/Iop/EnergyIsolation";
	String EI_ACTIVITY_ALERT = "Energy Isolation Alert!";

	// added for ITA
	// Destination
	String ITARulesDestination = "bpmrulesruntime_ITA";

	// Type
	String ITATaskType = "Task";
	String ITADOPType = "DOP";
	String ITAWaterOilCarryOver = "CarryOver";
	String ITAGasBlowBy = "GasBlowBy";
	String ssvCanaryTag = "STSSV";

	// Rule service ID:
	String ITADOPRuleServiceId = "2f8e8edb91eb451ca5b91b95325e1646";
	String ITATaskRuleServiceId = "38c2ae2409634a92b0c2a53cb9f034d9";
	String ITAWaterOilRulesServiceId = "4c47a47f75c248ed9ad1eeeb186405a5"; 
	String ITAGasBlowByRulesServiceId="8d149b6d781d4a92afb569948408af07";
	
	String DRAFT_STATUS = "DRAFT";
	String REJECTED = "REJECTED";

	// ATS Flag
	String ATSRunningFlag = "ATS_RUNNING_FLAG";

	// AR constants

	String INCALL = "IN CALL";
	String OUTCALL = "OUT CALL";
	String AR_NOTIFICATION_TYPE = "ARVIDEO_CALLING";
	String PUBLISHER = "PUBLISHER";
	String SUBSCRIBER = "SUBSCRIBER";
	String BUSY = "BUSY";
	String CONNECTED = "CONNECTED";
	String AR_VIDEO_CALL_ENDED = "AR_VIDEO_CALL_ENDED";
	String DECLINED = "DECLINED";
	String ENDED = "ENDED";

	// Operator Availability
	String Catarina = "Catarina";
	String Karnes = "Karnes";
	String Tilden = "Tilden";
	String PRO = "PRO";
	
	//EnergyIsolation JasperReports 
	String ENERGY_ISOLATION_TEMPLATE="Energy_isolation.jrxml";
	String MURPHY_LOGO="Murphy_Logo.png";
	String CHECKBOXTICK="Tick.png";
	String ENERGYISOLATION_SUBJECT="Energy Isolation (LOTO) form ";
	
	
	//Canary Constants
	 String FRAC_REPORT_ID="FH01";
	 String PROVE_REPORT_ID="PR01";
	 String DOP_REPORT_ID="DP01";
	 String DGP_REPORT_ID="DG01";
	 String ALARM_REPORT_ID="AL01";
	 String KAYBOB_GASDAILY_TAG="SEP_PRDTOTTDY";
	 String KAYBOB_INJECTIONDAILY_TAG="SEP_INJTOTTDY";
	 String DATEFORMAT_FOR_CANARY_FULL = "yyyy-MM-dd'T'HH:mm:ss'.0000000-06:00'";
	 String CANARY_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	 String CANARY_PLOTLY_FOLDERNAME="MUWI_Prod.";
	 String EFS_OIL_CANARY_TAG="QTOILD";
	 String EFS_GAS_CANARY_TAG="QTGASD";
	 String KAYBOB_OIL_CANARY_TAG="SEP_CNDTOTTDY";
	 String KAYBOB_GAS_CANARY_TAG="SEP_PRDTOTTDY";
	 String TUPPER_OIL_CANARY_TAG="";
	 String TUPPER_WEST_OIL_CANARY_TAG="";
	 String TUPPER_GAS_CANARY_TAG="AFLOW";
	 String TUPPER_WEST_GAS_CANARY_TAG="PG_FLOWD";
	 String START_TIME = "startTime";
	 String END_TIME = "endTime";
	 
	 //Shift Register
	 int SHIFT_AUDIT_PGE_SIZE=50;
	 String PRO_CATARINA="PRO_CATARINA";
	 String PRO_TILDEN="PRO_TILDEN";
	 String PRO_KARNES="PRO_KARNES";
	 String PRO_KAYBOB="PRO_Kaybob";
	 String PRO_MONTNEY="PRO_Montney";
	 String OBX_CATARINA="OBX_CATARINA";
	 String OBX_TILDEN="OBX_TILDEN";
	 String OBX_KARNES="OBX_KARNES";
	 String CATARINA_CODE="MUR-US-EFS-CT00";
	 String KARNES_CODE="MUR-US-EFS-KS00,MUR-US-EFS-KN00";
	 String TILDEN_CODE="MUR-US-EFS-TW00,MUR-US-EFS-TC00,MUR-US-EFS-TE00,MUR-US-EFS-TN00";
	 String ALS_EAST_LOC="MUR-US-EFS-KN00,MUR-US-EFS-KS00,MUR-US-EFS-TC00,MUR-US-EFS-TE00";
	 String ALS_WEST_LOC="MUR-US-EFS-CT00,MUR-US-EFS-TW00,MUR-US-EFS-TN00";
	 String KAYBOB_CODE="MUR-CA-KAY";
	 String MONTNEY_CODE="MUR-CA-MTM";
	 String SHIFT_REGISTER_ROLES="OBX_CATARINA,OBX_KARNES,OBX_TILDEN,PRO_CATARINA,PRO_TILDEN,PRO_KARNES,PRO_KAYBOB,PRO_MONTNEY";//check with george for EFS and Canada
	 String MORNING_SHIFT="Morning";
	 String NIGHT_SHIFT="Night";
	 String TODAY_SHIFT="Today";
	 String TOMORROW="Tomorrow";
	 
	 
	 //CountryCode Constants
	String EFS_CODE = "EFS";
	String CA_CODE = "CA";
	String EFS_BASE_MUWI = "926";
	String CANADA_BASE_MUWI = "CAN";
	String EFS_BASE_LOC_CODE = "MUR-US-EFS";
	String KAY_BASE_LOC_CODE = "MUR-CA-KAY";
	String MTW_BASE_LOC_CODE = "MUR-CA-MTW";
	String TWP_LOC_CODE = "MUR-CA-TWP";
	String MTM_BASE_LOC_CODE = "MUR-CA-MTM";
	String CANADA_BASE_LOC_CODE = "MUR-CA";
	String TUPPER_WEST_LOC = "Tupper West";
	String TUPPER_MAIN_LOC = "Tupper Main";
	String KAYBOB_LOC = "Kaybob";
	 

	 
	 
	 //Date Maintainence
	 String LOCATION_COORDINATE = "LocationCoordinate";
	 String WELL_TIER = "WellTier";
	 
	//Central Facility
	String CENTRAL_FACILITY_CATARINA = "MUR-US-EFS-CT00-KWCF";
	String CENTRAL_FACILITY_KARNES = "MUR-US-EFS-KS00-DSCF";
	String CENTRAL_FACILITY_TILDEN = "MUR-US-EFS-TC00-JWCF";
	
	String DRIVE_TIME="DriveTime";
	
	//UMA Destination
	String UMA_API_DEST = "UMA_API_DEST";
	String UMA_EMAIL_API = "/rest/getEmailIdByEmpName?empName=";
	
	String ALS_WEST = "ALS_West";
	String ALS_EAST = "ALS_East";
	String ALS_MONTNEY = "ALS_Montney";
	String ALS_KAYBOB = "ALS_Kaybob";
	
	
	//Oil and Gas constants for dop / dgp
	Double OIL_FACTOR =6.29287;
	Double GAS_FACTOR=35.49373;
}
