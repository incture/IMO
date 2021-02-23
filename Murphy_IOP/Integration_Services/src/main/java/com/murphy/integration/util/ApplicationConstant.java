package com.murphy.integration.util;

public interface ApplicationConstant {

	String PROVE_DB_USERNAME = "SVC_IOP";
	String PROVE_DB_PASSWORD = "D3nCr0_10p";
	String PROVE_DB_CONNECTION_URL = "jdbc:sqlserver://10.170.3.228:1433;databaseName=IOP_READ";
	String ALS_DB_USERNAME = "SVC_IOP";
	String ALS_DB_PASSWORD = "IOP@sql01P"; //"W@y2g04it";
	String ALS_DB_CONNECTION_URL = "jdbc:sqlserver://10.103.110.10:1433;databaseName=ALS_DB";
			//"jdbc:sqlserver://10.100.6.137:1433;databaseName=ALS_DB";



	// MCQ - Start
	  String CLOUD_SUBACCOUNT_NAME = "b0ot37y8l6";
	 String HANA_DB_CONNECTION_URL =
	 "jdbc:sap://vadbi4t.od.sap.biz:30015/";
	 String HANA_DB_USERNAME = "IMO";
	 String HANA_DB_PASSWORD = "Incturedb@#2021";
	 String SOCKS_NONPROXY_HOST = "vadbi4t.od.sap.biz";
	 String PROCOUNT_DB_USERNAME = "";
	 String PROCOUNT_DB_PASSWORD = "";
	 String PROCOUNT_DB_CONNECTION_URL = "";
	 String PRODVIEW_DB_USERNAME = "";
	 String PRODVIEW_DB_PASSWORD = "";
	 String PRODVIEW_DB_CONNECTION_URL ="";
	 String DORA_DB_USR_NAME = "";
	 String DORA_DB_PASSWORD = "";
	 String DORA_DB_CONNECTION_URL ="";
	 String DOCUMENTSERVICES =
	 "";
	 String CPI_SERVICE_URL =
	 "";
	 String CPI_USERNAME = "";
	 String CPI_PASSWORD = "";
	 String DOCUMENTSERVICE_SCHEMA = "";
	 String INQUIRY_APP_LINK =
	 "";
		String WELLVIEW_DB_USERNAME = "";
		String WELLVIEW_DB_PASSWORD="";
		String WELLVIEW_DB_CONNECTION_URL="";
	// MCQ - End



	String DATE_DISPLAY_FORMAT_NOTIME = "dd-MMM-yy";
	String ALS_DATE_FORMAT = "yyyy-MM-dd";
	String WELL_VIEW_DATE_FORMAT ="MMM dd yyyyHH:mma";

	
	String CLOUD_HOUSTON_LOCATION ="HOUST" ;//"HOUSTNONPROD";

	String CLOUD_HOUST_LOCATION = "HOUST"; //"HOUSTNONPROD";

	String FIELD = "FIELD";
	String FACILITY = "FACILITY";
	String WELLPAD = "WELL PAD";
	String WELL = "WELL";

	String TM_ORIGIN = "Investigation";
	String TM_STATUS = "COMPLETED";

	/* Changes based on Production / Dev */
	String SOCKS_PORT = "20004";
	String SOCKS_PORT_NAME = "socksProxyPort";
	String SOCKS_HOST = "localhost";
	String SOCK_HOST_NAME = "socksProxyHost";

	String IQ_ORIGIN = "Inquiry";

	String ITARuleService = "RulesService";
	
	String UOM_BOE="BOE";
	String UOM_E3M3="E3m3";

}
