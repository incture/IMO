package com.murphy.integration.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnections {
	// private static Connection procountConnection;
	// private static Connection hanaConnection;
	// private static Connection alsConnection;
	// private static Connection proveConnection;

	private static final Logger logger = LoggerFactory.getLogger(DBConnections.class);

	private static final String PROCOUNT_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public synchronized static Connection createConnectionForProcount() {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			Class.forName(PROCOUNT_DRIVER_CLASS);

			return DriverManager.getConnection(ApplicationConstant.PROCOUNT_DB_CONNECTION_URL,
					ApplicationConstant.PROCOUNT_DB_USERNAME, ApplicationConstant.PROCOUNT_DB_PASSWORD);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForProcount] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForProcount] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForProcount] : Error- Exception while creating connection " + e); 
		}
		return null;
	}

	private static final String HANA_DRIVER_CLASS = "com.sap.db.jdbc.Driver";

	public synchronized static Connection createConnectionForHana() {
		try {
//			ServicesUtil.setupSOCKS(null);
			Class.forName(HANA_DRIVER_CLASS);

			return DriverManager.getConnection(ApplicationConstant.HANA_DB_CONNECTION_URL,
					ApplicationConstant.HANA_DB_USERNAME, ApplicationConstant.HANA_DB_PASSWORD);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForHana] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForHana] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForHana] : Error- Exception while creating connection " + e); 
		}
		return null;
	}

	private static final String PROVE_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public synchronized static Connection createConnectionForProve() {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			Class.forName(PROVE_DRIVER_CLASS);

			return DriverManager.getConnection(ApplicationConstant.PROVE_DB_CONNECTION_URL,
					ApplicationConstant.PROVE_DB_USERNAME, ApplicationConstant.PROVE_DB_PASSWORD);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForProve] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForProve] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForProve] : Error- Exception while creating connection " + e); 
		}
		return null;
	}

	private static final String ALS_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public synchronized static Connection createConnectionForAls() {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			Class.forName(ALS_DRIVER_CLASS);

			return DriverManager.getConnection(ApplicationConstant.ALS_DB_CONNECTION_URL,
					ApplicationConstant.ALS_DB_USERNAME, ApplicationConstant.ALS_DB_PASSWORD);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForAls] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForAls] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForAls] : Error- Exception while creating connection " + e); 
		}
		return null;
	}

	
	
	private static final String PRODVIEW_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public synchronized static Connection createConnectionForProdView() {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			Class.forName(PRODVIEW_DRIVER_CLASS);

			return DriverManager.getConnection(ApplicationConstant.PRODVIEW_DB_CONNECTION_URL,
					ApplicationConstant.PRODVIEW_DB_USERNAME, ApplicationConstant.PRODVIEW_DB_PASSWORD);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForProdView] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForProdView] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForProdView] : Error- Exception while creating connection " + e); 
		}
		return null;
	}
	
	private static final String DORA_DB_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public synchronized static Connection createConnectionForDORADB(String dataBaseName) {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			Class.forName(DORA_DB_DRIVER_CLASS);

			return DriverManager.getConnection(null,
					null, null);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForDORADB] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForDORADB] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForDORADB] : Error- Exception while creating connection " + e); 
		}
		return null;
	}
	
	private static final String DORA_DB2 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public synchronized static Connection createConnectionForDORADB2() {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			Class.forName(DORA_DB2);

			return DriverManager.getConnection("jdbc:sqlserver://10.103.110.133:53653;databaseName=CAL_PV30",
					"hadoop_ro","a_EN9jk-");

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForDORADB] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForDORADB] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForDORADB] : Error- Exception while creating connection " + e); 
		}
		return null;
	}
	
	
	
	private static final String WELLVIEW_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public synchronized static Connection testOnprimeDbonnection(String url, String userName, String password)
			throws ClassNotFoundException, SQLException {
		ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
		Class.forName(WELLVIEW_DRIVER_CLASS);

		logger.error("testOnprimeCDbonnection db url : " + url);

		logger.error("testOnprimeCDbonnection id : " + userName);
		logger.error("testOnprimeCDbonnection : " + password);

		return DriverManager.getConnection(url, userName, password);

	}
	
	public synchronized static Connection createConnectionForWellView() {
		try {
			ServicesUtil.setupSOCKS(ApplicationConstant.CLOUD_HOUST_LOCATION);
			//ServicesUtil.setupSOCKS("HOUST");
			Class.forName(WELLVIEW_DRIVER_CLASS);

			//logger.error("wellview db url : " +ApplicationConstant.WELLVIEW_DB_CONNECTION_URL);
			
			//logger.error("user id : " +ApplicationConstant.WELLVIEW_DB_USERNAME);
			//logger.error("pwd : " +ApplicationConstant.WELLVIEW_DB_PASSWORD);
			
			return DriverManager.getConnection(ApplicationConstant.WELLVIEW_DB_CONNECTION_URL,
					ApplicationConstant.WELLVIEW_DB_USERNAME, ApplicationConstant.WELLVIEW_DB_PASSWORD);

		} catch (ClassNotFoundException e) {
			logger.error("[createConnectionForWellView] : Error- Class Not Found Exception while creating connection " + e); 
		} catch (SQLException e) {
			logger.error("[createConnectionForWellView] : Error- SQL Exception while creating connection " + e); 
		} catch (Exception e) {
			logger.error("[createConnectionForWellView] : Error- Exception while creating connection " + e); 
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	// public static Connection getProcountConnection() {
	// return procountConnection;
	// }
	//
	// public static Connection getHanaConnection() {
	// return hanaConnection;
	// }
	//
	// public static Connection getAlsConnection() {
	// return alsConnection;
	// }
	//
	// public static Connection getProveConnection() {
	// return proveConnection;
	// }
	
	
}
